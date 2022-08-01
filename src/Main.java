import EnigmaMachine.Mapper;
import impl.Plugboard;
import impl.Reflector;
import impl.Rotor;
import impl.RotorOutputData;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
       // <CTE-Rotor id="1" notch="4">
//				<CTE-Positioning left="A" right="F"/>
//				<CTE-Positioning left="B" right="E"/>
//				<CTE-Positioning left="C" right="D"/>
//				<CTE-Positioning left="D" right="C"/>
//				<CTE-Positioning left="E" right="B"/>
//				<CTE-Positioning left="F" right="A"/>
        Rotor rotor1=new Rotor(6,4,1);
        rotor1.addMapLatterToRotor('F','A');
        rotor1.addMapLatterToRotor('E','B');
        rotor1.addMapLatterToRotor('D','C');
        rotor1.addMapLatterToRotor('C','D');
        rotor1.addMapLatterToRotor('B','E');
        rotor1.addMapLatterToRotor('A','F');
        Rotor rotor2=new Rotor(6,1,2);
        rotor2.addMapLatterToRotor('E','A');
        rotor2.addMapLatterToRotor('B','B');
        rotor2.addMapLatterToRotor('D','C');
        rotor2.addMapLatterToRotor('F','D');
        rotor2.addMapLatterToRotor('C','E');
        rotor2.addMapLatterToRotor('A','F');

        //test the example in the word   <F|A><I><CC><2,1>
        char[] latter={'A','B','C','D','E','F'};
        Map<Character,Integer> latterToIndex=new HashMap<>();
        for (int i = 0; i < latter.length ; i++) {
                latterToIndex.put(latter[i],i);
        }
        String input="AABBCCDDEEFF";
        StringBuilder output= new StringBuilder();
        Reflector reflector1=new Reflector(6, "I");
        Mapper<Character, Character> plugborad=new Plugboard();
        plugborad.addMappedInputOutput('F','A');
        reflector1.addMappedInputOutput(1,4);
        reflector1.addMappedInputOutput(2,5);
        reflector1.addMappedInputOutput(3,6);
        rotor1.setInitialWindowPosition('C');
        rotor2.setInitialWindowPosition('C');
       // rotor1.printRotorTableDebugModeOnly();
        //rotor2.printRotorTableDebugModeOnly();
        int refOut;
        char afterBoard;
            for (int i = 0; i < input.length(); i++) {
            afterBoard=(char)plugborad.getMappedOutput(input.charAt(i));
            RotorOutputData r1=rotor1.getOutputMapIndex(latterToIndex.get(afterBoard),false,true);
            RotorOutputData r2=rotor2.getOutputMapIndex(r1.getOutputIndex(),false,r1.isAdvanceNextRotor());
            refOut=reflector1.getMappedOutput(r2.getOutputIndex());
            r2=rotor2.getOutputMapIndex(refOut,true,false);
            r1=rotor1.getOutputMapIndex(r2.getOutputIndex(),true,false);

            output.append(plugborad.getMappedOutput(latter[r1.getOutputIndex()]));
           // System.out.println("latter is:"+latter[r2.getOutputIndex()]);

        }
        System.out.println("output:"+output);

    }


}