package enigmaMachine.parts;
import enigmaMachine.Mapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Plugboard implements Mapper<Character,Character>, Serializable
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

        if (plugMap.containsKey(input))
            throw new RuntimeException("You use the same character '" + input + "' in more than one mapping pair. Please check this.");
        if (input == output)
            throw new RuntimeException("You map character '" + input + "' to itself. Please change this mapping.");
        plugMap.put(input,output);
        plugMap.put(output,input);
    }

    @Override
    public String toString() {
        return "Plugboard{" +
                "plugMap=" + plugMap +
                '}';
    }

    public  Map<Character,Character> getPlugBoardPairs() {
        return plugMap;
    }
    public void resetPlugBoardPairs()
    {
        plugMap=new HashMap<>();
    }



}
