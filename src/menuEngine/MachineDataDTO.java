package menuEngine;

public class MachineDataDTO {

    private final int NumberOfReflectors;
    private final int NumberOfRotorsInUse;
    private final int[] RotorsId;
    private final int[] NotchNums;
//test


    public MachineDataDTO(int numberOfReflectors,int numOfRotorsInUse,int[] rotorsIdArray,int[] notchArray) {
        NumberOfReflectors = numberOfReflectors;
        NumberOfRotorsInUse=numOfRotorsInUse;
        RotorsId=rotorsIdArray;
        NotchNums=notchArray;
    }

    public int getNumberOfReflectors() {
        return NumberOfReflectors;
    }

    public int[] getNotchNums() {
        return NotchNums;
    }

    public int[] getRotorsId() {
        return RotorsId;
    }

    public int getNumberOfRotorsInUse() {
        return NumberOfRotorsInUse;
    }
}
