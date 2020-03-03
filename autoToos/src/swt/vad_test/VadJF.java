package swt.vad_test;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import com.jxcell.View;

import swt.SelectTestUI;
import swt.android.AndroidMonkey;

public class VadJF extends JFrame {

	private JPanel contentPane;
	
	
	Helpers helpers;
	
	boolean helpers_isExsit=false;
	
	private static String tooldir=System.getProperty("user.dir");
	private static String VadFileDirPath;
	private static String logsFileDirPath;
	private static String reportsFileDirPath;
	
	JButton btn_start;
	JButton button_stop;
	JButton button_generate_report;
	JButton button_look_report; 
	
	
	JTextArea textArea;
	JTextArea textArea_1;
	
	String cmd;
	
	
	//用来生成日志文件名
	long dateTime;
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	//vad文件总数量
	int vadfileSum;
	
	//测试状态已经启动的标签（用来控制停止测试）
//	boolean testStart=false;
	boolean testStop=false;
	
	//用来显示音频目录路径的text
	private JTextField textField;
	
	//用来显示日志文件的text
	private JTextField textField_1;
	
	
	//生成报告所要的数据
	List<String> file_name_list=new ArrayList<String>();
	List<String> file_timeLong_list=new ArrayList<String>();
	List<String> feed_file_timelist=new ArrayList<String>();
	List<String> vad_startTimelist=new ArrayList<String>();
	List<String> vad_end_notpauseTime_Timelist=new ArrayList<String>();
	List<String> vad_endTimelist=new ArrayList<String>();
	List<String> asrIdentify_Resultlist=new ArrayList<String>();
	
	//最近生成的报告
	String Recent;
	
	//设备device
	String deviceID;
	List<String> deviceIDlist=new ArrayList<String>();
	
	//标签
	JLabel lblNewLabel_1;
	JLabel lblDevices;
	JLabel lblAndroid;
	
	//检测测试包是否安装：
	boolean installed_sample=false;
	boolean installed_sample_test=false;
	
	String installed_sample_applastTime;
	String installed_sample_test_applastTime;
	
	//安装apk 并push文件标志：
	boolean installed_pushed=false;
	
	private JButton btnNewButton;
	
	Thread thread;
	private JButton button_2;
	
	
	//日志文件最后一行内容
	String lastlineContent;
	private JButton button_3;
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					VadJF frame = new VadJF();
					frame.setVisible(true);
					frame.setResizable(false);
					File VadFileDir=new File(tooldir+"\\VadFileDir");
					if(!VadFileDir.exists()&&!VadFileDir.isDirectory()){
						VadFileDir.mkdir();
					}
					File logsDir=new File(tooldir+"\\logs");
					if(!logsDir.exists()&&!logsDir.isDirectory()){
						logsDir.mkdir();
					}
					File excelDir=new File(tooldir+"\\reports");
					if(!excelDir.exists()&&!excelDir.isDirectory()){
						excelDir.mkdir();
					}
					
