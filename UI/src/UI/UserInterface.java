package UI;

import dtoObjects.*;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static UI.UserInterface.OPTIONS.*;

public class UserInterface {

    private final static int START_OPTION = 1;
    private final Scanner scanner;
    private Engine mEngine;
    private MachineDataDTO machineData;

    private StatisticsDataDTO historyData;

    private SelectedConfigurationDTO selectedData;
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

    public UserInterface()
    {
        currentCode=false;
        mEngine=new EnigmaEngine();
        scanner=new Scanner(System.in);
      //  cipheredInputs=0;
        historyData=null;

       // selectedOptions=new HashSet<>();

    }

    public void startMenu(){
        printMenu();
        try {
            int option = getOptionAndValidate();

            while (option - 1 != EXIT.ordinal()) {
                switch (OPTIONS.values()[option - 1]) {
                    case LOAD_XML: {

                        //    loadMachineDataFile();

                        loadMachineConfigurationFromXmlFile();
                        currentCode = false;

                        ///     selectedOptions.clear();
                        //mEngine.resetAllData();
                        break;

                    }
                    case SHOW_SPECS: {
                        printMachineData();
                        break;
                    }
                    case CHSE_CNFG: {
                       // currentCode = false;
                       // mEngine.resetAllData();
                        machineConfByUser();
                        break;
                    }
                    case AUTO_CONFG: {
                        currentCode = false;
                        mEngine.resetSelected();
                        machineConfAutomatically();
                        break;
                    }
                    case CIPER_DATA: {
                        if (selectedData.getSelectedRotorsID() == null) {
                            System.out.format("machine configuration not set yet.\n" +
                                    "please select the %d or %d option from menu", CHSE_CNFG.ordinal(), AUTO_CONFG.ordinal());
                        } else {
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
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
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

    private int getOptionAndValidate()  {
       String line = checkThatStringInputValid(scanner.nextLine());

       int optionNum = Integer.parseInt(line);

       while(optionNum<START_OPTION || optionNum-1>EXIT.ordinal() )
       {
           System.out.println("You have selected an incorrect option.");
           printMenu();
           line =checkThatStringInputValid(scanner.nextLine());
           optionNum = Integer.parseInt(line);
       }
       while(optionNum!=LOAD_XML.ordinal()+1&&optionNum!=LOAD_DATA.ordinal()+1 && !mEngine.isMachineLoaded())
       {
           System.out.println("You need first load the machine from file.\nPlease select option number 1.");
           printMenu();
           line =checkThatStringInputValid(scanner.nextLine());
           optionNum = Integer.parseInt(line);
       }

       while((optionNum==RST_CODE.ordinal()+1 || optionNum==CIPER_DATA.ordinal()+1) && selectedData==null)
       {
           System.out.println("You need first configure the machine ( option 3 or 4).");
           printMenu();
           line =checkThatStringInputValid(scanner.nextLine());
           optionNum = Integer.parseInt(line);
       }



        if(optionNum-1!=EXIT.ordinal() && (optionNum==STATS.ordinal()+1 && historyData==null))
            System.out.println("Your selection was chosen successfully.");

        if(optionNum==STATS.ordinal()+1 && historyData==null)
            System.out.println("There is no history to show yet.");

        return optionNum;
    }

    private String checkThatStringInputValid(String inputOption)
    {
        while(inputOption.length()>2 || inputOption.equals("") || !Character.isDigit(inputOption.charAt(0)))
        {
            System.out.println("You have selected an incorrect option.");
            printMenu();
            inputOption=scanner.nextLine();
        }
        return inputOption;

    }

    private void loadMachineConfigurationFromXmlFile()  //case 1
    {
            try {
                System.out.println("Please enter full XML file path(with or without path commas): ");
               String xmlPath= scanner.nextLine();
                mEngine.loadXMLFile(xmlPath);
                machineData=mEngine.getMachineData();
                System.out.println("The file path loaded successfully.");
                currentCode=false;
                mEngine.resetAllData();

            } catch (Exception e) {
                System.out.println(e.getMessage()+"\nPlease try again...");

            }

    }

    private void printMachineData()  //case 2
    {
        System.out.println("Machine details:");


        System.out.printf("Amount of rotors in use out of the total amount of rotors : %d / %d \n" , machineData.getNumberOfRotorsInUse(),machineData.getRotorsId().length);

        System.out.printf("Number of reflectors: %d\n",machineData.getNumberOfReflectors());
        System.out.printf("The amount of inputs that have ciphered through the machine so far: %d\n" ,  mEngine.getCipheredInputs());

        if(currentCode) {

            System.out.println("Selected machine code:");
            System.out.println(mEngine.getCodeFormat(true));
            System.out.println("Current machine code:");
            System.out.println(mEngine.getCodeFormat(false));
        }
    }

    private void printHistoricalStaticsData()
    {
        historyData= mEngine.getStatisticDataDTO();
        Map<CodeFormatDTO, List<StatisticRecordDTO>> statisticsList=historyData.getStatisticsData();
        String formatStatistics = "  #. <%s> --> <%s> (%d nano-seconds)\n";
        for(CodeFormatDTO code:statisticsList.keySet())
        {
            System.out.println(code);
            for(StatisticRecordDTO stat: statisticsList.get(code))
            {
                System.out.format(formatStatistics,stat.getInput(),stat.getOutput(),stat.getProcessingTime());
            }
        }
    }


    private void machineConfByUser() // case 3
    {

        if(!rotorsConfig())
        {
            currentCode=selectedData!=null;
            return;
        }
        if(!reflectorConfig())
        {
            currentCode=selectedData!=null;
            return;
        }
        if(!PlugBoardConfig())
        {
            currentCode=selectedData!=null;
            return;
        }

        selectedData = mEngine.getSelectedData();
        mEngine.setInitialCode();
        currentCode=true;

        System.out.println("The data was successfully received.");
    }

    private boolean rotorsConfig()
    {
        boolean res=false;
        System.out.printf("Please enter %d Rotor IDS between 1 and %d with commas between them (for example: 43,27,5):\n\nEnter 'Tab' and 'Enter' to return to the menu\n",
                    machineData.getNumberOfRotorsInUse(),machineData.getNumberOfRotorInSystem());
        while(!res) {
            try {
                String rotors=scanner.nextLine();
                if(rotors.contains("\t")) {
                    System.out.println("You choose not complete the code configuration,revert to previous code configuration");
                    return false;
                }

                mEngine.checkIfRotorsValid(rotors);
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage()+"\nEnter Tab and enter to return to the menu or Enter valid input.");
            }
        }

        System.out.format("Please enter the %d initial positions of the rotors without white spaces between them.\nfor example: ABC " +
                            "(the order between them is: rotor number 43 in position A, rotor number 27 in position B, etc.. )" +
                            "\nthe letters in alphabet of the machine is below(without 'space','space' could be part of alphabet):\n%s\n" +
                            "Enter 'Tab' and 'Enter' to return to the menu\n",machineData.getNumberOfRotorsInUse(),machineData.getAlphabetString());
        res=false;
        while(!res) {
            try {
                String positions=scanner.nextLine();
                if(positions.contains("\t"))
                {
                    System.out.println("You choose not complete the code configuration,revert to previous code configuration");
                    return false;
                }
               mEngine.checkIfPositionsValid(positions);
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage()+"\nEnter Tab and enter to return to the menu or Enter valid input.");
            }
        }

        return true;
    }
    private boolean reflectorConfig()
    {
        boolean res=false;
        System.out.println("Please select reflector number:\nEnter 'Tab' and 'Enter' to return to the menu\n");
        List<String> reflectorIDName=machineData.getReflectorIdList();
        for(int i = 0; i< machineData.getNumberOfReflectors(); i++)
        {
            System.out.printf("%d. %s\n",i+1, reflectorIDName.get(i));
        }
        while(!res) {
            try {
                String reflector = scanner.nextLine();
                if(reflector.contains("\t"))
                {
                    System.out.println("You choose not complete the code configuration,revert to previous code configuration");
                    return false;
                }
                mEngine.checkIfReflectorNumValid(reflector);
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage()+"\nEnter Tab and enter to return to the menu or Enter valid input.");
            }
        }
        return true;
    }

    private boolean PlugBoardConfig()
    {
            System.out.format("Please enter pairs(without white space) for plugBoard.\nFor example: ACBG (A and C connected in the plugBoard, B and G connected in the plugBoard)" +
                    "\nIf you don't want plugBoard pair, press Enter.\nthe letters in alphabet of the machine is below(without 'space','space' could be part of alphabet):\n%s\nEnter 'Tab' and 'Enter' to return to the menu\n",machineData.getAlphabetString());
            boolean res = false;
            while (!res) {
                try {
                    String plugBoardPairsString = scanner.nextLine();
                    if(plugBoardPairsString.contains("\t"))
                    {

                        System.out.println("You choose not complete the code configuration,revert to previous code configuration");
                        return false;
                    }
                    mEngine.checkPlugBoardPairs(plugBoardPairsString);

                    res = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage()+"\nEnter Tab and enter to return to the menu or Enter valid input.");

                }
            }
            return true;

    }

    private void machineConfAutomatically()
    {
        currentCode=true;
        System.out.println("You choose to get machine code automatically. ");
        mEngine.setCodeAutomatically();
        selectedData = mEngine.getSelectedData();
        System.out.println("The automatically selected code is:");
        System.out.println(mEngine.getCodeFormat(true));
//        printCurrentCode(true);
    }

    private void  saveMachineData() {
        System.out.println("You selected to save machine data in file.");
        System.out.println("Please enter the full file(without extension) to save the file:");
        String path = scanner.nextLine();
        mEngine.saveMachineStateToFile(path);
        System.out.println("The data saved successfully.");
    }

    private void loadMachineData() {
        System.out.println("You selected to load the machine data from file.");
        System.out.println("Please enter the full file(without extension) of the file(with or without path commas): ");
        String path = scanner.nextLine();
        try {

        mEngine= EnigmaEngine.loadMachineStateFromFile(path);
        System.out.println("The data was loaded successfully.");
            machineData = mEngine.getMachineData();
            selectedData = mEngine.getSelectedData();
            historyData = mEngine.getStatisticDataDTO();
            currentCode=selectedData!=null;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage()+"\nPlease try again...");

        }
    }
        private void getInputAndCipher()
    {
        System.out.println("Please enter data that you want to chipper:");
        System.out.printf("The Alphabet: %s\n", machineData.getAlphabetString());
        String inputData;
            try {
                inputData = scanner.nextLine();
                System.out.println("output:" + mEngine.cipherData(inputData));
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage()+"\nPlease try again...");

            }



    }
}
