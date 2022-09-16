package decryptionManager.components;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static decryptionManager.DecryptionManager.doneBruteForceTasks;

public class AgentsThreadPool extends ThreadPoolExecutor {
    public static AtomicLong taskNumber;
    AtomicCounter totalDoneCounter;
    public static AtomicLong totalTimeTasks;
    private double totalTaskAmount;
    public AgentsThreadPool(int corePoolSize, int maximumPoolSize,
                            long keepAliveTime, TimeUnit unit,
                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,AtomicCounter totalDoneCounter) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
        taskNumber=new AtomicLong(0);
        this.totalDoneCounter=totalDoneCounter;
        totalTimeTasks=new AtomicLong(0);
    }

    public long getTotalTimeTasks() {
        return totalTimeTasks.get();
    }
    public void setTotalTaskAmount(double taskAmount)
    {
        totalTaskAmount=taskAmount;
    }
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
   //     System.out.println("Perform beforeExecute() logic");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            throw new RuntimeException(t);
        }
        totalDoneCounter.increment();
        if(totalDoneCounter.getValue()==totalTaskAmount)
            doneBruteForceTasks();
       // System.out.println("Perform afterExecute() logic");
    }
}
