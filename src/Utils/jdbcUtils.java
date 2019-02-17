package Utils;


import java.sql.Connection;	
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class jdbcUtils {
	
	public static ComboPooledDataSource dataSource=new ComboPooledDataSource("Myapp");
	private static ThreadLocal<Connection> t=new ThreadLocal<Connection>();
	
	/**
	 *��ȡ����
	 * ����ǰ�̵߳�connection������Ϊnull��˵���Ѿ�������beginTransaction()����
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
	 * �������񣬰�����ȡ���Ӻ������ֶ��ύ����
	 * ��ȡ��ǰ�̵߳�connection������Ϊnull���Ѿ����������׳�SQLException
	 * @throws SQLException
	 */
	public static void beginTransaction() throws SQLException{
		
		Connection conn=t.get();
		if(conn!=null){
			throw new SQLException("�ѿ�������");
		}
		conn=getConnection();
		conn.setAutoCommit(false);
		t.set(conn);
	}
	
	/**
	 * �ύ���񲢹ر��߳�
	 * ��ȡ��ǰ�̵߳�connection����Ϊnull����δ���������׳�SQLException
	 * ����Ϊnull���ύ���񲢹ر��߳�
	 * ����connection��Ȼ�ر��ˣ����ö���������ֵ����Ҫ���߳�������
	 * @throws SQLException
	 */
	public static void commitTransaction() throws SQLException{
		
		Connection conn=t.get();
		if(conn==null){
			throw new SQLException("δ��������");
		}
		conn.commit();
		conn.close();
		t.remove();
	}
	
	/**
	 * �ع����񲢹ر��߳�
	 * ��ȡ��ǰ�̵߳�connection����Ϊnull����δ���������׳�SQLException
	 * ����Ϊnull��ع����񲢹ر��߳�
	 * ����connection��Ȼ�ر��ˣ����ö���������ֵ����Ҫ���߳�������
	 * @throws SQLException
	 */
	public static void rollbackTransaction() throws SQLException{
		Connection conn=t.get();
		if(conn==null){
			throw new SQLException("δ��������");
		}
		conn.rollback();
		conn.close();
		t.remove();
	}
	
	/**
	 * �ͷ�����
	 * ��ȡ��ǰ�̵߳�Connection����Ϊnull����������ӿ϶������������ӣ����Թر�
	 * ����Ϊnull���жϲ����������߳������ǲ���ͬһ��������ͬһ�����ܹرգ��׳�SQLException������������Թر�
	 * @param conn
	 * @throws SQLException
	 */
	public static void releaseConnection(Connection conn) throws SQLException{
		Connection con=t.get();
		if(con==null){
			conn.close(); 
		}
		if(con==conn){
			throw new SQLException("������������ͨ��releaseConnection()�����ر�");
		}
		conn.close();
	}
}
