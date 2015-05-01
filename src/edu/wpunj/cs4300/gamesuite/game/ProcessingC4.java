package edu.wpunj.cs4300.gamesuite.game;
import processing.core.*;


public class ProcessingC4 extends PApplet {
	int x = 7; //width of the board on the x axis
	int y = 6; //height of the board on the y axis
	int block = 100; //the size of each grid in pixels
	int[][] board = new int[y][x]; //creates a array board, size being the x and y values
	int player = 1;//creating player 1

	public void setup() { 
	  size(700, 600);
	  ellipseMode(CORNER);

	}

	int border(int h, int w) {
	  return (h<0||w<0||h>=y||w>=x)?0:board[h][w]; //makes sure that everything stays within the board
	}

	int checkFour() { //checks the rows, columns, diagnoals for any four
	  for(int h=0; h<y; h++)for(int w=0; w<x; w++)
	    if(border(h,w) !=0&&border(h,w) ==border(h,w+1)&&border(h,w)==border(h,w+2)&&border(h,w)==border(h,w+3))
	    return border(h,w); //checks if there is any four in a row
	    
	  for(int h=0; h<y; h++)for(int w=0; w<x; w++)
	    if(border(h,w) !=0&&border(h,w) ==border(h+1,w)&&border(h,w)==border(h+2,w)&&border(h,w)==border(h+3,w))
	    return border(h,w);
	    
	  for(int h=0; h<y; h++)for(int w=0; w<x; w++)for(int d=-1;d<=1;d+=2)//checking diaganoals
	    if (border(h,w) !=0&&border(h,w) ==border(h+1*d,w+1)&&border(h,w)==border(h+2*d,w+2)&&border(h,w)==border(h+3*d,w+3))
	    return border(h,w);
	    
	  for(int h=0; h<y; h++)for(int w=0; w<x; w++)
	    if(border(h,w)==0)
	    return 0;
	   
	  return -1; //when there is a tie
	}
	int checkSpace(int w) { //checks the board to see if there is an empty space
	  for(int h=y-1; h>=0; h--) 
	  if(board[h][w]==0) return h;
	    return -1;
	}

	public void mousePressed() {
	  int w = mouseX / block, h = checkSpace(w);
	  if(h>=0) {
	    board[h][w] = player;
	    player = player == 1?2:1; //this switches the players
	  }   
	}

	public void draw() {
	  if (checkFour()==0){  
	    for(int i=0; i<y; i++) for(int j=0; j<x;j++) { //this loops covers the entirety of the board
	      fill(255); //fill function is just the color, 255 being the color code for white
	      rect(j*block, i*block, block, block); //makes sure all rectangles are even
	      if(board[i][j]>0) {
	        fill(board[i][j]==1?255:100, board[i][j]==2?255:204,0);//places colored circles based on the player number
	        ellipse(j*block,i*block,block,block);//the ? is a ternary operator
	      }
	    }
	  }else { //win condition
	    background(0);
	    fill(255);
	    text("Winner" +checkFour()+ ". Space restarts the game",width/2, height/2);
	    if(keyPressed&&key==' ') { //restarts the game
	      player = 1;
	      for(int h=0; h<y; h++)for(int w=0; w<x; w++)
	      board[h][w]=0;
	    }
	  }
	}
}
