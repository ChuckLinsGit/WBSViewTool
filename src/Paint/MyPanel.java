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
 * 定义一个继承自JPanel的类，重写它的paint方法 * 
 */  
public class MyPanel extends JPanel  
{  
      
    private Image image;    //图像缓冲  
    private Graphics2D og;  
    private int griphicFontSize=20; 
  
    //定义第一个矩形的起点行列号
    private int x = 0;  
    private int y =5;
    
    //定义矩形的长宽及间距并推测形成坐标的行列号的乘数
    private int width=70;
    private int height=100;
    private int widthspace=40;
    private int heightspace=10;
    private int rowCompany=width+widthspace;
    private int columnCompany=height+heightspace;
    
    private StructureFactory sf;//项目节点工厂
    private ProjectStructure ps;//项目节点
    int pointerQty;//节点数量
    int[] pointerStateArray;//节点状态，表示是否已画
    Point[] pointerCoordinate;//记录每个节点的起始位置
    
    public MyPanel(String projectName){
    	//得到项目及其节点信息
    	ps=new ProjectStructure();
    	ps.setProjectName(projectName);
		sf=new StructureFactory(ps);
    	//初始化节点状态数组
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
     * 画矩形
     * 如果是第一个节点，直接画
     * 如果是中间节点，判断该节点的前节点数组中的节点是否都已出现，若是按下面矩形画法画，若不是则不画
     * 画法：根据其在pointers数组中的序号大小设置其距离第一个节点的位置（行列号），间距保持一直，
     *      画完写入各种属性信息，根据矩形坐标起点连箭头，在状态数组中将其设置为已画
     */
    public void display()  
    {
    	//System.out.println("enter function：display()");
    	//画图准备
    	if(og == null)  
    	{  
    		//JPanel继承自Component类，可以使用它的方法createImage创建一幅和JPanel大小相同的图形缓冲  
    		//然后用它Image接口的方法获得绘图对像  
    		image = this.createImage(this.getWidth(),this.getHeight());  
    		if(image != null)og = (Graphics2D) image.getGraphics();  
    		Font font=new Font("宋体",Font.ITALIC,griphicFontSize);
    		og.setFont(font);
    		//System.out.println("function display() base set have been done");
    	} 
    	

    	//循环遍历节点状态数组知道最后一个节点被画完
    	if(pointerQty!=0){
    		int row=x;//行数
    		int colmun=y;//列数
    		//System.out.println("enter display circul");
    		for(int i=0;i<pointerQty;i++){
    			System.out.println("节点"+(i+1)+"画图");
            	if(pointerStateArray[i]==0){
            		//画第一个节点
            		if(i==0){
            			//System.out.println("paint the first node");
            			System.out.println("行列号:("+row+","+colmun+")");
            			pointerCoordinate[0]=new Point(row,colmun);
            			drawContent(0);
            		}
            		//画满足条件的节点
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
        					
        					//找到该节点最大的前置节点
        					if(head>maxHead) maxHead=head;
        					
        					//分析并判断该节点能不能画:该节点的所有前置节点必须已画
        					if(pointerStateArray[head-1]==0){
        						ifCanPaint=false;
//此处还需抛出错误：前置节点未画只能是因为本节点的序号小于其未画的前置节点，本节点有前置节点未画属于项目输入错误，目前不予以处理。
        						System.out.println("节点"+i+"出错啦：本节点有前置节点未画");
        						break;
        					}
            			}
            			System.out.println("最大前置节点："+maxHead);
            			
            			if(ifCanPaint==true){
            				int this_row=0;
            				int this_column=0;
            				
            				StructurePointer maxHeaderp=ps.pointers[maxHead-1];
            				//遍历最大的前置节点的后置节点以找到已画的序号最大的后置节点
            				Integer maxnfheader=0;
            				for(int k=0;k<20;k++){
            					Integer nfheader=maxHeaderp.next[k];
            					System.out.println("节点"+maxHead+"第"+(k+1)+"个后置节点为："+nfheader);
            					if(nfheader==null) break;
            					
            					if(k==0&&nfheader-1==i){//最大前置节点的第一个后置节点是本节点:本节点的行列号为：最大前置前置节点的行列号都+1
            						this_row=pointerCoordinate[maxHeaderp.getNumber()-1].x+1;
            						this_column=pointerCoordinate[maxHeaderp.getNumber()-1].y;
            						System.out.println("最大前置节点的第一个后置节点是本节点"+nfheader+",本节点起点行列号:("+this_row+","+this_column+")");
            						break;
            					}else if(nfheader-1!=i){
            						//最大前置节点的第一个后置节点非本节点：找到已画的序号最大的后置节点，则本节点行列号：最大前置节点已画最大的后置节点（行号，列号+1）
            						if(nfheader-1<i){ 
            							if(pointerStateArray[nfheader-1]!=0){ 
            								maxnfheader=nfheader;
            							}else{
                							System.out.println("出错了：最大前置节点的第一个后置节点非本节点且遇到序号小于本节点的后置节点未画");
                						}
            						}
            						System.out.println("maxnfheader:"+maxnfheader);
            						this_row=pointerCoordinate[maxnfheader-1].x;
            						this_column=pointerCoordinate[maxnfheader-1].y-1;
            						System.out.println("节点"+(i+1)+"的最大的前置节点已画的序号最大的后置节点为："+maxnfheader+",本节点起点行列号:("+this_row+","+this_column+")");
            					}
            				}
            				pointerCoordinate[i]=new Point(this_row,this_column);//记录本节点的起点 
            				drawContent(i);
            			}
            		}
            	}
            }
    	}
 
