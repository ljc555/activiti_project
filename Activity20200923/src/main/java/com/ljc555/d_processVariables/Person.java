package com.ljc555.d_processVariables;

import java.io.Serializable;

public class Person implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6757393795687480331L;
	
	private Integer id;//���
	private String name;//����
	
	private String education;
	
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
