package Paint;

import javax.swing.JFrame;


  
public class PaintPanel  
{  
    public static void main(String[] args)  
    {  
        JFrame  jf = new JFrame();  
        jf.setBounds(0, 0, 2000, 1800);  
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        jf.setLayout(null);
        
        MyPanel jp = new MyPanel("商业企划"); 
        jp.setBounds(0, 0, 2000, 1800);
        jp.setAutoscrolls(true);
        
        jf.add(jp);  
        jf.setVisible(true);  
        jf.setResizable(true);
        
        jp.display();  

    }  
}  
