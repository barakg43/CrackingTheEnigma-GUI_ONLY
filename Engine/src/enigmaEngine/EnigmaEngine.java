package enigmaEngine;

import dtoObjects.*;
import enigmaMachine.EnigmaMachine;
import enigmaMachine.parts.Reflector;
import enigmaMachine.parts.Rotor;
import jaxb.CTEEnigma;
import jaxb.CTERotor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EnigmaEngine implements Engine , Serializable {

    private final static String JAXB_XML_PACKAGE_NAME = "jaxb";
    private static final String stateMachineFileExtension=".bin";
    private EnigmaMachine enigmaMachine;
    private MachineDataDTO machineData;
    private List<PlugboardPairDTO> plugBoardPairs;

    private StatisticsData statisticsData;

    private Reflector selectedReflector = null;
    private char[] selectedPositions;
    private Rotor[] selectedRotors;
    private int cipheredInputs;
    private CodeFormatDTO initialCodeFormat;
    private  char[] tempSelectedInitPositions;
    private  int tempSelectedReflectorID;
    private  List<Integer> tempSelectedRotorsID;
    private  List<PlugboardPairDTO> tempPlugBoardPairs;
    private long sumProcessingTime=0;
    @Override
    public boolean isMachineLoaded()
    {
        return enigmaMachine!=null;
    }

    public EnigmaEngine() {
        enigmaMachine = null;
        statisticsData=new StatisticsData();
        cipheredInputs=0;
        initialCodeFormat=null;
    }

    public void resetAllData()
    {

        plugBoardPairs=new ArrayList<>();
        statisticsData=new StatisticsData();
        selectedRotors=null;
        selectedPositions=null;
        selectedReflector=null;
        cipheredInputs=0;
    }


    @Override
    public MachineDataDTO getMachineData() {

        return machineData;
    }


    @Override
    public boolean isCodeConfigurationIsSet() {
        return initialCodeFormat!=null;
    }

    @Override
    public void loadXMLFile(String filePath) {
        filePath=filePath.replaceAll("\"","");//for case user enter with " "

        File file = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            CTEEnigma eng = (CTEEnigma) u.unmarshal(inputStream);
            copyAllData(eng);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error on parsing xml file!");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void checkIfRotorsValid( List<Integer> rotors) {
        tempSelectedRotorsID = new ArrayList<>(enigmaMachine.getRotorNumberInUse());
         int rotorNum;
        if (rotors.size() != enigmaMachine.getRotorNumberInUse())
            throw new RuntimeException("You need to choose " + enigmaMachine.getRotorNumberInUse() + " rotors.");

        for (int j = rotors.size() - 1; j >= 0; j--) {
            rotorNum=rotors.get(j);
            if (tempSelectedRotorsID.contains(rotorNum))
                throw new RuntimeException("You select the same rotor twice.");
            tempSelectedRotorsID.add(rotorNum);

        }
    }



    @Override
    public StatisticsDataDTO getStatisticDataDTO()
    {
        return statisticsData;
    }

    @Override
    public void checkIfPositionsValid(List<Character> positions) {
//        positions = positions.toUpperCase();
         tempSelectedInitPositions = new char[enigmaMachine.getRotorNumberInUse()];
//        int i = enigmaMachine.getRotorNumberInUse()-1;
//        if (positions.length() != enigmaMachine.getRotorNumberInUse())
//            throw new RuntimeException("You need to give position for each rotor.");
//        for (char ch : positions.toCharArray()) {
//            if (enigmaMachine.getAlphabet().indexOf(ch) == -1)
//                throw new RuntimeException("This position is not exist in the machine.");
//            tempSelectedInitPositions[i--] = ch;
//        }

        int i = enigmaMachine.getRotorNumberInUse()-1;
        for (int j = positions.size()-1; j >=0; j--) {
            Character ch=positions.get(j);
            tempSelectedInitPositions[i--]=ch;
        }

    }

    @Override
    public String processDataInput(String dataInput) {

        resetProcessingTime();
        dataInput = dataInput.toUpperCase();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < dataInput.length(); i++) {
            output.append(processDataInput(dataInput.charAt(i)));
        }
        //System.out.println("output:" + output);

        statisticsData.addCipheredDataToStats(getCodeFormat(true),dataInput, output.toString(), sumProcessingTime);
        cipheredInputs++;
        return output.toString();
    }

    @Override
    public void resetProcessingTime() {
        sumProcessingTime=0;
    }

    @Override
    public void resetCodePosition() {
        for (Rotor selectedRotor : selectedRotors) {
            selectedRotor.resetWindowPositionToInitialPosition();
        }
    }


    private CodeFormatDTO createCodeFormat(boolean isCalcDistanceFromInitWindow)
    {
        int rotorID;
        int distanceToWindow;
        char initPositionLetter;
        RotorInfoDTO[] rotorInfoArray=new RotorInfoDTO[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < rotorInfoArray.length; i++) {
            rotorID=selectedRotors[i].getRotorID();
            distanceToWindow=selectedRotors[i].calculateDistanceFromNotchToWindows(isCalcDistanceFromInitWindow);
            initPositionLetter=isCalcDistanceFromInitWindow? selectedPositions[i ]: selectedRotors[i].getLetterInWindowPosition();
            rotorInfoArray[i]=new RotorInfoDTO(rotorID,distanceToWindow,initPositionLetter);

        }
        return new CodeFormatDTO(rotorInfoArray,selectedReflector.getReflectorIdName(),plugBoardPairs);


    }
    @Override
    public CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow) {
        if (isCalcDistanceFromInitWindow)
            return initialCodeFormat;
        else
            return createCodeFormat(false);
    }

    @Override
    public void resetSelected() {
        selectedRotors = null;
        selectedPositions = null;
        selectedReflector = null;
       // tempSelectedReflectorID=0;
      //  tempPlugBoardPairs=null;
        //tempSelectedInitPositions=null;
       // tempSelectedRotorsID=null;
        enigmaMachine.getPlugBoard().resetPlugBoardPairs();

    }


    @Override
    public void checkIfReflectorNumValid(String ReflectorNum) {
        int refNum;
        tempSelectedReflectorID=0;
        try {
            refNum = Integer.parseInt(ReflectorNum);
        } catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer.");
        }
        if (!Reflector.isIdExist(refNum) || refNum > enigmaMachine.getReflectorsNumber())
            throw new RuntimeException("You need to choose one of the options 1-" + enigmaMachine.getReflectorsNumber());

        tempSelectedReflectorID=refNum;

    }

    @Override
    public void setReflector(String reflector)
    {
        tempSelectedReflectorID=Reflector.convertRomanIdToNumber(reflector);
    }



    @Override
    public void checkPlugBoardPairs(String pairs)  {
        pairs=pairs.toUpperCase();
        tempPlugBoardPairs=new ArrayList<>();
        if(pairs.length()==0) {
            setMachineConfigurationByUser();
            return;
        }


        if(pairs.length()%2!=0)
            throw new RuntimeException("There is a character that has no pair.must be even number in input string.");


        //withPlugBoardPairs=true;
        List<Character> alreadyExistLetter=new ArrayList<>();
        Set<Character> letterSet=new HashSet<>(pairs.length());
        for(int i=0;i<pairs.length();i++) {
            if (enigmaMachine.getAlphabet().indexOf(pairs.charAt(i)) == -1 )
                throw new RuntimeException("Pair: " + pairs.charAt(i) + " doesn't exist in the machine alphabet.");
            if(letterSet.contains(pairs.charAt(i)))
                alreadyExistLetter.add(pairs.charAt(i));
            else
                letterSet.add(pairs.charAt(i));
        }
        if(alreadyExistLetter.size()!=0)
            throw new RuntimeException("the letters:\n"+alreadyExistLetter+ " appears more then once.");

        for(int i=0;i<pairs.length();i+=2)
        {
            tempPlugBoardPairs.add(new PlugboardPairDTO(pairs.charAt(i),pairs.charAt(i+1)));
        }

        setMachineConfigurationByUser();//user success to choose all machine configuration ,move to new setup
    }

    public void setPlugBoardPairs(List<PlugboardPairDTO> plugBoardPairs)
    {
        tempPlugBoardPairs=new ArrayList<>();
        tempPlugBoardPairs.addAll(plugBoardPairs);
        setMachineConfigurationByUser();
    }
    public void setMachineConfigurationByUser()
    {
        resetSelected();

        selectedPositions=tempSelectedInitPositions;
        selectedRotors=new Rotor[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i <enigmaMachine.getRotorNumberInUse() ; i++) {
            int rotorId=tempSelectedRotorsID.get(i);
            selectedRotors[i] = enigmaMachine.getRotorById(rotorId);
            selectedRotors[i].setInitialWindowPosition(selectedPositions[i]);
        }
        selectedReflector=enigmaMachine.getReflectorById(tempSelectedReflectorID);
        plugBoardPairs=tempPlugBoardPairs;
      for(PlugboardPairDTO pair:tempPlugBoardPairs)
          enigmaMachine.getPlugBoard().addMappedInputOutput(pair.getFirstLetter(),pair.getSecondLetter());

        setInitialCode();

    }

    @Override
    public void  saveMachineStateToFile(String filePathNoExtension) {
        filePathNoExtension=filePathNoExtension.replaceAll("\"","");//for case user enter with " "
        filePathNoExtension+=stateMachineFileExtension;
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(filePathNoExtension)))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static EnigmaEngine loadMachineStateFromFile(String filePathNoExtension) {
        filePathNoExtension = filePathNoExtension.replaceAll("\"", "");//for case user enter with " "
        filePathNoExtension += stateMachineFileExtension;
        File savedStateFile = new File(filePathNoExtension);
        if (!savedStateFile.exists())
            throw new RuntimeException("This file doesn't exists. PLease enter valid file path");

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(filePathNoExtension)))) {
                   return (EnigmaEngine) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setCodeAutomatically() {
        setRandomRotors();
        setRandomReflector();
        setRandomPositions();
        setRandomPlugboardPairs();
        setInitialCode();

    }

    private void setInitialCode() {
        initialCodeFormat = createCodeFormat(true);
    }

    @Override
    public boolean getWithPlugBoardPairs()
    {
        return plugBoardPairs!=null&&plugBoardPairs.size()!=0;
    }

    private void setRandomRotors() {
        Random random = new Random();
        int rotorNum;
        Set<Integer> alreadyUsed=new HashSet<>();
        selectedRotors = new Rotor[enigmaMachine.getRotorNumberInUse()];
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            selectedRotors[i] = null;
        }
        for (int i = 0; i < enigmaMachine.getRotorNumberInUse(); i++) {
            do {
                rotorNum=random.nextInt(enigmaMachine.getNumberOfRotors()) + 1;
            }
            while (alreadyUsed.contains(rotorNum));
            alreadyUsed.add(rotorNum);
            selectedRotors[i] = enigmaMachine.getRotorById(rotorNum);
        }
    }

    private void setRandomReflector() {
        int reflectorNum=ThreadLocalRandom.current().nextInt(enigmaMachine.getReflectorsNumber())+1;
        selectedReflector = enigmaMachine.getReflectorById(reflectorNum);
    }

    private void setRandomPositions() {
        selectedPositions = new char[selectedRotors.length];
        for (int i = 0; i < selectedRotors.length; i++) {
            selectedPositions[i] = getRandomCharacter(enigmaMachine.getAlphabet());
            selectedRotors[i].setInitialWindowPosition(selectedPositions[i]);
        }

    }

    private void setRandomPlugboardPairs() {
        Random random = new Random();
        boolean withPlugBoardPairs = random.nextBoolean();
        plugBoardPairs=new ArrayList<>();
        boolean res;
        if (withPlugBoardPairs) {
            String alphabet=enigmaMachine.getAlphabet();
            int alphabetSize=alphabet.length();
            Set<Character> alreadyExist=new HashSet<>(alphabet.length());
            int numOfPairs = random.nextInt(alphabet.length() / 2) + 1;
            char input,output;
            for (int i = 0; i < numOfPairs; i++) {
                 res = false;
                while (!res) {
                    try {
                        input = alphabet.charAt(random.nextInt(alphabetSize));
                        while(alreadyExist.contains(input))
                        {
                            input = alphabet.charAt(random.nextInt(alphabetSize));
                        }
                        alreadyExist.add(input);
                        output = alphabet.charAt(random.nextInt(alphabetSize));
                        while(alreadyExist.contains(output))
                        {
                            output = alphabet.charAt(random.nextInt(alphabetSize));
                        }
                        alreadyExist.add(output);
                        enigmaMachine.getPlugBoard().addMappedInputOutput(input, output);
                        plugBoardPairs.add(new PlugboardPairDTO(input,output));
                        res = true;
                    } catch (RuntimeException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }

        }
    }

        private char getRandomCharacter ( String alphabet){
            return alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length()));
        }


        private Rotor findRotorByIdInSelectedRotors ( int rotorNum)
        {
            for (Rotor rotor : selectedRotors) {
                if (rotor != null && rotor.getRotorID() == rotorNum)
                    return rotor;
            }
            return null;
        }




        private void copyAllData (CTEEnigma eng){
            String alphabet = eng.getCTEMachine().getABC().trim().toUpperCase();
            if (alphabet.length() % 2 != 0)
                throw new RuntimeException("The number of letters need to be even.");

            if (eng.getCTEMachine().getRotorsCount() < 2)
                throw new RuntimeException("The number of rotors need to be bigger or equal to 2.");
            if (eng.getCTEMachine().getRotorsCount() > eng.getCTEMachine().getCTERotors().getCTERotor().size())
                throw new RuntimeException("The number of rotors that used is greater than the number of rotors given with the machine.");

            EnigmaMachine tempEnigmaMachine = new EnigmaMachine();
            tempEnigmaMachine.setAlphabet(eng.getCTEMachine().getABC());
            tempEnigmaMachine.setRotorsInUse(eng.getCTEMachine().getRotorsCount());
            tempEnigmaMachine.setReflectors(eng.getCTEMachine().getCTEReflectors().getCTEReflector());
            tempEnigmaMachine.setRotors(eng.getCTEMachine().getCTERotors().getCTERotor());
            int[] rotorsArrayId = copyRotorsID(eng.getCTEMachine().getCTERotors().getCTERotor());
            enigmaMachine= tempEnigmaMachine;
            initialCodeFormat=null;
            machineData = new MachineDataDTO(eng.getCTEMachine().getRotorsCount(),
                                             rotorsArrayId,
                                             enigmaMachine.getReflectorIDList(),
                                             enigmaMachine.getAlphabet());

        }

        private int[] copyRotorsID (List < CTERotor > rotorsArray)
        {
            int[] rotorsID = new int[rotorsArray.size()];
            for (int i = 0; i < rotorsArray.size(); i++) {
                rotorsID[i] = rotorsArray.get(i).getId();
            }
            return rotorsID;
        }

    @Override
    public char processDataInput(char charInput) {
        long startTime=System.nanoTime();


        boolean advanceNextRotor = true;//first right rotor always advance every typing of letter
        //the row input after moving in plug board
        int currentRow = enigmaMachine.getKeyboard().getMappedOutput(charInput);
        //move input flow from right rotor to left rotors
        for (Rotor selectedRotor : selectedRotors) {
            advanceNextRotor = selectedRotor.forwardWindowPosition(advanceNextRotor);
            currentRow = selectedRotor.getOutputMapIndex(currentRow, false);
        }
        currentRow = selectedReflector.getMappedOutput(currentRow);
//        System.out.format("Reflect:%d->%d\n", rotorOutput.getOutputIndex(),currentRow);
        //input flow go back from left rotor to right rotor after reflector
        for (int j = selectedRotors.length - 1; j >= 0; j--) {
            currentRow = selectedRotors[j].getOutputMapIndex(currentRow, true);
        }

        sumProcessingTime+=System.nanoTime()-startTime;

        return enigmaMachine.getKeyboard().getLetterFromRowNumber(currentRow);
    }

    public int getCipheredInputs()
    {
        return cipheredInputs;
    }
    @Override
    public String toString() {
        return "MenuEngine{" +
                ", \nmachineData=" + machineData +
                ", \nplugBoardPairs=" + plugBoardPairs +
                ", \nselectedRotors=" + Arrays.toString(selectedRotors) +
                ", \nstatisticsData=" + statisticsData +
                ", \nselectedPositions=" + Arrays.toString(selectedPositions) +
                ", \nselectedReflector=" + selectedReflector +
                ", \nwithPlugBoardPairs=" + getWithPlugBoardPairs() +
                ", \ncipheredInputs=" + cipheredInputs +
                '}';
    }

}
