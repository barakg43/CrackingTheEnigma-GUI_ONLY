package enigmaMachine.parts;

import java.io.Serializable;
import java.util.Arrays;

public class Reflector implements Serializable {
    private enum ID implements Serializable {
        I("I",1),
        II("II",2),
        III("III",3),
        IV("IV",4),
        V("V",5);
        private final String name;
        private final int id;
        ID(String romanId,int id){
            this.name=romanId;
            this.id=id;
        }
        public int getID() { return id; }

        public String toString() {
            return name;}
        public static boolean isExist(int number) {
            for(ID item : ID.values()) {
                if(number==item.getID()) {
                    return true;
                }
            }
            return false;
        }
        public static boolean isExist(String id) {
            for(ID item : ID.values()) {
                if(id.equals(item.toString())) {
                    return true;
                }
            }
            return false;
        }

    }

    private final ID reflectorIdNum ;
    private final int[] mappedReflectorsArray;
    private final int lettersSize;
    private final int NOT_INIT=-1;
    public static int convertRomanIdToNumber(String id)
    {
            if (ID.isExist(id))
                return ID.valueOf(id).getID();
            else
                return -1;
    }
    public Reflector(int lettersSize,String refID)
    {
        mappedReflectorsArray=new int[lettersSize];
        this.lettersSize=lettersSize;
        reflectorIdNum = ID.valueOf(refID);
        initArray();
    }

    @Override
    public String toString() {
        return "Reflector{" +
                "reflectorIdNum=" + reflectorIdNum +
                ", mappedReflectorsArray=" + Arrays.toString(mappedReflectorsArray) +
                ", lettersSize=" + lettersSize +
                '}';
    }

    private void  initArray()
    {
        for (int i = 0; i < lettersSize; i++) {
            mappedReflectorsArray[i]=NOT_INIT;
        }
    }
    public String getReflectorIdName()
    {
        return reflectorIdNum.toString();
    }
    public static boolean isIdExist(int num)
    {
        return ID.isExist(num);
    }

    public Integer getMappedOutput(Integer input) {
        if(input>= lettersSize ||input<0)
            throw new RuntimeException("invalid input,out of bound reflector id");

        return mappedReflectorsArray[input];
    }

    public void addMappedInputOutput(Integer input, Integer output)
    {

        if(input.equals(output))
            throw new RuntimeException("In reflector No '"+ reflectorIdNum +"' cant mapped input "+(input+1)+" to himself!");
        if(input<0||input>= lettersSize)
            throw new RuntimeException("In reflector No '"+ reflectorIdNum +"'invalid input mapped "+(input+1)+" reflection-out of bounds.number need to be between 1 to "+lettersSize);
          if(output<0||output>= lettersSize)
              throw new RuntimeException("In reflector No '"+ reflectorIdNum +"'invalid input mapped "+(output+1)+" reflection-out of bounds.number need to be between 1 to "+lettersSize);

        if(mappedReflectorsArray[output]!=NOT_INIT)
                throw new RuntimeException("In reflector No '"+ reflectorIdNum +"' the output "  + (output+1) +" appears more than once.");
        if(mappedReflectorsArray[input]!=NOT_INIT)
            throw new RuntimeException("In reflector No '"+ reflectorIdNum +"' the input "  + (input+1) +" appears more than once.");

        mappedReflectorsArray[input]=output;
        mappedReflectorsArray[output]=input;
    }

}
