/**
 * 
 */
package de.tuberlin.sese.swtpp.gameserver.model.ploy;

import java.util.LinkedList;
import java.util.List;

import de.tuberlin.sese.swtpp.gameserver.model.Move;

/**
 * @author Prospero
 * Implements a general Figure on a concrete board
 *
 */
public class Figure {
	private String board;
	private String position;

	private LinkedList<Integer> shieldCode = new LinkedList<Integer>();
	private LinkedList<Integer> probeCode = new LinkedList<Integer>();
	//private LinkedList<Integer> probeCode2  = new LinkedList<Integer>();
	//private LinkedList<Integer> probeCode3 = new LinkedList<Integer>();
	private LinkedList<Integer> lanceCode  = new LinkedList<Integer>();
	//private LinkedList<Integer> lanceCode2  = new LinkedList<Integer>();
	//private LinkedList<Integer> lanceCode3  = new LinkedList<Integer>();
	private LinkedList<Integer> commanderCode  = new LinkedList<Integer>();
	
	public Figure() {
		this.shieldCode = getShiftLeftFigureCodeCombinations((byte)1,7);
		
		this.probeCode = getShiftLeftFigureCodeCombinations((byte)3,7);
		this.probeCode.addAll(getShiftLeftFigureCodeCombinations((byte)17,7));
		this.probeCode.addAll(getShiftLeftFigureCodeCombinations((byte)130,7));
		

		
		this.lanceCode = getShiftLeftFigureCodeCombinations((byte)131,7);
		this.lanceCode.addAll(getShiftLeftFigureCodeCombinations((byte)146,7));
		this.lanceCode.addAll(getShiftLeftFigureCodeCombinations((byte)69,7));

		this.commanderCode = getShiftLeftFigureCodeCombinations((byte)170,7);
	}



	
	public String whatFigure(String figureCode) {
		String figureCodeNumber = figureCode.substring(1);
		if(this.shieldCode.contains(Integer.parseInt(figureCodeNumber))) return "shield";
		
		if(this.probeCode.contains(Integer.parseInt(figureCodeNumber))) return "probe";
		//if(this.probeCode2.contains(Integer.parseInt(figureCodeNumber))) return "probe2";
		//if(this.probeCode3.contains(Integer.parseInt(figureCodeNumber))) return "probe3";

		if(this.lanceCode.contains(Integer.parseInt(figureCodeNumber))) return "lance";
		//if(this.lanceCode2.contains(Integer.parseInt(figureCodeNumber))) return "lance2";
		//if(this.lanceCode3.contains(Integer.parseInt(figureCodeNumber))) return "lance3";

		if(this.commanderCode.contains(Integer.parseInt(figureCodeNumber))) return "commander";

		return "no such figure";
	}
	
	public LinkedList<String> figureFaces(String figureCode) {
		LinkedList<String> directions = new LinkedList<String>();
		String figureCodeNumber = figureCode.substring(1);
		int figureCodeNumberInt = Integer.parseInt(figureCodeNumber);
		
		if(getBit(figureCodeNumberInt,0) == 1) directions.add("NN");
		if(getBit(figureCodeNumberInt,1) == 1) directions.add("NE");
		if(getBit(figureCodeNumberInt,2) == 1) directions.add("EE");
		if(getBit(figureCodeNumberInt,3) == 1) directions.add("SE");
		if(getBit(figureCodeNumberInt,4) == 1) directions.add("SS");
		if(getBit(figureCodeNumberInt,5) == 1) directions.add("SW");
		if(getBit(figureCodeNumberInt,6) == 1) directions.add("WW");
		if(getBit(figureCodeNumberInt,7) == 1) directions.add("NW");

		return directions;
	}
	
	//helper functions
	//rotates a byte "bits" by "shift" places to the left
	public static byte rotateLeft(byte bits, int shift)
    {
        return (byte)(((bits & 0xff) << shift) | ((bits & 0xff) >>> (8 - shift)));
    }

	//creates a linked list of all possible rotation positions of a figure with a certain code
    private static LinkedList<Integer> getShiftLeftFigureCodeCombinations(Byte code, int shift)
    {
        LinkedList<Integer> listOfCodes = new LinkedList<Integer>();
        for(int i = 0; i <= shift; i ++)
        {
            listOfCodes.add((rotateLeft(code,i) & 0xff));
        }
        return listOfCodes;
    }
    
    private int getBit(int n, int k) {
        return (n >> k) & 1;
    }
}
