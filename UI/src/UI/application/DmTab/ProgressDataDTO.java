package UI.application.DmTab;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProgressDataDTO {

    private StringProperty taskMessageProperty;
    private StringProperty progressPercentProperty;
    private DoubleProperty progressBarProperty;
    private StringProperty totalNumberOfTasksProperty;

    public ProgressDataDTO(StringProperty taskMessageProperty, StringProperty progressPercentProperty,
                           DoubleProperty progressBarProperty, StringProperty totalNumberOfTasksProperty) {
        this.taskMessageProperty = taskMessageProperty;
        this.progressPercentProperty = progressPercentProperty;
        this.progressBarProperty = progressBarProperty;
        this.totalNumberOfTasksProperty = totalNumberOfTasksProperty;
    }

    public StringProperty taskMessagePropertyProperty() {
        return taskMessageProperty;
    }

    public StringProperty progressPercentPropertyProperty() {
        return progressPercentProperty;
    }

    public DoubleProperty progressBarPropertyProperty() {
        return progressBarProperty;
    }

    public StringProperty totalNumberOfTasksPropertyProperty() {
        return totalNumberOfTasksProperty;
    }
}
