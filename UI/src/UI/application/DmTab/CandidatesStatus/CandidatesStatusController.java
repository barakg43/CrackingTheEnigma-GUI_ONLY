package UI.application.DmTab.CandidatesStatus;

import UI.application.CommonResourcesPaths;
import UI.application.DmTab.CandidatesStatus.singleCandidate.SingleCandidateController;
import UI.application.DmTab.DMTaskComponents.TaskDataController;
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
            singledCandidateTileController.setData(candidateDTO,agentID);
            Platform.runLater(
                    ()->flowPaneCandidates.getChildren().add(singledCandidateTile)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setTaskDataController(TaskDataController taskDataController) {
        this.taskDataController=taskDataController;
    }
}
