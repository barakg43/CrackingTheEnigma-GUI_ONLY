package UI;

import impl.reflectorId;
import java.util.*;
import menuEngine.*;

public class UserInterface {

    private final static int EXIT = 8;
    private final static int START_OPTION = 1;
    private final Scanner scanner;
    private final MenuEngine mEngine;
    private MachineDataDTO machineData;
    private SelectedDataDTO selectedData;

    private boolean currentCode;
    private boolean isFirstOptionSelected;
    private boolean withPlugBoardPairs;



    public UserInterface(MenuEngine menuEngine)
    {
        currentCode=false;
        mEngine=menuEngine;
        isFirstOptionSelected=false;
        withPlugBoardPairs=false;
        scanner=new Scanner(System.in);
    }

    public void startMenu(){
        printMenu();
        int option = getOptionAndValidate();

        while(option!=EXIT) {
            switch (option) {
                case 1: {
                    currentCode=false;
                    withPlugBoardPairs=false;
                    loadMachineDataFile();
                    break;
                }
                case 2:{
                      printMachineData();
                     break;
                }
                case 3: {
                    withPlugBoardPairs=false;
                    currentCode=false;
                    machineConfByUser();
                    break;
                }
                case 4: {
                    // some method
                  //  break;
                }
                case 5: {
                    System.out.println("Please enter data that you want to chipper:");
                    String inputData=scanner.nextLine();
                    while (mEngine.checkIfDataValid(inputData))
                    {
                        System.out.println("not valid input,please enter data again");
                        inputData=scanner.nextLine();
                    }

                   // mEngine.checkIfReflectorNumValid(selectedData.getSelectedReflectorID());
                    //mEngine.checkIfRotorsValid(selectedData.getSelectedRotorsID());

                    mEngine.chipperData(inputData);
                    // some method
                   // break;
                }
                case 6: {
                    // some method
                    //break;
                }
                case 7: {
                    // some method
                   // break;
                }
            }
            printMenu();
            option = getOptionAndValidate();

        }
        System.out.println("You selected to exit. Goodbye! ");

    }

    private void printMenu() {
        System.out.printf("\nPlease choose one of the options below ( between %d to %d ):\n",START_OPTION,EXIT);
        String[] MenuOptions =
                {   "1. Load machine data file.",
                        "2. Show machine data.",
                        "3. Initialize machine configuration by yourself.",
                        "4. Initialize machine configuration automatically.",
                        "5. Enter Input for the machine.",
                        "6. Reset machine.",
                        "7. History and statistics of the machine.",
                        "8. Exit"
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

        while(optionNum<START_OPTION || optionNum>EXIT )
        {
            System.out.println("You have selected an incorrect option.");
            printMenu();
            optionNum=scanner.nextInt();
        }
        if(optionNum ==1)
            isFirstOptionSelected=true;

        while(!isFirstOptionSelected)
        {
            System.out.println("You need first select the first option.");
            optionNum=scanner.nextInt();
            if(optionNum ==1)
                isFirstOptionSelected=true;
        }
        if(optionNum!=EXIT)
            System.out.println("Your selection was chosen successfully.");

        return optionNum;
    }

    private void loadMachineDataFile()  //case 1
    {
        Scanner scanner = new Scanner(System.in);
        boolean res=false;
        while(!res){
            try {
                System.out.println("Please enter full XML file path: ");
                String xmlPath= scanner.nextLine();
                mEngine.LoadXMLFile(xmlPath);
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
        System.out.println("The amount of messages that have passed through the machine so far:"); //TODO

        if(currentCode) {
            int[] selectedRotorsArray=selectedData.getSelectedRotorsID();
            char[] selectedPositions= selectedData.getSelectedPositions();

            System.out.println("Current machine code:");
            System.out.print("<");
            for(int i=0;i<selectedRotorsArray.length-1;i++)
            {
                System.out.printf("%d,",selectedRotorsArray[i]);
            }
            System.out.printf("%d",selectedRotorsArray[selectedRotorsArray.length-1]);
            System.out.print(">");

            System.out.print("<");
            for (char selectedPosition : selectedPositions) {
                System.out.printf("%c", selectedPosition);
            }
            System.out.print(">");

            System.out.print("<");
            System.out.printf("%s",selectedData.getSelectedReflectorID());
            System.out.print(">");

            List<String> pairs=selectedData.getPlugBoardPairs();

            if(withPlugBoardPairs)
            {
                System.out.print("<");
                for(int i=0;i<pairs.size()-1;i++)
                {
                    System.out.printf("%c|%c,",pairs.get(i).charAt(0),pairs.get(i).charAt(1));
                }

                System.out.printf("%c|%c",pairs.get(pairs.size()-1).charAt(0),pairs.get(pairs.size()-1).charAt(1));
                System.out.println(">");
            }
            else{
                System.out.println("\n");
            }
        }

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
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Please enter %d Rotors with commas between them (for example: 43,27,5):\n",machineData.getNumberOfRotorsInUse());
        while(!res) {
            try {
                mEngine.checkIfRotorsValid(scanner.nextLine());
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Please enter the initial positions (in UPPER CASE) of the rotors without white spaces between them.\nfor example: ABC " +
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
        Scanner scanner = new Scanner(System.in);
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
        Scanner scanner = new Scanner(System.in);
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
}