        //重绘JPanel  
        this.repaint();  
    }

  /**
   * 能画则根据矩形画法画图并记录起点和修改状态数组
   * @param pointNum
   */
	private void drawContent(int pointNum) {
		paintRectange(pointNum);//画矩形
		drawString(pointNum);//画文本信息
		drawAls(pointNum);//画路径
		pointerStateArray[pointNum]=1;//修改状态数组
		System.out.println("是否标志已画："+pointerStateArray[pointNum]);
	}  
    
    /**
     * 画矩形
     * @param x
     * @param y
     */
    public void paintRectange(int pointNum){ 
    	System.out.println("paint the number "+(pointNum+1)+" rectange");
    	Point thisPoint=pointerCoordinate[pointNum];   
        if(og != null)  
        {  
            //调用的super.paint(g),让父类做一些事前的工作，如刷新屏幕  
            //super.paint(og);      
                  
            og.setColor(Color.WHITE);             //设置画图的颜色  
            System.out.println("x: "+rowCompany*thisPoint.x+"," +"y: "+columnCompany*thisPoint.y);
            og.fill3DRect(rowCompany*thisPoint.x, columnCompany*thisPoint.y, width, height, true);//绘图                  
            //this.paint(this.getGraphics());  
        } 
    }
    
    /**
     * 画节点各种文本信息
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
     * 画节点与其前节点的各条路径
     * @param pointNum
     */
    public void drawAls(int pointNum){
    	System.out.println("paint the number "+(pointNum+1)+" ALs");
    	StructurePointer p=ps.pointers[pointNum];
		int lenthofheader=p.head.length;
		Point thisPoint=pointerCoordinate[pointNum];   
		Point header;
		boolean ifCirticial=false;
		//根据pointerCoordinate数组中各个前节点和本节点的矩形起点画直线路径:前置节点（起点x+width/2，起点y），本节点（起点x，起点y）
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
     * 画箭头
     */
    public void drawAL(int sx, int sy, int ex, int ey,boolean ifCirticial)  
    {  
  
        double H = 10; // 箭头高度  
        double L = 4; // 底边的一半  
        int x3 = 0;  
        int y3 = 0;  
        int x4 = 0;  
        int y4 = 0;  
        double awrad = Math.atan(L / H); // 箭头角度  
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);  
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);  
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点  
        double y_3 = ey - arrXY_1[1];  
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点  
        double y_4 = ey - arrXY_2[1];  
  
        Double X3 = new Double(x_3);  
        x3 = X3.intValue();  
        Double Y3 = new Double(y_3);  
        y3 = Y3.intValue();  
        Double X4 = new Double(x_4);  
        x4 = X4.intValue();  
        Double Y4 = new Double(y_4);  
        y4 = Y4.intValue();  
        // 画线 :关键路径位红色，普通路径位黑色
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
        //实心箭头  
        og.fill(triangle);  
        //非实心箭头  
        //g2.draw(triangle);  
  
    }  
  
    // 计算  
    // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度  
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
     * repaint方法会调用paint方法，并自动获得Graphics对像 
     * 然后可以用该对像进行2D画图 
     * 注：该方法是重写了JPanel的paint方法 
     */  
    public void paint(Graphics g)  
    {  
        g.drawImage(image, 0, 0, this);   
    }  
}  