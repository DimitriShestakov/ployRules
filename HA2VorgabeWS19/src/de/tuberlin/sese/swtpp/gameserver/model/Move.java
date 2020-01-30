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
