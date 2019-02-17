package Paint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.sql.SQLException;

import javax.swing.JPanel;

import Entries.ProjectStructure;
import Entries.StructureFactory;
import Entries.StructurePointer;

/** 
 * ����һ���̳���JPanel���࣬��д����paint���� * 
 */  
public class MyPanel extends JPanel  
{  
      
    private Image image;    //ͼ�񻺳�  
    private Graphics2D og;  
    private int griphicFontSize=20; 
  
    //�����һ�����ε�������к�
    private int x = 0;  
    private int y =5;
    
    //������εĳ�����ಢ�Ʋ��γ���������кŵĳ���
    private int width=70;
    private int height=100;
    private int widthspace=40;
    private int heightspace=10;
    private int rowCompany=width+widthspace;
    private int columnCompany=height+heightspace;
    
    private StructureFactory sf;//��Ŀ�ڵ㹤��
    private ProjectStructure ps;//��Ŀ�ڵ�
    int pointerQty;//�ڵ�����
    int[] pointerStateArray;//�ڵ�״̬����ʾ�Ƿ��ѻ�
    Point[] pointerCoordinate;//��¼ÿ���ڵ����ʼλ��
    
    public MyPanel(String projectName){
    	//�õ���Ŀ����ڵ���Ϣ
    	ps=new ProjectStructure();
    	ps.setProjectName(projectName);
		sf=new StructureFactory(ps);
    	//��ʼ���ڵ�״̬����
    	pointerQty=ps.getProjectPointerQty();
    	//System.out.println(pointerQty);
    	//System.out.println(ps);
    	pointerCoordinate=new Point[pointerQty];
    	pointerStateArray=new int[pointerQty];
        for(int i=0;i<pointerQty;i++){
        	pointerStateArray[i]=0;
        }
        this.setLayout(null);
        
    }
    
    /**
     * ������
     * ����ǵ�һ���ڵ㣬ֱ�ӻ�
     * ������м�ڵ㣬�жϸýڵ��ǰ�ڵ������еĽڵ��Ƿ��ѳ��֣����ǰ�������λ��������������򲻻�
     * ��������������pointers�����е���Ŵ�С����������һ���ڵ��λ�ã����кţ�����ౣ��һֱ��
     *      ����д�����������Ϣ�����ݾ��������������ͷ����״̬�����н�������Ϊ�ѻ�
     */
    public void display()  
    {
    	//System.out.println("enter function��display()");
    	//��ͼ׼��
    	if(og == null)  
    	{  
    		//JPanel�̳���Component�࣬����ʹ�����ķ���createImage����һ����JPanel��С��ͬ��ͼ�λ���  
    		//Ȼ������Image�ӿڵķ�����û�ͼ����  
    		image = this.createImage(this.getWidth(),this.getHeight());  
    		if(image != null)og = (Graphics2D) image.getGraphics();  
    		Font font=new Font("����",Font.ITALIC,griphicFontSize);
    		og.setFont(font);
    		//System.out.println("function display() base set have been done");
    	} 
    	

    	//ѭ�������ڵ�״̬����֪�����һ���ڵ㱻����
    	if(pointerQty!=0){
    		int row=x;//����
    		int colmun=y;//����
    		//System.out.println("enter display circul");
    		for(int i=0;i<pointerQty;i++){
    			System.out.println("�ڵ�"+(i+1)+"��ͼ");
            	if(pointerStateArray[i]==0){
            		//����һ���ڵ�
            		if(i==0){
            			//System.out.println("paint the first node");
            			System.out.println("���к�:("+row+","+colmun+")");
            			pointerCoordinate[0]=new Point(row,colmun);
            			drawContent(0);
            		}
            		//�����������Ľڵ�
            		else if(i>0 ){
            			//System.out.println("paint the other nodes");
            			StructurePointer p=ps.pointers[i];
            			//System.out.println(p);
            			int lenthofheader=p.head.length;
         
            			int maxHead=0;
            			boolean ifCanPaint=true;
            			for(int j=0;j<lenthofheader;j++){
            				if(p.head[j]==null) break;
        					int head=p.head[j];
        					
        					//�ҵ��ýڵ�����ǰ�ýڵ�
        					if(head>maxHead) maxHead=head;
        					
        					//�������жϸýڵ��ܲ��ܻ�:�ýڵ������ǰ�ýڵ�����ѻ�
        					if(pointerStateArray[head-1]==0){
        						ifCanPaint=false;
//�˴������׳�����ǰ�ýڵ�δ��ֻ������Ϊ���ڵ�����С����δ����ǰ�ýڵ㣬���ڵ���ǰ�ýڵ�δ��������Ŀ�������Ŀǰ�����Դ���
        						System.out.println("�ڵ�"+i+"�����������ڵ���ǰ�ýڵ�δ��");
        						break;
        					}
            			}
            			System.out.println("���ǰ�ýڵ㣺"+maxHead);
            			
            			if(ifCanPaint==true){
            				int this_row=0;
            				int this_column=0;
            				
            				StructurePointer maxHeaderp=ps.pointers[maxHead-1];
            				//��������ǰ�ýڵ�ĺ��ýڵ����ҵ��ѻ���������ĺ��ýڵ�
            				Integer maxnfheader=0;
            				for(int k=0;k<20;k++){
            					Integer nfheader=maxHeaderp.next[k];
            					System.out.println("�ڵ�"+maxHead+"��"+(k+1)+"�����ýڵ�Ϊ��"+nfheader);
            					if(nfheader==null) break;
            					
            					if(k==0&&nfheader-1==i){//���ǰ�ýڵ�ĵ�һ�����ýڵ��Ǳ��ڵ�:���ڵ�����к�Ϊ�����ǰ��ǰ�ýڵ�����кŶ�+1
            						this_row=pointerCoordinate[maxHeaderp.getNumber()-1].x+1;
            						this_column=pointerCoordinate[maxHeaderp.getNumber()-1].y;
            						System.out.println("���ǰ�ýڵ�ĵ�һ�����ýڵ��Ǳ��ڵ�"+nfheader+",���ڵ�������к�:("+this_row+","+this_column+")");
            						break;
            					}else if(nfheader-1!=i){
            						//���ǰ�ýڵ�ĵ�һ�����ýڵ�Ǳ��ڵ㣺�ҵ��ѻ���������ĺ��ýڵ㣬�򱾽ڵ����кţ����ǰ�ýڵ��ѻ����ĺ��ýڵ㣨�кţ��к�+1��
            						if(nfheader-1<i){ 
            							if(pointerStateArray[nfheader-1]!=0){ 
            								maxnfheader=nfheader;
            							}else{
                							System.out.println("�����ˣ����ǰ�ýڵ�ĵ�һ�����ýڵ�Ǳ��ڵ����������С�ڱ��ڵ�ĺ��ýڵ�δ��");
                						}
            						}
            						System.out.println("maxnfheader:"+maxnfheader);
            						this_row=pointerCoordinate[maxnfheader-1].x;
            						this_column=pointerCoordinate[maxnfheader-1].y-1;
            						System.out.println("�ڵ�"+(i+1)+"������ǰ�ýڵ��ѻ���������ĺ��ýڵ�Ϊ��"+maxnfheader+",���ڵ�������к�:("+this_row+","+this_column+")");
            					}
            				}
            				pointerCoordinate[i]=new Point(this_row,this_column);//��¼���ڵ����� 
            				drawContent(i);
            			}
            		}
            	}
            }
    	}
 
        //�ػ�JPanel  
        this.repaint();  
    }

