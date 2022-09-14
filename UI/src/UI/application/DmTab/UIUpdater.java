package UI.application.DmTab;

import UI.application.DmTab.DMTaskComponents.CandidatesStatus.CandidatesStatusController;
import decryptionManager.DecryptionManager;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class UIUpdater extends Task<Boolean> {

    private AtomicBoolean stillHaveCandidate;
    private Thread candidateListener;
    private DecryptionManager decryptionManager;
    private CandidatesStatusController candidatesStatusController;
    private ProgressDataDTO progressDataDTO;
    private long totalTaskAmount;
    public UIUpdater(DecryptionManager decryptionManager, ProgressDataDTO progressDataDTO, CandidatesStatusController candidatesStatusController) {
        this.decryptionManager = decryptionManager;
        this.candidatesStatusController = candidatesStatusController;


        this.progressDataDTO = progressDataDTO;
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
        progressDataDTO.taskMessagePropertyProperty().bind(this.messageProperty());


        // task progress bar
        progressDataDTO.progressBarPropertyProperty().bind(this.progressProperty());

        // task percent label
        progressDataDTO.progressPercentPropertyProperty().bind( Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                this.progressProperty(),
                                100)),
                " %"));

        decryptionManager.addListenerTotalTaskDoneCounter(e->{
            this.updateProgress((Long) e.getNewValue(),totalTaskAmount);
        });




        // task cleanup upon finish


    }
    @Override
    protected Boolean call() throws Exception {
        totalTaskAmount= decryptionManager.getTotalTimeTasks();
        candidateListener=new Thread(this::updateNewCandidate,"Candidate Updater");
        candidateListener.start();
        return true;
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
