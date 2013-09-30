import java.util.Random;

import SE111aClasses.*;

public class RobbyPathPrediction {

	public static int destX;
	public static int destY;
	
	public static void main(String[] args) {
		Room room = new Room(8);
		Robot robby = new Robot();
		Picture pic = new Picture(room,robby);
		pic.draw(room, robby);
		printCoords(robby);
		
		//Set end point
		setDestination(room);
		
		//Start solving
		//moveThroughMaze(room,robby,pic);
		aStar(room,robby,pic);
		
		
	}	
	
	//Randomly set the target
	public static void setDestination(Room room) {
		Random random = new Random();	
		destX = random.nextInt(19);
		while (destX < 5) {
			destX = random.nextInt(19);
		}
		destY = random.nextInt(19);
		while (destY < 5) {
			destY = random.nextInt(19);
		}
		if (room.cell_state(destX, destY) == Room.WHITE) {
			room.setTarget(destX, destY);		
			System.out.println("Destination set at (" + destX + "," + destY + ")");
		} else {
			System.out.println("Target is already assigned a colour. Retrying...");
			setDestination(room);
		}
		
	}
	
	public static int calcGH (int x, int y) {
		int GH = (int) Math.ceil(Math.sqrt(Math.pow((double)(destX - x),2.0) + Math.pow((double)(destY - y),2.0)));
		return GH;
	}
	
	public static void aStar (Room room, Robot robby, Picture pic) {
		boolean[][] openClosed = new boolean[20][20];		
		openClosed[1][1] = true; //open is true, closed if false
		while (!robby.ahead_is_colour(room, Room.GREEN)) {
			int[][] fScore = new int[20][20];
			//Set values of robby x and y
			int x = robby.get_xpos();
			int y = robby.get_ypos();
			
			//Check node to N of robby
			if (room.cell_state(x, y + 1) == Room.WHITE /*|| room.cell_state(x, y + 1) == Room.YELLOW*/) {
				openClosed[x][y+1] = true;
				fScore[x][y+1] = 10 + calcGH(x, y+1); //G + H
			} else {
				openClosed[x][y+1] = false;
				fScore[x][y+1] = 100000;
			}
			//Check node to NE of robby
			if (room.cell_state(x + 1, y + 1)==Room.WHITE /*|| room.cell_state(x + 1, y + 1) == Room.YELLOW*/) {
				openClosed[x + 1][y + 1] = true;
				fScore[x+1][y+1] = 100000;
				//fScore[x+1][y+1] = 14 + Math.abs(((destX + destY) - ((x+1) + (y+1)))*10); //G + H
			} else {
				openClosed[x + 1][y + 1] = false;
				fScore[x + 1][y+1] = 100000;
			}
			//Check node to E of robby
			if (room.cell_state(x + 1, y)==Room.WHITE /*|| room.cell_state(x + 1, y) == Room.YELLOW*/) {
				openClosed[x+1][y] = true;
				fScore[x+1][y] = 10 + calcGH(x+1, y); //G + H
			} else {
				openClosed[x+1][y]=false;
				fScore[x+1][y] = 100000;
			}
			//Check node to SE of robby
			if (room.cell_state(x+1, y-1)==Room.WHITE /*|| room.cell_state(x + 1, y - 1) == Room.YELLOW*/) {
				openClosed[x+1][y-1]=true;
				fScore[x+1][y-1] = 100000;
				//fScore[x+1][y-1] = 14 + Math.abs(((destX + destY) - ((x+1) + (y-1)))*10); //G + H
			} else {
				openClosed[x+1][y-1]=false;
				fScore[x+1][y-1] = 100000;
			}
			//Check node to S of robby
			if (room.cell_state(x, y-1)==Room.WHITE /*|| room.cell_state(x, y - 1) == Room.YELLOW*/) {
				openClosed[x][y-1]=true;
				fScore[x][y-1] = 10 + calcGH(x, y-1); //G + H
			} else {
				openClosed[x][y-1]=false;
				fScore[x][y-1] = 100000;
			}
			//Check node to SW of robby
			if (room.cell_state(x-1, y-1)==Room.WHITE /*|| room.cell_state(x - 1, y - 1) == Room.YELLOW*/) {
				openClosed[x-1][y-1]=true;
				fScore[x-1][y-1] = 100000;
				//fScore[x-1][y-1] = 14 + Math.abs(((destX + destY) - ((x-1) + (y-1)))*10); //G + H
			} else {
				openClosed[x-1][y-1]=false;
				fScore[x-1][y-1] = 100000;
			}
			//Check node to W of robby
			if (room.cell_state(x-1, y)==Room.WHITE /*|| room.cell_state(x - 1, y) == Room.YELLOW*/) {
				openClosed[x-1][y]=true;
				fScore[x-1][y] = 10 + calcGH(x-1, y); //G + H
			} else {
				openClosed[x-1][y]=false;
				fScore[x-1][y] = 100000;
			}
			//Check node to NW of robby
			if (room.cell_state(x-1, y+1)==Room.WHITE /*|| room.cell_state(x - 1, y + 1) == Room.YELLOW*/) {
				openClosed[x-1][y+1]=true;
				fScore[x-1][y+1] = 100000;
				//fScore[x-1][y+1] = 14 + Math.abs(((destX + destY) - ((x-1) + (y+1)))*10); //G + H
			} else {
				openClosed[x-1][y+1]=false;
				fScore[x-1][y+1] = 100000;
			}
			openClosed[x][y]=false;
			
			//Find node with lowest F value
			int[][] smallestFValue = lowestF(openClosed,fScore,x,y);
			
			room.setPath(smallestFValue[0][0], smallestFValue[0][1]);
			pic.draw(room, robby);
			if (smallestFValue[0][0] > x) {
				robby.face_right();
				robby.move();
				pic.draw(room, robby);
			}
			if (smallestFValue[0][0] < x) {
				robby.face_left();
				robby.move();
				pic.draw(room, robby);
			}
			if (smallestFValue[0][1] > y) {
				robby.face_up();
				robby.move();
				pic.draw(room, robby);
			}
			if (smallestFValue[0][1] < y) {
				robby.face_down();
				robby.move();
				pic.draw(room, robby);
			}
			
		}
		robby.move();
		pic.draw(room, robby);
	}
	
