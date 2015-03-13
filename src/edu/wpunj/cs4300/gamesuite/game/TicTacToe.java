package edu.wpunj.cs4300.gamesuite.game;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.wpunj.cs4300.gamesuite.Game;

public class TicTacToe extends Game{
	private JFrame frame;
	private JPanel panel;
	Thread mainThread;
	public TicTacToe(boolean isHost) {
		super(isHost);
		mainThread = Thread.currentThread();
		frame = new JFrame("P2PGS - Tic Tac Toe");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowListener(){

			@Override
			public synchronized void windowClosed(WindowEvent e) {
				//System.out.println("Notifying Main");
				//notify();
				mainThread.interrupt();
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
	}
	
	@Override
	protected synchronized void start() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//System.out.println("Main Thread going to sleep.");
		try {
			wait();
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		//System.out.println("Main Thread has been waken up!");
	}
	
}
