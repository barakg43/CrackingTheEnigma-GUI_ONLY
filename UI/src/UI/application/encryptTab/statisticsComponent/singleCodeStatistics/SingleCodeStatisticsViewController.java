package UI.application.encryptTab.statisticsComponent.singleCodeStatistics;


import UI.SimpleCode.SimpleCodeController;
import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.tableViewRecord.TableStatisticRecordController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.StatisticRecordDTO;
import javafx.fxml.FXML;

import java.util.List;

public class SingleCodeStatisticsViewController {


    //Space clonedSpace = SerializationUtils.clone(space);

    @FXML
    private SimpleCodeController codeLayoutController;
    @FXML
    private TableStatisticRecordController tableRecordLayoutController;
    public void createCodeStatisticsView(CodeFormatDTO codeFormatDTO, List<StatisticRecordDTO> statisticRecordDTOList)
    {
        codeLayoutController.setSelectedCode(codeFormatDTO);
        tableRecordLayoutController.addRecordsToStatisticTable(statisticRecordDTOList);

    }

}