  /**
   * �ܻ�����ݾ��λ�����ͼ����¼�����޸�״̬����
   * @param pointNum
   */
	private void drawContent(int pointNum) {
		paintRectange(pointNum);//������
		drawString(pointNum);//���ı���Ϣ
		drawAls(pointNum);//��·��
		pointerStateArray[pointNum]=1;//�޸�״̬����
		System.out.println("�Ƿ��־�ѻ���"+pointerStateArray[pointNum]);
	}  
    
    /**
     * ������
     * @param x
     * @param y
     */
    public void paintRectange(int pointNum){ 
    	System.out.println("paint the number "+(pointNum+1)+" rectange");
    	Point thisPoint=pointerCoordinate[pointNum];   
        if(og != null)  
        {  
            //���õ�super.paint(g),�ø�����һЩ��ǰ�Ĺ�������ˢ����Ļ  
            //super.paint(og);      
                  
            og.setColor(Color.WHITE);             //���û�ͼ����ɫ  
            System.out.println("x: "+rowCompany*thisPoint.x+"," +"y: "+columnCompany*thisPoint.y);
            og.fill3DRect(rowCompany*thisPoint.x, columnCompany*thisPoint.y, width, height, true);//��ͼ                  
            //this.paint(this.getGraphics());  
        } 
    }
    
