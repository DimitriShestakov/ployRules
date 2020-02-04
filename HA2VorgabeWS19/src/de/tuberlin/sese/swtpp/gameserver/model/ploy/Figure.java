/**
 * 
 */
package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.util.LinkedList;


/**
 * @author Prospero
 * Implements a general Figure on a concrete board
 *
 */
public class Figure {
	private String figureCode;
	private String type;
	LinkedList<String> directionsFacing = new LinkedList<String>();

	private LinkedList<Integer> shieldCode = new LinkedList<Integer>();
	private LinkedList<Integer> probeCode = new LinkedList<Integer>();
	private LinkedList<Integer> lanceCode  = new LinkedList<Integer>();
	private LinkedList<Integer> commanderCode  = new LinkedList<Integer>();
	
	//initialize the lists of all possible notations for every given figure
	public Figure(String figureCode) {
        this.directionsFacing = new LinkedList<String>();
		this.figureCode = figureCode;
		this.directionsFacing = figureFaces();
		
		this.shieldCode = getRotationLeftFigureCodeCombinations((byte)1,7);
		
		this.probeCode = getRotationLeftFigureCodeCombinations((byte)3,7);
		this.probeCode.addAll(getRotationLeftFigureCodeCombinations((byte)17,7));
		this.probeCode.addAll(getRotationLeftFigureCodeCombinations((byte)130,7));
	
		this.lanceCode = getRotationLeftFigureCodeCombinations((byte)131,7);
		this.lanceCode.addAll(getRotationLeftFigureCodeCombinations((byte)146,7));
		this.lanceCode.addAll(getRotationLeftFigureCodeCombinations((byte)69,7));

		this.commanderCode = getRotationLeftFigureCodeCombinations((byte)170,7);
		//has to be initialized after all the lists
		this.type = whatFigure();

	}
	/*
	 * Checks whether this figure can do a certain move according to the given rules
	 */
	public Boolean canMoveAccordingToRules(int moveDistance, String moveDirection, Boolean rotation) {
		//every figure can do only rotation
		if(moveDistance == 0 && rotation) return true;
		//has to be true for any figure type
		if(this.directionsFacing.contains(moveDirection)) {
			//shield can move and rotate in the same move
			if(this.type.equals("shield")) {
				return (moveDistance == 1);
			}
			//other figures can not move and rotate at the same time
			if(!rotation) {
				if(this.type.equals("probe")) {
					return (moveDistance <= 2 && moveDistance > 0);
				}
				if(this.type.equals("lance")) {
					return (moveDistance <= 3 && moveDistance > 0);
				}
				if(this.type.equals("commander")) {
					return (moveDistance == 1);
				}
			}
		}
		return false;
	}

	/*
	 * Checks what figure is given as an argument and returns a string with its name
	 * The figure code should be recieved from the Move class 
	 */
	public String whatFigure() {
		String figureCodeNumber = this.figureCode.substring(1);
		
		if(this.shieldCode.contains(Integer.parseInt(figureCodeNumber))) this.type = "shield";
		if(this.probeCode.contains(Integer.parseInt(figureCodeNumber))) this.type = "probe";
		if(this.lanceCode.contains(Integer.parseInt(figureCodeNumber))) this.type = "lance";
		if(this.commanderCode.contains(Integer.parseInt(figureCodeNumber))) this.type = "commander";

		return this.type;
	}
	
	/*
	 * returns a String list of the directionsFacing that a certain figure faces
	 */
	public LinkedList<String> figureFaces() {
		this.directionsFacing.clear();
		String figureCodeNumber = this.figureCode.substring(1);
		int figureCodeNumberInt = Integer.parseInt(figureCodeNumber);
		//add an empty string to include the option that no direction is given(does not move)
		this.directionsFacing.add("");
		if(getBit(figureCodeNumberInt,0) == 1) this.directionsFacing.add("NN");
		if(getBit(figureCodeNumberInt,1) == 1) this.directionsFacing.add("NE");
		if(getBit(figureCodeNumberInt,2) == 1) this.directionsFacing.add("EE");
		if(getBit(figureCodeNumberInt,3) == 1) this.directionsFacing.add("SE");
		if(getBit(figureCodeNumberInt,4) == 1) this.directionsFacing.add("SS");
		if(getBit(figureCodeNumberInt,5) == 1) this.directionsFacing.add("SW");
		if(getBit(figureCodeNumberInt,6) == 1) this.directionsFacing.add("WW");
		if(getBit(figureCodeNumberInt,7) == 1) this.directionsFacing.add("NW");
	
		return this.directionsFacing;
	}
	
	/*
	 * Helper functions
	 */
	
	/*
	 * rotates a byte "bits" by "shift" places to the left
	 */
	private static byte rotateLeft(byte bits, int shift)
    {
        return (byte)(((bits & 0xff) << shift) | ((bits & 0xff) >>> (8 - shift)));
    }

	/*
	 * creates a linked list of all possible rotation positions of a figure with a certain code
	 */
    private static LinkedList<Integer> getRotationLeftFigureCodeCombinations(Byte code, int shift)
    {
        LinkedList<Integer> listOfCodes = new LinkedList<Integer>();
        for(int i = 0; i <= shift; i ++)
        {
            listOfCodes.add((rotateLeft(code,i) & 0xff));
        }
        return listOfCodes;
    }
    
    /*
     * gets a certain bit in an int n at the k_th position
     */
    private int getBit(int n, int k) {
        return (n >> k) & 1;
    }
    
    /*
     *Set Get 
     */
    public LinkedList<String> getdirectionsFacing(){
		return this.directionsFacing;
	}
    public LinkedList<Integer> getShieldCode() {
    	return this.shieldCode;
    }
    public LinkedList<Integer> getProbeCode() {
    	return this.probeCode;
    }
    public LinkedList<Integer> getLanceCode() {
    	return this.lanceCode;
    }
    public LinkedList<Integer> getCommanderCode() {
    	return this.commanderCode;
    }
    public String getFigureCode() {
    	return this.figureCode;
    }
    public String getType() {
    	return this.type;
    }

    
}
