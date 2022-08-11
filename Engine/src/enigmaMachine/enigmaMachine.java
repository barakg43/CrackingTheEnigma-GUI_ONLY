package enigmaMachine;

import enigmaMachine.parts.*;

import jaxb.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class enigmaMachine implements Serializable {

    // from file
    private Reflector[] allReflectorsArray;
    private Rotor[] allRotorsArray;
    private int numberOfRotors;

    private int numOfRotorsInUse;
    private String alphabet;
    private final Plugboard plugBoardPairs;

    private Keyboard keyboard;



    //selected by user
//    private Reflector selectedReflector;
//    private Rotor[] selectedRotors;
//    private char[] selectedPositions;


    public enigmaMachine() {
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
//    public Reflector[] getAllReflectors() {
//        return allReflectorsArray;
//    }
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

//    public void setSelectedPositions(char[] selectedPositions) {
//        this.selectedPositions = selectedPositions;}
//    public char[] getSelectedPositions() {
//        return selectedPositions;
//    }

//    public Rotor[] getAllRotorsArray(){
//        return AllRotorsArray;
//    }
    public Rotor getRotorById(int id)
    {
        if(id<1||id>allRotorsArray.length)
            throw new RuntimeException("Invalid Rotor ID:"+id+".must be number between 1 and "+allRotorsArray.length);
        return allRotorsArray[id-1];
    }

    public void setAlphabet(String alphabet) {
        alphabet=alphabet.replaceAll("\n","");
        alphabet=alphabet.replaceAll("\t","");
        keyboard=new Keyboard(alphabet,plugBoardPairs);
        this.alphabet=alphabet;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public String getAlphabet() {
        return alphabet;
    }
//    public void setSelectedReflector(Reflector selectedRef) {
//        selectedReflector=selectedRef;
//    }





//    public Reflector getSelectedReflector() {
//        return selectedReflector;
//    }
//    public Rotor[] getSelectedRotors() {
//        return selectedRotors;
//    }

//    public void setSelectedRotors(Rotor[] selectedRotorsArray) {
//        selectedRotors=selectedRotorsArray;
//    }

    public void setRotors(List<CTERotor> RotorsArray) {
        numberOfRotors=RotorsArray.size();
        allRotorsArray = new Rotor[RotorsArray.size()];
        for (CTERotor rotor: RotorsArray) {
            if(rotor.getId()<=0)
                throw new RuntimeException("rotor number "+ rotor.getId() + " need to be bigger then zero." );
            if(allRotorsArray[rotor.getId()-1]!=null)
                throw new RuntimeException("There are 2 rotors with same id.\nplease correct this.");
            if(rotor.getNotch() >rotor.getCTEPositioning().size() || rotor.getNotch() <= 0)
                throw new RuntimeException("Notch number of rotor: "+rotor.getId()+  " need to be smaller than " + (rotor.getCTEPositioning().size()+1) + " and bigger then 0" +"\nPlease correct this.");
            allRotorsArray[rotor.getId()-1]=new Rotor(alphabet.length(),rotor.getNotch(),rotor.getId());
            setRotorTable(rotor.getCTEPositioning(), allRotorsArray[rotor.getId()-1]);

        }
    }

    private void setRotorTable(List<CTEPositioning> ctePos,Rotor rotor) {
        for(CTEPositioning pos:ctePos)
            rotor.addMapLatterToRotor(pos.getLeft().charAt(0),pos.getRight().charAt(0));
    }

    public void setReflectors(List<CTEReflector> ReflectorsArray) {
        allReflectorsArray =new Reflector[ReflectorsArray.size()];
        for(CTEReflector reflector : ReflectorsArray){
            int reflectorId=Reflector.convertRomanIdToNumber(reflector.getId())-1;
            if( allReflectorsArray[reflectorId]!=null)
            {   allReflectorsArray=null;
                throw new RuntimeException("There are 2 reflectors with same id.\nplease correct this.");}
            allReflectorsArray[reflectorId]= new Reflector(reflector.getCTEReflect().size()*2,reflector.getId());
           setReflectorArray(reflector.getCTEReflect(), allReflectorsArray[reflectorId]);
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
                reflector.addMappedInputOutput(ref.getInput()-1,ref.getOutput()-1);
            }

        }
    }

