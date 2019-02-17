package Utils;


import java.sql.Connection;	
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class jdbcUtils {
	
	public static ComboPooledDataSource dataSource=new ComboPooledDataSource("Myapp");
	private static ThreadLocal<Connection> t=new ThreadLocal<Connection>();
	
	/**
	 *获取连接
	 * 若当前线程的connection，若不为null，说明已经调用了beginTransaction()方法
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		
		Connection conn=t.get();
		if(conn!=null){
			return conn;
		}
		return dataSource.getConnection();
	}
	
	/**
	 * 开启事务，包括获取连接和设置手动提交事务
	 * 获取当前线程的connection，若不为null则已经开启事务，抛出SQLException
	 * @throws SQLException
	 */
	public static void beginTransaction() throws SQLException{
		
		Connection conn=t.get();
		if(conn!=null){
			throw new SQLException("已开启事务");
		}
		conn=getConnection();
		conn.setAutoCommit(false);
		t.set(conn);
	}
	
	/**
	 * 提交事务并关闭线程
	 * 获取当前线程的connection，若为null，则未开启事务，抛出SQLException
	 * 若不为null则提交事务并关闭线程
	 * 由于connection虽然关闭了，但该对象依旧有值，需要从线程中消除
	 * @throws SQLException
	 */
	public static void commitTransaction() throws SQLException{
		
		Connection conn=t.get();
		if(conn==null){
			throw new SQLException("未开启事务");
		}
		conn.commit();
		conn.close();
		t.remove();
	}
	
	/**
	 * 回滚事务并关闭线程
	 * 获取当前线程的connection，若为null，则未开启事务，抛出SQLException
	 * 若不为null则回滚事务并关闭线程
	 * 由于connection虽然关闭了，但该对象依旧有值，需要从线程中消除
	 * @throws SQLException
	 */
	public static void rollbackTransaction() throws SQLException{
		Connection conn=t.get();
		if(conn==null){
			throw new SQLException("未开启事务");
		}
		conn.rollback();
		conn.close();
		t.remove();
	}
	
	/**
	 * 释放连接
	 * 获取当前线程的Connection，若为null在则参数连接肯定不是事务连接，可以关闭
	 * 若不为null，判断参数连接与线程连接是不是同一个，若是同一个则不能关闭，抛出SQLException，若不是则可以关闭
	 * @param conn
	 * @throws SQLException
	 */
	public static void releaseConnection(Connection conn) throws SQLException{
		Connection con=t.get();
		if(con==null){
			conn.close(); 
		}
		if(con==conn){
			throw new SQLException("事务锁，不能通过releaseConnection()方法关闭");
		}
		conn.close();
	}
}
