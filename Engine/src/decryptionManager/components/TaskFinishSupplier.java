package decryptionManager.components;

import dtoObjects.DmDTO.TaskFinishDataDTO;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class TaskFinishSupplier implements Supplier<TaskFinishDataDTO> {
    private final BlockingQueue<TaskFinishDataDTO> finishQueue;
    AtomicBoolean allTaskDone;
    public TaskFinishSupplier(BlockingQueue<TaskFinishDataDTO> queue, AtomicBoolean allTaskDone)
    {
        this.finishQueue=queue;
        this.allTaskDone=allTaskDone;
    }

    @Override
    public TaskFinishDataDTO get() {
        try {
            if(!finishQueue.isEmpty()&&!allTaskDone.get())
                return finishQueue.take();
            else
                return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
