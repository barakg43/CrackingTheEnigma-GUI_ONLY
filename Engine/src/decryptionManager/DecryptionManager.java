package decryptionManager;

import decryptionManager.components.*;

import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.BruteForceLevel;

import dtoObjects.DmDTO.TaskFinishDataDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.RotorInfoDTO;
import enigmaEngine.Engine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


public class DecryptionManager {


    private final Dictionary dictionary;
    private final Engine engine;
    BruteForceLevel level=null;
    private int taskSize=0;
    private  int agentsAmount;
    private final CodeCalculatorFactory codeCalculatorFactory;
    private final MachineDataDTO machineData;
    BlockingQueue<Runnable> taskQueue;
    private final BlockingQueue<TaskFinishDataDTO> successfulDecryption;
     private AgentsThreadPool agents;
    private byte[] engineCopyBytes;
    private final int QUEUE_SIZE=1000;
    public static PrintWriter fileOutput;
    private AtomicCounter taskDoneAmount;

    private Thread taskCreator;
    private double totalTaskAmount;
    private long taskCounter;
    public DecryptionManager(Engine engine) {
        taskQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        successfulDecryption =new LinkedBlockingDeque<>();
        this.engine = engine;
        dictionary=engine.getDictionary();
        machineData=engine.getMachineData();
        totalTaskAmount=0;
        saveEngineCopy();
        codeCalculatorFactory =new CodeCalculatorFactory(engine.getMachineData().getAlphabetString(),
                machineData.getNumberOfRotorsInUse());



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
    public void setSetupConfiguration(BruteForceLevel level,int agentAmount,int taskSize)
    {
    this.level=level;
    this.agentsAmount=agentAmount;
    this.taskSize=taskSize;
    totalTaskAmount=0;
    if(taskSize<1)
        throw new RuntimeException("task size must be positive number!(greater then 0)");
    if(agentAmount<2||agentAmount>50)
        throw new RuntimeException("Agent amount must be between 2 and 50");
    if(level==null)
        throw new RuntimeException("Brute force level must be selected");
    agents=new AgentsThreadPool(2,agentAmount,20, TimeUnit.SECONDS,taskQueue,new AgentThreadFactory());
    }
    public double getTotalTasksAmount(BruteForceLevel level) {
        if(totalTaskAmount==0)
            calculateTotalTaskAmount(level);

        return totalTaskAmount;
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
    public void pause()  {
//        try {
//            taskCreator.checkAccess();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
    public void resume()  {
        taskCreator.notify();
    }
    public Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption);
    }
    public void startBruteForce()
    {

        taskCounter=0;
        totalTaskAmount=0;
        try {
            fileOutput = new PrintWriter( "C:\\ComputerScience\\Java\\EXCISES\\RUN-TEST\\output3.txt","UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Total amount:"+getTotalTasksAmount(level));

        taskCreator=new Thread(()-> {
            fileOutput.print("");
            try {
                agents.prestartAllCoreThreads();
                CodeFormatDTO startingCode = engine.getCodeFormat(false);
                switch (level) {
                    case easyLevel:
                        createTaskEasyLevel(startingCode);
                        break;
                    case middleLevel:
                        createTaskMiddleLevel(startingCode);
                        break;
                    case hardLevel:
                        createTaskHardLevel(startingCode);
                        break;
                    case impossibleLevel:
                        createTaskImpossibleLevel();
                        break;
                }

            } catch (RuntimeException e) {
                throw new RuntimeException("Error when creating tasks: "+e);
            }
            fileOutput.close();
        },"Task Creator DM Thread");
        taskCreator.start();
       // fileOutput.close();
    }


    private void createTaskImpossibleLevel() {

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


    private void createTaskHardLevel(CodeFormatDTO codeFormatDTO)
    {
        CodeFormatDTO currentCode=codeFormatDTO;
        int rotorUsedNumber=machineData.getNumberOfRotorsInUse();
        Permuter permuterFactory=new Permuter(rotorUsedNumber);
        int[] rotorId=new int[ rotorUsedNumber];
        for (int i = 0; i <rotorUsedNumber ; i++) {
            rotorId[i]=codeFormatDTO.getRotorInfoArray()[i].getId();
        }
        int[] currentPermutationIndex=permuterFactory.getNext();

        while (currentPermutationIndex!=null)
        {
            RotorInfoDTO[] currentRotorInfo=codeFormatDTO.getRotorInfoArray();

            for (int i = 0; i < rotorUsedNumber; i++) {
                int indexPermute=currentPermutationIndex[i];
                int rotorID=rotorId[indexPermute];
                currentRotorInfo[i]=new RotorInfoDTO(rotorID,
                        currentRotorInfo[indexPermute].getDistanceToWindow(),
                        currentRotorInfo[indexPermute].getStatingLetter());

            }

            currentCode=new CodeFormatDTO(currentRotorInfo,codeFormatDTO.getReflectorID(), new ArrayList<>());
            createTaskMiddleLevel(currentCode);
            currentPermutationIndex=permuterFactory.getNext();
        }


    }

    private void createTaskMiddleLevel(CodeFormatDTO codeFormatDTO){
        List<String> reflectorIdList = machineData.getReflectorIdList();
        CodeFormatDTO currentCode;
        for(String reflector:reflectorIdList)
        {
           // System.out.println("reflector " + reflector);
            currentCode=new CodeFormatDTO(codeFormatDTO.getRotorInfoArray(),reflector,codeFormatDTO.getPlugboardPairDTOList());
          //  System.out.println("After new reflector: " + currentCode);
            createTaskEasyLevel(currentCode);

        }


    }
    private void createTaskEasyLevel(CodeFormatDTO codeFormatDTO)
    {
        CodeFormatDTO currentCode=null,temp;

        temp=resetAllPositionToFirstPosition(codeFormatDTO);
        double i = 0;

        while(temp!=null){
            currentCode=temp;

            fileOutput.println("Total Task number:"+(++taskCounter));
            System.out.println("Total Task number:"+(taskCounter));
            //                taskQueue.put(new DecryptedTask(CodeFormatDTO.copyOf(currentCode),
//                        "german poland leg else",codeCalculatorFactory
//                        ,createNewEngineCopy(),
//                         taskSize,
//                        successfulDecryption,
//                        dictionary));
            new DecryptedTask(CodeFormatDTO.copyOf(currentCode),
                    "aaaa",codeCalculatorFactory
                    ,createNewEngineCopy(),
                     taskSize,
                    successfulDecryption,
                    dictionary).run();
            fileOutput.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            fileOutput.flush();
            temp=codeCalculatorFactory.getNextCodeIndexOffset(temp,taskSize);
            fileOutput.println("inner while");
            //System.out.println("current code  " + currentCode);
        }

        fileOutput.println("last code:"+currentCode);
        fileOutput.flush();
    }

    private CodeFormatDTO resetAllPositionToFirstPosition(CodeFormatDTO codeFormatDTO) {
        RotorInfoDTO[] rotorInfoArray = codeFormatDTO.getRotorInfoArray();
        for (int i = 0; i < rotorInfoArray.length; i++) {
            rotorInfoArray[i] = new RotorInfoDTO(rotorInfoArray[i].getId(), 0,
                    codeCalculatorFactory.getFirstLetter());
        }
        return new CodeFormatDTO(rotorInfoArray, codeFormatDTO.getReflectorID(), new ArrayList<>());


    }


    private double calculateEasyLevelTaskAmount()
    {
        double totalAmount=codeCalculatorFactory.getCodeConfAmount()/taskSize;
        if(codeCalculatorFactory.getCodeConfAmount()%taskSize>0)
            totalAmount+=1;
        return Math.floor(totalAmount);
    }
    private double calculateMiddleLevelTaskAmount()
    {
        return (machineData.getReflectorIdList().size())*calculateEasyLevelTaskAmount();
    }
    private double calculateHardLevelTaskAmount()
    {
        double totalAmount=calculateFactorial(machineData.getNumberOfRotorsInUse());
        return totalAmount*calculateMiddleLevelTaskAmount();
    }
    private double calculateImpossibleLevelTaskAmount()
    {
        int n=machineData.getNumberOfRotorInSystem();
        int k=machineData.getNumberOfRotorsInUse();
        double totalAmount=calculateFactorial(n)
                /(calculateFactorial(k)*calculateFactorial(n-k));
        return totalAmount*calculateHardLevelTaskAmount();
    }
    private double calculateFactorial(int number) {
        double fact = 1;
        for (int i = 1; i <= number; i++) {
            fact = fact * i;
        }
        return fact;
    }
    private void calculateTotalTaskAmount(BruteForceLevel level)
    {
            switch (level)
            {
                case easyLevel:
                    totalTaskAmount= calculateEasyLevelTaskAmount();
                break;
                case middleLevel:
                    totalTaskAmount= calculateMiddleLevelTaskAmount();
                    break;
                case hardLevel:
                    totalTaskAmount= calculateHardLevelTaskAmount();
                    break;
                case impossibleLevel:
                    totalTaskAmount= calculateImpossibleLevelTaskAmount();
                    break;
            }

    }

}



