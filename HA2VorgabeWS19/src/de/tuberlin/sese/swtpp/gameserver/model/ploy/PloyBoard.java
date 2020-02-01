package de.tuberlin.sese.swtpp.gameserver.model.ploy;
import de.tuberlin.sese.swtpp.gameserver.model.ploy.PloyPiece;
public class PloyBoard {
		private PloyPiece[][] board;
		
		
		/**Create new board instance**/
		public PloyBoard() {
			board = new PloyPiece[9][9];
		}
		
		/**
		 * Check if piece is at a specified location
		 * @param row Row of the location
		 * @param col Col of the location
		 * @return Boolean: if there is piece on location
		 */
		public boolean isPieceAt(int row, int col) {
			return board[row][col] != null;
		}
		
		/**
		 * Places piece at location,if piece already exists there, the old piece will be overwritten.
		 * @param piece The piece to move 
		 * @param location The location to move to
		 */
		public void placePieceAt(PloyPiece piece, PloyLocation location) {
			if(isPieceAt(location.getRow(),location.getCol())) {
				removePieceAt(location);
			}
			if(piece.getPloyLocation() != null) {
				removePieceAt(piece.getPloyLocation());
			}
			board[location.getRow()][location.getCol()] = piece;
			piece.setPloyLocation(location);
		}
		
		
		/**
	     * Removes piece at location.
	     * @param location The location to remove at.
	     */
		private void removePieceAt(PloyLocation location) {
			board[location.getRow()][location.getCol()] = null;
		}
		
		/**
		 * Check if the location is within the board
		 * @param location The location to check
		 * @return Boolean: in bounds or not
		 */
		public static boolean locationInBounds(PloyLocation location) {
			return location.getRow() >= 0 &&
				   location.getRow() < 9 &&
				   location.getCol() >= 0 &&
				   location.getCol() < 9;
		}
		
		/**
	     * Gets piece at location of board.
	     * @param location Location to find piece at.
	     * @return Piece at location/
	     */
	    public PloyPiece getPieceAt(PloyLocation location) {
	        return board[location.getRow()][location.getCol()];
	    }
	    
	    /**
	     * Displays board with P being Piece.
	     */
	    @Override
	    public String toString() {
	        String s = "  0 1 2 3 4 5 6 7\n";
	        for (int row = 0; row < 8; row++) {
	            s += row;
	            for (int col = 0; col < 8; col++) {
	                if (board[row][col] != null) {
	                    s += " " + board[row][col].getId();
	                } else {
	                    s += " -";
	                }
	            }
	            s += "\n";
	        }
	        return s;
	    }
}
