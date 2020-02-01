package de.tuberlin.sese.swtpp.gameserver.model.ploy;
import de.tuberlin.sese.swtpp.gameserver.model.ploy.PloyGame;
import de.tuberlin.sese.swtpp.gameserver.model.ploy.PloyLocation;
import de.tuberlin.sese.swtpp.gameserver.model.ploy.PloyBoard;
import java.util.ArrayList;

public abstract class PloyPiece {
	protected PloyGame ployGame;
	protected String owner;
	protected PloyLocation ployLocation;
	protected char id;
	protected ArrayList<PloyLocation> threateningLocations;
	
	protected abstract void updateThreateningLocation();
	
	public PloyPiece(String owner, PloyLocation initiallocation, PloyGame game) {
		this.owner = owner;
		ployLocation = null;
		ployGame = game;
		threateningLocations = new ArrayList<>();
		ployGame.getPloyBoard().placePieceAt(this, initiallocation);
	}
	
	/**
    * Checks for the line of sight of the move.
    * @param start Start location.
    * @param end End location.
    * @return Valid move or not
    */
	protected boolean checkLineOfSight(PloyLocation start, PloyLocation end) {
        // Vertical
        if (start.getCol() == end.getCol()) { 
            int one = (start.getRow() - end.getRow() < 0) ? 1: -1;
            for (int row = start.getRow() + one; row < end.getRow(); row += one) {
                if (ployGame.getPloyBoard().isPieceAt(row, start.getCol())) {
                    return false;
                }
            }
            return true;
        }

        // Horizontal
        if (start.getRow() == end.getRow()) {
            int one = (start.getCol() - end.getCol() < 0) ? 1: -1;
            for (int col = start.getCol() + one; col < end.getCol(); col += one) {
                if (ployGame.getPloyBoard().isPieceAt(start.getRow(), col)) {
                    return false;
                }
            }
            return true;
        }

        // Diagonal
        // Case 1 : Slope -1
        // Case 2 : Slope 1
        if (start.getCol() - end.getCol() == 
            start.getRow() - end.getRow()) {

            int one = (start.getRow() - end.getRow() < 0) ? 1: -1;
            for (int inc = one; Math.abs(inc) < Math.abs(start.getRow() - end.getRow()); inc += one) {
                if (ployGame.getPloyBoard().isPieceAt(start.getRow() + inc, start.getCol() + inc)) {
                    return false;
                }
            }
            return true;
        } else if (start.getCol() - end.getCol() * -1 == 
                   start.getRow() - end.getCol()) {

            int one = (start.getRow() - end.getRow() < 0) ? 1: -1;
            int negOne = one * -1;
            for (int inc = one; Math.abs(inc) < Math.abs(start.getRow() - end.getRow()); inc += one) {
                if (ployGame.getPloyBoard().isPieceAt(start.getRow() + inc, start.getCol() + (inc * negOne))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Updates the threatening locations for a vertical direction.
     * @param one The direction to check in
     */
    protected void updateVertical(int one) {
        PloyLocation location = new PloyLocation(ployLocation.getRow() + one, ployLocation.getCol());
        int inc = one;
        while (PloyBoard.locationInBounds(location)) {
            PloyPiece piece = ployGame.getPloyBoard().getPieceAt(location);
            if (piece != null) {
                if (!piece.getOwner().equalsIgnoreCase(owner)) {
                    threateningLocations.add(location); 
                    return;
                } else if (!ployLocation.equals(location)) {
                    threateningLocations.add(new PloyLocation(location.getRow() - one, location.getCol())); 
                    return;
                }
            } else {
                location = new PloyLocation(location.getRow() + one, location.getCol());
            }
        }
    }

    /**
     * Updates the threatening locations for a horizontal direction.
     * @param one The direction to check in
     */
    protected void updateHorizontal(int one) {
        PloyLocation location = new PloyLocation(ployLocation.getRow(), ployLocation.getCol() + one);
        while (PloyBoard.locationInBounds(location)) {
            PloyPiece piece = ployGame.getPloyBoard().getPieceAt(location);
            if (piece != null) {
                if (!piece.getOwner().equalsIgnoreCase(owner)) {
                    threateningLocations.add(location); 
                    return;
                } else if (!ployLocation.equals(location)) {
                    threateningLocations.add(new PloyLocation(location.getRow(), location.getCol() - one)); 
                    return;
                }
            } else {
                location = new PloyLocation(location.getRow(), location.getCol() + one); 
            }
        }
    }

    /**
     * Updates the threatening locations for a diagonal direction.
     * @param rowOne The row direction to check in
     * @param colOne The col direction to check in
     */
    protected void updateDiagonal(int rowOne, int colOne) {
        PloyLocation location = new PloyLocation(ployLocation.getRow() + rowOne, ployLocation.getCol() + colOne);
        while (PloyBoard.locationInBounds(location)) {
            PloyPiece piece = ployGame.getPloyBoard().getPieceAt(location);
            if (piece != null) {
                if (!piece.getOwner().equalsIgnoreCase(owner)) {
                    threateningLocations.add(location); 
                    return;
                } else if (!ployLocation.equals(location)) {
                    threateningLocations.add(new PloyLocation(location.getRow() - rowOne, location.getCol() - colOne)); 
                    return;
                }
            } else {
                location = new PloyLocation(location.getRow() + rowOne, location.getCol() + colOne);
            }
        }
    }
    
    /**
     * Sets the location of the PloyPiece.
     * @param newLocation The new location of the knight.
     */
    public boolean moveTo(PloyLocation newLocation) {
        PloyBoard board = ployGame.getPloyBoard();
        PloyPiece oldPiece = board.getPieceAt(newLocation);
        
        if (oldPiece == null ||
            oldPiece.getOwner() != owner) {
            
            board.placePieceAt(this, newLocation);
            return true;
        }
        return false;
    }
        

        /**
         * Gets the owner String
         * @return Owner string
         */
        public String getOwner() {
        	return owner;
        }

        /**
         * Gets the id of the piece.
         * @return Char of the id.
         */
        public char getId() {
            return id;
        }

        /**
         * Gets threateningLocation
         * @return ArrayList of ChessLocations
         */
        public ArrayList<PloyLocation> getThreateningLocations() {
            return threateningLocations;
        }
    
  
		public PloyLocation getPloyLocation() {
			return ployLocation;
		}
	
		public void setPloyLocation(PloyLocation location) {
			ployLocation = location;
		}
}
