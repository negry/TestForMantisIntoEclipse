/**
 * 
 */
package com.opesystems.converter.tools;

import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.SimpleXYSeries.ArrayFormat;
import com.androidplot.xy.XYPlot;
import com.opesystems.interfaces.IFee;
import com.opesystems.interfaces.INode;
import com.opesystems.list.ColorListView;
import com.opesystems.list.ColorListViewWithBars;
import com.opesystems.utils.ColorRelationship;
import com.opesystems.utils.ColorViewConfiguration;
import com.opesystems.utils.DomainRelationship;

/**
 * @author luicaba
 *
 */
public class ChartGenerator {
	
	protected Context context;
	private ArrayList<ColorRelationship> lastColors;
	private ArrayList<Double> lastValues;
	private ArrayList<DomainRelationship> lastColumns;
	
	protected ChartGenerator(Context context) {
		this.context = context;
		lastColors = new ArrayList<ColorRelationship>();
		lastValues = new ArrayList<Double>();
		lastColumns = new ArrayList<DomainRelationship>();
	}
	
	protected XYPlot getBaseChart(){
		XYPlot chart = new XYPlot(context, "");
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		chart.setLayoutParams(params);
		chart.getLayoutManager().remove(chart.getLegendWidget());
		chart.setTicksPerRangeLabel(1);
        chart.getGraphWidget().setGridPadding(30, 10, 30, 0);
        chart.getGraphWidget().setSize(new SizeMetrics(
                0, SizeLayoutType.FILL,
                0, SizeLayoutType.FILL));
        chart.setDomainValueFormat(getAutomaticStringFormat());
        chart.setDomainStepValue(1);
		return chart;
	}
	
	private int getRandomColor(){
		int red = (int)(Math.random()*255);
		int green = (int)(Math.random()*255);
		int blue = (int)(Math.random()*255);
		return Color.rgb(red, green, blue);
	}

	protected ArrayList<ChartSegment> getAllSegments(INode node, int depth){
		lastColumns.clear();
		lastValues.clear();
		lastColors.clear();
		return getSegments(node, depth);
	}
	
	private ArrayList<ChartSegment> getSegments(INode node, int depth){
		ArrayList<ChartSegment> segments = new ArrayList<ChartSegment>();
		if(depth>0 && node.getElements().size()>0){
			for(int i=0; node.getElements().size() > i; i++){
				segments.addAll(getSegments(node.getElements().get(i), depth-1));
			}
		} else {
			int actualColor = getRandomColor();
			ChartSegment actualSegment = new ChartSegment(node.getDescription(), node.getAmount(), actualColor);
			String domainRelationshipName = getAutomaticStringFormat().
					format(lastColumns.size(), null, null).toString();
			ColorRelationship actualRelationship = new ColorRelationship(actualSegment.getTitle(), actualColor, domainRelationshipName);
			lastColors.add(actualRelationship);
			lastColumns.add(new DomainRelationship(domainRelationshipName, node.getDescription()));
			lastValues.add(node.getAmount());
			segments.add(actualSegment);
		}
		if(IFee.class.isInstance(node)){
			/*Agregamos los complementos como segmentos*/
			IFee fee = (IFee) node;
			if(depth>0 && fee.getComplementElements().size()>0){
				for(int j=0; fee.getComplementElements().size() > j; j++){
					segments.addAll(getSegments(fee.getComplementElements().get(j), depth-1));
				}
			} else {
				double subtraction = fee.getObjectiveAmount() - fee.getAmount();
				String domainRelationshipName = getAutomaticStringFormat().
						format(lastColumns.size(), null, null).toString();
				//Verificar excedente o residuo
				ChartSegment residualSegment = null;
				if(subtraction >= 0){
					int actualColor = getRandomColor();
					residualSegment = new ChartSegment(fee.getSubtractionDescription(), subtraction, actualColor);
					ColorRelationship actualRelationship = new ColorRelationship(residualSegment.getTitle(), actualColor, domainRelationshipName);
					lastColors.add(actualRelationship);
					lastColumns.add(new DomainRelationship(domainRelationshipName, fee.getSubtractionDescription()));
					lastValues.add(subtraction);
				} else if (subtraction < 0){
					subtraction*=-1;
					int actualColor = getRandomColor();
					residualSegment = new ChartSegment(fee.getSurplusDescription(), subtraction, actualColor);
					ColorRelationship actualRelationship = new ColorRelationship(residualSegment.getTitle(), actualColor, domainRelationshipName);
					lastColors.add(actualRelationship);
					lastColumns.add(new DomainRelationship(domainRelationshipName, fee.getSurplusDescription()));
					lastValues.add(subtraction);
				}
				//añadimos los segmentos a la serie
				segments.add(residualSegment);
			}
		}
		return segments;
	}
	
