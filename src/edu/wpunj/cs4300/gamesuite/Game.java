package edu.wpunj.cs4300.gamesuite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import edu.wpunj.cs4300.gamesuite.GameSuite.GameType;
import edu.wpunj.cs4300.gamesuite.game.TicTacToe;

public abstract class Game {
	protected boolean isHost;

	private BufferedWriter output;
	private BufferedReader input;
	private Socket socket;
	private GameType gameType;
	protected String hostName;
	protected String clientName;
	protected abstract void start();
	public Game(Socket socket, String hostName, String clientName, BufferedReader input, BufferedWriter output) {
		this.socket = socket;
		this.hostName = hostName;
		this.clientName = clientName;
		this.input = input;
		this.output = output;
		isHost = false;
	}
	public Game(String hostName, GameType gameType){
		this.hostName = hostName;
		this.gameType = gameType;
		isHost = true;
	}
	public final void lookForPlayers() throws IOException{
		System.out.println("Waiting for a player to join");
		//open server socker
		ServerSocket ss = new ServerSocket(GameSuite.GAME_PORT);
		//wait for player to join
		Socket s = ss.accept();
		this.socket = s;
		//set up input/output streams
		input = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
		output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
		//send name of game and player's name
		output.write(gameType + " " + hostName + "\n");
		output.flush();
		//user sends their name back
		clientName = getMessage(30000);
		System.out.println("Player "+clientName+" has been found! Starting game.");
		start();
		ss.close();
		//TODO close server sockets
	}
	
	
	public static Game joinGame(String clientName, Socket socket) throws IOException{
		//establish streams
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		//figure out what game we're playing
		String in = getMessage(input, 30000);
		GameType gameType = GameType.valueOf(in.substring(0, in.indexOf(' ')));
		String hostName = in.substring(in.indexOf(' ')+1).trim();
		System.out.println("Game: " + gameType);
		System.out.println("Host: " + hostName);
		output.write(clientName + "\n");
		output.flush();
		Game game = null;
		switch(gameType) {
		case TIC_TAC_TOE: 
			game = new TicTacToe(socket, hostName, clientName, input, output);
			break;
		}
		return game;
		
	}
	
	public void exitGame() {
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMessage(long timeout) throws IOException {
		return Game.getMessage(input, timeout);
	}
	
	private static String getMessage(BufferedReader input, long timeout) throws IOException {
		int count = 0;
		long maxCount = timeout / 100;
		while(!input.ready()) {
			try{Thread.sleep(100);}catch(Exception e){}
			count++;
			//after 30 seconds just give up!
			if(count > maxCount){
				throw new IOException("Timed out after " + timeout + " milliseconds");
			}
		}
		String in = input.readLine();
		return in;
	}
	
	public void sendMessage(String message) throws IOException {
		output.write(message + "\n");
		output.flush();
	}
}
