package com.feature.preference.row.dto;

public class OrderWaitTimeDTO {
	private String orderId;
	private Double avgWaitTime;
	
	public OrderWaitTimeDTO() {
	
	}
	
	
	public OrderWaitTimeDTO(String orderId, Double avgWaitTime) {
		super();
		this.orderId = orderId;
		this.avgWaitTime = avgWaitTime;
	}


	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Double getAvgWaitTime() {
		return avgWaitTime;
	}
	public void setAvgWaitTime(Double avgWaitTime) {
		this.avgWaitTime = avgWaitTime;
	}
	
}
