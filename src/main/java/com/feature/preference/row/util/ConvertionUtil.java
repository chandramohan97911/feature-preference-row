package com.feature.preference.row.util;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import org.springframework.util.StringUtils;

import com.feature.preference.row.exception.ErrorCode;
import com.feature.preference.row.exception.InvalidDataException;
import com.feature.preference.row.model.OrderPriority;
import com.feature.preference.row.model.WorkOrder;

public class ConvertionUtil {
	private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
	private static int PRIORITY = 3;
	private static int VIP = 5;
	private ConvertionUtil(){}
	
	/**
	 * Order Id supported format 1 to 9223372036854775807
	 * @param orderId
	 * @return Long type of OrderId 
	 */
	public static Long convertOrderId(String orderId) {
		Long id = Long.parseLong(orderId);
		if(id == 0){
			throw new NumberFormatException("Order Id shouldn't be zero");
		}
		return id;
	}
	/**
	 * Returns the age of Order on the basis of current age
	 * @param orderTime
	 * @return
	 */
	public static long calculateCurrentAge(LocalDateTime orderTime) {
		LocalDateTime currentTime = LocalDateTime.now();
		return Math.abs(ChronoUnit.SECONDS.between(orderTime, currentTime));
	}
	
	/**
	 * Returns the age of Order on the basis of provided date
	 * @param orderTime
	 * @param currentDate
	 * @return
	 * @throws ParseException
	 * @throws InvalidDataException
	 */
	public static Double caleculateCurrentAge(LocalDateTime orderTime, String currentDate) throws ParseException, InvalidDataException {
		LocalDateTime currentTime = convertDate(currentDate);
		return Double.valueOf(Math.abs(ChronoUnit.SECONDS.between(orderTime, currentTime)));
	}
	/**
	 * Calculates the rank based on Priority, if Order is of PRIORITY then use max(3,nlogn)
	 * if Order is of VIP then use max(4,2nlogn)
	 * @param order
	 * @return
	 */
	public static Double calculateRank(WorkOrder order){
		Long age = order.getAge();
		Double rank = null;
		if(order.getOrderPriority().ordinal() == OrderPriority.PRIORITY.ordinal()){
			rank =  Math.max(3, age*Math.log(age));
		}else if(order.getOrderPriority().ordinal() == OrderPriority.VIP.ordinal()){
			rank =  Math.max(4, 2*age*Math.log(age));
		}else{
			rank = order.getAge().doubleValue();
		}
		if(StringUtils.isEmpty(rank)){
			rank = Double.valueOf(0);
		}
		return rank;
		
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 * @throws InvalidDataException 
	 */
	public static LocalDateTime convertDate(String date) throws ParseException, InvalidDataException {
		LocalDateTime orderDate = null;
		try{
			orderDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
		}catch(DateTimeParseException e){
			throw new InvalidDataException(ErrorCode.FPR_0006,"Invalid Date Exception ",e);
		}
		return orderDate;
	}
	/**
	 * Find priority for the order
	 * @param orderId
	 * @return
	 */
	public static OrderPriority findPriority(Long orderId) {
		OrderPriority orderPriority = null;
		if (orderId % PRIORITY == 0) {
			orderPriority = OrderPriority.PRIORITY;
		} else if (orderId % VIP == 0) {
			orderPriority = OrderPriority.VIP;
		}
		if (orderId % VIP == 0 && orderId % PRIORITY == 0) {
			orderPriority = OrderPriority.MANAGEMENT;
		}
		if (orderPriority == null) {
			orderPriority = OrderPriority.NORMAL;
		}
		return orderPriority;

	}
}
