package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import dtoObjects.RotorInfoDTO;

import java.util.HashMap;
import java.util.Map;

public class CodeCalculatorFactory {

    private Map<Character,Integer> letter2Index =null;
    private char[] index2letter =null;
    private int letterSize;
    public CodeCalculatorFactory(String alphabet) {
        addLettersToCal(alphabet);
    }

    private void addLettersToCal(String alphabet)
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
        int leftestRotorIndex = offset /letterSize;
        if(leftestRotorIndex >=rotorInfoDTOS.length)
            return null;

        int initialIndex = letter2Index.get(rotorInfoDTOS[leftestRotorIndex].getStatingLetter());
        int innerIndexLetter = initialIndex + offset %letterSize;
        leftestRotorIndex+=innerIndexLetter/letterSize;//for case innerIndexLetter overflow to next index position
        if(leftestRotorIndex >=rotorInfoDTOS.length)
            return null;

        innerIndexLetter=innerIndexLetter%letterSize;//for case innerIndexLetter is greater then letterSize
        //set all previous rotor to last letter in alphabet
        for(int i=0;i<leftestRotorIndex;i++)
            rotorInfoDTOS[i]=new RotorInfoDTO(rotorInfoDTOS[i].getId(),
                    (rotorInfoDTOS[i].getDistanceToWindow()+offset)%letterSize,
                        index2letter[letterSize-1]);
        rotorInfoDTOS[leftestRotorIndex]=new RotorInfoDTO(rotorInfoDTOS[leftestRotorIndex].getId(),
                (rotorInfoDTOS[leftestRotorIndex].getDistanceToWindow()+offset)%letterSize,
                index2letter[innerIndexLetter]);
        return new CodeFormatDTO(rotorInfoDTOS, initialCode.getReflectorID(), initialCode.getPlugboardPairDTOList());
    }

    public int remainCodeConfTask(CodeFormatDTO initialCode) {
        int remainTaskSize = 1;
        RotorInfoDTO[] rotorInfoDTOS = initialCode.getRotorInfo();
        for (RotorInfoDTO rotorInfoDTO : rotorInfoDTOS) {
            remainTaskSize += (letterSize- 1) - letter2Index.get(rotorInfoDTO.getStatingLetter());
        }
        return remainTaskSize;
    }


}
