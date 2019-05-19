package com.skcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class JsApigatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsApigatewayServiceApplication.class, args);
	}
}
