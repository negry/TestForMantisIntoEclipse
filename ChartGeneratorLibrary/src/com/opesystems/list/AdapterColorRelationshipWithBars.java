/**
 * 
 */
package com.opesystems.list;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.opesystems.converter.R;
import com.opesystems.utils.BarColorsRelationship;
import com.opesystems.utils.ColorRelationship;
import com.opesystems.utils.ColorViewConfiguration;

/**
 * @author luicaba
 *
 */
public class AdapterColorRelationshipWithBars extends ArrayAdapter<BarColorsRelationship>{
	private int resource;
	private List<BarColorsRelationship> relations;
	private Double totalAmount;
	private ColorViewConfiguration configuration;

	private class HolderBarColorRow {
		public TextView textViewBarName;
		public LinearLayout linearLayoutColors;
	}
	
	private class ColorRow {
		public TextView textViewColor;
		public TextView textViewPercentages;
		public TextView textViewAmount;
		public View viewSeparator;
	}

	public AdapterColorRelationshipWithBars(Context context,
			List<BarColorsRelationship> relations, ColorViewConfiguration configuration) {
		super(context, R.layout.row_bar_color, relations);
		resource = R.layout.row_bar_color;
		this.relations = relations;
		this.configuration = configuration;
		calculateTotal();
	}
	
	private void calculateTotal(){
		totalAmount = 0.0;
		for(int index=0; index<relations.size(); index++){
			List<Double> actualAmounts = relations.get(index).getAmounts();
			for(int i=0; i<actualAmounts.size(); i++){
				totalAmount += actualAmounts.get(i);
			}
		}
	}
	
	private String getPercentageAsString(double total, double amount) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat percentageFormat = new DecimalFormat("##0.##%", symbols);
		double percentage = amount/ total;
		String percentageAsString = percentageFormat.format(percentage);
		return percentageAsString;
	}

	private HolderBarColorRow initBarColorRow(View item) {
		HolderBarColorRow rBarColorR = new HolderBarColorRow();
		rBarColorR.textViewBarName = (TextView) item.findViewById(R.id.text_view_bar_name);
		rBarColorR.linearLayoutColors = (LinearLayout) item.findViewById(R.id.linear_layout_colors);
		return rBarColorR;
	}
	
	private ColorRow initColorRow(View item) {
		ColorRow rColorR = new ColorRow();
		rColorR.textViewColor = (TextView) item.findViewById(R.id.text_view_color);
		rColorR.textViewPercentages = (TextView) item.findViewById(R.id.text_view_percentages);
		rColorR.textViewAmount = (TextView) item.findViewById(R.id.text_view_amount);
		rColorR.viewSeparator = item.findViewById(R.id.view_separator);
		return rColorR;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item = convertView;
		HolderBarColorRow rBarColorRel;
		BarColorsRelationship barColorRel = this.getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		if (item == null) {
			item = inflater.inflate(resource, null);
			rBarColorRel = initBarColorRow(item);
			item.setTag(rBarColorRel);
		} else {
			rBarColorRel = (HolderBarColorRow) item.getTag();
		}
		((LinearLayout.LayoutParams)rBarColorRel.textViewBarName.getLayoutParams()).weight = 4;
		rBarColorRel.textViewBarName.setText(barColorRel.getBar());
		
		
		
		((LinearLayout.LayoutParams)rBarColorRel.linearLayoutColors.getLayoutParams()).weight = 1;
		rBarColorRel.linearLayoutColors.removeAllViews();
		List<ColorRelationship> actualRelationships = barColorRel.getColorRelationships();
		List<Double> actualAmounts = barColorRel.getAmounts();
		for(int index = 0; index < actualRelationships.size(); index++){
			ColorRelationship actualColorRelationship = actualRelationships.get(index);
			LinearLayout row = (LinearLayout)inflater.inflate(R.layout.row_color, null);
			ColorRow colorRow = initColorRow(row);
			((LinearLayout.LayoutParams)colorRow.textViewColor.getLayoutParams()).
			weight = (configuration.showPercentages() && configuration.showAmount())?2:1;
			colorRow.textViewColor.setText(actualColorRelationship.getName());
			colorRow.textViewColor.setBackgroundColor(actualColorRelationship.getColor());
			if(configuration.showPercentages()){
				((LinearLayout.LayoutParams)colorRow.textViewPercentages.getLayoutParams()).weight = 3;
				if(relations.size() > 1){
					colorRow.textViewPercentages.setText(getPercentageAsString(totalAmount, actualAmounts.get(index)));
				} else {
					colorRow.textViewPercentages.setText(getPercentageAsString(totalAmount, totalAmount));
				}
			} else {
				colorRow.textViewPercentages.setVisibility(View.GONE);
			}
			if(configuration.showAmount()){
				((LinearLayout.LayoutParams)colorRow.textViewAmount.getLayoutParams()).weight = 3;
				if(relations.size()>1){
					colorRow.textViewAmount.setText(Double.toString(actualAmounts.get(index)));
				} else {
					colorRow.textViewAmount.setText(Double.toString(totalAmount));
				}
			} else {
				colorRow.textViewAmount.setVisibility(View.GONE);
			}
			if(index < actualRelationships.size()-1){
				colorRow.viewSeparator.setVisibility(View.VISIBLE);
			}
			rBarColorRel.linearLayoutColors.addView(row);
		}
		return item;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
}