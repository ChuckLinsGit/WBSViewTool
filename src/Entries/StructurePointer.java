package Entries;
import java.util.Arrays;


public class StructurePointer {
	
	Integer number,days;
	String content,projectName;
	public Integer[] head =new Integer[20];
	public Integer[] next =new Integer[20];
	double earlistBegin,earlistEnd,latestBegin,latestEnd;
	boolean isCriticial;
	
	public StructurePointer() {
		super();
	}

	public StructurePointer(int number, int days, String content,
			String projectName, double earlistBegin, double earlistEnd,
			double latestBegin, double latestEnd, boolean isCriticial) {
		super();
		this.number = number;
		this.days = days;
		this.content = content;
		this.projectName = projectName;
		this.earlistBegin = earlistBegin;
		this.earlistEnd = earlistEnd;
		this.latestBegin = latestBegin;
		this.latestEnd = latestEnd;
		this.isCriticial = isCriticial;
	}
	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public double getEarlistBegin() {
		return earlistBegin;
	}

	public void setEarlistBegin(double earlistBegin) {
		this.earlistBegin = earlistBegin;
	}

	public double getEarlistEnd() {
		return earlistEnd;
	}

	public void setEarlistEnd(double earlistEnd) {
		this.earlistEnd = earlistEnd;
	}

	public double getLatestBegin() {
		return latestBegin;
	}

	public void setLatestBegin(double latestBegin) {
		this.latestBegin = latestBegin;
	}

	public double getLatestEnd() {
		return latestEnd;
	}

	public void setLatestEnd(double latestEnd) {
		this.latestEnd = latestEnd;
	}

	public boolean getIsCriticial() {
		return isCriticial;
	}

	public void setCriticial(boolean isCriticial) {
		this.isCriticial = isCriticial;
	}

	@Override
	public String toString() {
		return "StructurePointer [number=" + number + ", days=" + days
				+ ", content=" + content + ", projectName=" + projectName
				+ ", head=" + Arrays.toString(head) + ", next="
				+ Arrays.toString(next) + ", earlistBegin=" + earlistBegin
				+ ", earlistEnd=" + earlistEnd + ", latestBegin=" + latestBegin
				+ ", latestEnd=" + latestEnd + ", isCriticial=" + isCriticial
				+ "]";
	}
	
	
}
