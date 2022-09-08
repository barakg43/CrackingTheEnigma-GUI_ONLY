package dtoObjects;

public class TaskFinishDataDTO {

    private long timeProcessing;
    private String agentID;
    CodeFormatDTO codeConf;

    public TaskFinishDataDTO( String agentID, CodeFormatDTO codeConf) {
        this.agentID = agentID;
        this.codeConf = codeConf;
    }



    public String getAgentID() {
        return agentID;
    }

    public CodeFormatDTO getCodeConf() {
        return codeConf;
    }
}
