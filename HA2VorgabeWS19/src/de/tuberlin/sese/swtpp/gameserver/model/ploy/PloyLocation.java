package de.tuberlin.sese.swtpp.gameserver.model.ploy;

public class PloyLocation {
	private int row;
	private int col;
	
	public PloyLocation(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof PloyLocation) {
			PloyLocation l = (PloyLocation) obj;
			return(row == l.getRow() &&
				   col == l.getCol());
		}
		return false;
	}
	
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
}
