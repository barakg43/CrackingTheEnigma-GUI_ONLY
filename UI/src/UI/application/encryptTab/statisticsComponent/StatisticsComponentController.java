package UI.application.encryptTab.statisticsComponent;

import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.CodeStatisticsFactory;
import dtoObjects.CodeFormatDTO;
import dtoObjects.StatisticRecordDTO;
import dtoObjects.StatisticsDataDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatisticsComponentController {

    @FXML
    private ScrollPane statisticsCodeScrollPane;

    @FXML
    private GridPane mainContainer;
    private final ExecutorService statisticsNodeMangerService;
    private final CodeStatisticsFactory codeStatisticsFactory;

    public StatisticsComponentController() {
        codeStatisticsFactory = new CodeStatisticsFactory();
        statisticsNodeMangerService = Executors.newSingleThreadExecutor();
    }

    public void bindSizePropertyToParent(ReadOnlyDoubleProperty widthProperty,ReadOnlyDoubleProperty heightProperty){

        mainContainer.prefWidthProperty().bind(widthProperty);
        mainContainer.prefHeightProperty().bind(heightProperty);
        statisticsCodeScrollPane.prefWidthProperty().bind(mainContainer.widthProperty());
        statisticsCodeScrollPane.prefHeightProperty().bind(mainContainer.heightProperty());
        mainContainer.prefWidthProperty().bind(widthProperty);
        mainContainer.prefHeightProperty().bind(heightProperty);

    }
    public void clearAllData(){
        statisticsCodeScrollPane.setContent(new VBox());

    }
    public void updateCodeStatisticsView(Map<CodeFormatDTO, List<StatisticRecordDTO>> statisticsDataHistory) {
        //run the task on new tread,may be heavy I/O loading file 'createNewCodeStatisticsNode'
        statisticsNodeMangerService.execute(() -> {
            VBox vboxCodeStaticsNode=new VBox();
            vboxCodeStaticsNode.setPrefSize(VBox.USE_COMPUTED_SIZE,VBox.USE_COMPUTED_SIZE);
             vboxCodeStaticsNode.setMaxWidth(Double.MAX_VALUE);
             vboxCodeStaticsNode.setAlignment(Pos.CENTER);

             //using this bind to center all statics code node+table
        vboxCodeStaticsNode.translateXProperty().bind(
                statisticsCodeScrollPane.widthProperty().
                        subtract(vboxCodeStaticsNode.widthProperty()).divide(2)
                    );
           // System.out.println(Thread.currentThread().getName()+ ": before load component");
            for (CodeFormatDTO code : statisticsDataHistory.keySet()) {
//                System.out.println("Current code:"+code);

//                System.out.println("before factory isEmpty:"+ statisticsDataHistory.get(code).isEmpty());
                Pane statisticsCodeRecordsNode = codeStatisticsFactory.createNewCodeStatisticsNode(code, statisticsDataHistory.get(code));
                vboxCodeStaticsNode.getChildren().add(statisticsCodeRecordsNode);
//                System.out.println("after factory isEmpty:"+ statisticsDataHistory.get(code).isEmpty());

            }
           // System.out.println(Thread.currentThread().getName()+ ": after create component");
            Platform.runLater(//update UI without blocking JAT
                    () -> {
                      //  System.out.println(Thread.currentThread().getName()+ ": update component");
                        statisticsCodeScrollPane.setContent(vboxCodeStaticsNode);

                    });
        });
       }
    public void updateCodeStatisticsView(StatisticsDataDTO statisticsDataDTO) {
        updateCodeStatisticsView(statisticsDataDTO.getStatisticsData());
    }

    @FXML
    private void initialize() {

    }
}

