package DAO;

import java.lang.reflect.ParameterizedType;	


import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import Utils.jdbcUtils;



public class BaseDAO<T> {
	
	private QueryRunner queryRunner=new QueryRunner();
	
	private Class<T> clazz;
	
	
	public BaseDAO() {
		Type superClass=getClass().getGenericSuperclass();
		System.out.print("DAOparameterType:"+superClass+" ");
		if(superClass instanceof ParameterizedType){
			ParameterizedType parameterizedType=(ParameterizedType) superClass;
			
			Type[] typeArgs=parameterizedType.getActualTypeArguments();
			if(typeArgs!=null&&typeArgs.length>0){
				if(typeArgs[0] instanceof Class){
					clazz=(Class<T>)typeArgs[0];
					System.out.println(clazz);
				} 
			}
		}
	}
	
	/* ���·�����װ��ѯ�ĸ��ַ���*/
	public <E> E getForVlaue(String sql,Object...args){
		Connection connection=null;
		
		try{
			connection=jdbcUtils.getConnection();
			return (E) queryRunner.query(connection, sql,new ScalarHandler(),args);
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				jdbcUtils.releaseConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<T> getForList(String sql,Object...args){
		
		Connection connection=null;
		
		try{
			connection=jdbcUtils.getConnection();
			return queryRunner.query(connection, sql,new BeanListHandler<T>(clazz),args);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				jdbcUtils.releaseConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public T get(String sql,Object...args){
		Connection connection=null;
		
		try{
			connection=jdbcUtils.getConnection();
			return  queryRunner.query(connection, sql,new BeanHandler<>(clazz),args);
		}catch(Exception e){
			e.printStackTrace();
			return null;	
		}finally{
			try {
				jdbcUtils.releaseConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * �����ݿ��ò�ѯ�õ��Ķ����ֵ����Ž�IntegerList��
	 * ��������⣺�޷�ʹ��QueryRunner��ѯԭʼ�������͵����ݣ���Ϊ�е���������û���޲ι��캯�����޷���װ
	 * @param sql
	 * @param number
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getInteger(String sql){
		Connection connection=null;
		List<Integer> IntegerList=new ArrayList<Integer>();
		try{
			connection=jdbcUtils.getConnection();//��ȡ���ӳص�����
			PreparedStatement prepareStatement = connection.prepareStatement(sql);//ʹ��PreparedStatement��ǰ����SQL��䣬����ǰ����쳣�����Ч��
			ResultSet executeQueryRs = prepareStatement.executeQuery();//ִ�в�ѯ���
		
			//����ResultSet����ӽ�IntegerList
			while(executeQueryRs.next()){
				int colVal = executeQueryRs.getInt(1);
				IntegerList.add(colVal);
			}
			return IntegerList;
		}catch(Exception e){
			e.printStackTrace();
			return null;	
		}finally{
			try {
				jdbcUtils.releaseConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��װ��ɾ�ķ���
	 * @param sql
	 * @param args
	 * @throws SQLException 
	 */
	public Integer update(String sql,Object...args){
		Connection connection=null;
		try{
			connection=jdbcUtils.getConnection();
			return(queryRunner.update(connection,sql,args));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				jdbcUtils.releaseConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
