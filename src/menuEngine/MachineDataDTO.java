package menuEngine;

public class MachineDataDTO {


    private final int numberOfReflectors;
    private final int numberOfRotorsInUse;
    private final int[] rotorsId;
    private final int[] notchNums;


    public MachineDataDTO(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray) {
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
