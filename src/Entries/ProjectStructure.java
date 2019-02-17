package Entries;
import java.sql.Date;
import java.util.Arrays;


public class ProjectStructure{

	String projectName;
	double projectBeginDate;
	double projectEndDate;
	Integer projectPointerQty;
	
	public StructurePointer[] pointers=new StructurePointer[200];
	
	
	public ProjectStructure() {
		super();
	}

	public ProjectStructure(String projectName, double projectBeginDate,
			double projectEndDate, Integer projectPointerQty) {
		super();
		this.projectName = projectName;
		this.projectBeginDate = projectBeginDate;
		this.projectEndDate = projectEndDate;
		this.projectPointerQty = projectPointerQty;
	}





	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public double getProjectBeginDate() {
		return projectBeginDate;
	}


	public void setProjectBeginDate(double projectBeginDate) {
		this.projectBeginDate = projectBeginDate;
	}


	public double getProjectEndDate() {
		return projectEndDate;
	}


	public void setProjectEndDate(double projectEndDate) {
		this.projectEndDate = projectEndDate;
	}


	public Integer getProjectPointerQty() {
		return projectPointerQty;
	}


	public void setProjectPointerQty(Integer projectPointerQty) {
		this.projectPointerQty = projectPointerQty;
	}


	@Override
	public String toString() {
		return "ProjectStructure [projectName=" + projectName
				+ ", projectBeginDate=" + projectBeginDate
				+ ", projectEndDate=" + projectEndDate + ", projectPointerQty="
				+ projectPointerQty + ", pointers=" + Arrays.toString(pointers)
				+ "]";
	}
}
