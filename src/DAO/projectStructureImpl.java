package DAO;

import java.sql.SQLException;	
import java.util.List;	

import Entries.ProjectStructure;
import Entries.StructurePointer;



public class projectStructureImpl extends BaseDAO<ProjectStructure>  {
	
	public ProjectStructure getProjectStructureByProjectName(String projectName) throws SQLException {
		String sql="SELECT * FROM projectstructure "
				+ "WHERE projectName=?";
		return get(sql,projectName);
	}

	
	public  Integer addProjectStructure(ProjectStructure ps) throws SQLException {
		String sql="INSERT INTO projectstructure "
					+ "	VALUES (?,?,?,?)";
		return (update(sql,ps.getProjectName(),ps.getProjectBeginDate(),ps.getProjectEndDate(),
						ps.getProjectPointerQty()));
	}

	public Integer deleteProjectStructureByProjectName(String projectName) throws SQLException {
		String sql="DELETE FROM projectstructure "
						+ "WHERE projectName=?";
		return (update(sql,projectName));
	}

	public Integer updateProjectStructure(ProjectStructure ps) throws SQLException {
		String sql="UPDATE projectstructure "
					+ "SET projectBeginDate=?,projectEndDate=?,"
				         + "projectPointerQty=?  WHERE projectName=?";
		return (update(sql,ps.getProjectBeginDate(),ps.getProjectEndDate(),
					ps.getProjectPointerQty(),ps.getProjectName()));
	}

}
