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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIUpdater {

    private Thread candidateListener;
    private final DecryptionManager decryptionManager;
    private final CandidatesStatusController candidatesStatusController;
    private final ProgressDataDTO progressDataDTO;
    private final SimpleStringProperty messageProperty;
    private final SimpleDoubleProperty progressProperty;
    private  SimpleLongProperty counterProperty;
    private double totalTaskAmount= 0L;
    private final Consumer<String> messageManager;
    private final Consumer<Long> startTimeConsumer;
    private final Consumer<Long> singleTaskTime;
    private final SimpleBooleanProperty isDoneBruteForce;
    private final SimpleBooleanProperty isDM_DoneAllTasks;
    private final SimpleBooleanProperty isCandidateUpdaterDone;
    private final AtomicCounter tasksDoneCounter;
    private Long startTime;
    private long currentTasksTimeAmount;
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

       // taskDoneCounter.addPropertyChangeListener(newValue->counterProperty.set((Long)newValue));
        bindUpdaterToUIComponents();

        messageManager = this::updateMassage;
        singleTaskTime=this::getSingleTaskTime;
        startTimeConsumer=this::getStartTime;
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
                updateMassage("Done brute force!");
                updateTotalTime();
            }
        }));
        tasksDoneCounter.addPropertyChangeListener((counter) -> {
            Platform.runLater(()->counterProperty.set((Long) counter.getNewValue()));
        });

        decryptionManager.setTaskDoneAmount(tasksDoneCounter);

       // taskDoneCounter.addPropertyChangeListener(newValue -> counterProperty.set((Long) newValue.getNewValue()));



        // task cleanup upon finish


    }



    public void startCandidateListener() {


        totalTaskAmount= decryptionManager.getTotalTasksAmount();
        progressDataDTO.totalNumberOfTasksProperty().set(String.valueOf(totalTaskAmount));
        progressProperty.bind(Bindings.divide(counterProperty,totalTaskAmount));
        decryptionManager.setDataConsumer(messageManager,startTimeConsumer,singleTaskTime);
        currentTasksTimeAmount=0;
        isDM_DoneAllTasks.set(false);
        isCandidateUpdaterDone.set(false);
        isDoneBruteForce.bind(Bindings.and(isDM_DoneAllTasks,isCandidateUpdaterDone));

        counterProperty.set(0);
        tasksDoneCounter.resetCounter();
        candidatesStatusController.clearAllTiles();
        candidateListener=new Thread(this::updateNewCandidate,"Candidate Updater");
        candidateListener.start();

    }

    public void pause(){
        candidateListener.notify();
    }
    private void getStartTime(Long startTime)
    {
        this.startTime=startTime;
        isDM_DoneAllTasks.set(true);

    }

    private void updateTotalTime()
    {

        Platform.runLater(()->progressDataDTO.totalTimeTaskAmountProperty().
                set(String.valueOf(System.nanoTime()-startTime)));
    }

    private void updateNewCandidate()
    {
        Supplier<TaskFinishDataDTO> supplier = decryptionManager.getFinishQueueSupplier();
        TaskFinishDataDTO currentData;
        do{
            currentData=supplier.get();
            if(currentData!=null)
                candidatesStatusController.addAllCandidate(currentData);
            else
                System.out.println("Finish Taker!");

        } while(currentData!=null);
        isCandidateUpdaterDone.set(true);
    }
    public void getSingleTaskTime(long time)
    {
        currentTasksTimeAmount+=time;
        if(tasksDoneCounter.getValue()>0)
        {
            Platform.runLater(()->  progressDataDTO.getAverageTaskTimeProperty()
                    .set(String.valueOf(currentTasksTimeAmount/tasksDoneCounter.getValue())));}


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
