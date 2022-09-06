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

    public DecryterTask(CodeFormatDTO initialCode, Engine copyEngine, double taskSize) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;

    }

    @Override
    public void run() {
        CodeFormatDTO currentCode=initialCode;
        for (double i = 0; i < taskSize; i++) {
            String processedOutput = copyEngine.processDataInput(chiperedString);
            if(dictionary.checkIfAllLetterInDic(processedOutput))
                //TODO:enter to queue
            currentCode= codeCalculatorFactory.getNextCode(currentCode);
        }

    }




}