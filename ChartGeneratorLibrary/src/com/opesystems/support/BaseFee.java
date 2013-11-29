/**
 * 
 */
package com.opesystems.support;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.opesystems.interfaces.IFee;
import com.opesystems.interfaces.INode;

/**
 * @author luicaba
 *
 */
public class BaseFee implements IFee{
	private Date actualDate;
	private double actualAmount;
	private String actualDescription;
	private ArrayList<INode> elements;
	private String depthName;

	private String name;
	private Date startDate;
	private Date objectiveDate;
	private double objectiveAmount;
	private String subtractionDescription;
	private String surplusDescription;
	private ArrayList<INode> complementElements;

	@SuppressWarnings("rawtypes")
	public final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BaseFee createFromParcel(Parcel in) {
            return new BaseFee(in); 
        }

        public BaseFee[] newArray(int size) {
            return new BaseFee[size];
        }
    };
	
	public BaseFee() {
		elements = new ArrayList<INode>();
		complementElements = new ArrayList<INode>();
	}
	
	@SuppressWarnings("unchecked")
	public BaseFee(Parcel in){
		this.actualDate = new Date(in.readLong());
		this.actualAmount = in.readDouble();
		this.actualDescription = in.readString();
		this.elements = in.readArrayList(INode.class.getClassLoader());
		this.depthName = in.readString();
		
		this.name = in.readString();
		this.startDate = new Date(in.readLong());
		this.objectiveDate = new Date(in.readLong());
		this.objectiveAmount = in.readDouble();
		this.subtractionDescription = in.readString();
		this.surplusDescription = in.readString();
		this.complementElements = in.readArrayList(INode.class.getClassLoader());;
	}

	@Override
	public Date getDate() {
		return actualDate;
	}

	@Override
	public void setDate(Date date) {
		this.actualDate = date;
	}

	@Override
	public double getAmount() {
		return actualAmount;
	}

	@Override
	public void setAmount(double amount) {
		this.actualAmount = amount;
	}

	@Override
	public String getDescription() {
		return actualDescription;
	}

	@Override
	public void setDescription(String description) {
		this.actualDescription = description;
	}

	@Override
	public ArrayList<INode> getElements() {
		return elements;
	}

	@Override
	public String getDepthName() {
		return depthName;
	}

	@Override
	public void setDepthName(String depthName) {
		this.depthName = depthName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public Date getObjectiveDate() {
		return objectiveDate;
	}

	@Override
	public void setObjectiveDate(Date objectiveDate) {
		this.objectiveDate = objectiveDate;
	}

	@Override
	public double getObjectiveAmount() {
		return objectiveAmount;
	}

	@Override
	public void setObjectiveAmount(double objectiveAmount) {
		this.objectiveAmount = objectiveAmount;
	}

	@Override
	public String getSubtractionDescription() {
		return subtractionDescription;
	}

	@Override
	public void setSubtractionDescription(String subtractionDescription) {
		this.subtractionDescription = subtractionDescription;
	}

	@Override
	public String getSurplusDescription() {
		return surplusDescription;
	}

	@Override
	public void setSurplusDescription(String surplusDescription) {
		this.surplusDescription = surplusDescription;
	}

	@Override
	public void addElement(INode element) {
		this.elements.add(element);
	}

	@Override
	public ArrayList<INode> getComplementElements() {
		return complementElements;
	}

	@Override
	public void addComplementElement(INode element) {
		complementElements.add(element);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(actualDate.getTime());
		dest.writeDouble(actualAmount);
		dest.writeString(actualDescription);
		dest.writeArray(elements.toArray());
		dest.writeString(depthName);
		
		dest.writeString(name);
		dest.writeLong(startDate.getTime());
		dest.writeLong(objectiveDate.getTime());
		dest.writeDouble(objectiveAmount);
		dest.writeString(subtractionDescription);
		dest.writeString(surplusDescription);
		dest.writeArray(complementElements.toArray());
	}
}