					VadFileDirPath=VadFileDir.getAbsolutePath();
					logsFileDirPath=logsDir.getAbsolutePath();
					reportsFileDirPath=excelDir.getAbsolutePath();
					System.out.println(VadFileDirPath);
					frame.autoCheck_connect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	//自动检测设备连接
	public void autoCheck_connect(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Process process;
				
				try {
					while(true){
						deviceIDlist.clear();
						process = Runtime.getRuntime().exec("adb\\adb.exe devices");
						InputStream input = process.getInputStream();
				        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
				        String s = null;
				        while((s=reader.readLine())!=null){
//				        	 System.out.println(s);
				        	 if(s.contains("device")&&(!s.contains("List"))){
				        		 String[] sList=s.split("\t");
//				        		 System.out.println(sList[0]);
				        		 if(deviceIDlist.isEmpty()){//如果设备id列表是空，则把设备id添加到设备id列表中
				        			 deviceIDlist.add(sList[0]);
				        		 }else {//如果不为空
									if(!deviceIDlist.contains(sList[0])){//先查看设备列表中如果不存在设备ID,则把该设备id添加到设备id列表中
										deviceIDlist.add(sList[0]);
									}
								}
				        	 }
				        }
				        reader.close();
				        input.close();
//				        System.out.println(deviceIDlist.size());
				        if(deviceIDlist.isEmpty()){
				        	lblNewLabel_1.setText("设备是否连接：未连接");
				        	lblDevices.setText("deviceID：");
				        	lblAndroid.setText("Android：");
				        	btnNewButton.setEnabled(false);
				        	btn_start.setEnabled(false);
				        	
				        }else {
				        	lblNewLabel_1.setText("设备是否连接：已连接");
				        	lblDevices.setText("deviceID："+deviceIDlist.get(0));
				        	
				        	process = Runtime.getRuntime().exec("adb\\adb.exe -s "+deviceIDlist.get(0)+ " shell \"getprop ro.build.version.release\"");
					        BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
					        String s1 = reader1.readLine();
					        if(s1!=null){
//					        	System.out.println(s1);
					        	lblAndroid.setText("Android："+s1);
					        }
					        reader1.close();
					        btnNewButton.setEnabled(true);
					        btn_start.setEnabled(true);
					        
						}
				        Thread.sleep(3000);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	//检测app安装
	public void Check_appInstall(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				btn_start.setEnabled(false);
				
				try {
					String cmd1="adb\\adb.exe -s "+deviceIDlist.get(0);
					System.out.println("deviceIDlist_SIZE："+deviceIDlist.size());
					System.out.println("deviceIDlist_get(0)："+deviceIDlist.get(0));
					cmd=cmd1;
					
					exeC(cmd1+" shell pm list packages -3");

					if(!installed_sample){//如果未安装sample-debug.apk，则安装
						exeC(cmd1+" install -t -r "+"apk\\sample-debug.apk");
					}else {
//						exeC(cmd1+" install -t -r "+"apk\\sample-debug.apk");
					}
					if(!installed_sample_test){//如果未安装sample-debug-androidTest.apk，则安装
						exeC(cmd1+" install -t -r "+"apk\\sample-debug-androidTest.apk");
					}else {
						//已经安装了，则替换
						exeC(cmd1+" install -t -r "+"apk\\sample-debug-androidTest.apk");
					}

					exeC(cmd1+" shell am instrument -w -r   -e debug false -e class 'unit_test.export.vadtest.AICloudDMEngineTest1#testIsExistPath_and_createPath' com.aispeech.sample.test/android.support.test.runner.AndroidJUnitRunner");
					
					
					File vadfiles=new File(textField.getText());
					File[] vadfileList = vadfiles.listFiles();
					String[] vadfilenames=vadfiles.list();
					textArea.append("该目录下文件数量为："+vadfileList.length+"，如下所示:\n");
					textArea.paintImmediately(textArea.getBounds());
					textArea.setCaretPosition(textArea.getText().length());
					System.out.println("该目录下文件数量为："+vadfileList.length);
					int fileSum=0;
					boolean dir_blank=false;
					if(vadfiles.isDirectory()&&vadfiles.getPath().equals(VadFileDirPath)){
						
						for(int i=0;i<vadfileList.length;i++){
							if(vadfileList[i].getPath().endsWith(".wav")){
								fileSum++;
								double ls = 0;
								AudioFileIO audioFileIO=new AudioFileIO();
								AudioFile audioFile = null;
								try {
									audioFile = audioFileIO.readFile(vadfileList[i]);
								} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
										| InvalidAudioFrameException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								AudioHeader audioHeader=audioFile.getAudioHeader();
								ls= (double) audioHeader.getPreciseTrackLength();
								double fileTime_ms=ls*1000;
								DecimalFormat myformat = new DecimalFormat(".000");
								String timelong=myformat.format(fileTime_ms);
								//							String timelong=(ls+"").substring(0, ((ls+"").indexOf(".")+4));
								System.out.println(vadfileList[i].getPath());
								//							System.out.println(ls);
								System.out.println(timelong); 
								file_timeLong_list.add(timelong);
								//							try {
								//								docs.insertString(docs.getLength(), vadfileList[i].getPath()+"\n", attrset);
								//							} catch (BadLocationException e1) {
								//								// TODO Auto-generated catch block
								//								e1.printStackTrace();
								//							}//对文本进行追加
								//							try {
								//								textPane.setText(docs.getText(0, docs.getLength()));
								//							} catch (BadLocationException e) {
								//								// TODO Auto-generated catch block
								//								e.printStackTrace();
								//							}
								textArea.append(vadfileList[i].getPath()+"\n");
								textArea.paintImmediately(textArea.getBounds());
								textArea.setCaretPosition(textArea.getText().length());
								try {
									textArea.append("开始执行push文件："+vadfilenames[i]+"\n");
									textArea.paintImmediately(textArea.getBounds());
									System.out.println(vadfileList[i].getParent());
									exeC(cmd1+" push "+"VadFileDir\\"+vadfilenames[i]+" /sdcard/vadTest/"+vadfilenames[i]);
									//								exeC("cmd /c "+"\" \""+cmd1+" push "+"\""+vadfileList[i].getPath()+"\""+"/sdcard/vadTest/"+vadfilenames[i]);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					}else if(vadfiles.isDirectory()&&!vadfiles.getPath().equals(VadFileDirPath)){
						for(int i=0;i<vadfileList.length;i++){
							if(vadfileList[i].getPath().endsWith(".wav")){
								fileSum++;
								double ls = 0;
								AudioFileIO audioFileIO=new AudioFileIO();
								AudioFile audioFile = null;
								try {
									audioFile = audioFileIO.readFile(vadfileList[i]);
								} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
										| InvalidAudioFrameException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								AudioHeader audioHeader=audioFile.getAudioHeader();
								ls= (double) audioHeader.getPreciseTrackLength();
								double fileTime_ms=ls*1000;
								DecimalFormat myformat = new DecimalFormat(".000");
								String timelong=myformat.format(fileTime_ms);
								//							String timelong=(ls+"").substring(0, ((ls+"").indexOf(".")+4));
								System.out.println(vadfileList[i].getPath());
								//							System.out.println(ls);
								System.out.println(timelong); 
								file_timeLong_list.add(timelong);
							
								textArea.append(vadfileList[i].getPath()+"\n");
								textArea.paintImmediately(textArea.getBounds());
								textArea.setCaretPosition(textArea.getText().length());
								try {
									textArea.append("开始执行push文件："+vadfilenames[i]+"\n");
									textArea.paintImmediately(textArea.getBounds());
									System.out.println(vadfileList[i].getParent());
									if(vadfileList[i].getPath().contains(" ")){
										dir_blank=true;
//										String a=vadfileList[i].getPath().replaceAll(" ", "\\\\\" \\\\\"");
//										exeC(cmd1+" push "+"\""+a+"\""+" /sdcard/vadTest/"+vadfilenames[i]);
										JOptionPane.showMessageDialog(getContentPane(),"非工具默认测试音频目录下的路径，其他测试音频目录路径中含有空格，请把音频文件放在不含有空格的路径目录下");
										break;
									}else {
										exeC(cmd1+" push "+"\""+vadfileList[i].getPath()+"\""+" /sdcard/vadTest/"+vadfilenames[i]);
									}

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					}
					vadfileSum=fileSum;
					installed_pushed=true;
					if(!dir_blank){
						textArea.append("push文件结束，请开始测试\n");
						textArea.paintImmediately(textArea.getBounds());
					}else {
						textArea.append("非工具默认测试音频目录下的路径，其他测试音频目录路径中含有空格，请把音频文件放在不含有空格的路径目录下\n");
						textArea.paintImmediately(textArea.getBounds());
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
	
	public void exeC(String cmd) throws IOException{
		System.out.println(cmd);
		if(cmd.contains(" shell pm list packages -3")){
			Process process=Runtime.getRuntime().exec(cmd);
			InputStream input = process.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
	        String s = null;
	        while((s=reader.readLine())!=null){
	            if(s.contains("com.aispeech.sample")&&(!s.contains("com.aispeech.sample.test"))){
	            	System.out.println("存在测试包：com.aispeech.sample");
	            	installed_sample=true;
	 	            textArea.append("存在"+s+"\n");
	 	            textArea.paintImmediately(textArea.getBounds());
	 	            textArea.setCaretPosition(textArea.getText().length());
	            }else if(s.contains("com.aispeech.sample.test")){
	            	System.out.println("存在测试包：com.aispeech.sample.test");
	            	installed_sample_test=true;
	            	textArea.append("存在"+s+"\n");
	 	            textArea.paintImmediately(textArea.getBounds());
	 	            textArea.setCaretPosition(textArea.getText().length());
	            }
	        }
	        reader.close();
	        input.close();
		}else if(cmd.contains("unit_test.export.vadtest.AICloudDMEngineTest1#Vad_test1")){
			Process process=Runtime.getRuntime().exec(cmd);
			InputStream input = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
			String s = null;
			while((s=reader.readLine())!=null){
				System.out.println(s);
				textArea.append(s+"\n");
				textArea.paintImmediately(textArea.getBounds());
				textArea.setCaretPosition(textArea.getText().length());
				if(s.contains("OK (")){
					
					testStop=true;
					System.out.println("testStop="+get_testStop());
				}
			}
			reader.close();
			input.close();
		}else {
			Process process=Runtime.getRuntime().exec(cmd);
			InputStream input = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
			String s = null;
			while((s=reader.readLine())!=null){
				System.out.println(s);
				textArea.append(s+"\n");
				textArea.paintImmediately(textArea.getBounds());
				textArea.setCaretPosition(textArea.getText().length());
			}
			reader.close();
			input.close();
		}
		
		
		
	}
	
	public void exeC_adb_log(String cmd) throws IOException{
		dateTime=new Date().getTime();
		String time = dateFormat.format(dateTime);
		String pathname=tooldir+"\\logs\\"+time+".txt";
		File file=new File(pathname);
//		if(!file.exists()){
//			System.out.println("创建文件"+pathname);
//			file.createNewFile();
//		}
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		Process process=Runtime.getRuntime().exec(cmd);
		InputStream input = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
        String s = null;
        int num=0;
        while((s=reader.readLine())!=null){
        	String writedate=s+"\n";
//        	System.out.println(""+writedate.length());
        	writer.write(writedate, 0,writedate.length());
        	writer.flush();//刷新缓冲区
//        	if(s.contains("unit_test")){
//        		textArea_1.append(s+"\n");
//            	textArea_1.paintImmediately(textArea_1.getBounds());
//            	textArea_1.setCaretPosition(textArea_1.getText().length());  
//            	try {
//    				Thread.sleep(10);
//    			} catch (InterruptedException e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//            	num++;
//            	if(num==999){//如果显示了1000行内容，则进行清空
//            		textArea_1.setText(""); 
//            	}
//        	}
        	
        	textArea_1.append(s+"\n");
        	textArea_1.paintImmediately(textArea_1.getBounds());
        	textArea_1.setCaretPosition(textArea_1.getText().length());  
        	try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	num++;
        	if(num==999){//如果显示了1000行内容，则进行清空
        		textArea_1.setText(""); 
        		num=0;
        	}
        	
        	
        	if(s.contains("TestRunner: finished: Vad_test_end_stat")){//如果手动测试结束，则退出该循环
        		btn_start.setEnabled(true);
        		break;
        	}
        	
        	if(s.contains("TestRunner: finished: Vad_test1")){//如果正常测试结束，则退出该循环
        		btn_start.setEnabled(true);
        		break;
        	}
        	
        	if(s.contains("com.aispeech.sample E/AndroidRuntime: FATAL EXCEPTION:")){//引擎崩溃
        		btn_start.setEnabled(true);
        		break;
        	}
        	
        	if(get_testStop()==true){//手动停止测试时的判断
        		testStop=false;
        		break;
        	}
        }
        System.out.println("执行结束endStat");
		String writedate1="执行结束endStat"+"\n";
    	writer.write(writedate1, 0,writedate1.length());
    	
        reader.close();
        input.close();
        writer.flush();//刷新缓冲区
        writer.close();
        
        
        textArea_1.append("执行结束"+"\n");
        textArea_1.paintImmediately(textArea_1.getBounds());
        textArea_1.setCaretPosition(textArea_1.getText().length());  
        button_stop.setEnabled(false);
        btn_start.setEnabled(true);
 
	}
	
	
	
	public String get_test_process(){
		String pid=null;
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd+" shell \"ps | grep com.aispeech.sample\"");
			InputStream input = process.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
	        String s = null;
	        while((s=reader.readLine())!=null){
//	        	System.out.println(s);
//	            String[] s_list=s.split(" ");
//	            for(int i=0;i<s_list.length;i++){
//	            	System.out.println(i+" "+s_list[i]);
//	            }
	        	if(s.contains("com.aispeech.sample")){
	        		pid=s.split(" ")[4];
	        	}
	        }
	        reader.close();
	        input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pid;
	}
	
	
	public void end_test_process(){
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd+" shell \"ps | grep com.aispeech.sample\"");
			InputStream input = process.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
	        String s = null;
	        while((s=reader.readLine())!=null){
//	        	System.out.println(s);
//	            String[] s_list=s.split(" ");
//	            for(int i=0;i<s_list.length;i++){
//	            	System.out.println(i+" "+s_list[i]);
//	            }
	        	if(s.contains("com.aispeech.sample")){
	        		String pid=s.split(" ")[4];
	        		System.out.println("执行结束进程："+pid);
	        		Runtime.getRuntime().exec(cmd+" shell am force-stop com.aispeech.sample");
	        	}
	        }
	        reader.close();
	        input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public Thread getThread(){
		return thread;
		
	}
	
//	public boolean get_testStart(){
//		return testStart;
//		
//	}
	
	
	public boolean get_testStop(){
		return testStop;
		
	}
	
	
	
	public void init(){
	
		File VadFileDir=new File(tooldir+"\\VadFileDir");
		if(!VadFileDir.exists()&&!VadFileDir.isDirectory()){
			VadFileDir.mkdir();
		}
		File logsDir=new File(tooldir+"\\logs");
		if(!logsDir.exists()&&!logsDir.isDirectory()){
			logsDir.mkdir();
		}
		File excelDir=new File(tooldir+"\\reports");
		if(!excelDir.exists()&&!excelDir.isDirectory()){
			excelDir.mkdir();
		}
		
		VadFileDirPath=VadFileDir.getAbsolutePath();
		logsFileDirPath=logsDir.getAbsolutePath();
		reportsFileDirPath=excelDir.getAbsolutePath();
		System.out.println(VadFileDirPath);
		this.autoCheck_connect();
		
	}

	/**
	 * Create the frame.
	 */
	public VadJF() {
		
		setTitle("DUILite Vad测试-1.0.7V");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1018, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		init();
		
		JButton button = new JButton("\u6D4B\u8BD5\u97F3\u9891\u8DEF\u5F84");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
//		        chooser.setSelectedFile(new File(VadFileDirPath));
		        chooser.setDialogTitle("选择Vad音频目录");
		        chooser.showDialog(new JLabel(), "选择");//选择Vad音频目录
		        File file = chooser.getSelectedFile();
		        
		        try {
		        	if(file.exists()){
		        		textField.setText(file.getAbsoluteFile().toString());
			        }
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("未选择Vad音频目录");
				}
		        

			}
		});
		button.setBounds(10, 66, 134, 32);
		contentPane.add(button);
		
		JLabel lblvad = new JLabel("\u8BF7\u6DFB\u52A0Vad\u6D4B\u8BD5\u97F3\u9891\u8DEF\u5F84");
		lblvad.setFont(new Font("黑体", Font.PLAIN, 18));
		lblvad.setBounds(10, 23, 293, 32);
		contentPane.add(lblvad);
		
		textField = new JTextField();
		textField.setBounds(169, 67, 478, 32);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
//		StyleConstants.setFontSize(attrset,12);
//		Document docs = textPane.getDocument();
		
		JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(20, 201, 283, 317);
		textArea = new JTextArea();
		textArea.setBounds(20, 201, 452, 317);
		textArea.setLineWrap(true);
		scrollPane_1.setViewportView(textArea);
		contentPane.add(scrollPane_1);
		
		JLabel lblNewLabel = new JLabel("\u6D4B\u8BD5\u65E5\u5FD7");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 169, 59, 22);
		contentPane.add(lblNewLabel);
		
		JButton button_open_tool_dir = new JButton("\u6253\u5F00\u5DE5\u5177\u76EE\u5F55");
		button_open_tool_dir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_open_tool_dir.setEnabled(false);
				
//				String dir ="cmd /C start "+tooldir;
//				
//				Runtime rt = Runtime.getRuntime();
//				
//				try {
//					rt.exec(dir);
//					Thread.sleep(1000);
//				} catch (IOException | InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				File file =new File("config");
				if  (!file.exists()  && !file.isDirectory()) {
				    file.mkdir();
				}
				try {
					FileWriter fwriter = null;
					String strWrite = null;
					fwriter = new FileWriter("config\\opendir.vbs", false);
					strWrite = "Set ws = WScript.CreateObject(\"Wscript.Shell\")\r\n";
					strWrite = strWrite + "ws.run \"\"\""+System.getProperty("user.dir")+"\"\"\"";
					fwriter.write(strWrite);
					fwriter.close();
					
					Runtime rt = Runtime.getRuntime();
					rt.exec("cmd.exe /C config\\opendir.vbs");
					Thread.sleep(3500);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				button_open_tool_dir.setEnabled(true);
			}
		});
		button_open_tool_dir.setFont(new Font("宋体", Font.PLAIN, 10));
		button_open_tool_dir.setBounds(903, 23, 99, 23);
		contentPane.add(button_open_tool_dir);
		
		btn_start = new JButton("\u5F00\u59CB\u6D4B\u8BD5");
		btn_start.setToolTipText("需要连接Android设备");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				btn_start.setEnabled(false);
				if(!installed_pushed){//如果未执行安装/push按钮
					JOptionPane.showMessageDialog(getContentPane(),"请先安装apk并push文件");
					btn_start.setEnabled(true);
				}else {
					button_stop.setEnabled(true);
					
//					testStart=true;
					
					
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							textArea_1.setText("");
							try {
								exeC(cmd+" shell logcat -c");
								String cmd1=cmd+" shell \"logcat -v threadtime | grep -e \"unit_test\" -e \"FATAL\\ EXCEPTION:\"\"";
								exeC_adb_log(cmd1);
//								while(true){
//									
//									String pid=get_test_process();
//									if(pid!=null){
//										System.out.println("pid="+pid);
//										exeC_adb_log(cmd+" shell \"logcat -v threadtime | grep "+pid+"\"");
//										break;
//									}else {
//										Thread.sleep(50);
//									}							
//								}
								
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					

					
					new Thread(new Runnable() {
						public void run() {
							textArea.setText("");
							try {
								exeC(cmd+" shell am instrument -w -r   -e debug false -e class 'unit_test.export.vadtest.AICloudDMEngineTest1#Vad_test1' com.aispeech.sample.test/android.support.test.runner.AndroidJUnitRunner");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
				}
			}
		});
		btn_start.setBounds(10, 127, 134, 32);
		btn_start.setEnabled(false);
		contentPane.add(btn_start);
		
		button_stop = new JButton("\u505C\u6B62\u6D4B\u8BD5");
		button_stop.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				button_stop.setEnabled(false);
//				testStart=false;
				
				
				try {
					Thread thread1=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							end_test_process();
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					thread1.start();
					while(true){
						if(!thread1.isAlive()){
							Runtime.getRuntime().exec(cmd+" shell am instrument -w -r   -e debug false -e class 'unit_test.export.vadtest.AICloudDMEngineTest1#Vad_test_end_stat' com.aispeech.sample.test/android.support.test.runner.AndroidJUnitRunner");
							break;
						}else {
							Thread.sleep(500);
						}
					}
					end_test_process();
//					exeC(cmd+" shell am instrument -w -r   -e debug false -e class 'unit_test.export.vadtest.AICloudDMEngineTest1#Vad_test_end_stat' com.aispeech.sample.test/android.support.test.runner.AndroidJUnitRunner");
					
//					BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd+" shell am instrument -w -r   -e debug false -e class 'unit_test.export.vadtest.AICloudDMEngineTest1#Vad_test_end_stat' com.aispeech.sample.test/android.support.test.runner.AndroidJUnitRunner").getInputStream(),"UTF-8"));
//			        String s = null;
//			        while((s=reader.readLine())!=null){
//			        	System.out.println(s);
//			        }
//					reader.close();

//					Thread.sleep(3000);
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btn_start.setEnabled(true);
			}
		});
		button_stop.setBounds(169, 127, 134, 32);
		button_stop.setEnabled(false);
		contentPane.add(button_stop);
		
		button_generate_report = new JButton("\u9009\u62E9\u65E5\u5FD7\u6587\u4EF6");
		button_generate_report.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        chooser.setCurrentDirectory(new File(logsFileDirPath));
//		        chooser.setSelectedFile(new File(logsFileDirPath));
		        chooser.setDialogTitle("选择测试完毕后的日志文件");
//		        chooser.setApproveButtonText("选择");
//		        chooser.showOpenDialog(null);
		        chooser.showDialog(null, "选择");//选择测试完毕后的日志文件
		        
		        File file = chooser.getSelectedFile();
		        try{
		        	if(file.exists()){
		        		textField_1.setText(file.getAbsoluteFile().toString());
		        	}
		        }catch(NullPointerException e){
		        	System.out.println("未选择日志文件");
		        }
			}
		});
		button_generate_report.setBounds(325, 127, 147, 32);
		contentPane.add(button_generate_report);
		
		button_look_report = new JButton("\u67E5\u770B\u62A5\u544A");
		button_look_report.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_look_report.setEnabled(false);
				if(Recent==null){
					JOptionPane.showMessageDialog(getContentPane(),"最近未生成报告");
					button_look_report.setEnabled(true);
				}else {
					String a=tooldir;
					if(tooldir.contains(" ")){
					    a=tooldir.replaceAll(" ", "\\\" \\\"");
						System.out.println(a);
					}
					String file ="cmd /C start "+a+"\\reports\\"+Recent;
					
					
					Runtime rt = Runtime.getRuntime();
					
					try {
						rt.exec(file);
						Thread.sleep(2000);
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					button_look_report.setEnabled(true);
				}
			}
		});
		button_look_report.setBounds(855, 127, 147, 32);
		button_look_report.setToolTipText("可以查看最近一份生成的报告");
		contentPane.add(button_look_report);
		
		JLabel lblAdb = new JLabel("adb \u65E5\u5FD7");
		lblAdb.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdb.setBounds(325, 169, 59, 22);
		contentPane.add(lblAdb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(325, 201, 677, 317);
		textArea_1 = new JTextArea();
		textArea_1.setBounds(325, 201, 283, 317);
		textArea_1.setLineWrap(true);
		scrollPane.setViewportView(textArea_1);
		contentPane.add(scrollPane);
		
		textField_1 = new JTextField();
		textField_1.setBounds(475, 127, 172, 32);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton button_1 = new JButton("\u751F\u6210\u62A5\u544A");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_1.setEnabled(false);
				if(textField_1.getText().isEmpty()){
					JOptionPane.showMessageDialog(getContentPane(),"未选择日志文件，无法生成报告");
					button_1.setEnabled(true);
				}else {
					if(new File(textField_1.getText()).exists()&&new File(textField_1.getText()).isFile()){
						try {
							scanfile(textField_1.getText());
							System.out.println("日志文件最后一行内容为："+lastlineContent);
							
							if(!lastlineContent.equals("执行结束endStat")){
								System.out.println("在日志文件最后一行增加结束标识");
								BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(textField_1.getText()),true),"UTF-8"));
								String writedate="\n"+"执行结束endStat"+"\n";
					        	writer.append(writedate, 0,writedate.length());
					        	writer.flush();//刷新缓冲区
					        	writer.close();
					        	scanfile(textField_1.getText());
					        	System.out.println("增加结束标识完毕后，内容为："+lastlineContent);
							}
							
//							System.out.println(file_name_list.get(0));
//							System.out.println(vad_startTimelist.get(0));
//							System.out.println(vad_end_notpauseTime_Timelist.get(0));
//							System.out.println(vad_endTimelist.get(0));
//							System.out.println(asrIdentify_Resultlist.get(0));
							long time=new Date().getTime();
							String filename=dateFormat.format(time)+".xlsx";
							Recent=filename;
							create_excel(filename);
							read_excel(filename);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						textArea_1.append("选择日志文件："+textField_1.getText()+"生成报告完毕！"+"\n");
			        	textArea_1.paintImmediately(textArea_1.getBounds());
			        	textArea_1.setCaretPosition(textArea_1.getText().length());  
						button_1.setEnabled(true);
						textField_1.setText(null);
					}else {
						JOptionPane.showMessageDialog(getContentPane(),"文件不存在/不是文件");
						button_1.setEnabled(true);
					}
					
				}
			}
		});
		button_1.setBounds(682, 127, 147, 32);
		contentPane.add(button_1);
		
		lblNewLabel_1 = new JLabel("\u8BBE\u5907\u662F\u5426\u8FDE\u63A5\uFF1A");
		lblNewLabel_1.setToolTipText("\u8BBE\u5907\u8FDE\u63A5\u72B6\u6001");
		lblNewLabel_1.setBounds(10, 101, 134, 22);
		contentPane.add(lblNewLabel_1);
		
		lblDevices = new JLabel("deviceID\uFF1A");
		lblDevices.setToolTipText("\u9ED8\u8BA4\u5728\u7B2C\u4E00\u4E2A\u8BBE\u5907\u4E0A\u8FDB\u884C\u6D4B\u8BD5");
		lblDevices.setBounds(169, 101, 134, 22);
		contentPane.add(lblDevices);
		
		lblAndroid = new JLabel("Android\uFF1A");
		lblAndroid.setToolTipText("Adnroid\u7248\u672C");
		lblAndroid.setBounds(325, 101, 134, 22);
		contentPane.add(lblAndroid);
		
		btnNewButton = new JButton("\u5B89\u88C5apk\u5E76push\u97F3\u9891");
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 11));
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNewButton.setEnabled(false);
				if(textField.getText().isEmpty()){
					JOptionPane.showMessageDialog(getContentPane(),"未添加测试音频所在目录路径，请设置测试音频所在目录路径");
					btnNewButton.setEnabled(true);
				}else {
					textArea.setText(""); 
//					textArea.paintImmediately(textArea.getBounds());
//					textArea.setCaretPosition(textArea.getText().length());
					Check_appInstall();
				}
			}
		});
		btnNewButton.setBounds(682, 66, 147, 32);
		contentPane.add(btnNewButton);
		
		button_2 = new JButton("\u8F85\u52A9\u5DE5\u5177");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					if(helpers_isExsit){
						System.out.println("已存在辅助工具");
					}else {
						System.out.println("打开辅助工具");
						helpers=new Helpers();
						helpers_isExsit=true;
						
						helpers.addWindowListener(new WindowListener() {
							
							@Override
							public void windowOpened(WindowEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void windowIconified(WindowEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void windowDeiconified(WindowEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void windowDeactivated(WindowEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void windowClosing(WindowEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void windowClosed(WindowEvent e) {
								// TODO Auto-generated method stub
								helpers_isExsit=false;
							}
							
							@Override
							public void windowActivated(WindowEvent e) {
								// TODO Auto-generated method stub
								
							}
						});
						helpers.setLocation(VadJF.this.getX(), VadJF.this.getY());
						helpers.setVisible(true);
					}
				
			}
		});
		button_2.setToolTipText("\u7528\u6765\u5206\u7C7B\u97F3\u9891\u6587\u4EF6");
		button_2.setBounds(855, 66, 147, 32);
		contentPane.add(button_2);
		
		button_3 = new JButton("<");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectTestUI.Selectframe.setLocation(VadJF.this.getX(), VadJF.this.getY());
				SelectTestUI.Selectframe.setVisible(true);
				
				VadJF.this.dispose();
				
				
			}
		});
		button_3.setBounds(10, 0, 25, 22);
		contentPane.add(button_3);
	}
	
	
	public void scanfile(String filepath_name) throws IOException{
		File file=new File(filepath_name);
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		String strRead = null;
//		int count=0;
		while((strRead = reader.readLine())!= null){
//			count++;
//			System.out.println("第"+count+"行为:"+strRead);
			
			
			if(strRead.contains("unit_test.export.vadtest.AICloudDMListenerTest1: onInit: 初始化成功!")){
				file_name_list.clear();
				feed_file_timelist.clear();
				vad_startTimelist.clear();
				vad_end_notpauseTime_Timelist.clear();
				vad_endTimelist.clear();
				asrIdentify_Resultlist.clear();
			}
			
			
			if(strRead.contains("开始喂第")){
				String filename=strRead.substring(strRead.lastIndexOf("：")+1);
//				System.out.println(filename);
				file_name_list.add(filename);
			}
			
			if(strRead.contains("开始feed音频的时间为:")){
				String feed_file_time=strRead.substring(strRead.indexOf("开始feed音频的时间为:")+("开始feed音频的时间为:").length(),strRead.indexOf(","));
//				System.out.println(feed_file_time);
				feed_file_timelist.add(feed_file_time);
			}
			
			if(strRead.contains("此时vadstart在wav中位置大约是：")){
				if(strRead.contains("此时vadstart在wav中位置大约是：null")){
					String vadStartTime="null";
//					System.out.println(vadStartTime);
					vad_startTimelist.add(vadStartTime);
				}else {
					String vadStartTime=strRead.substring(strRead.indexOf("此时vadstart在wav中位置大约是：")+("此时vadstart在wav中位置大约是：").length());
					float vadStart=Float.parseFloat(vadStartTime)-300;
//					System.out.println(vadStartTime);
					vad_startTimelist.add(vadStart+"");
				}
			}
			
			if(strRead.contains("此时vadend在wav中位置大约是：")){
				if(strRead.contains("此时vadend在wav中位置大约是：null")){
					String vadEndTime="null";
//					System.out.println(vadEndTime);
					vad_end_notpauseTime_Timelist.add(vadEndTime);
					vad_endTimelist.add(vadEndTime);
				}else {
					String vadEndTime=strRead.substring(strRead.indexOf("此时vadend在wav中位置大约是：")+("此时vadend在wav中位置大约是：").length());
					float vadEnd_noPausetime=Float.parseFloat(vadEndTime)-425;
//					System.out.println(vadEndTime);
					vad_end_notpauseTime_Timelist.add(vadEnd_noPausetime+"");
					vad_endTimelist.add(vadEndTime);
				}
			}
			
			if(strRead.contains("最终识别结果onAsr：")){
				String AsrIdentify_Result=strRead.substring(strRead.indexOf("最终识别结果onAsr：")+("最终识别结果onAsr：").length());
//				System.out.println(AsrIdentify_Result);
				asrIdentify_Resultlist.add(AsrIdentify_Result);
			}
			
			if(strRead.contains("执行结束endStat")){
				System.out.println("file_name_list:"+file_name_list.size());
				System.out.println("feed_file_timelist:"+feed_file_timelist.size());
				System.out.println("vad_startTimelist:"+vad_startTimelist.size());
				System.out.println("vad_end_notpauseTime_Timelist:"+vad_end_notpauseTime_Timelist.size());
				System.out.println("vad_endTimelist:"+vad_endTimelist.size());
				System.out.println("asrIdentify_Resultlist:"+asrIdentify_Resultlist.size());
				if(file_name_list.size()>feed_file_timelist.size()){
					System.out.println("file_name_list.size()>feed_file_timelist.size()");
					for(int i=0;i<file_name_list.size()-feed_file_timelist.size();i++){
						feed_file_timelist.add("null");
					}
					System.out.println("添加完毕后feed_file_timelist长度为："+feed_file_timelist.size());
				}
				if(file_name_list.size()>vad_startTimelist.size()){
					System.out.println("file_name_list.size()>vad_startTimelist.size()");
					for(int i=0;i<file_name_list.size()-vad_startTimelist.size();i++){
						vad_startTimelist.add("null");
					}
					System.out.println("添加完毕后vad_startTimelist长度为："+vad_startTimelist.size());
				}
				
				if(vad_startTimelist.size()>vad_end_notpauseTime_Timelist.size()){
					System.out.println("vad_startTimelist.size()>vad_end_notpauseTime_Timelist.size()");
					for(int i=0;i<vad_startTimelist.size()-vad_end_notpauseTime_Timelist.size();i++){
						vad_end_notpauseTime_Timelist.add("null");
					}
					System.out.println("添加完毕后vad_end_notpauseTime_Timelist长度为："+vad_end_notpauseTime_Timelist.size());
				}
				
				if(vad_startTimelist.size()>vad_endTimelist.size()){
					System.out.println("vad_startTimelist.size()>vad_endTimelist.size()");
					for(int i=0;i<vad_startTimelist.size()-vad_endTimelist.size();i++){
						vad_endTimelist.add("null");
					}
					System.out.println("添加完毕后vad_endTimelist长度为："+vad_endTimelist.size());
				}
				
				if(vad_startTimelist.size()>asrIdentify_Resultlist.size()){
					System.out.println("vad_startTimelist.size()>asrIdentify_Resultlist.size()");
					for(int i=0;i<vad_startTimelist.size()-asrIdentify_Resultlist.size();i++){
						asrIdentify_Resultlist.add("null");
					}
					System.out.println("添加完毕后asrIdentify_Resultlist长度为："+asrIdentify_Resultlist.size());
				}
				
			}
			
			lastlineContent=strRead;
			
			
		}
		reader.close();
	}
	
	//创建excel
	public void create_excel(String filename) throws Exception{
		View m_view = new View();
		 //设置sheet页数
        m_view.setNumSheets(1);
        //设置sheet页的名称
        m_view.setSheetName(0,"测试报告");
        //写入第1个页数据
        m_view.setSheet(0);
        
        //标题 setTextAsValue(行，列，值)；
        m_view.setTextAsValue(0,0,"编号");
        m_view.setTextAsValue(0,1,"音频文件名称");
        m_view.setTextAsValue(0,2,"音频文件时长(单位:ms)");
        m_view.setTextAsValue(0,3,"开始feed音频时间点");
        m_view.setTextAsValue(0,4,"在音频内vadstart大约位置(单位：ms)");//本身算出来的vadstart是包含了pauseTime的时间（默认为300，没有更改pausetime时间），所以要减去300ms
        m_view.setTextAsValue(0,5,"在音频内vadend不包含pauseTime的时间，大约位置(单位：ms)");//因为默认是300ms的pauseTime时间，由延时，300所对应的延时是425ms，所以要减去425ms
        m_view.setTextAsValue(0,6,"在音频内vadend大约位置(单位：ms)");
        m_view.setTextAsValue(0,7,"最终识别结果");
        
        
        m_view.setColWidth(0, 1216);//设置第一列的自定义单元格行宽
        m_view.setColWidth(1, 7040);//设置第一列的自定义单元格行宽
        m_view.setColWidth(2, 6400);//设置第一列的自定义单元格行宽
        m_view.setColWidth(3, 5504);//设置第一列的自定义单元格行宽
        m_view.setColWidth(4, 7859);//设置第一列的自定义单元格行宽
        m_view.setColWidth(5, 8448);//设置第一列的自定义单元格行宽
        m_view.setColWidth(6, 7680);//设置第一列的自定义单元格行宽
        m_view.setColWidth(7, 7680);//设置第一列的自定义单元格行宽
        
        m_view.writeXLSX(reportsFileDirPath+"\\"+filename);
	}
	
	
	
	public void read_excel(String filename) throws Exception{
		View m_view = new View();
		m_view.readXLSX(reportsFileDirPath+"\\"+filename);
		m_view.setSheet(0);

		for(int i=0;i<file_name_list.size();i++){
			m_view.setTextAsValue(1+i,0,(1+i+""));
			m_view.setTextAsValue(1+i,1,file_name_list.get(i));
			try {
				m_view.setTextAsValue(1+i,2,file_timeLong_list.get(i));
			} catch (Exception e) {
				// TODO: handle exception
				m_view.setTextAsValue(1+i,2,"未执行测试，未获取音频时长");
			}
			
			m_view.setTextAsValue(1+i,3,feed_file_timelist.get(i));//喂音频时间点

			if(vad_startTimelist.get(i)=="null"){
				m_view.setTextAsValue(1+i,4,"null");
			}else {
				m_view.setTextAsValue(1+i,4,vad_startTimelist.get(i));
			}
			if(vad_end_notpauseTime_Timelist.get(i)=="null"){
				m_view.setTextAsValue(1+i,5,"null");
			}else {
				m_view.setTextAsValue(1+i,5,vad_end_notpauseTime_Timelist.get(i));
			}
			
			if(vad_endTimelist.get(i)=="null"){
				m_view.setTextAsValue(1+i,6,"null");
			}else {
				m_view.setTextAsValue(1+i,6,vad_endTimelist.get(i));
			}
			m_view.setTextAsValue(1+i,7,asrIdentify_Resultlist.get(i));
		}

		m_view.writeXLSX(reportsFileDirPath+"\\"+filename);
	}	
}
