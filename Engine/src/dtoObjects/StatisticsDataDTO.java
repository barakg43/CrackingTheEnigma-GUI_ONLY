package dtoObjects;

import menuEngine.StatisticRecord;

import java.io.Serializable;
import java.util.*;

public class StatisticsDataDTO  implements Serializable {


    protected Map<String, List<StatisticRecord>> codesToProcessData ;
    public StatisticsDataDTO(Map<String, List<StatisticRecord>> codesToProcessData)
    {
        this.codesToProcessData=new HashMap<>();
        this.codesToProcessData.putAll(codesToProcessData);

    }

    @Override
    public String toString() {
        return "dtoObjects.StatisticsDataDTO{" +
                "codesToProcessData=" + codesToProcessData +
                '}';
    }

    public Map<String, List<StatisticRecord>> getStatisticsData(){
        return codesToProcessData;
    }


}
