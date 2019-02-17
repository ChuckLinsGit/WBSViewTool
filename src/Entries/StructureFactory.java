package Entries;

import java.sql.SQLException;	
import java.util.List;

import DAO.Pre_SubNodeImpl;
import DAO.projectStructureImpl;
import DAO.structurePointerImpl;

/**
 * ���������ڳ�ʼ��ProjectStructure(��Ŀ�����)
 * ��������ڵ��������ʱ��
 * ���ͨ���Ƚϱ�ʶ�ؼ�·��
 * @author Chuck Lin
 *
 */
public class StructureFactory {
	ProjectStructure ps;
	structurePointerImpl structurePointerImpl=new structurePointerImpl();
	projectStructureImpl projectStructureImpl=new projectStructureImpl();
	Pre_SubNodeImpl pre_SubNodeImpl=new Pre_SubNodeImpl();
	
	/**
	 * ��ʼ���������ǳ�ʼ��ProjectStructure�������Ƹ��ڵ�
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
	 * ����ProjectStructure�Ľڵ����鲢�õ��ڵ���Ŀ��
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
	 * ���Ƹ��ڵ�
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
			System.out.println("���ƵĽڵ�-"+sp.number+": "+sp);
			System.out.println("����Ӱ��������� "+structurePointerImpl.updateStructurePointer(sp));//���½ڵ���Ϣ�����ݿ���
			//�Ƚϸ��ڵ��������ʱ�䣬�õ���Ŀ���������ʱ��
			if (earlistTime<sp.earlistEnd)
				earlistTime=sp.earlistEnd;
		}
		this.ps.projectEndDate=earlistTime;
	}
	
	/**���ýڵ��ǰ�ڵ��������
	 * 
	 * @param sp
	 * @throws SQLException 
	 */
	void setHeads(StructurePointer sp) throws SQLException{
		List<Integer> headTemp=pre_SubNodeImpl.getHeads(sp.getNumber());//�����ݿ��л��ǰ�ڵ��������
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
	
	/**���ýڵ�ĺ�ڵ��������
	 * 
	 * @param sp
	 * @throws SQLException 
	 */
	 void setNexts(StructurePointer sp) throws SQLException{
		 List<Integer> nextTemp=pre_SubNodeImpl.getNexts(sp.getNumber());//�����ݿ��л�ú�ڵ��������	
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
	  * ��������ʱ��,�����ظ��¼����������ʱ�����ڼ�����Ŀ������ʱ��
	  * @param sp
	  */
	 void setEarlistTime(StructurePointer sp){
		double earlistBegin=0;
		if(sp.number!=1){//��Ϊ�׸��ڵ�ʱ�����翪ʼʱ��=(Max)heads.earlistEnd
			Integer[] heads=sp.head;
			for(Integer head:heads){//����ProjectStructure�е�ÿ��ǰ�ڵ㲢�Ƚ�earlistEnd
				if (head!=null){
					if(earlistBegin<ps.pointers[head-1].earlistEnd){
						earlistBegin=ps.pointers[head-1].earlistEnd;
					}
				}else break;
			}
		}
		sp.earlistBegin=earlistBegin;
		sp.earlistEnd=earlistBegin+sp.days;//�������ʱ��=���翪ʱ��+����ʱ��
		ps.projectEndDate=earlistBegin+sp.days;
	}
	
	/**
	 * �������ʱ��
	 * @param sp
	 */
	void setLatestTime(StructurePointer sp){
		double latestEnd=ps.projectEndDate;
		if(sp.number!=ps.projectPointerQty){//����������һ���ڵ㣬����ٽ���ʱ��=(Min)nexts.latestBegin
			Integer[] nexts=sp.next;
			for(Integer next:nexts){//����ProjectStructure�е�ÿ����ڵ㲢�Ƚ�latestBegin
				if(next!=null){
					if(latestEnd>ps.pointers[next-1].latestBegin){
						latestEnd=ps.pointers[next-1].latestBegin;
					}
				}
			}
		}
		sp.latestEnd=latestEnd;
		sp.latestBegin=latestEnd-sp.days;//��ٿ�ʼʱ��=��ٽ���ʱ��-����ʱ��
	}
	
	
	/**
	 * �жϲ������Ƿ�Ϊ�ؼ�·��
	 * @param sp
	 */
	void setIsCriticial(StructurePointer sp){//������翪ʼʱ��=��ٿ�ʼʱ�����������ʱ��=��ٽ���ʱ����Ϊ�ؼ�·��
		if((sp.earlistBegin==sp.latestBegin)&&(sp.earlistEnd==sp.latestEnd)){
			sp.isCriticial=true;
		}
	}

}
