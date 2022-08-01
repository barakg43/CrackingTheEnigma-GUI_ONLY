package impl;

public enum reflectorId {
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