	protected ArrayList<ArrayList<ChartSegment>> getAllSegmentsForDepthLevel(INode node, int depth){
		lastColumns.clear();
		lastValues.clear();
		lastColors.clear();
		return getSegmentsForDepthLevel(node, depth);
	}
	
	private ArrayList<ArrayList<ChartSegment>> getSegmentsForDepthLevel(INode node, int depth){
		ArrayList<ArrayList<ChartSegment>> segments = new ArrayList<ArrayList<ChartSegment>>();
		int actualPosition = -1;
		if(depth>0 && node.getElements().size()>0){
			for(int i=0; node.getElements().size() > i; i++){
				segments.addAll(getSegmentsForDepthLevel(node.getElements().get(i), depth-1));
			}
		} else {
			int actualColor = getRandomColor();
			ChartSegment actualSegment = new ChartSegment(node.getDescription(), node.getAmount(), actualColor);
			String domainRelationshipName = getAutomaticStringFormat().
					format(lastColumns.size(), null, null).toString();
			ColorRelationship actualRelationship = new ColorRelationship(actualSegment.getTitle(), actualColor, domainRelationshipName);
			lastColors.add(actualRelationship);
			ArrayList<ChartSegment> actualList = new ArrayList<ChartSegment>();
			actualList.add(actualSegment);
			segments.add(actualList);
			actualPosition = segments.size()-1;
			if(!IFee.class.isInstance(node)){
				lastColumns.add(new DomainRelationship(domainRelationshipName, node.getDescription()));
			}
			lastValues.add(node.getAmount());
		}
		if(IFee.class.isInstance(node)){
			/*Agregamos los complementos como segmentos*/
			IFee fee = (IFee) node;
			if(depth>0 && fee.getComplementElements().size()>0){
				for(int j=0; fee.getComplementElements().size() > j; j++){
					segments.addAll(getSegmentsForDepthLevel(fee.getComplementElements().get(j), depth-1));
				}
			} else {
				double subtraction = fee.getObjectiveAmount() - fee.getAmount();
				//Verificar excedente o residuo
				ChartSegment residualSegment = null;
				String domainRelationshipName = getAutomaticStringFormat().
						format(lastColumns.size(), null, null).toString();
				if(subtraction >= 0){
					int actualColor = getRandomColor();
					residualSegment = new ChartSegment(fee.getSubtractionDescription(), subtraction, actualColor);
					ColorRelationship actualRelationship = new ColorRelationship(residualSegment.getTitle(), actualColor, domainRelationshipName);
					lastColors.add(actualRelationship);
				} else if (subtraction < 0){
					subtraction*=-1;
					int actualColor = getRandomColor();
					residualSegment = new ChartSegment(fee.getSurplusDescription(), subtraction, actualColor);
					ColorRelationship actualRelationship = new ColorRelationship(residualSegment.getTitle(), actualColor, domainRelationshipName);
					lastColors.add(actualRelationship);
				}
				//añadimos los segmentos a la serie
				if(actualPosition!=-1){
					segments.get(actualPosition).add(residualSegment);
					lastColumns.add(new DomainRelationship(domainRelationshipName, fee.getName()));
					lastValues.add(subtraction);
				} else {
					ArrayList<ChartSegment> residualList = new ArrayList<ChartSegment>();
					residualList.add(residualSegment);
					segments.add(residualList);
					lastColumns.add(new DomainRelationship(domainRelationshipName, residualSegment.getTitle()));
					lastValues.add(subtraction);
				}
			}
		}
		return segments;
	}
	
	protected ArrayList<SimpleXYSeries> getXYSeriesFromSegments(ArrayList<ChartSegment> segments) {
		ArrayList<SimpleXYSeries> series = new ArrayList<SimpleXYSeries>();
		for(int i=0; i<segments.size(); i++){
			ArrayList<Number> values = new ArrayList<Number>();
			for(int j=0; j<segments.size(); j++){
				if(i!=j){
					values.add(null);
				} else {
					values.add(segments.get(i).getValue());
				}
			}
			SimpleXYSeries serie = new SimpleXYSeries(values, ArrayFormat.Y_VALS_ONLY, segments.get(i).getTitle());
			series.add(serie);
		}
		return series;
	}
	
