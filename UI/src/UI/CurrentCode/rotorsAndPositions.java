<<<<<<<< HEAD:UI/src/UI/application/rotorsAndPositions.java
package UI.application;
========
package UI.CurrentCode;
>>>>>>>> Nikol:UI/src/UI/CurrentCode/rotorsAndPositions.java

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class rotorsAndPositions {
    private SimpleIntegerProperty rotorID;
    private SimpleStringProperty rotorCurrPosition;

    public rotorsAndPositions( int rotorID,String rotorCurrPosition)
    {
        this.rotorID=new SimpleIntegerProperty(rotorID);
        this.rotorCurrPosition=new SimpleStringProperty(rotorCurrPosition);
    }

    public int getRotorID() {
        return rotorID.get();
    }

    public void setRotorID(SimpleIntegerProperty rotorID) {
        this.rotorID = rotorID;
    }

    public void setRotorCurrPosition(SimpleStringProperty rotorCurrPosition) {
        this.rotorCurrPosition = rotorCurrPosition;
    }

    public String getRotorCurrPosition() {
        return rotorCurrPosition.get();
    }
}
