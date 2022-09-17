package UI.application.DmTab;

import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.concurrent.Task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
concerns:
 1. I had to give some sleep time for the task so it will give a chance for the UI thread to run - the task attempted to over utilize the CPU
    one option is to use Thread.Yield() - that will cause this thread to release CPU on it's own, but since it's only an hint to the scheduler it didn't really helped
    another option to avoid that was to reduce the times I am attempting to update the UI: instead of doing that
    per word, perhaps do it per line
 2. why not using an observable map in which the task thread will update its data (the histograms) and on which
    the UI will observe and build / update the tiles from ?
    since every update / add done to this observable map that is shared and binded to UI components that was made using the
    task thread (this thread) caused illegal state exception since it wasn't performed on the EDT thread.
    moving all the updates to the EDT made no sense since that is the most of the work of this task so why using it at all in separate thread
    if the most of it will happen on the EDT ??
    to solve and avoid the problem, I had to use a different data structure to which the UI is binded and a different one that is for internal use of this task
 3. all ui adapter actions runs within platform invoke later style since they attempt to change UI stuff
 */
public class CandidateUpdater extends Task<Boolean> {

    private final long SLEEP_TIME = 5;
    AtomicBoolean setRunning;
    private UIUpdater uiAdapter;
    private Consumer<Runnable> onCancel;
    Supplier<TaskFinishDataDTO> taskSupplier;
    public CandidateUpdater(UIUpdater uiAdapter, Consumer<Runnable> onCancel, Supplier<TaskFinishDataDTO> taskSupplier) {
       this.taskSupplier=taskSupplier;
        this.uiAdapter = uiAdapter;
        this.onCancel = onCancel;
        setRunning=new AtomicBoolean(true);

    }

    public AtomicBoolean getSetRunning() {
        return setRunning;
    }

    @Override
    protected Boolean call() throws Exception {
        updateMessage("Start fetch Tasks ...");
        //updateProgress(0, totalWords);

        while(setRunning.get())
        {

            TaskFinishDataDTO currentTaskData=taskSupplier.get();
            uiAdapter.pauseCandidateListener();

        }


       // updateMessage("Traversing words...");
//        final int[] wordCount = {1};
//        try {
//            reader
//                    .lines()
//                    .forEach(aLine -> {
//                        if (isCancelled()) {
//                            throw new TaskIsCanceledException();
//                        }
//                        String[] words = aLine.split(" ");
//                        Arrays.stream(words)
//                                .forEach(word -> {
//                                    updateProgress(wordCount[0]++, totalWords);
//                                    HistogramData histogramData = histograms.get(word);
//                                    if (histogramData == null) {
//                                        histogramData = new BasicHistogramData(word, 1);
//                                        histograms.put(word, histogramData);
//                                        uiAdapter.addNewWord(histogramData);
//                                    } else {
//                                        histogramData.increase();
//                                        uiAdapter.updateExistingWord(histogramData);
//                                    }
//
//                                    //Thread.yield(); // doesn't work so good...
//
//                                    HistogramsUtils.sleepForAWhile(SLEEP_TIME);
//                                });
//                        uiAdapter.updateTotalProcessedWords(words.length);
//                    });
//
//        } catch (TaskIsCanceledException e) {
//            HistogramsUtils.log("Task was canceled !");
//            onCancel.accept(null);
//        }
        updateMessage("Done...");
        return Boolean.TRUE;
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        updateMessage("Cancelled!");
    }
}
