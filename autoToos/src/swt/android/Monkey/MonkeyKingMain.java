package swt.android.Monkey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;

import java.lang.reflect.Field;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;



public class MonkeyKingMain extends ApplicationWindow {
	Display display = new Display(); 
	boolean log = false;
	Process prlog = null;
	Process prroot = null;
	boolean rootOK = false;
	boolean testrunning = false;
	Text text_package;
	Text text_param;
	String[] project = new String[100]; //项目名称
	String[] packname = new String[100]; //测试包名
	String[] param = new String[100]; //其他参数
	String currentNode = "";
	
	String strCmd = ""; //当前准备执行的命令
	String strLogno = "";
	String strTime = ""; //当前时间文件夹
	String strTime2 = ""; //当前时间文件夹(logcat日志分段保存用)
	
	String currentDir = "."; //log解析路径
	
	int num = 0; //测试项目的个数
	
	
	private static void killProcessTree(Process process) {
		try {
			Field f = process.getClass().getDeclaredField("handle");
			f.setAccessible(true);
			long handl = f.getLong(process);
			Kernel32 kernel = Kernel32.INSTANCE;
			WinNT.HANDLE handle = new WinNT.HANDLE();
			handle.setPointer(Pointer.createConstant(handl));
			int ret = kernel.GetProcessId(handle);
			Long PID = Long.valueOf(ret);
			Runtime rt = Runtime.getRuntime();
			Process prkillproc = rt.exec("taskkill /pid "+PID+" /t /f");
			prkillproc.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the application window.
	 */
	public MonkeyKingMain() {
		super(null);
		setShellStyle(SWT.MIN);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		final Button btn_runApkGetPname = new Button(container, SWT.NONE);
		btn_runApkGetPname.setEnabled(false);
		
		final Button btn_startMonkey = new Button(container, SWT.NONE);
		btn_startMonkey.setEnabled(false);
		final Button btn_stopMonkey = new Button(container, SWT.NONE);
		btn_stopMonkey.setEnabled(false);
		final Button btn_parseMonkey = new Button(container, SWT.NONE);
		
		
		//Monkey测试命令框
		final Combo combo = new Combo(container, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = combo.getSelectionIndex();
				text_package.setText(packname[i]);
				text_param.setText(param[i]);
			}
		});
		combo.setBounds(123, 47, 189, 25);
		
		
		File conffile = new File("config\\config.txt"); //要读取的文件
		if(!conffile.exists()) {
		    try {
		    	conffile.createNewFile();
		    } catch (IOException e) {
		    }
		}
		BufferedReader reader = null;
		String strRead = null;
		
		try {
			int beginIndex=-1, endIndex=-1;
			reader = new BufferedReader(new FileReader(conffile));
			while ((strRead = reader.readLine()) != null) { //每次读一行，直到文件结尾
				strRead = strRead.trim();
				switch(currentNode) {
				case "<PACKAGE>":
					if( packname[num]==null ){
						packname[num] = "";
					}
					if( !strRead.startsWith("<") && !strRead.startsWith("=") ){
						packname[num] = packname[num] + strRead + "\r\n";
					}
					break;
				case "<PARAM>":
					if( param[num]==null ){
						param[num] = "";
					}
					if( !strRead.startsWith("<") && !strRead.startsWith("=") ){
						param[num] = param[num] + strRead + "\r\n";
					}
					break;
				default:
					break;
				}
				
				beginIndex = strRead.indexOf("[");
				if( beginIndex != -1 ){
					endIndex = strRead.indexOf("]");
					project[num] = strRead.substring(beginIndex+1, endIndex);
				}else{
					beginIndex = strRead.indexOf("<PACKAGE>");
					if( beginIndex != -1 ){
						currentNode = "<PACKAGE>";
					}else{
						beginIndex = strRead.indexOf("<PARAM>");
						if( beginIndex != -1 ){
							currentNode = "<PARAM>";
						}else{
							beginIndex = strRead.indexOf("<LOGYES>");
							if( beginIndex != -1 ){
								currentNode = "<LOGYES>";
							}else{
								beginIndex = strRead.indexOf("<LOGNO>");
								if( beginIndex != -1 ){
									currentNode = "<LOGNO>";
								}else{
									beginIndex = strRead.indexOf("===");
									if( beginIndex != -1 ){
										currentNode = "";
										num++;
									}
								}
							}
						}
					}
				}
				
			}
			reader.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int f=0; f<num; f++) //载入测试项目列表
			combo.add(project[f], f);
		
		
		//检测机器连接按钮
		final Button button_connDevice = new Button(container, SWT.NONE);
		button_connDevice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				button_connDevice.setEnabled(false);
				btn_startMonkey.setEnabled(false);
				btn_stopMonkey.setEnabled(false);
            	btn_runApkGetPname.setEnabled(false);
            	rootOK = false;
            	
				try {
		            final Runtime rt = Runtime.getRuntime();
		            Process pr = null;
		            BufferedReader input = null;
		            String line = null; //接收cmd执行结果
		            String strName = "";
		            
		            pr = rt.exec("adb devices"); //注意转义符
		            input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
		            input.readLine(); //过滤第一行List of devices attached
		            int r = 0;
		            while ( (line=input.readLine()) != null ) {
		            	if(!line.equals("")){
		            		strName = line.substring(0, line.indexOf("\t"));
		            		r++;
		            	}
		            }
		            if(r==0){
		            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
		            	messageBox.setMessage("机器未连接！");
		            	messageBox.open();
		            }
		            else if(r==1){
		            	
		            	//尝试执行root，可能阻塞无返回
						new Thread (new Runnable() {
		                    public void run() {
		                    	Runtime rt = Runtime.getRuntime();
		                    	rootOK = false;
		                    	try {
		                    		prroot = rt.exec("adb root");
		                    		prroot.waitFor();
		                    		rootOK = true;
		                    		
								} catch (Exception e) {
								}
		                    }
		                }).start();
						
						//结束root进程
						new Thread (new Runnable() {
		                    public void run() {
		                    	try {
									Thread.sleep(8000);
								} catch (InterruptedException e) {
								}
		                    	prroot.destroy();
		                    	
		                    }
		                }).start();
						
		            	pr = rt.exec("adb shell \"getprop ro.build.version.release\""); //查询安卓版本号
			            input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
			            line=input.readLine();
//			            androidVersion = Integer.parseInt(line.substring(0, line.indexOf(".")));
		            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
		            	messageBox.setMessage("机器已连接，设备号为 " + strName + "\r\n安卓版本号为 "+line);
		            	messageBox.open();
		            	
		            	//只要机器连接就可以进行的操作
						btn_startMonkey.setEnabled(true);
						btn_stopMonkey.setEnabled(true);
		            	btn_runApkGetPname.setEnabled(true);
		            	
		            }
		            else {
		            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
		            	messageBox.setMessage("多台设备连接，请断开其他设备！");
		            	messageBox.open();
		            }
		            
		        } catch (Exception ex) {
		            button_connDevice.setEnabled(true);
		        }
				
				button_connDevice.setEnabled(true);
			}
		});
		button_connDevice.setBounds(28, 10, 95, 27);
		button_connDevice.setText("\u68C0\u6D4B\u673A\u5668\u8FDE\u63A5");
		button_connDevice.setFocus();
		
		Label lblMonkey = new Label(container, SWT.NONE);
		lblMonkey.setText("测试项目列表：");
		lblMonkey.setBounds(30, 50, 89, 17);
		
		
		//运行Monkey测试
		btn_startMonkey.setText("启动测试");
		btn_startMonkey.setEnabled(false);
		btn_startMonkey.setBounds(223, 10, 89, 27);
		
		
		//停止Monkey测试
		btn_stopMonkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				btn_stopMonkey.setEnabled(false);
				log = false;
				
				try {
		            final Runtime rt = Runtime.getRuntime();
		            Process pr = null;
		            String line = null;
		            String strLine = "";
		            String[] dateLine= new String[10];
		            BufferedReader input = null;
		            String[] dateAfterSplit= new String[15];
		            
		            pr = rt.exec("adb shell \"ps | grep monkey\"");
		            input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
		            while ( (line=input.readLine()) != null ) {
		            	if(!line.equals("")){
		            		strLine = strLine + line.trim()+"\r\n";
		            	}
		            }
		            dateLine = strLine.split("\r\n");
		            for(int i=0;i<dateLine.length;i++){
		            	if( dateLine[i]==null || dateLine[i].equals("") ){
			            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
			            	messageBox.setMessage("当前未运行Monkey测试！");
			            	messageBox.open();
			            }else{
			            	if(testrunning){ //如果软件打开后，启动了monkey测试
			            		pr = rt.exec("adb pull /data/tombstones .\\MonkeyTest_"+strTime+"\\tombstones");
					            pr.waitFor();
//					            pr = rt.exec("adb pull /data/system/dropbox/ .\\MonkeyTest_"+strTime+"\\dropbox\\");
//					            pr.waitFor();
					            pr = rt.exec("adb pull /data/anr .\\MonkeyTest_"+strTime+"\\traces");
					            pr.waitFor();
			            	}
			            	
			            	dateAfterSplit = dateLine[i].split("\\s+");
				            pr = rt.exec("adb shell kill " + dateAfterSplit[1] );
				            pr.waitFor();
				            if( i==dateLine.length-1 ){
				            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				            	messageBox.setMessage("Monkey测试已停止！");
				            	messageBox.open();
				            }
			            }
		            }
		            
			        
		        } catch (Exception ex) {
//		            ex.printStackTrace();
		        }
				
				testrunning = false;
				btn_stopMonkey.setEnabled(true);
				btn_startMonkey.setEnabled(true);
			}
		});
		btn_stopMonkey.setText("停止测试");
		btn_stopMonkey.setEnabled(false);
		btn_stopMonkey.setBounds(325, 10, 89, 27);
		
		
		//查看Apk包名
		final Button btn_selApkGetPname = new Button(container, SWT.NONE);
		btn_selApkGetPname.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_selApkGetPname.setEnabled(false);
				
				String currentDir = ".";
		    	JFileChooser jfc=new JFileChooser(currentDir);
		    	FileNameExtensionFilter filter = new FileNameExtensionFilter("apk", "apk"); //("JPG & GIF & txt", "jpg", "gif","txt")
		    	jfc.setFileFilter(filter);
		    	int returnVal = jfc.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
				    currentDir = jfc.getCurrentDirectory()+""; //记录路径
				    if(!currentDir.endsWith("\\"))
						currentDir = currentDir + "\\";
				    currentDir = jfc.getSelectedFile() + ""; //记录路径或“路径+文件名”
				}else{
					btn_selApkGetPname.setEnabled(true);
					return; //用户点“取消”则直接返回
				}
		        
				String[] str = null;
		        try {  
		            str = AnalysisApk.unZip( currentDir, "");
		        } catch (Exception ex) {
		            ex.printStackTrace();  
		        }
		        
		        MessageBox messageBox = new MessageBox( getShell(), SWT.OK | SWT.CANCEL );
            	messageBox.setMessage("  Apk包名："+str[0]+"\r\n  您可以点击【确定】按钮复制包名到剪切板！");
            	int rc = messageBox.open();
            	if (rc == SWT.OK) {
            		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            		StringSelection text1 = new StringSelection(str[0]);
            		clipboard.setContents(text1,null);
            	}
				
				btn_selApkGetPname.setEnabled(true);
			}
		});
		btn_selApkGetPname.setBounds(123, 378, 103, 27);
		btn_selApkGetPname.setText(".apk文件查包名");
		
		//运行Apk查看包名
		btn_runApkGetPname.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_runApkGetPname.setEnabled(false);
				
				MessageBox messageBox = new MessageBox( getShell(), SWT.OK | SWT.CANCEL );
            	messageBox.setMessage("  请在设备中打开Apk，使其保持在最前端，然后点击【确定】按钮");
            	int rc = messageBox.open();
            	if (rc == SWT.OK) {
            		Runtime rt = Runtime.getRuntime();
		            Process pr = null;
		            String line = null;
		            String strLine = "";
		            BufferedReader input = null;
		            int s = -1;
		            String packName = "";
                	try {
						pr = rt.exec("adb shell \"dumpsys activity | grep mF\""); //注意转义符
						input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
			            while ( (line=input.readLine()) != null ) {
			            	if(!line.equals("")){
			            		strLine = strLine + line.trim()+"\r\n";
			            	}
			            }
			            s = strLine.indexOf("u0 ");
			            if( s != -1 ){
			            	packName = strLine.substring(s+3, strLine.indexOf("/", s));
			            	MessageBox mb = new MessageBox( getShell(), SWT.OK | SWT.CANCEL );
			            	mb.setMessage("  Apk包名："+packName+"\r\n  您可以点击【确定】按钮复制包名到剪切板！");
			            	int mbrc = mb.open();
			            	if (mbrc == SWT.OK) {
			            		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			            		StringSelection text1 = new StringSelection(packName);
			            		clipboard.setContents(text1,null);
			            	}
			            }else{
			            	MessageBox messageBox2 = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
			            	messageBox2.setMessage("包名查询失败！");
			            	messageBox2.open();
			            	return;
			            }
			            
			            
					} catch (IOException ex) {
						ex.printStackTrace();
					}
                
            	}
            	btn_runApkGetPname.setEnabled(true);
			}
		});
		btn_runApkGetPname.setText("运行apk查包名");
		btn_runApkGetPname.setBounds(245, 378, 103, 27);
		
		//Monkey运行按钮
		btn_startMonkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				btn_startMonkey.setEnabled(false);
				
				if( (combo.getText()).equals("") ){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	            	messageBox.setMessage("请选择一个测试项目！");
	            	messageBox.open();
	            	btn_startMonkey.setEnabled(true);
	            	return;
				}
				
				testrunning = true;
				log = true;
				
				strCmd = "adb shell monkey";
				String[] dateAfterSplit= new String[100];
				dateAfterSplit=(text_package.getText()).split("\r\n");
				for(int i=0; i<dateAfterSplit.length; i++)
					strCmd = strCmd + " -p " + dateAfterSplit[i];
				dateAfterSplit = null;
				dateAfterSplit= new String[100];
				dateAfterSplit=(text_param.getText()).split("\r\n");
				for(int i=0; i<dateAfterSplit.length; i++){
					strCmd = strCmd + " " + dateAfterSplit[i];
				}
					
				
				Date date=new Date();
        		DateFormat format=new SimpleDateFormat("yyMMdd_HHmmss");
        		strTime = format.format(date);
        		strTime2 = strTime;
        		
        		File file=new File("MonkeyTest_"+strTime); //创建日志文件夹
        		if(!file.exists()  && !file.isDirectory()) {
        		    try {
        		        file.mkdir();
        		    } catch (Exception ex) {
        		        ex.printStackTrace();
        		    }
        		}
        		currentDir = "MonkeyTest_"+strTime;
				
				
				//执行Monkey测试和抓取Monkey log
				new Thread (new Runnable() {
                    public void run() { //新线程执行内容
                    	Runtime rt = Runtime.getRuntime();
    		            BufferedReader input = null;
    		            String line = null;
    		            FileWriter fwriter = null;
                    	try {
                    		if(rootOK){ //如果已执行过root，不管是否成功
                    			prlog = rt.exec("adb shell rm -rf /data/tombstones/*");
                    			prlog.waitFor();
                    			prlog = rt.exec("adb shell rm -rf /data/anr/*");
                    			prlog.waitFor();
                    		}
                    		
                    		Date logdate;
                    		DateFormat logformat;
                    		prlog = rt.exec(strCmd);//执行Monkey测试指令
							input = new BufferedReader(new InputStreamReader(prlog.getInputStream(), "UTF-8")); //GBK
							while (log) {
				            	line=input.readLine();
				            	if( line!=null && !line.equals("") ){
				            		logdate = new Date();
				            		logformat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
				            		fwriter = new FileWriter("MonkeyTest_"+strTime+"\\monkeylog_"+strTime+".txt", true);
				    				fwriter.write(logformat.format(logdate)+" "+line+"\r\n");
				        			fwriter.close();
				            	}
				            }
							killProcessTree(prlog); //结束阻塞的命令
							prlog.destroy(); //和上一句连用
							
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }//新线程执行内容结束
                }).start();
				
				
				MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
            	messageBox.setMessage("Monkey测试已启动！");
            	messageBox.open();
            	
				
			}
		});
		
		text_package = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_package.setBounds(123, 82, 295, 286);
		
		Label label = new Label(container, SWT.NONE);
		label.setText("测试包名：");
		label.setBounds(53, 82, 65, 17);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("包名快捷查询：");
		label_1.setBounds(28, 383, 89, 17);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("其他参数：");
		label_2.setBounds(443, 82, 65, 17);
		
		text_param = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_param.setBounds(514, 82, 230, 286);
		
		//保存当前设置
		final Button btn_saveProj = new Button(container, SWT.NONE);
		btn_saveProj.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_saveProj.setEnabled(false);
				
				String projname = combo.getText().trim();
				if(projname.equals("")){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	            	messageBox.setMessage("测试项目不能为空！");
	            	messageBox.open();
	            	btn_saveProj.setEnabled(true);
	            	return;
				}
				
				boolean projexsit = false;
				for(int f=0; f<num; f++){
					if( projname.equals(project[f]) ){
						projexsit = true;
					}
				}
				
				if( projexsit ){
					MessageBox messageBox = new MessageBox( getShell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO ); //new Shell(new Display())
					messageBox.setMessage("确认将当前设置保存到 "+projname+" 测试项目？");
					int rc = messageBox.open();
					if (rc == SWT.YES) {
						int i = combo.getSelectionIndex();
						packname[i] = text_package.getText().trim()+"\r\n";
						param[i] = text_param.getText().trim()+"\r\n";
					}
				}else{
					MessageBox messageBox = new MessageBox( getShell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO ); //new Shell(new Display())
					messageBox.setMessage("确认新建 "+projname+" 测试项目，\r\n并将当前设置保存到此项目？");
					int rc = messageBox.open();
					if (rc == SWT.YES) {
						project[num] = projname;
						packname[num] = text_package.getText().trim()+"\r\n";
						param[num] = text_param.getText().trim()+"\r\n";
						combo.add(project[num], num);
						num++;
					}
				}
				
				
				try {
					FileWriter fwriter = null;
					fwriter = new FileWriter("config\\config.txt", false);
					for(int f=0; f<num; f++){
						fwriter.write("["+project[f]+"]"+"\r\n<PACKAGE>\r\n");
						fwriter.write(packname[f]);
						if(!packname[f].endsWith("\r\n")){
							fwriter.write("\r\n");
						}
						fwriter.write("<PARAM>\r\n"+param[f]);
						if(!param[f].endsWith("\r\n")){
							fwriter.write("\r\n");
						}
						fwriter.write("=========================\r\n");
					}
					fwriter.close();
					
				} catch (Exception e1) {
				}
				
				
				btn_saveProj.setEnabled(true);
			}
		});
		btn_saveProj.setText("保存当前设置");
		btn_saveProj.setBounds(325, 45, 89, 27);
		
		//解析日志结果
		btn_parseMonkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_parseMonkey.setEnabled(false);
				
		    	JFileChooser jfc=new JFileChooser(currentDir);
		    	jfc.setDialogTitle("请选择 monkeylog 日志文件");
		    	FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt"); //限定文件格式("JPG & GIF & txt", "jpg", "gif","txt")
		    	jfc.setFileFilter(filter);
		    	jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
		    		public boolean accept(File f) { //设定可用的文件的后缀名
		    			if(f.getName().startsWith("monkeylog")||f.isDirectory()){
		    				return true;
		    			}
		    			return false;
		    		}
		    		public String getDescription() {
		    			return "";
		    		}
		    		});
		    	int returnVal = jfc.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    currentDir = jfc.getCurrentDirectory()+""; //记录路径
				    if(!currentDir.endsWith("\\"))
						currentDir = currentDir + "\\";
				}else{
					btn_parseMonkey.setEnabled(true);
					return; //用户点“取消”则直接返回
				}
				
				//解析日志中的关键字段
				File file = new File(jfc.getSelectedFile() + "");
				BufferedReader reader = null;
				FileWriter fwriter1 = null;
				FileWriter fwriter2 = null;
				FileWriter fwriter3 = null;
				FileWriter fwriter4 = null;
				String strRead = null;
				boolean checkAnr = false;
				boolean checkCrash = false;
				boolean checkException = false;
				boolean checkDied = false;
				
				try {
					//为避免重复累积记录，如果存在这些文件就删除之
					File filedel = new File(currentDir+"\\monkeyParse_ANR.txt");
					if(file.exists()) {
						filedel.delete();
					}
					filedel = new File(currentDir+"\\monkeyParse_CRASH.txt");
					if(file.exists()) {
						filedel.delete();
					}
					filedel = new File(currentDir+"\\monkeyParse_Exception.txt");
					if(file.exists()) {
						filedel.delete();
					}
					filedel = new File(currentDir+"\\monkeyParse_Died.txt");
					if(file.exists()) {
						filedel.delete();
					}
					
					reader = new BufferedReader(new FileReader(file));
					while ((strRead = reader.readLine()) != null) {
						if(strRead.contains("ANR in")){
							checkAnr = true;
							fwriter1 = new FileWriter(currentDir+"\\monkeyParse_ANR.txt", true);
							fwriter1.write(strRead+"\r\n");
							fwriter1.close();
						}else if(strRead.contains("CRASH")){
							checkCrash = true;
							fwriter2 = new FileWriter(currentDir+"\\monkeyParse_CRASH.txt", true);
							fwriter2.write(strRead+"\r\n");
							fwriter2.close();
						}else if(strRead.contains("Exception")){
							checkException = true;
							fwriter3 = new FileWriter(currentDir+"\\monkeyParse_Exception.txt", true);
							fwriter3.write(strRead+"\r\n");
							fwriter3.close();
						}else if(strRead.contains("deid")){
							checkDied = true;
							fwriter4 = new FileWriter(currentDir+"\\monkeyParse_Died.txt", true);
							fwriter4.write(strRead+"\r\n");
							fwriter4.close();
						}
					}
					reader.close();
					
					String res = "";
					if(!checkAnr && !checkCrash && !checkException && !checkDied){ //没有任何错误
						res = "Monkeylog 解析完毕，结果正常！";
					}else{
						res = "Monkeylog 解析完毕，结果存在以下异常：\r\n";
						if(checkAnr){
							res = res + "ANR, ";
						}
						if(checkCrash){
							res = res + "CRASH, ";
						}
						if(checkException){
							res = res + "Exception, ";
						}
						if(checkDied){
							res = res + "Deid, ";
						}
						res = res.substring(0, res.length()-2);
					}
					
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	            	messageBox.setMessage(res);
	            	messageBox.open();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
				btn_parseMonkey.setEnabled(true);
			}
		});
		btn_parseMonkey.setText("解析结果");
		btn_parseMonkey.setBounds(425, 10, 89, 27);
		
		Label label_5 = new Label(container, SWT.NONE);
		label_5.setBounds(428, 420, 330, 17);
		label_5.setText("fengyang.li © 版权所有（fengyang.li制作）");
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(420, 50, 276, 17);
		lblNewLabel.setText("（如果要新增测试项目，可修改项目名称后再保存）");
		
		
		//打开工具根目录按钮
		final Button btn_openDir = new Button(container, SWT.NONE);
		btn_openDir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_openDir.setEnabled(false);
				
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
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				btn_openDir.setEnabled(true);
			}
		});
		btn_openDir.setText("打开工具目录");
		btn_openDir.setBounds(649, 10, 95, 27);
		
		Label lblmonkey = new Label(container, SWT.NONE);
		lblmonkey.setText("获取Monkey指令：");
		lblmonkey.setBounds(425, 383, 103, 17);
		
		
		//获取完整Monkey命令
		final Button btn_getMonkeyCmd = new Button(container, SWT.NONE);
		btn_getMonkeyCmd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_getMonkeyCmd.setEnabled(false);
				
				if( (combo.getText()).equals("") ){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	            	messageBox.setMessage("请选择一个测试项目！");
	            	messageBox.open();
	            	btn_getMonkeyCmd.setEnabled(true);
	            	return;
				}
				
				strCmd = "adb shell monkey";
				String[] dateAfterSplit= new String[100];
				dateAfterSplit=(text_package.getText()).split("\r\n");
				for(int i=0; i<dateAfterSplit.length; i++)
					strCmd = strCmd + " -p " + dateAfterSplit[i];
				dateAfterSplit = null;
				dateAfterSplit= new String[100];
				dateAfterSplit=(text_param.getText()).split("\r\n");
				for(int i=0; i<dateAfterSplit.length; i++){
					strCmd = strCmd + " " + dateAfterSplit[i];
				}
				MessageBox messageBox = new MessageBox( getShell(), SWT.OK | SWT.CANCEL );
            	messageBox.setMessage("Monkey命令如下：\r\n"+strCmd+"\r\n\r\n您可以点击【确定】按钮复制包名到剪切板！");
            	int rc = messageBox.open();
            	if (rc == SWT.OK) {
            		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            		StringSelection text1 = new StringSelection(strCmd);
            		clipboard.setContents(text1,null);
            	}
				
				btn_getMonkeyCmd.setEnabled(true);
			}
		});
		btn_getMonkeyCmd.setText("获取指令");
		btn_getMonkeyCmd.setBounds(540, 378, 89, 27);
		
		
		
		
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MonkeyKingMain window = new MonkeyKingMain();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("MonkeyKing（安卓Monkey测试工具） V1.3");
		newShell.setImage(new Image(display, "./icons/MonkeyKing.ico")); //添加图标
		newShell.addShellListener(new ShellAdapter(){
			public void shellClosed(ShellEvent e){
//			    e.doit = false; //阻止关闭窗口
				log = false;

			  }
		});
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(785, 608);
	}
}
