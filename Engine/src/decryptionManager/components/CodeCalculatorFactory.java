package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import dtoObjects.RotorInfoDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CodeCalculatorFactory implements Serializable {

    private Map<Character,Integer> letter2Index =null;
    private char[] index2letter =null;
    private int letterSize;
    private int numberOfPositions;
    final double MAX_VALUE_OFFSET;
    public CodeCalculatorFactory(String alphabet,int numberOfPositions) {
        addLettersToCal(alphabet);
        this.numberOfPositions=numberOfPositions;
        MAX_VALUE_OFFSET=Math.pow(letterSize,numberOfPositions);
    }
    public char getFirstLetter(){
        return index2letter[0];
    }
    public double getCodeConfAmount()
    {
        return MAX_VALUE_OFFSET;
    }
    public void addLettersToCal(String alphabet)
    {
        letter2Index=new HashMap<>();
        letterSize=alphabet.length();
        index2letter=new char[letterSize];
        for (int i = 0; i < letterSize; i++) {
            index2letter[i]=alphabet.charAt(i);
            letter2Index.put(alphabet.charAt(i),i);
        }
    }

    public CodeFormatDTO getNextCode(CodeFormatDTO currentCode)
    {
        return getNextCodeIndexOffset(currentCode, 1);
    }

    public CodeFormatDTO getNextCodeIndexOffset(CodeFormatDTO initialCode,int offset)//TODO:fix the calculation,for edge cae
    {
        RotorInfoDTO[] rotorInfoDTOS=initialCode.getRotorInfo();
        int codeOffset=offset+convertCodePositionToNumber(rotorInfoDTOS);

        if(codeOffset >=MAX_VALUE_OFFSET)
            return null;

        System.out.println("offset:"+codeOffset+" max offset:"+Math.pow(letterSize,rotorInfoDTOS.length));


//        System.out.println("after calc");

        for(int i=0;i<numberOfPositions;i++)
        {
            int innerIndex=codeOffset%letterSize;
            int distance=((rotorInfoDTOS[i].getDistanceToWindow()-offset) % letterSize + letterSize) % letterSize;
            rotorInfoDTOS[i]=new RotorInfoDTO(rotorInfoDTOS[i].getId(), distance, index2letter[innerIndex]);
            codeOffset=codeOffset/letterSize;
        }
//
//
//
////        System.out.println("number in lang:"+convertCodePositionToNumber(rotorInfoDTOS));
//
////        System.out.println("leftestRotorIndex:"+leftestRotorIndex);
//        I
//        int initialIndex = letter2Index.get(rotorInfoDTOS[leftestRotorIndex].getStatingLetter());
////        System.out.println("initialIndex:"+initialIndex);
//        int innerIndexLetter = initialIndex + offset %letterSize;
////        System.out.println("innerIndexLetter:"+innerIndexLetter);
//        leftestRotorIndex+=innerIndexLetter/letterSize;//for case innerIndexLetter overflow to next index position
//        if(leftestRotorIndex >=rotorInfoDTOS.length)
//            return null;
////        System.out.println("leftestRotorIndex2:"+leftestRotorIndex);
//        innerIndexLetter=innerIndexLetter%letterSize;//for case innerIndexLetter is greater then letterSize
////        System.out.println("innerIndexLetter2:"+innerIndexLetter);
//        //set all previous rotor to last letter in alphabet
//
//        rotorInfoDTOS[leftestRotorIndex]=new RotorInfoDTO(rotorInfoDTOS[leftestRotorIndex].getId(),
//                (rotorInfoDTOS[leftestRotorIndex].getDistanceToWindow()+offset)%letterSize,
//                index2letter[innerIndexLetter]);
        return new CodeFormatDTO(rotorInfoDTOS, initialCode.getReflectorID(), initialCode.getPlugboardPairDTOList());
    }
    private int convertCodePositionToNumber(RotorInfoDTO[] rotorInfoDTOS)
    {

        int numberInLetterSizeBase=0;
        for (int i = 0; i <numberOfPositions ; i++) {
            int currentIndex=letter2Index.get(rotorInfoDTOS[i].getStatingLetter());
            numberInLetterSizeBase+=currentIndex*Math.pow(letterSize,i);
//            System.out.println("current dig:"+currentIndex*Math.pow(letterSize,i)+" index "+currentIndex);
        }
        //System.out.println("number is:"+numberInLetterSizeBase);
        return numberInLetterSizeBase;
    }
    public double remainCodeConfTask(CodeFormatDTO initialCode) {
        return MAX_VALUE_OFFSET-convertCodePositionToNumber(initialCode.getRotorInfo());
    }


}
