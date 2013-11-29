/**
 * 
 */
package com.opesystems.support;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.opesystems.interfaces.INode;

/**
 * @author luicaba
 *
 */
public class BaseNode implements INode{
	private Date actualDate;
	private double actualAmount;
	private String actualDescription;
	private ArrayList<INode> elements;
	private String depthName;

	@SuppressWarnings("rawtypes")
	public final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BaseNode createFromParcel(Parcel in) {
            return new BaseNode(in); 
        }

        public BaseNode[] newArray(int size) {
            return new BaseNode[size];
        }
    };
	
	public BaseNode() {
		elements = new ArrayList<INode>();
	}
	
	@SuppressWarnings("unchecked")
	public BaseNode(Parcel in){
		this.actualDate = new Date(in.readLong());
		this.actualAmount = in.readDouble();
		this.actualDescription = in.readString();
		this.elements = in.readArrayList(INode.class.getClassLoader());
		this.depthName = in.readString();
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
	public void addElement(INode element) {
		this.elements.add(element);
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
	}
}