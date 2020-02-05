/**
 * 
 */
package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

/**
 * @author Prospero
 *
 */
public class PloyGameMove extends Move {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8558072110629910438L;

	public PloyGameMove(String move, String boardBefore, Player player) {
		super(move, boardBefore, player);
	}

	public Boolean moveInBetweenPositionsAndTargetForPlayerAreFree(String playerString) {
		return !(moveIsBlockedByFiguresInBetween() || targetPositionHasWhitesOrBlacksOnIt(playerString));
	}
	
	/*
	 * Hilfsfunktion for the moveIsBlockedByFiguresInBetween
	 */
	 private Boolean moveInBetweenPositionsFull(Integer stepDistance) {
	        PloyGameState pgs = new PloyGameState(this.board);
	        ArrayList<String> positionsOfFigures = pgs.positionsOfFiguresOnBoard();
	        Integer indexOfStart = pgs.getIndexOfAPosition(this.move.substring(0,2));
	        Integer indexOfEnd = pgs.getIndexOfAPosition(this.move.substring(3,5));
	        Integer smallerIndex = Math.min(indexOfStart,indexOfEnd);
	        Integer biggerIndex = Math.max(indexOfStart,indexOfEnd);

	            for (int i = smallerIndex + stepDistance; i < biggerIndex  ; i += stepDistance) {
	                if(!(positionsOfFigures.get(i).equals("") || positionsOfFigures.get(i).equals(" "))) return true;
	            }

	        return false;
	    }


	 /*
	  * Checks whether there are pieces in between two positions on the current board
	  */
	    public Boolean moveIsBlockedByFiguresInBetween() {
	         String moveDistanceAndDirection = distanceAndDirectionOfTheMove();
	        //check if the direction could not be found
	        if(moveDistanceAndDirection.equals("-1")) return true;
	        //check if the start and target pints are the same -> there are no figures in between
	        if(moveDistanceAndDirection.equals("")) return false;
	        String moveDirection = moveDistanceAndDirection.substring(1);
	        String moveDistance = moveDistanceAndDirection.substring(0,1);
	        //if the move Distance is 1 there can be nothing in between
	        if(moveDistance.equals("1")) return false;
	        //check if the move done is horizontal
	        if(moveDirection.equals("WW") || moveDirection.equals("EE")) {
	          return(moveInBetweenPositionsFull(1));
	        }
	        //check if vertical
	        if(moveDirection.equals("SS") || moveDirection.equals("NN")) {
	            return (moveInBetweenPositionsFull(9));
	        }
	        //check if diagonal from left to right
	        if(moveDirection.equals("SE") || moveDirection.equals("NW")) {
	            return (moveInBetweenPositionsFull(10));
	        }
	        //check if diagonal from right to left
	        else return moveInBetweenPositionsFull(8);
	    }
	
	/*
	 * Returns a String of the distance and and direction between the start and the target of a move
	 * in a format z.B "3SW" meaning distance = 3, and direction = SW.
	 * Returns "-1" if there is no possible diagonal,vertical or horizontal way between these points
	 * Returns "" if the start and the target are the same
	 * Badly formated because of the 25 lines restriction
	 */
	public String distanceAndDirectionOfTheMove() {
		PloyGameState pgs = new PloyGameState(this.board);
		Integer indexOfStart = pgs.getIndexOfAPosition(this.move.substring(0,2));
		Integer indexOfEnd = pgs.getIndexOfAPosition(this.move.substring(3,5));
		Integer smallerIndex = Math.min(indexOfStart,indexOfEnd);  Integer biggerIndex = Math.max(indexOfStart,indexOfEnd);
		Integer moveDistance = -1; String moveDirection = ""; String moveDistanceAndDirection = "";
		//check if does not move
		for (int j = 0; j <= 8 ; j++) {
			//i = 0,9,18,27...
			int i = j * 9;
			//Check horizontally
			if(indexOfStart >= i && indexOfStart <= (i + 9) && indexOfEnd >= i && indexOfEnd <= (i + 9)) {
				moveDirection = indexOfEnd - indexOfStart > 0 ? "EE" : "WW"; moveDistance = Math.abs(indexOfEnd - indexOfStart); }
			//Check vertically
			if((indexOfStart - j) % 9 == 0 && (indexOfEnd - j) % 9 == 0) {
				moveDirection = indexOfEnd - indexOfStart > 0 ? "SS" : "NN"; moveDistance = Math.abs(((indexOfEnd - j) / 9) - ((indexOfStart - j) / 9)); }
			//Check the diagonal first check the NW-SE diagonal and then NE-SW
			if(biggerIndex == smallerIndex + 10 * j) {
				moveDirection = indexOfEnd - indexOfStart > 0 ? "SE" : "NW"; moveDistance = j; }
			if(biggerIndex == smallerIndex + 8 * j) {
				moveDirection = indexOfEnd - indexOfStart > 0 ? "SW" : "NE"; moveDistance = j; }
		}
		//check if does not move
		moveDistanceAndDirection = (this.move.substring(0,2).equals(this.move.substring(3,5))) ? "" : Integer.toString(moveDistance) + moveDirection;
		return moveDistanceAndDirection;
	}
	
    /*
     *Checks whether the target location of the move has a white "w" or a black "b" figure on it
     */
    public Boolean targetPositionHasWhitesOrBlacksOnIt(String playerString) {
        if(!(playerString.equals("w") || playerString.equals("b"))) return false;
		PloyGameState currentGameState = new PloyGameState(this.board);
        String moveEnd = this.move.substring(3, 5);
        String codeOfTheFigure = currentGameState.whatFigureIsPlacedOnThatPosition(moveEnd);
        //I check if the figure does not move and only has rotation -> the move can be done
		//It is only done to reduce the dumb McCabe complexity
        String distanceAndDirectionCurrentMove = distanceAndDirectionOfTheMove();
		Boolean rotation = !this.move.endsWith("0");
		Integer moveDistance = (distanceAndDirectionCurrentMove.equals("")) ? 0 : Integer.parseInt(distanceAndDirectionCurrentMove.substring(0,1));
		if(moveDistance == 0 && rotation) return false;
//----------------------------------------------------
		if(codeOfTheFigure.startsWith(playerString)) return true;

        return false;
    }
	
    /*
     * Returns the code of the figure that we are willing to move with the current Move
     */
    public String whatFigureToMove() {
    	PloyGameState currentGameState = new PloyGameState(this.board);
        String moveStart = this.move.substring(0, 2);
        String codeOfTheFigure = currentGameState.whatFigureIsPlacedOnThatPosition(moveStart);

        return codeOfTheFigure;
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
