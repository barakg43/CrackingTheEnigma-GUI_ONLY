package impl;

import com.sun.deploy.util.StringUtils;
import javafx.print.PrinterJob;

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