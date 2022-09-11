package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import enigmaEngine.Engine;

public class DecryterTask implements Runnable {


    private CodeFormatDTO initialCode;
   // private BlockingDeque<String>
    private Engine copyEngine;
    private double taskSize;
    private Dictionary dictionary;
    private String chiperedString;
    private CodeCalculatorFactory codeCalculatorFactory;

    public DecryterTask(CodeFormatDTO initialCode, Engine copyEngine, double taskSize,String chiperedString,Dictionary dictionary) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
        this.chiperedString=chiperedString;
        this.dictionary=dictionary;


        codeCalculatorFactory=new CodeCalculatorFactory(copyEngine.getMachineData().getAlphabetString(),copyEngine.getMachineData().getNumberOfRotorsInUse());

    }

    @Override
    public void run() {
        CodeFormatDTO currentCode=initialCode;
        for (double i = 0; i < taskSize && currentCode!=null ; i++) {
            String processedOutput = copyEngine.processDataInput(chiperedString);
            if(dictionary.checkIfAllLetterInDic(processedOutput))
            {
                System.out.println(currentCode);
                System.out.println("********************************************\nOutput " + processedOutput);

            }

                //TODO:enter to queue
            currentCode= codeCalculatorFactory.getNextCode(currentCode);
        }

    }




}