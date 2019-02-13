package com.feature.preference.row.model;

import java.time.LocalDateTime;

public class WorkOrder {
	private Long orderId;
	private OrderPriority orderPriority;
	private LocalDateTime orderTime;
	private Double rank;
	private Long age;

	public WorkOrder() {
		
	}

	public WorkOrder(Long orderId) {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public OrderPriority getOrderPriority() {
		return orderPriority;
	}

	public void setOrderPriority(OrderPriority orderPriority) {
		this.orderPriority = orderPriority;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}
	
	public Double getRank() {
		return rank;
	}

	public void setRank(Double rank) {
		this.rank = rank;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof WorkOrder)) {
			return false;
		}
		WorkOrder order = (WorkOrder) obj;

		return order.orderId == this.orderId;
	}

	@Override
	public int hashCode() {
		return orderId.hashCode();
	}

	@Override
	public String toString() {
		return "WorkOrder [orderId=" + orderId + ", orderPriority=" + orderPriority + ", orderTime=" + orderTime
				+ ", age=" + age + "]";
	}

}
