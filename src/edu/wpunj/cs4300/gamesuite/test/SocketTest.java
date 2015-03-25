package edu.wpunj.cs4300.gamesuite.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {
	public static void main(String[] args) {
		new SocketTest().test();
	}
	public static final int PORT = 4567;
	
	public void test(){
		ServerSocket ss = null;
		System.out.println("SERVER: Opening ServerSocket");
		try {
			ss = new ServerSocket(PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SERVER: Starting Client Thread");
		Thread t = new Thread(new ClientThread());
		t.start();
		try {
			Socket s = ss.accept();
			System.out.println("SERVER: Connected to a Client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class ClientThread implements Runnable {
		public void run(){
			System.out.println("CLIENT: connecting to socket");
			try {
				Socket s = new Socket("255.255.255.255", PORT);
				System.out.println("CLIENT: socket connected");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
