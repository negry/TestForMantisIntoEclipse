/**
 * 
 */
package com.opesystems.list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.opesystems.converter.R;
import com.opesystems.utils.BarColorsRelationship;
import com.opesystems.utils.ColorRelationship;
import com.opesystems.utils.ColorViewConfiguration;

/**
 * @author luicaba
 *
 */
public class ColorListViewWithBars extends ListView{
	private AdapterColorRelationshipWithBars barColorAdapter;
	public ColorListViewWithBars(Context context, List<ColorRelationship> relations,
			List<Double> values, ColorViewConfiguration configuration) {
		super(context);
		barColorAdapter = new AdapterColorRelationshipWithBars(context,
				getBarColorsRelationships(relations, values), configuration);
		if(configuration.showAmount() && relations.size()>1){
			showFooterView();
		}
		setAdapter(barColorAdapter);
	}
	
	private ColorListViewWithBars(Context context) {
		super(context);
	}
	
	private void showFooterView(){
		View footerView = getFooter();
		addFooterView(footerView);
	}
	
	private View getFooter(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		LinearLayout footerView = (LinearLayout) inflater.inflate(R.layout.row_total_footer, null);
		TextView textViewTotalLabel = (TextView) footerView.findViewById(R.id.text_view_total_label);
		((LinearLayout.LayoutParams)textViewTotalLabel.getLayoutParams()).weight = 1;
		TextView textViewTotalAmount = (TextView) footerView.findViewById(R.id.text_view_total_amount);
		((LinearLayout.LayoutParams)textViewTotalAmount.getLayoutParams()).weight = 4;
		textViewTotalAmount.setText(Double.toString(barColorAdapter.getTotalAmount()));
		return footerView;
	}
	
	private List<BarColorsRelationship> getBarColorsRelationships(List<ColorRelationship> relations, List<Double> amounts){
		LinkedHashMap<String, List<ColorRelationship>> barColorsMap = new LinkedHashMap<String, List<ColorRelationship>>();
		LinkedHashMap<String, List<Double>> amountsMap = new LinkedHashMap<String, List<Double>>();
		for(int index = 0; index < relations.size(); index++){
			ColorRelationship colorRelationship = relations.get(index);
			Double amount = amounts.get(index);
			List<ColorRelationship> listColors = barColorsMap.get(colorRelationship.getColumn());
			List<Double> listAmounts = amountsMap.get(colorRelationship.getColumn());
			if(listColors==null){
				listColors = new ArrayList<ColorRelationship>();
				listAmounts = new ArrayList<Double>();
			}
			listColors.add(colorRelationship);
			listAmounts.add(amount);
			barColorsMap.put(colorRelationship.getColumn(), listColors);
			amountsMap.put(colorRelationship.getColumn(), listAmounts);
		}
		List<BarColorsRelationship> barColorsRelationships = new ArrayList<BarColorsRelationship>();
		for (Entry<String, List<ColorRelationship>> entry : barColorsMap.entrySet()) {
			BarColorsRelationship actual = new BarColorsRelationship(entry.getKey(),
					entry.getValue(), amountsMap.get(entry.getKey()));
			barColorsRelationships.add(actual);
		}
		return barColorsRelationships;
	}
}