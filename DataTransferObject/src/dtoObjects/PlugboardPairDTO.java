package dtoObjects;

import java.io.Serializable;

public class PlugboardPairDTO implements Serializable {

   private final char firstLetter;
   private final char secondLetter;

    public PlugboardPairDTO(char firstLetter, char secondLetter) {
        this.firstLetter = firstLetter;
        this.secondLetter = secondLetter;
    }

    public char getFirstLetter() {
        return firstLetter;
    }

    public char getSecondLetter() {
        return secondLetter;
    }


    @Override
    public boolean equals(Object obj) {
        //if the other object is null - and this object is definitely not null
        //return false
        if (obj == null) {
            return false;
        }

        if (obj == this){
            return true;
        }

        //compare the other object Class to mine
        //if we are not the same exact class - return false
        //another option was to use
        if (!(obj instanceof PlugboardPairDTO)) {
//        if (this.getClass() != obj.getClass()) {
            return false;
        }

        //if we got here then obj is of type PlugboardPairDTO
        //we can case it to another reference in order to easily compare
        //between selected data members
        final PlugboardPairDTO other = (PlugboardPairDTO) obj;

        //compare the data member name ,there is no meaning to the order in the object(first or second)
        return  (firstLetter==other.firstLetter &&secondLetter==other.secondLetter)
                ||(secondLetter==other.secondLetter&&secondLetter== other.firstLetter);

    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + firstLetter+secondLetter;
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%c|%c",firstLetter,secondLetter);
    }
}
