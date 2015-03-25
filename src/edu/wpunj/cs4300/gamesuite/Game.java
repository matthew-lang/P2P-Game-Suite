package edu.wpunj.cs4300.gamesuite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Game {
	protected boolean isHost;

	private BufferedWriter output;
	private BufferedReader input;
	protected abstract void start();
	public Game(BufferedReader input, BufferedWriter output) {
		isHost = false;
	}
	public Game(){
		isHost = true;
	}
	public final void lookForPlayers(){
		System.out.println("Waiting for a player to join");
		
		//ServerSocket ss = new ServerSocket(1337);
		//Socket s = ss.accept();
		System.out.println("Player {{NAME}} has been found! Starting game.");
		start();
	}
	
	
	public static Game joinGame(Socket socket) throws IOException{
		//establish streams
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		//figure out what game we're playing
		
		return null;
		
	};
}
