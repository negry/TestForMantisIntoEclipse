package com.opesystems.viewer;

import java.util.ArrayList;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.holoeverywhere.widget.Spinner;

import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.opesystems.converter.BarChartGenerator;
import com.opesystems.converter.PieChartGenerator;
import com.opesystems.converter.R;
import com.opesystems.converter.tools.ChartGenerator;
import com.opesystems.interfaces.INode;
import com.opesystems.interfaces.IReporter;
import com.opesystems.utils.ChartTypes;
import com.opesystems.utils.ColorViewConfiguration;
import com.opesystems.utils.DepthReports;

public class ChartViewerHoloFragment extends Fragment {
	//Views
	private Context context;
	private INode reportRoot;
	private LinearLayout principal;
	private Spinner selector;
	private LinearLayout chartLayout;
	private LinearLayout colorTableLayout;
	private View colorListView;
	private IReporter reporter = null;
	
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
	
	private final OnTouchListener chartlistener = new OnTouchListener() {
		float x1=0, x2=0, y1=0, y2=0, dx=0, dy=0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()) {
			    case(MotionEvent.ACTION_DOWN):
			        x1 = event.getX();
			        y1 = event.getY();
			        break;
			    case(MotionEvent.ACTION_UP):
			        x2 = event.getX();
			        y2 = event.getY();
			        dx = x2-x1;
		            dy = y2-y1;
		            if(Math.abs(dx)>75){
		            	if(Math.abs(dx) > Math.abs(dy)) {
		            		int chartSelected;
		            		if(dx>0){
		            			chartSelected = selector.getSelectedItemPosition()-1;
			            		if(chartSelected <= 0){
			            			chartSelected = selector.getCount()-1;
			            		}
				            } else {
				            	chartSelected = selector.getSelectedItemPosition()+1;
			            		if(chartSelected == selector.getCount()){
			            			chartSelected = 1;
			            		}
				            }
		            		selector.setSelection(chartSelected);
				        }
		            }
			        x1=0;
			        x2=0;
			        y1=0;
			        y2=0;
			        dx=0;
			        dy=0;
			        break;
			}
			return true;
		}
	};
	
	public static ChartViewerHoloFragment getInstance(Context context, INode node){
		Bundle arguments = new Bundle();
		arguments.putParcelable(ChartViewerHoloFragment.KEY_ROOT_NODE, node);
		Fragment fragment = ChartViewerHoloFragment.instantiate(ChartViewerHoloFragment.class, arguments);
		return (ChartViewerHoloFragment)fragment;
	}
	
	public static ChartViewerHoloFragment getInstance(Context context, IReporter reporter){
		ChartViewerHoloFragment fragment = getInstance(context, reporter.getReport());
		fragment.setReporter(reporter);
		return fragment;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().setSubtitle(R.string.chart_viewer);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.context=container.getContext();
		principal = (LinearLayout)inflater.inflate(R.layout.chart_viewer);
		selector = (org.holoeverywhere.widget.Spinner) principal.findViewById(R.id.spinner_chart_selector);
		chartLayout = (LinearLayout) principal.findViewById(R.id.linear_layout_chart);
		colorTableLayout = (LinearLayout) principal.findViewById(R.id.linear_layout_color_table);
		return principal;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(reporter != null){
			initialize();
		} else if(getArguments().getParcelable(KEY_ROOT_NODE) != null){
			initialize();
		}
	}
	
	private void initialize() {
		preferences = getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
		chartLayout.setOnTouchListener(chartlistener);
		if(reporter != null){
			reportRoot = reporter.getReport();
		} else {
			reportRoot = getArguments().getParcelable(KEY_ROOT_NODE);
		}
		colorListView = new ListView(context);
		maxDepthLevel = DepthReports.getDepthReport(reportRoot);
		depthLevelNames = DepthReports.getDepthNamesReport(reportRoot);
		actualDepthLevel = preferences.getInt(KEY_ACTUAL_DEPTH_LEVEL_SELECTED, 0);
		actualConfiguration = ColorViewConfiguration.values()[
                              	preferences.getInt(KEY_ACTUAL_COLOR_CONFIGURATION,
                      			ColorViewConfiguration.SHOW_COLORS_ONLY.ordinal())];
		int lastChartSelected = preferences.getInt(KEY_LAST_CHART_SELECTED, 0);
		drawSelector();
		selector.setSelection(lastChartSelected);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,
			com.actionbarsherlock.view.MenuInflater inflater) {
		SubMenu subMenuChart = menu.addSubMenu(getResources().getString(R.string.label_chart_menu));
		
		SubMenu menuDepthLevel = subMenuChart.addSubMenu(
				Menu.NONE, MENU_DEPTH_LEVEL_ID, Menu.NONE,
				getResources().getString(R.string.label_depth_level_menu));
		for(int i=0; i<maxDepthLevel; i++){
			menuDepthLevel.add(MENU_DEPTH_LEVEL_ID, i+1, Menu.NONE, depthLevelNames.get(i));
		}
		
		ColorViewConfiguration[] configurations = ColorViewConfiguration.values();
		SubMenu menuConfigurationLevel = subMenuChart.addSubMenu(
				Menu.NONE, MENU_CONFIGURATION_ID, Menu.NONE,
				getResources().getString(R.string.label_configuration_menu));
		for(int i=0; i<configurations.length; i++){
			menuConfigurationLevel.add(MENU_CONFIGURATION_ID,
					configurations[i].ordinal(), Menu.NONE,
					getResources().getString(configurations[i].getResource()));
		}
		
		subMenuChart.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		int groupId = item.getGroupId();
    	int itemId = item.getItemId();
    	switch (groupId) {
		case MENU_DEPTH_LEVEL_ID:
			int depthLevelSelected = itemId-1;
        	if(actualDepthLevel != depthLevelSelected){
        		actualDepthLevel = depthLevelSelected;
            	drawChart(selector.getSelectedItemPosition());
        	}
			break;
		case MENU_CONFIGURATION_ID:
			actualConfiguration = ColorViewConfiguration.values()[itemId];
			if(generator != null){
				drawColorList(generator);
			}
			break;
		default:
			break;
		}
    	return true;
    }
    
    @Override
	public void onStop(){
    	super.onStop();
		editor.putInt(KEY_LAST_CHART_SELECTED, selector.getSelectedItemPosition());
		editor.putInt(KEY_ACTUAL_DEPTH_LEVEL_SELECTED, actualDepthLevel);
		editor.putInt(KEY_ACTUAL_COLOR_CONFIGURATION, actualConfiguration.ordinal());
		editor.commit();
	}
    
	private void drawSelector(){
		ArrayList<String> chartSelection = new ArrayList<String>();
		chartSelection.add(getResources().getString(R.string.label_default_chart_selection));
		for(int i=0; i<ChartTypes.values().length; i++){
			String name = getResources().getString(ChartTypes.values()[i].getResource());
			chartSelection.add(name);
		}
		String[] items = chartSelection.toArray(new String[chartSelection.size()]);
		SpinnerAdapter adapter = new ArrayAdapter<String>(context, 0, items){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				String text = getItem(position);
				TextView textView = new TextView(context);
				textView.setTextColor(Color.BLACK);
				textView.setGravity(Gravity.CENTER);
				textView.setText(text);
				return textView;
			}
			
			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				if(position!=0){
					TextView textView = new TextView(context);
					String text = getItem(position);
					textView.setHeight(50);
					textView.setGravity(Gravity.CENTER);
					textView.setText(text);
					return textView;
				} else {
					return new View(context);
				}
			}
		};
		selector.setAdapter(adapter);
		selector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				drawChart(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		selector.setOnItemClickListener(new OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> parent, View view, 
//		            int pos, long id) {
//				drawChart(pos);
//		    }
//
//		    public void onNothingSelected(AdapterView<?> parent) {}
//		});
	}
	
	private void drawChart(int selection) {
		chartLayout.removeAllViews();
		colorTableLayout.removeAllViews();
		switch (selection) {
			case 1:
				drawPieChart();
				break;
			case 2:
				drawMonochromaticBarChart();			
				break;
			case 3:
				drawPolychromaticBarChart();
				break;
			case 4:
				drawDepthLevelBarChart();
				break;
			default:
				break;
		}
	}

	private void drawPieChart(){
		PieChartGenerator generador = new PieChartGenerator(context);
		chartLayout.addView(generador.getChart(reportRoot, actualDepthLevel));
		
		colorTableLayout.addView(colorListView);
		drawColorList(generador);
	}
	
	private void drawMonochromaticBarChart(){
		BarChartGenerator generador = new BarChartGenerator(context);
		chartLayout.addView(generador.getMonochromaticChart(reportRoot, actualDepthLevel));
		
		colorTableLayout.addView(colorListView);
		drawColorList(generador);
		drawColumnList(generador);
	}
	
	private void drawPolychromaticBarChart(){
		BarChartGenerator generador = new BarChartGenerator(context);
		chartLayout.addView(generador.getPolychromaticChart(reportRoot, actualDepthLevel));
		
		colorTableLayout.addView(colorListView);
		drawColorList(generador);
	}
	
	private void drawDepthLevelBarChart(){
		BarChartGenerator generador = new BarChartGenerator(context);
		chartLayout.addView(generador.getDepthLevelChart(reportRoot, actualDepthLevel));
		
		colorTableLayout.addView(colorListView);
		drawColorList(generador);
	}
	
	private void drawColorList(ChartGenerator generator){
		this.generator = generator;
		ViewGroup parent = (ViewGroup) colorListView.getParent();
	    int index = parent.indexOfChild(colorListView);
	    parent.removeView(colorListView);
	    boolean showBars = false;
	    int position = selector.getSelectedItemPosition();
	    switch(position){
		    case 1:case 2:default:
		    	showBars=false;
		    	break;
		    case 3:case 4:
		    	showBars=true;
		    	break;
	    }
	    colorListView = generator.getLastColorRelationshipsAsView(showBars, actualConfiguration);
	    parent.addView(colorListView, index);
	}
	
	private void drawColumnList(ChartGenerator generator){
		ListView domainList = generator.getLastDomainRelationshipsAsView();
		colorTableLayout.addView(domainList);
	}

	public IReporter getReporter() {
		return reporter;
	}

	public void setReporter(IReporter reporter) {
		this.reporter = reporter;
	}
}