	public static int[][] lowestF (boolean[][] openClosed, int[][] f, int startx, int starty) {
		int[][] minIndex = new int[1][2];
		int [][] searchValues = new int [9][2];
		int minValue = 0;
		
		//Row 1
		searchValues[0][0] = startx - 1;
		searchValues[0][1] = starty + 1;
		searchValues[1][0] = startx;
		searchValues[1][1] = starty + 1;
		searchValues[2][0] = startx + 1;
		searchValues[2][1] = starty + 1;
		//Row 2
		searchValues[3][0] = startx - 1;
		searchValues[3][1] = starty;
		//Miss out the currently occupied square
		searchValues[4][0] = startx + 1;
		searchValues[4][1] = starty;
		//Row 3
		searchValues[5][0] = startx - 1;
		searchValues[5][1] = starty - 1;
		searchValues[6][0] = startx;
		searchValues[6][1] = starty - 1;
		searchValues[7][0] = startx + 1;
		searchValues[7][1] = starty - 1;
		
		minValue = f[searchValues[0][0]][searchValues[0][1]];
		for (int i = 0; i < 8; i++) {
			if (f[searchValues[i][0]][searchValues[i][1]] < minValue) {
				minValue = f[searchValues[i][0]][searchValues[i][1]];
				minIndex[0][0] = searchValues[i][0];
				minIndex[0][1] = searchValues[i][1];
			}
		}		
		return minIndex;
	}
	
	//This is where the shit really goes down
	public static void moveThroughMaze(Room room, Robot robby, Picture pic) {
		while (!(robby.get_xpos() == destX) || !(robby.get_ypos() == destY)) {
			if (destX > robby.get_xpos()) {
				robby.face_right();
				if (robby.ahead_is_colour(room, Room.WHITE)) {
					robby.move();
					pic.draw(room, robby);
				} else {
					if (destY > robby.get_ypos()) {
						robby.face_up();
						if (!robby.ahead_is_colour(room, Room.WHITE)) {
							robby.face_left();
							if (!robby.ahead_is_colour(room, Room.WHITE)) {
								robby.face_down();
								robby.move();
								pic.draw(room, robby);
							} else {
								robby.move();
								pic.draw(room, robby);
							}
						} else {
							robby.move();
							pic.draw(room, robby);
						}
					}
				}
			} else {
				if (destY > robby.get_ypos()) {
					robby.face_up();
				}
			}
		}
		System.out.println("Success!");
	}
	
	
	
	/*
	Method to print current position on console cos I can't be fucked
	typing this all out every time
	*/
	public static void printCoords(Robot robby) {
		System.out.println("Coords are: (" + robby.get_xpos() + "," + robby.get_ypos() + ")");
	}

}
