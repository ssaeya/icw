package com.skcc.order.event.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface OrderInputChannel {
	
	String productAmountSubtractFailed = "productAmountSubtractFailed";
	String paymentCreateFailed = "paymentCreateFailed";
	String paymentCreated = "paymentCreated";
	String paymentPaid = "paymentPaid";
	
	@Input(OrderInputChannel.productAmountSubtractFailed)
	SubscribableChannel productAmountSubtractFailed();
	
	@Input(OrderInputChannel.paymentCreateFailed)
	SubscribableChannel paymentCreateFailed();
	
	@Input(OrderInputChannel.paymentPaid)
	SubscribableChannel paymentPaid();
	
	@Input(OrderInputChannel.paymentCreated)
	SubscribableChannel paymentCreated();
	
}
