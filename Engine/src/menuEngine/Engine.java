package menuEngine;

import dtoObjects.*;

import java.util.List;

public interface Engine {

    MachineDataDTO getMachineData();

    SelectedConfigurationDTO getSelectedData();

     void LoadXMLFile(String filePath);

     void checkIfRotorsValid(String rotors);

     boolean checkIfDataValid(String data);

     StatisticsDataDTO getStatisticDataDTO();

    void checkIfPositionsValid(String positions);

    String cipherData(String dataInput);

    void resetCodePosition();

    void resetSelected();

    void checkIfReflectorNumValid(String ReflectorNum);
    int getNumberOfRotorInSystem();
    List<String> getReflectorIdList();

    void checkPlugBoardPairs(String pairs) ;
    void  saveMachineStateToFile(String filePathNoExtension);
    int checkPlugBoardNum(String plugBoardNum);



    void setCodeAutomatically();
    String getAlphabetString();
    boolean getWithPlugBoardPairs();
    int getCipheredInputs();
    int addCipheredInputs();
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();
    String getCodeFormat(boolean isSelectedData,boolean isHistory);

    }
