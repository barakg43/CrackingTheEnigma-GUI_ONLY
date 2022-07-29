import impl.Roter;

public class Main {
    public static void main(String[] args) {

        Roter roter1=new Roter(6,5,1,true);
        roter1.addMapLatterToRotor('A','F');
        roter1.addMapLatterToRotor('B','E');
        roter1.addMapLatterToRotor('C','D');
        roter1.addMapLatterToRotor('D','C');
        roter1.addMapLatterToRotor('E','B');
        System.out.println("(16-23+26)%26:"+(16+23)%26);
        System.out.println("(16-23+26)%26:"+(16+23+26)%26);
        System.out.println("(16-23+26)%26:"+(16+23+26)%26);
        System.out.println("(7-8)%13:"+(7-8)%13);
        System.out.println("(3-1)%13:"+(3-1)%13);
        System.out.println("(3+1)%13:"+(3+1)%13);
        System.out.println("all mapped :"+roter1.checkIfAllLatterMapped());
        System.out.println("left the index is:2->"+(roter1.getOutputMapIndex(2,false)+1));
        System.out.println("left the index is:4->"+(roter1.getOutputMapIndex(4,false)+1));
        System.out.println("left the index is:3->"+(roter1.getOutputMapIndex(3,false)+1));
        System.out.println("left the index is:1->"+(roter1.getOutputMapIndex(1,false)+1));
        System.out.println("left the index is:4->"+(roter1.getOutputMapIndex(4,false)+1));

        System.out.println("right the index is:2->"+(roter1.getOutputMapIndex(2,true)+1));
        System.out.println("right the index is:4->"+(roter1.getOutputMapIndex(4,true)+1));
        System.out.println("right the index is:3->"+(roter1.getOutputMapIndex(3,true)+1));
        System.out.println("right the index is:1->"+(roter1.getOutputMapIndex(1,true)+1));
        System.out.println("right the index is:4->"+(roter1.getOutputMapIndex(4,true)+1));
        System.out.println("Hello world!");
    }
}