package com.skcc.order.subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.skcc.order.event.channel.OrderInputChannel;
import com.skcc.order.service.OrderService;
import com.skcc.payment.event.message.PaymentEvent;
import com.skcc.product.event.message.ProductEvent;

@Component
@EnableBinding(OrderInputChannel.class)
public class OrderSubscribe {

	private OrderService orderService;
	
	@Autowired
	public OrderSubscribe(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@StreamListener(OrderInputChannel.productAmountSubtractFailed)
	public void receiveProductAmountSubtractFailedEvent(ProductEvent productEvent) {
		this.orderService.cancelOrderAndCreatePublishOrderEvent(productEvent);
	}
	
	@StreamListener(OrderInputChannel.paymentCreateFailed)
	public void receivePaymentCreateFailedEvent(PaymentEvent paymentEvent) {
		this.orderService.cancelOrderAndCreatePublishOrderEvent(paymentEvent);
	}

	@StreamListener(OrderInputChannel.paymentPaid)
	public void receivePaymentPaidEvent(PaymentEvent paymentEvent) {
//		throw new Exception();
		this.orderService.payOrderAndCreatePublishOrderEvent(paymentEvent);
	}
	
	@StreamListener(OrderInputChannel.paymentCreated)
	public void receivePaymentCreatedEvent(PaymentEvent paymentEvent) {
		this.orderService.setOrderPaymentIdAndCreatePublishOrderEvent(paymentEvent);
	}
}
