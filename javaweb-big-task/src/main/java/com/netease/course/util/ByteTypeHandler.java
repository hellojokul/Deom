package com.netease.course.util;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ByteTypeHandler extends BaseTypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			String parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		try {
			if(rs.getBytes(columnName)!=null) {
				return new String(rs.getBytes(columnName),"UTF-8");
			} else {
				return "";
			}
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		try {
			if(rs.getBytes(columnIndex)!=null) {
				return new String(rs.getBytes(columnIndex),"UTF-8");
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		try {
			if(cs.getBytes(columnIndex)!=null) {
				return new String(cs.getBytes(columnIndex),"UTF-8");
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

}
