package enigmaEngine;

public interface Encryptor {

    String processDataInput(String dataInput) throws Exception;
    char processDataInput(char charInput) throws Exception;
    void resetProcessingTime();
    void resetCodePosition();
    void addOutputStringToStatics(String input,String output);

}
