package dtoObjects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SelectedConfigurationDTO implements Serializable {

    private final char[] selectedPositions;
    private final String selectedReflectorID;
    private final int[] selectedRotorsID;

    private final List<String> plugBoardPairs;
    private final int[] notchPositions;

    public SelectedConfigurationDTO(char[] selectedPositions, String selectedReflectorID, int[] selectedRotorsID, List<String> plugBoardPairs,int[] notchPositions) {
        this.selectedPositions = selectedPositions;
        this.selectedReflectorID = selectedReflectorID;
        this.selectedRotorsID = selectedRotorsID;
        this.plugBoardPairs=plugBoardPairs;
        this.notchPositions=notchPositions;
    }
    public SelectedConfigurationDTO()
    {
        this.selectedPositions=null;
        this.selectedReflectorID="";
        this.selectedRotorsID=null;
        this.plugBoardPairs=null;
        this.notchPositions=null;
    }
    public String getSelectedReflectorID() {
        return selectedReflectorID;
    }

    public char[] getSelectedPositions() {
        return selectedPositions;
    }

    public int[] getSelectedRotorsID() {
        return selectedRotorsID;
    }

    public List<String> getPlugBoardPairs() {
        return plugBoardPairs;
    }

    public int[] getNotchPositions(){return notchPositions;}

    @Override
    public String toString() {
        return "dtoObjects.SelectedConfigurationDTO{" +
                "selectedPositions=" + Arrays.toString(selectedPositions) +
                ", selectedReflectorID='" + selectedReflectorID + '\'' +
                ", selectedRotorsID=" + Arrays.toString(selectedRotorsID) +
                ", plugBoardPairs=" + plugBoardPairs +
                '}';
    }
}
