/**
 * 
 */
package com.opesystems.converter.tools;

import com.androidplot.pie.Segment;

/**
 * @author luicaba
 *
 */
public class ChartSegment extends Segment{

	private int color;
	
	public ChartSegment(String name, Number value, int color) {
		super(name, value);
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
