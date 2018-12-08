package com.min.charge.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class AutoEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    @SuppressWarnings("rawtypes")
	private BaseTypeHandler typeHandler = null;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public AutoEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        if(BaseEnum.class.isAssignableFrom(type)){
            // 如果实现了 BaseCodeEnum 则使用我们自定义的转换器
            typeHandler = new CodeEnumTypeHandler(type);
        }else {
            // 默认转换器 也可换成 EnumOrdinalTypeHandler
            typeHandler = new EnumTypeHandler<>(type);
        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter,
			JdbcType jdbcType) throws SQLException {
		 typeHandler.setNonNullParameter(ps,i, parameter,jdbcType);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		 return (E) typeHandler.getNullableResult(rs,columnName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		 return (E) typeHandler.getNullableResult(rs,columnIndex);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		 return (E) typeHandler.getNullableResult(cs,columnIndex);
	}

    
}
