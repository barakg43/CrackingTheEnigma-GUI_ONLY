package decryptionManager;

import decryptionManager.components.*;
import dtoObjects.*;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import enigmaEngine.Engine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

public class DecryptionManager implements Serializable{

    private final int numberOfAgents;
    private Dictionary dictionary;
    private Engine engine;
    private int taskSize;
    private List<int[]> allPossibleRotors;

    private CodeCalculatorFactory codeCalculatorFactory;
    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }
    private MachineDataDTO machineData;
    BlockingQueue<Runnable> taskQueue;
    private BlockingQueue<TaskFinishDataDTO> successfulDecryption;
     private AgentsThreadPool agents;
    private byte[] engineCopyBytes;
    private final int QUEUE_SIZE=1000;
    private CodeFormatDTO saveTempCode;
    public DecryptionManager(int numberOfAgents,Engine engine) {
        this.numberOfAgents = numberOfAgents;
        dictionary=new Dictionary();
        taskQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        successfulDecryption =new LinkedBlockingDeque<>();
        this.engine = engine;
        machineData=engine.getMachineData();
        saveEngineCopy();

        codeCalculatorFactory =new CodeCalculatorFactory(engine.getMachineData().getAlphabetString(),
                machineData.getNumberOfRotorsInUse());
        agents=new AgentsThreadPool(2,numberOfAgents,20, TimeUnit.SECONDS,taskQueue,new AgentThreadFactory());
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

    public  RotorInfoDTO[] getRotorsInfoWithoutPositions(RotorInfoDTO[] rotorsInfo)
    {
        RotorInfoDTO[] tempRotorsInfo=rotorsInfo;
        for(int i=0;i<rotorsInfo.length;i++)
        {
            tempRotorsInfo[i]=new RotorInfoDTO(rotorsInfo[i].getId(),-1, ' ');
        }
        return tempRotorsInfo;
    }

    public void getValidDictionaryWords(String words, List<Character> excludeCharsList, String alphabet)
    {
        dictionary.getValidDictionaryWords(words,excludeCharsList,alphabet);
    }

    public void setDictionary(Dictionary dictionary)
    {
        this.dictionary=dictionary;
    }

    public void createTaskEasyLevel(CodeFormatDTO codeFormatDTO)
    {
        CodeFormatDTO currentCode;
        double numberOftask=codeCalculatorFactory.getCodeConfAmount()/taskSize;
        currentCode=new CodeFormatDTO(codeFormatDTO.getRotorInfo(),codeFormatDTO.getReflectorID(),new ArrayList<>());
        codeCalculatorFactory.addLettersToCal(machineData.getAlphabetString());
        agents.prestartAllCoreThreads();
        for (double i = 0; i < numberOftask && currentCode!=null; i++) {
            try {
                taskQueue.put(new DecryterTask(currentCode,"german poland leg else",createNewEngineCopy(),
                        taskSize,successfulDecryption,dictionary));

            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
            System.out.println("Task number:"+(int)i);
            System.out.println("\n");
           currentCode=codeCalculatorFactory.getNextCodeIndexOffset(currentCode,taskSize);
            System.out.println("current code  " + currentCode);
        }
        double remainTask=codeCalculatorFactory.getCodeConfAmount()%taskSize;
        if(remainTask>0) {
            try {
                taskQueue.put(new DecryterTask(currentCode,"aaaaaaa leg",createNewEngineCopy(),remainTask
                        ,successfulDecryption,dictionary));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void createTaskMiddleLevel(CodeFormatDTO codeFormatDTO){
        saveTempCode= engine.getBFCodeFormat();
        List<String> reflectorIdList = machineData.getReflectorIdList();
        CodeFormatDTO currentCode;
        System.out.println("Starting code: " + codeFormatDTO);
        for(String reflector:reflectorIdList)
        {
            codeFormatDTO=engine.getBFCodeFormat();
            System.out.println("reflector " + reflector);
            currentCode=copyCodeData(codeFormatDTO,reflector);
            System.out.println("After new reflector: " + currentCode);
            createTaskEasyLevel(currentCode);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
    Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption);
    }
    private CodeFormatDTO copyCodeData(CodeFormatDTO codeFormatDTO,String reflector) {
        CodeFormatDTO newCodeFormat;
        RotorInfoDTO[] rotorInfoDTOS=new RotorInfoDTO[codeFormatDTO.getRotorInfo().length];
        for (int i = 0; i < codeFormatDTO.getRotorInfo().length; i++) {
            int distance=codeFormatDTO.getRotorInfo()[i].getDistanceToWindow();
            char positionLetter=codeFormatDTO.getRotorInfo()[i].getStatingLetter();
            rotorInfoDTOS[i]=new RotorInfoDTO(codeFormatDTO.getRotorInfo()[i].getId(),distance,positionLetter);
        }

        newCodeFormat=new CodeFormatDTO(rotorInfoDTOS,reflector,new ArrayList<>());
        return newCodeFormat;
    }

    public void createTaskHardLevel(CodeFormatDTO codeFormatDTO)
    {
        List<String> reflectorIdList = machineData.getReflectorIdList();
        CodeFormatDTO currentCode=codeFormatDTO;

        int rotorUsedNumber=machineData.getNumberOfRotorsInUse();
        Permuter permuterFactory=new Permuter(rotorUsedNumber);
        int[] rotorId=new int[ rotorUsedNumber];
        for (int i = 0; i <rotorUsedNumber ; i++) {
            rotorId[i]=codeFormatDTO.getRotorInfo()[i].getId();
        }
        int[] currentPermutationIndex=permuterFactory.getNext();;

        while (currentPermutationIndex!=null)
        {
            RotorInfoDTO[] currentRotorInfo=codeFormatDTO.getRotorInfo();
            System.out.print("[");
            for (int i = 0; i < rotorUsedNumber; i++) {
                int indexPermute=currentPermutationIndex[i];
                int rotorID=rotorId[indexPermute];
                currentRotorInfo[i]=new RotorInfoDTO(rotorID,
                                                    currentRotorInfo[indexPermute].getDistanceToWindow(),
                                                    currentRotorInfo[indexPermute].getStatingLetter());
                System.out.format("%d,",rotorID);
            }
            System.out.println("]");
            currentCode=new CodeFormatDTO(currentRotorInfo,codeFormatDTO.getReflectorID(), new ArrayList<>());
            createTaskMiddleLevel(currentCode);
            currentPermutationIndex=permuterFactory.getNext();
        }


    }


    public void createTaskImpossibleLevel() {

       // List<int[]> combinations = new ArrayList<>();
        int rotorNumberInSystem=machineData.getNumberOfRotorInSystem();
        int rotorNumberInUsed=machineData.getNumberOfRotorsInUse();



        int[] rotorIdSet = new int[rotorNumberInUsed];

        // initialize with the lowest lexicographic rotorIdSet
        for (int i = 0; i < rotorNumberInUsed; i++) {
            rotorIdSet[i] = i;
        }

        while (rotorIdSet[rotorNumberInUsed - 1] < rotorNumberInSystem) {
            RotorInfoDTO[] rotorInfoDTO=new RotorInfoDTO[rotorNumberInUsed];
            for (int i = 0; i <rotorNumberInUsed ; i++) {
                rotorInfoDTO[i]=new RotorInfoDTO(rotorIdSet[i]+1,0,
                                        codeCalculatorFactory.getFirstLetter());
            }

            CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTO,
                    machineData.getReflectorIdList().get(0),new ArrayList<>());
            createTaskHardLevel(codeFormatDTO);

            // generate next rotorIdSet in lexicographic order
            int temp = rotorNumberInUsed - 1;
            while (temp != 0 && rotorIdSet[temp] == rotorNumberInSystem - rotorNumberInUsed + temp) {
                temp--;
            }
            rotorIdSet[temp]++;
            for (int i = temp + 1; i < rotorNumberInUsed; i++) {
                rotorIdSet[i] = rotorIdSet[i - 1] + 1;
            }
        }




    }

    public Dictionary getDictionary()
    {
        return dictionary;
    }


}
