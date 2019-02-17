package DAO;

import java.sql.SQLException;		
import java.util.List;	

import Entries.ProjectStructure;
import Entries.StructurePointer;

public class structurePointerImpl extends BaseDAO<StructurePointer>  {
		 
		public Integer addStructurePointer(StructurePointer sp) throws SQLException {
			String sql="INSERT INTO structurepointer"
						+ " VALUES (?,?,?,?,?,?,?,?,?)";
			return (update(sql,sp.getNumber(),sp.getDays(),sp.getProjectName(),
								sp.getContent(),sp.getEarlistBegin(),sp.getEarlistEnd(),
										sp.getLatestBegin(),sp.getLatestEnd(),sp.getIsCriticial()));
		}

		public Integer deleteStructurePointerByProjectName(String projectName) throws SQLException {
			String sql="DELETE FROM structurepointer "
						+ "WHERE projectName=?";
			return (update(sql,projectName));
		}

		
		public Integer updateStructurePointer(StructurePointer sp) throws SQLException {
			String sql="UPDATE structurepointer "
					 + "SET days=?,content=?,earlistBegin=?,"
					 	 + "earlistEnd=?,latestBegin=?,latestEnd=?,isCriticial=? WHERE number=?";
			return (update(sql,sp.getDays(),sp.getContent(),sp.getEarlistBegin(),
					sp.getEarlistEnd(),sp.getLatestBegin(),sp.getLatestEnd(),
					sp.getIsCriticial(),sp.getNumber()));
		}

		public List<StructurePointer> getAllPointersByProjectName(String projectName) throws SQLException {
			String sql="SELECT * FROM structurepointer "
							+ "	WHERE projectName=?";
			return getForList(sql, projectName);
		}

	}
