package com.feature.preference.row.resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.feature.preference.row.dto.WorkOrderDTO;
import com.feature.preference.row.exception.DuplicateOrderException;
import com.feature.preference.row.exception.OrderNotFoundException;
import com.feature.preference.row.service.PreferenceRowService;

public class PreferenceRowServiceTest {
	
	private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	private PreferenceRowService subjectUnderTest = new PreferenceRowService();
	
	@Test
	public void testAddOrder() throws Exception {
		WorkOrderDTO order = getOrderObject("3", LocalDateTime.now());
		subjectUnderTest.addOrder(order);
		Assert.assertSame(1, subjectUnderTest.getPriorityQueue().size());
	}

	@Test(expected = NumberFormatException.class)
	public void testAddOrderWrongId() throws Exception {
		WorkOrderDTO order = getOrderObject("9223372036854775808", LocalDateTime.now());
		subjectUnderTest.addOrder(order);
	}
	@Test(expected=DuplicateOrderException.class)
	public void testAddOrderDuplicateOrder() throws Exception {
		WorkOrderDTO orderDTO1 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		WorkOrderDTO orderDTO2 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		subjectUnderTest.addOrder(orderDTO1);
		subjectUnderTest.addOrder(orderDTO2);
	}

	@Test(expected = NumberFormatException.class)
	public void testAddOrderWrongOrderDate() throws Exception {
		WorkOrderDTO order = getOrderObject("9223372036854775808", "11-2-2019 11:066:00 PM");
		subjectUnderTest.addOrder(order);
	}

	@Test(expected = OrderNotFoundException.class)
	public void testProcessOrderQueueIsEmpty() throws Exception {
		subjectUnderTest.processOrder();

	}

	@Test
	public void testProcessOrderQueue() throws Exception {

		WorkOrderDTO orderDTO1 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		WorkOrderDTO orderDTO2 = new WorkOrderDTO("5", "13-2-2019 09:46:00 PM");

		WorkOrderDTO orderDTO3 = new WorkOrderDTO("3", "13-2-2019 09:46:00 PM");
		WorkOrderDTO orderDTO4 = new WorkOrderDTO("2", "13-2-2019 09:46:00 PM");

		WorkOrderDTO orderDTO5 = new WorkOrderDTO("30", "13-2-2019 09:34:00 PM");
		WorkOrderDTO orderDTO6 = new WorkOrderDTO("10", "13-2-2019 09:34:00 PM");

		WorkOrderDTO orderDTO7 = new WorkOrderDTO("6", "13-2-2019 09:34:00 PM");
		WorkOrderDTO orderDTO8 = new WorkOrderDTO("4", "13-2-2019 09:34:00 PM");

		WorkOrderDTO orderDTO9 = new WorkOrderDTO("45", "13-2-2019 08:16:00 PM");
		WorkOrderDTO orderDTO10 = new WorkOrderDTO("20", "13-2-2019 08:16:00 PM");

		WorkOrderDTO orderDTO11 = new WorkOrderDTO("9", "13-2-2019 08:16:00 PM");
		WorkOrderDTO orderDTO12 = new WorkOrderDTO("8", "13-2-2019 08:16:00 PM");

		subjectUnderTest.addOrder(orderDTO1);
		subjectUnderTest.addOrder(orderDTO2);
		subjectUnderTest.addOrder(orderDTO3);
		subjectUnderTest.addOrder(orderDTO4);
		subjectUnderTest.addOrder(orderDTO5);
		subjectUnderTest.addOrder(orderDTO6);
		subjectUnderTest.addOrder(orderDTO7);
		subjectUnderTest.addOrder(orderDTO8);
		subjectUnderTest.addOrder(orderDTO9);
		subjectUnderTest.addOrder(orderDTO10);
		subjectUnderTest.addOrder(orderDTO11);
		subjectUnderTest.addOrder(orderDTO12);
		
		List<Long> queue = subjectUnderTest.getPriorityOrderList();
		Assert.assertSame(12, subjectUnderTest.getPriorityQueue().size());
		Assert.assertSame(12, queue.size());
		while(!subjectUnderTest.getPriorityOrderList().isEmpty()){
			Assert.assertSame(queue.get(0), Long.valueOf(subjectUnderTest.processOrder().getOrderId()));
		}
		Assert.assertSame(0, subjectUnderTest.getPriorityOrderList().size());
		Assert.assertSame(0, subjectUnderTest.getPriorityQueue().size());

	}

