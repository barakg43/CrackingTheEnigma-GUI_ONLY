package enigmaEngine;

import decryptionManager.components.Dictionary;
import dtoObjects.CodeFormatDTO;
import dtoObjects.MachineDataDTO;
import dtoObjects.PlugboardPairDTO;
import dtoObjects.StatisticsDataDTO;

import java.util.List;


public interface Engine extends Encryptor {
    void loadXMLFile(String filePath);
    void checkIfRotorsValid(List<Integer> arrayInteger);
    void checkIfPositionsValid(List<Character> positions);
    void checkIfReflectorNumValid(String ReflectorNum);
    void checkPlugBoardPairs(List<PlugboardPairDTO> plugBoardPairs) ;
     void setReflector(String reflector);
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

    Dictionary getDictionary();

}
