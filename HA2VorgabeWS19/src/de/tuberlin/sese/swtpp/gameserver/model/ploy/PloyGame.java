package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;

/**
 * Class Cannon extends the abstract class Game as a concrete game instance that
 * allows to play Cannon.
 *
 */
public class PloyGame extends Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424778147226994452L;

	/************************
	 * member
	 ***********************/

	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state
	// TODO: insert additional game data here
	private String board;


	/************************
	 * constructors
	 ***********************/

	public PloyGame() {
		super();
		// TODO: init internal representation
		this.board = ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,";
		this.setNextPlayer(blackPlayer);
	}

	public String getType() {
		return "ploy";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.blackPlayer= players.get(0);
				this.whitePlayer = players.get(1);
				nextPlayer = blackPlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (whiteGaveUp())
				gameInfo = "white gave up";
			else if (didWhiteDraw() && !didBlackDraw())
				gameInfo = "white called draw";
			else if (!didWhiteDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "white won";
		}

		return gameInfo;
	}

	@Override
	public String nextPlayerString() {
		return isWhiteNext() ? "w" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(p -> p.requestedDraw())) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) {
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/*******************************************
	 * Helpful stuff
	 ******************************************/

	/**
	 * 
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 * 
	 * @param winner
	 * @return
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	@Override
	public void setBoard(String state) {
		// TODO: implement
		this.board = state;
		// Note: This method is for automatic testing. A regular game would not start at some artificial state. 
		//       It can be assumed that the state supplied is a regular board that can be reached during a game. 
	}

	@Override
	public String getBoard() {
		// TODO: implement and replace dummy with actual board
		return this.board;
		//return ",w84,w41,w56,w170,w56,w41,w84,/,,w24,w40,w17,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b1,b1,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,";
	}

	@Override
	public boolean tryMove(String moveString, Player player) {
	if(moveString.matches("[a-i][0-9]-[a-i][0-9]-[0-7]") && player.getGame().isPlayersTurn(player)) {
		Boolean rotation = moveString.endsWith("0") ? false : true;
		String opponentString = nextPlayerString().equals("w") ? "b" : "w";
		//initialize a boardState, a Figure and the current move using the existing data
		PloyGameState currentGameState = new PloyGameState(this.board); PloyGameMove currentMove = new PloyGameMove(moveString,this.board,player); Figure figureToMove = new Figure(currentMove.whatFigureToMove());
		//Extract the distance and the direction of the move and check the resulting String whether it is an invalid move(then returns "-1")
		String distanceAndDirectionOfTheMove = currentMove.distanceAndDirectionOfTheMove(); Integer moveDistance; String moveDirection;
		//if the distanceAndDirectionOfTheMove returns "-1" the target position can not be reached in 1 move
		if(distanceAndDirectionOfTheMove.equals("-1")) return false;
		if(distanceAndDirectionOfTheMove.equals("")) {
			moveDistance = 0; moveDirection = ""; }
		else { moveDistance = Integer.parseInt(distanceAndDirectionOfTheMove.substring(0,1));
		       moveDirection = distanceAndDirectionOfTheMove.substring(1);}
		if((!(currentMove.moveIsBlockedByFiguresInBetween() || currentMove.targetPositionHasWhitesOrBlacksOnIt(nextPlayerString()))
				&& figureToMove.canMoveAccordingToRules(moveDistance, moveDirection, rotation)) || (moveDistance == 0 && rotation)) {
			this.board = currentGameState.doMove(moveString);
			this.history.add(currentMove);
			if(!(currentGameState.playerHasCommander(opponentString)) || currentGameState.playerHasOnlyCommanderLeft(opponentString)) {
				player.setWinner();
				finish();
				return true; }
			if(player.getGame().getNextPlayer() == whitePlayer) player.getGame().setNextPlayer(blackPlayer);
			else player.getGame().setNextPlayer(whitePlayer);
				return true; }
		return false; }
		return false; }

	public static void main(String[] args) {
		User firstUser = new User("John","id");
		User secondUser = new User("Bob","idd");
		Game aGame = new PloyGame();
		Player firstPlayer = new Player(firstUser,aGame);
		Player secondPlayer = new Player(secondUser,aGame);
		aGame.addPlayer(firstPlayer);
		aGame.addPlayer(secondPlayer);
		aGame.setNextPlayer(firstPlayer);
		aGame.setBoard( "b69,,,w56,w170,w56,w41,w84,/,,w24,w40,b1,w40,w48,,/,,,w16,w16,w16,,,/,,,,,,,,/,,,,,,,,/,,,,,,,,/,,,b2,w84,b1,,,/,,b3,b130,b17,b130,b129,,/,b69,b146,b131,b170,b131,b146,b69,");

		String moveString = "a9-d9-0";

		Boolean check = aGame.tryMove(moveString, firstPlayer);
		System.out.println(check);

	}
}
