package enigmaEngine;

import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.StatisticsDataDTO;

import java.util.List;


public interface Engine {
    void loadXMLFile(String filePath);
    void checkIfRotorsValid(String arrayString);
    void checkIfPositionsValid(String positions);
    void checkIfReflectorNumValid(String ReflectorNum);
    void checkPlugBoardPairs(String pairs) ;
    public void setReflector(String reflector);

    StatisticsDataDTO getStatisticDataDTO();
    MachineDataDTO getMachineData();


    String cipherData(String dataInput);
    void resetCodePosition();
    void resetSelected();
    void setCodeAutomatically();
    boolean getWithPlugBoardPairs();
    int getCipheredInputs();
    void  saveMachineStateToFile(String filePathNoExtension);
    boolean isCodeConfigurationIsSet();
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();
    CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow);

    }
