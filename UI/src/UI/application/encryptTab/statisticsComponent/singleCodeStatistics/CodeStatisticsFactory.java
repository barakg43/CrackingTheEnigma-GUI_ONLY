package UI.application.encryptTab.statisticsComponent.singleCodeStatistics;

import dtoObjects.CodeFormatDTO;
import dtoObjects.StatisticRecordDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static UI.AllMachine.CommonResourcesPaths.SINGLE_CODE_STATISTICS_RESOURCE;

public class CodeStatisticsFactory {


    private URL url;
    public CodeStatisticsFactory() {
        url=getClass().getClassLoader().getResource(SINGLE_CODE_STATISTICS_RESOURCE);
    }

    public synchronized Pane createNewCodeStatisticsNode(CodeFormatDTO codeFormatDTO, List<StatisticRecordDTO> statisticRecordDTOList) {

        Pane loadedStatisticsNode;
        FXMLLoader fxmlLoader  = new FXMLLoader();
        fxmlLoader.setLocation(url);
        try {
             loadedStatisticsNode = fxmlLoader.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        TableStatisticRecordController controller= fxmlLoader.getController();
        SingleCodeStatisticsViewController controller= fxmlLoader.getController();
        controller.createCodeStatisticsView(codeFormatDTO,statisticRecordDTOList);
        return loadedStatisticsNode;

    }
}
