package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class PloyGameState{
	String board;

	public PloyGameState(String board) {
		this.board = board;
	}

	public Boolean playerHasCommander(String player) {
		ArrayList<String> everyFigureOnBoard = positionsOfFiguresOnBoard();
		return false;
		

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
    
    public int getIndexOfAPosition(String position) {
        char column = position.charAt(0);
        int row = Integer.parseInt(position.substring(1));
        int index = ((9 - row) * 9) + (column - 'a');

        return index;
    }
	

}
