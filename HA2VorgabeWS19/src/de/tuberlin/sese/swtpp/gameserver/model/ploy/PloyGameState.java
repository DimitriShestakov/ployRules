package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import de.tuberlin.sese.swtpp.gameserver.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;


public class PloyGameState{
	/**
	 * 
	 */
	String board;
	Player player;

	public PloyGameState(String board) {		
		this.board = board;
	}
	
	  private  byte rotateLeft(byte bits, int shift)
	    {
	        return (byte)(((bits & 0xff) << shift) | ((bits & 0xff) >>> (8 - shift)));
	    }

	    public Boolean gameIsLostForPlayer(String playerString) {
	    return  (!(playerHasCommander(playerString)) || playerHasOnlyCommanderLeft(playerString));
        }
	  /*
	   * Takes an arrayList of positions and turns it into a properly formated board string
	   */
	public String arrayListOfPositionsToString(ArrayList<String> newList) {
		if(newList.get(newList.size() - 1).equals(" ")) newList.remove(newList.size() - 1);
		
        for (int i = 0; i < newList.size() ; i++) {
            String stringToInsert = (i + 1) % 9 == 0 && i < newList.size() - 1 ? "/" : ",";
            newList.set(i,newList.get(i) + stringToInsert);
        }
        String listString = String.join("",newList);
        
        return listString;
	}
	
	/*
	 * takes a move in a format start-ziel-rotation and returns an updated board position
	 */
	public String doMove(String move) {
        ArrayList<String> newList = positionsOfFiguresOnBoard();
		 //get Start and End strings and the rotation
        String moveStart = move.substring(0,2);
        String moveEnd = move.substring(3,5);
        Integer rotation = Integer.parseInt(move.substring(6));
        //get their indexes in the array
        Integer indexOfStart = getIndexOfAPosition(moveStart);
        Integer indexOfEnd = getIndexOfAPosition(moveEnd);
        //get the code of the moved figure, also only its integer part
       String figireToMove = whatFigureIsPlacedOnThatPosition(moveStart);
        int figureCodeInt = Integer.parseInt(figireToMove.substring(1));

        //do the rotation on the given figure, rotation method is local
        Integer figureCodeIntRotated = rotateLeft((byte)figureCodeInt,rotation) & 0xff;
        String figureToMoveAfterRotation = figireToMove.substring(0,1) + figureCodeIntRotated.toString();
        //change the start and end positions in the arraylist of our moves
        newList.set(indexOfStart,"");
        newList.set(indexOfEnd,figureToMoveAfterRotation);
         String resultingString = arrayListOfPositionsToString(newList);
         this.board = resultingString;

         return resultingString;
	}
	/*
	 * Returns the figure code of a figure placed on the specified position
	 */
	public String whatFigureIsPlacedOnThatPosition(String position) {
		ArrayList<String> positionsOfFiguresOnBoard = positionsOfFiguresOnBoard();
        int index = getIndexOfAPosition(position);
        String codeOfTheFigure = positionsOfFiguresOnBoard.get(index);

        return codeOfTheFigure;
	}


	/*
	 * Checks whether a player "w" or "b" has a Commander figure
	 */
	public Boolean playerHasCommander(String playerString) {
		//just a random figure as it does not matter for us here
		Figure f = new Figure("b1");
		LinkedList<Integer> allFiguresOfThePlayer = figuresOfAPlayer(playerString);
		//if there is one of the possible codes of the Commander figure in the list above
		if(!Collections.disjoint(allFiguresOfThePlayer, f.getCommanderCode())) return true;
		
		return false;
	}
	
	/*
	 * Checks whether a player "w" or "b" has any other figures other than Commander left
	 */
	public Boolean playerHasOnlyCommanderLeft(String playerString) {
		LinkedList<Integer> allFiguresOfTheGivenPlayer = figuresOfAPlayer(playerString);
		if(allFiguresOfTheGivenPlayer.size() == 1 && playerHasCommander(playerString)) return true;
		
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

        //to get the right last position on the board correctly using split function
        //we need to check if ',' is the last symbol in our string
        if (boardState.charAt(boardState.length() - 1) == ',') {
            array = boardState.split(",");
            arrayList = new ArrayList<String>(Arrays.asList(array));
            arrayList.add(" ");
        }
        else {
            array = boardState.split(",");
            arrayList = new ArrayList<String>(Arrays.asList(array));
        }
        return arrayList;
    }
    
    /*
     * Get list of all figureCodes(only the string) of a certain player
     * PlaterString "w" or "b"
     */
    public LinkedList<Integer> figuresOfAPlayer(String playerString)
    {
        LinkedList<Integer> codesOfFigures = new LinkedList<>();
        String toRemove = playerString.equals("w") ? "b" : "w";
        ArrayList<String> newList = positionsOfFiguresOnBoard();
        for (int i = 0; i < newList.size(); i++) {
            if(newList.get(i).equals("") || newList.get(i).startsWith(toRemove) || newList.get(i).equals(" ") ) {
                newList.remove(i);
                i--;
            }
            else codesOfFigures.add(Integer.parseInt(newList.get(i).substring(1)));
        }
        return codesOfFigures;
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
