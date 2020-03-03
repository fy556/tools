package swt.vad_test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.ScrollPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.awt.event.ActionEvent;

public class Helpers extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	
	
	private static String tooldir=System.getProperty("user.dir");
	private static String helpersFileDirPath;

	List<String> audioNames=new ArrayList<String>();
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private JTextField textField_1;
	
	private static String VadFileDirPath;
	private String copyDirPath;
	
	JTextArea textArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Helpers frame = new Helpers();
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
	public Helpers() {
		setTitle("辅助工具");
		
		File helpersFileDir=new File(tooldir+"\\helpers\\");
		if(!helpersFileDir.exists()&&!helpersFileDir.isDirectory()){
			helpersFileDir.mkdir();
		}
		
		File VadFileDir=new File(tooldir+"\\VadFileDir");
		if(!VadFileDir.exists()&&!VadFileDir.isDirectory()){
			VadFileDir.mkdir();
		}
		VadFileDirPath=VadFileDir.getAbsolutePath();
		helpersFileDirPath=helpersFileDir.getAbsolutePath();
		System.out.println(helpersFileDirPath);
		
		
		
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton button = new JButton("\u8BF7\u9009\u62E9\u6587\u4EF6");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        chooser.setCurrentDirectory(new File(helpersFileDirPath));
//		        chooser.setSelectedFile(new File(helpersFileDirPath));
		        chooser.setDialogTitle("选择文件");
		        chooser.showDialog(new JLabel(), "选择");//选择Vad音频目录
		        File file = chooser.getSelectedFile();
		        
		        try {
		        	if(file.exists()){
			        	textField.setText(file.getAbsoluteFile().toString());
			        }
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("未选择文件");
				}
		        
		        
			}
		});
		button.setBounds(10, 26, 117, 23);
		contentPane.add(button);
		
		textField = new JTextField();
		textField.setBounds(162, 27, 262, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button_1 = new JButton("\u5F00\u59CB");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				audioNames.clear();
				
				if(!textField.getText().isEmpty()){
					if(new File(textField.getText()).isFile()){
						try {
							//获取目录下文件名信息
							readfile(textField.getText());//获取音频列表数据
							
							
							//创建目录
							String strTime=dateFormat.format(new Date().getTime());
							File file=new File(helpersFileDirPath+"\\"+strTime);
							if(!file.exists()){
								file.mkdir();
								if(file.exists()){
									copyDirPath=file.getAbsolutePath();
									System.out.println("创建目录"+helpersFileDirPath+"\\"+strTime+"成功");
								}
							}
							
							//copy文件到创建目录下
							if(audioNames.size()!=0){
								if(!textField_1.getText().isEmpty()){
									scanDirfile_copy(textField_1.getText());
								}
								
							}else {
								System.out.println(audioNames.size());
							}
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
				
				
				
				
				
				
				
			}
		});
		button_1.setBounds(10, 107, 93, 23);
		contentPane.add(button_1);
		
		textArea = new JTextArea();
		textArea.setBounds(113, 107, 311, 144);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(113, 107, 311, 144);
		scrollPane.setViewportView(textArea);
		contentPane.add(scrollPane);
		
		JButton button_2 = new JButton("\u8BF7\u9009\u62E9\u97F3\u9891\u76EE\u5F55");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
//		        chooser.setSelectedFile(new File(VadFileDirPath));
		        chooser.setDialogTitle("选择音频目录");
		        chooser.showDialog(new JLabel(), "选择");//选择Vad音频目录
		        File file = chooser.getSelectedFile();
		        
		        try {
		        	if(file.exists()){
		        		textField_1.setText(file.getAbsoluteFile().toString());
			        }
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("未选择音频目录");
				}
		       
			}
		});
		button_2.setBounds(10, 59, 117, 23);
		contentPane.add(button_2);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(162, 60, 262, 21);
		contentPane.add(textField_1);
	}
	
	
	public void readfile(String file_name) throws IOException{
		File file=new File(file_name);
		if(file.isFile()){
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String strRead = null;
			int count=0;
			while((strRead = reader.readLine())!= null){
				count++;
//				System.out.println("第"+count+"行为:"+strRead);
				audioNames.add(strRead);
			}
			reader.close();
			
		}
	}
	
	public void scanDirfile_copy(String filepath_name) throws IOException{
		File file=new File(filepath_name);
		if(file.isDirectory()){
			int filelistLength=file.listFiles().length;
			File[] files=file.listFiles();
			
			if(filelistLength>0){
				int count=0;
				for(int i=0;i<filelistLength;i++){
					if(audioNames.contains(files[i].getName())){
						String cmd="cmd /c copy "+"\""+files[i].getAbsolutePath()+"\""+" "+"\""+copyDirPath+"\\"+files[i].getName()+"\"";
						
						System.out.println(cmd);
						
						textArea.append(cmd+"\n");
				        textArea.paintImmediately(textArea.getBounds());
				        textArea.setCaretPosition(textArea.getText().length());
				        
				        
				        
				        Runtime.getRuntime().exec(cmd);
				        if(count==999){//如果显示了1000行内容，则进行清空
			        		textArea.setText(""); 
			        		count=0;
			        	}
				        count++;
						
					}
					
				}
			}
			
		}
	}
}
