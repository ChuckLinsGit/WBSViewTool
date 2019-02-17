package DAO;

import java.sql.SQLException;	
import java.util.List;

public class Pre_SubNodeImpl extends BaseDAO<Object>{
	public List<Integer> getHeads(int pointerNumber) throws SQLException {
		String sql="SELECT headNumber FROM headerpointers "
						+ "	WHERE number="+pointerNumber;
		return getInteger(sql);
	}


	public List<Integer> getNexts(int pointerNumber) throws SQLException {
		String sql="SELECT nextNumber FROM nextpointers "
				+ "	WHERE number="+pointerNumber;
		return getInteger(sql);
	}

}
