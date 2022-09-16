package UI.application.DmTab.DMTaskComponents.taskProgress;

import UI.application.DmTab.DMTaskComponents.TaskDataController;
import UI.application.DmTab.DMcontroller;
import UI.application.DmTab.ProgressDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class taskProgressController {
    @FXML
    public Label totalDecryptTime;
    @FXML
    public Label averageTaskTime;
    @FXML
    private Label totalNumberOfTasks;
    @FXML
    public Label totalAmountTaskDone;


    @FXML
    private Label taskMessage;

    @FXML
    private Label progressPercentLabel;

    @FXML
    private ProgressBar progressBar;



    public ProgressDataDTO createNewProgressProperties()
    {
        return new ProgressDataDTO(taskMessage.textProperty()
                ,progressPercentLabel.textProperty()
                ,progressBar.progressProperty()
                ,totalNumberOfTasks.textProperty()
                ,totalAmountTaskDone.textProperty(),
                averageTaskTime.textProperty(),
                totalDecryptTime.textProperty());
    }

    public void restartAllData() {
    }

//    private TaskDataController taskDataController;
//
//
//    public void setTaskDataController(TaskDataController taskDataController) {
//        this.taskDataController=taskDataController;
//
//    }


}

