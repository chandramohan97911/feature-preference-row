package com.feature.preference.row.resource;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feature.preference.row.dto.OrderWaitTimeDTO;
import com.feature.preference.row.dto.WorkOrderDTO;
import com.feature.preference.row.exception.DuplicateOrderException;
import com.feature.preference.row.exception.InvalidDataException;
import com.feature.preference.row.exception.OrderNotFoundException;
import com.feature.preference.row.service.PreferenceRowService;

@RestController
@RequestMapping("/orders")
public class PreferenceRowResource {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PreferenceRowService rowService;
	
	@PostMapping
	public ResponseEntity<String> addOrder(@RequestBody WorkOrderDTO workdOrder ) throws InvalidDataException, DuplicateOrderException{
		logger.trace("addOrder invoked {}", workdOrder);
		rowService.addOrder(workdOrder);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping
	public ResponseEntity<WorkOrderDTO> processOrder() throws OrderNotFoundException{
		logger.trace("processOrder invoked");
		return new ResponseEntity<>(rowService.processOrder(), HttpStatus.OK);
	}
	
	
	@GetMapping
	public List<Long> displayQueue() throws OrderNotFoundException{
		logger.trace("displayQueue invoked");
		return rowService.displayQueue();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeOrder(@PathVariable("id")String id) throws OrderNotFoundException{
		logger.trace("removeOrder invoked with Order Id{}",id);
		rowService.removeOrder(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/{id}/order")
	public int positionOfOrder(@PathVariable("id")String id) throws OrderNotFoundException{
		logger.trace("positionOfOrder invoked with Order Id{}",id);
		return rowService.positionOfOrder(id);
	}
	
	@GetMapping("/{date}")
	public List<OrderWaitTimeDTO> orderAvgWaitTime(@PathVariable("date")String date) throws OrderNotFoundException, InvalidDataException, ParseException{
		logger.trace("orderAvgWaitTime invoked with matching date{}",date);
		return rowService.orderAvgWaitTime(date);
	}
}
