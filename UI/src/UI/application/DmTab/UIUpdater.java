package UI.application.DmTab;

import UI.application.DmTab.DMTaskComponents.CandidatesStatus.CandidatesStatusController;
import decryptionManager.DecryptionManager;
import decryptionManager.components.AtomicCounter;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIUpdater  {

    private AtomicBoolean stillHaveCandidate;
    private Thread candidateListener;
    private final DecryptionManager decryptionManager;
    private final CandidatesStatusController candidatesStatusController;
    private final ProgressDataDTO progressDataDTO;
    private final SimpleStringProperty messageProperty;
    private final SimpleDoubleProperty progressProperty;
    private  SimpleLongProperty counterProperty;
    private double totalTaskAmount= 0L;
    private final AtomicCounter tasksDoneCounter;
    public UIUpdater(DecryptionManager decryptionManager, ProgressDataDTO progressDataDTO, CandidatesStatusController candidatesStatusController) {
        this.decryptionManager = decryptionManager;
        this.candidatesStatusController = candidatesStatusController;

        stillHaveCandidate=new AtomicBoolean();
        this.progressDataDTO = progressDataDTO;
        messageProperty=new SimpleStringProperty();
        counterProperty=new SimpleLongProperty();
        progressProperty=new SimpleDoubleProperty(0);
        tasksDoneCounter=new AtomicCounter();
       // taskDoneCounter.addPropertyChangeListener(newValue->counterProperty.set((Long)newValue));
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

    public ProgressDataDTO getProgressDataDTO(){
        return progressDataDTO;
    }

    private void bindUpdaterToUIComponents() {
        // task message
        progressDataDTO.taskMessageProperty().bind(this.messageProperty);
        // task progress bar
        progressDataDTO.progressBarProperty().bind(this.progressProperty);
         progressDataDTO.totalAmountTaskDoneProperty().bind(counterProperty.asString());
        // task percent label
        progressDataDTO.progressPercentProperty().bind( Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                this.progressProperty,
                                100)),
                " %"));


        tasksDoneCounter.addPropertyChangeListener((newValue) -> {
            Platform.runLater(()->counterProperty.set((Long) newValue.getNewValue()));
            //System.out.println("counter:"+newValue.getNewValue());
        });

        decryptionManager.setTaskDoneAmount(tasksDoneCounter);
       // taskDoneCounter.addPropertyChangeListener(newValue -> counterProperty.set((Long) newValue.getNewValue()));



        // task cleanup upon finish


    }



    public void startCandidateListener() {


        totalTaskAmount= decryptionManager.getTotalTasksAmount();
        progressDataDTO.totalNumberOfTasksProperty().set(String.valueOf(totalTaskAmount));
        progressProperty.bind(Bindings.divide(counterProperty,totalTaskAmount));
        stillHaveCandidate.set(true);
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

 public void updateMassage(String massage)
 {
     messageProperty.set(massage);
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
