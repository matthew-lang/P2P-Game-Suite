package edu.wpunj.cs4300.gamesuite.game;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.wpunj.cs4300.gamesuite.Game;
import edu.wpunj.cs4300.gamesuite.GameSuite.GameType;

public class TicTacToe extends Game{
	private JFrame frame;
	private JPanel panel;
	private JButton[] buttons;
	Thread mainThread;
	public TicTacToe(Socket socket, String hostName, String clientName, BufferedReader input, BufferedWriter output){
		super(socket, hostName, clientName, input,output);
		initialize();
	}
	public TicTacToe(String hostName) {
		super(hostName, GameType.TIC_TAC_TOE);
		initialize();
	}
	public void initialize() {
		mainThread = Thread.currentThread();
		frame = new JFrame("P2PGS - Tic Tac Toe");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowListener(){

			@Override
			public synchronized void windowClosed(WindowEvent e) {
				//System.out.println("Notifying Main");
				//notify();
				releaseThread();
			}
			@Override public void windowOpened(WindowEvent e) {}
			@Override public void windowClosing(WindowEvent e) {}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowActivated(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
		});
		panel = new JPanel();
		frame.setContentPane(panel);
		panel.setPreferredSize(new Dimension(800,600));
		initializeGraphics();
	}
	
	public void releaseThread() {
		mainThread.interrupt();
	}
	
	@Override
	protected synchronized void start() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//Host goes first
		if(!isHost){
			startWaitingThread();
		}
		try {
			wait();
		} catch (InterruptedException e) {
			System.out.println("I think the game has ended.");
		}
	}
	
	private void startWaitingThread() {
		Thread t = new Thread(new GameWorker());
		t.start();
	}
	
	
	public void initializeGraphics(){
		panel.setLayout(new GridLayout(3,3));
		buttons = new JButton[9];
		for(int i = 0;i<buttons.length;i++){
			JButton b = new JButton();
			b.addActionListener(new TicTacToeButtonListener(i, b));
			panel.add(b);
			buttons[i] = b;
		}
	}
	
	public boolean checkWinner(String character){
		//9 buttons
		//0 1 2
		//3 4 5
		//6 7 8
		//winning combinations: 012, 048, 036, 147, 258, 345, 678, 642
		if(buttons[0].getText().equals(character)) {
			if(buttons[1].getText().equals(character) && buttons[2].getText().equals(character)) {
				return true;
			}
			else if(buttons[4].getText().equals(character) && buttons[8].getText().equals(character)) {
				return true;
			}
			else if(buttons[3].getText().equals(character) && buttons[6].getText().equals(character)) {
				return true;
			}
		}
		else if(buttons[1].getText().equals(character) && buttons[4].getText().equals(character) && buttons[7].getText().equals(character)) {
			return true;
		}
		else if(buttons[2].getText().equals(character) && buttons[5].getText().equals(character) && buttons[8].getText().equals(character)) {
			return true;
		}
		else if(buttons[3].getText().equals(character) && buttons[4].getText().equals(character) && buttons[5].getText().equals(character)) {
			return true;
		}
		else if(buttons[6].getText().equals(character)) {
			if(buttons[7].getText().equals(character) && buttons[8].getText().equals(character)) {
				return true;
			}
			else if(buttons[4].getText().equals(character) && buttons[2].getText().equals(character)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void exitGame(){
		frame.dispose();
		releaseThread();
	}
	
	public class TicTacToeButtonListener implements ActionListener {
		private JButton button;
		private int index;
		public TicTacToeButtonListener(int index, JButton button){
			this.index = index;
			this.button = button;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			button.setEnabled(false);
			button.setText("X");
			try {
				sendMessage("MOVE " + index);
			} catch (IOException e1) {
				System.out.println("Unable to send move. Exiting Game.");
				e1.printStackTrace();
				exitGame();
			}
			if(checkWinner("X")) {
				JOptionPane.showMessageDialog(null, (isHost?hostName:clientName) + " WINS!");
				exitGame();
				return;
			}
			startWaitingThread();
		}
	}
	
	public class GameWorker implements Runnable {
		@Override
		public void run() {
			boolean hasMoreTurns = false;
			for(int i = 0;i<buttons.length;i++) {
				if(buttons[i].isEnabled()) {
					hasMoreTurns = true;
					buttons[i].setEnabled(false);
				}
			}
			if(!hasMoreTurns) {
				JOptionPane.showMessageDialog(null, "It was a tie!");
				exitGame();
				return;
			}
			//System.out.println("Waiting for Other player to go.");
			String input = null;
			try {
				input = getMessage(60000);
			} catch (IOException e) {
				System.out.println("Waited 60 seconds for other player to make move. Aborting.");
				exitGame();
				return;
			}
			//TODO handle input
			StringTokenizer tok = new StringTokenizer(input);
			if(tok.nextToken().equals("MOVE")) {
				int which = Integer.parseInt(tok.nextToken());
				if(!buttons[which].getText().isEmpty()) {
					System.out.println("Other player is trying to cheat!");
					return;
				}
				buttons[which].setText("O");
				if(checkWinner("O")) {
					JOptionPane.showMessageDialog(null, (isHost?clientName:hostName) + " WINS!");
					exitGame();
				}
			}
			hasMoreTurns = false;
			for(int i = 0;i<buttons.length;i++) {
				if(buttons[i].getText().isEmpty()) {
					hasMoreTurns = true;
					buttons[i].setEnabled(true);
				}
			}
			if(!hasMoreTurns) {
				JOptionPane.showMessageDialog(null, "It was a tie!");
				exitGame();
				return;
			}
		}
	}
	
}
