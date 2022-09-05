package decryptionManager;

import decryptionManager.components.DecryterTask;
import decryptionManager.components.Dictionary;
import enigmaEngine.Engine;

import java.util.List;
import java.util.concurrent.BlockingDeque;

public class DecryptionManager {

    private final int numberOfAgents;
    private Dictionary dictionary;
    private Engine engine;
    private int taskSize;


    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }


    private BlockingDeque<DecryterTask> taskQueue;

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public DecryptionManager(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
        dictionary=new Dictionary();
    }

    public void getValidDictionaryWords(String words, List<Character> excludeCharsList, String alphabet)
    {
        dictionary.getValidDictionaryWords(words,excludeCharsList,alphabet);
    }



}
