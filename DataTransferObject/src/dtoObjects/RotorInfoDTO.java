package dtoObjects;

import java.io.Serializable;

public class RotorInfoDTO implements Serializable {
    private final int id;
    private final int distanceToWindow;
    private final char statingLetter;

    public RotorInfoDTO(int id,int distanceToWindow,char statingLetter){
        this.id=id;
        this.distanceToWindow=distanceToWindow;
        this.statingLetter=statingLetter;

    }

    public int getId() {
        return id;
    }

    public int getDistanceToWindow() {
        return distanceToWindow;
    }

    public char getStatingLetter() {
        return statingLetter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash +id+distanceToWindow+statingLetter;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RotorInfoDTO other = (RotorInfoDTO) obj;
        return (id == other.id) && (distanceToWindow == other.distanceToWindow) && (statingLetter == other.statingLetter);
    }
}
