package com.opesystems.interfaces;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcelable;

public interface INode extends Parcelable{
	
	public void setDate(Date date);

	public Date getDate();

	public void setAmount(double amount);

	public double getAmount();

	public void setDescription(String description);

	public String getDescription();

	public ArrayList<INode> getElements();

	public void addElement(INode element);

	public void setDepthName(String depthName);

	public String getDepthName();
}