package UI.application.DmTab;

import UI.application.DmTab.DMTaskComponents.CandidatesStatus.CandidatesStatusController;
import decryptionManager.DecryptionManager;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIUpdater  implements Consumer<String> {

    private AtomicBoolean stillHaveCandidate;
    private Thread candidateListener;
    private final DecryptionManager decryptionManager;
    private final CandidatesStatusController candidatesStatusController;
    private final ProgressDataDTO progressDataDTO;
    private final SimpleStringProperty messageProperty;
    private final SimpleDoubleProperty progressProperty;
    SimpleLongProperty counterProperty;
    private Long totalTaskAmount;
    public UIUpdater(DecryptionManager decryptionManager, ProgressDataDTO progressDataDTO, CandidatesStatusController candidatesStatusController) {
        this.decryptionManager = decryptionManager;
        this.candidatesStatusController = candidatesStatusController;


        this.progressDataDTO = progressDataDTO;
        messageProperty=new SimpleStringProperty();
        counterProperty=new SimpleLongProperty();
        progressProperty=new SimpleDoubleProperty(0);
        bindUpdaterToUIComponents();
    }
//
//    public void addNewCandidates(TaskFinishDataDTO histogramData) {
//
//                 introduceNewCandidates.accept(histogramData);
//                 updateDistinct.run();
//
//
//    }
    private void bindUpdaterToUIComponents() {
        // task message
        progressDataDTO.taskMessageProperty().bind(this.messageProperty);

        // task progress bar
        progressDataDTO.progressBarProperty().bind(this.progressProperty);

        // task percent label
        progressDataDTO.progressPercentProperty().bind( Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                this.progressProperty,
                                100)),
                " %"));

      //  decryptionManager.addListenerTotalTaskDoneCounter((newValue) -> counterProperty.set((Long)newValue/totalTaskAmount));




        // task cleanup upon finish


    }



    public void startCandidateListener() {
        totalTaskAmount= decryptionManager.getTotalTimeTasks();
        candidateListener=new Thread(this::updateNewCandidate,"Candidate Updater");
        candidateListener.start();

    }

    public void pause(){
        candidateListener.notify();
    }

    private void updateNewCandidate()
    {
        Supplier<TaskFinishDataDTO> supplier = decryptionManager.getFinishQueueSupplier();
        while(stillHaveCandidate.get())
        {
            candidatesStatusController.addAllCandidate(supplier.get());
        }
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }
    @Override
    public void accept(String message) {
        messageProperty.set(message);
    }

//    public void updateExistingWord(CandidateDTO histogramData) {
//        Platform.runLater(
//                () -> updateExistingWord.accept(histogramData)
//        );
//    }
//
//    public void updateTotalProcessedWords(int delta) {
//        Platform.runLater(
//                () -> updateTotalProcessedWords.accept(delta)
//        );
//    }

}
