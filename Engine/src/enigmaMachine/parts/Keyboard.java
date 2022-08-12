package enigmaMachine.parts;

import java.io.Serializable;
import java.util.*;

public class Keyboard implements Serializable {
    private final Map<Character,Integer> keyboard2inputRow;
    private final char[] input2keyboard;
    private final Plugboard plugboard;
    private final String alphabetString;

    public Keyboard(String alphabet,Plugboard plugboard) {
        alphabetString=alphabet.toUpperCase();
        this.keyboard2inputRow = new HashMap<>();
        input2keyboard=new char[alphabetString.length()];
        this.plugboard=plugboard;
        initKeyboard(alphabetString);
    }
    private void initKeyboard(String alphabet)
    {
        for(int i=0;i<alphabet.length();i++) {
            addMappedInputOutput(alphabet.charAt(i), i);
        }

    }

    @Override
    public String toString() {
        return "Keyboard{" +
                "keyboard2inputRow=" + keyboard2inputRow +
                ", input2keyboard=" + Arrays.toString(input2keyboard) +
                ", plugboard=" + plugboard +
                '}';
    }
    /**
     * *the function check if all letter in string 'data' are valid in this machine
     * and return runtime exception if found invalid letter in input
     * @param data - the input line of letter in enigma ABC
     * @throws RuntimeException if data input contain invalid character
     */
    public void checkValidInput(String data)
    {
        for (int i = 0; i < data.length(); i++) {
            if(!checkIfLetterValid(data.charAt(i)))
                throw new RuntimeException("the latter: "+data.charAt(i)+" isn't valid letter in this enigma machine\nplease choose letter from\n"+alphabetString);
        }
    }
    public boolean checkIfLetterValid(Character latter)
    {
        return keyboard2inputRow.containsKey(latter);
    }
    public char getLetterInIndex(int index)
    {
        return input2keyboard[index];
    }
    /**
     * *the function return the index of character in alphabet language over enigma machine
     * and return runtime exception if found invalid letter in input
     * @param input - the input character in keyboard
     * @throws RuntimeException if character not found in ABC
     */
    public int getMappedOutput(char input) {
        if(!checkIfLetterValid(input))
            throw new RuntimeException("the latter: "+input+" isn't valid letter in this enigma machine\nplease choose letter from\n"+alphabetString);
        return keyboard2inputRow.get(plugboard.getMappedOutput(input));
    }
    public char getLetterFromRowNumber(int input) {
        return plugboard.getMappedOutput(getLetterInIndex(input));
    }


    private void addMappedInputOutput(Character input, Integer output) {
        if(keyboard2inputRow.containsKey(input))
            throw new RuntimeException("keyboard : the "+ input.toString()+ " appears more than once.");
        keyboard2inputRow.put(input,output);
        input2keyboard[output]=input;

    }
}
