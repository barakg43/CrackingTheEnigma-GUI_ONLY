package decryptionManager.components;

import dtoObjects.DmDTO.TaskFinishDataDTO;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class TaskFinishSupplier implements Supplier<TaskFinishDataDTO> {
    private final BlockingQueue<TaskFinishDataDTO> finishQueue;
    public TaskFinishSupplier(BlockingQueue<TaskFinishDataDTO> queue)
    {
        this.finishQueue=queue;;
    }

    @Override
    public TaskFinishDataDTO get() {
        try {
            return finishQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
