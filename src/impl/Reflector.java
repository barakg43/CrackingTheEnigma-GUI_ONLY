package impl;

import EnigmaMachine.Mapper;

public class Reflector implements Mapper<Integer> {

    private final int[] mappedReflectorsArray;
    private final int lettersSize;
    public Reflector(int latterSize)
    {
        mappedReflectorsArray=new int[latterSize];
        this.lettersSize =latterSize;
    }
    @Override
    public Integer getMappedOutput(Integer input) {
        if(input>= lettersSize ||input<0)
            throw new RuntimeException("invalid input,out of bound reflector id");

        return mappedReflectorsArray[input];
    }

    public void addMappedInputOutput(Integer input, Integer output)
    {
        input--;
        output--;
        if(input.equals(output))
            throw new RuntimeException("cant mapped input to himself!");
        if(input<0||input> lettersSize ||output<0||output> lettersSize)
            throw new RuntimeException("invalid input mapped reflection-out of bounds");

        mappedReflectorsArray[input]=output;
        mappedReflectorsArray[output]=input;
    }

}
