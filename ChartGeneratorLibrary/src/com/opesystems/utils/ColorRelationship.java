/**
 * 
 */
package com.opesystems.utils;

import android.graphics.Color;

/**
 * @author luicaba
 * 
 */
public class ColorRelationship {
	private String name;
	private int color;
	private String column;

	public ColorRelationship() {
		this.name = "";
		this.color = Color.TRANSPARENT;
	}

	public ColorRelationship(String name, int color, String column) {
		this.name = name;
		this.color = color;
		this.column = column;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
