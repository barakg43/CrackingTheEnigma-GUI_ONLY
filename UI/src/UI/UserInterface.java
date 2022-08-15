package UI;
import dtoObjects.MachineDataDTO;
import dtoObjects.SelectedConfigurationDTO;
import dtoObjects.StatisticsDataDTO;
import menuEngine.StatisticRecord;
//import enigmaMachine.parts.reflectorId;
import menuEngine.*;


import java.io.*;
import java.util.*;

import static UI.UserInterface.OPTIONS.*;

public class UserInterface {

    private final static int START_OPTION = 1;
    private final Scanner scanner;
    private Engine mEngine;
    private MachineDataDTO machineData;
    private SelectedConfigurationDTO selectedData;
    private StatisticsDataDTO historyData;
  //  private Set<Integer> selectedOptions;
    protected enum  OPTIONS{  LOAD_XML,
                            SHOW_SPECS,
                            CHSE_CNFG,
                            AUTO_CONFG,
                            CIPER_DATA,
                            RST_CODE,
                            STATS,
                            SAVE_DATA,
                            LOAD_DATA,
                            EXIT
    }
    private boolean currentCode;
    private boolean isFirstOptionSelected;
    private boolean withPlugBoardPairs;

  //  private int cipheredInputs;

    public UserInterface()
    {
        currentCode=false;
        mEngine=new MenuEngine();
        isFirstOptionSelected=false;
        withPlugBoardPairs=false;
        scanner=new Scanner(System.in);
        selectedData=null;
      //  cipheredInputs=0;
        historyData=null;
       // selectedOptions=new HashSet<>();

    }

