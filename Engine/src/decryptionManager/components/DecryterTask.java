package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import dtoObjects.TaskFinishDataDTO;
import enigmaEngine.Engine;

import java.util.concurrent.BlockingQueue;


public class DecryterTask implements Runnable {


    private final CodeFormatDTO initialCode;

    private final Engine copyEngine;
    private final double taskSize;
    private Dictionary dictionary;
    private String cipheredString;
    private CodeCalculatorFactory codeCalculatorFactory;
    BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    public DecryterTask(CodeFormatDTO initialCode, Engine copyEngine, double taskSize, BlockingQueue<TaskFinishDataDTO> successfulDecryption) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
        this.successfulDecryption=successfulDecryption;
    }

    @Override
    public void run() {
        long startTime=System.nanoTime();
        CodeFormatDTO currentCode=initialCode;
        copyEngine.setCodeManually(initialCode);
        for (double i = 0; i < taskSize; i++) {
            String processedOutput = copyEngine.processDataInput(cipheredString);
            if(dictionary.checkIfAllLetterInDic(processedOutput)) {
                try {
                    successfulDecryption.put(new TaskFinishDataDTO(Thread.currentThread().getName(),
                                                copyEngine.getCodeFormat(false)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            currentCode= codeCalculatorFactory.getNextCode(currentCode);
        }

    }




}