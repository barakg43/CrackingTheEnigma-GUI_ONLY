package UI.SimpleCode;

import UI.MachineConfTab.MachineConfigurationController;
import UI.NewCodeFormat.NewCodeFormatController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SimpleCodeController {

    @FXML
    private TextFlow codeTextFlow;

    private MachineConfigurationController machineCodeController;
    private NewCodeFormatController currentCodeController;

   public void SetMachineConfController(MachineConfigurationController machineController)
   {
       machineCodeController=machineController;
   }

    public void setCurrCodeController(NewCodeFormatController currCodeController)
    {
        currentCodeController=currCodeController;
    }

   public void setSelectedCode(String currCode)
   {
       String selectedParts[]=currCode.split(">");

       Text rotors=new Text(selectedParts[0] + ">");
       rotors.getStyleClass().add("Rotors-text");
       codeTextFlow.getChildren().add(rotors);

       Text positions=new Text(selectedParts[1] + ">");
       positions.getStyleClass().add("Positions-text");
       codeTextFlow.getChildren().add(positions);

       Text reflector=new Text(selectedParts[2]+">");
       reflector.getStyleClass().add("Reflector-text");
       codeTextFlow.getChildren().add(reflector);

       if(selectedParts.length==4)
       {
           Text pairs=new Text(selectedParts[3]+">");
           pairs.getStyleClass().add("PlugBoardPairs-text");
           codeTextFlow.getChildren().add(pairs);

       }
   }

   public void resetTextFlow()
   {
       codeTextFlow.getChildren().clear();
   }

}
