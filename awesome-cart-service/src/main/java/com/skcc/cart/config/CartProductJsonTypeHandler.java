package com.skcc.cart.config;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.cart.domain.CartProduct;

public class CartProductJsonTypeHandler extends BaseTypeHandler<Object>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
			throws SQLException {
		
		String Json = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Json = objectMapper.writeValueAsString(parameter);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		ps.setObject(i, Json);

	}

	@Override
	public CartProduct getNullableResult(ResultSet rs, String columnName) throws SQLException {
		
		Clob d = (Clob) rs.getObject(columnName);
		if(d == null) return null;
		
		CartProduct cp = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			cp = objectMapper.readValue(d.getSubString(1, (int) d.length()), CartProduct.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cp;
//		Object d = rs.getObject(columnName);
//		if(d == null) return null;
//		
//		List<CartProduct> cps = null;
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			cps = objectMapper.readValue(d.toString(), new TypeReference<List<CartProduct>>() {});
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return cps;
		
	}

	@Override
	public CartProduct getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

		Object d = rs.getObject(columnIndex);
		if(d == null) return null;

		CartProduct cp = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			cp = objectMapper.readValue(d.toString(), CartProduct.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cp;
		
	}

	@Override
	public CartProduct getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

		Object d =cs.getObject(columnIndex);
		if(d == null) return null;

		CartProduct cp = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			cp = objectMapper.readValue(d.toString(), CartProduct.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cp;
		
	}

}
