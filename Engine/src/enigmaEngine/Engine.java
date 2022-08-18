package enigmaEngine;

import dtoObjects.*;


public interface Engine {
    void setInitialCode();
    void loadXMLFile(String filePath);
    void checkIfRotorsValid(String rotors);
    void checkIfPositionsValid(String positions);
    void checkIfReflectorNumValid(String ReflectorNum);
    void checkPlugBoardPairs(String pairs) ;
    StatisticsDataDTO getStatisticDataDTO();
    MachineDataDTO getMachineData();

    SelectedConfigurationDTO getSelectedData();
    String cipherData(String dataInput);
    void resetCodePosition();
    void resetSelected();
    void setCodeAutomatically();
    boolean getWithPlugBoardPairs();
    int getCipheredInputs();
    void  saveMachineStateToFile(String filePathNoExtension);
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();
    CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow);

    }