	@Test(expected = OrderNotFoundException.class)
	public void testDisplayQueueIsEmpty() throws Exception {
		subjectUnderTest.displayQueue();

	}

	@Test
	public void testDisplayQueue() throws Exception {
		WorkOrderDTO orderDTO1 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		WorkOrderDTO orderDTO2 = new WorkOrderDTO("2", "13-2-2019 09:46:00 PM");
		subjectUnderTest.addOrder(orderDTO1);
		subjectUnderTest.addOrder(orderDTO2);
		List<Long> list = subjectUnderTest.displayQueue();
		Assert.assertSame(2, list.size());
		Assert.assertSame(Long.valueOf(15), list.get(0));
		Assert.assertSame(Long.valueOf(2), list.get(1));

	}

	@Test(expected = OrderNotFoundException.class)
	public void testRemoveFromQueueIsEmpty() throws Exception {
		subjectUnderTest.removeOrder("5");

	}

	@Test
	public void testRemoveFromQueue() throws Exception {
		WorkOrderDTO orderDTO1 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		subjectUnderTest.addOrder(orderDTO1);
		Assert.assertSame(1, subjectUnderTest.getPriorityOrderList().size());
		subjectUnderTest.removeOrder("15");
		Assert.assertSame(0, subjectUnderTest.getPriorityOrderList().size());
	}

	@Test(expected=OrderNotFoundException.class)
	public void testPositionOfOrderQueueIsEmpty() throws Exception {
		subjectUnderTest.positionOfOrder("5");
	}

	@Test
	public void testPositionOfOrderFromQueue() throws Exception {
		WorkOrderDTO orderDTO1 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		WorkOrderDTO orderDTO2 = new WorkOrderDTO("5", "13-2-2019 09:46:00 PM");
		subjectUnderTest.addOrder(orderDTO1);
		subjectUnderTest.addOrder(orderDTO2);
		Assert.assertSame(1, subjectUnderTest.positionOfOrder("5"));

	}

	@Test(expected = OrderNotFoundException.class)
	public void testOrderAvgWaitTimeQueueIsEmpty() throws Exception {
		subjectUnderTest.orderAvgWaitTime("11-2-2019 11:06:00 PM");
	}

	@Test(expected = OrderNotFoundException.class)
	public void testOrderAvgWaitTimeQueueDateInvalid() throws Exception {
		subjectUnderTest.orderAvgWaitTime("11-2-2019 11:006:00 PM");

	}

	@Test
	public void testOrderAvgWaitTime() throws Exception {
		WorkOrderDTO orderDTO1 = new WorkOrderDTO("15", "13-2-2019 09:46:00 PM");
		WorkOrderDTO orderDTO2 = new WorkOrderDTO("5", "13-2-2019 09:46:00 PM");
		subjectUnderTest.addOrder(orderDTO1);
		subjectUnderTest.addOrder(orderDTO2);
		Assert.assertFalse(subjectUnderTest.orderAvgWaitTime("11-2-2019 11:06:00 PM").isEmpty());

	}

	private WorkOrderDTO getOrderObject(String orderId, LocalDateTime orderDate) throws JsonProcessingException {
		return new WorkOrderDTO(orderId, orderDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

	}

	private WorkOrderDTO getOrderObject(String orderId, String orderDate) throws JsonProcessingException {
		return new WorkOrderDTO(orderId, orderDate);

	}

}
