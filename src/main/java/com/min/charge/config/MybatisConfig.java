package com.min.charge.config;

import com.min.charge.enums.AutoEnumTypeHandler;
import com.min.charge.enums.OrderStatusEnum;
import com.min.charge.enums.TradeStatusEnum;
import com.min.charge.enums.TradeTypeEnum;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class MybatisConfig {
	
	private static final Logger logger = Logger.getLogger(MybatisConfig.class);
	private static SqlSessionFactory factory;
	private static final ThreadLocal<SqlSession> tl = new ThreadLocal<>();

	static {
		
		InputStream is = null;
		
		try{
			is = MybatisConfig.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
			MybatisConfig.factory = new SqlSessionFactoryBuilder().build(is);
			 // 取得类型转换注册器
	        TypeHandlerRegistry typeHandlerRegistry = MybatisConfig.factory .getConfiguration().getTypeHandlerRegistry();
	        // 注册默认枚举转换器
	        typeHandlerRegistry.register(OrderStatusEnum.class, AutoEnumTypeHandler.class);
	        typeHandlerRegistry.register(TradeStatusEnum.class, AutoEnumTypeHandler.class);
	        typeHandlerRegistry.register(TradeTypeEnum.class, AutoEnumTypeHandler.class);
	        
			System.out.println("数据库加载完成：" + MybatisConfig.factory);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}finally{
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}


	
	/**
	 * 得到与当前线程绑定的 SqlSession对象
	 * 如果当前线程已经绑定了SqlSession对象，则返回该对象，否则打开新的SqlSession对象，绑定到当前线程，并返回
	 * @return SqlSession 对象
	 */
	public static SqlSession getCurrent(){
		SqlSession result = tl.get();
		if(result == null){
			result = factory.openSession();
			tl.set(result);
		}
		return result;
	}
	
	/**
	 * 关闭与当前线程绑定的 SqlSession 对象
	 */
	public static void closeCurrent(){
		SqlSession session = tl.get();
		if(session != null){
			try{
				session.close();
				tl.remove();
			}catch(Exception ex){
				logger.error(ex.getMessage(),ex);
			}
		}
	}
	
	/**
	 * 提交当前线程事务
	 */
	public static void commitCurrent(){
		getCurrent().commit();
	}
	
	/**
	 * 回滚当前线程事务
	 */
	public static void rollbackCurrent(){
		getCurrent().rollback();
	}
	
	/**
	 * 得到一个新打开的SqlSession对象
	 * @return 新打开的SqlSession对象
	 */
	public static SqlSession openNewSession(){
		return factory.openSession();
	}
	
}
