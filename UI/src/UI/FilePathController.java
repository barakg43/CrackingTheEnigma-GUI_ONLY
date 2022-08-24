package UI;


import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.xml.soap.Text;
import java.awt.*;

public class FilePathController {

    public Label fileSuccessfullyLabel;
    private Engine mEngine;
    private AllMachineController mainAppController;

    @FXML
    private TextField FilePathTextField;
    @FXML
    private Button LoadFileButton;

    public FilePathController(){
        mEngine=new EnigmaEngine();
    }

    @FXML
    public void initialize(){
        fileSuccessfullyLabel.setVisible(false);
    }
    public void setMainAppController(AllMachineController MainController)
    {
        mainAppController=MainController;
    }

    @FXML
    public void LoadFileButtonActionListener(javafx.event.ActionEvent actionEvent) {
        try {
            mEngine.loadXMLFile(FilePathTextField.getText());
            fileSuccessfullyLabel.setText("File loded successfully");


        }catch (Exception ex)
        {
            fileSuccessfullyLabel.setText(ex.getMessage());
        }
        fileSuccessfullyLabel.setVisible(true);
    }
}
