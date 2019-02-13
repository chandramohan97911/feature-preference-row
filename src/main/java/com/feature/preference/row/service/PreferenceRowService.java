package com.feature.preference.row.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.feature.preference.row.dto.OrderWaitTimeDTO;
import com.feature.preference.row.dto.WorkOrderDTO;
import com.feature.preference.row.exception.DuplicateOrderException;
import com.feature.preference.row.exception.ErrorCode;
import com.feature.preference.row.exception.InvalidDataException;
import com.feature.preference.row.exception.OrderNotFoundException;
import com.feature.preference.row.model.OrderPriority;
import com.feature.preference.row.model.WorkOrder;
import com.feature.preference.row.util.ConvertionUtil;

@Service
public class PreferenceRowService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Queue<Map.Entry<Long, WorkOrder>> priorityQueue = new PriorityQueue<>(new CompareByValue());
	private List<Long> priorityOrderList = new ArrayList<>();
	/**
	 * 
	 * @param workOrderDto(String orderId, String orderDate)
	 * @throws InvalidDataException
	 * @throws DuplicateOrderException
	 */
	public void addOrder(WorkOrderDTO workOrderDto) throws InvalidDataException, DuplicateOrderException {
		WorkOrder order = new WorkOrder();
		Long orderId = ConvertionUtil.convertOrderId(workOrderDto.getOrderId());
		try {
			order.setOrderId(orderId);
			order.setOrderPriority(ConvertionUtil.findPriority(orderId));
			order.setOrderTime(ConvertionUtil.convertDate(workOrderDto.getOrderDate()));
			order.setAge(ConvertionUtil.calculateCurrentAge(order.getOrderTime()));
			order.setRank(ConvertionUtil.calculateRank(order));
		} catch (ParseException e) {
			throw new InvalidDataException(ErrorCode.FPR_0006, "Exception while parsing the date", e);
		}
		OrderEntry orderEntry = new OrderEntry(orderId, order);
		if (validateOrderExists(orderEntry)) {
			throw new DuplicateOrderException("Order " + orderId + " is already exists");
		}
		priorityQueue.add(orderEntry);
		updatePriorityOrdersList();
		logger.info("Priority Queue**** {}",priorityQueue);
	}
	/**
	 * processing the order and removing the order from QUEUE
	 * @return
	 * @throws OrderNotFoundException
	 */
	public WorkOrderDTO processOrder() throws OrderNotFoundException {
		checkPriorityEmptiness();
		Entry<Long, WorkOrder> order = priorityQueue.poll();
		processPriorityOrdersList(order.getKey());
		return new WorkOrderDTO(order.getKey().toString(),order.getValue().getOrderTime().toString());
	}

	/**
	 * Display Queue with Order Id's based on rank or processing order
	 * @return
	 * @throws OrderNotFoundException
	 */
	public List<Long> displayQueue() throws OrderNotFoundException {
		if (priorityOrderList.isEmpty()) {
			throw new OrderNotFoundException(ErrorCode.FPR_0002, "Queue is empty");
		}
		return priorityOrderList;
	}

	/**
	 * Removes the specific order from QUEUE which matches user input order id
	 * @param id
	 * @throws OrderNotFoundException
	 */
	public void removeOrder(String id) throws OrderNotFoundException {
		Long orderId = ConvertionUtil.convertOrderId(id);
		checkPriorityEmptiness();
		OrderEntry orderEntry = new OrderEntry(orderId, new WorkOrder(orderId));
		if (validateOrderExists(orderEntry)) {
			priorityQueue.remove(orderEntry);
			processPriorityOrdersList(orderId);
		} else {
			throw new OrderNotFoundException(ErrorCode.FPR_0002, "Order " + orderId + " Does not existing");
		}
	}

	/**
	 * 
	 * @param id
	 * @return Position of the order in the Queue which based on rank
	 * @throws OrderNotFoundException
	 */
	public int positionOfOrder(String id) throws OrderNotFoundException {
		Long orderId = ConvertionUtil.convertOrderId(id);
		if (priorityOrderList.isEmpty()) {
			throw new OrderNotFoundException(ErrorCode.FPR_0002, "Queue is empty");
		}
		return priorityOrderList.indexOf(orderId);
	}

	/**
	 * 
	 * @param Current date
	 * @return List<OrderWaitTimeDTO> OrderWaitTimeDTO(String orderId, Double avgWaitTime)
	 * @throws OrderNotFoundException
	 * @throws InvalidDataException
	 * @throws ParseException
	 */
	public List<OrderWaitTimeDTO> orderAvgWaitTime(String date) throws OrderNotFoundException, InvalidDataException, ParseException {
		checkPriorityEmptiness();
		List<OrderWaitTimeDTO> avgWaitList = new CopyOnWriteArrayList<>();
		Double totalTime = Double.valueOf(0);
		Queue<Map.Entry<Long, WorkOrder>> tempPriorityQueue = new PriorityQueue<>(priorityQueue);
		while (!tempPriorityQueue.isEmpty()) {
			Map.Entry<Long, WorkOrder> entry = tempPriorityQueue.poll();
			Double age = ConvertionUtil.caleculateCurrentAge(entry.getValue().getOrderTime(),date);
			totalTime +=age;
			avgWaitList.add(new OrderWaitTimeDTO(entry.getKey().toString(), age));
		}
		if(totalTime == 0){
			throw new InvalidDataException(ErrorCode.FPR_0002," No order exists or no wait time exisits");
		}
		for(OrderWaitTimeDTO dto : avgWaitList){
			dto.setAvgWaitTime(dto.getAvgWaitTime()/totalTime);
		}
		return avgWaitList;
	}

	/**
	 * 
	 * Sort the Order entry based on priority and Ranking when order entering to Queue
	 *
	 */
	private class CompareByValue implements Comparator<Map.Entry<Long, WorkOrder>> {

		@Override
		public int compare(Entry<Long, WorkOrder> lhs, Entry<Long, WorkOrder> rhs) {

			OrderPriority lhsPriority = lhs.getValue().getOrderPriority();
	    	OrderPriority rhsPriority = rhs.getValue().getOrderPriority();
	    	Double lhsRank = lhs.getValue().getRank();
	    	Double rhsRank = rhs.getValue().getRank();
	    	int priority = rhsPriority.compareTo(lhsPriority);
			if (priority != 0) {
				if(!(lhsPriority.ordinal() == OrderPriority.MANAGEMENT.ordinal() || rhsPriority.ordinal() == OrderPriority.MANAGEMENT.ordinal())){
					return rhsRank.compareTo(lhsRank);
				}
			}else{
				return rhsRank.compareTo(lhsRank);
			}
			return priority;
		}
	}

	/**
	 * 
	 * Implemented to support order entry to store in Priority queue
	 *
	 */
	private final class OrderEntry implements Map.Entry<Long, WorkOrder> {
		private final Long orderId;
		private WorkOrder workOrder;

		public OrderEntry(Long orderId, WorkOrder workOrder) {
			this.orderId = orderId;
			this.workOrder = workOrder;
		}

		@Override
		public Long getKey() {
			return orderId;
		}

		@Override
		public WorkOrder getValue() {
			return workOrder;
		}

		@Override
		public WorkOrder setValue(WorkOrder workOrder) {
			WorkOrder old = this.workOrder;
			this.workOrder = workOrder;
			return old;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof OrderEntry)) {
				return false;
			}
			OrderEntry order = (OrderEntry) obj;
			return order.orderId.longValue() == this.orderId.longValue();
		}

		@Override
		public int hashCode() {
			return orderId.hashCode();
		}
		 @Override
		    public String toString() {
		    	
		    	return String.format("%d: %f: $%s", orderId,workOrder.getRank(), workOrder.getOrderPriority());
		    }
	}

	

	public Queue<Map.Entry<Long, WorkOrder>> getPriorityQueue() {
		return priorityQueue;
	}
	
	public List<Long> getPriorityOrderList() {
		return priorityOrderList;
	}
	private void processPriorityOrdersList(Long orderId) {
		if(!priorityOrderList.isEmpty()) {
			priorityOrderList.remove(orderId);
		}
	}
	private void updatePriorityOrdersList() {
		Queue<Map.Entry<Long, WorkOrder>> tempPriorityQueue = new PriorityQueue<>(priorityQueue);
		priorityOrderList.clear();
		while (!tempPriorityQueue.isEmpty()) {
			Map.Entry<Long, WorkOrder> entry = tempPriorityQueue.poll();
			priorityOrderList.add(entry.getKey());
		}
	}

	private void checkPriorityEmptiness() throws OrderNotFoundException {
		if (priorityQueue.isEmpty()) {
			throw new OrderNotFoundException(ErrorCode.FPR_0002, "Queue is empty");
		}
	}

	private boolean validateOrderExists(OrderEntry order) {
		if (priorityQueue.isEmpty()) {
			return false;
		}else if (priorityQueue.contains(order)) {
			return true;
		}

		return false;
	}

}
