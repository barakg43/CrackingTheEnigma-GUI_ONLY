package UI.application.DmTab;

import decryptionManager.DecryptionManager;
import javafx.fxml.FXML;

public class DMcontroller {


private DecryptionManager decryptionManager;
Thread candidateListener;

    @FXML
    public void initialize()
    {
     //   decryptionManager=new DecryptionManager()

        candidateListener=new Thread("candidate taker");

    }

}
