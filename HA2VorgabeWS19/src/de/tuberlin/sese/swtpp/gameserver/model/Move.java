package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * Represents one move of a player in a certain stage of the game.
 * 
 * May be specialized further to represent game-specific move information.
 *
 */
public class Move implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8030012939073138731L;

	// attributes
	protected String move;
	protected String board;

	// associations
	protected Player player;

	/************************************
	 * constructors
	 ************************************/

	public Move(String move, String boardBefore, Player player) {
		this.move = move;
		this.board = boardBefore;
		this.player = player;
	}
	

	
    /*
     * Returns the code of the figure that we are willing to move with the current Move
     */
    public String whatFigureToMove() {
        String moveStart = this.move.substring(0, 2);
        ArrayList<String> positionsOfFiguresOnBoard = positionsOfFiguresOnBoard();
        int index = getIndexOfAPosition(moveStart);
        String codeOfTheFigure = positionsOfFiguresOnBoard.get(index);

        return codeOfTheFigure;
    }

    
    /*
     * Returns an arraylist of all figures on the current board sorted by their position
     */
    public ArrayList<String> positionsOfFiguresOnBoard() {
        //array of all figures on the current board sorted by their position
        String[] array;
        //arraylist with the same contents as the array
        ArrayList<String> arrayList;
        String boardState = this.board;


        //to get every figure in the board string separated by a comma
        //we need to remove the '/' sign that separates every row and replace it with a comma
        char[] boardStateCharArray = boardState.toCharArray();
        for (int i = 0; i < boardState.length(); i++) {
            if (boardStateCharArray[i] == '/') {
                boardStateCharArray[i] = ',';
            }
        }
        boardState = String.valueOf(boardStateCharArray);

        //to get the right position on the board correctly using split function
        //we need to check if ',' is the last symbol in our string
        if (boardState.charAt(boardState.length() - 1) == ',') {
            array = boardState.split(",");
            arrayList = new ArrayList<String>(Arrays.asList(array));
            arrayList.add(" ");

        } else {
            array = boardState.split(",");
            arrayList = new ArrayList<String>(Arrays.asList(array));

        }

        return arrayList;
    }

    
    
    /*
     * Returns the index of a certain position in the arraylist created by the
       positionsOfFiguresOnBoard method
     */
    
    int getIndexOfAPosition(String position) {
        char column = position.charAt(0);
        int row = Integer.parseInt(position.substring(1));
        int index = ((9 - row) * 9) + (column - 'a');

        return index;
    }

    
	/************************************
	 * getters/setters
	 ************************************/

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public String getState() {
		return board;
	}

	public void setBoard(String state) {
		this.board = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