	protected ArrayList<SimpleXYSeries> getXYSeriesFromSegmentLists(ArrayList<ArrayList<ChartSegment>> segmentsForLevel) {
		ArrayList<SimpleXYSeries> series = new ArrayList<SimpleXYSeries>();
		for(int i=0; i<segmentsForLevel.size(); i++){
			for(int j=0; j<segmentsForLevel.get(i).size(); j++){
				ArrayList<Number> values = new ArrayList<Number>();
				for(int k=0; k<segmentsForLevel.size(); k++){
					if(i!=k){
						values.add(null);
					} else{
						double valueToShow = segmentsForLevel.get(i).get(j).getValue().doubleValue();
						values.add(valueToShow);
					}
				}
				SimpleXYSeries serie = new SimpleXYSeries(values, ArrayFormat.Y_VALS_ONLY, segmentsForLevel.get(i).get(j).getTitle());
				series.add(serie);
			}
		}
		return series;
	}
	
	protected Format getCustomFormat(final ArrayList<String> namesSerie){
		Format format = new NumberFormat() {
            private static final long serialVersionUID = 1L;			
            @Override
            public StringBuffer format(double value, StringBuffer buffer, FieldPosition field) {
				return new StringBuffer(namesSerie.get((int)(value+0.5)));
            }
            @Override
            public StringBuffer format(long value, StringBuffer buffer, FieldPosition field) {
                throw new UnsupportedOperationException("Not yet implemented.");
            }
            @Override
            public Number parse(String string, ParsePosition position) {
                throw new UnsupportedOperationException("Not yet implemented.");
            }
        };
		return format;
	}
	
	protected NumberFormat getAutomaticStringFormat(){
		NumberFormat format = new NumberFormat() {
			private static final long serialVersionUID = 1L;			
			@Override
			public StringBuffer format(double value, StringBuffer buffer, FieldPosition field) {
				int number = (int)(value+1.5);
				return new StringBuffer(getAutomaticString(number));
			}
			@Override
			public StringBuffer format(long value, StringBuffer buffer, FieldPosition field) {
				int number = (int)(value+1.5);
				return new StringBuffer(getAutomaticString(number));
			}
			@Override
			public Number parse(String string, ParsePosition position) {
				throw new UnsupportedOperationException("Not yet implemented.");
			}
			
			public String getAutomaticString(int lenght) {
				final int base = 26;
				int exponent = 0;
				double pow = 0;
				do {
					exponent++;
					pow += Math.pow(base, exponent);
				} while(pow<lenght);
				
				final char[] abcDigits = {
					'0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
					'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
				};
				String cadena = "";
				int amount = lenght;
				for(int i=exponent; i>0; i--){
					double actualPow = Math.pow(base, i-1);
					
					double maxActualComplement=0;
					for(int j=1; j<=i-1; j++){
						maxActualComplement+= Math.pow(base, j);
					}
					
					int count = 0;
					for(count=0; amount>maxActualComplement; count++){
						amount-=actualPow;
					}
					cadena+=abcDigits[count];
				}
				return cadena;
			}
		};
		return format;
	}
	
	public ArrayList<ColorRelationship> getLastColorRelationships(){
		return lastColors;
	}
	
	public View getLastColorRelationshipsAsView(boolean showBarName, final ColorViewConfiguration configuration){
		if(showBarName){
			return getLastColorsWithBars(configuration);
		} else {
			return getLastColorsAsView(configuration);
		}
	}
	
	private ListView getLastColorsAsView(final ColorViewConfiguration configuration){
		ListView colorList = new ColorListView(context, lastColors, lastValues, configuration);
		return colorList;
	}
	
	private View getLastColorsWithBars(final ColorViewConfiguration configuration){
		ListView barColorList = new ColorListViewWithBars(context, lastColors, lastValues, configuration);
		return barColorList;
	}
	
	public ArrayList<DomainRelationship> getLastDomainRelationships(){
		return lastColumns;
	}
	
	public ListView getLastDomainRelationshipsAsView(){
		ListView listView = new ListView(context);
		ListAdapter adapter = new ArrayAdapter<DomainRelationship>(context, 0, getLastDomainRelationships()){
			@Override
			public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
				LinearLayout layout = new LinearLayout(context);
				TextView id = new TextView(context);
				id.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 4));
				id.setGravity(Gravity.CENTER);
				id.setText(getItem(position).getDomainId());
				layout.addView(id);
				TextView textView = new TextView(context);
				textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
				textView.setText(getItem(position).getName());
				textView.setTextColor(Color.BLACK);
				layout.addView(textView);
				return layout;
			}
		};
		listView.setAdapter(adapter);
		return listView;
	}
}
