package swt.android;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
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
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.border.BevelBorder;
import javax.swing.JTextArea;
import javax.swing.event.AncestorListener;

import swt.SelectTestUI;

import javax.swing.event.AncestorEvent;
import javax.swing.SwingConstants;

public class AndroidMonkey extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	int devices;
	int packages;
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	private static JList list_1;
	private static JList list_2;
	private JButton button_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AndroidMonkey frame = new AndroidMonkey();
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
	public AndroidMonkey() {
		setTitle("AndroidMonkey");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblMonkey = new JLabel("Android monkey\u6D4B\u8BD5");
		lblMonkey.setBounds(230, 17, 168, 20);
		lblMonkey.setFont(new Font("宋体", Font.PLAIN, 17));
		contentPane.add(lblMonkey);

		JButton button = new JButton("\u67E5\u8BE2\u8FDE\u63A5\u8BBE\u5907");
		button.setBounds(30, 73, 168, 20);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = new File(this.getClass().getResource("").getPath());
				StringBuilder stringBuilder=new StringBuilder("cmd /c adb devices > ");
				stringBuilder.append(f);
				stringBuilder.append("\\devices.txt");
				String cmd=stringBuilder.toString();

				System.out.println("执行命令：");
				execCmd(cmd);
				File devicesfile = new File(f+"\\devices.txt");
				try {
					FileInputStream devicesfileStream = new FileInputStream(devicesfile);
					Scanner scanner = new Scanner(devicesfileStream);
					int count=0;
					List<String> list = new ArrayList<String>();
					while(scanner.hasNextLine()){
						list.add(scanner.nextLine());
						count++;
					}
					devices=count-2;
					System.out.println("设备数量为："+devices);
					textField.setText(devices+"");
					System.out.println(list.get(1));
					Vector contents=new Vector();
					for(int i=1;i<list.size()-1;i++){
						contents.addElement(list.get(i).substring(0,list.get(i).indexOf("\t")));//添加设备名称
					}
					list_1=new JList(contents);
					scrollPane1.setViewportView(list_1);

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			}
		});
		contentPane.add(button);

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(208, 73, 66, 20);

		textField.setText("\u6570\u91CF...");
		contentPane.add(textField);
		textField.setColumns(10);

		scrollPane1 = new JScrollPane();
		scrollPane1.setToolTipText("\u8BBE\u5907ID");
		scrollPane1.setBounds(295, 73, 196, 20);
		contentPane.add(scrollPane1);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(30, 141, 168, 33);
		contentPane.add(lblNewLabel);

		JButton button_1 = new JButton("\u67E5\u8BE2\u8BBE\u5907\u5B89\u88C5\u5305");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = new File(this.getClass().getResource("").getPath());
				StringBuilder stringBuilder=new StringBuilder("cmd /c adb shell pm list package > ");
				stringBuilder.append(f);
				stringBuilder.append("\\package.txt");
				String cmd=stringBuilder.toString();

				System.out.println("执行命令：");
				execCmd(cmd);
				File devicesfile = new File(f+"\\package.txt");
				try {
					FileInputStream devicesfileStream = new FileInputStream(devicesfile);
					Scanner scanner = new Scanner(devicesfileStream);
					int count=0;
					List<String> list = new ArrayList<String>();
					while(scanner.hasNextLine()){
						list.add(scanner.nextLine());
						count++;
					}
					packages=count;
					System.out.println("安装包数量为："+packages);
					textField.setText(packages+"");
					//					System.out.println(list.get(1));

					Vector contents=new Vector();

					for(int i=0;i<list.size();i++){
						contents.addElement(list.get(i));
					}
					list_2=new JList(contents);
					scrollPane2.setViewportView(list_2);

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.setBounds(30, 184, 168, 42);
		contentPane.add(button_1);

		scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(30, 227, 168, 207);
		contentPane.add(scrollPane2);

		button_2 = new JButton("\u5F00\u59CB\u6267\u884C\u6D4B\u8BD5");
		button_2.setBounds(230, 184, 168, 42);
		contentPane.add(button_2);

		JButton button_3 = new JButton("<");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectTestUI.Selectframe.setLocation(AndroidMonkey.this.getX(), AndroidMonkey.this.getY());
				SelectTestUI.Selectframe.setVisible(true);
				
				AndroidMonkey.this.dispose();
			}
		});
		button_3.setBounds(30, 17, 25, 22);
		contentPane.add(button_3);
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
