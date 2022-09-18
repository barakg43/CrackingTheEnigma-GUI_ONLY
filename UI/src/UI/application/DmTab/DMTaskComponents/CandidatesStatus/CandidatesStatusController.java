package UI.application.DmTab.DMTaskComponents.CandidatesStatus;

import UI.application.CommonResourcesPaths;
import UI.application.DmTab.DMTaskComponents.CandidatesStatus.singleCandidate.SingleCandidateController;
import UI.application.DmTab.DMTaskComponents.TaskDataController;
import decryptionManager.components.AtomicCounter;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;

public class CandidatesStatusController {

    @FXML
    private FlowPane flowPaneCandidates;

    private TaskDataController taskDataController;
    private AtomicCounter numberOfCandidates;

    @FXML
    public void initialize(){
        numberOfCandidates=new AtomicCounter();

    }


    public void addAllCandidate(TaskFinishDataDTO taskFinishDataDTO)
    {

        for(CandidateDTO candidateDTO: taskFinishDataDTO.getPossibleCandidates())
        {
            addNewTile(candidateDTO,taskFinishDataDTO.getAgentID());
        }

    }
    private void addNewTile(CandidateDTO candidateDTO,String agentID)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CommonResourcesPaths.MAIN_FXML_RESOURCE);
            Node singledCandidateTile = loader.load();
            SingleCandidateController singledCandidateTileController = loader.getController();
            numberOfCandidates.increment();
            singledCandidateTileController.setData(candidateDTO,agentID);
            Platform.runLater(
                    ()->flowPaneCandidates.getChildren().add(singledCandidateTile)
            );
            Platform.runLater(
                    ()->taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()))
            );

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
public void clearAllTiles()
{
    numberOfCandidates.resetCounter();
    taskDataController.getNumberOfCandidates().setText(String.valueOf(numberOfCandidates.getValue()));

    flowPaneCandidates.getChildren().clear();

}
    public void setTaskDataController(TaskDataController taskDataController) {
        this.taskDataController=taskDataController;
    }

}
