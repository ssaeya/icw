package com.skcc.order.event.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrderOutputChannel {
	String OrderOutput = "OrderOutput";
	
	@Output(OrderOutputChannel.OrderOutput)
	MessageChannel getMessageChannel();
}
