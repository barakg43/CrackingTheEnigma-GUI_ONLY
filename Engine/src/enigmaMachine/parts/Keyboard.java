package enigmaMachine.parts;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Keyboard implements Serializable {
    private final Map<Character,Integer> keyboard2inputRow;
    private final char[] input2keyboard;
    private final Plugboard plugboard;

    public Keyboard(String alphabet,Plugboard plugboard) {
        this.keyboard2inputRow = new HashMap<>();
        input2keyboard=new char[alphabet.length()];
        this.plugboard=plugboard;
        initKeyboard(alphabet);
    }
    private void initKeyboard(String alphabet)
    {
        for(int i=0;i<alphabet.length();i++)
            addMappedInputOutput(alphabet.charAt(i),i);

    }

    @Override
    public String toString() {
        return "Keyboard{" +
                "keyboard2inputRow=" + keyboard2inputRow +
                ", input2keyboard=" + Arrays.toString(input2keyboard) +
                ", plugboard=" + plugboard +
                '}';
    }

    public boolean checkValidInput(String data)
    {

        for (int i = 0; i < data.length(); i++) {
            if(!keyboard2inputRow.containsKey(data.charAt(i)))
                return false;

        }
        return true;
    }

    public Integer getMappedOutput(Character input) {
        if(!keyboard2inputRow.containsKey(input))
            throw new RuntimeException("the "+input+ " not existed in alphabet.");
        return keyboard2inputRow.get(plugboard.getMappedOutput(input));
    }
    public Character getLetterFromRowNumber(Integer input) {
        return plugboard.getMappedOutput(input2keyboard[input]);
    }


    private void addMappedInputOutput(Character input, Integer output) {
        if(keyboard2inputRow.containsKey(input))
            throw new RuntimeException("keyboard : the "+ input.toString()+ " appears more than once.");
        keyboard2inputRow.put(input,output);
        input2keyboard[output]=input;

    }
}
