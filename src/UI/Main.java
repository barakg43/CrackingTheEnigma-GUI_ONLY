package UI;

import menuEngine.MenuEngine;

import java.util.Scanner;
public class Main {

   private static MenuEngine menuEngine;
    private static UserInterface ui;

    public static void main(String[] args) {
        menuEngine = new MenuEngine();
        ui=new UserInterface(menuEngine);
        ui.startMenu();
    }


}