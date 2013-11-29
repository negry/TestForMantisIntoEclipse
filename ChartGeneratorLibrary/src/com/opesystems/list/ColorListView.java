/**
 * 
 */
package com.opesystems.list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.opesystems.converter.R;
import com.opesystems.utils.ColorRelationship;
import com.opesystems.utils.ColorViewConfiguration;

/**
 * @author luicaba
 *
 */
public class ColorListView extends ListView{
	private AdapterColorRelationship colorAdapter;
	
	public ColorListView(Context context, List<ColorRelationship> relations,
			List<Double> values, ColorViewConfiguration configuration) {
		super(context);
		colorAdapter = new AdapterColorRelationship(context, relations, values, configuration);
		if(configuration.showAmount() && relations.size()>1){
			showFooterView();
		}
		setAdapter(colorAdapter);
	}
	
	private ColorListView(Context context) {
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
		((LinearLayout.LayoutParams)textViewTotalAmount.getLayoutParams()).weight = 3;
		textViewTotalAmount.setText(Double.toString(colorAdapter.getTotalAmount()));
		return footerView;
	}
}
