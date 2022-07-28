package impl;

public class Reflector {

    private final int[] mappedReflectorsArray;
    private final int lattersSize;
    public Reflector(int lattersSize)
    {
        mappedReflectorsArray=new int[lattersSize];
        this.lattersSize=lattersSize;
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
