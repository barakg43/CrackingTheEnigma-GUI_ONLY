package menuEngine;

import EnigmaMachine.enigmaMachine;
import impl.*;
import jaxb.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MenuEngine implements Engine , Serializable {

    private final static String JAXB_XML_PACKAGE_NAME = "jaxb";
    private final enigmaMachine enigmaMachine;
    private MachineDataDTO machineData;
    private SelectedConfigurationDTO selectedConfigurationDTO;
    private List<String> plugBoardPairs;
    private Rotor[] selectedRotors;
    private StatisticsData statisticsData;
    private char[] selectedPositions;
    private Reflector selectedReflector = null;
    private int cipheredInputs;

    boolean withPlugBoardPairs;


    public MenuEngine() {
        enigmaMachine = new enigmaMachine();
        statisticsData=new StatisticsData();
        selectedConfigurationDTO =new SelectedConfigurationDTO();
        cipheredInputs=0;
    }


    @Override
    public MachineDataDTO getMachineData() {
        return machineData;
    }

    @Override
    public SelectedConfigurationDTO getSelectedData() {
        createSelectedDataObj(false);
        return selectedConfigurationDTO;
    }

    @Override
    public void LoadXMLFile(String filePath) {
        File file = new File(filePath);
        if (!(filePath.toLowerCase().endsWith(".xml")))
            throw new RuntimeException("The file you entered isn't XML file.");
        else if (!file.exists())
            throw new RuntimeException("The file you entered isn't exists.");

        try {
            InputStream inputStream = new FileInputStream(filePath);
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            CTEEnigma eng = (CTEEnigma) u.unmarshal(inputStream);
            copyAllData(eng);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void checkIfRotorsValid(String rotors) {
        List<String> arrayString = Arrays.asList(rotors.split(","));
        selectedRotors = new Rotor[enigmaMachine.getRotorsInUse()];
        for (int i = 0; i < enigmaMachine.getRotorsInUse(); i++) {
            selectedRotors[i] = null;
        }
        int i = 0;
        int rotorNum;
        if (arrayString.size() != enigmaMachine.getRotorsInUse())
            throw new RuntimeException("You need to enter " + enigmaMachine.getRotorsInUse() + " rotors with comma between them.");

        for (int j = arrayString.size() - 1; j >= 0; j--) {
            try {
                rotorNum = Integer.parseInt(arrayString.get(j));
            } catch (Exception ex) {
                throw new RuntimeException("The number you entered isn't integer. Please enter an integer number: ");
            }
            if (rotorNum > enigmaMachine.getNumberOfRotors() || rotorNum < 1)
                throw new RuntimeException("There is no such rotors, please select again valid rotors");
            if (findRotor(rotorNum, selectedRotors) != null)
                throw new RuntimeException("You select the same rotor twice, please select again valid rotors\"");
            selectedRotors[i++] = enigmaMachine.getAllRotorsArray()[rotorNum - 1];
        }

        enigmaMachine.setSelectedRotors(selectedRotors);
    }

    @Override
    public boolean checkIfDataValid(String data) {
        return enigmaMachine.getKeyboard().checkValidInput(data);
    }

    @Override
    public StatisticsDataDTO getStatisticDataDTO()
    {
        return statisticsData.getStatisticsDataDTO();
    }

    @Override
    public void checkIfPositionsValid(String positions) {
        positions = positions.toUpperCase();
        char[] positionsList = new char[enigmaMachine.getRotorsInUse()];
        int i = enigmaMachine.getRotorsInUse()-1;
        if (positions.length() != enigmaMachine.getRotorsInUse())
            throw new RuntimeException("You need to give position for each rotor.");
        for (char ch : positions.toCharArray()) {
            if (enigmaMachine.getAlphabet().indexOf(ch) == -1)
                throw new RuntimeException("This position is not exist in the machine. Please enter valid positions:");
            positionsList[i] = ch;
            i--;
        }
        selectedPositions = positionsList;
        for (int j = 0; j < selectedRotors.length; j++) {
            selectedRotors[j].setInitialWindowPosition(selectedPositions[j]);
        }
        enigmaMachine.setSelectedPositions(positionsList);
    }

    @Override
    public String cipherData(String dataInput) {
        int currentRow;
        long startTime=System.nanoTime();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < dataInput.length(); i++) {
            boolean advanceNextRotor = true;//first right rotor always advance every typing of letter
            //the row input after moving in plug board
            currentRow = enigmaMachine.getKeyboard().getMappedOutput(dataInput.charAt(i));
            //move input flow from right rotor to left rotors
            for (Rotor selectedRotor : selectedRotors) {
                advanceNextRotor = selectedRotor.forwardWindowPosition(advanceNextRotor);
                currentRow = selectedRotor.getOutputMapIndex(currentRow, false);
            }
            currentRow = selectedReflector.getMappedOutput(currentRow);
//        System.out.format("Reflct:%d->%d\n", rotorOutput.getOutputIndex(),currentRow);
            //input flow go back from left rotor to right rotor after reflector
            for (int j = selectedRotors.length - 1; j >= 0; j--) {
                currentRow = selectedRotors[j].getOutputMapIndex(currentRow, true);
            }
//        System.out.println("=====");
            output.append(enigmaMachine.getKeyboard().getLetterFromRowNumber(currentRow));
        }
        //System.out.println("output:" + output);
        long endTime=System.nanoTime();
        statisticsData.addCipheredDataToStats(getCodeFormat(),dataInput, output.toString(), endTime-startTime);
        return output.toString();
    }

    @Override
    public void resetCodePosition() {
        for (Rotor selectedRotor : selectedRotors) {
            selectedRotor.resetWindowPositionToInitialPosition();
        }
    }

    @Override
    public String getCodeFormat(){

            int[] selectedRotorsArray= selectedConfigurationDTO.getSelectedRotorsID();
            char[] selectedPositions= selectedConfigurationDTO.getSelectedPositions();
            if(selectedRotorsArray==null)//if user only start the program and not select any configuration
                return "";

            StringBuilder codeFormat=new StringBuilder();
            codeFormat.append('<');
            for(int i=selectedRotorsArray.length-1;i>0;i--)
            {
                codeFormat.append(selectedRotorsArray[i]).append(",");
            }
            codeFormat.append(selectedRotorsArray[0]).append("><");

            for(int i=selectedPositions.length-1;i>=0;i--) {
                codeFormat.append(selectedPositions[i]);
            }
           codeFormat.append(">");
            codeFormat.append(String.format("<%s>", selectedConfigurationDTO.getSelectedReflectorID()));

            if(withPlugBoardPairs)
            {
                List<String> pairs= selectedConfigurationDTO.getPlugBoardPairs();
                codeFormat.append('<');
                for (int i = 0; i < pairs.size()-1; i++)
                    codeFormat.append(String.format("%c|%c,", pairs.get(i).charAt(0), pairs.get(i).charAt(1)));

                codeFormat.append(String.format("%c|%c>",pairs.get(pairs.size()-1).charAt(0),pairs.get(pairs.size()-1).charAt(1)));

            }
            return codeFormat.toString();
    }

    @Override
    public void resetSelected() {
        selectedRotors = null;
        selectedPositions = null;
        selectedReflector = null;
        enigmaMachine.getPlugBoard().resetPlugBoardPairs();
        createSelectedDataObj(true);
    }

    @Override
    public void checkIfReflectorNumValid(String ReflectorNum) {
        int refNum;
        try {
            refNum = Integer.parseInt(ReflectorNum);
        } catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer. Please enter an integer number: ");
        }
        if (!reflectorId.isExist(refNum) || refNum > machineData.getNumberOfReflectors())
            throw new RuntimeException("Please choose one of the options 1-" + enigmaMachine.getAllReflectors().length);

        selectedReflector = enigmaMachine.getAllReflectors()[refNum - 1];
        //enigmaMachine.setSelectedReflector(selectedReflector);
    }

    @Override
    public void CheckPlugBoardPairs(String pairs) {
        plugBoardPairs = Arrays.asList(pairs.split(","));

        for (String str : plugBoardPairs) {
            if (enigmaMachine.getAlphabet().indexOf(str.charAt(0)) == -1 || enigmaMachine.getAlphabet().indexOf(str.charAt(1)) == -1)
                throw new RuntimeException("Pair: " + str + " doesn't exist in the machine alphabet.");
            enigmaMachine.getPlugBoard().addMappedInputOutput(str.charAt(0), str.charAt(1));
        }

    }

    @Override
    public int checkPlugBoardNum(String plugBoardNum) {
        int plugboardNum;
        try {
            plugboardNum = Integer.parseInt(plugBoardNum);
        } catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer. Please enter an integer number: ");
        }
        if (plugboardNum > 2 || plugboardNum < 1)
            throw new RuntimeException("Please choose 1 or 2.");

        withPlugBoardPairs=plugboardNum==1;

        return plugboardNum;
    }

    @Override
    public void getCodeAutomatically() {
        getRotors();
        getReflector();
        getPositions();
        getPairs();
    }

    @Override
    public boolean getWithPlugBoardPairs()
    {
        return withPlugBoardPairs;
    }

    private void getRotors() {
        Random random = new Random();
        int rotorNum;
        selectedRotors = new Rotor[enigmaMachine.getRotorsInUse()];
        for (int i = 0; i < enigmaMachine.getRotorsInUse(); i++) {
            selectedRotors[i] = null;
        }
        for (int i = 0; i < getMachineData().getNumberOfRotorsInUse(); i++) {
            rotorNum = random.nextInt(getMachineData().getRotorsId().length) + 1;
            while (rotorNum > enigmaMachine.getNumberOfRotors() || rotorNum < 1 ||
                    findRotor(rotorNum, selectedRotors) != null) {
                rotorNum = random.nextInt(getMachineData().getRotorsId().length) + 1;
            }
            selectedRotors[i] = enigmaMachine.getAllRotorsArray()[rotorNum - 1];
        }
    }

    private void getReflector() {
        int reflectorNum;
        reflectorNum = getRandomReflector(enigmaMachine.getAllReflectors()).getReflectorId().ordinal() + 1;
        while (!reflectorId.isExist(reflectorNum) || reflectorNum > machineData.getNumberOfReflectors()) {
            reflectorNum = getRandomReflector(enigmaMachine.getAllReflectors()).getReflectorId().ordinal() + 1;
        }

        selectedReflector = enigmaMachine.getAllReflectors()[reflectorNum - 1];
    }

    private static Reflector getRandomReflector(Reflector[] arr) {
        return arr[ThreadLocalRandom.current().nextInt(arr.length)];
    }

    private void getPositions() {
        char position;
        selectedPositions = new char[selectedRotors.length];
        char[] alphabetArray = new char[enigmaMachine.getAlphabet().length()];
        for (int i = 0; i < enigmaMachine.getAlphabet().length(); i++) {
            alphabetArray[i] = enigmaMachine.getAlphabet().charAt(i);
        }
        for (int i = 0; i < selectedRotors.length; i++) {
            selectedPositions[i] = getRandomCharacter(alphabetArray);
        }

        for (int j = 0; j < selectedRotors.length; j++) {
            selectedRotors[j].setInitialWindowPosition(selectedPositions[j]);
        }
    }

    private void getPairs() {
        Random random = new Random();
        withPlugBoardPairs = random.nextBoolean();
        plugBoardPairs=new ArrayList<>();
        boolean res;
        if (withPlugBoardPairs) {
            List<Character> charList = new ArrayList<>();
            for (int i = 0; i < enigmaMachine.getAlphabet().length(); i++) {
                charList.add(enigmaMachine.getAlphabet().charAt(i));
            }
            int numOfPairs = random.nextInt(enigmaMachine.getAlphabet().length() / 2) + 1;
            for (int i = 0; i < numOfPairs; i++) {
                 res = false;
                while (!res) {
                    try {
                        char input = charList.get(random.nextInt(charList.size()));
                        charList.remove(charList.indexOf(input));
                        char output = charList.get(random.nextInt(charList.size()));
                        while (!charList.contains(output))
                            output = charList.get(random.nextInt(charList.size()));
                        charList.remove(charList.indexOf(output));
                        enigmaMachine.getPlugBoard().addMappedInputOutput(input, output);
                        plugBoardPairs.add(String.valueOf(input) + String.valueOf(output));
                        res = true;
                    } catch (Exception ex) {
                        break;
                    }

                }
            }

        }
    }

        private char getRandomCharacter ( char[] arr){
            return arr[ThreadLocalRandom.current().nextInt(arr.length)];
        }


        private Rotor findRotor ( int rotorNum, Rotor[] allRotors)
        {
            for (Rotor rotor : allRotors) {
                if (rotor != null && rotor.getRotorID() == rotorNum)
                    return rotor;
            }
            return null;
        }


        private void createSelectedDataObj ( boolean alreadyExists)
        {
            if (!alreadyExists) {
                int[] rotorsID = copySelectedRotorsID(selectedRotors);
                selectedConfigurationDTO = new SelectedConfigurationDTO(selectedPositions, selectedReflector.getReflectorId().name(),
                        rotorsID, plugBoardPairs);
            } else {
                selectedConfigurationDTO = new SelectedConfigurationDTO();
            }

        }

        private int[] copySelectedRotorsID (Rotor[] selectedRotors)
        {
            int[] selectedRotorsID = new int[selectedRotors.length];
            for (int i = selectedRotorsID.length-1; i >= 0 ; i--) {
                selectedRotorsID[i] = selectedRotors[i].getRotorID();
            }
            return selectedRotorsID;
        }

        private void copyAllData (CTEEnigma eng){
            String alphabet = eng.getCTEMachine().getABC().replaceAll("\n", "");
            alphabet = alphabet.replaceAll("\t", "");

            if (alphabet.length() % 2 != 0)
                throw new RuntimeException("The number of letters need to be even.\nPlease correct this.");
            enigmaMachine.setAlphabet(eng.getCTEMachine().getABC());
            if (eng.getCTEMachine().getRotorsCount() < 2)
                throw new RuntimeException("The number of rotors need to be bigger or equal to 2.\nPlease correct this.");
            if (eng.getCTEMachine().getRotorsCount() > eng.getCTEMachine().getCTERotors().getCTERotor().size())
                throw new RuntimeException("The number of rotors that used is greater than the number of rotors given with the machine.\nPlease correct this.");
            enigmaMachine.setRotorsInUse(eng.getCTEMachine().getRotorsCount());
            enigmaMachine.setReflectors(eng.getCTEMachine().getCTEReflectors().getCTEReflector());
            enigmaMachine.setRotors(eng.getCTEMachine().getCTERotors().getCTERotor());

            int[] rotorsArrayId = copyRotorsID(eng.getCTEMachine().getCTERotors().getCTERotor());
            int[] notchArray = copyNotchArray(eng.getCTEMachine().getCTERotors().getCTERotor());
            machineData = new MachineDataDTO(eng.getCTEMachine().getCTEReflectors().getCTEReflector().size(),
                    eng.getCTEMachine().getRotorsCount(), rotorsArrayId, notchArray);
        }

        private int[] copyRotorsID (List < CTERotor > rotorsArray)
        {
            int[] rotorsID = new int[rotorsArray.size()];
            for (int i = 0; i < rotorsArray.size(); i++) {
                rotorsID[i] = rotorsArray.get(i).getId();
            }
            return rotorsID;
        }

        private int[] copyNotchArray (List < CTERotor > rotorsArray)
        {
            int[] notchNumbers = new int[rotorsArray.size()];
            for (int i = 0; i < rotorsArray.size(); i++) {
                notchNumbers[i] = rotorsArray.get(i).getNotch();
            }
            return notchNumbers;
        }

    public int getCipheredInputs()
    {
        return cipheredInputs;
    }
    public int addCipheredInputs()
    {
        return cipheredInputs++;
    }
    @Override
    public String toString() {
        return "MenuEngine{" +
                ", \nmachineData=" + machineData +
                ", \nselectedConfigurationDTO=" + selectedConfigurationDTO +
                ", \nplugBoardPairs=" + plugBoardPairs +
                ", \nselectedRotors=" + Arrays.toString(selectedRotors) +
                ", \nstatisticsData=" + statisticsData +
                ", \nselectedPositions=" + Arrays.toString(selectedPositions) +
                ", \nselectedReflector=" + selectedReflector +
                ", \nwithPlugBoardPairs=" + withPlugBoardPairs +
                ", \ncipheredInputs=" + cipheredInputs +
                '}';
    }

}
