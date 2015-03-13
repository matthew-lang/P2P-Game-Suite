package edu.wpunj.cs4300.gamesuite;

public abstract class Game {
	protected boolean isHost;
	protected abstract void start();
	public Game(boolean isHost) {
		this.isHost = isHost;
	}
	public final void lookForPlayers(){
		System.out.println("Waiting for a player to join");
		//TODO add socket p2p logic here
		System.out.println("Player {{NAME}} has been found! Starting game.");
		start();
	}
}
