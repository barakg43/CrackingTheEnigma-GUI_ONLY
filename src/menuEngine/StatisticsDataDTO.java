package menuEngine;

import impl.StatisticRecord;

import java.util.*;

public class StatisticsDataDTO {


    protected Map<String, List<StatisticRecord>> codesToProcessData ;
    public StatisticsDataDTO(Map<String, List<StatisticRecord>> codesToProcessData)
    {
        this.codesToProcessData=new HashMap<>();
        this.codesToProcessData.putAll(codesToProcessData);

    }

    public Map<String, List<StatisticRecord>> getStatisticsData(){
        return codesToProcessData;
    }


}
