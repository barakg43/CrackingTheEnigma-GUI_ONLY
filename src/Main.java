import EnigmaMachine.Mapper;
import impl.Plugboard;
import impl.Reflector;
import EnigmaMachine.Roter;
import EnigmaMachine.RotorOutputData;

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
        Roter roter1=new Roter(6,4,1);
        roter1.addMapLatterToRotor('F','A');
        roter1.addMapLatterToRotor('E','B');
        roter1.addMapLatterToRotor('D','C');
        roter1.addMapLatterToRotor('C','D');
        roter1.addMapLatterToRotor('B','E');
        roter1.addMapLatterToRotor('A','F');
        Roter roter2=new Roter(6,1,2);
        roter2.addMapLatterToRotor('E','A');
        roter2.addMapLatterToRotor('B','B');
        roter2.addMapLatterToRotor('D','C');
        roter2.addMapLatterToRotor('F','D');
        roter2.addMapLatterToRotor('C','E');
        roter2.addMapLatterToRotor('A','F');

        //test the example in the word   <F|A><I><CC><2,1>
        char[] latter={'A','B','C','D','E','F'};
        Map<Character,Integer> latterToIndex=new HashMap<>();
        for (int i = 0; i < latter.length ; i++) {
                latterToIndex.put(latter[i],i);
        }
        String input="AABBCCDDEEFF";
        StringBuilder output= new StringBuilder();
        Reflector reflector1=new Reflector(6);
        Mapper<Character> plugborad=new Plugboard();
        plugborad.addMappedInputOutput('F','A');
        reflector1.addMappedInputOutput(1,4);
        reflector1.addMappedInputOutput(2,5);
        reflector1.addMappedInputOutput(3,6);
        roter1.setInitialWindowPosition('C');
        roter2.setInitialWindowPosition('C');
       // roter1.printRotorTableDebugModeOnly();
        //roter2.printRotorTableDebugModeOnly();
        int refOut;
        char afterBoard;
            for (int i = 0; i < input.length(); i++) {
            afterBoard=plugborad.getMappedOutput(input.charAt(i));
            RotorOutputData r1=roter1.getOutputMapIndex(latterToIndex.get(afterBoard),false,true);
            RotorOutputData r2=roter2.getOutputMapIndex(r1.getOutputIndex(),false,r1.isAdvanceNextRotor());
            refOut=reflector1.getMappedOutput(r2.getOutputIndex());
            r2=roter2.getOutputMapIndex(refOut,true,false);
            r1=roter1.getOutputMapIndex(r2.getOutputIndex(),true,false);

            output.append(plugborad.getMappedOutput(latter[r1.getOutputIndex()]));
           // System.out.println("latter is:"+latter[r2.getOutputIndex()]);

        }
        System.out.println("output:"+output);

    }


}