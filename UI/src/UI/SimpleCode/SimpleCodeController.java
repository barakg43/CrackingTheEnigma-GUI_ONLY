package UI.SimpleCode;

import dtoObjects.CodeFormatDTO;
import dtoObjects.PlugboardPairDTO;
import dtoObjects.RotorInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

public class SimpleCodeController {


    @FXML
    private HBox hboxCodeFormat;



    @FXML
    private TextFlow rotorIDsList;



    @FXML
    private TextFlow positionDistance;


    @FXML
    private Text reflectorIDtext;

    @FXML
    private GridPane plugboardGridComp;

    @FXML
    private TextFlow plugboardPairsList;

   public void setSelectedCode(CodeFormatDTO currCode)
   {
       hboxCodeFormat.setVisible(true);

      RotorInfoDTO[] rotorInfo=currCode.getRotorInfo();
       List<PlugboardPairDTO> plugboardPairDTOList = currCode.getPlugboardPairDTOList();
       //example:<45,27,94><A(2)O(5)!(20)><III><A|Z,D|E>
       //<rotor ID(distance from notch to window),...> =<45,27,94>
       Text temp;
       for(int i = currCode.getRotorInfo().length-1; i>0; i--)
       {
           temp= new Text(String.format("%d,",rotorInfo[i].getId()));
           temp.getStyleClass().add("Rotors-text");
           rotorIDsList.getChildren().add(temp);
       }
       temp=new Text(String.format("%d",rotorInfo[0].getId()));
       temp.getStyleClass().add("Rotors-text");
       rotorIDsList.getChildren().add(temp);;


       //<starting letter leftest,...,starting letter rightest> = <A(2)O(5)!(20)>
       for(int i=rotorInfo.length-1;i>=0;i--) {
           temp=new Text(
                   String.format(
                           "%c(%d)",rotorInfo[i].getStatingLetter(),rotorInfo[i].getDistanceToWindow()
                   ));
           temp.getStyleClass().add("Positions-text");
           positionDistance.getChildren().add(temp);
       }
       //<reflector id> = <III>
       reflectorIDtext.setText(currCode.getReflectorID());
       plugboardGridComp.setVisible(!plugboardPairDTOList.isEmpty());
       if(!plugboardPairDTOList.isEmpty())
       {
           //<letter-in|letter-out,...> =<A|Z,D|E>
           for (int i = 0; i < plugboardPairDTOList.size()-1; i++) {
               temp=new Text(
                       String.format(
                               "%s,", plugboardPairDTOList.get(i)
                       ));
               temp.getStyleClass().add("PlugBoardPairs-text");
               plugboardPairsList.getChildren().add(temp);//print using PlugboardPairDTO toString
           }

           temp=new Text(
                   String.format(
                           "%s", plugboardPairDTOList.get(plugboardPairDTOList.size()-1)
                   ));
           temp.getStyleClass().add("PlugBoardPairs-text");
           plugboardPairsList.getChildren().add(temp);

       }


   }
       @FXML
       private void initialize() {
            plugboardGridComp.disableProperty().bind(plugboardGridComp.visibleProperty().not());
            plugboardGridComp.managedProperty().bind(plugboardGridComp.visibleProperty());
            hboxCodeFormat.disableProperty().bind(hboxCodeFormat.visibleProperty());
            hboxCodeFormat.managedProperty().bind(hboxCodeFormat.visibleProperty());

       }

    public void clearCurrentCodeView()
    {
        hboxCodeFormat.setVisible(false);
        rotorIDsList.getChildren().clear();
        positionDistance.getChildren().clear();
        reflectorIDtext.setText("");
        plugboardPairsList.getChildren().clear();
    }
}

//
//
//       String selectedParts[]=currCode.split(">");
//
//       Text rotors=new Text(selectedParts[0] + ">");
//       rotors.getStyleClass().add("Rotors-text");
//       codeTextFlow.getChildren().add(rotors);
//
//       Text positions=new Text(selectedParts[1] + ">");
//       positions.getStyleClass().add("Positions-text");
//       codeTextFlow.getChildren().add(positions);
//
//       Text reflector=new Text(selectedParts[2]+">");
//       reflector.getStyleClass().add("Reflector-text");
//       codeTextFlow.getChildren().add(reflector);
//
//       if(selectedParts.length==4)
//       {
//           Text pairs=new Text(selectedParts[3]+">");
//           pairs.getStyleClass().add("PlugBoardPairs-text");
//           codeTextFlow.getChildren().add(pairs);
//
//       }












