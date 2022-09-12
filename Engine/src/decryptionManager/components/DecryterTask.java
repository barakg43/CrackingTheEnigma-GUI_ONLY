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
    public DecryterTask(CodeFormatDTO initialCode,,String chiperedString
	Engine copyEngine, double taskSize, BlockingQueue<TaskFinishDataDTO> successfulDecryption,
	Dictionary dictionary) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
		this.chiperedString=chiperedString;
        this.dictionary=dictionary;
        this.successfulDecryption=successfulDecryption;
	}

    @Override
    public void run() {
        long startTime=System.nanoTime();
        CodeFormatDTO currentCode=initialCode;

        for (double i = 0; i < taskSize && currentCode!=null ; i++) {
            String processedOutput = copyEngine.processDataInput(chiperedString);
            if(dictionary.checkIfAllLetterInDic(processedOutput))
            {
                System.out.println(currentCode);
                System.out.println("********************************************\nOutput " + processedOutput);
				    try {    //TODO:enter to queue
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