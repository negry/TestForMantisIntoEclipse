/**
 * 
 */
package com.opesystems.utils;

/**
 * @author luicaba
 * 
 */
public class DomainRelationship {
	String domainId;
	String name;

	public DomainRelationship() {
		domainId = "";
		name = "";
	}

	public DomainRelationship(String domainId, String name) {
		this.domainId = domainId;
		this.name = name;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
