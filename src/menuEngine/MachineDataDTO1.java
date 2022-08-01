package menuEngine;

public class MachineDataDTO1 {

    private final int NumberOfReflectors;
    private final int NumberOfRotorsInUse;
    private final int[] RotorsId;
    private final int[] NotchNums;



    public MachineDataDTO1(int numberOfReflectors, int numOfRotorsInUse, int[] rotorsIdArray, int[] notchArray) {
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
