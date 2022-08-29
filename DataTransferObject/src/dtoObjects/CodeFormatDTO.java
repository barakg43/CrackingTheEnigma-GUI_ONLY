package dtoObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeFormatDTO implements Serializable {

    private final RotorInfoDTO[] rotorInfo;
    private final String reflectorID;
    private final List<PlugboardPairDTO> plugboardPairDTOList;

    public CodeFormatDTO(RotorInfoDTO[] rotorInfo, String reflectorID, List<PlugboardPairDTO> plugboardPairDTOList) {
        this.rotorInfo = rotorInfo;
        this.reflectorID = reflectorID;
        this.plugboardPairDTOList =new ArrayList<>(plugboardPairDTOList);


    }

    public RotorInfoDTO[] getRotorInfo() {
        return rotorInfo;
    }

    public String getReflectorID() {
        return reflectorID;
    }

    public List<PlugboardPairDTO> getPlugboardPairDTOList() {
        return plugboardPairDTOList;
    }



    @Override
    public String toString() {
        StringBuilder codeFormat=new StringBuilder();
        //example:<45,27,94><A(2)O(5)!(20)><III><A|Z,D|E>
        //<rotor ID(distance from notch to window),...> =<45,27,94>
        codeFormat.append('<');
        for(int i=rotorInfo.length-1;i>0;i--)
        {
            codeFormat.append(String.format("%d,",rotorInfo[i].getId()));
        }

        codeFormat.append(String.format("%d>",rotorInfo[0].getId()));
        //<starting letter leftest,...,starting letter rightest> = <A(2)O(5)!(20)>
        codeFormat.append('<');
        for(int i=rotorInfo.length-1;i>0;i--) {
            codeFormat.append(String.format("%c(%d),",rotorInfo[i].getStatingLetter(),rotorInfo[i].getDistanceToWindow()));
        }
        codeFormat.append(String.format("%c(%d)",rotorInfo[0].getStatingLetter(),rotorInfo[0].getDistanceToWindow()));

        codeFormat.append('>');
        //<reflector id> = <III>
        codeFormat.append(String.format("<%s>", reflectorID));

        if(!plugboardPairDTOList.isEmpty())
        {
            //<letter-in|letter-out,...> =<A|Z,D|E>
            codeFormat.append('<');
            for (int i = 0; i < plugboardPairDTOList.size()-1; i++)
                codeFormat.append(String.format("%s,", plugboardPairDTOList.get(i)));//print using PlugboardPairDTO toString


            codeFormat.append(String.format("%s>", plugboardPairDTOList.get(plugboardPairDTOList.size()-1)));

        }
        return codeFormat.toString();
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Arrays.hashCode(rotorInfo) +
                           plugboardPairDTOList.hashCode()+
                           reflectorID.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeFormatDTO other = (CodeFormatDTO) obj;
        return  (Arrays.equals(rotorInfo, other.rotorInfo)  &&
                (reflectorID.equals(other.reflectorID)) &&
                ( plugboardPairDTOList.equals(other.plugboardPairDTOList)));
    }
}
