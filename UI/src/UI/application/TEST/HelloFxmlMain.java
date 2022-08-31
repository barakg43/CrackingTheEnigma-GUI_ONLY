package UI.application.TEST;

import UI.application.encryptTab.statisticsComponent.singleCodeStatistics.tableViewRecord.TableStatisticRecordController;
import dtoObjects.StatisticRecordDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//video: 100189 - FXML Hello World [JAD, JavaFX] | Powered by SpeaCode
public class HelloFxmlMain extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.setTitle("Hello There in FXML");
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL url = getClass().getClassLoader().getResource("UI/application/encryptTab/statisticsComponent/singleCodeStatistics/tableViewRecord/tableStatisticRecord.fxml");
        System.out.println("before 1");
        fxmlLoader.setLocation(url);
        System.out.println("before 2");
        assert url != null;
        System.out.println("before 2.5:"+url);
        Parent load = fxmlLoader.load(url.openStream());
        System.out.println("before 3");
        TableStatisticRecordController controller= fxmlLoader.getController();
        System.out.println("before 4");
        List<StatisticRecordDTO> statisticRecordDTOList=new ArrayList<>();
        System.out.println("before 5");
        statisticRecordDTOList.add(new StatisticRecordDTO("aaa","bbb",500));
        System.out.println("before 6");
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        statisticRecordDTOList.add(new StatisticRecordDTO("bbb","ccc",600));
        System.out.println("before 7");
        controller.addRecordsToStatisticTable(statisticRecordDTOList);
        statisticRecordDTOList.add(new StatisticRecordDTO("rrr","aaa",600));
        System.out.println("before 8");
        Scene scene = new Scene(load, 600, 400);
        System.out.println("before 9");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(HelloFxmlMain.class);
    }
}
