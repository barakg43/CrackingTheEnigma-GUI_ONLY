package UI;

import dtoObjects.*;
import menuEngine.Engine;
import menuEngine.MenuEngine;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static UI.UserInterface.OPTIONS.*;

public class UserInterface {

    private final static int START_OPTION = 1;
    private final Scanner scanner;
    private Engine mEngine;
    private MachineDataDTO machineData;
    private SelectedConfigurationDTO selectedData;
    private StatisticsDataDTO historyData;

    private boolean isDataCipered;
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
        isDataCipered=false;
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
                        withPlugBoardPairs = false;
                        ///     selectedOptions.clear();
                        //mEngine.resetAllData();
                        break;

                    }
                    case SHOW_SPECS: {
                        printMachineData();
                        break;
                    }
                    case CHSE_CNFG: {
                        withPlugBoardPairs = false;
                        currentCode = false;
                        isDataCipered=false;
                       // mEngine.resetSelected();
                        machineConfByUser();
                        break;
                    }
                    case AUTO_CONFG: {
                        withPlugBoardPairs = false;
                        currentCode = false;
                        isDataCipered=false;
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

    private int getOptionAndValidate() throws Exception {
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

        boolean res=false;
       // while(!res){
            try {
               //  "C:\\Users\\nikol\\Desktop\\java\\new\\CrackingTheEnigma\\src\\Resources\\ex1-sanity-small.xml"
                System.out.println("Please enter full XML file path: ");
               String xmlPath= scanner.nextLine();
              //
                // if(xmlPath.contains(""))
//                String xmlPath=  "C:\\ComputerScience\\Java\\EXCISES\\TEST-Files\\EX 1\\ex1-sanity-small.xml";
                mEngine.LoadXMLFile(xmlPath);
                machineData=mEngine.getMachineData();
                System.out.println("The file path loaded successfully.");
                currentCode=false;
                withPlugBoardPairs=false;
                //selectedOptions.clear();
                mEngine.resetAllData();
                res=true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //System.out.println("Please enter full XML file path or Press Esc to go back to main menu.");
            }
       // }

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
//    private void printCurrentCode(boolean selectedCode)
//    {
//        System.out.println(mEngine.getCodeFormat(selectedCode,false));
//    }

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
        currentCode=true;

        System.out.println("The data was successfully received.");
    }

    private boolean rotorsConfig()
    {
        boolean res=false;
        System.out.printf("Please enter %d Rotor IDS between 1 and %d with commas between them (for example: 43,27,5):\n",
                    machineData.getNumberOfRotorsInUse(),mEngine.getNumberOfRotorInSystem());
        while(!res) {
            try {
                String rotors=scanner.nextLine();
                if(rotors.contains("\t"))
                    return false;
                mEngine.checkIfRotorsValid(rotors);
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage()+"\nEnter Tab and enter to return to the menu or Enter valid input.");
            }
        }

        System.out.format("Please enter the initial positions of the rotors without white spaces between them.\nfor example: ABC " +
                "(the order between them is: rotor number 43 in position A, rotor number 27 in position B, etc.. )\nthe letters in alphabet of the machine is below(without 'space','space' could be part of alphabet):\n%s\n",mEngine.getAlphabetString());
        res=false;
        while(!res) {
            try {
                String positions=scanner.nextLine().toUpperCase();
                if(positions.contains("\t"))
                {
                   // mEngine.resetSelected();
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
        int numOfReflectors= machineData.getNumberOfReflectors();
        System.out.println("Please select reflector number: ");
        List<String> reflectorIDName=mEngine.getReflectorIdList();
        for(int i=0;i<numOfReflectors;i++)
        {
            System.out.printf("%d. %s\n",i+1, reflectorIDName.get(i));
        }
        while(!res) {
            try {
                String reflector = scanner.nextLine();
                if(reflector.contains("\t"))
                {
                   // mEngine.resetSelected();
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

            withPlugBoardPairs=true;
            System.out.format("Please enter pairs(without white space) for plugBoard.\nFor example: ACBG (A and C connected in the plugBoard, B and G connected in the plugBoard)" +
                    "\nIf you don't want plugBoard pair, press Enter.\nthe letters in alphabet of the machine is below(without 'space','space' could be part of alphabet):\n%s\n",mEngine.getAlphabetString());
            boolean res = false;
            while (!res) {
                try {
                    String plugBoardPairs = scanner.nextLine().toUpperCase();
                    if(plugBoardPairs.contains("\t"))
                    {
                       // mEngine.resetSelected();
                        return false;
                    }
                    mEngine.CheckPlugBoardPairs(plugBoardPairs);

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
        withPlugBoardPairs=mEngine.getWithPlugBoardPairs();
        selectedData = mEngine.getSelectedData();
        System.out.println("The automatically selected code is:");
        System.out.println(mEngine.getCodeFormat(true));
//        printCurrentCode(true);
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
            machineData = mEngine.getMachineData();
            selectedData = mEngine.getSelectedData();
            historyData = mEngine.getStatisticDataDTO();
            currentCode=selectedData!=null;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());

        }
    }
        private void getInputAndCipher()
    {
        System.out.println("Please enter data that you want to chipper:");
        System.out.printf("The Alphabet: %s\n", mEngine.getAlphabetString());
        String inputData;
        boolean validInput=true;

       // do {
            try {
                inputData = scanner.nextLine();
                System.out.println("output:" + mEngine.cipherData(inputData));
                isDataCipered=true;
                mEngine.addCipheredInputs();
            }
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
             //   validInput=false;
            }
      //  } while (!validInput);


    }
}
