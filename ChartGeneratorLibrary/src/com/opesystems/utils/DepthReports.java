package com.opesystems.utils;

import java.util.ArrayList;

import com.opesystems.interfaces.IFee;
import com.opesystems.interfaces.INode;

/**
 * @author luicaba
 * 
 */
public class DepthReports {
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getDepthNamesReport(INode elementReport){
		ArrayList<String> nameList = new ArrayList<String>();
		if(elementReport != null){
			int actualLevel = 0;
			ArrayList<INode> actualElements = new ArrayList<INode>();
			ArrayList<INode> nextElements = new ArrayList<INode>();
			nextElements.add(elementReport);
			do{
				String actualName = null;
				actualElements = (ArrayList<INode>)nextElements.clone();
				nextElements = new ArrayList<INode>();
				int index=0;
				for(index = 0; index < actualElements.size(); index++){
					INode actualNode = actualElements.get(index);
					nextElements.addAll(actualNode.getElements());
					if(IFee.class.isInstance(actualNode)){
						nextElements.addAll(((IFee)actualNode).getComplementElements());
					}
					String nodeDepthName = actualNode.getDepthName();
					boolean isActualNameEmpty = actualName == null || actualName.equals("");
					if(isActualNameEmpty && nodeDepthName != null && !nodeDepthName.equals("")){
						actualName = nodeDepthName;
					}
				}
				if(actualName != null && !actualName.equals("")){
					nameList.add(actualName);
				} else {
					nameList.add(Integer.toString(actualLevel));
				}
				actualLevel++;
			} while(nextElements.size() > 0);
		}
		return nameList;
	}
	
	@SuppressWarnings("unchecked")
	public static int getDepthReport(INode elementReport){
		int actualLevel = 0;
		if(elementReport != null){
			ArrayList<INode> actualElements = new ArrayList<INode>();
			ArrayList<INode> nextElements = new ArrayList<INode>();
			nextElements.add(elementReport);
			do{
				actualElements = (ArrayList<INode>)nextElements.clone();
				nextElements = new ArrayList<INode>();
				int index=0;
				for(index = 0; index < actualElements.size(); index++){
					INode actualNode = actualElements.get(index);
					nextElements.addAll(actualNode.getElements());
					if(IFee.class.isInstance(actualNode)){
						nextElements.addAll(((IFee)actualNode).getComplementElements());
					}
				}
				actualLevel++;
			} while(nextElements.size() > 0);
		}
		return actualLevel;
	}
}
