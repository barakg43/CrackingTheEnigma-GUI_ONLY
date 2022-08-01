package UI;

import javafx.util.Pair;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import menuEngine.MenuEngine;

public class UserInterface {

    private final static int EXIT = 8;
    private final static int START_OPTION = 1;

    private MenuEngine mEngine;
    private boolean isFirstOptionSelected;

    public UserInterface(MenuEngine menuEngine)
    {

        mEngine=menuEngine;
        isFirstOptionSelected=false;
    }

    public void startMenu(){
        printMenu();
        int option = getOptionAndValidate();

        while(option!=EXIT) {
            switch (option) {
                case 1: {
                    loadMachineDataFile();
                    break;
                }
                case 2:{
                    //  printMachineData();
                    // break;
                   // break;
                }
                case 3: {
                   machineConfByUser();
                    break;
                }
                case 4: {
                    // some method
                  //  break;
                }
                case 5: {
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
        Scanner scanner = new Scanner(System.in);
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

        while(isFirstOptionSelected==false)
        {
            System.out.println("You need first select the first option. ");
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
        System.out.println("Please enter full XML file path: ");
        String filePath = scanner.nextLine();
        try {
            mEngine.LoadXMLFile(filePath);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        System.out.println("The file path loaded successfully.\n");
        return;
    }

    private void machineConfByUser() // case 3
    {
        boolean res=false;
        Scanner scanner = new Scanner(System.in);
        String line;
        int numberOfRotors=0;
        System.out.println("Please enter the number of Rotors:");

        while(!res){
            try {
                numberOfRotors=  mEngine.checkIfNumberOfRotorsValid(scanner.nextLine());
                res=true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        res=false;
        System.out.printf("Please enter %d Rotors with commas between them (for example: 43,27,5):\n",numberOfRotors);
        while(!res) {
            try {
                mEngine.checkIfRotorsValid(scanner.nextLine(), numberOfRotors);
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
                mEngine.checkIfPositionsValid(scanner.nextLine(), numberOfRotors);
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Please select reflector number:\n1. I\n2. II\n3. III\n4. IV\n5. V");
        res=false;
        while(!res) {
            try {
                mEngine.checkIfReflectorNumValid(scanner.nextLine());
                res = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }



        System.out.println("Please select if you want PlugBoard\n1-with plugboard\n2-without plugboard");
        String plugBoard=scanner.nextLine();
        int plugboardNum= Integer.parseInt(plugBoard);
        if(plugboardNum==1) {
            System.out.println("Please enter pairs(without white space) for plugBoard with commas between them for example: AC,BG (A and C connected in the plugBoard, B and G connected in the plugBoard)");
            String plugBoardPairs = scanner.nextLine();
            //mEngine.CheckAndSavePlugBoardPairs(plugBoardPairs);
        }
    }
}
