package Entries;

import java.sql.SQLException;	
import java.util.List;

import DAO.Pre_SubNodeImpl;
import DAO.projectStructureImpl;
import DAO.structurePointerImpl;

/**
 * 工厂类用于初始化ProjectStructure(项目类对象)
 * 并计算各节点最迟最早时间
 * 最后通过比较标识关键路径
 * @author Chuck Lin
 *
 */
public class StructureFactory {
	ProjectStructure ps;
	structurePointerImpl structurePointerImpl=new structurePointerImpl();
	projectStructureImpl projectStructureImpl=new projectStructureImpl();
	Pre_SubNodeImpl pre_SubNodeImpl=new Pre_SubNodeImpl();
	
	/**
	 * 初始化工厂即是初始化ProjectStructure，并完善各节点
	 * @param ps
	 * @throws SQLException 
	 */
	public StructureFactory(ProjectStructure ps) {
		this.ps=ps;
		try {
			setProjectPointersArray();
			fillProjectPointers();
			System.out.println(ps);
			projectStructureImpl.updateProjectStructure(ps);
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	/**
	 * 设置ProjectStructure的节点数组并得到节点数目；
	 * @throws SQLException 
	 */
	void setProjectPointersArray() throws SQLException{
		List<StructurePointer> sps=structurePointerImpl.getAllPointersByProjectName(this.ps.projectName);
		int i=0;
		for(StructurePointer ps:sps){
			this.ps.pointers[i]=ps;
			i++;
		}
		this.ps.projectPointerQty=i;
	}
	
	/**
	 * 完善各节点
	 * @throws SQLException 
	 */
	void fillProjectPointers() throws SQLException{
		
		System.out.println("Set pointers' head&next and earlistTime");
		for(StructurePointer sp:ps.pointers){
			if(sp!=null){
				setHeads(sp);
				setNexts(sp);
				setEarlistTime(sp);
			}
		}
		
		System.out.println("Set pointers' lastistTime");
		double earlistTime=0;
		for(int i=ps.projectPointerQty;i>0;i--){
			StructurePointer sp = ps.pointers[i-1];
			setLatestTime(sp);
			setIsCriticial(sp);
			System.out.println("完善的节点-"+sp.number+": "+sp);
			System.out.println("更新影响的行数： "+structurePointerImpl.updateStructurePointer(sp));//更新节点信息到数据库中
			//比较各节点最早结束时间，得到项目的最早结束时间
			if (earlistTime<sp.earlistEnd)
				earlistTime=sp.earlistEnd;
		}
		this.ps.projectEndDate=earlistTime;
	}
	
	/**设置节点的前节点序号数组
	 * 
	 * @param sp
	 * @throws SQLException 
	 */
	void setHeads(StructurePointer sp) throws SQLException{
		List<Integer> headTemp=pre_SubNodeImpl.getHeads(sp.getNumber());//从数据库中获得前节点序号数组
		int i=0;
		if(headTemp!=null){
			for(Integer head:headTemp){
				if(head!=null){
				sp.head[i]=head;
				i++;
				}
			}
		}
	}
	
	/**设置节点的后节点序号数组
	 * 
	 * @param sp
	 * @throws SQLException 
	 */
	 void setNexts(StructurePointer sp) throws SQLException{
		 List<Integer> nextTemp=pre_SubNodeImpl.getNexts(sp.getNumber());//从数据库中获得后节点序号数组	
		 int i=0;
			if(nextTemp!=null){
				for(Integer next:nextTemp){
					if(next!=null){
					sp.next[i]= next;
					i++;
					}
					else break; 
				}
			}
	}
	
	 /**
	  * 设置最早时间,并返回该事件的再造结束时间用于计算项目的总体时间
	  * @param sp
	  */
	 void setEarlistTime(StructurePointer sp){
		double earlistBegin=0;
		if(sp.number!=1){//不为首个节点时，最早开始时间=(Max)heads.earlistEnd
			Integer[] heads=sp.head;
			for(Integer head:heads){//遍历ProjectStructure中的每个前节点并比较earlistEnd
				if (head!=null){
					if(earlistBegin<ps.pointers[head-1].earlistEnd){
						earlistBegin=ps.pointers[head-1].earlistEnd;
					}
				}else break;
			}
		}
		sp.earlistBegin=earlistBegin;
		sp.earlistEnd=earlistBegin+sp.days;//最早结束时间=最早开时间+所需时间
		ps.projectEndDate=earlistBegin+sp.days;
	}
	
	/**
	 * 设置最迟时间
	 * @param sp
	 */
	void setLatestTime(StructurePointer sp){
		double latestEnd=ps.projectEndDate;
		if(sp.number!=ps.projectPointerQty){//如果不是最后一个节点，则最迟结束时间=(Min)nexts.latestBegin
			Integer[] nexts=sp.next;
			for(Integer next:nexts){//遍历ProjectStructure中的每个后节点并比较latestBegin
				if(next!=null){
					if(latestEnd>ps.pointers[next-1].latestBegin){
						latestEnd=ps.pointers[next-1].latestBegin;
					}
				}
			}
		}
		sp.latestEnd=latestEnd;
		sp.latestBegin=latestEnd-sp.days;//最迟开始时间=最迟结束时间-所需时间
	}
	
	
	/**
	 * 判断并设置是否为关键路径
	 * @param sp
	 */
	void setIsCriticial(StructurePointer sp){//如果最早开始时间=最迟开始时间且最早结束时间=最迟结束时间则为关键路径
		if((sp.earlistBegin==sp.latestBegin)&&(sp.earlistEnd==sp.latestEnd)){
			sp.isCriticial=true;
		}
	}

}