    /**
     * ���ڵ�����ı���Ϣ
     * @param pointNum
     */
    public void drawString(int pointNum){
    	System.out.println("paint the number "+(pointNum+1)+" String");
    	StructurePointer p=ps.pointers[pointNum];
    	Point thisPoint=pointerCoordinate[pointNum];
    	og.setColor(Color.BLACK);
    	og.drawString(String.valueOf(p.getEarlistBegin()), rowCompany*thisPoint.x, columnCompany*thisPoint.y+20);
    	og.drawString(String.valueOf(p.getEarlistEnd()), rowCompany*thisPoint.x+50, columnCompany*thisPoint.y+20);
    	og.drawString(String.valueOf(p.getNumber()), rowCompany*thisPoint.x, columnCompany*thisPoint.y+(height/2));
    	og.drawString(p.getContent(), rowCompany*thisPoint.x+30, columnCompany*thisPoint.y+(height/2));
    	og.drawString(String.valueOf(p.getLatestBegin()), rowCompany*thisPoint.x, columnCompany*thisPoint.y+height);
    	og.drawString(String.valueOf(p.getLatestEnd()), rowCompany*thisPoint.x+50, columnCompany*thisPoint.y+height);
    }
    
    /**
     * ���ڵ�����ǰ�ڵ�ĸ���·��
     * @param pointNum
     */
    public void drawAls(int pointNum){
    	System.out.println("paint the number "+(pointNum+1)+" ALs");
    	StructurePointer p=ps.pointers[pointNum];
		int lenthofheader=p.head.length;
		Point thisPoint=pointerCoordinate[pointNum];   
		Point header;
		boolean ifCirticial=false;
		//����pointerCoordinate�����и���ǰ�ڵ�ͱ��ڵ�ľ�����㻭ֱ��·��:ǰ�ýڵ㣨���x+width/2�����y�������ڵ㣨���x�����y��
		if(pointNum!=0){
			for(int i=0;i<lenthofheader;i++){
				if(p.head[i]==null) break;
				Integer headNumber = p.head[i];
				header=pointerCoordinate[headNumber-1];
				if(p.getIsCriticial()==true&&ps.pointers[headNumber-1].getIsCriticial()==true)ifCirticial=true;
				drawAL(rowCompany*header.x+width/2,columnCompany*header.y, rowCompany*thisPoint.x, columnCompany*thisPoint.y,ifCirticial);
			}
		}
    }
    
    /*
     * ����ͷ
     */
    public void drawAL(int sx, int sy, int ex, int ey,boolean ifCirticial)  
    {  
  
        double H = 10; // ��ͷ�߶�  
        double L = 4; // �ױߵ�һ��  
        int x3 = 0;  
        int y3 = 0;  
        int x4 = 0;  
        int y4 = 0;  
        double awrad = Math.atan(L / H); // ��ͷ�Ƕ�  
        double arraow_len = Math.sqrt(L * L + H * H); // ��ͷ�ĳ���  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);  
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);  
        double x_3 = ex - arrXY_1[0]; // (x3,y3)�ǵ�һ�˵�  
        double y_3 = ey - arrXY_1[1];  
        double x_4 = ex - arrXY_2[0]; // (x4,y4)�ǵڶ��˵�  
        double y_4 = ey - arrXY_2[1];  
  
        Double X3 = new Double(x_3);  
        x3 = X3.intValue();  
        Double Y3 = new Double(y_3);  
        y3 = Y3.intValue();  
        Double X4 = new Double(x_4);  
        x4 = X4.intValue();  
        Double Y4 = new Double(y_4);  
        y4 = Y4.intValue();  
        // ���� :�ؼ�·��λ��ɫ����ͨ·��λ��ɫ
        if(ifCirticial==true) {
        	og.setColor(Color.RED);
        }else {        	
        	og.setColor(Color.BLACK);
        }
        og.drawLine(sx, sy, ex, ey);  
        //  
        GeneralPath triangle = new GeneralPath();  
        triangle.moveTo(ex, ey);  
        triangle.lineTo(x3, y3);  
        triangle.lineTo(x4, y4);  
        triangle.closePath();  
        //ʵ�ļ�ͷ  
        og.fill(triangle);  
        //��ʵ�ļ�ͷ  
        //g2.draw(triangle);  
  
    }  
  
    // ����  
    // ʸ����ת��������������ֱ���x������y��������ת�ǡ��Ƿ�ı䳤�ȡ��³���  
    public static double[] rotateVec(int px, int py, double ang,  
            boolean isChLen, double newLen) {  
  
        double mathstr[] = new double[2];  
        double vx = px * Math.cos(ang) - py * Math.sin(ang);  
        double vy = px * Math.sin(ang) + py * Math.cos(ang);  
        if (isChLen) {  
            double d = Math.sqrt(vx * vx + vy * vy);  
            vx = vx / d * newLen;  
            vy = vy / d * newLen;  
            mathstr[0] = vx;  
            mathstr[1] = vy;  
        }  
        return mathstr;  
    }  
  
      
    /** 
     * repaint���������paint���������Զ����Graphics���� 
     * Ȼ������øö������2D��ͼ 
     * ע���÷�������д��JPanel��paint���� 
     */  
    public void paint(Graphics g)  
    {  
        g.drawImage(image, 0, 0, this);   
    }  
}  