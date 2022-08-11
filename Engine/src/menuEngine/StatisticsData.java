package menuEngine;

//import menuEngine.dtoObjects.StatisticsDataDTO;

import dtoObjects.StatisticsDataDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsData implements Serializable
{

    private final Map<String, List<StatisticRecord>> codesToProcessData;

    public StatisticsData() {
        this.codesToProcessData = new HashMap<>();
    }

    public void addCipheredDataToStats(String codeConfiguration, String input, String output, long processingTime)
    {
        StatisticRecord processDataRecord=new StatisticRecord(input,output,processingTime);
        addCodeToStats(codeConfiguration);
        codesToProcessData.get(codeConfiguration).add(processDataRecord);

    }
    public void addCodeToStats(String codeConfiguration)
    {
        if(!codesToProcessData.containsKey(codeConfiguration))
        {
            codesToProcessData.put(codeConfiguration,new ArrayList<>());
        }
    }
    public StatisticsDataDTO getStatisticsDataDTO(){
        return new StatisticsDataDTO(codesToProcessData);
    }

    @Override
    public String toString() {
        return "StatisticsData{" +
                "codesToProcessData=" + codesToProcessData +
                '}';
    }
}
