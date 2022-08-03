package menuEngine;

import java.util.List;

public class SelectedDataDTO {

    private final char[] selectedPositions;
    private final String selectedReflectorID;
    private final int[] selectedRotorsID;

    private final List<String> plugBoardPairs;

    public SelectedDataDTO(char[] selectedPositions, String selectedReflectorID, int[] selectedRotorsID, List<String> plugBoardPairs) {
        this.selectedPositions = selectedPositions;
        this.selectedReflectorID = selectedReflectorID;
        this.selectedRotorsID = selectedRotorsID;
        this.plugBoardPairs=plugBoardPairs;
    }
    public SelectedDataDTO()
    {
        this.selectedPositions=null;
        this.selectedReflectorID="";
        this.selectedRotorsID=null;
        this.plugBoardPairs=null;
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
}
