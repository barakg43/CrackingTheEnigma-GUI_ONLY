package impl;

import EnigmaMachine.Mapper;

public class Reflector implements Mapper<Integer,Integer> {
    private final reflectorId reflectorIdNum ;
    private final int[] mappedReflectorsArray;
    private final int lettersSize;
    private final int NOT_INIT=-1;
    public Reflector(int lettersSize,String refID)
    {
        mappedReflectorsArray=new int[lettersSize];
        this.lettersSize=lettersSize;
        reflectorIdNum = reflectorId.valueOf(refID);
        initArray();
    }
    private void  initArray()
    {
        for (int i = 0; i < lettersSize; i++) {
            mappedReflectorsArray[i]=NOT_INIT;
        }
    }
    public reflectorId getReflectorId()
    {
        return reflectorIdNum;
    }

    @Override
    public Integer getMappedOutput(Integer input) {
        if(input>= lettersSize ||input<0)
            throw new RuntimeException("invalid input,out of bound reflector id");

        return mappedReflectorsArray[input];
    }

    public void addMappedInputOutput(Integer input, Integer output)
    {

        if(input.equals(output))
            throw new RuntimeException("cant mapped input to himself!");
        if(input<0||input> lettersSize ||output<0||output> lettersSize)
            throw new RuntimeException("invalid input mapped reflection-out of bounds");
        if(mappedReflectorsArray[output]!=NOT_INIT)
                throw new RuntimeException("In reflector No '"+ reflectorIdNum +"' the output "  + (output+1) +" appears more than once.");
        if(mappedReflectorsArray[input]!=NOT_INIT)
            throw new RuntimeException("In reflector No '"+ reflectorIdNum +"' the input "  + (input+1) +" appears more than once.");

        mappedReflectorsArray[input]=output;
        mappedReflectorsArray[output]=input;
    }

}
