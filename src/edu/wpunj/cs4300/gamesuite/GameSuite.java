package edu.wpunj.cs4300.gamesuite;
import java.util.Scanner;

import edu.wpunj.cs4300.gamesuite.game.TicTacToe;


public class GameSuite {
	
	private enum MainMenuOption {LOOK_FOR_GAME, HOST_A_GAME, CHANGE_NAME}
	private enum GameType {TIC_TAC_TOE}
	
	private Scanner consoleIn;
	private String username;
	
	public GameSuite() {
		consoleIn = new Scanner(System.in);
	}
	
	public void start() {
		changeName();
		System.out.println("Welcome, " + username + '!');
		while(true) {
			displayMainMenu();
		}
	}
	private void changeName(){
		username = null;
		while(username == null || username.length() < 2 || username.length() > 20) {
			System.out.print("Please enter your username(2-20 characters): ");
			username = consoleIn.nextLine();
		}
	}
	private void displayMainMenu(){
		System.out.println("What would you like to do?");
		MainMenuOption selection = showMenu(consoleIn, MainMenuOption.values());
		if(selection == null){
			System.out.println("GoodBye!");
			System.exit(0);
		}
		switch(selection){
		case HOST_A_GAME:
			hostGame();
			break;
		case LOOK_FOR_GAME:
			findGame();
			break;
		case CHANGE_NAME:
			changeName();
			break;
		}
	}
	
	public void hostGame(){
		System.out.println("Available Games To Host:");
		GameType gameType = showMenu(consoleIn, GameType.values());
		Game game = null;
		if(gameType == null) {
			return;
		}
		switch(gameType) {
		case TIC_TAC_TOE:
			game = new TicTacToe(true);
			break;
		}
		game.lookForPlayers();
	}
	public void findGame() {
		
	}
	//Shows a list of options for user to enter input. Q will always be one of the options to go back and will return null
	public static <T> T showMenu(Scanner in, T[] items) {
		int index = 0;
		boolean canContinue = false;
		while(!canContinue) {
			System.out.println("Q - Quit");
			for(int i = 0;i<items.length;i++) {
				System.out.println((i+1) + " - " + items[i].toString().replace('_', ' '));
			}
			System.out.print("Please select from one of the option above: ");
			String selection = in.next();
			if(selection.equalsIgnoreCase("Q")){
				return null;
			}
			index = Integer.parseInt(selection);
			if(index > 0 && index <= items.length) {
				canContinue = true;
			}
		}
		return items[index - 1];
	}
}
