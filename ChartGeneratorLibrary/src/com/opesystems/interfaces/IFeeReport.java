package com.opesystems.interfaces;


/**
 * 
 * @author luicaba
 *
 */
public interface IFeeReport{
	public void setName(String name);
	public String getName();
	public void setElement(INode element);
	public INode getElement();
	public int getDepthLevelToShow();
	public void setDepthLevelToShow(int depth);
}