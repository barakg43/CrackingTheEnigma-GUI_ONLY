package dtoObjects;

public class TaskFinishDataDTO {

    private long timeProcessing;
    private int agentID;
    CodeFormatDTO codeConf;

    public TaskFinishDataDTO(long timeProcessing, int agentID, CodeFormatDTO codeConf) {
        this.timeProcessing = timeProcessing;
        this.agentID = agentID;
        this.codeConf = codeConf;
    }

    public long getTimeProcessing() {
        return timeProcessing;
    }

    public int getAgentID() {
        return agentID;
    }

    public CodeFormatDTO getCodeConf() {
        return codeConf;
    }
}
