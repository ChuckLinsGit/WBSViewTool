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
	
	/* 以下方法封装查询的各种方法*/
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
	 * 将数据库获得查询得到的多个数值处理放进IntegerList中
	 * 解决的问题：无法使用QueryRunner查询原始数据类型的数据，因为有的数据类型没有无参构造函数，无法封装
	 * @param sql
	 * @param number
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getInteger(String sql){
		Connection connection=null;
		List<Integer> IntegerList=new ArrayList<Integer>();
		try{
			connection=jdbcUtils.getConnection();//获取连接池的链接
			PreparedStatement prepareStatement = connection.prepareStatement(sql);//使用PreparedStatement提前编译SQL语句，能提前坚持异常，提高效率
			ResultSet executeQueryRs = prepareStatement.executeQuery();//执行查询语句
		
			//遍历ResultSet并添加进IntegerList
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
	 * 封装增删改方法
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
