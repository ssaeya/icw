package com.skcc.order.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.skcc.order.event.channel.OrderOutputChannel;
import com.skcc.order.event.message.OrderEvent;

@Component
@EnableBinding(OrderOutputChannel.class)
public class OrderPublish {
	
	private OrderOutputChannel orderOutputChannel;
	
	@Value("${domain.name}")
	private String domain;
	
	@Autowired
	public OrderPublish(OrderOutputChannel orderOutputChannel) {
		this.orderOutputChannel = orderOutputChannel;
	}
	
	public void send(OrderEvent orderEvent) {
		this.orderOutputChannel.getMessageChannel().send(MessageBuilder.withPayload(orderEvent).setHeader("routeTo", domain + "." + orderEvent.getEventType()).build());
	}
	
}
