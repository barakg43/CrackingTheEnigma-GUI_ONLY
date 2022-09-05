package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import enigmaEngine.Engine;

public class DecryterTask implements Runnable {


    private CodeFormatDTO initialCode;
   // private BlockingDeque<String>
    private Engine copyEngine;
    private int taskSize;
    private Dictionary dictionary;
    private String chiperedString;
    private CodeCalculatorFactory codeCalculatorFactory;

    public DecryterTask(CodeFormatDTO initialCode, Engine copyEngine, int taskSize) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
        codeCalculatorFactory =new CodeCalculatorFactory(copyEngine.getMachineData().getAlphabetString());
    }

    @Override
    public void run() {
        CodeFormatDTO currentCode=initialCode;
        for (int i = 0; i < taskSize; i++) {
            String processedOutput = copyEngine.processDataInput(chiperedString);
            if(dictionary.checkIfAllLetterInDic(processedOutput))
                //TODO:enter to queue
            currentCode= codeCalculatorFactory.getNextCode(currentCode);
        }

    }




}