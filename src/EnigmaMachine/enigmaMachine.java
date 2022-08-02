package EnigmaMachine;

import impl.*;
import jaxb.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class enigmaMachine {

    // from file
    private Reflector[] AllReflectorsArray;
    private Rotor[] AllRotorsArray;
    private int numberOfRotors;
    private int numOfRotorsInUse;
    private String alphabet;
    private final Plugboard plugBoardPairs;

    private Keyboard keyboard;



    //selected by user
    private Reflector selectedReflector;
    private Rotor[] selectedRotors;
    private char[] selectedPositions;


    public enigmaMachine() {
        plugBoardPairs=new Plugboard();
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

    public Plugboard getPlugBoard() {
        return plugBoardPairs;
    }

    public int getNumberOfRotors() {
        return numberOfRotors;
    }
    public void setSelectedPositions(char[] selectedPositions) {
        this.selectedPositions = selectedPositions;}
    public char[] getSelectedPositions() {
        return selectedPositions;
    }

    public Rotor[] getAllRotorsArray(){
        return AllRotorsArray;
    }

    public void setAlphabet(String alphabet) {
        alphabet=alphabet.replaceAll(" ","");
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
    public void setSelectedReflector(Reflector selectedRef) {
        selectedReflector=selectedRef;
    }





    public Reflector getSelectedReflector() {
        return selectedReflector;
    }
    public Rotor[] getSelectedRotors() {
        return selectedRotors;
    }

    public void setSelectedRotors(Rotor[] selectedRotorsArray) {
        selectedRotors=selectedRotorsArray;
    }

    public void setRotors(List<CTERotor> RotorsArray) {
        numberOfRotors=RotorsArray.size();
        AllRotorsArray = new Rotor[RotorsArray.size()];
        for (CTERotor rotor: RotorsArray) {
            if(AllRotorsArray[rotor.getId()-1]!=null)
                throw new RuntimeException("There are 2 rotors with same id.\nplease correct this.");
            if(rotor.getNotch() >rotor.getCTEPositioning().size() || rotor.getNotch() < 0)
                throw new RuntimeException("Notch number of rotor: "+rotor.getId()+  " need to be smaller than " + (rotor.getCTEPositioning().size()+1) + " and bigger then 0" +"\nPlease correct this.");
            AllRotorsArray[rotor.getId()-1]=new Rotor(alphabet.length(),rotor.getNotch(),rotor.getId());
            setRotorTable(rotor.getCTEPositioning(), AllRotorsArray[rotor.getId()-1]);

        }
    }

    private void setRotorTable(List<CTEPositioning> ctePos,Rotor rotor) {
        for(CTEPositioning pos:ctePos)
            rotor.addMapLatterToRotor(pos.getLeft().charAt(0),pos.getRight().charAt(0));
    }

    public void setReflectors(List<CTEReflector> ReflectorsArray) {
        AllReflectorsArray=new Reflector[ReflectorsArray.size()];
        for(CTEReflector reflector : ReflectorsArray){
            int reflectorId=(impl.reflectorId.valueOf(reflector.getId()).ordinal());
            if( AllReflectorsArray[reflectorId]!=null)
                throw new RuntimeException("There are 2 reflectors with same id.\nplease correct this.");

            AllReflectorsArray[reflectorId]= new Reflector(reflector.getCTEReflect().size()*2,reflector.getId());
           setReflectorArray(reflector.getCTEReflect(), AllReflectorsArray[reflectorId]);
        }
    }
        public void setReflectorArray(List<CTEReflect> reflectorsArray, Reflector reflector)
        {
           for(CTEReflect ref:reflectorsArray) {
                reflector.addMappedInputOutput(ref.getInput()-1,ref.getOutput()-1);
            }

        }
    }

