package impl;



import java.util.HashMap;
import java.util.Map;


public class Roter {
    public static final int RIGHTSET_ROTOR=-1;
    //right side of the rotor
    private Map<Character, Integer> latter2IndexRightSide = null;
    private char[] index2latterRightSide = null;
    //left side of the rotor
    private Map<Character, Integer> latter2IndexLeftSide = null;
    private char[] index2latterLeftSide = null;
    private final int latterSize;
    private int currentMapSize = 0;
    private int windowPosition = 0;
    private int notchLocation;
    private int rotorID;
    boolean rightestRotor;
    public Roter(int latterSize, int notch, int id,boolean isRightestRotor) {
        this.latterSize = latterSize;
        notchLocation = notch;
        rotorID = id;
        rightestRotor=isRightestRotor;
        initArray();
    }

    private void initArray() {
        index2latterRightSide = new char[latterSize];
        index2latterLeftSide = new char[latterSize];
        latter2IndexLeftSide = new HashMap<>(latterSize);
        latter2IndexRightSide = new HashMap<>(latterSize);
        for (int i = 0; i < latterSize; i++) {
            index2latterLeftSide[i] = index2latterRightSide[i] = 0;
        }
    }

    public int getRotorID() {
        return rotorID;
    }

    public void addMapLatterToRotor(char leftLatter, char rightLatter) {
        if (currentMapSize > latterSize)
            throw new RuntimeException("overflow latter size,too many mapped latter!");
        if (latter2IndexLeftSide.containsKey(leftLatter))
            throw new RuntimeException(String.format("the latter %c is already in roter table with %c", leftLatter, index2latterRightSide[latter2IndexLeftSide.get(leftLatter)]));
        if (latter2IndexRightSide.containsKey(rightLatter))
            throw new RuntimeException(String.format("the latter %c is already in roter table with %c", rightLatter, index2latterLeftSide[latter2IndexRightSide.get(rightLatter)]));
        index2latterRightSide[currentMapSize] = rightLatter;
        index2latterLeftSide[currentMapSize] = leftLatter;

        latter2IndexLeftSide.put(leftLatter, currentMapSize);
        latter2IndexRightSide.put(rightLatter, currentMapSize);
        currentMapSize++;
    }


    int gerRelativIndex(int index,boolean isSubtractWindowPosition)
    {
        if(isSubtractWindowPosition)
           return (index - windowPosition) % latterSize;
        else

    }
    private void forwardWindow(int previousNotch, int relativeIndex)
    {
        if(previousNotch==RIGHTSET_ROTOR||relativeIndex==RIGHTSET_ROTOR) {
            windowPosition++;
            windowPosition = windowPosition % latterSize;
        }
    }
    public int getOutputMapIndex(int inputIndex,boolean isInputFromLeft)
    {
        char inputLatter;
        int relativeIndex=(inputIndex + windowPosition) % latterSize;
        if(isInputFromLeft) {
            inputLatter =index2latterLeftSide[relativeIndex];//translate the input index to latter in left side
            return (latter2IndexRightSide.get(inputLatter)-windowPosition)%latterSize;
        }
        else
        {
            inputLatter =index2latterRightSide[relativeIndex];//translate the input index to latter in right side
            return (latter2IndexLeftSide.get(inputLatter)-windowLocation)%latterSize;
        }
    }
    public boolean checkIfAllLatterMapped()
    {
        return latter2IndexLeftSide.size()==latterSize&&latter2IndexRightSide.size()==latterSize;
    }

}
