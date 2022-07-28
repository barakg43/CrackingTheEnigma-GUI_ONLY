package impl;



import java.util.HashMap;
import java.util.Map;


public class Roter {

    //right side of the rotor
    private Map<Character,Integer> latter2IndexRightSide=null;
    private char[] index2latterRightSide=null;
    //left side of the rotor
    private Map<Character,Integer> latter2IndexLeftSide=null;
    private char[] index2latterLeftSide=null;
    private final int latterSize;
    private int currentMapSize=0;
    private int windowLocation=0;
    private int notchLocation;
    public Roter(int latterSize)
    {
       this.latterSize=latterSize;
        initArray();
    }
    private void initArray()
    {
        index2latterRightSide=new char[latterSize];
        index2latterLeftSide=new char[latterSize];
        latter2IndexLeftSide=new HashMap<>(latterSize);
        latter2IndexRightSide=new HashMap<>(latterSize);
        for (int i = 0; i < latterSize; i++) {
            index2latterLeftSide[i]=index2latterRightSide[i]=0;
        }
    }
    public void addMapLatterToRotor(char leftLatter,char rightLatter)
    {
        if(currentMapSize>latterSize)
            throw new RuntimeException("overflow latter size,too many mapped latter!");
        if(latter2IndexLeftSide.containsKey(leftLatter))
            throw new RuntimeException(String.format("the latter %c is already in roter table with %c",leftLatter,index2latterRightSide[latter2IndexLeftSide.get(leftLatter)]));
        if(latter2IndexRightSide.containsKey(rightLatter))
            throw new RuntimeException(String.format("the latter %c is already in roter table with %c",rightLatter,index2latterLeftSide[latter2IndexRightSide.get(rightLatter)]));
        index2latterRightSide[currentMapSize]=rightLatter;
        index2latterLeftSide[currentMapSize]=leftLatter;

        latter2IndexLeftSide.put(leftLatter,currentMapSize);
        latter2IndexRightSide.put(rightLatter,currentMapSize);
        currentMapSize++;
    }

    public int getOutputMapIndex(int inputIndex,boolean isInputFromLeft)
    {
        char inputLatter;
        if(isInputFromLeft) {
            inputLatter =index2latterLeftSide[inputIndex];//translate the input index to latter in left side
            return latter2IndexRightSide.get(inputLatter);
        }
        else
        {
            inputLatter =index2latterRightSide[inputIndex];//translate the input index to latter in right side
            return latter2IndexLeftSide.get(inputLatter);
        }
    }
    public boolean checkIfAllLatterMapped()
    {
        return latter2IndexLeftSide.size()==latterSize&&latter2IndexRightSide.size()==latterSize;
    }

}
