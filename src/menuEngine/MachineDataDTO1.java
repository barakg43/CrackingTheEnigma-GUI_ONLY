package menuEngine;

public class MachineDataDTO1 {

    private final int numberOfReflectors;
    private final int numberOfRotorsInUse;
    private final int[] rotorsId;
    private final int[] notchNums;


    public MachineDataDTO1(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray) {
        this.numberOfReflectors = numberOfReflectors;
        this.numberOfRotorsInUse =numOfRotorsInUse;
        rotorsId =rotorsIdArray;
        notchNums =notchArray;
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
}
