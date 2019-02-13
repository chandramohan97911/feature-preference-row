package com.feature.preference.row.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feature.preference.row.dto.WorkOrderDTO;
import com.feature.preference.row.exception.InvalidDataException;
import com.feature.preference.row.exception.OrderNotFoundException;
import com.feature.preference.row.service.PreferenceRowService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PreferenceRowResource.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PreferenceRowResourceTest {
	private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PreferenceRowService rowService;

	@Test
	public void testAddOrder() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.post("/orders").accept(MediaType.APPLICATION_JSON)
				.content(getOrderObjectJSON("3", LocalDateTime.now())).contentType(MediaType.APPLICATION_JSON);
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.CREATED.value(), postResponse.getStatus());
		logger.info("addOrder Response {}",postResponse.getContentAsString());
	}

	@Test
	public void testAddOrderWrongId() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.post("/orders").accept(MediaType.APPLICATION_JSON)
				.content(getOrderObjectJSON("9223372036854775808", LocalDateTime.now()))
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(NumberFormatException.class).when(rowService).addOrder(any(WorkOrderDTO.class));
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), postResponse.getStatus());
		logger.info("addOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testAddOrderWrongOrderDate() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.post("/orders").accept(MediaType.APPLICATION_JSON)
				.content(getOrderObjectJSON("9223372036854775808", "11-2-2019 11:066:00 PM"))
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(InvalidDataException.class).when(rowService).addOrder(any(WorkOrderDTO.class));
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), postResponse.getStatus());
		logger.info("addOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testProcessOrderQueueIsEmpty() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.put("/orders").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(OrderNotFoundException.class).when(rowService).processOrder();
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), postResponse.getStatus());
		logger.info("processOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testProcessOrderQueue() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.put("/orders").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		WorkOrderDTO orderDTO = new WorkOrderDTO("3", "11-2-2019 11:06:00 PM");
		when(rowService.processOrder()).thenReturn(orderDTO);
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.OK.value(), postResponse.getStatus());
		logger.info("processOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testDisplayQueueIsEmpty() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(OrderNotFoundException.class).when(rowService).displayQueue();
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), postResponse.getStatus());
		logger.info("displayQueue Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testDisplayQueue() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		List<Long> queue = new ArrayList<>();
		queue.add(5l);
		queue.add(10l);
		when(rowService.displayQueue()).thenReturn(queue);
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.OK.value(), postResponse.getStatus());
		logger.info("displayQueue Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testRemoveFromQueueIsEmpty() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.delete("/orders/"+"5").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(OrderNotFoundException.class).when(rowService).removeOrder("5");
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), postResponse.getStatus());
		logger.info("removeOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testRemoveFromQueue() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.delete("/orders/"+"5");
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.OK.value(), postResponse.getStatus());
		logger.info("removeOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testPositionOfOrderQueueIsEmpty() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders/"+"5/order").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(OrderNotFoundException.class).when(rowService).positionOfOrder("5");
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), postResponse.getStatus());
		logger.info("positionOfOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testPositionOfOrderFromQueue() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders/"+"5/order");
		when(rowService.positionOfOrder("5")).thenReturn(2);
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.OK.value(), postResponse.getStatus());
		logger.info("positionOfOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testOrderAvgWaitTimeQueueIsEmpty() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders/11-2-2019 11:06:00 PM").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(OrderNotFoundException.class).when(rowService).orderAvgWaitTime("11-2-2019 11:06:00 PM");
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(), postResponse.getStatus());
		logger.info("positionOfOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testOrderAvgWaitTimeQueueDateInvalid() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders/11-2-2019 11:006:00 PM").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		doThrow(InvalidDataException.class).when(rowService).orderAvgWaitTime("11-2-2019 11:006:00 PM");
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), postResponse.getStatus());
		logger.info("positionOfOrder Response {}",postResponse.getContentAsString());
	}
	@Test
	public void testOrderAvgWaitTime() throws Exception {
		RequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/orders/11-2-2019 11:06:00 PM");
		
		when(rowService.orderAvgWaitTime("11-2-2019 11:06:00 PM")).thenReturn(Mockito.any(List.class));
		MvcResult postResult = mockMvc.perform(postRequestBuilder).andReturn();
		MockHttpServletResponse postResponse = postResult.getResponse();
		assertEquals(HttpStatus.OK.value(), postResponse.getStatus());
		logger.info("positionOfOrder Response {}",postResponse.getContentAsString());
	}

	private String getOrderObjectJSON(String orderId, LocalDateTime orderDate) throws JsonProcessingException {
		WorkOrderDTO orderDTO = new WorkOrderDTO(orderId, orderDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(orderDTO);
	}
	private String getOrderObjectJSON(String orderId, String orderDate) throws JsonProcessingException {
		WorkOrderDTO orderDTO = new WorkOrderDTO(orderId, orderDate);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(orderDTO);
	}
}
