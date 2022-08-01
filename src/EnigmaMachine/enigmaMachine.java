package EnigmaMachine;

import impl.*;
import jaxb.schema.generated.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class enigmaMachine {

    // from file
    private Reflector[] AllReflectorsArray;
    private Roter[] AllRotorsArray;
    private int numberOfRotors;
    private int numOfRotorsInUse;
    private String alphabet;
    private Map<Character, Character> plugBoardPairs;




    //selected by user
    private Reflector selectedReflector;
    private Roter[] selectedRotors;
    private char[] selectedPositions;


    public void enigmaMachine() {
        plugBoardPairs=new HashMap<>();
        for(int i=0;i<alphabet.length();i++)
        {
            plugBoardPairs.put(alphabet.charAt(i),alphabet.charAt(i));
        }
    }

    public void setRotorsInUse(int rotorsInuse) {
        numOfRotorsInUse=rotorsInuse;
    }
    public int getRotorsInUse() {
        return numOfRotorsInUse;
    }
    public Reflector[] getAllReflectors() {
        return AllReflectorsArray;
    }

    public int getNumberOfRotors() {
        return numberOfRotors;
    }
    public void setSelectedPositions(char[] selectedPositions) {
        this.selectedPositions = selectedPositions;}
    public char[] getSelectedPositions() {
        return selectedPositions;
    }

    public Roter[] getAllRotorsArray(){
        return AllRotorsArray;
    }

    public void setAlphabet(String Alphabet) {
        Alphabet.replaceAll(" ","");
        alphabet=Alphabet;
    }

    public String getAlphabet() {
        return alphabet;
    }
    public void setSelectedReflector(Reflector selectedRef) {
        selectedReflector=selectedRef;
    }

    public void setPlugBoard(Map<Character, Character> PlugBoardPairs) {
        plugBoardPairs=PlugBoardPairs;
    }

    public Map<Character, Character> getPlugBoard(){
        return plugBoardPairs;
    }

    public Reflector getSelectedReflector() {
        return selectedReflector;
    }
    public Roter[] getSelectedRotors() {
        return selectedRotors;
    }

    public void setSelectedRotors(Roter[] selectedRotorsArray) {
        selectedRotors=selectedRotorsArray;
    }

    public void setRotors(List<CTERotor> RotorsArray) throws Exception {
        numberOfRotors=RotorsArray.size();
        AllRotorsArray = new Roter[RotorsArray.size()];
        for (CTERotor rotor: RotorsArray) {
            if(AllRotorsArray[rotor.getId()-1]!=null)
                throw new Exception("There are 2 rotors with same id.\nplease correct this.");
            if(rotor.getNotch() >rotor.getCTEPositioning().size() || rotor.getNotch() < 0)
                throw new Exception("Notch number of rotor: "+rotor.getId()+  " need to be smaller than " + (rotor.getCTEPositioning().size()+1) + " and bigger then 0" +"\nPlease correct this.");
            AllRotorsArray[rotor.getId()-1]=new Roter(alphabet.length(),rotor.getNotch(),rotor.getId()); // check about latterSize
            AllRotorsArray[rotor.getId()-1].setRightSide(rotor.getCTEPositioning());
            AllRotorsArray[rotor.getId()-1].setLeftSide(rotor.getCTEPositioning());

        }
    }

    public void setReflectors(List<CTEReflector> ReflectorsArray) throws Exception {
        AllReflectorsArray=new Reflector[ReflectorsArray.size()];
        for(CTEReflector reflector : ReflectorsArray){

            int reflectorId=(impl.reflectorId.valueOf(reflector.getId()).ordinal());

            if( AllReflectorsArray[reflectorId]!=null)
                throw new Exception("There are 2 reflectors with same id.\nplease correct this.");

            AllReflectorsArray[reflectorId]= new Reflector(reflector.getCTEReflect().size()*2,reflector.getId());
            AllReflectorsArray[reflectorId].setReflectorArray(reflector.getCTEReflect(),reflector.getId());
        }
    }
}
