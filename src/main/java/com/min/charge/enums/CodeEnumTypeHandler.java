package com.min.charge.enums;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class CodeEnumTypeHandler<E extends Enum<?> & BaseEnum> extends BaseTypeHandler<BaseEnum> {

    private Class<E> type;

    public CodeEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

	@Override
	public void setNonNullParameter(java.sql.PreparedStatement ps, int i,
			BaseEnum parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getValue());
	}

	@Override
	public BaseEnum getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		
		int code = rs.getInt(columnName);
        return rs.wasNull() ? null : codeOf(code);
	}

	@Override
	public BaseEnum getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		int code = rs.getInt(columnIndex);
        return rs.wasNull() ? null : codeOf(code);
	}

	@Override
	public BaseEnum getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		int code = cs.getInt(columnIndex);
        return cs.wasNull() ? null : codeOf(code);
	}

	private E codeOf(int code){
        try {
            return BaseEnumUtil.codeOf(type, code);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + code + " to " + type.getSimpleName() + " by code value.", ex);
        }
    }
}
