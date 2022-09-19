package decryptionManager.components;

import decryptionManager.DecryptionManager;
import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import enigmaEngine.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static decryptionManager.DecryptionManager.*;
import static decryptionManager.components.AgentsThreadPool.totalTimeTasks;


public class DecryptedTask implements Runnable {


    private final CodeFormatDTO initialCode;

    private final Engine copyEngine;
    private final double taskSize;
    private Dictionary dictionary;
    private List<CandidateDTO> possibleCandidates;
    private final String cipheredString;
    private CodeCalculatorFactory codeCalculatorFactory;
    BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    public DecryptedTask(CodeFormatDTO initialCode, String cipheredString,CodeCalculatorFactory codeCalculatorFactory,
                         Engine copyEngine, double taskSize, BlockingQueue<TaskFinishDataDTO> successfulDecryption,
                         Dictionary dictionary) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
		this.cipheredString=cipheredString;
        this.dictionary=dictionary;
        this.successfulDecryption=successfulDecryption;
        this.codeCalculatorFactory=codeCalculatorFactory;
        possibleCandidates=new ArrayList<>();

        codeCalculatorFactory=new CodeCalculatorFactory(copyEngine.getMachineData().getAlphabetString(),copyEngine.getMachineData().getNumberOfRotorsInUse());

    }

    @Override
    public void run() {

        long startTime=System.nanoTime();
        CodeFormatDTO currentCode=initialCode;

        for (double i = 0; i < taskSize && currentCode!=null ; i++) {
            isPauseRunningTask();
      //     System.out.println(Thread.currentThread().getName() + " is running!");
            copyEngine.setCodeManually(currentCode);
            String processedOutput = null;
            try {
                processedOutput = copyEngine.processDataInput(cipheredString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(dictionary.checkIfAllLetterInDic(processedOutput))
                    {
                        possibleCandidates.add(new CandidateDTO(copyEngine.getCodeFormat(true),processedOutput));
//                        System.out.println(currentCode);
//
//                        System.out.println("Output: "+ processedOutput+"\n********************************************" );

                    }
                    currentCode= codeCalculatorFactory.getNextCode(currentCode);
                }
        long totalTime=System.nanoTime()-startTime;
        try {
            if(possibleCandidates.size()>0)
                 successfulDecryption.put(new TaskFinishDataDTO(possibleCandidates,Thread.currentThread().getName(),totalTime));
        totalTimeTasks.set(totalTimeTasks.get()+totalTime);

       // Thread.sleep(DecryptionManager.UI_SLEEP_TIME);//to
            currentTaskTimeConsumer.accept(totalTime);
    } catch (InterruptedException ignored) {

       // throw new RuntimeException(e);
        }


    }

        private void isPauseRunningTask() {
            if (isSystemPause) {
                synchronized (pauseLock) {
                    if (isSystemPause) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " is pause!");
                            pauseLock.wait();
                             System.out.println(Thread.currentThread().getName() + " is resume!");
                        } catch (InterruptedException ignored) {
                           // throw new RuntimeException(e);
                        }

                    }
                }

            }

        }

}