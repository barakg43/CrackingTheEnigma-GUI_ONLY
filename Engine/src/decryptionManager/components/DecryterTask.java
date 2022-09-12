package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import enigmaEngine.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class DecryterTask implements Runnable {


    private final CodeFormatDTO initialCode;

    private final Engine copyEngine;
    private final double taskSize;
    private Dictionary dictionary;
    private List<CandidateDTO> possibleCandidates;
    private final String cipheredString;
    private CodeCalculatorFactory codeCalculatorFactory;
    BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    public DecryterTask(CodeFormatDTO initialCode,String cipheredString,
	Engine copyEngine, double taskSize, BlockingQueue<TaskFinishDataDTO> successfulDecryption,
	Dictionary dictionary) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
		this.cipheredString=cipheredString;
        this.dictionary=dictionary;
        this.successfulDecryption=successfulDecryption;
        possibleCandidates=new ArrayList<>();
	}

    @Override
    public void run() {
        long startTime=System.nanoTime();
        CodeFormatDTO currentCode=initialCode;

        for (double i = 0; i < taskSize && currentCode!=null ; i++) {
                String processedOutput = copyEngine.processDataInput(cipheredString);
                    if(dictionary.checkIfAllLetterInDic(processedOutput))
                    {

                        possibleCandidates.add(new CandidateDTO(copyEngine.getCodeFormat(false),processedOutput));
                        System.out.println(currentCode);
                        System.out.println("********************************************\nOutput " + processedOutput);



                    }
                    currentCode= codeCalculatorFactory.getNextCode(currentCode);
                }

                   try {
        successfulDecryption.put(new TaskFinishDataDTO(possibleCandidates,Thread.currentThread().getName(),
                System.nanoTime()-startTime));
    } catch (InterruptedException e) {
        throw new RuntimeException(e);}


    }




}