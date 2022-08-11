package dtoObjects;

import java.io.Serializable;
import java.util.Arrays;

public class MachineDataDTO implements Serializable {


    private final int numberOfReflectors;
    private final int numberOfRotorsInUse;
    private final int[] rotorsId;
    private final int[] notchNums;

    private final int[] notchPositions;

    public MachineDataDTO()
    {
        numberOfReflectors=0;
        numberOfRotorsInUse=0;
        rotorsId=null;
        notchNums=null;
        notchPositions=null;
    }
    public MachineDataDTO(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray,int[] notchPositions) {
        this.numberOfReflectors = numberOfReflectors;
        this.numberOfRotorsInUse =numOfRotorsInUse;
        rotorsId =rotorsIdArray;
        notchNums =notchArray;
        this.notchPositions=notchPositions;
    }

    public static MachineDataDTO create(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray,int[] notchPositions) {
        return new MachineDataDTO(numberOfReflectors,numOfRotorsInUse, rotorsIdArray, notchArray,notchPositions);
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

    public int[] getNotchPositions(){return notchPositions;}

    @Override
    public String toString() {
        return "dtoObjects.MachineDataDTO{" +
                "numberOfReflectors=" + numberOfReflectors +
                ", numberOfRotorsInUse=" + numberOfRotorsInUse +
                ", rotorsId=" + Arrays.toString(rotorsId) +
                ", notchNums=" + Arrays.toString(notchNums) +
                ", notchPositions=" + Arrays.toString(notchPositions) +
                '}';
    }

}
