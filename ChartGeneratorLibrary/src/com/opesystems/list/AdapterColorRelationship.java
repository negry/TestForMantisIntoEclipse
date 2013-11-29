/**
 * 
 */
package com.opesystems.list;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.opesystems.converter.R;
import com.opesystems.utils.ColorRelationship;
import com.opesystems.utils.ColorViewConfiguration;

/**
 * @author luicaba
 *
 */
public class AdapterColorRelationship extends ArrayAdapter<ColorRelationship>{
	private int resource;
	private List<ColorRelationship> relations;
	private List<Double> values;
	private volatile Double totalAmount;
	private ColorViewConfiguration configuration;

	private class HolderColorRow {
		public TextView textViewColor;
		public TextView textViewPercentages;
		public TextView textViewAmount;
	}

	public AdapterColorRelationship(Context context, List<ColorRelationship> relations,
			List<Double> values, ColorViewConfiguration configuration) {
		super(context, R.layout.row_color, relations);
		if(relations.size() <= values.size()){
			resource = R.layout.row_color;
			this.relations = relations;
			this.values = values;
			this.configuration = configuration;
			calculateTotal();
		} else {
			clear();
			resource = R.layout.row_color;
			this.relations = new ArrayList<ColorRelationship>();
			this.values = new ArrayList<Double>();
			this.configuration = configuration;
			totalAmount = 0.0;
		}
	}
	
	private void calculateTotal(){
		totalAmount = 0.0;
		for(int i=0; i<values.size(); i++){
			totalAmount += values.get(i);
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

	private HolderColorRow initColorRow(View item) {
		HolderColorRow rColorR = new HolderColorRow();
		rColorR.textViewColor = (TextView) item.findViewById(R.id.text_view_color);
		rColorR.textViewPercentages = (TextView) item.findViewById(R.id.text_view_percentages);
		rColorR.textViewAmount = (TextView) item.findViewById(R.id.text_view_amount);
		return rColorR;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View item = convertView;
		HolderColorRow rColorRel;
		ColorRelationship colorRel = this.getItem(position);
		if (item == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			item = inflater.inflate(resource, null);
			rColorRel = initColorRow(item);
			item.setTag(rColorRel);
		} else {
			rColorRel = (HolderColorRow) item.getTag();
		}
		
		((LinearLayout.LayoutParams)rColorRel.textViewColor.getLayoutParams()).
		weight = (configuration.showPercentages() && configuration.showAmount())?2:1;
		rColorRel.textViewColor.setText(colorRel.getName());
		rColorRel.textViewColor.setBackgroundColor(colorRel.getColor());
		if(configuration.showPercentages()){
			((LinearLayout.LayoutParams)rColorRel.textViewPercentages.getLayoutParams()).weight = 3;
			if(relations.size() > 1){
				rColorRel.textViewPercentages.setText(getPercentageAsString(totalAmount, values.get(position)));
			} else {
				rColorRel.textViewPercentages.setText(getPercentageAsString(totalAmount, totalAmount));
			}
		} else {
			rColorRel.textViewPercentages.setVisibility(View.GONE);
		}
		if(configuration.showAmount()){
			((LinearLayout.LayoutParams)rColorRel.textViewAmount.getLayoutParams()).weight = 3;
			if(relations.size()>1){
				rColorRel.textViewAmount.setText(Double.toString(values.get(position)));
			} else {
				rColorRel.textViewAmount.setText(Double.toString(totalAmount));
			}
		} else {
			rColorRel.textViewAmount.setVisibility(View.GONE);
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