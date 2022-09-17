package UI.application.DmTab;

import UI.application.DmTab.DMTaskComponents.CandidatesStatus.CandidatesStatusController;
import decryptionManager.DecryptionManager;
import decryptionManager.components.AtomicCounter;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIUpdater {

    private static Thread candidateListener=null;
    private final DecryptionManager decryptionManager;
    private final CandidatesStatusController candidatesStatusController;
    private final ProgressDataDTO progressDataDTO;
    private final SimpleStringProperty messageProperty;
    private final SimpleDoubleProperty progressProperty;
    private  SimpleLongProperty counterProperty;
    private double totalTaskAmount= 0L;
    private final Consumer<String> messageManager;
    private final Consumer<Long> singleTaskTime;
    private final SimpleBooleanProperty isDoneBruteForce;
    private final SimpleBooleanProperty isDM_DoneAllTasks;
    private final SimpleBooleanProperty isCandidateUpdaterDone;
    private final AtomicCounter tasksDoneCounter;

    private Long totalTimeDuration;

    private final Object candidateThreadPauseLock=new Object();
    private Boolean isCandidatePause;
    public UIUpdater(DecryptionManager decryptionManager, ProgressDataDTO progressDataDTO, CandidatesStatusController candidatesStatusController) {
        this.decryptionManager = decryptionManager;
        this.candidatesStatusController = candidatesStatusController;

        this.progressDataDTO = progressDataDTO;
        messageProperty=new SimpleStringProperty();
        counterProperty=new SimpleLongProperty();
        progressProperty=new SimpleDoubleProperty(0);
        tasksDoneCounter=new AtomicCounter();
         isDoneBruteForce=new SimpleBooleanProperty();
         isDM_DoneAllTasks=new SimpleBooleanProperty();
         isCandidateUpdaterDone=new SimpleBooleanProperty();
        decryptionManager.setCandidateListenerStarter(this::startCandidateListenerTread);
       // taskDoneCounter.addPropertyChangeListener(newValue->counterProperty.set((Long)newValue));
        bindUpdaterToUIComponents();

        messageManager = this::updateMassage;
        singleTaskTime=this::getSingleTaskTime;

    }
//
//    public void addNewCandidates(TaskFinishDataDTO histogramData) {
//
//                 introduceNewCandidates.accept(histogramData);
//                 updateDistinct.run();
//
//
//    }
        public SimpleBooleanProperty getIsDoneBruteForceProperty()
        {
            return isDoneBruteForce;
        }
//    public ProgressDataDTO getProgressDataDTO(){
//        return progressDataDTO;
//    }

    private void startCandidateListenerTread()
    {
        candidateListener.start();
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

        isDoneBruteForce.addListener(((observable, oldValue, newValue) -> {
            if(newValue)
            {

                updateMassage("Done brute force and update all candidates!");

            }
        }));
        tasksDoneCounter.addPropertyChangeListener((counter) -> {

            Platform.runLater(()->counterProperty.set((Long) counter.getNewValue()));
            if(totalTaskAmount ==(Long) counter.getNewValue()) {
                isDM_DoneAllTasks.set(true);
                if(candidateListener.isAlive())
                     candidateListener.interrupt();
            }
        });

        decryptionManager.setTaskDoneAmount(tasksDoneCounter);
        resetAllUIData();


    }
    public static boolean isCandidateLinterAlive()
    {
        return candidateListener!=null&&candidateListener.isAlive();
    }
    private void resetAllUIData()
    {

        totalTimeDuration=0L;
        counterProperty.set(0);
        updateMassage("Waiting for start Brute Force!");
        isDM_DoneAllTasks.set(false);
        isCandidateUpdaterDone.set(false);
        isCandidatePause=Boolean.FALSE;
        progressDataDTO.getAverageTaskTimeProperty().set(String.valueOf(0));
        progressDataDTO.totalTimeTaskAmountProperty().set(String.valueOf(0));
        tasksDoneCounter.resetCounter();
        candidatesStatusController.clearAllTiles();
    }


    public void setupCandidateListener() {

        totalTaskAmount= decryptionManager.getTotalTasksAmount();
        progressDataDTO.totalNumberOfTasksProperty().set(String.valueOf(totalTaskAmount));
        progressProperty.bind(Bindings.divide(counterProperty,totalTaskAmount));
        decryptionManager.setDataConsumer(messageManager,singleTaskTime);
        isDoneBruteForce.bind(Bindings.and(isDM_DoneAllTasks,isCandidateUpdaterDone));
        isDoneBruteForce.bind(Bindings.and(isDM_DoneAllTasks,isCandidateUpdaterDone));
        candidateListener=new Thread(this::updateNewCandidate,"Candidate Updater");
        resetAllUIData();

    }

    public void pauseCandidateListener(){
        isCandidatePause=true;
    }
    public void resumeCandidateListener(){
        isCandidatePause=false;
        synchronized (candidateThreadPauseLock){
        candidateThreadPauseLock.notifyAll();
        }
    }
    public void stopCandidateListener()
    {
        if(candidateListener!=null)
            candidateListener.interrupt();
    }
//    private void getStartTime(Long startTime)
//    {
//        this.startTime=startTime;
//        isDM_DoneAllTasks.set(true);
//
//    }



    private void updateNewCandidate()
    {
        Supplier<TaskFinishDataDTO> supplier = decryptionManager.getFinishQueueSupplier();
        TaskFinishDataDTO currentData;
        do{

            if(isCandidatePause) {
                try {
                    synchronized (candidateThreadPauseLock){
                        if(isCandidatePause)
                        {
                            System.out.println("Candidate Thread is paused");
                            candidateThreadPauseLock.wait();
                        }
                        System.out.println("Candidate Thread resuming...");
                    }
                } catch (InterruptedException ignored) {
                }
            }

            currentData = supplier.get();
            if(currentData!=null)
                candidatesStatusController.addAllCandidate(currentData);
            else
                System.out.println("Finish Update all candidate!");

        } while(currentData!=null);
        isCandidateUpdaterDone.set(true);
    }
    private String convertNanoTimeToTimerDisplay(long nanoTime)
    {
       return String.format("%02d:%02d:%02d.%04d",
                Duration.ofNanos(nanoTime).toHours()%24,
                Duration.ofNanos(nanoTime).toMinutes()%60,
                Duration.ofNanos(nanoTime).getSeconds()%60,
                Duration.ofNanos(nanoTime).toMillis()%10000
        );
    }
    public void getSingleTaskTime(long time) {
       // currentTasksTimeAmount += time;
        totalTimeDuration+=time;

        if (tasksDoneCounter.getValue() > 0) {
            Platform.runLater(() -> {
            progressDataDTO.getAverageTaskTimeProperty()
                    .set(totalTimeDuration / tasksDoneCounter.getValue()+" nano");
            progressDataDTO.totalTimeTaskAmountProperty().
                        set(convertNanoTimeToTimerDisplay(totalTimeDuration));
            });
        }
    }
 public void updateMassage(String massage)
 {
  Platform.runLater(()-> messageProperty.set(massage));
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
