package com.opesystems.interfaces;

import java.util.ArrayList;
import java.util.Date;

public interface IFee extends INode {
	public String getName();

	public void setName(String name);

	public void setStartDate(Date startDate);

	public Date getStartDate();

	public void setObjectiveDate(Date date);

	public Date getObjectiveDate();

	public void setObjectiveAmount(double amount);

	public double getObjectiveAmount();

	public void setSubtractionDescription(String subtractionDescription);

	public String getSubtractionDescription();

	public void setSurplusDescription(String description);

	public String getSurplusDescription();

	public ArrayList<INode> getComplementElements();

	public void addComplementElement(INode element);
}