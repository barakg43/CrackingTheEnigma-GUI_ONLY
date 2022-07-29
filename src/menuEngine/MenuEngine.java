package menuEngine;

import EnigmaMachine.enigmaMachine;
import impl.Reflector;
import impl.Roter;
import impl.reflectorId;
import jaxb.schema.generated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuEngine {

    private enigmaMachine enigmaMachine;

    public MenuEngine()
    {
        enigmaMachine=new enigmaMachine();
    }
    private final static String JAXB_XML_PACKAGE_NAME = "jaxb.schema.generated";
    public void LoadXMLFile(String filePath) // TODO: copy all classes
    {
        File file = new File(filePath);
        if(!(filePath.toLowerCase().endsWith(".xml")))
            throw new RuntimeException("The file you entered isn't XML file.");
        else if(!file.exists())
            throw new RuntimeException("The file you entered isn't exists.");

        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
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

    private void copyAllData(CTEEnigma eng) throws Exception {
        String alphabet=eng.getCTEMachine().getABC().replaceAll("\n","");
        alphabet=alphabet.replaceAll("\t","");
        if(alphabet.length()%2!=0)
            throw new RuntimeException("The number of letters need to be even.\nPlease correct this.");
        enigmaMachine.setAlphabet(eng.getCTEMachine().getABC());
        if(eng.getCTEMachine().getRotorsCount()<2)
            throw new RuntimeException("The number of rotors need to be bigger or equal to 2.\nPlease correct this.");
        if(eng.getCTEMachine().getRotorsCount()>eng.getCTEMachine().getCTERotors().getCTERotor().size())
            throw new RuntimeException("The number of rotors that used is greater than the number of rotors given with the machine.\nPlease correct this.");
        enigmaMachine.setReflectors(eng.getCTEMachine().getCTEReflectors().getCTEReflector());
        enigmaMachine.setRotors(eng.getCTEMachine().getCTERotors().getCTERotor());

    }

    public void checkValidationAndSaveData(int numberOfRotors,String allRotors,String rotorsPositions) throws Exception {
        if(numberOfRotors > enigmaMachine.getNumberOfRotors() || numberOfRotors<1)
            throw new Exception("Please choose number of rotors between 1 to "+ enigmaMachine.getNumberOfRotors());


    }

    public int checkIfNumberOfRotorsValid(String numOfRotors) throws Exception {
        int numberOfRotors;
        try {
             numberOfRotors = Integer.parseInt(numOfRotors);
        }catch (Exception ex) {
            throw new Exception("The number you entered isn't integer. Please enter an integer number: ");
        }
        if(numberOfRotors>enigmaMachine.getNumberOfRotors() || numberOfRotors<1)
            throw  new Exception("Number of rotors need to be between 1 to "+ enigmaMachine.getNumberOfRotors() +" Please enter valid number of rotors. ");

        return numberOfRotors;
    }

    public void checkIfRotorsValid(String rotors,int numOfRotors) throws Exception {
        List<String> ArrayString = Arrays.asList(rotors.split(","));
        Roter[] selectedRotors=new Roter[numOfRotors];
        int i=0;
        int rotorNum;
        if(ArrayString.size()!=numOfRotors)
            throw new Exception("You need to enter "+ numOfRotors+ " rotors with comma between them.");

        for (String st : ArrayString) {
                try {
                    rotorNum = Integer.parseInt(st);
                } catch (Exception ex) {
                    throw new Exception("The number you entered isn't integer. Please enter an integer number: ");
                }
                if(rotorNum > enigmaMachine.getNumberOfRotors() ||rotorNum < 1)
                    throw new Exception("There is no such rotors, please select again valid rotors");
                selectedRotors[i]=findRotor(rotorNum);
                 i++;
                }

        enigmaMachine.setSelectedRotors(selectedRotors);
    }

    public void checkIfPositionsValid(String positions, int numberOfRotors) throws Exception {
        char[] positionsList = new char[numberOfRotors];
        int i=0;
        if(positions.length()!=numberOfRotors)
            throw new Exception("You need to give position for each rotor.");
        for (char ch : positions.toCharArray()) {
            if(enigmaMachine.getAlphabet().indexOf(ch)==-1)
                throw new Exception("This position is not exist in the machine. Please enter valid positions:");
            positionsList[i]=ch;
            i++;
        }
        enigmaMachine.setSelectedPositions(positionsList);
    }

    public void checkIfReflectorNumValid(String ReflectorNum) throws Exception {
        int refNum;
        try {
            refNum = Integer.parseInt(ReflectorNum);
        } catch (Exception ex) {
            throw new Exception("The number you entered isn't integer. Please enter an integer number: ");
        }
        if(!reflectorId.isExist(refNum))
            throw new Exception("Please choose one of the options 1-5 ");
        enigmaMachine.setSelectedReflector(findReflector(refNum));

    }



    private Roter findRotor(int rotorNum)
    {
        Roter[] allRotors= enigmaMachine.getAllRotorsArray();
        for (Roter rotor:allRotors) {
            if(rotor.getRotorID()==rotorNum)
                return rotor;
        }
        return null;
    }
    private Reflector findReflector(int reflectorNum)
    {
        Reflector[] reflector=enigmaMachine.getAllReflectors();
        for (Reflector ref:reflector) {
            if(ref.getReflectorId().ordinal()==reflectorNum)
                return ref;
        }
        return null;
    }
}
