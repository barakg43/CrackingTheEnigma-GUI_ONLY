package enigmaMachine;

import enigmaMachine.parts.Keyboard;
import enigmaMachine.parts.Plugboard;
import enigmaMachine.parts.Reflector;
import enigmaMachine.parts.Rotor;
import jaxb.CTEPositioning;
import jaxb.CTEReflect;
import jaxb.CTEReflector;
import jaxb.CTERotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnigmaMachine implements Serializable {

    // from file
    private Reflector[] allReflectorsArray;
    private Rotor[] allRotorsArray;
    private int numberOfRotors;

    private int numOfRotorsInUse;
    private String alphabet;
    private final Plugboard plugBoardPairs;

    private Keyboard keyboard;
    public EnigmaMachine() {
        plugBoardPairs=new Plugboard();
    }
    public int getReflectorsNumber()
    {return allReflectorsArray.length;}
    public void setRotorsInUse(int rotorsInuse) {
        numOfRotorsInUse=rotorsInuse;
    }
    public int getRotorNumberInUse() {
        return numOfRotorsInUse;
    }

    public int getNumberOfRotorsInMachine()
    {return allRotorsArray.length;}
    public Reflector getReflectorById(int id)
    {
        if(id<1||id>allReflectorsArray.length)
            throw new RuntimeException("Invalid Reflector ID:"+id+".must be number between 1 and "+allReflectorsArray.length);
        return allReflectorsArray[id-1];
    }
    public Plugboard getPlugBoard() {
        return plugBoardPairs;
    }

    public int getNumberOfRotors() {
        return numberOfRotors;
    }


    public Rotor getRotorById(int id)
    {
        if(id<1||id>allRotorsArray.length)
            throw new RuntimeException("Invalid Rotor ID:"+id+".must be number between 1 and "+allRotorsArray.length);
        return allRotorsArray[id-1];
    }

    public void setAlphabet(String alphabet) {

        alphabet=alphabet.trim();
        keyboard=new Keyboard(alphabet.toUpperCase(),plugBoardPairs);
        this.alphabet=alphabet.toUpperCase();
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setRotors(List<CTERotor> RotorsArray) {
        numberOfRotors=RotorsArray.size();
        allRotorsArray = new Rotor[RotorsArray.size()];

        for (CTERotor rotor: RotorsArray) {
            if(rotor.getId()<=0)
                throw new RuntimeException("The id of rotor "+ rotor.getId() + " need to be bigger then zero." );
            if(rotor.getId()>RotorsArray.size())
                throw new RuntimeException("The id of rotor "+ rotor.getId() + " need to be smaller then "+ RotorsArray.size()+1+ " .");
            if(getRotorById(rotor.getId())!=null)
                throw new RuntimeException("The rotor id "+rotor.getId()+" appear more then once.");
            if(rotor.getNotch() >rotor.getCTEPositioning().size() || rotor.getNotch() <= 0)
                throw new RuntimeException("Rotor id "+ rotor.getId() +": notch number need to be between 1 to " + rotor.getCTEPositioning().size());
            allRotorsArray[rotor.getId()-1]=new Rotor(alphabet.length(),rotor.getNotch(),rotor.getId(),alphabet);
            setRotorTable(rotor.getCTEPositioning(), allRotorsArray[rotor.getId()-1]);
            if(!allRotorsArray[rotor.getId()-1].checkIfAllLetterMapped())
                throw new RuntimeException("Rotor id "+ rotor.getId() +":not all letter in alphabet are mapped in rotor table");
            if(rotor.getCTEPositioning().size()!=alphabet.length() )
                throw new RuntimeException("Rotor id "+ rotor.getId() +": the number of positions are not equal to the alphabet size.");


        }
    }

    private void setRotorTable(List<CTEPositioning> ctePos, Rotor rotor) {
        for(CTEPositioning pos:ctePos)
                rotor.addMapLetterToRotor(pos.getLeft().charAt(0), pos.getRight().charAt(0));

    }

    public void setReflectors(List<CTEReflector> ReflectorsArray) {
        allReflectorsArray =new Reflector[ReflectorsArray.size()];
        for(CTEReflector reflector : ReflectorsArray){
            int reflectorId=Reflector.convertRomanIdToNumber(reflector.getId());
            if(reflectorId==-1)
                throw new RuntimeException("reflector number "+ reflector.getId() + " don't exist");

            if(reflectorId <= 0 || reflectorId > ReflectorsArray.size())
                throw new RuntimeException("The reflector id " + reflectorId + " is not between 1 to " + ReflectorsArray.size());
            reflectorId--;
            if( allReflectorsArray[reflectorId]!=null) {
                allReflectorsArray=null;
                throw new RuntimeException("The reflector id "+(reflectorId+1)+" appear more then once.");
            }


            allReflectorsArray[reflectorId]= new Reflector(reflector.getCTEReflect().size()*2,reflector.getId());
           setReflectorArray(reflector.getCTEReflect(), allReflectorsArray[reflectorId]);
            if(reflector.getCTEReflect().size() *2 >alphabet.length() )
                throw new RuntimeException("in reflector number "+ reflector.getId() +" there are too many line input to line output mappings");
            if(reflector.getCTEReflect().size() *2 <alphabet.length() )
                throw new RuntimeException("in reflector number "+ reflector.getId() +" there are missing line input to line output mappings");
        }
    }
    public List<String> getReflectorIDList()
    {
        List<String> res =new ArrayList<>(allReflectorsArray.length);
        for(Reflector reflector:allReflectorsArray)
            res.add(reflector.getReflectorIdName());
        return res;
    }
        public void setReflectorArray(List<CTEReflect> reflectorsArray, Reflector reflector)
        {
           for(CTEReflect ref:reflectorsArray) {
               if(!reflectorsArray.contains(ref))
                   throw new RuntimeException("reflector "+ ref + " don't exist in the reflectors numbers.");
                reflector.addMappedInputOutput(ref.getInput()-1,ref.getOutput()-1);
            }

        }
    }

