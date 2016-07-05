package com.netease.course.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class StringTypeHandler extends BaseTypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			String parameter, JdbcType jdbcType) throws SQLException {
		if(parameter==null) {
			ps.setString(i, "");
		} else {
			ps.setString(i, ConvertStringUtils.removePoint(parameter));
		}
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		if(rs.getString(columnName)==null) {
			return "";
		} else {
			return ConvertStringUtils.addPoint(rs.getString(columnName));
		}
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		if(rs.getString(columnIndex)==null) {
			return "";
		} else {
			return ConvertStringUtils.addPoint(rs.getString(columnIndex));
		}
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		if(cs.getString(columnIndex)==null) {
			return "";
		} else {
			return ConvertStringUtils.addPoint(cs.getString(columnIndex));
		}
	}
}
