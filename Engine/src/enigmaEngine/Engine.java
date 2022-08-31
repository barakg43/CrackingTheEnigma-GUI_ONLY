package enigmaEngine;

import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.PlugboardPairDTO;
import dtoObjects.StatisticsDataDTO;

import java.util.List;


public interface Engine extends Encryptor {
    void loadXMLFile(String filePath);
    void checkIfRotorsValid(List<Integer> arrayString);
    void checkIfPositionsValid(List<Character> positions);
    void checkIfReflectorNumValid(String ReflectorNum);
    void checkPlugBoardPairs(String pairs) ;
    public void setPlugBoardPairs(List<PlugboardPairDTO> plugBoardPairs);
    public void setReflector(String reflector);

    StatisticsDataDTO getStatisticDataDTO();
    MachineDataDTO getMachineData();
    
    void resetSelected();
    void setCodeAutomatically();
    boolean getWithPlugBoardPairs();
    int getCipheredInputsAmount();
    void  saveMachineStateToFile(String filePathNoExtension);
    boolean isCodeConfigurationIsSet();
    @Override
    String toString();
    void resetAllData();
    boolean isMachineLoaded();
    CodeFormatDTO getCodeFormat(boolean isCalcDistanceFromInitWindow);

    }
