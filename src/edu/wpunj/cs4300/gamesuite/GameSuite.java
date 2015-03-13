package edu.wpunj.cs4300.gamesuite;
import java.util.Scanner;


public class GameSuite {
	
	private enum MainMenuOption {LOOK_FOR_GAME, HOST_A_GAME}
	private enum Game {TIC_TAC_TOE}
	
	private Scanner consoleIn;
	
	
	public GameSuite() {
		consoleIn = new Scanner(System.in);
	}
	
	public void start() {
		while(true) {
			MainMenuOption selection = displayMainMenu();
			switch(selection){
			case HOST_A_GAME:
				hostGame();
				break;
			case LOOK_FOR_GAME:
				findGame();
				break;
			default:
				break;
			}
		}
	}
	
	public MainMenuOption displayMainMenu(){
		boolean canContinue = false;
		while(!canContinue)
		{
			String selection = null;
			System.out.println("==============MENU==============");
			System.out.println("Q - Quit");
			for(int i = 0;i<MainMenuOption.values().length;i++){
				System.out.println((i+1)+" - " + (MainMenuOption.values()[i].toString().replace('_', ' ')));
			}
			System.out.print("What would you like to do: ");
			selection = consoleIn.next();
			//Check to see if they want to quit
			if(selection.equalsIgnoreCase("Q")){
				System.out.println("Goodbye.");
				System.exit(0);
			}
			int sel = Integer.parseInt(selection);
			return MainMenuOption.values()[sel-1];
		}
		System.exit(1);
		return null;
	}
	
	public void hostGame(){
		
	}
	public void findGame() {
		
	}
	//Shows a list of options for user to enter input. Q will always be one of the options to go back and will return null
	public static <T> T[] showMenu(Scanner in, T[] items) {
		System.out.println("Q - Quit");
		for(int i = 0;i<items.length;i++) {
			System.out.println((i+1) + " - " + items[i].toString());
		}
		System.out.print("Please select from one of the option above: ");
		String selection = in.next();
		if(selection.equalsIgnoreCase("Q")){
			return null;
		}
		return null;
	}
}
