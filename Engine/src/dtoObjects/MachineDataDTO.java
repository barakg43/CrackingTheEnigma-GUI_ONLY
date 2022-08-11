package dtoObjects;

import java.io.Serializable;
import java.util.Arrays;

public class MachineDataDTO implements Serializable {


    private final int numberOfReflectors;
    private final int numberOfRotorsInUse;
    private final int[] rotorsId;
    private final int[] notchNums;

    public MachineDataDTO()
    {
        numberOfReflectors=0;
        numberOfRotorsInUse=0;
        rotorsId=null;
        notchNums=null;
    }
    public MachineDataDTO(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray) {
        this.numberOfReflectors = numberOfReflectors;
        this.numberOfRotorsInUse =numOfRotorsInUse;
        rotorsId =rotorsIdArray;
        notchNums =notchArray;
    }

    public static MachineDataDTO create(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray) {
        return new MachineDataDTO(numberOfReflectors,numOfRotorsInUse, rotorsIdArray, notchArray);
    }

    public int getNumberOfReflectors() {
        return numberOfReflectors;
    }

    public int[] getNotchNums() {
        return notchNums;
    }

    public int[] getRotorsId() {
        return rotorsId;
    }

    public int getNumberOfRotorsInUse() {
        return numberOfRotorsInUse;
    }

    @Override
    public String toString() {
        return "dtoObjects.MachineDataDTO{" +
                "numberOfReflectors=" + numberOfReflectors +
                ", numberOfRotorsInUse=" + numberOfRotorsInUse +
                ", rotorsId=" + Arrays.toString(rotorsId) +
                ", notchNums=" + Arrays.toString(notchNums) +
                '}';
    }

}
