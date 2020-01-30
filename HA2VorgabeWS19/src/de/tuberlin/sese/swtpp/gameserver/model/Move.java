package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import de.tuberlin.sese.swtpp.gameserver.model.ploy.PloyGameState;

/**
 * 
 * Represents one move of a player in a certain stage of the game.
 * 
 * May be specialized further to represent game-specific move information.
 *
 */
public class Move implements Serializable{

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
	 * Returns a String of the distance and and direction between the start and the target of a move
	 * in a format z.B "3SW" meaning distance = 3, and direction = SW.
	 * Returns "-1" if there is no possible diagonal,vertical or horizontal way between these points
	 * Returns "0KEINE" if the start and the target are the same
	 * Badly formated because of the 25 lines restriction
	 */
	public String distanceAndDirectionOfTheMove() {
		PloyGameState pgs = new PloyGameState(this.board);
        Integer indexOfStart = pgs.getIndexOfAPosition(this.move.substring(0,2));
        Integer indexOfEnd = pgs.getIndexOfAPosition(this.move.substring(3,5));
        Integer moveDistance = -1; String moveDirection = ""; String moveDistanceAndDirection = "";
        //check if does not move
        if(this.move.substring(0,2).equals(this.move.substring(3,5))) moveDistanceAndDirection = Integer.toString(0) + "KEINE";
        //Check horizontally
        for (int j = 0; j <= 8 ; j++) {
            //i = 0,9,18,27...
            int i = j * 9;
            if(indexOfStart >= i && indexOfStart <= (i + 9) && indexOfEnd >= i && indexOfEnd <= (i + 9)) {
                moveDirection = indexOfEnd - indexOfStart > 0 ? "EE" : "WW"; moveDistance = Math.abs(indexOfEnd - indexOfStart); }
            //Check vertically
            if((indexOfStart - j) % 9 == 0 && (indexOfEnd - j) % 9 == 0) {
                moveDirection = indexOfEnd - indexOfStart > 0 ? "SS" : "NN"; moveDistance = Math.abs(((indexOfEnd - j) / 9) - ((indexOfStart - j) / 9)); }
            //Check the diagonal first check the NW-SE diagonal and then NE-SW
            if(indexOfEnd == indexOfStart + 10 * j) {
                moveDirection = indexOfEnd - indexOfStart > 0 ? "SE" : "NW"; moveDistance = j; }
            if(indexOfEnd == indexOfStart + 8 * j) {
                moveDirection = indexOfEnd - indexOfStart > 0 ? "SW" : "NE"; moveDistance = j; }
        }
        moveDistanceAndDirection = Integer.toString(moveDistance) + moveDirection;
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
