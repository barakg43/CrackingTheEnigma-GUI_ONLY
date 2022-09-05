package decryptionManager.components;

import dtoObjects.CodeFormatDTO;
import dtoObjects.RotorInfoDTO;

import java.util.HashMap;
import java.util.Map;

public class CodeCalculatorFactory {

    private Map<Character,Integer> letter2Index =null;
    private char[] index2letter =null;

    public CodeCalculatorFactory(String alphabet) {
        addLettersToCal(alphabet);
    }

    private void addLettersToCal(String alphabet)
    {
        letter2Index=new HashMap<>();
        index2letter=new char[alphabet.length()];
        for (int i = 0; i < alphabet.length(); i++) {
            index2letter[i]=alphabet.charAt(i);
            letter2Index.put(alphabet.charAt(i),i);
        }
    }

    public CodeFormatDTO getNextCode(CodeFormatDTO currentCode)
    {
        return getNextCodeIndexOffset(currentCode, 1);
    }

    public CodeFormatDTO getNextCodeIndexOffset(CodeFormatDTO initialCode,int offset)
    {
        RotorInfoDTO[] rotorInfoDTOS=initialCode.getRotorInfo();
        int newIndex= offset /index2letter.length;
        if(newIndex>=rotorInfoDTOS.length)
            return null;
        RotorInfoDTO newRotorInfo=rotorInfoDTOS[offset /index2letter.length];
        int indexLetter = letter2Index.get(newRotorInfo.getStatingLetter());
        rotorInfoDTOS[newIndex]=new RotorInfoDTO(newRotorInfo.getId(), 0, index2letter[indexLetter + 1]);
        return new CodeFormatDTO(rotorInfoDTOS, initialCode.getReflectorID(), initialCode.getPlugboardPairDTOList());
    }

    public int remainCodeConfTask(CodeFormatDTO initialCode) {
        int remainTaskSize = 1;
        RotorInfoDTO[] rotorInfoDTOS = initialCode.getRotorInfo();
        for (RotorInfoDTO rotorInfoDTO : rotorInfoDTOS) {
            remainTaskSize += (index2letter.length - 1) - letter2Index.get(rotorInfoDTO.getStatingLetter());
        }
        return remainTaskSize;
    }


}
