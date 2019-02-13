package com.feature.preference.row.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
@JsonInclude(value = Include.NON_NULL)
public class WorkOrderDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(required = true, notes = "Range: 1 to 9223372036854775807", position = 0)
	private String orderId;
	@ApiModelProperty(required = true, notes = "DateFormat: dd-M-yyyy hh:mm:ss a \n Eg: 11-2-2019 11:06:00 PM", position = 1)
	private String orderDate;
	
	public WorkOrderDTO() {
		
	}
	
	public WorkOrderDTO(String orderId, String orderDate) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
}
