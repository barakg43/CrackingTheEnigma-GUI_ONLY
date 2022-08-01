package menuEngine;

import EnigmaMachine.enigmaMachine;
import impl.*;
import jaxb.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class MenuEngine {
    private final static String JAXB_XML_PACKAGE_NAME = "jaxb";
    private final enigmaMachine enigmaMachine;
    private MachineDataDTO1 MachineData;
    private SelectedDataDTO1 SelectedData;
    private List<String>  plugBoardPairs;
    private Rotor[] selectedRotors;

    private char[] selectedPositions;
    private Reflector selectedReflector=null;


    public MenuEngine()
    {
        enigmaMachine=new enigmaMachine();
    }
    public MachineDataDTO1 getMachineData() {
        return MachineData;
    }

    public SelectedDataDTO1 getSelectedData() {
        createSelectedDataObj();
        return SelectedData;
    }

    public void LoadXMLFile(String filePath)
    {
        File file = new File(filePath);
        if(!(filePath.toLowerCase().endsWith(".xml")))
            throw new RuntimeException("The file you entered isn't XML file.");
        else if(!file.exists())
            throw new RuntimeException("The file you entered isn't exists.");

        try {
            InputStream inputStream = new FileInputStream(filePath);
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            CTEEnigma eng = (CTEEnigma) u.unmarshal(inputStream);
            copyAllData(eng);

        }  catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void checkIfRotorsValid(String rotors)  {
        List<String> arrayString = Arrays.asList(rotors.split(","));
        selectedRotors=new Rotor[enigmaMachine.getRotorsInUse()];
        for (int i = 0; i <enigmaMachine.getRotorsInUse() ; i++) {
            selectedRotors[i]=null;
        }
        int i=0;
        int rotorNum;
        if(arrayString.size()!=enigmaMachine.getRotorsInUse())
            throw new RuntimeException("You need to enter "+ enigmaMachine.getRotorsInUse()+ " rotors with comma between them.");

        for (int j = arrayString.size()-1; j >=0 ; j--) {
                try {
                    rotorNum = Integer.parseInt(arrayString.get(j));
                } catch (Exception ex) {
                    throw new RuntimeException("The number you entered isn't integer. Please enter an integer number: ");
                }
                if(rotorNum > enigmaMachine.getNumberOfRotors() ||rotorNum < 1)
                    throw new RuntimeException("There is no such rotors, please select again valid rotors");
                if(findRotor(rotorNum,selectedRotors)!=null)
                    throw new RuntimeException("You select the same rotor twice, please select again valid rotors\"");
                selectedRotors[i++]= enigmaMachine.getAllRotorsArray()[rotorNum-1];
                }

        enigmaMachine.setSelectedRotors(selectedRotors);
    }
    public boolean checkIfDataValid(String data)
    {
        return enigmaMachine.getKeyboard().checkValidInput(data);
    }
    public void checkIfPositionsValid(String positions)  {
        positions=positions.toUpperCase();
        char[] positionsList = new char[enigmaMachine.getRotorsInUse()];
        int i=0;
        if(positions.length()!=enigmaMachine.getRotorsInUse())
            throw new RuntimeException("You need to give position for each rotor.");
        for (char ch : positions.toCharArray()) {
            if(enigmaMachine.getAlphabet().indexOf(ch)==-1)
                throw new RuntimeException("This position is not exist in the machine. Please enter valid positions:");
            positionsList[i]=ch;
            i++;
        }
        selectedPositions=positionsList;
        for(int j=0;j<selectedRotors.length;j++)
        {
            selectedRotors[j].setInitialWindowPosition(selectedPositions[j]);
        }
        //enigmaMachine.setSelectedPositions(positionsList);
    }
public String chipperData(String dataInput) {
    int currentRow;
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
    System.out.println("output:" + output);
    return output.toString();
}

    public void resetCodePosition()
    {
        for (Rotor selectedRotor : selectedRotors) {
            selectedRotor.resetWindowPositionToInitialPosition();

        }
    }

    public void checkIfReflectorNumValid(String ReflectorNum) {
        int refNum;
        try {
            refNum = Integer.parseInt(ReflectorNum);
        } catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer. Please enter an integer number: ");
        }
        if(!reflectorId.isExist(refNum) || refNum> MachineData.getNumberOfReflectors() )
            throw new RuntimeException("Please choose one of the options 1-"+ enigmaMachine.getAllReflectors().length);

        selectedReflector=enigmaMachine.getAllReflectors()[refNum-1];
        //enigmaMachine.setSelectedReflector(selectedReflector);
    }

    public void CheckPlugBoardPairs(String pairs)  {
        plugBoardPairs = Arrays.asList(pairs.split(","));

        for (String str : plugBoardPairs) {
            if(enigmaMachine.getAlphabet().indexOf(str.charAt(0))==-1 || enigmaMachine.getAlphabet().indexOf(str.charAt(1))==-1)
                throw new RuntimeException("Pair: "+ str + " doesn't exist in the machine alphabet.");
            enigmaMachine.getPlugBoard().addMappedInputOutput(str.charAt(0), str.charAt(1));
        }

    }

    public int checkPlugBoardNum(String plugBoardNum)  {
        int plugboardNum;
        try {
            plugboardNum = Integer.parseInt(plugBoardNum);
        }catch (Exception ex) {
            throw new RuntimeException("The number you entered isn't integer. Please enter an integer number: ");
        }
        if(plugboardNum>2|| plugboardNum<1)
            throw new RuntimeException("Please choose 1 or 2.");

        return plugboardNum;
    }


    private Rotor findRotor(int rotorNum, Rotor[] allRotors)
    {
        for (Rotor rotor:allRotors) {
            if(rotor!=null && rotor.getRotorID()==rotorNum)
                return rotor;
        }
        return null;
    }


    private void createSelectedDataObj()
    {
        int[] rotorsID=copySelectedRotorsID(enigmaMachine.getSelectedRotors());
        SelectedData=new SelectedDataDTO1(enigmaMachine.getSelectedPositions(),selectedReflector.getReflectorId().name(),
               rotorsID,plugBoardPairs);
    }

    private int[] copySelectedRotorsID(Rotor[] selectedRotors)
    {
        int[] selectedRotorsID=new int[selectedRotors.length];
        for(int i=0;i<selectedRotorsID.length;i++)
        {
            selectedRotorsID[i]=selectedRotors[i].getRotorID();
        }
        return selectedRotorsID;
    }

    private void copyAllData(CTEEnigma eng)  {
        String alphabet=eng.getCTEMachine().getABC().replaceAll("\n","");
        alphabet=alphabet.replaceAll("\t","");

        if(alphabet.length()%2!=0)
            throw new RuntimeException("The number of letters need to be even.\nPlease correct this.");
        enigmaMachine.setAlphabet(eng.getCTEMachine().getABC());//TODO:
                                                                                //change to real plugboard
        if(eng.getCTEMachine().getRotorsCount()<2)
            throw new RuntimeException("The number of rotors need to be bigger or equal to 2.\nPlease correct this.");
        if(eng.getCTEMachine().getRotorsCount()>eng.getCTEMachine().getCTERotors().getCTERotor().size())
            throw new RuntimeException("The number of rotors that used is greater than the number of rotors given with the machine.\nPlease correct this.");
        enigmaMachine.setRotorsInUse(eng.getCTEMachine().getRotorsCount());
        enigmaMachine.setReflectors(eng.getCTEMachine().getCTEReflectors().getCTEReflector());
        enigmaMachine.setRotors(eng.getCTEMachine().getCTERotors().getCTERotor());

        int[] rotorsArrayId=copyRotorsID(eng.getCTEMachine().getCTERotors().getCTERotor());
        int[] notchArray=copyNotchArray(eng.getCTEMachine().getCTERotors().getCTERotor());
        MachineData=new MachineDataDTO1(eng.getCTEMachine().getCTEReflectors().getCTEReflector().size(),
                eng.getCTEMachine().getRotorsCount(),rotorsArrayId,notchArray);
    }

    private int[] copyRotorsID(List<CTERotor> rotorsArray )
    {
        int[] rotorsID=new int[rotorsArray.size()];
        for(int i=0;i<rotorsArray.size();i++)
        {
            rotorsID[i]=rotorsArray.get(i).getId();
        }
        return rotorsID;
    }

    private int[] copyNotchArray(List<CTERotor> rotorsArray )
    {
        int[] notchNumbers=new int[rotorsArray.size()];
        for(int i=0;i<rotorsArray.size();i++)
        {
            notchNumbers[i]=rotorsArray.get(i).getNotch();
        }
        return notchNumbers;
    }

    /*public int checkIfNumberOfRotorsValid(String numOfRotors) throws Exception {
        int numberOfRotors;
        try {


            numberOfRotors = Integer.parseInt(numOfRotors);
        }catch (Exception ex) {
            throw new Exception("The number you entered isn't integer. Please enter an integer number: ");
        }
        if(numberOfRotors>enigmaMachine.getNumberOfRotors() || numberOfRotors<1)
            throw  new Exception("Number of rotors need to be between 1 to "+ enigmaMachine.getNumberOfRotors() +" Please enter valid number of rotors. ");

        return numberOfRotors;
    }*/
}
