package com.skcc.order.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esotericsoftware.minlog.Log;
import com.skcc.order.domain.Order;
import com.skcc.order.domain.OrderPayment;
import com.skcc.order.event.message.OrderEvent;
import com.skcc.order.event.message.OrderEventType;
import com.skcc.order.event.message.OrderPayload;
import com.skcc.order.publish.OrderPublish;
import com.skcc.order.repository.OrderMapper;
import com.skcc.payment.event.message.PaymentEvent;
import com.skcc.product.event.message.ProductEvent;

@Service
public class OrderService {
	
	private OrderMapper orderMapper;
	private OrderPublish orderPublish;
	
	@Autowired
	private OrderService orderService;
	
	@Value("${domain.name}")
	private String domain;
	
	@Autowired
	public OrderService(OrderMapper orderMapper, OrderPublish orderPublish) {
		this.orderMapper = orderMapper;
		this.orderPublish = orderPublish;
	}
	
	public List<Order> findOrderByAccountId(long accountId) {
		return this.orderMapper.findOrderByAccountId(accountId);
	}
	
	public boolean createOrderAndCreatePublishOrderEvent(Order order) {
		boolean result = false;
		
		try {
			this.orderService.createOrderAndCreatePublishOrderCreatedEvent(order);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.orderService.createPublishOrderCreateFailedEvent(order);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean cancelOrderAndCreatePublishOrderEvent(long id) {
		boolean result = false;
		
		Order resultOrder = this.findOrderById(id);
		try {
			this.orderService.cancelOrderAndCreatePublishOrderCanceledEvent(null, resultOrder);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.orderService.createPublishOrderCanceledEvent(null, resultOrder);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean cancelOrderAndCreatePublishOrderEvent(ProductEvent productEvent) {
		boolean result = false;
		
		String txId = productEvent.getTxId();
		String eventType = OrderEventType.OrderCreated.toString();
		Order resultOrder = this.convertOrderEventToOrder(this.findOrderEventByTxId(txId, eventType));
		try {
			this.orderService.cancelOrderAndCreatePublishOrderCanceledEvent(txId, resultOrder);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.orderService.createPublishOrderCanceledEvent(txId, resultOrder);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean cancelOrderAndCreatePublishOrderEvent(PaymentEvent paymentEvent) {
		boolean result = false;
		
		String txId = paymentEvent.getTxId();
		String eventType = OrderEventType.OrderCreated.toString();
		Order resultOrder = this.convertOrderEventToOrder(this.findOrderEventByTxId(txId, eventType));
		try {
			this.orderService.cancelOrderAndCreatePublishOrderCanceledEvent(txId, resultOrder);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.orderService.createPublishOrderCanceledEvent(txId, resultOrder);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean setOrderPaymentIdAndCreatePublishOrderEvent(PaymentEvent paymentEvent) {
		boolean result = false;
		
		Order resultOrder = this.findOrderAndSetPaymentId(paymentEvent);
		try {
			this.orderService.setOrderPaymentIdAndCreatePublishOrderPaymentIdSetEvent(paymentEvent.getTxId(), resultOrder);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.orderService.createPublishOrderPaymentIdSetFailedEvent(paymentEvent.getTxId(), resultOrder);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean payOrderAndCreatePublishOrderEvent(PaymentEvent paymentEvent) {
		boolean result = false;
		
		String txId = paymentEvent.getTxId();
		Order resultOrder = this.findOrderAndSetPaid(paymentEvent);
		try {
			this.orderService.payOrderAndCreatePublishOrderPaidEvent(txId, resultOrder);
			result = true;
		} catch(Exception e) {
			try {
				result = false;
				e.printStackTrace();
				this.orderService.createPublishOrderPayFailedEvent(txId, resultOrder);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Transactional
	public Order createOrderAndCreatePublishOrderCreatedEvent(Order order) throws Exception{
		Order resultOrder = this.createOrder(order);
		this.CreatePublishOrderEvent(null, resultOrder, OrderEventType.OrderCreated);
		return resultOrder;
	}
	
	@Transactional
	public void createPublishOrderCreateFailedEvent(Order order) throws Exception{
		this.CreatePublishOrderEvent(null, order, OrderEventType.OrderCreateFailed);
	}
	
	@Transactional
	public void cancelOrderAndCreatePublishOrderCanceledEvent(String txId, Order order) throws Exception{
		this.cancelOrder(order);
		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderCanceled);
	}
	
	@Transactional
	public void createPublishOrderCanceledEvent(String txId, Order order) throws Exception{
		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderCancelFailed);
	}
	
//	@Transactional
//	public void cancelOrderAndCreatePublishOrderCancelCuzProductAmountSubtractFailedEvent(String txId, Order order) throws Exception{
//		this.cancelOrder(order);
//		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderCanceledCuzProductAmountSubtractFailed);
//	}
//	
//	@Transactional
//	public void createPublishOrderCancelFailedCuzProductAmountSubtractFailedEvent(String txId, Order order) throws Exception{
//		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderCancelFailedCuzProductAmountSubtractFailed);
//	}
	
	@Transactional
	public void setOrderPaymentIdAndCreatePublishOrderPaymentIdSetEvent(String txId, Order order) throws Exception{
		this.setOrderPaymentId(order);
		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderPaymentIdSet);
	}
	
	@Transactional
	public void createPublishOrderPaymentIdSetFailedEvent(String txId, Order order) throws Exception{
		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderPaymentIdSetFailed);
	}
	
	@Transactional
	public void payOrderAndCreatePublishOrderPaidEvent(String txId, Order order) throws Exception{
		this.payOrder(order);
		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderPaid);
	}
	
	@Transactional
	public void createPublishOrderPayFailedEvent(String txId, Order order) throws Exception{
		this.CreatePublishOrderEvent(txId, order, OrderEventType.OrderPayFailed);
	}

	public Order createOrder(Order order) throws Exception {
		this.createOrderValidationCheack(order);
		long orderId = this.orderMapper.getOrderId();
		order.setId(orderId);
		order.getPaymentInfo().setOrderId(orderId);
		order.setPaid("unpaid");
		order.setStatus("ordered");
		this.orderMapper.createOrder(order);
		
		return order;
	}
	
	public void createOrderValidationCheack(Order order) throws Exception{}

	public boolean cancelOrder(Order order) throws Exception {
		this.cancelOrderValidationCheck(order);
		order.setStatus("canceled");
		return this.orderMapper.cancelOrder(order.getId());
	}
	
	public void cancelOrderValidationCheck(Order order) throws Exception{
		if(order.getStatus().isEmpty() || !order.getStatus().equals("ordered"))
			throw new Exception();
	}
	
	public void setOrderPaymentId(Order order) throws Exception {
		this.setOrderPaymentIdValidationCheck(order);
		this.orderMapper.setOrderPaymentId(order);
	}
	
	public void payOrder(Order order) throws Exception {
		this.payOrderValidationCheck(order);
		this.orderMapper.payOrder(order);
	}
	
	public void payOrderValidationCheck(Order order) throws Exception {
		//test용
//		throw new Exception();
		
		if(!"paid".equals(order.getPaid()))
			throw new Exception();
	}
	
	public Order findOrderAndSetPaymentId(PaymentEvent paymentEvent) {
		Order order = this.findOrderById(paymentEvent.getPayload().getOrderId());
		order.setPaymentId(paymentEvent.getPayload().getId());
		order.setPaymentInfo(this.convertPaymentEventToOrderPayment(paymentEvent));
		
		return order;
	}
	
	public Order findOrderAndSetPaid(PaymentEvent paymentEvent) {
		Order order = this.findOrderById(paymentEvent.getPayload().getOrderId());
		order.setPaid(paymentEvent.getPayload().getPaid());
		//Order is completed because there are no processes after payments
		order.setStatus("completed");
		order.getPaymentInfo().setPaid(paymentEvent.getPayload().getPaid());
		return order;
	}
	
	public OrderPayment convertPaymentEventToOrderPayment(PaymentEvent paymentEvent) {
		OrderPayment orderPayment = new OrderPayment();
		
		orderPayment.setId(paymentEvent.getPayload().getId());
		orderPayment.setAccountId(paymentEvent.getPayload().getAccountId());
		orderPayment.setOrderId(paymentEvent.getPayload().getOrderId());
		orderPayment.setPaymentMethod(paymentEvent.getPayload().getPaymentMethod());
		orderPayment.setPaymentDetail1(paymentEvent.getPayload().getPaymentDetail1());
		orderPayment.setPaymentDetail2(paymentEvent.getPayload().getPaymentDetail2());
		orderPayment.setPaymentDetail3(paymentEvent.getPayload().getPaymentDetail3());
		orderPayment.setPrice(paymentEvent.getPayload().getPrice());
		orderPayment.setPaid(paymentEvent.getPayload().getPaid());
		orderPayment.setActive(paymentEvent.getPayload().getActive());
		
		return orderPayment;
	}
	
	public void setOrderPaymentIdValidationCheck(Order order) throws Exception {
		if(order.getPaymentId() == 0)
			throw new Exception();
	}
	
	public List<OrderEvent> getOrderEvent(){
		return this.orderMapper.getOrderEvent();
	}
	
	public void CreatePublishOrderEvent(String txId, Order order, OrderEventType orderEventType) {
		OrderEvent orderEvent = this.orderService.convertOrderToOrderEvent(txId, order.getId(), orderEventType);
		this.createOrderEvent(orderEvent);
		this.publishOrderEvent(orderEvent);
	}
	
	public void publishOrderEvent(OrderEvent orderEvent) {
		this.orderPublish.send(orderEvent);
	}
	
	public void createOrderEvent(OrderEvent orderEvent) {
		this.orderMapper.createOrderEvent(orderEvent);
	}
	
	public OrderEvent convertOrderToOrderEvent(String txId, long id, OrderEventType orderEventType) {
		Log.info("in service txId : {}", txId);
		
		Order order = this.findOrderById(id);
		
		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setId(this.getOrderEventId());
		orderEvent.setDomain(domain);
		orderEvent.setOrderId(order.getId());
		orderEvent.setEventType(orderEventType);
		orderEvent.setPayload(new OrderPayload(order.getId(), order.getAccountId(), order.getPaymentId(), order.getAccountInfo(), order.getPaymentInfo(), order.getProductsInfo(), order.getPaid(), order.getStatus()));
		orderEvent.setTxId(txId);
		orderEvent.setCreatedAt(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		Log.info("in service orderEvent : {}", orderEvent.toString());
		
		return orderEvent;
	}
	
	public Order convertOrderEventToOrder(OrderEvent orderEvent) {
		Order order = new Order();
		
		order.setId(orderEvent.getOrderId());
		order.setAccountId(orderEvent.getPayload().getAccountId());
		order.setAccountInfo(orderEvent.getPayload().getAccountInfo());
		order.setPaymentId(orderEvent.getPayload().getPaymentId());
		order.setPaymentInfo(orderEvent.getPayload().getPaymentInfo());
		order.setProductsInfo(orderEvent.getPayload().getProductsInfo());
		order.setPaid(orderEvent.getPayload().getPaid());
		order.setStatus(orderEvent.getPayload().getStatus());
		
		return order;
	}
	
	public long getOrderEventId() {
		return this.orderMapper.getOrderEventId();
	}
	
	public Order findOrderById(long id) {
		return this.orderMapper.findOrderById(id);
	}
	
	public OrderEvent findOrderEventByTxId(String txId, String eventType) {
		return this.orderMapper.findOrderEventByTxId(txId, eventType);
	}
	
}