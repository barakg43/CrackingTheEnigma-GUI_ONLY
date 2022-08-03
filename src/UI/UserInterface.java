package UI;
import impl.StatisticRecord;
import impl.reflectorId;
import menuEngine.*;


import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static UI.UserInterface.OPTIONS.*;

public class UserInterface {

    private final static int START_OPTION = 1;
    private final Scanner scanner;
    private final MenuEngine mEngine;
    private MachineDataDTO machineData;
    private SelectedConfigurationDTO selectedData;
    protected enum  OPTIONS{  LOAD_XML,
                            SHOW_SPECS,
                            CHSE_CNFG,
                            AUTO_CONFG,
                            CIPER_DATA,
                            RST_CODE,
                            STATS,
                            EXIT}
    private boolean currentCode;
    private boolean isFirstOptionSelected;
    private boolean withPlugBoardPairs;

    private int cipheredInputs;

    public UserInterface(MenuEngine menuEngine)
    {
        currentCode=false;
        mEngine=menuEngine;
        isFirstOptionSelected=false;
        withPlugBoardPairs=false;
        scanner=new Scanner(System.in);
        cipheredInputs=0;


    }

    public void startMenu(){
        printMenu();
        int option = getOptionAndValidate();

        while(option-1!=EXIT.ordinal()) {
            switch (OPTIONS.values()[option-1]) {
                case LOAD_XML: {
                    currentCode=false;
                    withPlugBoardPairs=false;
                    loadMachineDataFile();
                    break;
                }
                case SHOW_SPECS:{
                    printMachineData();
                    break;
                }
                case CHSE_CNFG: {
                    withPlugBoardPairs=false;
                    currentCode=false;
                    mEngine.resetSelected();
                    machineConfByUser();
                    break;
                }
                case AUTO_CONFG: {
                    withPlugBoardPairs=false;
                    currentCode=false;
                    mEngine.resetSelected();
                    machineConfAutomatically();
                    break;
                }
                case CIPER_DATA: {
                    if(selectedData.getSelectedRotorsID()==null)
                    {
                        System.out.format("machine configuration not set yet.\n" +
                                "please select the %d or %d option from menu",CHSE_CNFG.ordinal(),AUTO_CONFG.ordinal());
                    }
                    else{
                        getInputAndCipher();
                        cipheredInputs++;
                    }
                    break;
                }

                case RST_CODE: {
                    System.out.println("you selected to reset the rotors. ");
                    mEngine.resetCodePosition();
                    System.out.println("The reset successfully");
                    break;
                }
                case STATS: {
                    printHistoricalStaticsData();
                     break;
                }
            }
            printMenu();
            option = getOptionAndValidate();

        }
        System.out.println("You selected to exit. Goodbye! ");

    }

    private void printMenu() {
        System.out.printf("\nPlease choose one of the options below ( between %d to %d ):\n",START_OPTION,EXIT.ordinal()+1);
        String[] MenuOptions =
                {       "# 1. Load machine data file.",
                        "# 2. Show machine data.",
                        "# 3. Initialize machine configuration by yourself.",
                        "# 4. Initialize machine configuration automatically.",
                        "# 5. Enter Input for the machine.",
                        "# 6. Reset machine.",
                        "# 7. History and statistics of the machine.",
                        "# 8. Exit"
                };

        for (String option : MenuOptions){
            System.out.println(option);
        }
    }

    private int getOptionAndValidate()
    {
        String line= scanner.nextLine();
        int optionNum ;
        while(line.length()!=1)
        {
            System.out.println("You have selected an incorrect option.");
            printMenu();
            line=scanner.nextLine();
        }
        optionNum=Integer.parseInt(line);

        while(optionNum<START_OPTION || optionNum>EXIT.ordinal() )
        {
            System.out.println("You have selected an incorrect option.");
            printMenu();
            optionNum=scanner.nextInt();
        }
        if(optionNum ==1)
            isFirstOptionSelected=true;

        while(!isFirstOptionSelected)
        {
            System.out.println("You need first load the machine from file.");
            optionNum=scanner.nextInt();
            if(optionNum ==1)
                isFirstOptionSelected=true;
        }
        if(optionNum!=EXIT.ordinal())
            System.out.println("Your selection was chosen successfully.");

        return optionNum;
    }

