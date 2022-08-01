package UI;

import EnigmaMachine.Mapper;
import menuEngine.MenuEngine;
import impl.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        MenuEngine menuEngine = new MenuEngine();
        UserInterface ui = new UserInterface(menuEngine);
       //1 testChipper();
        ui.startMenu();
    }



}