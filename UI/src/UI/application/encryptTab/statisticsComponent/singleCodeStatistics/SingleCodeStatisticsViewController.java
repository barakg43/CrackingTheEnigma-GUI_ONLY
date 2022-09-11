package UI.application.encryptTab.statisticsComponent.singleCodeStatistics;


import UI.application.AllMachineController;
import UI.application.generalComponents.SimpleCode.SimpleCodeController;
import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.tableViewRecord.TableStatisticRecordController;
import dtoObjects.CodeFormatDTO;
import dtoObjects.StatisticRecordDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

public class SingleCodeStatisticsViewController {


    //Space clonedSpace = SerializationUtils.clone(space);

    private AllMachineController mainController;
    @FXML
    private SimpleCodeController codeLayoutController;
    @FXML
    private TableStatisticRecordController tableRecordLayoutController;


    public void createCodeStatisticsView(CodeFormatDTO codeFormatDTO, List<StatisticRecordDTO> statisticRecordDTOList)
    {
        System.out.println(Thread.currentThread().getName()+ ": createCodeStatisticsView");
        codeLayoutController.setSelectedCode(codeFormatDTO);
        tableRecordLayoutController.addRecordsToStatisticTable(statisticRecordDTOList);

    }

}
