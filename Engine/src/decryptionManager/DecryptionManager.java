package decryptionManager;

import decryptionManager.components.*;
import dtoObjects.CodeFormatDTO;
import dtoObjects.PlugboardPairDTO;
import dtoObjects.RotorInfoDTO;
import dtoObjects.TaskFinishDataDTO;
import enigmaEngine.Engine;
import sun.nio.cs.Surrogate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DecryptionManager {

    private final int numberOfAgents;
    private Dictionary dictionary;
    private Engine engine;
    private int taskSize;
    private List<int[]> allPossibleRotors;

    private CodeCalculatorFactory codeCalculatorFactory;
    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    BlockingQueue<Runnable> taskQueue;
    private ConcurrentLinkedQueue<TaskFinishDataDTO> successfulDecryrtion;
     private AgentsThreadPool agents;
    private byte[] engineCopyBytes;
    private final int QUEUE_SIZE=1000;
    public DecryptionManager(int numberOfAgents,Engine engine) {
        this.numberOfAgents = numberOfAgents;
        dictionary=new Dictionary();
        taskQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        successfulDecryrtion=new ConcurrentLinkedQueue<>();
        this.engine = engine;
        saveEngineCopy();
        codeCalculatorFactory =new CodeCalculatorFactory(engine.getMachineData().getAlphabetString(),
                engine.getMachineData().getNumberOfRotorsInUse());
        agents=new AgentsThreadPool(2,numberOfAgents,5, TimeUnit.SECONDS,taskQueue,new AgentThreadFactory());
        allPossibleRotors=new ArrayList<>();

    }
    private void saveEngineCopy()
    {

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(  bos);
            os.writeObject(engine);
            engineCopyBytes= bos.toByteArray();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private Engine createNewEngineCopy()  {
        ObjectInputStream oInputStream = null;
        Engine copyEngine=null;
        try {
            oInputStream = new ObjectInputStream(new ByteArrayInputStream(engineCopyBytes));
            copyEngine= (Engine) oInputStream.readObject();
            oInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return copyEngine;
    }

    public void getValidDictionaryWords(String words, List<Character> excludeCharsList, String alphabet)
    {
        dictionary.getValidDictionaryWords(words,excludeCharsList,alphabet);
    }

    public void createTaskEasyLevel(CodeFormatDTO codeFormatDTO)
    {
        CodeFormatDTO currentCode =codeFormatDTO;
        double numberOftask=codeCalculatorFactory.getCodeConfAmount()/taskSize;
        currentCode=new CodeFormatDTO(codeFormatDTO.getRotorInfo(),codeFormatDTO.getReflectorID(),new ArrayList<>());
        for (double i = 0; i < numberOftask; i++) {
            try {
                taskQueue.put(new DecryterTask(currentCode,createNewEngineCopy(),taskSize));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task number:"+i);
          //  currentCode=codeCalculatorFactory.getNextCode(currentCode);
        }
        double remainTask=codeCalculatorFactory.getCodeConfAmount()%taskSize;
        if(remainTask>0) {
            try {
                taskQueue.put(new DecryterTask(currentCode,createNewEngineCopy(),remainTask));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void createTaskMiddleLevel(CodeFormatDTO codeFormatDTO)
    {
        List<String> reflectorIdList = engine.getMachineData().getReflectorIdList();
        CodeFormatDTO currentCode=codeFormatDTO;
        for(String reflector:reflectorIdList)
        {
            currentCode=new CodeFormatDTO(codeFormatDTO.getRotorInfo(),reflector, new ArrayList<>());
            createTaskEasyLevel(currentCode);
        }


    }

    public void createTaskHardLevel(CodeFormatDTO codeFormatDTO)
    {
        List<String> reflectorIdList = engine.getMachineData().getReflectorIdList();
        CodeFormatDTO currentCode=codeFormatDTO;


        List<Integer> rotorId=new ArrayList<>();
        for(RotorInfoDTO rotorInfo:codeFormatDTO.getRotorInfo())
            rotorId.add(rotorInfo.getId());

        currentCode=new CodeFormatDTO(codeFormatDTO.getRotorInfo(),codeFormatDTO.getReflectorID(), new ArrayList<>());
        createTaskEasyLevel(currentCode);



    }



}
