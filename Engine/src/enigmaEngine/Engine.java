package enigmaEngine;

import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.SelectedConfigurationDTO;
import dtoObjects.StatisticsDataDTO;

import java.util.List;

public interface Engine {

    MachineDataDTO getMachineData();

    SelectedConfigurationDTO getSelectedData();
    void setInitialCode();

    void loadXMLFile(String filePath);

     void checkIfRotorsValid(String rotors);

     StatisticsDataDTO getStatisticDataDTO();

    void checkIfPositionsValid(String positions);

    String cipherData(String dataInput);

    void resetCodePosition();

    void resetSelected();

    void checkIfReflectorNumValid(String ReflectorNum);
    int getNumberOfRotorInSystem();
    List<String> getReflectorIdList();

    void checkPlugBoardPairs(String pairs) ;
    int checkPlugBoardNum(String plugBoardNum);

    void setCodeAutomatically();
    String getAlphabetString();
    boolean getWithPlugBoardPairs();
    int getCipheredInputs();
    void  saveMachineStateToFile(String filePathNoExtension);
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();
    CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow);

    }
