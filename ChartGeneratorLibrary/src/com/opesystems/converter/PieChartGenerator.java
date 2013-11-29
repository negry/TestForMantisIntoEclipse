package com.opesystems.converter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.opesystems.converter.tools.ChartGenerator;
import com.opesystems.converter.tools.ChartSegment;
import com.opesystems.interfaces.INode;

/**
 * @author luicaba
 * 
 */
public class PieChartGenerator extends ChartGenerator {

	public PieChartGenerator(Context context) {
		super(context);
	}

	public PieChart getChart(INode node, int depth) {
		ArrayList<ChartSegment> segmentos = getAllSegments(node, depth);
		EmbossMaskFilter emf = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 10, 8.2f);
		PieChart pieChart = getBasePieChart();
		makePieGrapghic(pieChart, segmentos, 15, emf);
		return pieChart;
	}
	
	private PieChart getBasePieChart(){
		PieChart pieChart = new PieChart(context, "");
		pieChart.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
		return pieChart;
	}

	private void makePieGrapghic(PieChart pie, ArrayList<ChartSegment> segments,
			int textSizeInDP, EmbossMaskFilter filter) {
		double sum = 0;
		double max = 0;
		
		for (int i = 0; i < segments.size(); i++) {
			SegmentFormatter sf = getSegmentFormatter(context, segments.get(i).getColor(),
					textSizeInDP, /*filter*/null, R.xml.pie_segment_default_formatter);
			Segment actualSegment = segments.get(i);
			actualSegment.setTitle("");
			pie.addSeries(actualSegment, sf);
			
			sum += actualSegment.getValue().doubleValue();
			if(actualSegment.getValue().doubleValue() > max){
				max = actualSegment.getValue().doubleValue();
			}
		}
		
		if (sum == max) {
			SegmentFormatter sf = new SegmentFormatter();
			pie.addSeries(new Segment("", 0.00001), sf);
		}
		pie.getBorderPaint().setColor(Color.TRANSPARENT);
		pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
		pie.getRenderer(PieRenderer.class).setDonutSize(0.0f, PieRenderer.DonutMode.PERCENT);
	}

	private SegmentFormatter getSegmentFormatter(Context context, int color,
			int textSizeInDP, EmbossMaskFilter filter, int configuration) {
		SegmentFormatter sf = new SegmentFormatter();
		sf.configure(context, configuration);
		sf.getFillPaint().setColor(color);
		if (filter != null) {
			sf.getFillPaint().setMaskFilter(filter);
		}
		sf.getLabelPaint().setTextSize(PixelUtils.dpToPix(textSizeInDP));
		sf.getLabelPaint().setFakeBoldText(true);
		MaskFilter textFilter = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
		sf.getLabelPaint().setMaskFilter(textFilter);
		return sf;
	}
}
