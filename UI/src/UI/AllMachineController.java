package UI;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class AllMachineController {

    @FXML private Pane HeaderComponent;
    @FXML private FilePathController HeaderComponentController;

    @FXML
    public void initialize() {
        if(HeaderComponentController!=null) {
            HeaderComponentController.setMainAppController(this);
        }
    }

}
