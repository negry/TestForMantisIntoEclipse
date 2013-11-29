/**
 * 
 */
package com.opesystems.utils;

import java.util.List;


/**
 * @author luicaba
 *
 */
public class BarColorsRelationship {
	private String bar;
	private List<ColorRelationship> colorRelationships;
	private List<Double> amounts;
	
	public BarColorsRelationship() {
		super();
	}
	
	public BarColorsRelationship(String bar,
			List<ColorRelationship> colorRelationships, List<Double> amounts) {
		super();
		this.bar = bar;
		this.colorRelationships = colorRelationships;
		this.amounts = amounts;
	}



	public String getBar() {
		return bar;
	}
	
	public void setBar(String bar) {
		this.bar = bar;
	}
	
	public List<ColorRelationship> getColorRelationships() {
		return colorRelationships;
	}
	
	public void setColorRelationships(List<ColorRelationship> colorRelationships) {
		this.colorRelationships = colorRelationships;
	}
	
	public void addColColorRelationship(ColorRelationship relation){
		colorRelationships.add(relation);
	}

	public List<Double> getAmounts() {
		return amounts;
	}

	public void setAmounts(List<Double> amounts) {
		this.amounts = amounts;
	}
	
	public void addAmount(Double amount){
		amounts.add(amount);
	}
}
