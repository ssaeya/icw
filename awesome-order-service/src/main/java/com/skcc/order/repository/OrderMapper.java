package com.skcc.order.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.skcc.order.domain.Order;
import com.skcc.order.event.message.OrderEvent;

@Mapper
public interface OrderMapper {

//	@Select("select * from orders where accountId = #{accountId}")
	public List<Order> findOrderByAccountId(long accountId);
	
//	@Insert("insert into orders(accountId, paymentId, accountInfo, productsInfo, paymentInfo, paid, active,createdAt) values(#{accountId}, #{accountInfo}, #{productsInfo}, #{paymentInfo}, #{paid}, #{active}, NOW())")
	public boolean createOrder(Order order);
	
//	@Update("update orders set status = 'canceled' where id = #{id}")
	public boolean cancelOrder(long id); 
	
//	@Update("update orders set paid = #{paid} where id = #{id}")
	public boolean payOrder(Order order); 
	
//	@Select("select get_account_eventid_seq('eventId')")
	public long getOrderEventId();

//	@Select("select get_account_eventid_seq('orderId')")
	public long getOrderId();
	
//	@insert("insert into order(id, domain, orderId, eventType, payload, txId, createdAt) values(#{id}, #{domain}, #{orderId}, #{eventType}, #{payload}, #{txId}, NOW())")
	public void createOrderEvent(OrderEvent orderEvent);
	
//	@select("select * from orders where id = #{id}")
	public Order findOrderById(long id); 
	
//	@Update("update orders set paymentId = #{paymentId}, paymentInfo = #{paymentInfo} where id = #{id}")
	public void setOrderPaymentId(Order order);
	
//	@Select("select * from order_events where txId = #{txId}")
	public OrderEvent findOrderEventByTxId(@Param("txId") String txId, @Param("eventType") String eventType);
	
//	@select("select * from order_events")
	public List<OrderEvent> getOrderEvent();
} 