    private void loadMachineDataFile()  //case 1
    {

        boolean res=false;
        while(!res){
            try {
                System.out.println("Please enter full XML file path: ");
                // String xmlPath= scanner.nextLine();
                mEngine.LoadXMLFile("C:\\ComputerScience\\Java\\EXCISES\\CrackingTheEnigma\\src\\Resources\\ex1-sanity-paper-enigma.xml");
                machineData=mEngine.getMachineData();
                res=true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("The file path loaded successfully.");
    }

    private void printMachineData()  //case 2
    {
        System.out.println("\nMachine details:");

        int[] rotorsArray=machineData.getRotorsId();
        int[] notchArray=machineData.getNotchNums();

        System.out.printf("Amount of rotors in use out of the total amount of rotors : %d / %d \n" , machineData.getNumberOfRotorsInUse(),machineData.getRotorsId().length);
        System.out.println("Position of notch in each rotor:");
        for(int i=0;i<machineData.getRotorsId().length;i++)
        {
            System.out.printf("Rotor number: %d , notch position: %d\n" , rotorsArray[i],notchArray[i]);
        }
        System.out.printf("Number of reflectors: %d\n",machineData.getNumberOfReflectors());
        System.out.printf("The amount of inputs that have ciphered through the machine so far: %d\n" , cipheredInputs);

        if(currentCode) {
            System.out.println("Current machine code:");
            printCurrentCode();
        }

    }

    private void printHistoricalStaticsData()
    {
        Map<String, List<StatisticRecord>> statisticsList=mEngine.getStatisticDataDTO().getStatisticsData();
        String formatStatistics = "  #. <%s> --> <%s> (%d)\n";

        for(String code:statisticsList.keySet())
        {
            System.out.println(code);
            for(StatisticRecord stat: statisticsList.get(code))
            {
                System.out.format(formatStatistics,stat.getInput(),stat.getOutput(),stat.getProcessingTime());
            }
        }
    }
    private void printCurrentCode()
    {
        System.out.println(mEngine.getCodeFormat());
    }

    private void machineConfByUser() // case 3
    {
        currentCode=true;
        rotorsConfig();
        reflectorConfig();
        PlugBoardConfig();
        selectedData = mEngine.getSelectedData();

        System.out.println("The data was successfully received.");
    }

    private void rotorsConfig()
    {
        boolean res=false;
        System.out.printf("Please enter %d Rotors with commas between them (for example: 43,27,5):\n",machineData.getNumberOfRotorsInUse());
        while(!res) {
            try {
                mEngine.checkIfRotorsValid(scanner.nextLine());
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Please enter the initial positions of the rotors without white spaces between them.\nfor example: ABC " +
                "(the order between them is: rotor number 43 in position A, rotor number 27 in position B, etc.. )");
        res=false;
        while(!res) {
            try {
                mEngine.checkIfPositionsValid(scanner.nextLine());
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
    private void reflectorConfig()
    {
        boolean res=false;
        int numOfReflectors= machineData.getNumberOfReflectors();
        System.out.println("Please select reflector number: ");
        for(int i=0;i<numOfReflectors;i++)
        {
            System.out.printf("%d. %s\n",i+1, reflectorId.values()[i]);
        }
        while(!res) {
            try {
                mEngine.checkIfReflectorNumValid(scanner.nextLine());
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void PlugBoardConfig()
    {
        boolean res=false;
        System.out.println("Please select if you want PlugBoard\n1-with plugboard\n2-without plugboard");
        int plugboardNum=2;
        while(!res) {
            try {
                plugboardNum = mEngine.checkPlugBoardNum( scanner.nextLine());
                res=true;
            }catch (Exception e) {
                System.out.println("Please choose 1 or 2.");
            }
        }

        if(plugboardNum==1) {
            withPlugBoardPairs=true;
            System.out.println("Please enter pairs(without white space) for plugBoard with commas between them for example: AC,BG (A and C connected in the plugBoard, B and G connected in the plugBoard)");
            res = false;
            while (!res) {
                try {
                    String plugBoardPairs = scanner.nextLine();
                    mEngine.CheckPlugBoardPairs(plugBoardPairs);
                    res = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else{
            System.out.println("you choose without plugBoard pairs.");
        }

    }

    private void machineConfAutomatically()
    {
        currentCode=true;
        System.out.println("You choose to get machine code automatically. ");
        mEngine.getCodeAutomatically();
        withPlugBoardPairs=mEngine.getWithPlugBoardPairs();
        selectedData = mEngine.getSelectedData();
        System.out.println("The automatically selected code is:");
        printCurrentCode();
    }

    private void getInputAndCipher()
    {
        System.out.println("Please enter data that you want to chipper:");
//                    String inputData=scanner.nextLine();
//                    while (!mEngine.checkIfDataValid(inputData))
//                    {
//                        System.out.println("not valid input,please enter data again");
//                        inputData = scanner.nextLine();
//                    }

        //mEngine.checkIfReflectorNumValid(selectedData.getSelectedReflectorID());
        //mEngine.checkIfRotorsValid(selectedData.getSelectedRotorsID());
        String inputData="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("output:"+mEngine.cipherData(inputData));
    }
}
