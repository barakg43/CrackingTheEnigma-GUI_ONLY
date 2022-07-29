package impl;

import jaxb.schema.generated.CTEReflect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reflector {

    private final static reflectorId reflectorIdNum = null;
    private final int[] mappedReflectorsArray;
    private final int lattersSize;
    public Reflector(int lattersSize)
    {
        mappedReflectorsArray=new int[lattersSize];
        this.lattersSize=lattersSize;
    }
    public reflectorId getReflectorId()
    {
        return reflectorIdNum;
    }

    public int[] getMappedReflectorsArray() {
        return mappedReflectorsArray;
    }

    public void setReflectorArray(List<CTEReflect> reflectorsArray, String reflectorNumber)
    {
        int i;
        List<Integer> inputlist=new ArrayList<>();
        List<Integer> outputlist=new ArrayList<>();
        int inputNumber,outputNumber;
        for(i=0;i<reflectorsArray.size();i++) {
             inputNumber=reflectorsArray.get(i).getInput()-1;
             outputNumber=reflectorsArray.get(i).getOutput()-1;

            if(inputNumber==outputNumber)
                throw new RuntimeException("In reflector No '"+ reflectorNumber +"'  there is reflect that mapped to itself.\nplease check that every reflect mapped to a different reflect.");
            if(outputlist.contains(outputNumber))
                throw new RuntimeException("In reflector No '"+ reflectorNumber +"' the output "  + (outputNumber+1) +" appears more than once.");
            if(inputlist.contains(inputNumber))
                throw new RuntimeException("In reflector No '"+ reflectorNumber +"' the input "  + (inputNumber+1) +" appears more than once.");

            inputlist.add(inputNumber);
            outputlist.add(outputNumber);
            mappedReflectorsArray[inputNumber]= outputNumber;
        }
        for(i=0;i<reflectorsArray.size();i++) {
             inputNumber=reflectorsArray.get(i).getInput()-1;
             outputNumber=reflectorsArray.get(i).getOutput()-1;
            mappedReflectorsArray[outputNumber]= inputNumber;
        }
    }

    public int getOtherSideOfReflector(int input)
    {
        if(input>=lattersSize||input<0)
            throw new RuntimeException("invalid input,out of bound reflector id");

        return mappedReflectorsArray[input];
    }
    public void addMappedReflection(int input,int output)
    {
        if(input==output)
            throw new RuntimeException("cant mapped input to himself!");
        if(input<0||input>lattersSize||output<0||output>lattersSize)
            throw new RuntimeException("invalid input mapped reflection-out of bounds");

        mappedReflectorsArray[input]=output;
        mappedReflectorsArray[output]=input;
    }

}
