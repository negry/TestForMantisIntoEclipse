/**
 * 
 */
package com.opesystems.utils;

import com.opesystems.converter.R;

/**
 * @author luicaba
 *
 */
public enum ColorViewConfiguration {
	SHOW_PERCENTAGES			(true, false, R.string.label_percentages_configuration),
	SHOW_AMOUNT					(false, true, R.string.label_amount_configuration),
	SHOW_PERCENTAGES_AND_AMOUNT	(true, true, R.string.label_percentages_and_amount_configuration),
	SHOW_COLORS_ONLY			(false, false, R.string.label_colors_only_configuration);
	
	private final boolean showPercentages;
	private final boolean showAmount;
	private final int resource;
	
	private ColorViewConfiguration(boolean showPercentages, boolean showAmount, int resource) {
		this.showPercentages = showPercentages;
		this.showAmount = showAmount;
		this.resource = resource;
	}

	public boolean showPercentages() {
		return showPercentages;
	}

	public boolean showAmount() {
		return showAmount;
	}

	public int getResource() {
		return resource;
	}
}
