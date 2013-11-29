package com.opesystems.viewer;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.opesystems.converter.BarChartGenerator;
import com.opesystems.converter.PieChartGenerator;
import com.opesystems.converter.R;
import com.opesystems.converter.tools.ChartGenerator;
import com.opesystems.interfaces.INode;
import com.opesystems.utils.ChartTypes;
import com.opesystems.utils.ColorViewConfiguration;
import com.opesystems.utils.DepthReports;

public class ChartViewerFragment extends Fragment {
	//Views
	private Context context;
	private INode reportRoot;
	private LinearLayout principal;
	private LinearLayout chartLayout;
	private Spinner selector;
	private ListView colorListView;
	
	//Objects to use when make a plot 
	private int maxDepthLevel;
	private ArrayList<String> depthLevelNames;
	private int actualDepthLevel;
	private ColorViewConfiguration actualConfiguration;
	private ChartGenerator generator = null;
	public static final String KEY_ROOT_NODE = "ROOT_NODE";
	
	//Menu ids
	private static final int MENU_DEPTH_LEVEL_ID = 1;
	private static final int MENU_CONFIGURATION_ID = 2;
	
	//Objects to save data
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor; 
	private static final String PREFERENCE_NAME = "MyChartPreferences";
	private static final String KEY_ACTUAL_DEPTH_LEVEL_SELECTED = "actualDepthLevelSelected";
	private static final String KEY_LAST_CHART_SELECTED = "lastChartSelected";
	private static final String KEY_ACTUAL_COLOR_CONFIGURATION = "actualColorSelected";
	
//	private final OnTouchListener chartlistener = new OnTouchListener() {
//		float x1=0, x2=0, y1=0, y2=0, dx=0, dy=0;
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			switch(event.getAction()) {
//			    case(MotionEvent.ACTION_DOWN):
//			        x1 = event.getX();
//			        y1 = event.getY();
//			        break;
//			    case(MotionEvent.ACTION_UP):
//			        x2 = event.getX();
//			        y2 = event.getY();
//			        dx = x2-x1;
//		            dy = y2-y1;
//		            if(Math.abs(dx)>75){
//		            	if(Math.abs(dx) > Math.abs(dy)) {
//		            		int chartSelected;
//		            		if(dx>0){
//		            			chartSelected = selector.getSelectedItemPosition()-1;
//			            		if(chartSelected <= 0){
//			            			chartSelected = selector.getCount()-1;
//			            		}
//				            } else {
//				            	chartSelected = selector.getSelectedItemPosition()+1;
//			            		if(chartSelected == selector.getCount()){
//			            			chartSelected = 1;
//			            		}
//				            }
//		            		selector.setSelection(chartSelected);
//				        }
//		            }
//			        x1=0;
//			        x2=0;
//			        y1=0;
//			        y2=0;
//			        dx=0;
//			        dy=0;
//			        break;
//			}
//			return true;
//		}
//	};
//	
//	public static ChartViewerFragment getInstance(Context context, INode node){
//		Bundle arguments = new Bundle();
//		arguments.putParcelable(ChartViewerFragment.KEY_ROOT_NODE, node);
//		Fragment fragment = ChartViewerFragment.instantiate(context, ChartViewerFragment.class.getName(), arguments);
//		return (ChartViewerFragment)fragment;
//	}
//	
//	@Override
//	public View onCreateView(android.view.LayoutInflater inflater,
//			ViewGroup container,
//			Bundle savedInstanceState) {
//		super.onCreateView(inflater, container, savedInstanceState);
//		this.context=container.getContext();
//		principal = new LinearLayout(context);
//		principal.setOrientation(LinearLayout.VERTICAL);
//		if(getArguments().getParcelable(KEY_ROOT_NODE) != null){
//			initialize();
//			setHasOptionsMenu(true);
//		}
//		return principal;
//	}
//	
//	private void initialize() {
//		preferences = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//		editor = preferences.edit();
//		chartLayout = new LinearLayout(context);
//		chartLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1));
//		chartLayout.setOrientation(LinearLayout.VERTICAL);
//		chartLayout.setOnTouchListener(chartlistener);
//		reportRoot = getArguments().getParcelable(KEY_ROOT_NODE);
//		colorListView = new ListView(context);
//		maxDepthLevel = DepthReports.getDepthReport(reportRoot);
//		depthLevelNames = DepthReports.getDepthNamesReport(reportRoot);
//		actualDepthLevel = preferences.getInt(KEY_ACTUAL_DEPTH_LEVEL_SELECTED, 0);
//		actualConfiguration = ColorViewConfiguration.values()[
//                              	preferences.getInt(KEY_ACTUAL_COLOR_CONFIGURATION,
//                      			ColorViewConfiguration.SHOW_COLORS_ONLY.ordinal())];
//		int lastChartSelected = preferences.getInt(KEY_LAST_CHART_SELECTED, 0);
//		drawSelector();
//		selector.setSelection(lastChartSelected);
//	}
//
//	@Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		SubMenu menuDepthLevel = menu.addSubMenu(
//				Menu.NONE, MENU_DEPTH_LEVEL_ID, Menu.NONE,
//				getResources().getString(R.string.label_depth_level_menu));
//		for(int i=0; i<maxDepthLevel; i++){
//			menuDepthLevel.add(MENU_DEPTH_LEVEL_ID, i+1, Menu.NONE, depthLevelNames.get(i));
//		}
//		
//		ColorViewConfiguration[] configurations = ColorViewConfiguration.values();
//		SubMenu menuConfigurationLevel = menu.addSubMenu(
//				Menu.NONE, MENU_CONFIGURATION_ID, Menu.NONE,
//				getResources().getString(R.string.label_configuration_menu));
//		for(int i=0; i<configurations.length; i++){
//			menuConfigurationLevel.add(MENU_CONFIGURATION_ID,
//					configurations[i].ordinal(), Menu.NONE,
//					getResources().getString(configurations[i].getResource()));
//		}
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    	int groupId = item.getGroupId();
//    	int itemId = item.getItemId();
//    	switch (groupId) {
//		case MENU_DEPTH_LEVEL_ID:
//			int depthLevelSelected = itemId-1;
//			if(actualDepthLevel != depthLevelSelected){
//				actualDepthLevel = depthLevelSelected;
//	        	drawChart(selector.getSelectedItemPosition());
//			}
//			break;
//		case MENU_CONFIGURATION_ID:
//			actualConfiguration = ColorViewConfiguration.values()[itemId];
//			if(generator != null){
//				drawColorList(generator);
//			}
//			break;
//		default:
//			break;
//		}
//    	return true;
//    }
//    
//    @Override
//	public void onStop(){
//    	super.onStop();
//		editor.putInt(KEY_LAST_CHART_SELECTED, selector.getSelectedItemPosition());
//		editor.putInt(KEY_ACTUAL_DEPTH_LEVEL_SELECTED, actualDepthLevel);
//		editor.putInt(KEY_ACTUAL_COLOR_CONFIGURATION, actualConfiguration.ordinal());
//		editor.commit();
//	}
//	
//    private void drawSelector(){
//		ArrayList<String> chartSelection = new ArrayList<String>();
//		chartSelection.add(getResources().getString(R.string.label_default_chart_selection));
//		for(int i=0; i<ChartTypes.values().length; i++){
//			String name = getResources().getString(ChartTypes.values()[i].getResource());
//			chartSelection.add(name);
//		}
//		String[] items = chartSelection.toArray(new String[chartSelection.size()]);
//		selector = new Spinner(context);
//		SpinnerAdapter adapter = new ArrayAdapter<String>(context, 0, items){
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				String text = getItem(position);
//				TextView textView = new TextView(context);
//				textView.setTextColor(Color.BLACK);
//				textView.setGravity(Gravity.CENTER);
//				textView.setText(text);
//				return textView;
//			}
//			
//			@Override
//			public View getDropDownView(int position, View convertView,
//					ViewGroup parent) {
//				if(position!=0){
//					TextView textView = new TextView(context);
//					String text = getItem(position);
//					textView.setHeight(50);
//					textView.setGravity(Gravity.CENTER);
//					textView.setText(text);
//					return textView;
//				} else {
//					return new View(context);
//				}
//			}
//		};
//		selector.setAdapter(adapter);
//		selector.setOnItemSelectedListener(new OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> parent, View view, 
//		            int pos, long id) {
//				drawChart(pos);
//		    }
//
//		    public void onNothingSelected(AdapterView<?> parent) {}
//		});
//		principal.addView(selector);
//		principal.addView(chartLayout);
//	}
//	
//	private void drawChart(int selection) {
//		switch (selection) {
//			case 1:
//				chartLayout.removeAllViews();
//				drawPieChart();
//				break;
//			case 2:
//				chartLayout.removeAllViews();
//				drawMonochromaticBarChart();			
//				break;
//			case 3:
//				chartLayout.removeAllViews();
//				drawPolychromaticBarChart();
//				break;
//			case 4:
//				chartLayout.removeAllViews();
//				drawDepthLevelBarChart();
//				break;
//			default:
//				break;
//		}
//	}
//
//	private void drawPieChart(){
//		PieChartGenerator generador = new PieChartGenerator(context);
//		chartLayout.addView(generador.getChart(reportRoot, actualDepthLevel));
//		
//		chartLayout.addView(colorListView);
//		drawColorList(generador);
//	}
//	
//	private void drawMonochromaticBarChart(){
//		BarChartGenerator generador = new BarChartGenerator(context);
//		chartLayout.addView(generador.getMonochromaticChart(reportRoot, actualDepthLevel));
//		
//		chartLayout.addView(colorListView);
//		drawColorList(generador);
//		drawColumnList(generador);
//	}
//	
//	private void drawPolychromaticBarChart(){
//		BarChartGenerator generador = new BarChartGenerator(context);
//		chartLayout.addView(generador.getPolychromaticChart(reportRoot, actualDepthLevel));
//		
//		chartLayout.addView(colorListView);
//		drawColorList(generador);
//		drawColumnList(generador);
//	}
//	
//	private void drawDepthLevelBarChart(){
//		BarChartGenerator generador = new BarChartGenerator(context);
//		chartLayout.addView(generador.getDepthLevelChart(reportRoot, actualDepthLevel));
//		
//		chartLayout.addView(colorListView);
//		drawColorList(generador);
//		drawColumnList(generador);
//	}
//	
//	private void drawColorList(ChartGenerator generator){
//		this.generator = generator;
//		ViewGroup parent = (ViewGroup) colorListView.getParent();
//	    int index = parent.indexOfChild(colorListView);
//	    parent.removeView(colorListView);
//	    colorListView = generator.getLastColorRelationshipsAsView(actualConfiguration);
//		colorListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
//	    parent.addView(colorListView, index);
//	}
//	
//	private void drawColumnList(ChartGenerator generator){
//		ListView domainList = generator.getLastDomainRelationshipsAsView();
//		domainList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
//		chartLayout.addView(domainList);
//	}
}