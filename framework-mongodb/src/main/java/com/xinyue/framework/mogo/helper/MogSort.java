package com.xinyue.framework.mogo.helper;

import java.io.Serializable;

public class MogSort implements Serializable {

	private static final long serialVersionUID = 1780849244946639258L;
	
	public MogSort(String sortField,Direction direction){
		super();
		this.sortField=sortField;
		this.direction=direction;
	}
	
	/**
	 * 排序方式
	 */
	private Direction direction;
	
	
	/**
	 * 排序字段
	 */
	private String sortField;
	
	/**
	 * 自定义排序方式枚举	 
	 */
	public static enum Direction{
		ASC("asc"), DESC("desc");
		private String value;
		Direction(String value){
			this.value=value;
		}
		public String getValue(){
			return value;
		}
	}

	public String getSortField() {
		return sortField;
	}

	public Direction getDirection() {
		return direction;
	}

}
