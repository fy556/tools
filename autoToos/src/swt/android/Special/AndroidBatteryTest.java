package swt.android.Special;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.DropMode;
import javax.swing.JTextArea;

public class AndroidBatteryTest extends JFrame {

	private JPanel contentPane;
	int devices;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AndroidBatteryTest frame = new AndroidBatteryTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AndroidBatteryTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 742, 479);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JLabel label = new JLabel("\u7535\u91CF");
		label.setBounds(213, 65, 54, 15);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("\u624B\u673A");
		label_1.setBounds(213, 32, 81, 15);
		contentPane.add(label_1);
		
		JButton button_1 = new JButton("\u67E5\u8BE2\u5F53\u524D\u624B\u673A");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				Process p;
				try {
					p = Runtime.getRuntime().exec("adb devices");
					InputStream input = p.getInputStream();  
					BufferedReader reader = new BufferedReader(new InputStreamReader(input));
					String s = null;
					List<String> list = new ArrayList<String>();
					while((s=reader.readLine())!=null){
						System.out.println(s);
						list.add(s);
					}
					Vector contents=new Vector();
					for(int i=1;i<list.size()-1;i++){
					contents.addElement(list.get(i).substring(0,list.get(i).indexOf("\t")));//添加设备名称
					}
					label_1.setText(list.get(1).substring(0,list.get(1).indexOf("\t")));
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
			}
		});
		button_1.setBounds(45, 28, 158, 23);
		contentPane.add(button_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText("111");
		textArea.setBounds(45, 105, 158, 211);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 105, 222, 211);
		scrollPane.setViewportView(textArea);
		contentPane.add(scrollPane);
		
		JButton button = new JButton("\u67E5\u8BE2\u5F53\u524D\u624B\u673A\u7535\u91CF");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Process p;
				try {
					p = Runtime.getRuntime().exec("adb shell dumpsys battery");
					InputStream input = p.getInputStream();  
					BufferedReader reader = new BufferedReader(new InputStreamReader(input));
					StringBuilder builder=new StringBuilder();
					String s = null;
					List<String> list = new ArrayList<String>();
					while((s=reader.readLine())!=null){
						System.out.println(s);
						builder.append(s);
						builder.append("\n");
					}
					textArea.setText(builder.substring(0,builder.length()-1));
					
//					Vector contents=new Vector();
//					for(int i=1;i<list.size()-1;i++){
//					contents.addElement(list.get(i).substring(0,list.get(i).indexOf("\t")));//添加设备名称
//					}
//					lblNewLabel.setText(list.get(1).substring(0,list.get(1).indexOf("\t")));
//					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
			}
		});
		button.setBounds(45, 61, 158, 23);
		contentPane.add(button);
	
	}
	
	public void execCmd(String cmd) {
		System.out.println(cmd);  
		try {  
			Process p = Runtime.getRuntime().exec(cmd);  
			InputStream input = p.getInputStream();  
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String s = null;
			while((s=reader.readLine())!=null){
				System.out.println(s);
			}
			reader.close();
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
}
