package com.opesystems.converter;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.opesystems.converter.tools.ChartGenerator;
import com.opesystems.converter.tools.ChartSegment;
import com.opesystems.interfaces.IFee;
import com.opesystems.interfaces.INode;
import com.opesystems.utils.ColorRelationship;

/**
 * @author luicaba
 * 
 */
public class BarChartGenerator extends ChartGenerator {

	public BarChartGenerator(Context context) {
		super(context);
	}

	public XYPlot getMonochromaticChart(INode node, int depth) {
		ArrayList<ChartSegment> segmentos = getAllSegments(node, depth);
		XYPlot barChart = getBaseChart();
		String name = "";
		if(!IFee.class.isInstance(node)){
			name = node.getDescription();
		} else {
			IFee fee = (IFee) node;
			name = fee.getName();
		}
		makeMonochromaticGrapghic(barChart, name, segmentos);
		return barChart;
	}

	public XYPlot getPolychromaticChart(INode node, int depth) {
		ArrayList<ChartSegment> segmentos = getAllSegments(node, depth);
		XYPlot barChart = getBaseChart();
		String name = "";
		if(!IFee.class.isInstance(node)){
			name = node.getDescription();
		} else {
			IFee fee = (IFee) node;
			name = fee.getName();
		}
		makePolychromaticGrapghic(barChart, name, segmentos);
		return barChart;
	}

	public XYPlot getDepthLevelChart(INode node, int depth) {
		ArrayList<ArrayList<ChartSegment>> segmentsForLevel = getAllSegmentsForDepthLevel(node, depth);
		XYPlot barChart = getBaseChart();
		makeDepthLevelGrapghic(barChart, segmentsForLevel);
		return barChart;
	}

	private void makeMonochromaticGrapghic(XYPlot plot, String name, ArrayList<ChartSegment> segments) {
		// Obtenemos los datos
		ArrayList<Double> numberSerie = new ArrayList<Double>();
		ArrayList<String> namesSerie = new ArrayList<String>();
		double max = 0;
		if (segments.size() > 1) {
			for (int i = 0; segments.size() > i; i++) {
				numberSerie.add(segments.get(i).getValue().doubleValue());
				namesSerie.add(segments.get(i).getTitle());
				if (segments.get(i).getValue().doubleValue() > max) {
					max = segments.get(i).getValue().doubleValue();
				}
			}
		} else if (segments.size() == 1) {
			ArrayList<String> customList = new ArrayList<String>();
			Double[] numbers = {0.0, segments.get(0).getValue().doubleValue(), 0.0};
			numberSerie.addAll(Arrays.asList(numbers));
			String[] names = {"", "", ""};
			namesSerie.addAll(Arrays.asList(names));
			String[] custom = {"", getAutomaticStringFormat().format(0), ""};
			customList.addAll(Arrays.asList(custom));
			max = segments.get(0).getValue().doubleValue();
			plot.setDomainValueFormat(getCustomFormat(customList));
		}

		plot.setRangeBoundaries(0, max, BoundaryMode.FIXED);
		SimpleXYSeries aprLevelsSeries = new SimpleXYSeries(numberSerie, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, name);
		plot.addSeries(aprLevelsSeries, new BarFormatter(segments.get(0).getColor(), Color.BLACK));
		if(getLastColorRelationships().size() > 0){
			ColorRelationship relationship = new ColorRelationship(aprLevelsSeries.getTitle(),
					segments.get(0).getColor(), getLastColorRelationships().get(0).getColumn());
			getLastColorRelationships().clear();
			getLastColorRelationships().add(relationship);
		}
		plot.setDomainStepValue(numberSerie.size());
		configureBarRenderer(plot);
	}

	private void makePolychromaticGrapghic(XYPlot plot, String name, ArrayList<ChartSegment> segments) {
		if (segments.size() > 1) {
			double max = 0;
			for (int i = 0; segments.size() > i; i++) {
				if (segments.get(i).getValue().doubleValue() > max) {
					max = segments.get(i).getValue().doubleValue();
				}
			}
			ArrayList<SimpleXYSeries> series = getXYSeriesFromSegments(segments);
			plot.setRangeBoundaries(0, max, BoundaryMode.FIXED);
			for (int i = 0; i < series.size(); i++) {
				plot.addSeries(series.get(i), new BarFormatter(segments.get(i).getColor(), Color.BLACK));
			}
			plot.setDomainStepValue(series.size());
			configureBarRenderer(plot);
		} else {
			makeMonochromaticGrapghic(plot, name, segments);
		}
	}

	private void makeDepthLevelGrapghic(XYPlot plot, ArrayList<ArrayList<ChartSegment>> segmentsForLevel) {
		if (segmentsForLevel.size() > 0) {
			// Obtenemos los datos
			double max = 0;
			for (int i = 0; segmentsForLevel.size() > i; i++) {
				int actualValue = 0;
				for (int j = 0; segmentsForLevel.get(i).size() > j; j++) {
					actualValue += segmentsForLevel.get(i).get(j).getValue().doubleValue();
				}
				if (actualValue > max) {
					max = actualValue;
				}
			}
			ArrayList<SimpleXYSeries> series = new ArrayList<SimpleXYSeries>();
			if (segmentsForLevel.size() > 1) {
				series = getXYSeriesFromSegmentLists(segmentsForLevel);
				plot.setDomainStepValue(segmentsForLevel.size());
			} else {
				ArrayList<SimpleXYSeries> seriesToShow = getXYSeriesFromSegmentLists(segmentsForLevel);
				for (int k = 0; k < seriesToShow.size(); k++) {
					seriesToShow.get(k).addFirst(null, 0.0);
					seriesToShow.get(k).addLast(null, 0.0);
				}
				series.addAll(seriesToShow);
				ArrayList<String> customList = new ArrayList<String>();
				customList.add("");
				customList.add(getAutomaticStringFormat().format(0));
				customList.add("");
				plot.setDomainValueFormat(getCustomFormat(customList));
				plot.setDomainStepValue(3);
			}
			plot.setRangeBoundaries(0, max, BoundaryMode.FIXED);
			for (int i = 0; i < series.size(); i++) {
				plot.addSeries(series.get(i), new BarFormatter(getLastColorRelationships().get(i).getColor(), Color.BLACK));
			}
			configureBarRenderer(plot);
		}
	}

	@SuppressWarnings("rawtypes")
	private void configureBarRenderer(XYPlot plot) {
		BarRenderer renderer = (BarRenderer) plot.getRenderer(BarRenderer.class);
		renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.STACKED);
		if (plot.getSeriesSet().iterator().next().size() > 5) {
			renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.VARIABLE_WIDTH);
			renderer.setBarWidth(1);
			renderer.setBarGap(0);
		} else {
			renderer.setBarWidth(50);
		}
	}
}
