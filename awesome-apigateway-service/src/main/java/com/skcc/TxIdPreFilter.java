package com.skcc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class TxIdPreFilter extends ZuulFilter{
	
	private static final Logger log = LoggerFactory.getLogger(TxIdPreFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		UUID uuid = UUID.randomUUID();
		String txId = String.format("%s-%s", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), uuid.toString());
		ctx.addZuulRequestHeader("X-TXID", txId);
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
	
}