    public void startMenu(){
        printMenu();
        int option = getOptionAndValidate();

        while(option-1!=EXIT.ordinal()) {
            switch (OPTIONS.values()[option-1]) {
                case LOAD_XML: {

                  loadMachineConfigurationFromXmlFile();

                    currentCode=false;
                    withPlugBoardPairs=false;
                    //selectedOptions.clear();
                    mEngine.resetAllData();

                  //  loadMachineDataFile();

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
                       
                    }
                    break;
                }

                case RST_CODE: {
                    System.out.println("you selected to reset the rotors. ");
                    mEngine.resetCodePosition();
                    System.out.println("The code reset was made successfully");
                    break;
                }
                case STATS: {
                    printHistoricalStaticsData();
                     break;
                }
                case SAVE_DATA: {
                    saveMachineData();
                    break;
                }
                case LOAD_DATA: {
                    loadMachineData();
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
                {       "########################################################",
                        "# 1.  Load machine data file.                          #",
                        "# 2.  Show machine data.                               #",
                        "# 3.  Initialize machine configuration by yourself.    #",
                        "# 4.  Initialize machine configuration automatically.  #",
                        "# 5.  Enter Input for the machine.                     #",
                        "# 6.  Reset machine.                                   #",
                        "# 7.  History and statistics of the machine.           #",
                        "# 8.  Save machine data.                               #",
                        "# 9.  Load recent machine data.                        #",
                        "# 10. Exit.                                            #",
                        "########################################################"
                };

        for (String option : MenuOptions){
            System.out.println(option);
        }
    }

    private int getOptionAndValidate()
    {
        String line= scanner.nextLine();
        int optionNum ;
        while(line.length()>2)
        {
            System.out.println("You have selected an incorrect option.");
            printMenu();
            line=scanner.nextLine();
        }
        optionNum=Integer.parseInt(line);

        while(optionNum<START_OPTION || optionNum-1>EXIT.ordinal() )
        {
            System.out.println("You have selected an incorrect option.");
            printMenu();
            optionNum=scanner.nextInt();
        }

//        if(optionNum ==1)
//            isFirstOptionSelected=true;
//
//        if(selectedOptions.contains(LOAD_DATA.ordinal()+1))
//            isFirstOptionSelected=true;
        while(optionNum!=LOAD_XML.ordinal()+1&&optionNum!=LOAD_DATA.ordinal()+1 && !mEngine.isMachineLoaded())
        {

           System.out.println("You need first load the machine from file.\nPlease select option number 1.");
            printMenu();

            line=scanner.nextLine();
            optionNum=Integer.parseInt(line);
        }


        while(optionNum==CIPER_DATA.ordinal()+1 && selectedData==null)
        {
            System.out.format("Before ciphering data, you need to configure the machine.\n" +
                    "Please select option number %d or %d", CHSE_CNFG.ordinal()+1,AUTO_CONFG.ordinal()+1);
            line=scanner.nextLine();
            optionNum=Integer.parseInt(line);
        }


        if(optionNum-1!=EXIT.ordinal() && (optionNum==STATS.ordinal()+1 && historyData==null))
            System.out.println("Your selection was chosen successfully.");


        if(optionNum==STATS.ordinal()+1 && historyData==null)
        {
            System.out.println("There is no history to show yet.");
        }

        return optionNum;
    }

    private void loadMachineConfigurationFromXmlFile()  //case 1
    {

//        boolean res=false;
//        while(!res){
            try {
               //  "C:\\Users\\nikol\\Desktop\\java\\new\\CrackingTheEnigma\\src\\Resources\\ex1-sanity-small.xml"
                System.out.println("Please enter full XML file path: ");
              String xmlPath= scanner.nextLine();
//                String xmlPath=  "C:\\ComputerScience\\Java\\EXCISES\\TEST-Files\\EX 1\\ex1-sanity-small.xml";
                mEngine.LoadXMLFile(xmlPath);
                machineData=mEngine.getMachineData();
                System.out.println("The file path loaded successfully.");
                currentCode=false;
                withPlugBoardPairs=false;
                //selectedOptions.clear();
                mEngine.resetAllData();
              //  res=true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        //}


    }

    private void printMachineData()  //case 2
    {
        System.out.println("\nMachine details:");



        System.out.printf("Amount of rotors in use out of the total amount of rotors : %d / %d \n" , machineData.getNumberOfRotorsInUse(),machineData.getRotorsId().length);

        System.out.printf("Number of reflectors: %d\n",machineData.getNumberOfReflectors());
        System.out.printf("The amount of inputs that have ciphered through the machine so far: %d\n" ,  mEngine.getCipheredInputs());

        if(currentCode) {
            int[] notchArray=selectedData.getNotchPositions();
            int[] rotorsArray=selectedData.getSelectedRotorsID();
            System.out.println("Position of notch from window position in each rotor:");
            for(int i=0;i<selectedData.getNotchPositions().length;i++)
            {
                System.out.printf("Rotor number: %d , notch position from window position: %d\n" , rotorsArray[i],notchArray[i]);
            }
            System.out.println("Current machine code:");
            printCurrentCode();
        }

    }

    private void printHistoricalStaticsData()
    {
        historyData= mEngine.getStatisticDataDTO();
        Map<String, List<StatisticRecord>> statisticsList=historyData.getStatisticsData();
        String formatStatistics = "  #. <%s> --> <%s> (%d nano-seconds)\n";

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
        System.out.printf("Please enter %d Rotor IDS between 1 and %d with commas between them (for example: 43,27,5):\n",
                    machineData.getNumberOfRotorsInUse(),mEngine.getNumberOfRotorInSystem());
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
        List<String> reflectorIDName=mEngine.getReflectorIdList();
        for(int i=0;i<numOfReflectors;i++)
        {
            System.out.printf("%d. %s\n",i+1, reflectorIDName.get(i));
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

            withPlugBoardPairs=true;
            System.out.println("Please enter pairs(without white space) for plugBoard.\nFor example: ACBG (A and C connected in the plugBoard, B and G connected in the plugBoard)" +
                    "\nIf you don't want plugBoard pair, press Enter.");
            boolean res = false;
            while (!res) {
                try {
                    String plugBoardPairs = scanner.nextLine();
                    mEngine.checkPlugBoardPairs(plugBoardPairs);
                    res = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

    }

    private void machineConfAutomatically()
    {
        currentCode=true;
        System.out.println("You choose to get machine code automatically. ");
        mEngine.setCodeAutomatically();
        withPlugBoardPairs=mEngine.getWithPlugBoardPairs();
        selectedData = mEngine.getSelectedData();
        System.out.println("The automatically selected code is:");
        printCurrentCode();
    }

    private void  saveMachineData() {
        System.out.println("You selected to save machine data in file.");
        System.out.println("Please enter the full file(without extension) to save the file:");
        String path = scanner.nextLine();
//        path+=".bat";
//        try (ObjectOutputStream out =
//                     new ObjectOutputStream(
//                             new FileOutputStream(path))) {
//            out.writeObject(mEngine);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        mEngine.saveMachineStateToFile(path);
        System.out.println("The data saved successfully.");
    }

    private void loadMachineData() {
        System.out.println("You selected to load the machine data from file.");
        System.out.println("Please enter the full file(without extension) of the file: ");
        String path = scanner.nextLine();
//        path=path.replaceAll("\"","");//for case user enter with " "
//        path += ".bat";
//        File file = new File(path);
//
//
//
//        while (!file.exists()) {
//            System.out.println("This file doesn't exists. PLease enter valid file path:");
//            path = scanner.nextLine();
//            path += ".bat";
//            file = new File(path);
//        }
//
//        try (ObjectInputStream in =
//                     new ObjectInputStream(
//                             new FileInputStream(path))) {
//            MenuEngine menuEngine =
//                    (MenuEngine) in.readObject();
//            this.mEngine = menuEngine;
//            machineData = mEngine.getMachineData();
//            selectedData = mEngine.getSelectedData();
//            historyData = mEngine.getStatisticDataDTO();
        try {
        mEngine=MenuEngine.loadMachineStateFromFile(path);
        System.out.println("The data was loaded successfully.");

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

        private void getInputAndCipher()
    {
        System.out.println("Please enter data that you want to chipper:");
        String inputData;
        boolean validInput=true;

        do {
            try {
                inputData = scanner.nextLine();

                System.out.println("output:" + mEngine.cipherData(inputData));
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage() + "\nnot valid input,please enter data again");
                validInput=false;
            }
        } while (!validInput);


    }
}
