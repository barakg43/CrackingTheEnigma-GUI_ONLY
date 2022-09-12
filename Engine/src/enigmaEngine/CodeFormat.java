package enigmaEngine;

import dtoObjects.CodeFormatDTO;
import dtoObjects.PlugboardPairDTO;
import dtoObjects.RotorInfoDTO;

import java.util.List;

public class CodeFormat extends CodeFormatDTO {

    public CodeFormat(RotorInfoDTO[] rotorInfo, String reflectorID, List<PlugboardPairDTO> plugboardPairDTOList) {
        super(rotorInfo, reflectorID, plugboardPairDTOList);
    }


    public void setIsCurrentCode(boolean isCurrentMachineCode)
    {
        this.isCurrentMachineCode=isCurrentMachineCode;
    }
}
