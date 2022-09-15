package UI.application.DmTab;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

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

    public StringProperty taskMessageProperty() {
        return taskMessageProperty;
    }

    public StringProperty progressPercentProperty() {
        return progressPercentProperty;
    }

    public DoubleProperty progressBarProperty() {
        return progressBarProperty;
    }

    public StringProperty totalNumberOfTasksPropertyProperty() {
        return totalNumberOfTasksProperty;
    }
}
