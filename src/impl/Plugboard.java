package impl;

import EnigmaMachine.Mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Plugboard implements Mapper<Character>
{
    Map<Character,Character> plugMap;
    public Plugboard() {
        this.plugMap = new HashMap<>();
    }
    @Override
    public Character getMappedOutput(Character input) {
        Optional<Character> output=Optional.ofNullable(plugMap.get(input));
        return output.orElse(input);
    }
    @Override
    public void addMappedInputOutput(Character input,Character output) {
        plugMap.put(input,output);
        plugMap.put(output,input);
    }
}
