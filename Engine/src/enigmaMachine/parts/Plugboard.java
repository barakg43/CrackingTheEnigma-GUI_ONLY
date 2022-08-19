package enigmaMachine.parts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Plugboard implements Serializable
{
    Map<Character,Character> plugMap;
    public Plugboard() {
        this.plugMap = new HashMap<>();
    }

    public Character getMappedOutput(Character input) {
        Optional<Character> output=Optional.ofNullable(plugMap.get(input));
        return output.orElse(input);
    }

    public void addMappedInputOutput(Character input,Character output) {

        if (plugMap.containsKey(input)) {
            plugMap.clear();
            throw new RuntimeException("You use the same character '" + input + "' in more than one mapping pair.");
        }
        if (plugMap.containsKey(output)) {
            plugMap.clear();
            throw new RuntimeException("You use the same character '" + output + "' in more than one mapping pair.");
        }
        if (input == output) {
            plugMap.clear();
            throw new RuntimeException("You map character '" + input + "' to itself.");
        }
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
