/**
 * 
 */
package com.opesystems.utils;

import com.opesystems.converter.R;


/**
 * @author luicaba
 *
 */
public enum ChartTypes {
	PIE				(R.string.label_pie_chart),
	MONOCHROMATIC	(R.string.label_monochromatic_chart),
	POLYCHROMATIC	(R.string.label_polychromatic_chart),
	DEPTH_LEVEL		(R.string.label_depth_level_chart);
	
	private final int resource;
	
	private ChartTypes(int resource){
		this.resource = resource;
	}
	
	public int getResource(){
		return resource;
	}
}
