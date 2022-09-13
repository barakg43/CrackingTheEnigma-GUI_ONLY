import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimelineAnimationExample extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("JavaFX Timeline Animation Example");

        Button btn1 = new Button("Timeline");
        btn1.setOnAction(new Button1Listener());
        HBox buttonHb1 = new HBox(10);
        buttonHb1.setAlignment(Pos.CENTER);
        buttonHb1.getChildren().addAll(btn1);

        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(100, 25, 100, 25));
        vbox.getChildren().addAll(buttonHb1);

        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class Button1Listener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Timeline");
            stage.setResizable(false);

            Circle circle = new Circle(100, 100, 20);

            VBox vbox = new VBox(30);
            vbox.setPadding(new Insets(25, 25, 25, 25));

            Timeline timeline = new Timeline();

            Text currTimeText = new Text("Current time: 0 secs");
            currTimeText.setBoundsType(TextBoundsType.VISUAL);

            timeline.currentTimeProperty().addListener((Observable ov) -> {
                int time = (int) timeline.getCurrentTime().toSeconds();
                currTimeText.setText("Current time: " + time + " secs");
            });

            vbox.getChildren().addAll(circle, currTimeText);

            Scene scene = new Scene(vbox, 500, 400);
            stage.setScene(scene);
            stage.show();

            Duration time = new Duration(3* 1000);
            KeyValue keyValue = new KeyValue(circle.translateXProperty(), 300);
            KeyFrame keyFrame = new KeyFrame(time, keyValue);
            timeline.getKeyFrames().add(keyFrame);

            keyValue = new KeyValue(circle.opacityProperty(), 1);
            keyFrame = new KeyFrame(time, keyValue);
            timeline.getKeyFrames().add(keyFrame);

            timeline.setCycleCount(3);
            timeline.setAutoReverse(true);

            timeline.play();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
