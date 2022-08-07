package menuEngine;

public interface Engine {

    public MachineDataDTO getMachineData();

    public SelectedConfigurationDTO getSelectedData();

    public void LoadXMLFile(String filePath);

    public void checkIfRotorsValid(String rotors);

    public boolean checkIfDataValid(String data);

    public StatisticsDataDTO getStatisticDataDTO();

    public void checkIfPositionsValid(String positions);

    public String cipherData(String dataInput);

    public void resetCodePosition();

    public String getCodeFormat();

    public void resetSelected();

    public void checkIfReflectorNumValid(String ReflectorNum);

    public void CheckPlugBoardPairs(String pairs);

    public int checkPlugBoardNum(String plugBoardNum);

    public void getCodeAutomatically();

    public boolean getWithPlugBoardPairs();
    public int getCipheredInputs();
    public int addCipheredInputs();
    @Override
    public String toString();

    }
