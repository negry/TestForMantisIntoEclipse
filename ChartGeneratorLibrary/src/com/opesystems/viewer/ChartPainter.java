/**
 * 
 */
package com.opesystems.viewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.opesystems.interfaces.INode;

/**
 * @author luicaba
 *
 */
public class ChartPainter {
	public void drawChartView(FragmentActivity activity, View container, INode node){
		Bundle arguments = new Bundle();
		arguments.putParcelable(ChartViewerFragment.KEY_ROOT_NODE, node);
		Fragment fragment = ChartViewerFragment.instantiate(activity, ChartViewerFragment.class.getName(), arguments);
		
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		fragmentTransaction.add(container.getId(), fragment);
		fragmentTransaction.commit();
	}
}
