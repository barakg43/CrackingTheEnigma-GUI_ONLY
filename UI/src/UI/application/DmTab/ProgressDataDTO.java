package UI.application.DmTab;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProgressDataDTO {

    private final StringProperty taskMessageProperty;
    private final StringProperty progressPercentProperty;
    private final DoubleProperty progressBarProperty;
    private final StringProperty totalNumberOfTasksProperty;
    private StringProperty totalAmountTaskDoneProperty;
    public ProgressDataDTO(StringProperty taskMessageProperty,
                           StringProperty progressPercentProperty,
                           DoubleProperty progressBarProperty,
                           StringProperty totalNumberOfTasksProperty,
                           StringProperty totalAmountTaskDoneProperty) {
        this.taskMessageProperty = taskMessageProperty;
        this.progressPercentProperty = progressPercentProperty;
        this.progressBarProperty = progressBarProperty;
        this.totalNumberOfTasksProperty = totalNumberOfTasksProperty;
        this.totalAmountTaskDoneProperty = totalAmountTaskDoneProperty;
    }


    public StringProperty taskMessageProperty() {
        return taskMessageProperty;
    }


    public StringProperty progressPercentProperty() {
        return progressPercentProperty;
    }

    public DoubleProperty progressBarProperty() {
        return progressBarProperty;
    }


    public StringProperty totalNumberOfTasksProperty() {
        return totalNumberOfTasksProperty;
    }


    public StringProperty totalAmountTaskDoneProperty() {
        return totalAmountTaskDoneProperty;
    }
}
