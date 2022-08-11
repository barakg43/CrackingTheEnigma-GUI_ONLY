package enigmaMachine.parts;

import java.io.Serializable;

public enum reflectorId implements Serializable {
    I,II,III,IV,V;

    public static Boolean isExist(int number) {
        for(reflectorId  item : reflectorId.values()) {
            if(number-1==item.ordinal()) {
                return true;
            }
        }
        return false;
    }
}