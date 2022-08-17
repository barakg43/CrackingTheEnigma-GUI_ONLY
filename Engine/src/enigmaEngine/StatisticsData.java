package enigmaEngine;


import dtoObjects.CodeFormatDTO;
import dtoObjects.StatisticRecordDTO;
import dtoObjects.StatisticsDataDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class StatisticsData extends StatisticsDataDTO implements Serializable
{

    public StatisticsData() {super();}

    public void addCipheredDataToStats(CodeFormatDTO codeConfiguration, String input, String output, long processingTime)
    {
        StatisticRecordDTO processDataRecord=new StatisticRecordDTO(input,output,processingTime);
        codesToProcessData.putIfAbsent(codeConfiguration,new ArrayList<>());
        codesToProcessData.get(codeConfiguration).add(processDataRecord);

    }

}
