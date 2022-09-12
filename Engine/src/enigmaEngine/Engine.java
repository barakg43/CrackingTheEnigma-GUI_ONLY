package enigmaEngine;

import dtoObjects.*;
import dtoObjects.DmDTO.BruteForceLevel;

import java.util.List;


public interface Engine extends Encryptor {
    void loadXMLFile(String filePath);
    void checkIfRotorsValid(List<Integer> arrayInteger);
    void checkIfPositionsValid(List<Character> positions);
    void checkIfReflectorNumValid(String ReflectorNum);
    void checkPlugBoardPairs(List<PlugboardPairDTO> plugBoardPairs) ;
    public void setReflector(String reflector);
    void setCodeManually(CodeFormatDTO codeConfiguration);
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
    public void bruteForce(CodeFormatDTO codeFormatDTO, BruteForceLevel BFLevel);
    public CodeFormatDTO getBFCodeFormat();

}
