package swt.android.Special;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jxcell.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormat;

import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Text;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Group;


public class TopMainTest extends ApplicationWindow {
	
	Display display = new Display(); 
	
	private Text text_1;
	private Text text_2;
	String currentDir = ".";
	boolean stopTop1 = true;
	boolean stopTop2 = true;
	boolean stopMem = true; 
	boolean stopProcrank = true;
	boolean stopHprof = true;
	boolean stopCpuHz = true;
	boolean hasFoundDumped = false;
	boolean tips1 = false;
	boolean tips2 = false;
	boolean tips3 = false;
	boolean tips4 = false;
	private Text text_3;
	private Text text_packname1;
	private Text text_packname3;
	private Text text_packname4;
	private Text text3_sec;
	Button checkBtn2;
	Button checkBtn4;
	String strPattern = "";
	MessageBox messinfo;
	String ExcelColName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ" };
	int maxrow = 0;
	private Text text4_sec;
	private Text text_4;
	private Text checkTxt1;
	private Text text2_sec;
	private Text text1_sec;

	String porc[] = new String[20];
	private Text text_5;
	int clickNum = 0;
	
	boolean displayProc = true;
	int cpuCurve = 0;
	boolean cpuCurveRun = false;
	double rssCurve = 0;
	boolean rssCurveRun = false;
	
	
	boolean radio1 = true, radio2 = false, radio3 = true, radio4 = false;
	private Text text1_count;
	private Text text2_count;
	private Text checkTxt2;
	
	/**
	 * Create the application window.
	 */
	public TopMainTest() {
		super(null);
		setShellStyle(SWT.MIN);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}
	
	// 保存为图片
    public static void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {

        FileOutputStream out = null;
        try {
/*            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }*/
            out = new FileOutputStream(outputPath);
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }    

	//查找子串个数
    public static int indexOfCount(String string, String str) {
		String[] array = string.split(str);
		if (array != null) {
			return array.length - 1;
		}else{
			return 0;
		}
	}
    
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		final Button btn_StartMem = new Button(container, SWT.NONE);
		text_packname1 = new Text(container, SWT.BORDER);
		text_packname3 = new Text(container, SWT.BORDER);
		text_packname4 = new Text(container, SWT.BORDER);
		text_packname4.setBounds(430, 12, 201, 23);
		text3_sec = new Text(container, SWT.BORDER);
		text3_sec.setText("10");
		text3_sec.setTextLimit(2); //输入长度最多2位
		text3_sec.addVerifyListener(new VerifyListener(){
		    public void verifyText(VerifyEvent e) { //e.text为输入的内容
		       boolean b = ("0123456789".indexOf(e.text)>=0);
		       e.doit = b;
		    }
		});
		text4_sec = new Text(container, SWT.BORDER);
		text4_sec.setText("10");
		text4_sec.setTextLimit(2); //输入长度最多2位
		text4_sec.addVerifyListener(new VerifyListener(){
		    public void verifyText(VerifyEvent e) { //e.text为输入的内容
		       boolean b = ("0123456789".indexOf(e.text)>=0);
		       e.doit = b;
		    }
		});
		final Button btn_StopMem = new Button(container, SWT.NONE);
		checkBtn2 = new Button(container, SWT.CHECK);
		checkBtn2.setSelection(true);
		checkBtn2.setText("\u7CBE\u786E\u8FC7\u6EE4");
		checkBtn2.setBounds(385, 292, 68, 17);
		checkBtn4 = new Button(container, SWT.CHECK);
		checkBtn4.setText("\u7CBE\u786E\u8FC7\u6EE4");
		checkBtn4.setSelection(true);
		checkBtn4.setBounds(700, 533, 70, 17);
		final Button btn_StartProcrank = new Button(container, SWT.NONE);
		final Button btn_StopProcrank = new Button(container, SWT.NONE);
		final Button btn_catchInfo = new Button(container, SWT.NONE);
		btn_catchInfo.setEnabled(false);
		checkTxt1 = new Text(container, SWT.BORDER);
		checkTxt1.setEnabled(false);
		checkTxt1.setText("3");
		checkTxt1.setBounds(70, 205, 23, 23);
		checkTxt2 = new Text(container, SWT.BORDER);
		checkTxt2.setText("3");
		checkTxt2.setEnabled(false);
		checkTxt2.setBounds(519, 205, 23, 23);
		final Button checkBtn3 = new Button(container, SWT.CHECK);
		final Button checkBtn1 = new Button(container, SWT.CHECK);
		final Button btn_StartTop1 = new Button(container, SWT.NONE);
		final Button btn_StopTop1 = new Button(container, SWT.NONE);
		final Button btn_StartTop2 = new Button(container, SWT.NONE);
		final Button btn_StopTop2 = new Button(container, SWT.NONE);
		final Button btn_StartHprof = new Button(container, SWT.NONE);
		final Button btn_StopHprof = new Button(container, SWT.NONE);
		final Button btn_ManualHprof = new Button(container, SWT.NONE);
		
		//top指令指定测试时长
		final Button chkbtn_1 = new Button(container, SWT.CHECK);
		chkbtn_1.setBounds(310, 51, 54, 17);
		chkbtn_1.setText("\u53EA\u91C7\u96C6");
		
		final Button chkbtn_2 = new Button(container, SWT.CHECK);
		chkbtn_2.setText("\u53EA\u91C7\u96C6");
		chkbtn_2.setBounds(310, 321, 54, 17);
		
		text1_count = new Text(container, SWT.BORDER);
		text1_count.setText("10");
		text1_count.setBounds(367, 48, 25, 23);
		text1_count.setTextLimit(2);
		text1_count.addVerifyListener(new VerifyListener(){
		    public void verifyText(VerifyEvent e) {
		       boolean b = ("0123456789".indexOf(e.text)>=0);
		       e.doit = b;
		    }
		});
		
		text2_count = new Text(container, SWT.BORDER);
		text2_count.setText("10");
		text2_count.setBounds(367, 318, 25, 23);
		text2_count.setTextLimit(2);
		text2_count.addVerifyListener(new VerifyListener(){
		    public void verifyText(VerifyEvent e) {
		       boolean b = ("0123456789".indexOf(e.text)>=0);
		       e.doit = b;
		    }
		});
		
		
		File config;
		String strTemp = "", strConfig = "";
		BufferedReader readConfig = null;
		
		final Label label_tips1 = new Label(container, SWT.NONE);
		label_tips1.setAlignment(SWT.CENTER);
		label_tips1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		label_tips1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
		label_tips1.setBounds(23, 78, 80, 25);
		
		final Label label_tips2 = new Label(container, SWT.NONE);
		label_tips2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		label_tips2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
		label_tips2.setAlignment(SWT.CENTER);
		label_tips2.setBounds(23, 344, 80, 25);
		
		final Label label_tips3 = new Label(container, SWT.NONE);
		label_tips3.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		label_tips3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
		label_tips3.setAlignment(SWT.CENTER);
		label_tips3.setBounds(472, 78, 80, 25);
		
		final Label label_tips4 = new Label(container, SWT.NONE);
		label_tips4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		label_tips4.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 13, SWT.NORMAL));
		label_tips4.setAlignment(SWT.CENTER);
		label_tips4.setBounds(472, 263, 80, 25);
		
		final Button checkBtn_fdThreads = new Button(container, SWT.CHECK);
		checkBtn_fdThreads.setText("\u76D1\u6D4B\u53E5\u67C4\u548C\u7EBF\u7A0B");
		checkBtn_fdThreads.setBounds(795, 52, 102, 17);
		
		
		// 读取配置文件
		config = new File("config\\text5");
		try {
			if(!config.exists()) {
				config.createNewFile();
			}else{
				readConfig = new BufferedReader(new FileReader(config));
				while ((strTemp = readConfig.readLine()) != null) { 
					strConfig = strConfig + strTemp + "\r\n";
				}
				readConfig.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		text_5 = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_5.setBounds(569, 435, 296, 60);
		text_5.setText(strConfig);
		strTemp = "";
		strConfig = "";
		
		
		// 读取配置文件
		config = new File("config\\text1");
		try {
			if(!config.exists()) {
				config.createNewFile();
			}else{
				readConfig = new BufferedReader(new FileReader(config));
				while ((strTemp = readConfig.readLine()) != null) { 
					strConfig = strConfig + strTemp + "\r\n";
				}
				readConfig.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		
		text_1 = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_1.setBounds(120, 78, 296, 195);
		text_1.setText(strConfig);
		strTemp = "";
		strConfig = "";
		
		
		
		//打开文件1
		final Button btn_openFile1 = new Button(container, SWT.NONE);
		btn_openFile1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_openFile1.setEnabled(false);
				
				String str = (text_1.getText()).trim();
				String[] s; //存放待查询字符串
				if( str.equals("") ){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING); //判断文件名字符串
			    	messageBox.setMessage("请填写要解析的内容项，以换行分隔！");
			    	messageBox.open();
			    	btn_openFile1.setEnabled(true);
					return;
				}else{
					s = str.split("\\r\\n+");
				}
				
				JFileChooser jfc=new JFileChooser(currentDir); 
				jfc.setDialogTitle("请选择 cpu- 开头的数据文件...");
			 	FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
			 	jfc.setFileFilter(filter);
				int returnVal = jfc.showOpenDialog(null); 
				if(returnVal == JFileChooser.APPROVE_OPTION){
					
					currentDir = jfc.getCurrentDirectory()+"";
					if(!currentDir.endsWith("\\"))
						currentDir = currentDir + "\\";
					if( !(jfc.getSelectedFile().getName()).startsWith("cpu-") ){
						MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
						messageBox.setMessage("请选择 cpu- 开头的数据文件！");
						messageBox.open();
						btn_openFile1.setEnabled(true);
						return;
					}
					
					BufferedReader reader;
					String strRead = null;
					int lr = 1; //文件的行数
					int N = s.length;
					maxrow = 0;
					
					try{ //读取txt，整理数据后输出到EXCEL
						
						int cpuPos = 2, rssPos = 6; //CPU默认第2列，RSS第6列
						BufferedReader readerTmp = new BufferedReader(new FileReader(jfc.getSelectedFile()));
						while ((strRead = readerTmp.readLine()) != null) {
							strRead = strRead.trim();
							if(strRead.startsWith("PID")){
								String[] posStr = strRead.split("\\s+");
								for(int ps=0; ps<posStr.length; ps++){
									if(posStr[ps].equals("CPU%"))
										cpuPos = ps;
									if(posStr[ps].equals("RSS"))
										rssPos = ps;
								}
								break;
							}
						}
						readerTmp.close();
						
						View m_view = new View();
				        String strSheetName = "";
				        
						// 创建Excel工作表
						int r = 1; //Excel行坐标
						String[] aa;
						double dRss;
						DecimalFormat df = new DecimalFormat("#.00");
						if(checkBtn1.getSelection()){
							String strTxt = checkTxt1.getText().trim();
							if(strTxt.equals("")){
								MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
								messageBox.setMessage("请填写要汇总的行数！");
								messageBox.open();
								btn_openFile1.setEnabled(true);
								return;
							}else if(Integer.parseInt(strTxt)>N){
								MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
								messageBox.setMessage("要汇总的行数不能大于总行数！");
								messageBox.open();
								btn_openFile1.setEnabled(true);
								return;
							}else{
								m_view.setNumSheets(N+3); //汇总表
							}
						}else{
							m_view.setNumSheets(N+2);
						}
						
						
						XYSeriesCollection xySeriesCollection1;
						XYSeriesCollection xySeriesCollection2;
				        XYSeries[] xyseries1 = new XYSeries[1000];
				        XYSeries[] xyseries2 = new XYSeries[1000];
						XYLineAndShapeRenderer renderer1, renderer2;
						String cpuProcessor = "", cpuCore = "", cpuHardware = "";
						String cpuinfo = ""; //总CPU信息
						String sampInterval = "";
						String pidLast = "";
						int pidRow = 1;
						
						int ixy=0;
						String timestamp = ""; //记录时间戳
						
						for(int l=0; l<N; l++){
							
							xySeriesCollection1 = null;
							xySeriesCollection2 = null;
							for(int x=0; x<1000; x++){
								xyseries1[x] = null;
								xyseries2[x] = null;
							}
							
							cpuinfo = "";
							cpuProcessor = "";
							cpuCore = "";
							cpuHardware = "";
							pidLast = "";
							pidRow = 1;
							xySeriesCollection1 = new XYSeriesCollection();
							xySeriesCollection2 = new XYSeriesCollection();
							
							ixy=0;
					        xyseries1[ixy] = new XYSeries("CPU");
							xyseries2[ixy] = new XYSeries("RSS");
							
							s[l] = s[l].trim();
							if( !s[l].equals("") ){
								strSheetName = "Process "+String.valueOf(l+1);
								m_view.setSheetName(l+1, strSheetName);
								m_view.setSheet(l+1);
					            m_view.getLock();
								
								// 标题行
					            m_view.setTextAsValue(0, 0, "CPU (%)");
					            m_view.setTextAsValue(0, 1, "RSS (M)"); //m_view.setTextAsValue(0, 2, "RSS (M)");
					            m_view.setTextAsValue(0, 3, "Times");
					            m_view.setTextAsValue(0, 4, "PID");
					            m_view.setColWidth(3, 1800); //设置列宽
					            m_view.setColWidth(4, 1800);
					            m_view.setColWidth(5, 6800);
					            m_view.setColWidth(6, 2200);
					            m_view.setColWidth(7, 2000);
					            m_view.setSelection("F1:F1000");
					            CellFormat cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter); //水平居中
					            m_view.setCellFormat(cfmt);
					            m_view.setTextAsValue(0, 6, s[l]);
					            cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
					            cfmt.setFontColor(Color.BLUE.getRGB());
					            m_view.setCellFormat(cfmt, 0, 0, 0, 6);
					            
								r = 1;
								reader = new BufferedReader(new FileReader(jfc.getSelectedFile()));
								boolean hasFound = true; //本段数据中已找到该进程的数据，第一行标题不做处理，因此置为true
								boolean hasMarkBreak = false; //已设置的断点
								boolean isEmpty = true; //是否始终没有该进程的数据
								int aalen = 0;
								lr = 0;
								int cpunum = 2;
								int cpucorenum = 0;
								int cpucorenum100 = 0;
								while ((strRead = reader.readLine()) != null) { //每次从txt读一行，直到文件结尾
									lr++;
									strRead = strRead.trim();
									
									if( strRead.endsWith( s[l]) ){
										hasFound = true;
										isEmpty = false;
										hasMarkBreak = false;
										aa = strRead.split("\\s+");
										aa[0] = aa[0].trim();
										if( !pidLast.equals(aa[0]) ){
											m_view.setTextAsValue(pidRow, 3, String.valueOf(r));
											m_view.setTextAsValue(pidRow, 4, aa[0]);
											m_view.setTextAsValue(pidRow++, 5, timestamp);
											pidLast = aa[0];
										}
										
										aa[cpuPos] = aa[cpuPos].substring(0, aa[cpuPos].length()-1); //删除%
										if( cpucorenum>0 ){ //如果找到cpucorenum数据
											if( Double.parseDouble(aa[cpuPos])>cpucorenum100 ){ //CPU%超过cpucorenum100，只显示cpucorenum100
												aa[cpuPos] = String.valueOf(cpucorenum100);
											}
										}else if( Double.parseDouble(aa[cpuPos])>400 ){ //如果CPU%超过400%，只显示400%
											aa[cpuPos] = "400";
										}
										aa[rssPos] = aa[rssPos].substring(0, aa[rssPos].length()-1); //删除K
										dRss = Double.parseDouble(aa[rssPos])/1024.00;
										
										if( xyseries1[ixy]==null )
											xyseries1[ixy] = new XYSeries("CPU"+String.valueOf(ixy));
										if( xyseries2[ixy]==null )
											xyseries2[ixy] = new XYSeries("RSS"+String.valueOf(ixy));
										m_view.setTextAsValue(r, 0, aa[cpuPos]);
										xyseries1[ixy].add(r, Integer.parseInt(aa[cpuPos]));
										m_view.setTextAsValue(r, 1, df.format(dRss)); //m_view.setTextAsValue(r, 2, df.format(dRss));
										xyseries2[ixy].add(r++, dRss);
										
									}else if( strRead.startsWith("PID") ){ //如果到了下一段数据
										if( !hasFound ){
											if( !hasMarkBreak ){
												ixy++;
												hasMarkBreak = true;
											}	
											r++;
										}else
											hasFound = false;
									}else if( strRead.startsWith("Timestamp") ){
										timestamp = strRead;
									}else if( strRead.startsWith("Processor") ){ //获取CPU信息
										if( cpuProcessor.indexOf(strRead)!=-1 ){ //多核CPU重复信息
											int flg = cpuProcessor.indexOf("×");
											if( flg!=-1 ){
												cpuProcessor = cpuProcessor.substring(0, flg);
											}
											cpuProcessor = cpuProcessor.replaceAll("\r", "");
											cpuProcessor = cpuProcessor.replaceAll("\n", "");
											cpuProcessor = cpuProcessor + " × "+String.valueOf(cpunum++)+"\r\n";
										}else{
											cpuProcessor = cpuProcessor + strRead + "\r\n";
										}
									}else if( strRead.startsWith("processor") ){
										cpucorenum++;
										cpucorenum100 = cpucorenum*100;
									}else if( strRead.startsWith("Hardware") ){
										cpuHardware = cpuHardware + strRead + "\r\n";
									}else if( strRead.startsWith("Sampling interval") ){
										sampInterval = strRead.substring(strRead.indexOf(":")+1, strRead.length()).trim()+"秒";
									}
								}
								if(r>maxrow)
									maxrow = r; //记录最大行数
								reader.close(); //关闭txt
								if(cpucorenum>0){
									if(!cpuProcessor.equals("")){
										cpuProcessor = cpuProcessor.substring(0, cpuProcessor.length()-2);
									}
									cpuCore = "     [ Cores: "+String.valueOf(cpucorenum)+" ]\r\n";
								}
								cpuinfo = cpuProcessor + cpuCore + cpuHardware;
								if( !cpuinfo.equals("") ){
									cpuinfo = cpuinfo.substring(0, cpuinfo.length()-2);
								}
								if( isEmpty )
									continue;
								
								for(int i=0;i<=ixy;i++){
									if( xyseries1[i]!=null)
										xySeriesCollection1.addSeries(xyseries1[i]);
								}
								JFreeChart jfreechart1 = ChartFactory.createXYLineChart("CPU\r\n"+cpuinfo, "Sampling Times", "Value (%)", xySeriesCollection1, PlotOrientation.VERTICAL, true, false, false);
								jfreechart1.getLegend().setVisible(false);
								XYPlot plot1 = (XYPlot) jfreechart1.getPlot();
								plot1.setBackgroundAlpha(0.5f);
								plot1.setForegroundAlpha(1f);
								renderer1 = (XYLineAndShapeRenderer)plot1.getRenderer();
								renderer1.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart1, "config\\temp.png", 900, 340);
						        m_view.addPicture(8, 2, 18, 16, "config\\temp.png");
						        xySeriesCollection1 = null;
						        
						        for(int i=0;i<=ixy;i++){
						        	if( xyseries2[i]!=null)
						        		xySeriesCollection2.addSeries(xyseries2[i]);
								}
								JFreeChart jfreechart2 = ChartFactory.createXYLineChart("RSS", "Sampling Times", "Value (M)", xySeriesCollection2, PlotOrientation.VERTICAL, true, false, false);
								jfreechart2.getLegend().setVisible(false); 
								XYPlot plot2 = (XYPlot) jfreechart2.getPlot();
								plot2.setBackgroundAlpha(0.5f); 
								plot2.setForegroundAlpha(1f); 
						        renderer2 = (XYLineAndShapeRenderer)plot2.getRenderer();
								renderer2.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart2, "config\\temp.png", 900, 300);
						        m_view.addPicture(8, 16, 18, 28, "config\\temp.png");  m_view.addPicture(8, 30, 18, 42, "config\\temp.png");
						        xySeriesCollection2 = null;
					            
								//插入公式
								m_view.setTextAsValue(5, 6, "最大值");
								m_view.setFormula(5, 7, "MAX(A2:A"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(6, 6, "最小值");
								m_view.setFormula(6, 7, "MIN(A2:A"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(7, 6, "平均值");
								m_view.setFormula(7, 7, "ROUND(AVERAGE(A2:A"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(8, 6, "采样间隔");
								m_view.setText(8, 7, sampInterval);
								
					            //插入公式
								m_view.setTextAsValue(17, 6, "最大值");
								m_view.setFormula(17, 7, "MAX(B2:B"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(18, 6, "最小值");
								m_view.setFormula(18, 7, "MIN(B2:B"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(19, 6, "平均值");
								m_view.setFormula(19, 7, "ROUND(AVERAGE(B2:B"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(20, 6, "采样间隔");
								m_view.setText(20, 7, sampInterval);
								
								//设置数据右对齐
								m_view.setSelection("H1:H50");
								cfmt = m_view.getCellFormat();
								cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
								m_view.setCellFormat(cfmt);
								
								
								//插入饼图公式
								m_view.setTextAsValue(3, 18, "数值分布 (单位:%)");
								m_view.setTextAsValue(4, 18, "0-10%");
								m_view.setTextAsValue(5, 18, "11-20%");
								m_view.setTextAsValue(6, 18, "21-30%");
								m_view.setTextAsValue(7, 18, "31-40%");
								m_view.setTextAsValue(8, 18, "41-50%");
								m_view.setTextAsValue(9, 18, "51-60%");
								m_view.setTextAsValue(10, 18, "61-70%");
								m_view.setTextAsValue(11, 18, "71-80%");
								m_view.setTextAsValue(12, 18, "81-90%");
								m_view.setTextAsValue(13, 18, "91-100%");
								m_view.setFormula(4, 19, "ROUND(COUNTIF(A:A,\"<=10\")/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(5, 19, "ROUND((COUNTIF(A:A,\"<=20\")-COUNTIF(A:A,\"<11\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(6, 19, "ROUND((COUNTIF(A:A,\"<=30\")-COUNTIF(A:A,\"<21\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(7, 19, "ROUND((COUNTIF(A:A,\"<=40\")-COUNTIF(A:A,\"<31\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(8, 19, "ROUND((COUNTIF(A:A,\"<=50\")-COUNTIF(A:A,\"<41\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(9, 19, "ROUND((COUNTIF(A:A,\"<=60\")-COUNTIF(A:A,\"<51\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(10, 19, "ROUND((COUNTIF(A:A,\"<=70\")-COUNTIF(A:A,\"<61\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(11, 19, "ROUND((COUNTIF(A:A,\"<=80\")-COUNTIF(A:A,\"<71\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(12, 19, "ROUND((COUNTIF(A:A,\"<=90\")-COUNTIF(A:A,\"<81\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(13, 19, "ROUND((COUNTIF(A:A,\"<=100\")-COUNTIF(A:A,\"<91\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setSelection("S5:S50");
								cfmt = m_view.getCellFormat();
								cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
								m_view.setCellFormat(cfmt);
								
						        ChartShape chartpie = m_view.addChart(20, 4, 25, 14);
						        chartpie.setChartType(ChartShape.TypePie);
						        chartpie.initData(new RangeRef(18,4,19,13), false); //设置数据源
						        chartpie.setVaryColors(true);
						        
							} // if end
							
						} //for end
						
						N++;
						int M = 0;
						if(checkBtn1.getSelection()){
							XYSeriesCollection xySeriesCollection3;
							XYSeries xyseries3;
							XYLineAndShapeRenderer renderer3;
							xySeriesCollection3 = new XYSeriesCollection();
					        xyseries3 = new XYSeries("CPU");
							
					        strSheetName = "Subtotal_CPU";
							m_view.setSheetName(N, strSheetName); 
							
							String stmp = "";
							M = Integer.parseInt(checkTxt1.getText().trim())+1;
							// 标题行
							for(int p=1; p<M; p++){
				            	m_view.setSheet(p);
				            	stmp = m_view.getText(0, 6);
				            	m_view.setSheet(N);
				            	m_view.setTextAsValue(0, p-1, stmp);
				            	m_view.setColWidth(p-1, 5000);
				            	for(int q=1; q<=maxrow; q++){
					            	m_view.setSheet(p);
					            	stmp = m_view.getText(q, 0);
					            	m_view.setSheet(N);
					            	m_view.setTextAsValue(q, p-1, stmp);
					            }
							}
							
				            CellFormat cfmt = m_view.getCellFormat();
				            cfmt.setFontColor(Color.BLUE.getRGB());
				            m_view.setCellFormat(cfmt, 0, 0, 0, 100);
				            
				            
				            //插入公式
				            m_view.setColWidth(M+1, 2200); //设置列宽
				            m_view.setColWidth(M+2, 2000);
							m_view.setTextAsValue(0, M-1, "SUM");
							m_view.setTextAsValue(0, M+1, "Subtotal_CPU");
							m_view.setFormula(1, M-1, "SUM(A2:"+ExcelColName[M-2]+"2)");
							m_view.setSelection(ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow));
							m_view.editCopyDown();
							m_view.setTextAsValue(5, M+1, "最大值");
							m_view.setFormula(5, M+2, "MAX("+ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow)+")");
							m_view.setTextAsValue(6, M+1, "最小值");
							m_view.setFormula(6, M+2, "MIN("+ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow)+")");
							m_view.setTextAsValue(7, M+1, "平均值");
							m_view.setFormula(7, M+2, "ROUND(AVERAGE("+ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow)+"),2)");
							m_view.setTextAsValue(8, M+1, "采样间隔");
							m_view.setText(8, M+2, sampInterval);
							
							for(int xx=1; xx<maxrow; xx++){
								if( !m_view.getText(xx, M-1).equals("") )
									xyseries3.add(xx, Integer.parseInt(m_view.getText(xx, M-1)));
							}
							
							xySeriesCollection3 = new XYSeriesCollection();
							xySeriesCollection3.addSeries(xyseries3);
							JFreeChart jfreechart3 = ChartFactory.createXYLineChart("CPU\r\n"+cpuinfo, "Sampling Times", "Value (%)", xySeriesCollection3, PlotOrientation.VERTICAL, true, false, false);
							jfreechart3.getLegend().setVisible(false); 
							XYPlot plot3 = (XYPlot) jfreechart3.getPlot();
							plot3.setBackgroundAlpha(0.5f); 
							plot3.setForegroundAlpha(1f); 
							renderer3 = (XYLineAndShapeRenderer)plot3.getRenderer();
							renderer3.setSeriesPaint(0, Color.BLUE);
					        saveAsFile(jfreechart3, "config\\temp.png", 900, 340);
					        m_view.addPicture(M+3, 2, M+13, 16, "config\\temp.png"); 
					        xySeriesCollection3 = null;
							
							//设置格式
				            m_view.setSelection("A1:AZ1");
				            cfmt = m_view.getCellFormat();
				            cfmt.setFontColor(Color.BLUE.getRGB());
							cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
							m_view.setCellFormat(cfmt);
							m_view.setSelection(ExcelColName[M-1]+"1:"+ExcelColName[M-1]+String.valueOf(maxrow));
				            cfmt = m_view.getCellFormat();
				            cfmt.setFontBold(true);
							m_view.setCellFormat(cfmt);
							m_view.setSelection(ExcelColName[M+2]+"1:"+ExcelColName[M+2]+"50");
							cfmt = m_view.getCellFormat();
							cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
							m_view.setCellFormat(cfmt);
							//m_view.releaseLock();
							N++;
							
							
							//插入饼图公式
							m_view.setTextAsValue(18, M+4, "数值分布 (单位:%)");
							m_view.setTextAsValue(19, M+4, "0-10%");
							m_view.setTextAsValue(20, M+4, "11-20%");
							m_view.setTextAsValue(21, M+4, "21-30%");
							m_view.setTextAsValue(22, M+4, "31-40%");
							m_view.setTextAsValue(23, M+4, "41-50%");
							m_view.setTextAsValue(24, M+4, "51-60%");
							m_view.setTextAsValue(25, M+4, "61-70%");
							m_view.setTextAsValue(26, M+4, "71-80%");
							m_view.setTextAsValue(27, M+4, "81-90%");
							m_view.setTextAsValue(28, M+4, "91-100%");
							String strColName = ExcelColName[M-1]+":"+ExcelColName[M-1];
							m_view.setFormula(19, M+5, "ROUND(COUNTIF("+strColName+",\"<=10\")/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(20, M+5, "ROUND((COUNTIF("+strColName+",\"<=20\")-COUNTIF("+strColName+",\"<11\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(21, M+5, "ROUND((COUNTIF("+strColName+",\"<=30\")-COUNTIF("+strColName+",\"<21\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(22, M+5, "ROUND((COUNTIF("+strColName+",\"<=40\")-COUNTIF("+strColName+",\"<31\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(23, M+5, "ROUND((COUNTIF("+strColName+",\"<=50\")-COUNTIF("+strColName+",\"<41\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(24, M+5, "ROUND((COUNTIF("+strColName+",\"<=60\")-COUNTIF("+strColName+",\"<51\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(25, M+5, "ROUND((COUNTIF("+strColName+",\"<=70\")-COUNTIF("+strColName+",\"<61\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(26, M+5, "ROUND((COUNTIF("+strColName+",\"<=80\")-COUNTIF("+strColName+",\"<71\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(27, M+5, "ROUND((COUNTIF("+strColName+",\"<=90\")-COUNTIF("+strColName+",\"<81\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setFormula(28, M+5, "ROUND((COUNTIF("+strColName+",\"<=100\")-COUNTIF("+strColName+",\"<91\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
							m_view.setSelection(ExcelColName[M+4]+"20:"+ExcelColName[M+4]+"50");
							cfmt = m_view.getCellFormat();
							cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
							m_view.setCellFormat(cfmt);
							
					        //添加数值分布饼图
					        ChartShape chartpie = m_view.addChart(M+6, 19, M+11, 29);
					        chartpie.setChartType(ChartShape.TypePie);
					        chartpie.initData(new RangeRef(M+4,19,M+5,28), false);
					        chartpie.setVaryColors(true);
							
							
					        //文本框写入配置文件
							FileWriter fwriter2 = null;
							try {
								fwriter2 = new FileWriter("config\\text0", false);
								fwriter2.write(checkTxt1.getText().trim()+"\r\n");
								fwriter2.write(text_packname1.getText().trim()+"\r\n");
								fwriter2.write(text_packname3.getText().trim()+"\r\n");
								fwriter2.write(text_packname4.getText().trim()+"\r\n");
								fwriter2.write(checkTxt2.getText().trim()+"\r\n");
								fwriter2.close();
								
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						
						
						XYSeriesCollection xySeriesCollection3;
						XYSeries xyseries3;
						XYLineAndShapeRenderer renderer3;
						xySeriesCollection3 = new XYSeriesCollection();
				        xyseries3 = new XYSeries("CPU");
						
				        String sTotalCPU = "%, IRQ ";
				        strSheetName = "SystemTotal_CPU";
						m_view.setSheetName(N, strSheetName); 
						m_view.setSheet(N);
			            m_view.getLock();
						
						// 标题行
			            m_view.setTextAsValue(0, 0, "SystemTotal CPU (%)");
			            m_view.setColWidth(6, 2200);
			            m_view.setColWidth(7, 2000);
			            m_view.setTextAsValue(0, 6, strSheetName);
			            CellFormat cfmt = m_view.getCellFormat();
			            cfmt.setFontColor(Color.BLUE.getRGB());
			            m_view.setCellFormat(cfmt, 0, 0, 0, 6);
			            
						r = 1;
						reader = new BufferedReader(new FileReader(jfc.getSelectedFile()));
						boolean hasFound = true;
						lr = 0;
						while ((strRead = reader.readLine()) != null) {
							lr++;
							strRead = strRead.trim();
							if( strRead.contains(sTotalCPU) ){
								aa = strRead.split("\\s+");
								aa[1] = aa[1].substring(0, aa[1].length()-2); //删除%
								aa[3] = aa[3].substring(0, aa[3].length()-2); //删除%
								dRss = (Double.parseDouble(aa[1])+Double.parseDouble(aa[3]))/1.00;
								if( dRss>400 )
									dRss = 400;								
								
								m_view.setTextAsValue(r, 0, df.format(dRss));
								xyseries3.add(r++, dRss);
								
							}
						}
						reader.close(); //关闭txt
						
						xySeriesCollection3.addSeries(xyseries3);
						JFreeChart jfreechart3 = ChartFactory.createXYLineChart("CPU\r\n"+cpuinfo, "Sampling Times", "Value (%)", xySeriesCollection3, PlotOrientation.VERTICAL, true, false, false);
						jfreechart3.getLegend().setVisible(false); 
						XYPlot plot3 = (XYPlot) jfreechart3.getPlot();
						plot3.setBackgroundAlpha(0.5f); 
						plot3.setForegroundAlpha(1f); 
						renderer3 = (XYLineAndShapeRenderer)plot3.getRenderer();
						renderer3.setSeriesPaint(0, Color.BLUE);
				        saveAsFile(jfreechart3, "config\\temp.png", 900, 340);
				        m_view.addPicture(8, 2, 18, 16, "config\\temp.png"); 
				        xySeriesCollection1 = null;
				        
						//插入公式
						m_view.setTextAsValue(5, 6, "最大值");
						m_view.setFormula(5, 7, "MAX(A2:A"+Integer.valueOf(r)+")");
						m_view.setTextAsValue(6, 6, "最小值");
						m_view.setFormula(6, 7, "MIN(A2:A"+Integer.valueOf(r)+")");
						m_view.setTextAsValue(7, 6, "平均值");
						m_view.setFormula(7, 7, "ROUND(AVERAGE(A2:A"+Integer.valueOf(r)+"),2)");
						m_view.setTextAsValue(8, 6, "采样间隔");
						m_view.setText(8, 7, sampInterval);
						
						//设置数据右对齐
						m_view.setSelection("H1:H50");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
						m_view.setCellFormat(cfmt);
						
						
						//插入饼图公式
						m_view.setTextAsValue(18, 9, "数值分布 (单位:%)");
						m_view.setTextAsValue(19, 9, "0-10%");
						m_view.setTextAsValue(20, 9, "11-20%");
						m_view.setTextAsValue(21, 9, "21-30%");
						m_view.setTextAsValue(22, 9, "31-40%");
						m_view.setTextAsValue(23, 9, "41-50%");
						m_view.setTextAsValue(24, 9, "51-60%");
						m_view.setTextAsValue(25, 9, "61-70%");
						m_view.setTextAsValue(26, 9, "71-80%");
						m_view.setTextAsValue(27, 9, "81-90%");
						m_view.setTextAsValue(28, 9, "91-100%");
						m_view.setFormula(19, 10, "ROUND(COUNTIF(A:A,\"<=10\")/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(20, 10, "ROUND((COUNTIF(A:A,\"<=20\")-COUNTIF(A:A,\"<11\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(21, 10, "ROUND((COUNTIF(A:A,\"<=30\")-COUNTIF(A:A,\"<21\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(22, 10, "ROUND((COUNTIF(A:A,\"<=40\")-COUNTIF(A:A,\"<31\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(23, 10, "ROUND((COUNTIF(A:A,\"<=50\")-COUNTIF(A:A,\"<41\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(24, 10, "ROUND((COUNTIF(A:A,\"<=60\")-COUNTIF(A:A,\"<51\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(25, 10, "ROUND((COUNTIF(A:A,\"<=70\")-COUNTIF(A:A,\"<61\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(26, 10, "ROUND((COUNTIF(A:A,\"<=80\")-COUNTIF(A:A,\"<71\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(27, 10, "ROUND((COUNTIF(A:A,\"<=90\")-COUNTIF(A:A,\"<81\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setFormula(28, 10, "ROUND((COUNTIF(A:A,\"<=100\")-COUNTIF(A:A,\"<91\"))/COUNTIF(A:A,\">=0\")*100,2)");
						m_view.setSelection("J20:J50");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						
				        //添加数值分布饼图
				        ChartShape chartpie = m_view.addChart(11, 19, 16, 29);
				        chartpie.setChartType(ChartShape.TypePie);
				        chartpie.initData(new RangeRef(9,19,10,28), false); //设置数据源
				        chartpie.setVaryColors(true);
				        
						
						
						//Summary总表
						m_view.setSheetName(0, "Summary"); 
						m_view.setSheet(0);
						// 标题行
			            m_view.setTextAsValue(0, 0, "Process");
			            m_view.setColWidth(0, 8000); //设置列宽
			            m_view.setTextAsValue(0, 1, "CPU Max(%)");
			            m_view.setColWidth(1, 3000);
			            m_view.setTextAsValue(0, 2, "CPU Min(%)");
			            m_view.setColWidth(2, 3000);
			            m_view.setTextAsValue(0, 3, "CPU Avg(%)");
			            m_view.setColWidth(3, 3000);
			            m_view.setTextAsValue(0, 4, "RSS Max(M)");
			            m_view.setColWidth(4, 3000);
			            m_view.setTextAsValue(0, 5, "RSS Min(M)");
			            m_view.setColWidth(5, 3000);
			            m_view.setTextAsValue(0, 6, "RSS Avg(M)");
			            m_view.setColWidth(6, 3000);
			            cfmt = m_view.getCellFormat();
			            cfmt.setFontColor(Color.BLUE.getRGB());
			            m_view.setCellFormat(cfmt, 0, 0, 0, 6);
			            String stmp = "";
			            for( int sr=1; sr<=N; sr++ ){
			            	if( sr==N-1 & checkBtn1.getSelection()){
				            	//Process
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(0, M+1);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 0, stmp);
				            	//CPU Max
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(5, M+2);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 1, stmp);
				            	//CPU Min
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(6, M+2);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 2, stmp);
				            	//CPU Avg
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(7, M+2);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 3, stmp);
				            	
			            	}else{
			            		//Process
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(0, 6);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 0, stmp);
				            	//CPU Max
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(5, 7);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 1, stmp);
				            	//CPU Min
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(6, 7);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 2, stmp);
				            	//CPU Avg
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(7, 7);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 3, stmp);
				            	//RSS Max
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(17, 7);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 4, stmp);
				            	//RSS Min
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(18, 7);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 5, stmp);
				            	//RSS Avg
				            	m_view.setSheet(sr);
				            	stmp = m_view.getText(19, 7);
				            	m_view.setSheet(0);
				            	m_view.setTextAsValue(sr, 6, stmp);
			            	}
			            }
			            //设置格式
			            m_view.setSelection("A1:A1");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("B1:H200");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("D1:D200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("G1:G200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						
						String maxLine = "";
						
						//绘图区坐标addChart
						if(checkBtn1.getSelection()){
							maxLine = String.valueOf(N-1);
							ChartShape chart = m_view.addChart(9, 2, 18, 20);
				            chart.setChartType(ChartShape.TypeColumn);
				            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$D$2:$D$"+maxLine, false);
				            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
				            chart.addSeries();
				            chart.setAxisTitle(ChartShape.YAxis, 0, "CPU Avg(%)");
				            chart.setLegendVisible(false);
				            
				            chart = m_view.addChart(9, 22, 18, 40);
				            chart.setChartType(ChartShape.TypeColumn);
				            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$G$2:$G$"+maxLine, false);
				            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
				            chart.addSeries();
				            chart.setAxisTitle(ChartShape.YAxis, 0, "RSS Avg(M)");
				            chart.setLegendVisible(false);
						}else{
							maxLine = String.valueOf(N);
							ChartShape chart = m_view.addChart(9, 2, 18, 20);
				            chart.setChartType(ChartShape.TypeColumn);
				            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$D$2:$D$"+maxLine, false);
				            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
				            chart.addSeries();
				            chart.setAxisTitle(ChartShape.YAxis, 0, "CPU Avg(%)");
				            chart.setLegendVisible(false);
				            
				            chart = m_view.addChart(9, 22, 18, 40);
				            chart.setChartType(ChartShape.TypeColumn);
				            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$G$2:$G$"+maxLine, false);
				            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
				            chart.addSeries();
				            chart.setAxisTitle(ChartShape.YAxis, 0, "RSS Avg(M)");
				            chart.setLegendVisible(false);
						}
			            
			            
			            //excel写出路径
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
			            m_view.write(currentDir+"CPU内存数据_"+format.format(date)+".xls");
			            m_view.releaseLock();
			            
					}catch (Exception e2){
						FileWriter fwriter = null;
						String strWrite = null;
						try {
							e2.printStackTrace();
							fwriter = new FileWriter(currentDir+"ErrorLog.txt", false);
							fwriter.write("Line: "+lr+"\r\n"+e2.getMessage());
							fwriter.close();
							MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
							messageBox.setMessage("数据解析出现异常，请查看ErrorLog.txt！");
							messageBox.open();
							btn_openFile1.setEnabled(true);
							return;
						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
					
				}else{
					btn_openFile1.setEnabled(true);
					return;
				}
				
				//文本框写入配置文件
				FileWriter fwriter1 = null;
				try {
					fwriter1 = new FileWriter("config\\text1", false);
					fwriter1.write(text_1.getText());
					fwriter1.close();
				} catch (Exception ef) {
					ef.printStackTrace();
				}
				
				//文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				messageBox.setMessage("数据解析完毕，解析时对个别不合理数据点进行了处理！");
				messageBox.open();
				btn_openFile1.setEnabled(true);
				
			}
		});
		btn_openFile1.setBounds(23, 172, 80, 27);
		btn_openFile1.setText("\u89E3\u6790\u6570\u636E");
		
		org.eclipse.swt.widgets.Label label = new org.eclipse.swt.widgets.Label(container, SWT.NONE);
		label.setText("\u8BF7\u8F93\u5165\u6700\u540E\u4E00\u5217\uFF1A");
		label.setBounds(17, 236, 97, 17);
		
		org.eclipse.swt.widgets.Label lblFengyanglifengyangli = new org.eclipse.swt.widgets.Label(container, SWT.NONE);
		lblFengyanglifengyangli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				clickNum++;
				if(clickNum>=10){
					clickNum = 0;
					MessageBox messageBox = new MessageBox( getShell(), SWT.OK ); //new Shell(new Display())
					messageBox.setMessage("   Developed by fengyang.li");
					messageBox.open();
				}
			}
		});
		lblFengyanglifengyangli.setText("fengyang.li© 版权所有（fengyang.li制作）");
		lblFengyanglifengyangli.setBounds(567, 556, 330, 17);

		
		// 读取配置文件
		config = new File("config\\text2");
		try {
			if(!config.exists()) {
				config.createNewFile();
			}
			else{
				readConfig = new BufferedReader(new FileReader(config));
				while ((strTemp = readConfig.readLine()) != null) { 
					strConfig = strConfig + strTemp + "\r\n";
				}
				readConfig.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		
		//文本框
		text_2 = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_2.setBounds(120, 345, 296, 195);
		text_2.setText(strConfig);
		strTemp = "";
		strConfig = "";
		
		
		//打开文件2
		final Button btn_openFile2 = new Button(container, SWT.NONE);
		btn_openFile2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_openFile2.setEnabled(false);
				
				String str = (text_2.getText()).trim();
				String[] s = null;
				if( str.equals("") ){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
			    	messageBox.setMessage("请填写要解析的内容项，以换行分隔！");
			    	messageBox.open();
			    	btn_openFile2.setEnabled(true);
					return;
				}
				else{
					s = str.split("\\n+");
				}
				
				JFileChooser jfc=new JFileChooser(currentDir);
				jfc.setDialogTitle("请选择 cputhread- 开头的数据文件...");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
			 	jfc.setFileFilter(filter);
				int returnVal = jfc.showOpenDialog(null); 
				if(returnVal == JFileChooser.APPROVE_OPTION){

					currentDir = jfc.getCurrentDirectory()+"";
					if(!currentDir.endsWith("\\"))
						currentDir = currentDir + "\\";
					if( !(jfc.getSelectedFile().getName()).startsWith("cputhread-") ){
						MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
						messageBox.setMessage("请选择 cputhread- 开头的数据文件！");
						messageBox.open();
						btn_openFile2.setEnabled(true);
						return;
					}
					
					BufferedReader reader;
					String strRead = null;
					int lr = 0; //文件的行数
					maxrow = 0;
					
					try{ //读取txt，整理数据后输出到EXCEL
						
						int cpuPos = 3, rssPos = 6; //CPU默认第2列，RSS第6列
						BufferedReader readerTmp = new BufferedReader(new FileReader(jfc.getSelectedFile()));
						while ((strRead = readerTmp.readLine()) != null) {
							strRead = strRead.trim();
							if(strRead.startsWith("PID")){
								String[] posStr = strRead.split("\\s+");
								for(int ps=0; ps<posStr.length; ps++){
									if(posStr[ps].equals("CPU%"))
										cpuPos = ps;
									if(posStr[ps].equals("RSS"))
										rssPos = ps;
								}
								break;
							}
						}
						readerTmp.close();
						
						View m_view = new View();
				        String strSheetName = "";
				        
						// 创建Excel工作表
						int r = 1;
						String[] aa = null;
						double dRss = 0.0;
						DecimalFormat df = new DecimalFormat("#.00");
						int findSpace = -1;
						String strReg1 = "", strReg2 = "";
						
						m_view.setNumSheets(s.length+2);
						
						
						XYSeriesCollection xySeriesCollection1;
						XYSeriesCollection xySeriesCollection2;
				        XYSeries[] xyseries1 = new XYSeries[1000];
				        XYSeries[] xyseries2 = new XYSeries[1000];
						XYLineAndShapeRenderer renderer1, renderer2;
						String cpuProcessor = "", cpuCore = "", cpuHardware = "";
						String cpuinfo = "";
						String sampInterval = "";
						String pidLast = "";
						int pidRow = 1;
						
						int ixy=0;
						String timestamp = ""; //记录时间戳
						
						for(int l=0; l<s.length; l++){
							
							xySeriesCollection1 = null;
							xySeriesCollection2 = null;
							for(int x=0; x<1000; x++){
								xyseries1[x] = null;
								xyseries2[x] = null;
							}
							
							cpuinfo = "";
							cpuProcessor = "";
							cpuCore = "";
							cpuHardware = "";
							pidLast = "";
							pidRow = 1;
							xySeriesCollection1 = new XYSeriesCollection();
							xySeriesCollection2 = new XYSeriesCollection();
							
							ixy=0;
					        xyseries1[ixy] = new XYSeries("CPU");
							xyseries2[ixy] = new XYSeries("RSS");
							
							s[l] = s[l].trim();
							if( !s[l].equals("") ){
								
								findSpace = -1;
								findSpace = s[l].trim().lastIndexOf(" ");
								if( findSpace == -1 ){
									MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
									messageBox.setMessage("请输入后两列，如：05-wakeup-node  com.aispeech.aios");
									messageBox.open();
									btn_openFile2.setEnabled(true);
									return;
								}else{
									strReg1 = (s[l].substring(0, findSpace)).trim();
									strReg2 = (s[l].substring(findSpace, s[l].length())).trim();
								}
								
								strSheetName = (s[l].substring(0, findSpace)).trim();
								m_view.setSheetName(l+1, strSheetName); 
								m_view.setSheet(l+1);
					            m_view.getLock();
								
								// 标题行
					            m_view.setTextAsValue(0, 0, "CPU (%)");
					            m_view.setTextAsValue(0, 1, "RSS (M)"); //m_view.setTextAsValue(0, 2, "RSS (M)");
					            m_view.setTextAsValue(0, 3, "Times");
					            m_view.setTextAsValue(0, 4, "PID");
					            m_view.setTextAsValue(0, 5, "TID");
					            m_view.setColWidth(3, 1800); //设置列宽
					            m_view.setColWidth(4, 1800);
					            m_view.setColWidth(5, 1800);
					            m_view.setColWidth(6, 6800);
					            m_view.setColWidth(7, 2200);
					            m_view.setColWidth(8, 2000);
					            m_view.setSelection("G1:G1000");
					            CellFormat cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
					            m_view.setCellFormat(cfmt);
					            m_view.setTextAsValue(0, 7, s[l]);
					            cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
					            cfmt.setFontColor(Color.BLUE.getRGB());
					            m_view.setCellFormat(cfmt, 0, 0, 0, 7);
								
								r = 1;
								reader = new BufferedReader(new FileReader(jfc.getSelectedFile()));
								boolean hasFound = true; //本段数据中已找到该线程的数据，第一行标题不做处理，因此置为true
								boolean hasMarkBreak = false; //已设置的断点
								boolean isEmpty = true; //是否始终没有该线程的数据
//								int aalen = 0;
								lr = 0;
								int cpunum = 2;
								int cpucorenum = 0;
								int cpuSum = 0;
								boolean firstData = true;
								while ((strRead = reader.readLine()) != null) {
									lr++;
									strRead = strRead.trim();
									if( strRead.matches(".*"+strReg1+"\\s+"+strReg2) ){
										firstData = false;
										hasFound = true;
										isEmpty = false;
										hasMarkBreak = false;
										aa = strRead.split("\\s+");
										aa[0] = aa[0].trim();
										aa[1] = aa[1].trim();
										if( !pidLast.equals(aa[1]) ){
											m_view.setTextAsValue(pidRow, 3, String.valueOf(r));
											m_view.setTextAsValue(pidRow, 4, aa[0]);
											m_view.setTextAsValue(pidRow, 5, aa[1]);
											m_view.setTextAsValue(pidRow++, 6, timestamp);
											pidLast = aa[1];
										}
										
										aa[cpuPos] = aa[cpuPos].substring(0, aa[cpuPos].length()-1); //删除%
										if( Double.parseDouble(aa[cpuPos])>100 ){ //如果CPU%超过100%，只显示100%
											aa[cpuPos] = "100";
										}
										cpuSum = cpuSum + Integer.parseInt(aa[cpuPos]);
										
										aa[rssPos] = aa[rssPos].substring(0, aa[rssPos].length()-1); //删除K
										dRss = Double.parseDouble(aa[rssPos])/1024.00;
																			
										
									}else if( strRead.startsWith("PID") ){ //如果到了下一段数据
										if( !hasFound ){ 
											if( !hasMarkBreak ){
												ixy++;
												hasMarkBreak = true;
											}
											r++;
										}else {
											hasFound = false;
											if(!firstData) {
												
												if( xyseries1[ixy]==null )
													xyseries1[ixy] = new XYSeries("CPU"+String.valueOf(ixy));
												if( xyseries2[ixy]==null )
													xyseries2[ixy] = new XYSeries("RSS"+String.valueOf(ixy));
												m_view.setTextAsValue(r, 0, String.valueOf(cpuSum));
												xyseries1[ixy].add(r, Integer.parseInt(aa[cpuPos]));
												m_view.setTextAsValue(r, 1, df.format(dRss)); //m_view.setTextAsValue(r, 2, df.format(dRss));
												xyseries2[ixy].add(r++, dRss);
												cpuSum = 0;
											}
										}
									}else if( strRead.startsWith("Timestamp") ){
										timestamp = strRead;
									}else if( strRead.startsWith("Processor") ){ //获取CPU信息
										if( cpuProcessor.indexOf(strRead)!=-1 ){
											int flg = cpuProcessor.indexOf("×");
											if( flg!=-1 ){
												cpuProcessor = cpuProcessor.substring(0, flg);
											}
											cpuProcessor = cpuProcessor.replaceAll("\r", "");
											cpuProcessor = cpuProcessor.replaceAll("\n", "");
											cpuProcessor = cpuProcessor + " × "+String.valueOf(cpunum++)+"\r\n";
										}else{
											cpuProcessor = cpuProcessor + strRead + "\r\n";
										}
									}else if( strRead.startsWith("processor") ){
										cpucorenum++;
									}else if( strRead.startsWith("Hardware") ){
										cpuHardware = cpuHardware + strRead + "\r\n";
									}else if( strRead.startsWith("Sampling interval") ){
										sampInterval = strRead.substring(strRead.indexOf(":")+1, strRead.length()).trim()+"秒";
									}
								}
								if(r>maxrow)
									maxrow = r; //记录最大行数
								reader.close(); //关闭txt
								if(cpucorenum>0){
									if(!cpuProcessor.equals("")){
										cpuProcessor = cpuProcessor.substring(0, cpuProcessor.length()-2);
									}
									cpuCore = "     [ Cores: "+String.valueOf(cpucorenum)+" ]\r\n";
								}
								cpuinfo = cpuProcessor + cpuCore + cpuHardware;
								if( !cpuinfo.equals("") ){
									cpuinfo = cpuinfo.substring(0, cpuinfo.length()-2);
								}
								if( isEmpty )
									continue;
								
								
								for(int i=0;i<=ixy;i++){
									if( xyseries1[i]!=null)
										xySeriesCollection1.addSeries(xyseries1[i]);
								}
								JFreeChart jfreechart1 = ChartFactory.createXYLineChart("CPU\r\n"+cpuinfo, "Sampling Times", "Value (%)", xySeriesCollection1, PlotOrientation.VERTICAL, true, false, false);
								jfreechart1.getLegend().setVisible(false); 
								XYPlot plot1 = (XYPlot) jfreechart1.getPlot();
								plot1.setBackgroundAlpha(0.5f); 
								plot1.setForegroundAlpha(1f); 
								renderer1 = (XYLineAndShapeRenderer)plot1.getRenderer();
								renderer1.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart1, "config\\temp.png", 900, 340);
						        m_view.addPicture(9, 2, 19, 16, "config\\temp.png"); 
						        xySeriesCollection1 = null;
						        
						        for(int i=0;i<=ixy;i++){
						        	if( xyseries2[i]!=null)
						        		xySeriesCollection2.addSeries(xyseries2[i]);
								}
								JFreeChart jfreechart2 = ChartFactory.createXYLineChart("RSS", "Sampling Times", "Value (M)", xySeriesCollection2, PlotOrientation.VERTICAL, true, false, false);
								jfreechart2.getLegend().setVisible(false); 
								XYPlot plot2 = (XYPlot) jfreechart2.getPlot();
						        plot2.setBackgroundAlpha(0.5f); 
						        plot2.setForegroundAlpha(1f); 
						        renderer2 = (XYLineAndShapeRenderer)plot2.getRenderer();
								renderer2.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart2, "config\\temp.png", 900, 300);
						        m_view.addPicture(9, 16, 19, 28, "config\\temp.png");  m_view.addPicture(8, 30, 18, 42, "config\\temp.png");
						        xySeriesCollection2 = null;
						        
						        
								//插入公式
								m_view.setTextAsValue(5, 7, "最大值");
								m_view.setFormula(5, 8, "MAX(A2:A"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(6, 7, "最小值");
								m_view.setFormula(6, 8, "MIN(A2:A"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(7, 7, "平均值");
								m_view.setFormula(7, 8, "ROUND(AVERAGE(A2:A"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(8, 7, "采样间隔");
								m_view.setText(8, 8, sampInterval);
								
								//插入公式
								m_view.setTextAsValue(17, 7, "最大值");
								m_view.setFormula(17, 8, "MAX(B2:B"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(18, 7, "最小值");
								m_view.setFormula(18, 8, "MIN(B2:B"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(19, 7, "平均值");
								m_view.setFormula(19, 8, "ROUND(AVERAGE(B2:B"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(20, 7, "采样间隔");
								m_view.setText(20, 8, sampInterval);
								
								//设置数据右对齐
								m_view.setSelection("I1:I50");
								cfmt = m_view.getCellFormat();
								cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
								m_view.setCellFormat(cfmt);
								
								//插入饼图公式
								m_view.setTextAsValue(3, 19, "数值分布 (单位:%)");
								m_view.setTextAsValue(4, 19, "0-10%");
								m_view.setTextAsValue(5, 19, "11-20%");
								m_view.setTextAsValue(6, 19, "21-30%");
								m_view.setTextAsValue(7, 19, "31-40%");
								m_view.setTextAsValue(8, 19, "41-50%");
								m_view.setTextAsValue(9, 19, "51-60%");
								m_view.setTextAsValue(10, 19, "61-70%");
								m_view.setTextAsValue(11, 19, "71-80%");
								m_view.setTextAsValue(12, 19, "81-90%");
								m_view.setTextAsValue(13, 19, "91-100%");
								m_view.setFormula(4, 20, "ROUND(COUNTIF(A:A,\"<=10\")/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(5, 20, "ROUND((COUNTIF(A:A,\"<=20\")-COUNTIF(A:A,\"<11\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(6, 20, "ROUND((COUNTIF(A:A,\"<=30\")-COUNTIF(A:A,\"<21\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(7, 20, "ROUND((COUNTIF(A:A,\"<=40\")-COUNTIF(A:A,\"<31\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(8, 20, "ROUND((COUNTIF(A:A,\"<=50\")-COUNTIF(A:A,\"<41\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(9, 20, "ROUND((COUNTIF(A:A,\"<=60\")-COUNTIF(A:A,\"<51\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(10, 20, "ROUND((COUNTIF(A:A,\"<=70\")-COUNTIF(A:A,\"<61\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(11, 20, "ROUND((COUNTIF(A:A,\"<=80\")-COUNTIF(A:A,\"<71\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(12, 20, "ROUND((COUNTIF(A:A,\"<=90\")-COUNTIF(A:A,\"<81\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setFormula(13, 20, "ROUND((COUNTIF(A:A,\"<=100\")-COUNTIF(A:A,\"<91\"))/COUNTIF(A:A,\">=0\")*100,2)");
								m_view.setSelection("T5:T50");
								cfmt = m_view.getCellFormat();
								cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
								m_view.setCellFormat(cfmt);
								
						        //添加数值分布饼图
						        ChartShape chartpie = m_view.addChart(21, 4, 26, 14);
						        chartpie.setChartType(ChartShape.TypePie);
						        chartpie.initData(new RangeRef(19,4,20,13), false);
						        chartpie.setVaryColors(true);
								
							} // if end
							
						} //for end
						
						
						//CPU求和总表
						m_view.setSheetName(s.length+1, "Subtotal_CPU"); 
						m_view.setSheet(s.length+1);
						//m_view.getLock();
						
						
						XYSeriesCollection xySeriesCollection3;
				        XYSeries xyseries3 = new XYSeries("CPUTOTAL");
						XYLineAndShapeRenderer renderer3;
						String stmp = "";
						
						for(int l=0; l<s.length; l++){
							m_view.setSheet(s.length+1);
							m_view.setTextAsValue(0, l, m_view.getSheetName(l+1)); // 标题行
				            m_view.setColWidth(l, 2000); //设置列宽
				            for(int p=1; p<=maxrow; p++){
				            	m_view.setSheet(l+1);
				            	stmp = m_view.getText(p, 0);
				            	m_view.setSheet(s.length+1);
				            	m_view.setTextAsValue(p, l, stmp);
				            }
						}
						
						//插入公式
						m_view.setTextAsValue(0, s.length+2, "Subtotal_CPU");
						m_view.setColWidth(s.length+2, 2200);
			            m_view.setColWidth(s.length+3, 2000);
						m_view.setTextAsValue(0, s.length, "SUM");
						m_view.setFormula(1, s.length, "SUM(A2:"+ExcelColName[s.length-1]+"2)");
						m_view.setSelection(ExcelColName[s.length]+"2:"+ExcelColName[s.length]+String.valueOf(maxrow));
						m_view.editCopyDown();
						m_view.setTextAsValue(5, s.length+2, "最大值");
						m_view.setFormula(5, s.length+3, "MAX("+ExcelColName[s.length]+"2:"+ExcelColName[s.length]+String.valueOf(maxrow)+")");
						m_view.setTextAsValue(6, s.length+2, "最小值");
						m_view.setFormula(6, s.length+3, "MIN("+ExcelColName[s.length]+"2:"+ExcelColName[s.length]+String.valueOf(maxrow)+")");
						m_view.setTextAsValue(7, s.length+2, "平均值");
						m_view.setFormula(7, s.length+3, "ROUND(AVERAGE("+ExcelColName[s.length]+"2:"+ExcelColName[s.length]+String.valueOf(maxrow)+"),2)");
						m_view.setTextAsValue(8, s.length+2, "采样间隔");
						m_view.setText(8, s.length+3, sampInterval);
						
						for(int xx=1; xx<maxrow; xx++){
							if( !m_view.getText(xx, s.length).equals("") )
								xyseries3.add(xx, Integer.parseInt(m_view.getText(xx, s.length)));
						}
						
						xySeriesCollection3 = new XYSeriesCollection();
						xySeriesCollection3.addSeries(xyseries3);
						JFreeChart jfreechart3 = ChartFactory.createXYLineChart("CPU\r\n"+cpuinfo, "Sampling Times", "Value (%)", xySeriesCollection3, PlotOrientation.VERTICAL, true, false, false);
						jfreechart3.getLegend().setVisible(false); 
						XYPlot plot3 = (XYPlot) jfreechart3.getPlot();
						plot3.setBackgroundAlpha(0.5f); 
						plot3.setForegroundAlpha(1f); 
						renderer3 = (XYLineAndShapeRenderer)plot3.getRenderer();
						renderer3.setSeriesPaint(0, Color.BLUE);
				        saveAsFile(jfreechart3, "config\\temp.png", 900, 340);
				        m_view.addPicture(s.length+4, 2, s.length+14, 16, "config\\temp.png"); 
				        xySeriesCollection3 = null;
						
						//设置格式
			            m_view.setSelection("A1:AZ1");
			            CellFormat cfmt = m_view.getCellFormat();
			            cfmt.setFontColor(Color.BLUE.getRGB());
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
						m_view.setCellFormat(cfmt);
						m_view.setSelection(ExcelColName[s.length]+"1:"+ExcelColName[s.length]+String.valueOf(maxrow));
			            cfmt = m_view.getCellFormat();
			            cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						m_view.setSelection(ExcelColName[s.length+3]+"1:"+ExcelColName[s.length+3]+"50");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
						m_view.setCellFormat(cfmt);
						//m_view.releaseLock();
						
						//插入饼图公式
						m_view.setTextAsValue(18, s.length+5, "数值分布 (单位:%)");
						m_view.setTextAsValue(19, s.length+5, "0-10%");
						m_view.setTextAsValue(20, s.length+5, "11-20%");
						m_view.setTextAsValue(21, s.length+5, "21-30%");
						m_view.setTextAsValue(22, s.length+5, "31-40%");
						m_view.setTextAsValue(23, s.length+5, "41-50%");
						m_view.setTextAsValue(24, s.length+5, "51-60%");
						m_view.setTextAsValue(25, s.length+5, "61-70%");
						m_view.setTextAsValue(26, s.length+5, "71-80%");
						m_view.setTextAsValue(27, s.length+5, "81-90%");
						m_view.setTextAsValue(28, s.length+5, "91-100%");
						String strColName = ExcelColName[s.length]+":"+ExcelColName[s.length];
						m_view.setFormula(19, s.length+6, "ROUND(COUNTIF("+strColName+",\"<=10\")/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(20, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=20\")-COUNTIF("+strColName+",\"<11\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(21, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=30\")-COUNTIF("+strColName+",\"<21\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(22, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=40\")-COUNTIF("+strColName+",\"<31\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(23, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=50\")-COUNTIF("+strColName+",\"<41\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(24, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=60\")-COUNTIF("+strColName+",\"<51\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(25, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=70\")-COUNTIF("+strColName+",\"<61\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(26, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=80\")-COUNTIF("+strColName+",\"<71\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(27, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=90\")-COUNTIF("+strColName+",\"<81\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setFormula(28, s.length+6, "ROUND((COUNTIF("+strColName+",\"<=100\")-COUNTIF("+strColName+",\"<91\"))/COUNTIF("+strColName+",\">=0\")*100,2)");
						m_view.setSelection(ExcelColName[s.length+5]+"20:"+ExcelColName[s.length+5]+"50");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						
				        //添加数值分布饼图
				        ChartShape chartpie = m_view.addChart(s.length+7, 19, s.length+12, 29);
				        chartpie.setChartType(ChartShape.TypePie);
				        chartpie.initData(new RangeRef(s.length+5,19,s.length+6,28), false);
				        chartpie.setVaryColors(true);
				        
				        
						
						//Summary
						m_view.setSheetName(0, "Summary"); 
						m_view.setSheet(0);
						//m_view.getLock();
						// 标题行
			            m_view.setTextAsValue(0, 0, "Thread");
			            m_view.setColWidth(0, 8000); //设置列宽
			            m_view.setTextAsValue(0, 1, "CPU Max(%)");
			            m_view.setColWidth(1, 3000);
			            m_view.setTextAsValue(0, 2, "CPU Min(%)");
			            m_view.setColWidth(2, 3000);
			            m_view.setTextAsValue(0, 3, "CPU Avg(%)");
			            m_view.setColWidth(3, 3000);
			            m_view.setTextAsValue(0, 4, "RSS Max(M)");
			            m_view.setColWidth(4, 3000);
			            m_view.setTextAsValue(0, 5, "RSS Min(M)");
			            m_view.setColWidth(5, 3000);
			            m_view.setTextAsValue(0, 6, "RSS Avg(M)");
			            m_view.setColWidth(6, 3000);
			            cfmt = m_view.getCellFormat();
			            cfmt.setFontColor(Color.BLUE.getRGB());
			            m_view.setCellFormat(cfmt, 0, 0, 0, 6);
			            stmp = "";
			            for( int sr=1; sr<=s.length; sr++ ){
			            	//Thread
			            	m_view.setSheet(sr);
			            	stmp = m_view.getSheetName(sr);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 0, stmp);
			            	//CPU Max
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(5, 8);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 1, stmp);
			            	//CPU Min
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(6, 8);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 2, stmp);
			            	//CPU Avg
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(7, 8);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 3, stmp);
			            	//RSS Max
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(17, 8);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 4, stmp);
			            	//RSS Min
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(18, 8);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 5, stmp);
			            	//RSS Avg
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(19, 8);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 6, stmp);
			            }
			            m_view.setSheet(s.length+1);
		            	stmp = m_view.getText(5, s.length+3);
		            	m_view.setSheet(0);
		            	m_view.setTextAsValue(s.length+1, 0, "Subtotal_CPU");
		            	m_view.setTextAsValue(s.length+1, 1, stmp);
		            	m_view.setSheet(s.length+1);
		            	stmp = m_view.getText(6, s.length+3);
		            	m_view.setSheet(0);
		            	m_view.setTextAsValue(s.length+1, 2, stmp);
		            	m_view.setSheet(s.length+1);
		            	stmp = m_view.getText(7, s.length+3);
		            	m_view.setSheet(0);
		            	m_view.setTextAsValue(s.length+1, 3, stmp);
			            
			            //设置格式
			            m_view.setSelection("A1:A1");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("B1:H200");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("D1:D200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("G1:G200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						
						String maxLine = String.valueOf(s.length+1);
						
						//绘图区
			            ChartShape chart2 = m_view.addChart(9, 2, 18, 20);
			            chart2.setChartType(ChartShape.TypeColumn);
			            chart2.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$D$2:$D$"+maxLine, false);
			            chart2.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
			            chart2.addSeries();
			            chart2.setAxisTitle(ChartShape.YAxis, 0, "CPU Avg(%)");
			            chart2.setLegendVisible(false);
			            
						//excel写出路径
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
			            m_view.write(currentDir+"CPU内存线程数据_"+format.format(date)+".xls");
			            m_view.releaseLock();
					}
					catch (Exception e2){
						e2.printStackTrace();
						FileWriter fwriter = null;
						String strWrite = null;
						try {
							fwriter = new FileWriter(currentDir+"ErrorLog.txt", false);
							fwriter.write("Line: "+lr+"\r\n"+e2.getMessage());
							fwriter.close();
							MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
							messageBox.setMessage("数据解析出现异常，请查看ErrorLog.txt！");
							messageBox.open();
							btn_openFile2.setEnabled(true);
							return;
						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
					
				}else{
					btn_openFile2.setEnabled(true);
					return;
				}
				
				//文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text2", false);
					fwriter2.write(text_2.getText());
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				messageBox.setMessage("数据解析完毕，解析时对个别不合理数据点进行了处理！");
				messageBox.open();
				btn_openFile2.setEnabled(true);
				
			}
		});
		btn_openFile2.setBounds(23, 439, 80, 27);
		btn_openFile2.setText("\u89E3\u6790\u6570\u636E");
		
		org.eclipse.swt.widgets.Label lblNewLabel = new org.eclipse.swt.widgets.Label(container, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblNewLabel.setBounds(20, 292, 202, 17);
		lblNewLabel.setText("Top\u67E5\u770B\u7EBF\u7A0B\uFF1Atop -s cpu -t | grep");
		
		org.eclipse.swt.widgets.Label lblAdbShelltop = new org.eclipse.swt.widgets.Label(container, SWT.NONE);
		lblAdbShelltop.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblAdbShelltop.setBounds(20, 51, 175, 17);
		lblAdbShelltop.setText("Top\u67E5\u770B\u6240\u6709\u8FDB\u7A0B\uFF1Atop -s cpu");
		
		
		//连接设备
		final Button btn_ConnDevice = new Button(container, SWT.NONE);
		btn_ConnDevice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				btn_ConnDevice.setEnabled(false);
				btn_StartHprof.setEnabled(false);
				btn_StopHprof.setEnabled(false);
				btn_StartTop1.setEnabled(false);
				btn_StartTop2.setEnabled(false);
            	btn_StartMem.setEnabled(false);
            	btn_StartProcrank.setEnabled(false);
            	btn_StopTop1.setEnabled(false);
				btn_StopTop2.setEnabled(false);
            	btn_StopMem.setEnabled(false);
            	btn_StopProcrank.setEnabled(false);
            	btn_catchInfo.setEnabled(false);
            	btn_ManualHprof.setEnabled(false);
            	
				try {
		            final Runtime rt = Runtime.getRuntime();
		            Process pr = null;
		            BufferedReader input = null;
		            String line = null;
		            String strName = "";
		            
		            String strCmd = "adb devices";
		            pr = rt.exec(strCmd);
		            
		            input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
		            input.readLine();
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
		            	btn_ConnDevice.setFocus();
		            }
		            else if(r==1){
		            	pr = rt.exec("adb shell \"getprop ro.build.version.release\"");
			            input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
			            line=input.readLine();
		            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
		            	messageBox.setMessage("机器已连接，设备号为 " + strName + "\r\n安卓版本号为 "+line);
		            	messageBox.open();
		            	btn_ConnDevice.setEnabled(true);
		            	if(stopHprof){
		            		btn_StartHprof.setEnabled(true);
		            		btn_StopHprof.setEnabled(false);
		            	}else{
		            		btn_StartHprof.setEnabled(false);
		            		btn_StopHprof.setEnabled(true);
		            	}
		            	if(stopTop1){
		            		btn_StartTop1.setEnabled(true);
		            		btn_StopTop1.setEnabled(false);
		            	}else{
		            		btn_StartTop1.setEnabled(false);
		            		btn_StopTop1.setEnabled(true);
		            	}
		            	if(stopTop2){
		            		btn_StartTop2.setEnabled(true);
		            		btn_StopTop2.setEnabled(false);
		            	}else{
		            		btn_StartTop2.setEnabled(false);
		            		btn_StopTop2.setEnabled(true);
		            	}
		            	if(stopMem){
		            		btn_StartMem.setEnabled(true);
		            		btn_StopMem.setEnabled(false);
		            	}else{
		            		btn_StartMem.setEnabled(false);
		            		btn_StopMem.setEnabled(true);
		            	}
		            	if(stopProcrank){
		            		btn_StartProcrank.setEnabled(true);
		            		btn_StopProcrank.setEnabled(false);
		            	}else{
		            		btn_StartProcrank.setEnabled(false);
		            		btn_StopProcrank.setEnabled(true);
		            	}
		            	btn_catchInfo.setEnabled(true);
		            	btn_ManualHprof.setEnabled(true);
		            	btn_ConnDevice.setFocus();
		            }
		            else {
		            	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
		            	messageBox.setMessage("多台设备连接，请断开其他设备！");
		            	messageBox.open();
		            	btn_ConnDevice.setEnabled(true);
		            	btn_ConnDevice.setFocus();
		            }
		            
		        } catch (Exception ex) {
//		            System.out.println(ex.toString());
//		            ex.printStackTrace();
		        }
				
				btn_ConnDevice.setEnabled(true);
				
			}
		});
		btn_ConnDevice.setBounds(24, 10, 90, 27);
		btn_ConnDevice.setText("\u68C0\u6D4B\u673A\u5668\u8FDE\u63A5");
		
		Label lblProcrank = new Label(container, SWT.NONE);
		lblProcrank.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblProcrank.setText("\u67E5\u770BPSS\u5185\u5B58\uFF1Adumpsys meminfo");
		lblProcrank.setBounds(438, 51, 201, 17);
		
		
		//开始执行dumpsys meminfo指令
		btn_StartMem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StartMem.setEnabled(false);
				checkBtn_fdThreads.setEnabled(false);
				if(text_3.getText().trim().equals("")){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
					messageBox.setMessage("请填写要测试的进程包名！");
					messageBox.open();
					btn_StartMem.setEnabled(true);
					checkBtn_fdThreads.setEnabled(true);
					return;
				}
				if(checkBtn_fdThreads.getSelection()){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
					messageBox.setMessage("监控句柄需要root权限，否则监控到的句柄数为0！\r\n如果设备尚未root，请确认root后再点击【确定】开始采集！");
					messageBox.open();
				}
				
				new Thread (new Runnable() {
					String strSec = text3_sec.getText().trim(); //采样间隔
          		    String strProc = text_3.getText().trim(); //待测试进程
          		    boolean fdThreads = checkBtn_fdThreads.getSelection();
		                  public void run() {
		                	 try {
		                		  String[] sProc = new String[10]; //最多支持测试10个进程
		                		  sProc = strProc.split("\r\n");
		                		  
		                		  Date date=new Date();
		                		  DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
		                		  String time=format.format(date);
		                		  Date date2=new Date();
	                			  FileWriter fwriter, fwriter2=null;
	                			  Runtime rt=Runtime.getRuntime(), rt2=Runtime.getRuntime();
				    		      Process pr=null, pr2=null;
				    		      BufferedReader input=null, input2=null;
				    		      String line=null, line2=null;
				    		      
				    		      fwriter = new FileWriter("meminfo-"+time+".txt", true);
	                			  fwriter.write("Sampling interval : "+strSec+"\r\n");
	                			  fwriter.close();
	                			  int sec = Integer.parseInt(strSec)*1000;
	                			  if(fdThreads){
	                				  fwriter2 = new FileWriter("fdThreads-"+time+".txt", true); 
	                			  }
	                			  if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("已开始内存数据采集！");
	 			                			messinfo.open();
	 		                            }
	 		                        });
			            		  }
	                			  fwriter = new FileWriter("meminfo-"+time+".txt", true);
	                			  stopMem = false;
	                			  int wasteSec = 0; 
	                			  int count = 0; //采集次数
		                		  while( !stopMem ){
		                			  date=new Date();
		                			  fwriter.write("\r\n\r\nTimestamp: "+format.format(date)+"\r\n");
		                			  if(fdThreads){
		                				  fwriter2.write("\r\n\r\nTimestamp: "+format.format(date)+"\r\n");
		                			  }
			                		  for(int i=0; i<sProc.length; i++){
			                			  pr = rt.exec("adb shell dumpsys meminfo "+sProc[i]);
										  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
										  while ((line=input.readLine()) != null) {
											  if( !line.equals("") & !line.contains("warning") )
												  fwriter.write(line+"\r\n");
										  }
										  fwriter.write("\r\n\r\n");
										  fwriter.flush();
										  if(fdThreads){
											  String myPid = "";
											  String[] dateAfterSplit = new String[20];
											  pr2 = rt2.exec("adb shell \"ps | grep "+ sProc[i] +"$\""); 
											  pr2.waitFor();
									          input2 = new BufferedReader(new InputStreamReader(pr2.getInputStream(), "GBK"));
									          while ( (line2=input2.readLine()) != null ) {
									            	line2 = line2.trim();
									            	dateAfterSplit = line2.split("\\s+");
									            	if( !line2.equals("") ){
									            		myPid = dateAfterSplit[1];
									            	}
									          }
											  pr2 = rt2.exec("adb shell \"ls -l /proc/"+myPid+"/fd | grep '' -c\""); //采集句柄数
											  input2 = new BufferedReader(new InputStreamReader(pr2.getInputStream(), "GBK"));
											  if ((line2=input2.readLine()) != null) {
												  fwriter2.write(sProc[i]+"句柄数："+line2+"\r\n");
												  fwriter2.flush();
											  }
											  if(count%100==0){
												  pr2 = rt2.exec("adb shell \"ls -l /proc/"+myPid+"/fd\"");
												  input2 = new BufferedReader(new InputStreamReader(pr2.getInputStream(), "GBK"));
												  String tmp = "";
												  while ((line2=input2.readLine()) != null) {
													  if(!line2.equals("")){
														  tmp = tmp + line2 + "\r\n";
													  }
												  }
												  fwriter2.write(sProc[i]+"句柄：\r\n"+tmp+"\r\n");
												  fwriter2.flush();
											  }
											  
											  pr2 = rt2.exec("adb shell \"ps -t "+myPid+" | grep '' -c\""); //采集线程数
											  input2 = new BufferedReader(new InputStreamReader(pr2.getInputStream(), "GBK"));
											  if ((line2=input2.readLine()) != null) {
												  fwriter2.write(sProc[i]+"线程数："+line2+"\r\n");
												  fwriter2.flush();
											  }
											  if(count%100==0){
												  pr2 = rt2.exec("adb shell \"ps -t "+myPid+"\"");
												  input2 = new BufferedReader(new InputStreamReader(pr2.getInputStream(), "GBK"));
												  String tmp = "";
												  while ((line2=input2.readLine()) != null) {
													  if(!line2.equals("")){
														  tmp = tmp + line2 + "\r\n";
													  }
												  }
												  fwriter2.write(sProc[i]+"线程：\r\n"+tmp+"\r\n");
												  fwriter2.flush();
											  }
			                			  }
			                		  }
									  date2=new Date();
									  wasteSec = (int)(date2.getTime()-date.getTime());
									  if(wasteSec<sec){
										  Thread.sleep(sec-wasteSec);
									  }
									  count++;
			                	  }
		                		  fwriter.close();
		                		  if(fdThreads){
		                			  fwriter2.close();
		                		  }
		                		  
							 } catch (final Exception ex) {
								 stopMem = true;
								 tips4 = false;
								 if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	ex.printStackTrace();
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("错误信息：\r\n"+ex.getMessage());
	 			                			messinfo.open();
	 			                			btn_StartMem.setEnabled(true);
	 			                			checkBtn_fdThreads.setEnabled(true);
	 			                			btn_StopMem.setEnabled(false);
	 			                			return;
	 		                            }
	 		                        });
			            		}
							 }
		                	 stopMem = true;
		                	 if( !display.isDisposed() ){
		            				display.syncExec(new Runnable() {
 		                            public void run() {
 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
 			                			messinfo.setMessage("已停止内存数据采集！");
 			                			messinfo.open();
 			                			btn_StartMem.setEnabled(true);
 			                			checkBtn_fdThreads.setEnabled(true);
 		                            }
 		                        });
		            		}
		                	
		                 }
		        }).start();
				
				
				tips3 = true;
				new Thread() { //信息提示栏
   	            	int hour = 0;
					int min = 0;
					int sec = 0;
		   	        public void run() {
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips3.setText("");
	                            	label_tips3.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
	                            }
	                        });
            			}
		   	        	try {
		   	                 Thread.sleep(1000);
		   	            } catch (Exception e1) {
		   	                 e1.printStackTrace();
		   	            }
		   	        	while( tips3 ) {
			   	             display.syncExec(new Runnable() {
								public void run() {
									try {
										sec += 1;
										if (sec >= 60) {
											sec = 0;
											min += 1;
										}
										if (min >= 60) {
											min = 0;
											hour += 1;
										}
										String strTime = "";
										if (hour < 10)
											strTime = "0" + hour + ":";
										else
											strTime = hour + ":";
										if (min < 10)
											strTime = strTime + "0" + min + ":";
										else
											strTime = strTime + min + ":";
										if (sec < 10)
											strTime = strTime + "0" + sec;
										else
											strTime = strTime + sec;
										
										label_tips3.setText(strTime); // 打印计时情况

									} catch (Exception e) {
										e.printStackTrace();
									}
			   	                }
			   	              });
			   	              try {
			   	                 Thread.sleep(1000);
			   	              } catch (Exception e1) {
			   	                 e1.printStackTrace();
			   	              }
			   	        } //while end
		   	        	
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips3.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
	                            }
	                        });
            			}
		   	        	
		   	       }
		   	    }.start();
		   	    

				//文本框写入配置文件
				FileWriter fwriter3 = null;
				try {
					fwriter3 = new FileWriter("config\\text3", false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					fwriter3.write(text_3.getText());
					fwriter3.close();
				} catch (IOException ef) {
					ef.printStackTrace();
				}
		   	    
				
				//文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				btn_StopMem.setEnabled(true);
			}
		});
		btn_StartMem.setEnabled(false);
		btn_StartMem.setText("\u5F00\u59CB\u91C7\u96C6");
		btn_StartMem.setBounds(472, 108, 80, 27);
		
		
		//打开文件3
		final Button btn_openFile3 = new Button(container, SWT.NONE);
		btn_openFile3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_openFile3.setEnabled(false);
				
				String str = (text_3.getText()).trim();  //要解析的进程名
				String[] s = new String[100];
				if( str.equals("") ){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
			    	messageBox.setMessage("请填写要解析的进程名，以换行分隔！");
			    	messageBox.open();
			    	btn_openFile3.setEnabled(true);
					return;
				}
				else{
					s = str.split("\\n+");
				}
				
				JFileChooser jfc=new JFileChooser(currentDir);
				jfc.setDialogTitle("请选择 meminfo- 开头的数据文件...");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
			 	jfc.setFileFilter(filter);
				int returnVal = jfc.showOpenDialog(null); 
				if(returnVal == JFileChooser.APPROVE_OPTION){
					
					currentDir = jfc.getCurrentDirectory()+"";
					if(!currentDir.endsWith("\\"))
						currentDir = currentDir + "\\";
				    if( !(jfc.getSelectedFile().getName()).startsWith("meminfo-") ){
				    	MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING); 
				    	messageBox.setMessage("请选择 meminfo- 开头的文件！");
				    	messageBox.open();
				    	btn_openFile3.setEnabled(true);
				    	return;
				    }
					
				    
					BufferedReader reader;
					String strRead = null;
					int lr = 0; //当前行数
					int N = s.length;
					maxrow = 0;
					
					try{ //读取txt，整理数据后输出到EXCEL
						
						View m_view = new View();
				        String strSheetName = "";
				        
						// 创建Excel工作表
						int r = 1;
						String[] aa;
						double dPss, dPriDirty;
						DecimalFormat df = new DecimalFormat("#.00");
						String sampInterval = "";
						String pidLast = "";
						String pidtmp = "";
						int pidRow = 1;
						
						//创建Sheet总页数
						if(checkBtn3.getSelection()){
							String strTxt = checkTxt2.getText().trim();
							if(strTxt.equals("")){
								MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
								messageBox.setMessage("请填写要汇总的行数！");
								messageBox.open();
								btn_openFile3.setEnabled(true);
								return;
							}else if(Integer.parseInt(strTxt)>N){
								MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
								messageBox.setMessage("要汇总的行数不能大于总行数！");
								messageBox.open();
								btn_openFile3.setEnabled(true);
								return;
							}else{
								m_view.setNumSheets(N+2);
							}
						}else{
							m_view.setNumSheets(N+1);
						}
						
						
						XYSeriesCollection xySeriesCollection1;
				        XYSeries[] xyseries1 = new XYSeries[1000];
						XYLineAndShapeRenderer renderer1;
						
						int ixy=0;
						String timestamp = ""; //记录时间戳
						
						for(int l=0; l<N; l++){
							
							String title[] = new String[50];
							int iTitle = 0;
							int totalColIndex = 0;
							
					        xySeriesCollection1 = null;
							for(int x=0; x<1000; x++){
								xyseries1[x] = null;
							}
							
							pidLast = "";
							pidtmp = "";
							pidRow = 1;
							xySeriesCollection1 = new XYSeriesCollection();
							
							ixy=0;
							xyseries1[ixy] = new XYSeries("PSS");
					        
							s[l] = s[l].trim();
							if( !s[l].equals("") ){
								strSheetName = "Process "+String.valueOf(l+1);
								m_view.setSheetName(l+1, strSheetName); 
								m_view.setSheet(l+1);
					            m_view.getLock();
								
								// 标题行
					            m_view.setTextAsValue(0, 20, "Times");
					            m_view.setTextAsValue(0, 21, "PID");
					            m_view.setColWidth(22, 7500); //设置列宽
					            m_view.setSelection("W:W");
					            CellFormat cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
					            m_view.setCellFormat(cfmt);
					            m_view.setTextAsValue(0, 23, s[l]);
					            cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
					            cfmt.setFontColor(Color.BLUE.getRGB());
					            m_view.setCellFormat(cfmt, 0, 0, 0, 23);
					            
								r = 0;
								reader = new BufferedReader(new FileReader(jfc.getSelectedFile()));
								boolean hasFound = true;
								boolean hasMarkBreak = false; //已设置的断点
								boolean procBegin = false; //是否进入该进程区域
								boolean dataBegin = false; //是否进入数据区域
								
								lr = 0;
								while ((strRead = reader.readLine()) != null) { //每次从txt读一行，直到文件结尾
									lr++;
									strRead = strRead.trim();
									if( strRead.contains("["+s[l]+"]") ){
										procBegin = true;
										dataBegin = false;
										if( strRead.contains("MEMINFO in pid") ){
											pidtmp = strRead.substring(strRead.indexOf("MEMINFO in pid")+14, strRead.indexOf("[")-1).trim();
											if( !pidtmp.equals(pidLast) ){
												m_view.setTextAsValue(pidRow, 20, String.valueOf(r));
												m_view.setTextAsValue(pidRow, 21, pidtmp);
												m_view.setTextAsValue(pidRow++, 22, timestamp);
												pidLast = pidtmp;
											}
										}
										
									}else if( strRead.startsWith("---") ){
										dataBegin = true;
									}else if( procBegin & dataBegin ){
										if( strRead.startsWith("TOTAL") ){
											if( totalColIndex==0 )
												totalColIndex = iTitle;
											procBegin = false;
											dataBegin = false;
										}
										boolean foundTitle = false;
										int col = 0; //要写入的列
										for( int mt=0; mt<iTitle; mt++ ){
											if( strRead.startsWith(title[mt]) ){
												foundTitle = true;
												col = mt;
												break;
											}
										}
										if(!foundTitle){ //如果未找到，则新增一列
											Pattern p = Pattern.compile("\\s{1,}[0-9]");
											Matcher m;
											m = p.matcher(strRead);
											if(m.find()) {
												title[iTitle] = strRead.substring(0, m.start());
												m_view.setTextAsValue(0, iTitle, title[iTitle]);
												col = iTitle;
												iTitle++;
											}
										}
										
										strRead = strRead.replaceAll(title[col], "").trim();
										hasFound = true;
										hasMarkBreak = false;
										aa = strRead.split("\\s+");
										dPss = Double.parseDouble(aa[0])/1024.00;
										
										
										m_view.setTextAsValue(r, col, df.format(dPss));
										if( !procBegin ){ //如果是TOTAL数据列
											if( xyseries1[ixy]==null )
												xyseries1[ixy] = new XYSeries("PSS"+String.valueOf(ixy));
											xyseries1[ixy].add(r, dPss);
										}
										
									}else if( strRead.startsWith("Timestamp") ){
										r++;
										timestamp = strRead;
										if(iTitle>0 & !hasFound){
											r++;
											if( !hasMarkBreak ){
												ixy++;
												hasMarkBreak = true;
											}
										}else{
											hasFound = false;
										}
									}else if( strRead.startsWith("Sampling interval") ){
										sampInterval = strRead.substring(strRead.indexOf(":")+1, strRead.length()).trim()+"秒";
									}
								}
								if(r>maxrow)
									maxrow = r; //记录最大行数
								reader.close(); //关闭txt
								
								for(int i=0;i<=ixy;i++) {
									if( xyseries1[i]!=null)
										xySeriesCollection1.addSeries(xyseries1[i]);
								}
								JFreeChart jfreechart1 = ChartFactory.createXYLineChart("PSS (M)", "Sampling Times", "Value (M)", xySeriesCollection1, PlotOrientation.VERTICAL, true, false, false);
								jfreechart1.getLegend().setVisible(false); 
								XYPlot plot1 = (XYPlot) jfreechart1.getPlot();
								plot1.setBackgroundAlpha(0.5f); 
								plot1.setForegroundAlpha(1f); 
								renderer1 = (XYLineAndShapeRenderer)plot1.getRenderer();
								renderer1.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart1, "config\\temp.png", 900, 300);
						        m_view.addPicture(25, 2, 35, 14, "config\\temp.png"); 
						        xySeriesCollection1 = null;
						        
								//插入公式
						        String strTotalCol = ExcelColName[totalColIndex];
								m_view.setTextAsValue(3, 23, "最大值");
								m_view.setFormula(3, 24, "MAX("+strTotalCol+"2:"+strTotalCol+Integer.valueOf(r+1)+")");
								m_view.setTextAsValue(4, 23, "最小值");
								m_view.setFormula(4, 24, "MIN("+strTotalCol+"2:"+strTotalCol+Integer.valueOf(r+1)+")");
								m_view.setTextAsValue(5, 23, "平均值");
								m_view.setFormula(5, 24, "ROUND(AVERAGE("+strTotalCol+"2:"+strTotalCol+Integer.valueOf(r+1)+"),2)");
								m_view.setTextAsValue(6, 23, "采样间隔");
								m_view.setText(6, 24, sampInterval);
								
								m_view.setSelection(strTotalCol+":"+strTotalCol);
								cfmt = m_view.getCellFormat();
								cfmt.setFontBold(true);
								m_view.setCellFormat(cfmt);
								
								//设置数据右对齐
								m_view.setSelection("Y1:Y50");
								cfmt.setFontBold(false);
								cfmt = m_view.getCellFormat();
								cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
								m_view.setCellFormat(cfmt);
								
							} // if end
							
						} //for end
						
						
						//Summary总表
						m_view.setSheetName(0, "Summary"); 
						m_view.setSheet(0);
						// 标题行
			            m_view.setTextAsValue(0, 0, "");
			            m_view.setColWidth(0, 6000); //设置列宽
			            m_view.setTextAsValue(0, 1, "Pss Max(M)");
			            m_view.setColWidth(1, 3000);
			            m_view.setTextAsValue(0, 2, "Pss Min(M)");
			            m_view.setColWidth(2, 3000);
			            m_view.setTextAsValue(0, 3, "Pss Avg(M)");
			            m_view.setColWidth(3, 3000);
			            CellFormat cfmt = m_view.getCellFormat();
			            cfmt.setFontColor(Color.BLUE.getRGB());
			            m_view.setCellFormat(cfmt, 0, 0, 0, 6);
			            String stmp = "";
			            for( int sr=1; sr<=N; sr++ ){
			            	//参数列
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(0, 23);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 0, stmp);
			            	//Pss Max
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(3, 24);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 1, stmp);
			            	//Pss Min
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(4, 24);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 2, stmp);
			            	//Pss Avg
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(5, 24);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 3, stmp);
			            }
			            //设置格式
			            m_view.setSelection("A1:A1");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("B1:H200");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("D1:D200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("G1:G200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						
						String maxLine = String.valueOf(N+1);
						
						//绘图区
			            ChartShape chart = m_view.addChart(9, 2, 18, 20);
			            chart.setChartType(ChartShape.TypeColumn);
			            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$D$2:$D$"+maxLine, false);
			            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
			            chart.addSeries();
			            chart.setAxisTitle(ChartShape.YAxis, 0, "Pss Avg(M)");
			            chart.setLegendVisible(false);
			            
			            
			            //PSS求和
			            N++;
			            if(checkBtn3.getSelection()){
							XYSeriesCollection xySeriesCollection3;
							XYSeries xyseries3;
							XYLineAndShapeRenderer renderer3;
							xySeriesCollection3 = new XYSeriesCollection();
					        xyseries3 = new XYSeries("PSS");
							
					        strSheetName = "Subtotal";
							m_view.setSheetName(N, strSheetName); 
							
							String stemp = "";
							int M = Integer.parseInt(checkTxt2.getText().trim())+1;
							// 标题行
							for(int p=1; p<M; p++){
				            	m_view.setSheet(p);
				            	stemp = m_view.getText(0, 23);
				            	m_view.setSheet(N);
				            	m_view.setTextAsValue(0, p-1, stemp);
				            	m_view.setColWidth(p-1, 5000);
				            	int totalColNum = 0; //TOTAL位于第几列
				            	m_view.setSheet(p);
				            	for(int sch=0; sch<50; sch++){
				            		if( m_view.getText(0, sch).equals("TOTAL") ){
				            			totalColNum = sch;
				            			break;
				            		}
				            	}
				            	for(int q=1; q<=maxrow; q++){ //数据列
					            	m_view.setSheet(p);
					            	stemp = m_view.getText(q, totalColNum);
					            	m_view.setSheet(N);
					            	m_view.setTextAsValue(q, p-1, stemp);
					            }
							}
							
				            CellFormat cfmt2 = m_view.getCellFormat();
				            cfmt2.setFontColor(Color.BLUE.getRGB());
				            m_view.setCellFormat(cfmt2, 0, 0, 0, 100);
				            
				            
				            //插入公式
				            maxrow++;
				            m_view.setColWidth(M+1, 2200); //设置列宽
				            m_view.setColWidth(M+2, 2000);
							m_view.setTextAsValue(0, M-1, "SUM");
							m_view.setTextAsValue(0, M+1, "Subtotal_PSS");
							m_view.setFormula(1, M-1, "SUM(A2:"+ExcelColName[M-2]+"2)");
							m_view.setSelection(ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow));
							m_view.editCopyDown();
							m_view.setTextAsValue(5, M+1, "最大值");
							m_view.setFormula(5, M+2, "MAX("+ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow)+")");
							m_view.setTextAsValue(6, M+1, "最小值");
							m_view.setFormula(6, M+2, "MIN("+ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow)+")");
							m_view.setTextAsValue(7, M+1, "平均值");
							m_view.setFormula(7, M+2, "ROUND(AVERAGE("+ExcelColName[M-1]+"2:"+ExcelColName[M-1]+String.valueOf(maxrow)+"),2)");
							m_view.setTextAsValue(8, M+1, "采样间隔");
							m_view.setText(8, M+2, sampInterval);
							
							for(int xx=1; xx<maxrow; xx++){
								if( !m_view.getText(xx, M-1).equals("") )
									xyseries3.add(xx, Double.parseDouble(m_view.getText(xx, M-1)));
							}
							
							xySeriesCollection3 = new XYSeriesCollection();
							xySeriesCollection3.addSeries(xyseries3);
							JFreeChart jfreechart3 = ChartFactory.createXYLineChart("PSS TOTAL (M)\r\n", "Sampling Times", "Value (M)", xySeriesCollection3, PlotOrientation.VERTICAL, true, false, false);
							jfreechart3.getLegend().setVisible(false); 
							XYPlot plot3 = (XYPlot) jfreechart3.getPlot();
							plot3.setBackgroundAlpha(0.5f); 
							plot3.setForegroundAlpha(1f); 
							renderer3 = (XYLineAndShapeRenderer)plot3.getRenderer();
							renderer3.setSeriesPaint(0, Color.BLUE);
					        saveAsFile(jfreechart3, "config\\temp.png", 900, 340);
					        m_view.addPicture(M+3, 2, M+13, 16, "config\\temp.png"); 
					        xySeriesCollection3 = null;
							
							//设置格式
				            m_view.setSelection("A1:AZ1");
				            cfmt = m_view.getCellFormat();
				            cfmt.setFontColor(Color.BLUE.getRGB());
							cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
							m_view.setCellFormat(cfmt);
							m_view.setSelection(ExcelColName[M-1]+"1:"+ExcelColName[M-1]+String.valueOf(maxrow));
				            cfmt = m_view.getCellFormat();
				            cfmt.setFontBold(true);
							m_view.setCellFormat(cfmt);
							m_view.setSelection(ExcelColName[M+2]+"1:"+ExcelColName[M+2]+"50");
							cfmt = m_view.getCellFormat();
							cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
							m_view.setCellFormat(cfmt);
							//m_view.releaseLock();
							N++;
							
							
							//文本框写入配置文件
							FileWriter fwriter2 = null;
							try {
								fwriter2 = new FileWriter("config\\text0", false);
								fwriter2.write(checkTxt1.getText().trim()+"\r\n");
								fwriter2.write(text_packname1.getText().trim()+"\r\n");
								fwriter2.write(text_packname3.getText().trim()+"\r\n");
								fwriter2.write(text_packname4.getText().trim()+"\r\n");
								fwriter2.write(checkTxt2.getText().trim()+"\r\n");
								fwriter2.close();
								
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
			            
			            
			            
			            //excel写出路径
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
			            m_view.write(currentDir+"Meminfo内存数据_"+format.format(date)+".xls");
			            m_view.releaseLock();
			            
					}
					catch (Exception e2){
						e2.printStackTrace();
						FileWriter fwriter = null;
						String strWrite = null;
						try {
							fwriter = new FileWriter(currentDir+"ErrorLog.txt", false);
							fwriter.write("Line: "+lr+"\r\n"+e2.getMessage());
							fwriter.close();
							MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
							messageBox.setMessage("数据解析出现异常，请查看ErrorLog.txt！");
							messageBox.open();
							btn_openFile3.setEnabled(true);
							return;
						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
					
				}else{
					btn_openFile3.setEnabled(true);
					return;
				}
				
				//文本框写入配置文件
				FileWriter fwriter1 = null;
				try {
					fwriter1 = new FileWriter("config\\text3", false);
					fwriter1.write(text_3.getText());
					fwriter1.close();
				} catch (Exception ef) {
					ef.printStackTrace();
				}
				
				//文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				messageBox.setMessage("数据解析完毕！");
				messageBox.open();
				
				btn_openFile3.setEnabled(true);
			}
		});
		btn_openFile3.setText("\u89E3\u6790\u6570\u636E");
		btn_openFile3.setBounds(472, 172, 80, 27);
		
		
		// 读取配置文件
		config = new File("config\\text3");
		try {
			if(!config.exists()) {
				config.createNewFile();
			}
			else{
				readConfig = new BufferedReader(new FileReader(config));
				while ((strTemp = readConfig.readLine()) != null) { 
					strConfig = strConfig + strTemp + "\r\n";
				}
				readConfig.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		text_3 = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_3.setText(strConfig);
		text_3.setBounds(569, 78, 296, 145);
		strTemp = "";
		strConfig = "";
		btn_ConnDevice.setFocus();
		
		//打开目录
		final Button btnOpenDir = new Button(container, SWT.NONE);
		btnOpenDir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnOpenDir.setEnabled(false);
				
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
				
				btnOpenDir.setEnabled(true);
			}
		});
		btnOpenDir.setText("\u6253\u5F00\u5DE5\u5177\u76EE\u5F55");
		btnOpenDir.setBounds(132, 10, 90, 27);
		
		
		
		// 读取配置文件
		config = new File("config\\text0");
		try {
			if(!config.exists()) {
				config.createNewFile();
			}
			else{
				readConfig = new BufferedReader(new FileReader(config));
				strConfig = readConfig.readLine().trim();
				checkTxt1.setText(strConfig);
				strConfig = readConfig.readLine().trim();
				text_packname1.setText(strConfig);
				strConfig = readConfig.readLine().trim();
				text_packname3.setText(strConfig);
				strConfig = readConfig.readLine().trim();
				text_packname4.setText(strConfig);
				strConfig = readConfig.readLine().trim();
				checkTxt2.setText(strConfig);
				readConfig.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		text_packname1.setBounds(224, 289, 155, 23);
		strTemp = "";
		strConfig = "";
		btn_ConnDevice.setFocus();
		text3_sec.setBounds(694, 48, 25, 23);
		
		Label label_3 = new Label(container, SWT.NONE);
		label_3.setText("\u91C7\u6837\u95F4\u9694");
		label_3.setBounds(640, 51, 50, 17);
		
		Label lblS = new Label(container, SWT.NONE);
		lblS.setText("\u79D2 (\u81F3\u5C115\u79D2)");
		lblS.setBounds(722, 51, 70, 17);
		
		
		//停止执行meminfo指令
		btn_StopMem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StopMem.setEnabled(false);
				stopMem = true;
				tips3 = false;
			}
		});
		btn_StopMem.setText("\u505C\u6B62\u91C7\u96C6");
		btn_StopMem.setEnabled(false);
		btn_StopMem.setBounds(472, 140, 80, 27);
		
		Label lblProcrankdumpsysMeminfo = new Label(container, SWT.NONE);
		lblProcrankdumpsysMeminfo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblProcrankdumpsysMeminfo.setText("\u67E5\u770B\u6240\u6709\u8FDB\u7A0B\u7684\u5185\u5B58\uFF1Aprocrank");
		lblProcrankdumpsysMeminfo.setBounds(482, 237, 181, 17);
		
		Label label_6 = new Label(container, SWT.NONE);
		label_6.setText("\u91C7\u6837\u95F4\u9694");
		label_6.setBounds(688, 236, 50, 17);
		text4_sec.setBounds(742, 233, 25, 23);
		
		Label lblSs = new Label(container, SWT.NONE);
		lblSs.setText("\u79D2\uFF08\u5EFA\u8BAE\u81F3\u5C115\u79D2\uFF09");
		lblSs.setBounds(770, 236, 98, 17);
		
		
		//打开文件4
		final Button btn_openFile4 = new Button(container, SWT.NONE);
		btn_openFile4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_openFile4.setEnabled(false);
				
				String str = (text_4.getText()).trim();
				String[] s; //存放待查询字符串
				if( str.equals("") ){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
			    	messageBox.setMessage("请填写要解析的内容项，以换行分隔！");
			    	messageBox.open();
			    	btn_openFile4.setEnabled(true);
					return;
				}else{
					s = str.split("\\r\\n+");
				}
				
				JFileChooser jfc=new JFileChooser(currentDir); 
				jfc.setDialogTitle("请选择 procrank- 开头的数据文件...");
			 	FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
			 	jfc.setFileFilter(filter);
				int returnVal = jfc.showOpenDialog(null); 
				if(returnVal == JFileChooser.APPROVE_OPTION){

					currentDir = jfc.getCurrentDirectory()+"";
					if(!currentDir.endsWith("\\"))
						currentDir = currentDir + "\\";
					if( !(jfc.getSelectedFile().getName()).startsWith("procrank-") ){
						MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
						messageBox.setMessage("请选择 procrank- 开头的数据文件！");
						messageBox.open();
						btn_openFile4.setEnabled(true);
						return;
					}
					
					BufferedReader reader;
					String strRead = null;
					int lr = 1; //文件的行数
					
					try{ //读取txt，整理数据后输出到EXCEL
						
						View m_view = new View();
				        String strSheetName = "";
				        
						int r = 1;
						String[] aa;
						double dVss, dRss, dPss, dUss;
						DecimalFormat df = new DecimalFormat("#.00");
						m_view.setNumSheets(s.length+1);
						
						
						XYSeriesCollection xySeriesCollection1;
						XYSeriesCollection xySeriesCollection2;
						XYSeriesCollection xySeriesCollection3;
				        XYSeries[] xyseries1 = new XYSeries[1000];
				        XYSeries[] xyseries2 = new XYSeries[1000];
				        XYSeries[] xyseries3 = new XYSeries[1000];
						XYLineAndShapeRenderer renderer1, renderer2, renderer3;
						String cpuinfo = "";
						String sampInterval = "";
						String pidLast = "";
						int pidRow = 1;
						
						int ixy=0;
						String timestamp = ""; //记录时间戳
						
						for(int l=0; l<s.length; l++){
							
							xySeriesCollection1 = null;
							xySeriesCollection2 = null;
							xySeriesCollection3 = null;
							for(int x=0; x<1000; x++){
								xyseries1[x] = null;
								xyseries2[x] = null;
								xyseries3[x] = null;
							}
							
							cpuinfo = "";
							pidLast = "";
							pidRow = 1;
							xySeriesCollection1 = new XYSeriesCollection();
							xySeriesCollection2 = new XYSeriesCollection();
							xySeriesCollection3 = new XYSeriesCollection();
							
							ixy=0;
					        xyseries1[ixy] = new XYSeries("RSS");
							xyseries2[ixy] = new XYSeries("PSS");
							xyseries3[ixy] = new XYSeries("USS");
							
							s[l] = s[l].trim();
							if( !s[l].equals("") ){
								strSheetName = "Process "+String.valueOf(l+1);
								m_view.setSheetName(l+1, strSheetName); 
								m_view.setSheet(l+1);
					            m_view.getLock();
								
								// 标题行
					            m_view.setTextAsValue(0, 0, "VSS (M)");
					            m_view.setTextAsValue(0, 1, "RSS (M)");
					            m_view.setTextAsValue(0, 2, "Pss (M)");
					            m_view.setTextAsValue(0, 3, "USS (M)");
					            m_view.setTextAsValue(0, 5, "Times");
					            m_view.setTextAsValue(0, 6, "PID");
					            m_view.setColWidth(7, 7500); //设置列宽
					            m_view.setSelection("H1:H1000");
					            CellFormat cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
					            m_view.setCellFormat(cfmt);
					            m_view.setTextAsValue(0, 9, s[l]);
					            cfmt = m_view.getCellFormat();
					            cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
					            cfmt.setFontColor(Color.BLUE.getRGB());
					            m_view.setCellFormat(cfmt, 0, 0, 0, 10);
					            
								r = 1;
								reader = new BufferedReader(new FileReader(jfc.getSelectedFile()));
								boolean hasFound = true; //本段数据中已找到该进程的数据，第一行标题不做处理，因此置为true
								boolean hasMarkBreak = false; //已设置的断点
								boolean isEmpty = true; //是否始终没有该进程的数据
//								int aalen = 0;
								lr = 0;
								int cpucore = 2;
								while ((strRead = reader.readLine()) != null) { //每次从txt读一行，直到文件结尾
									lr++;
									strRead = strRead.trim();
									
									if( strRead.endsWith( s[l]) ){
										hasFound = true;
										isEmpty = false;
										hasMarkBreak = false;
										aa = strRead.split("\\s+");
										aa[0] = aa[0].trim();
										if( !pidLast.equals(aa[0]) ){
											m_view.setTextAsValue(pidRow, 5, String.valueOf(r));
											m_view.setTextAsValue(pidRow, 6, aa[0]);
											m_view.setTextAsValue(pidRow++, 7, timestamp);
											pidLast = aa[0];
										}
										
										aa[1] = aa[1].substring(0, aa[1].length()-1); //删除K
										dVss = Double.parseDouble(aa[1])/1024.00;
										aa[2] = aa[2].substring(0, aa[2].length()-1); //删除K
										dRss = Double.parseDouble(aa[2])/1024.00;
										aa[3] = aa[3].substring(0, aa[3].length()-1); //删除K
										dPss = Double.parseDouble(aa[3])/1024.00;
										aa[4] = aa[4].substring(0, aa[4].length()-1); //删除K
										dUss = Double.parseDouble(aa[4])/1024.00;
										
										
										if( xyseries1[ixy]==null )
											xyseries1[ixy] = new XYSeries("RSS"+String.valueOf(ixy));
										if( xyseries2[ixy]==null )
											xyseries2[ixy] = new XYSeries("PSS"+String.valueOf(ixy));
										if( xyseries3[ixy]==null )
											xyseries3[ixy] = new XYSeries("USS"+String.valueOf(ixy));
										
										
										m_view.setTextAsValue(r, 0, df.format(dVss));
										xyseries1[ixy].add(r, dRss);
										m_view.setTextAsValue(r, 1, df.format(dRss));
										xyseries2[ixy].add(r, dPss);
										m_view.setTextAsValue(r, 2, df.format(dPss));
										xyseries3[ixy].add(r, dUss);
										m_view.setTextAsValue(r++, 3, df.format(dUss));
										
										
									}else if( strRead.startsWith("PID") ){
										if( !hasFound ){ 
											if( !hasMarkBreak ){
												ixy++;
												hasMarkBreak = true;
											}
											r++; 
										}else 
											hasFound = false;
									}else if( strRead.startsWith("Timestamp") ){
										timestamp = strRead;
									}else if( strRead.startsWith("Hardware") ){
										cpuinfo = cpuinfo + strRead + "\r\n";
									}else if( strRead.startsWith("Sampling interval") ){
										sampInterval = strRead.substring(strRead.indexOf(":")+1, strRead.length()).trim()+"秒";
									}
								}
								reader.close(); //关闭txt
								if( isEmpty )
									continue;
								
								
								for(int i=0;i<=ixy;i++){
									if( xyseries1[i]!=null)
										xySeriesCollection1.addSeries(xyseries1[i]);
								}
								JFreeChart jfreechart1 = ChartFactory.createXYLineChart("RSS", "Sampling Times", "Value (M)", xySeriesCollection1, PlotOrientation.VERTICAL, true, false, false);
								jfreechart1.getLegend().setVisible(false); 
								XYPlot plot1 = (XYPlot) jfreechart1.getPlot();
								plot1.setBackgroundAlpha(0.5f); 
								plot1.setForegroundAlpha(1f); 
								renderer1 = (XYLineAndShapeRenderer)plot1.getRenderer();
								renderer1.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart1, "config\\temp.png", 900, 300);
						        m_view.addPicture(11, 2, 21, 15, "config\\temp.png"); 
						        xySeriesCollection1 = null;
						        
						        
						        for(int i=0;i<=ixy;i++){
						        	if( xyseries2[i]!=null)
						        		xySeriesCollection2.addSeries(xyseries2[i]);
								}
								JFreeChart jfreechart2 = ChartFactory.createXYLineChart("PSS", "Sampling Times", "Value (M)", xySeriesCollection2, PlotOrientation.VERTICAL, true, false, false);
								jfreechart2.getLegend().setVisible(false); 
								XYPlot plot2 = (XYPlot) jfreechart2.getPlot();
								plot2.setBackgroundAlpha(0.5f); 
								plot2.setForegroundAlpha(1f); 
						        renderer2 = (XYLineAndShapeRenderer)plot2.getRenderer();
								renderer2.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart2, "config\\temp.png", 900, 300);
						        m_view.addPicture(11, 16, 21, 28, "config\\temp.png"); 
						        xySeriesCollection2 = null;
						        
						        
						        for(int i=0;i<=ixy;i++){
						        	if( xyseries3[i]!=null)
						        		xySeriesCollection3.addSeries(xyseries3[i]);
								}
								JFreeChart jfreechart3 = ChartFactory.createXYLineChart("USS", "Sampling Times", "Value (M)", xySeriesCollection3, PlotOrientation.VERTICAL, true, false, false);
								jfreechart3.getLegend().setVisible(false); 
								XYPlot plot3 = (XYPlot) jfreechart3.getPlot();
								plot3.setBackgroundAlpha(0.5f); 
								plot3.setForegroundAlpha(1f); 
						        renderer3 = (XYLineAndShapeRenderer)plot3.getRenderer();
								renderer3.setSeriesPaint(0, Color.BLUE);
						        saveAsFile(jfreechart3, "config\\temp.png", 900, 300);
						        m_view.addPicture(11, 30, 21, 42, "config\\temp.png"); 
						        xySeriesCollection3 = null;
					            
								//插入公式
								m_view.setTextAsValue(3, 9, "最大值");
								m_view.setFormula(3, 10, "MAX(B2:B"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(4, 9, "最小值");
								m_view.setFormula(4, 10, "MIN(B2:B"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(5, 9, "平均值");
								m_view.setFormula(5, 10, "ROUND(AVERAGE(B2:B"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(6, 9, "采样间隔");
								m_view.setText(6, 10, sampInterval);
								
					            //插入公式
								m_view.setTextAsValue(17, 9, "最大值");
								m_view.setFormula(17, 10, "MAX(C2:C"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(18, 9, "最小值");
								m_view.setFormula(18, 10, "MIN(C2:C"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(19, 9, "平均值");
								m_view.setFormula(19, 10, "ROUND(AVERAGE(C2:C"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(20, 9, "采样间隔");
								m_view.setText(20, 10, sampInterval);
								
								//插入公式
								m_view.setTextAsValue(31, 9, "最大值");
								m_view.setFormula(31, 10, "MAX(D2:D"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(32, 9, "最小值");
								m_view.setFormula(32, 10, "MIN(D2:D"+Integer.valueOf(r)+")");
								m_view.setTextAsValue(33, 9, "平均值");
								m_view.setFormula(33, 10, "ROUND(AVERAGE(D2:D"+Integer.valueOf(r)+"),2)");
								m_view.setTextAsValue(34, 9, "采样间隔");
								m_view.setText(34, 10, sampInterval);
								
								
								//设置数据右对齐
								m_view.setSelection("K1:K50");
								cfmt = m_view.getCellFormat();
								cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
								m_view.setCellFormat(cfmt);
								
								
							} // if end
							
						} //for end
						
						
						
						//Summary总表
						m_view.setSheetName(0, "Summary"); 
						m_view.setSheet(0);
						// 标题行
			            m_view.setTextAsValue(0, 0, "Process");
			            m_view.setColWidth(0, 8000); //设置列宽
			            m_view.setTextAsValue(0, 1, "RSS Max(%)");
			            m_view.setColWidth(1, 3000);
			            m_view.setTextAsValue(0, 2, "RSS Min(%)");
			            m_view.setColWidth(2, 3000);
			            m_view.setTextAsValue(0, 3, "RSS Avg(%)");
			            m_view.setColWidth(3, 3000);
			            m_view.setTextAsValue(0, 4, "PSS Max(M)");
			            m_view.setColWidth(4, 3000);
			            m_view.setTextAsValue(0, 5, "PSS Min(M)");
			            m_view.setColWidth(5, 3000);
			            m_view.setTextAsValue(0, 6, "PSS Avg(M)");
			            m_view.setColWidth(6, 3000);
			            m_view.setTextAsValue(0, 7, "USS Max(M)");
			            m_view.setColWidth(7, 3000);
			            m_view.setTextAsValue(0, 8, "USS Min(M)");
			            m_view.setColWidth(8, 3000);
			            m_view.setTextAsValue(0, 9, "USS Avg(M)");
			            m_view.setColWidth(9, 3000);
			            CellFormat cfmt = m_view.getCellFormat();
			            cfmt.setFontColor(Color.BLUE.getRGB());
			            m_view.setCellFormat(cfmt, 0, 0, 0, 20);
			            String stmp = "";
			            for( int sr=1; sr<s.length+1; sr++ ){
			            	//Process
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(0, 9);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 0, stmp);
			            	//RSS Max
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(3, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 1, stmp);
			            	//RSS Min
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(4, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 2, stmp);
			            	//RSS Avg
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(5, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 3, stmp);
			            	//PSS Max
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(17, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 4, stmp);
			            	//PSS Min
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(18, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 5, stmp);
			            	//PSS Avg
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(19, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 6, stmp);
			            	//USS Max
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(31, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 7, stmp);
			            	//USS Min
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(32, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 8, stmp);
			            	//USS Avg
			            	m_view.setSheet(sr);
			            	stmp = m_view.getText(33, 10);
			            	m_view.setSheet(0);
			            	m_view.setTextAsValue(sr, 9, stmp);
			            }
			            //设置格式
			            m_view.setSelection("A1:A1");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("B1:K200");
						cfmt = m_view.getCellFormat();
						cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("D1:D200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("G1:G200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						m_view.setSelection("J1:J200");
						cfmt = m_view.getCellFormat();
						cfmt.setFontBold(true);
						m_view.setCellFormat(cfmt);
						
						String maxLine = String.valueOf(s.length+1);
						
						//绘图区
			            ChartShape chart = m_view.addChart(12, 1, 21, 15);
			            chart.setChartType(ChartShape.TypeColumn);
			            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$D$2:$D$"+maxLine, false);
			            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine);
			            chart.addSeries();
			            chart.setAxisTitle(ChartShape.YAxis, 0, "RSS Avg(M)");
			            chart.setLegendVisible(false); 
			            
			            chart = m_view.addChart(12, 16, 21, 30);
			            chart.setChartType(ChartShape.TypeColumn);
			            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$G$2:$G$"+maxLine, false);
			            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine); 
			            chart.addSeries();
			            chart.setAxisTitle(ChartShape.YAxis, 0, "PSS Avg(M)");
			            chart.setLegendVisible(false); 
			            
			            chart = m_view.addChart(12, 31, 21, 45);
			            chart.setChartType(ChartShape.TypeColumn);
			            chart.setLinkRange("Summary!$A$2:$A$"+maxLine+",Summary!$J$2:$J$"+maxLine, false);
			            chart.setCategoryFormula("Summary!$A$2:$A$"+maxLine); 
			            chart.addSeries();
			            chart.setAxisTitle(ChartShape.YAxis, 0, "USS Avg(M)");
			            chart.setLegendVisible(false); 
			            
						
			            //excel写出路径
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
			            m_view.write(currentDir+"Procrank内存数据_"+format.format(date)+".xls");
			            m_view.releaseLock();
			            
					}
					catch (Exception e2){
						FileWriter fwriter = null;
						String strWrite = null;
						try {
							fwriter = new FileWriter(currentDir+"ErrorLog.txt", false);
							fwriter.write("Line: "+lr+"\r\n"+e2.getMessage());
							fwriter.close();
							MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
							messageBox.setMessage("数据解析出现异常，请查看ErrorLog.txt！");
							messageBox.open();
							btn_openFile4.setEnabled(true);
							return;
						} catch (Exception ee) {
							ee.printStackTrace();
						}

					}
					
				}else{
					btn_openFile4.setEnabled(true);
					return;
				}
				
				//文本框写入配置文件
				FileWriter fwriter4 = null;
				try {
					fwriter4 = new FileWriter("config\\text4", false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					fwriter4.write(text_4.getText());
					fwriter4.close();
				} catch (IOException ef) {
					ef.printStackTrace();
				}

				MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				messageBox.setMessage("数据解析完毕！");
				messageBox.open();
				btn_openFile4.setEnabled(true);
			}
		});
		btn_openFile4.setText("\u89E3\u6790\u6570\u636E");
		btn_openFile4.setBounds(472, 358, 80, 27);
		
		// 读取配置文件
		config = new File("config\\text4");
		try {
			if(!config.exists()) {
				config.createNewFile();
			}
			else{
				readConfig = new BufferedReader(new FileReader(config));
				while ((strTemp = readConfig.readLine()) != null) { 
					strConfig = strConfig + strTemp + "\r\n";
				}
				readConfig.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		text_4 = new Text(container, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text_4.setText(strConfig);
		text_4.setBounds(569, 263, 296, 125);
		strTemp = "";
		strConfig = "";
		btn_ConnDevice.setFocus();
		
		
		//开始执行Procrank命令
		btn_StartProcrank.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StartProcrank.setEnabled(false);
				
				new Thread (new Runnable() {
					String strSec = text4_sec.getText().trim();
		                  public void run() { 
		                	 try {
		                		  Date date=new Date();
		                		  DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
		                		  String time=format.format(date);
		                		  Date date2=new Date();
	                			  FileWriter fwriter;
	                			  Runtime rt = Runtime.getRuntime();
				    		      Process pr = null;
				    		      BufferedReader input = null;
				    		      String line = null;
				    		      
				    		      fwriter = new FileWriter("procrank-"+time+".txt", true);
	                			  fwriter.write("Sampling interval : "+strSec+"\r\n");
	                			  fwriter.close();
	                			  int sec = Integer.parseInt(strSec)*1000;
	                			  
	                			  if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("已开始 Procrank 内存数据采集！");
	 			                			messinfo.open();
	 		                            }
	 		                        });
			            		  }
	                			  fwriter = new FileWriter("procrank-"+time+".txt", true);
	                			  stopProcrank = false;
	                			  int wasteSec = 0; 
		                		  while( !stopProcrank ){
		                			  date=new Date();
		                			  fwriter.write("\r\n\r\nTimestamp: "+format.format(date)+"\r\n");
					    		      pr = rt.exec("adb shell \"procrank\""); 
									  //pr.waitFor();
									  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
									  while ((line=input.readLine()) != null) {
										  if( !line.equals("") & !line.contains("warning") )
											  fwriter.write(line+"\r\n");
									  }
									  fwriter.write("\r\n");
									  fwriter.flush();
									  date2=new Date();
									  wasteSec = (int)(date2.getTime()-date.getTime());
									  if(wasteSec<sec){
										  Thread.sleep(sec-wasteSec);
									  }
			                	  }
		                		  fwriter.close();
		                		  
							 } catch (final Exception ex) {
								 stopProcrank = true;
								 tips4 = false;
								 if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("错误信息：\r\n"+ex.getMessage());
	 			                			messinfo.open();
	 			                			btn_StartProcrank.setEnabled(true);
	 			                			btn_StopProcrank.setEnabled(false);
	 			                			return;
	 		                            }
	 		                        });
			            		}
							 }
		                	 stopProcrank = true;
		                	 if( !display.isDisposed() ){
		            				display.syncExec(new Runnable() {
 		                            public void run() {
 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
 			                			messinfo.setMessage("已停止 Procrank 内存数据采集！");
 			                			messinfo.open();
 			                			btn_StartProcrank.setEnabled(true);
 		                            }
 		                        });
		            		}
		                	
		                 }
		        }).start();
				
				
				tips4 = true;
				new Thread() { //信息提示栏
   	            	int hour = 0;
					int min = 0;
					int sec = 0;
		   	        public void run() {
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips4.setText("");
	                            	label_tips4.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
	                            }
	                        });
            			}
		   	        	try {
		   	                 Thread.sleep(1000);
		   	            } catch (Exception e1) {
		   	                 e1.printStackTrace();
		   	            }
		   	        	while( tips4 ) {
			   	             display.syncExec(new Runnable() {
								public void run() {
									try {
										sec += 1;
										if (sec >= 60) {
											sec = 0;
											min += 1;
										}
										if (min >= 60) {
											min = 0;
											hour += 1;
										}
										String strTime = "";
										if (hour < 10)
											strTime = "0" + hour + ":";
										else
											strTime = hour + ":";
										if (min < 10)
											strTime = strTime + "0" + min + ":";
										else
											strTime = strTime + min + ":";
										if (sec < 10)
											strTime = strTime + "0" + sec;
										else
											strTime = strTime + sec;
										
										label_tips4.setText(strTime); // 打印计时情况

									} catch (Exception e) {
										e.printStackTrace();
									}
			   	                }
			   	              });
			   	              try {
			   	                 Thread.sleep(1000);
			   	              } catch (Exception e1) {
			   	                 e1.printStackTrace();
			   	              }
			   	        } //while end
		   	        	
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips4.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
	                            }
	                        });
            			}
		   	        	
		   	       }
		   	    }.start();
		   	    
		   	    
				//文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				btn_StopProcrank.setEnabled(true);
			}
		});
		btn_StartProcrank.setText("\u5F00\u59CB\u91C7\u96C6");
		btn_StartProcrank.setEnabled(false);
		btn_StartProcrank.setBounds(472, 294, 80, 27);
		
		
		//停止执行Procrank命令
		btn_StopProcrank.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StopProcrank.setEnabled(false);
				stopProcrank = true;
				tips4 = false;
			}
		});
		btn_StopProcrank.setText("\u505C\u6B62\u91C7\u96C6");
		btn_StopProcrank.setEnabled(false);
		btn_StopProcrank.setBounds(472, 326, 80, 27);
		
		Label lblcpu = new Label(container, SWT.NONE);
		lblcpu.setText("\u8FDB\u7A0BCPU\u5185\u5B58\u5FEB\u7167\uFF1A");
		lblcpu.setBounds(462, 510, 102, 17);
		
		
		//抓取关键进程快照
		btn_catchInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_catchInfo.setEnabled(false);
				if(text_packname3.getText().trim().equals("")){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
					messageBox.setMessage("请填写要抓取的进程名称！");
					messageBox.open();
					btn_catchInfo.setEnabled(true);
					return;
				}
				
				
				new Thread (new Runnable() {
					String procname = text_packname3.getText().trim();
					boolean exactprocname = checkBtn4.getSelection();
                    public void run() { //新线程执行内容
                    	if( exactprocname ){
                    		procname = procname+"$";
    					}
                    	Runtime rt = Runtime.getRuntime();
    		            Process pr = null;
                    	try {
							Date mydate=new Date();
                    		DateFormat myformat=new SimpleDateFormat("yyMMdd_HHmmss");
                    		String mystrlogcattime = myformat.format(mydate);
	        	            Runtime myrt = Runtime.getRuntime();
	        	            Process mypr = null;
	        	            BufferedReader myinput = null;
	        	            String myline = null;
	        	    		FileWriter myfwriter = null;
	        	    		
	        	    		String[] strProArray = new String[100]; //最多支持抓取100个进程
	        	    		String[] strPidArray = new String[100];
	        	    		String[] strtmp = new String[50];
	        	    		int inum = 0;
	        	    		mypr = myrt.exec("adb shell \"ps | grep "+procname+"\"");
	        	            myinput = new BufferedReader(new InputStreamReader(mypr.getInputStream(), "UTF-8"));
	        	            while ((myline=myinput.readLine()) != null) {
	        	            	if(!myline.equals("")){
	        	            		strtmp = myline.trim().split("\\s+");
	        	            		strPidArray[inum] = strtmp[1];
	        	            		strProArray[inum++] = strtmp[strtmp.length-1];
	        	            	}
	        	            }
	        	    		
	        	    		myfwriter = new FileWriter("catchInfo_"+mystrlogcattime+".txt", false);
	        	    		myfwriter.write("\r\n\r\n-------------------- TOP --------------------\r\n");
	        	            mypr = myrt.exec("adb shell \"top -d 0 -n 1 -s cpu\"");
	        	            myinput = new BufferedReader(new InputStreamReader(mypr.getInputStream(), "UTF-8"));
	        	            while ((myline=myinput.readLine()) != null) {
	        	            	if(!myline.equals(""))
	        	            		myfwriter.write(myline+"\r\n");
	        	            }
	        	            
	        	            myfwriter.write("\r\n\r\n-------------------- MEMINFO --------------------\r\n");
	        				for(int i=0;i<inum;i++){
	        					myfwriter.write(strProArray[i]+":\r\n");
	        					mypr = myrt.exec("adb shell \"dumpsys meminfo "+strProArray[i]+"\"");
	        					myinput = new BufferedReader(new InputStreamReader(mypr.getInputStream(), "UTF-8"));
		        	            while ((myline=myinput.readLine()) != null) {
		        	            	if(!myline.equals(""))
		        	            		myfwriter.write(myline+"\r\n");
		        	            }
		        	            myfwriter.write("\r\n\r\n");
	        				}
	        				
	        				myfwriter.write("\r\n\r\n-------------------- PROCRANK --------------------\r\n");
	        				mypr = myrt.exec("adb shell \"procrank\"");
	        				myinput = new BufferedReader(new InputStreamReader(mypr.getInputStream(), "UTF-8"));
	        	            while ((myline=myinput.readLine()) != null) {
	        	            	if(!myline.equals(""))
	        	            		myfwriter.write(myline+"\r\n");
	        	            }
	        	            
	        	            myfwriter.write("\r\n\r\n-------------------- PROCMEM --------------------\r\n");
	        	            for(int i=0;i<inum;i++){
	        	            	myfwriter.write(strProArray[i]+": Pid="+strPidArray[i]+"\r\n");
	        					mypr = myrt.exec("adb shell \"procmem "+strPidArray[i]+"\"");
	        					myinput = new BufferedReader(new InputStreamReader(mypr.getInputStream(), "UTF-8"));
	        					while ((myline=myinput.readLine()) != null) {
		        	            	if(!myline.equals(""))
		        	            		myfwriter.write(myline+"\r\n");
		        	            }
		        	            myfwriter.write("\r\n\r\n");
	        				}
	        	            
	        	            myfwriter.close();
	        	            
	        	            if( !display.isDisposed() ){
	            				display.syncExec(new Runnable() {
		                            public void run() {
		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
			                			messinfo.setMessage("相关进程的CPU内存信息已抓取完毕！");
			                			messinfo.open();
			                			btn_catchInfo.setEnabled(true);
		                            }
		                        });
	        	            }
                    		
                    		
						} catch (IOException e) {
							messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
                			messinfo.setMessage("出现异常信息："+e.getMessage());
                			messinfo.open();
                			btn_catchInfo.setEnabled(true);
						}
                    }//新线程执行内容结束
                }).start();
				
				
				//文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btn_catchInfo.setText("\u6293\u53D6\u5FEB\u7167");
		btn_catchInfo.setBounds(786, 505, 80, 27);
		
		Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("\u8FDB\u7A0B\u540D");
		label_4.setBounds(17, 253, 97, 17);
		
		Label label_5 = new Label(container, SWT.NONE);
		label_5.setText("\u8BF7\u8F93\u5165\u6700\u540E\u4E24\u5217\uFF1A");
		label_5.setBounds(17, 503, 97, 17);
		
		Label label_7 = new Label(container, SWT.NONE);
		label_7.setText("\u7EBF\u7A0B\u540D  \u8FDB\u7A0B\u540D");
		label_7.setBounds(17, 520, 97, 17);
		
		text_packname3.setBounds(570, 507, 202, 23);
		
		
		//汇总前面N行
		checkBtn1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkBtn1.getSelection()){
					checkTxt1.setEnabled(true);
				}else{
					checkTxt1.setEnabled(false);
				}
			}
		});
		checkBtn1.setText("\u6C47\u603B\u524D");
		checkBtn1.setBounds(14, 208, 54, 17);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("\u884C");
		label_2.setBounds(97, 208, 17, 17);
		
		
		//开始top进程数据采集
		btn_StartTop1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StartTop1.setEnabled(false);
				
				new Thread (new Runnable() {
					String strSec = text1_sec.getText().trim();
		                  public void run() {
		                	 try {
		                		  int sec = Integer.parseInt(strSec)*1000;
		                		  Date date=new Date();
		                		  DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
		                		  String time=format.format(date);
		                		  Date date2=new Date();
	                			  FileWriter fwriter;
	                			  Runtime rt = Runtime.getRuntime();
				    		      Process pr = null;
				    		      BufferedReader input = null;
				    		      String line = null;
				    		      
				    		      fwriter = new FileWriter("cpu-"+time+".txt", true);
				    		      pr = rt.exec("adb shell \"cat /proc/cpuinfo | grep -E 'Processor|processor|Hardware'\""); 
								  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
								  while ((line=input.readLine()) != null) {
									  if( !line.equals("") & !line.contains("warning") ){
										  fwriter.write(line+"\r\n");
										  fwriter.flush();
									  }
								  }
	                			  fwriter.write("Sampling interval : "+strSec+"\r\n");
	                			  fwriter.flush();
	                			  if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("已开始 Top 进程数据采集！");
	 			                			messinfo.open();
	 		                            }
	 		                        });
			            		  }
	                			  stopTop1 = false;
	                			  int wasteSec = 0; 
		                		  while( !stopTop1 ){
		                			  date=new Date();
		                			  fwriter.write("\r\n\r\nTimestamp: "+format.format(date)+"\r\n");
					    		      pr = rt.exec("adb shell \"top -n 1 -d 1 -s cpu\"");
									  //pr.waitFor();
									  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
									  while ((line=input.readLine()) != null) {
										  if( !line.equals("") & !line.contains("warning") )
											  fwriter.write(line+"\r\n");
									  }
									  fwriter.write("\r\n");
									  fwriter.flush();
									  date2=new Date();
									  wasteSec = (int)(date2.getTime()-date.getTime());
									  if(wasteSec<sec){
										  Thread.sleep(sec-wasteSec);
									  }
			                	  }
		                		  fwriter.close();
		                		  
							 } catch (final Exception ex) {
								 stopTop1 = true;
								 tips1 = false;
								 if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("错误信息：\r\n"+ex.getMessage());
	 			                			messinfo.open();
	 			                			btn_StartTop1.setEnabled(true);
	 			                			btn_StopTop1.setEnabled(false);
	 			                			return;
	 		                            }
	 		                        });
			            		}
							 }
		                	 stopTop1 = true;
		                	 if( !display.isDisposed() ){
		            				display.syncExec(new Runnable() {
 		                            public void run() {
 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
 			                			messinfo.setMessage("已停止 Top 进程数据采集！");
 			                			messinfo.open();
 			                			btn_StartTop1.setEnabled(true);
 		                            }
 		                        });
		            		}
		                	
		                 }
		        }).start();
				
				tips1 = true;
				new Thread() { //信息提示栏
   	            	int hour = 0;
					int min = 0;
					int sec = 0;
		   	        public void run() {
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips1.setText("");
	                            	label_tips1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
	                            }
	                        });
            			}
		   	        	try {
		   	                 Thread.sleep(1000);
		   	            } catch (Exception e1) {
		   	                 e1.printStackTrace();
		   	            }
		   	        	while( tips1 ) {
			   	             display.syncExec(new Runnable() {
								public void run() {
									try {
										sec += 1;
										if (sec >= 60) {
											sec = 0;
											min += 1;
										}
										if (min >= 60) {
											min = 0;
											hour += 1;
										}
										String strTime = "";
										if (hour < 10)
											strTime = "0" + hour + ":";
										else
											strTime = hour + ":";
										if (min < 10)
											strTime = strTime + "0" + min + ":";
										else
											strTime = strTime + min + ":";
										if (sec < 10)
											strTime = strTime + "0" + sec;
										else
											strTime = strTime + sec;
										
										label_tips1.setText(strTime); // 打印计时情况

									} catch (Exception e) {
										e.printStackTrace();
									}
			   	                }
			   	              });
			   	              try {
			   	                 Thread.sleep(1000);
			   	              } catch (Exception e1) {
			   	                 e1.printStackTrace();
			   	              }
			   	        } //while end
		   	        	
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
	                            }
	                        });
            			}
		   	        	
		   	       }
		   	    }.start();
		   	    
		   	    
		   	    if(chkbtn_1.getSelection()){ //如果定时
		   	    	new Thread() {
						int sec = Integer.parseInt(text1_count.getText().trim())*60;
			   	        public void run() {
			   	        	try {
			   	        		for(int i=0; tips1 & i<sec; i++){
			   	        			Thread.sleep(1000);
			   	        		}
			   	            } catch (Exception e1) {
			   	                 e1.printStackTrace();
			   	            }
			   	        	if( !display.isDisposed() ){ 
	            				display.syncExec(new Runnable() {
		                            public void run() {
		                            	if(btn_StopTop1.isEnabled()){ 
		                            		try{
		    									btn_StopTop1.setEnabled(false);
		    									Thread.sleep(300);
			    								stopTop1 = true;
			    								tips1 = false;
		    								}catch(Exception ex){}
		    			   	        	}
		                            }
		                        });
	            			}
			   	        	
			   	        	
			   	       }
			   	    }.start();
		   	    }
		   	    
				
		   	    //文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				btn_StopTop1.setEnabled(true);
			}
		});
		btn_StartTop1.setText("\u5F00\u59CB\u91C7\u96C6");
		btn_StartTop1.setEnabled(false);
		btn_StartTop1.setBounds(23, 108, 80, 27);
		
		
		//停止top进程数据采集
		btn_StopTop1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StopTop1.setEnabled(false);
				stopTop1 = true;
				tips1 = false;
			}
		});
		btn_StopTop1.setText("\u505C\u6B62\u91C7\u96C6");
		btn_StopTop1.setEnabled(false);
		btn_StopTop1.setBounds(23, 140, 80, 27);
		
		
		//开始线程top数据采集
		btn_StartTop2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StartTop2.setEnabled(false);
				if(text_packname1.getText().trim().equals("")){
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
					messageBox.setMessage("请填写进程名称！");
					messageBox.open();
					btn_StartTop2.setEnabled(true);
					return;
				}
				if( checkBtn2.getSelection() ){
					strPattern = "1";
				}else{
					strPattern = "2";
				}
				
				new Thread (new Runnable() {
					String strSec = text2_sec.getText().trim();
					String strPackName = text_packname1.getText().trim();
		                  public void run() {
		                	 try {
		                		  int sec = Integer.parseInt(strSec)*1000;
		                		  Date date=new Date();
		                		  DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
		                		  String time=format.format(date);
		                		  Date date2=new Date();
	                			  FileWriter fwriter;
	                			  Runtime rt = Runtime.getRuntime();
				    		      Process pr = null;
				    		      BufferedReader input = null;
				    		      String line = null;
				    		      
				    		      fwriter = new FileWriter("cputhread-"+time+".txt", true);
				    		      pr = rt.exec("adb shell \"cat /proc/cpuinfo | grep -E 'Processor|processor|Hardware'\""); 
								  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
								  while ((line=input.readLine()) != null) {
									  if( !line.equals("") & !line.contains("warning") ){
										  fwriter.write(line+"\r\n");
										  fwriter.flush();
									  }
								  }
	                			  fwriter.write("Sampling interval : "+strSec+"\r\n");
	                			  fwriter.flush();
	                			  if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("已开始 Top 线程数据采集！");
	 			                			messinfo.open();
	 		                            }
	 		                        });
			            		  }
	                			  stopTop2 = false;
	                			  int wasteSec = 0; 
	                			  
	                			  String titleLine = " PID   TID PR CPU S     VSS     RSS PCY UID      Thread          Proc";
	                			  Process prLine = null;
	                			  prLine = rt.exec("adb shell \"top -t -d 0 -n 1 -m 1 | grep PID\"");
								  //pr.waitFor();
								  input = new BufferedReader(new InputStreamReader(prLine.getInputStream(), "GBK"));
								  while ((line=input.readLine()) != null) {
									  if( !line.equals("") & !line.contains("warning") ){
										  titleLine = line;
									  }
								  }
	                			  
	                			  if( strPattern.equals("1") ){
	                				  while( !stopTop2 ){
			                			  date=new Date();
			                			  fwriter.write("\r\n\r\nTimestamp: "+format.format(date)+"\r\n");
			                			  fwriter.write(titleLine+"\r\n");
						    		      pr = rt.exec("adb shell \"top -n 1 -d 1 -s cpu -t | grep "+strPackName+"$\"");
										  //pr.waitFor();
										  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
										  while ((line=input.readLine()) != null) {
											  if( !line.equals("") & !line.contains("warning") )
												  fwriter.write(line+"\r\n");
										  }
										  fwriter.write("\r\n");
										  fwriter.flush();
										  date2=new Date();
										  wasteSec = (int)(date2.getTime()-date.getTime());
										  if(wasteSec<sec){
											  Thread.sleep(sec-wasteSec);
										  }
				                	  }
			                	  }else{
			                		  while( !stopTop2 ){
			                			  date=new Date();
			                			  fwriter.write("\r\n\r\nTimestamp: "+format.format(date)+"\r\n");
			                			  fwriter.write(" PID   TID PR CPU S     VSS     RSS PCY UID      Thread          Proc\r\n");
						    		      pr = rt.exec("adb shell \"top -n 1 -d 0 -s cpu -t | grep "+strPackName+"\""); 
										  //pr.waitFor();
										  input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
										  while ((line=input.readLine()) != null) {
											  if( !line.equals("") & !line.contains("warning") )
												  fwriter.write(line+"\r\n");
										  }
										  fwriter.write("\r\n");
										  fwriter.flush();
										  date2=new Date();
										  wasteSec = (int)(date2.getTime()-date.getTime());
										  if(wasteSec<sec){
											  Thread.sleep(sec-wasteSec);
										  }
				                	  }
			                	  }
		                		  fwriter.close();
		                		  
							 } catch (final Exception ex) {
								 stopTop2 = true;
								 tips2 = false;
								 if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("错误信息：\r\n"+ex.getMessage());
	 			                			messinfo.open();
	 			                			btn_StartTop2.setEnabled(true);
	 			                			btn_StopTop2.setEnabled(false);
	 			                			return;
	 		                            }
	 		                        });
			            		}
							 }
		                	 stopTop2 = true;
		                	 if( !display.isDisposed() ){
		            				display.syncExec(new Runnable() {
 		                            public void run() {
 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
 			                			messinfo.setMessage("已停止 Top 线程数据采集！");
 			                			messinfo.open();
 			                			btn_StartTop2.setEnabled(true);
 		                            }
 		                        });
		            		}
		                	
		                 }
		        }).start();
				
				
				tips2 = true;
				new Thread() { //信息提示栏
   	            	int hour = 0;
					int min = 0;
					int sec = 0;
		   	        public void run() {
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips2.setText("");
	                            	label_tips2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
	                            }
	                        });
            			}
		   	        	try {
		   	                 Thread.sleep(1000);
		   	            } catch (Exception e1) {
		   	                 e1.printStackTrace();
		   	            }
		   	        	while( tips2 ) {
			   	             display.syncExec(new Runnable() {
								public void run() {
									try {
										sec += 1;
										if (sec >= 60) {
											sec = 0;
											min += 1;
										}
										if (min >= 60) {
											min = 0;
											hour += 1;
										}
										String strTime = "";
										if (hour < 10)
											strTime = "0" + hour + ":";
										else
											strTime = hour + ":";
										if (min < 10)
											strTime = strTime + "0" + min + ":";
										else
											strTime = strTime + min + ":";
										if (sec < 10)
											strTime = strTime + "0" + sec;
										else
											strTime = strTime + sec;
										
										label_tips2.setText(strTime); // 打印计时情况

									} catch (Exception e) {
										e.printStackTrace();
									}
			   	                }
			   	              });
			   	              try {
			   	                 Thread.sleep(1000);
			   	              } catch (Exception e1) {
			   	                 e1.printStackTrace();
			   	              }
			   	        } //while end
		   	        	
		   	        	if( !display.isDisposed() ){ 
            				display.syncExec(new Runnable() {
	                            public void run() {
	                            	label_tips2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
	                            }
	                        });
            			}
		   	        	
		   	       }
		   	    }.start();
		   	    
		   	    
		   	    if(chkbtn_2.getSelection()){ //如果定时
		   	    	new Thread() { //计时器
						int sec = Integer.parseInt(text2_count.getText().trim())*60;
			   	        public void run() {
			   	        	try {
			   	        		for(int i=0; tips2 & i<sec; i++){
			   	        			Thread.sleep(1000);
			   	        		}
			   	            } catch (Exception e1) {
			   	                 e1.printStackTrace();
			   	            }
			   	        	if( !display.isDisposed() ){ 
	            				display.syncExec(new Runnable() {
		                            public void run() {
		                            	if(btn_StopTop2.isEnabled()){
		                            		try{
		    									btn_StopTop2.setEnabled(false);
		    									Thread.sleep(300);
			    								stopTop2 = true;
			    								tips2 = false;
		    								}catch(Exception ex){}
		    			   	        	}
		                            }
		                        });
	            			}
			   	        	
			   	        	
			   	       }
			   	    }.start();
		   	    }
		   	 
				
		   	    //text0文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				btn_StopTop2.setEnabled(true);
			}
		});
		btn_StartTop2.setText("\u5F00\u59CB\u91C7\u96C6");
		btn_StartTop2.setEnabled(false);
		btn_StartTop2.setBounds(23, 375, 80, 27);
		
		
		//停止top线程数据采集
		btn_StopTop2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StopTop2.setEnabled(false);
				stopTop2 = true;
				tips2 = false;
			}
		});
		btn_StopTop2.setText("\u505C\u6B62\u91C7\u96C6");
		btn_StopTop2.setEnabled(false);
		btn_StopTop2.setBounds(23, 407, 80, 27);
		
		Label label_8 = new Label(container, SWT.NONE);
		label_8.setText("\u91C7\u6837\u95F4\u9694");
		label_8.setBounds(205, 321, 50, 17);
		
		text2_sec = new Text(container, SWT.BORDER);
		text2_sec.setText("10");
		text2_sec.setBounds(258, 318, 25, 23);
		text2_sec.setTextLimit(2);
		text2_sec.addVerifyListener(new VerifyListener(){
		    public void verifyText(VerifyEvent e) {
		       boolean b = ("0123456789".indexOf(e.text)>=0);
		       e.doit = b;
		    }
		});
		
		Label label_9 = new Label(container, SWT.NONE);
		label_9.setText("\u79D2");
		label_9.setBounds(286, 321, 12, 17);
		
		Label label_10 = new Label(container, SWT.NONE);
		label_10.setText("\u91C7\u6837\u95F4\u9694");
		label_10.setBounds(205, 51, 50, 17);
		
		text1_sec = new Text(container, SWT.BORDER);
		text1_sec.setText("10");
		text1_sec.setBounds(258, 48, 25, 23);
		text1_sec.setTextLimit(2);
		text1_sec.addVerifyListener(new VerifyListener(){
		    public void verifyText(VerifyEvent e) {
		       boolean b = ("0123456789".indexOf(e.text)>=0);
		       e.doit = b;
		    }
		});

		
		Label label_11 = new Label(container, SWT.NONE);
		label_11.setText("\u79D2");
		label_11.setBounds(286, 51, 12, 17);
		
		
		Label lblhprof = new Label(container, SWT.NONE);
		lblhprof.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblhprof.setText("\u8FDB\u7A0B\u5185\u5B58\u8D85\u9650\u5BFC\u51FAhprof\uFF08RSS\uFF0C\u5355\u4F4DM\uFF09\uFF1A");
		lblhprof.setBounds(472, 406, 236, 17);
		
		//监控内存并导出文件
		btn_StartHprof.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StartHprof.setEnabled(false);
				btn_StopHprof.setEnabled(true);
				
				String s = text_5.getText().trim();
				porc = s.split("\r\n");
				
				stopHprof = false;
				new Thread (new Runnable() {
		                  public void run() {
		                	 try {
		                		  Runtime rt = Runtime.getRuntime();
				    		      Process prTmp = null, pr = null, prdump = null;
				    		      BufferedReader input = null;
				    		      String line = null;
				    		      int rssPos = 6;
				    		      prTmp = rt.exec("adb shell \"top -n 1 -d 0 -m 1\""); 
				    		      input = new BufferedReader(new InputStreamReader(prTmp.getInputStream(), "GBK"));
				    		      while ((line=input.readLine()) != null) {
				    		    	  line = line.trim();
				    		    	  if(line.startsWith("PID")){
				    		    		  String[] posStr = line.split("\\s+");
				    		    		  for(int ps=0; ps<posStr.length; ps++){
				    		    			  if(posStr[ps].equals("RSS")){
				    		    				  rssPos = ps+1;
				    		    			  }
										  }
										  break;
									  }
						    	  }
				    		      
		                		  String myPorc[] = new String[10]; //最多支持10个进程
		                		  String[] aa = new String[20];
		  						  double dRss;
		  						  Date date;
		  						  DateFormat format=new SimpleDateFormat("yyyyMMdd-HHmmss");
		  						  String fName = "";
		  						  boolean dumped[] = new boolean[10]; 
		  						  boolean allDumped = true;
		  						  hasFoundDumped = false;
		  						  boolean hasPorcess = false;
		                		  while(!stopHprof){
		                			  for(int i=0; i<porc.length; i++){
			                			  myPorc = porc[i].split(",");
			                			  if( myPorc.length==2 ){
				         						myPorc[0] = myPorc[0].trim();
				         						myPorc[1] = myPorc[1].trim();
				         						hasPorcess = false;
				         						pr = rt.exec("adb shell \"top -n 1 -d 0 | grep "+myPorc[0]+"$\""); 
												input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
												while ((line=input.readLine()) != null) {
													if( (Pattern.compile("^[0-9]")).matcher(line.trim()).find() ){
														hasPorcess = true;
														aa = line.split("\\s+");
														break;
													}
												}
												if(hasPorcess){ //如果被测进程数据不为空
													aa[rssPos] = aa[rssPos].substring(0, aa[rssPos].length()-1); //删除K
													dRss = Double.parseDouble(aa[rssPos])/1024.00;
													date = new Date();
													if( !dumped[i] & dRss>Integer.parseInt(myPorc[1]) ){ //如果内存超限
														hasFoundDumped = true;
														File file =new File(".\\HPROF");
														if(!file.exists()  && !file.isDirectory()){
														    file.mkdir();
														}
														fName = myPorc[0]+"_"+format.format(date);
														prdump = rt.exec("adb shell \"rm -rf /data/local/tmp/hprof\"");
														prdump.waitFor();
														prdump = rt.exec("adb shell \"mkdir -p /data/local/tmp/hprof/\"");
														prdump.waitFor();
														prdump = rt.exec("adb shell \"am dumpheap "+myPorc[0]+" /data/local/tmp/hprof/"+fName+".hprof\"");
														prdump.waitFor();
														Thread.sleep(10000);
														prdump = rt.exec("cmd.exe /C start adb pull /data/local/tmp/hprof .\\HPROF");
														prdump.waitFor();
														Thread.sleep(10000);
														prdump = rt.exec("adb shell \"rm -rf /data/local/tmp/hprof\"");
														prdump.waitFor();
														prdump = rt.exec(".\\config\\hprof-conv  -z  .\\HPROF\\"+fName+".hprof  .\\HPROF\\"+fName+"-z.hprof");
														prdump.waitFor();
														dumped[i] = true;
														
														allDumped = true;
														for( int j=0; j<porc.length; j++ ){
															if( !dumped[j] ){
																allDumped = false;
															}
														}
														if( allDumped ){
															stopHprof = true;
															break;
														}
													}
												}
												
												
				         				  }else{
				         					 if( !display.isDisposed() ){
						            				display.syncExec(new Runnable() {
				 		                            public void run() {
				 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				 			                			messinfo.setMessage("进程信息填写有误，请按照如下格式填写：\r\ncom.aispeech.aios,200");
				 			                			messinfo.open();
				 			                			btn_StartHprof.setEnabled(true);
				 			                			btn_StopHprof.setEnabled(false);
				 			                			return;
				 		                            }
				 		                        });
						            		  }
				         					  return;
				         				  }
				         			  }
			                		  
		                			  if(stopHprof){
		                				  break;
		                			  }else{
		                				  for(int t=0; !stopHprof & t<10; t++){ //每10秒检查一次内存
				                			  Thread.sleep(1000);
				                		  }
		                			  }
			                		  
		                		  } //while end
		                		  if(allDumped){ //如果已经导出
		                			  stopHprof = true;
		                		  }else{
		                			  stopHprof = false;
		                		  }
		                		  
							 } catch (final Exception ex) {
								 stopHprof = true;
								 if( !display.isDisposed() ){
			            				display.syncExec(new Runnable() {
	 		                            public void run() {
	 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
	 			                			messinfo.setMessage("错误信息：\r\n"+ex.getMessage());
	 			                			messinfo.open();
	 			                			btn_StartHprof.setEnabled(true);
	 			                			btn_StopHprof.setEnabled(false);
	 			                			return;
	 		                            }
	 		                        });
			            		}
							 }
		                	 stopHprof = true;
		                	 if( !display.isDisposed() ){
		            				display.syncExec(new Runnable() {
 		                            public void run() {
 		                            	String sMsg = "";
 		                            	if(hasFoundDumped){
 		                            		sMsg = "已停止内存超限监控，发现内存超限，HPROF文件已导出！";
 		                            	}else{
 		                            		sMsg = "已停止内存超限监控，未发现内存超限！";
 		                            	}
 		                            	messinfo = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
 			                			messinfo.setMessage(sMsg);
 			                			messinfo.open();
 			                			btn_StartHprof.setEnabled(true);
 			                			btn_StopHprof.setEnabled(false);
 		                            }
 		                        });
		            		}
		                	
		                	
		                 }
		        }).start();
				
				
				//文本框写入配置文件
				FileWriter fwriter5 = null;
				try {
					fwriter5 = new FileWriter("config\\text5", false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					fwriter5.write(text_5.getText());
					fwriter5.close();
				} catch (IOException ef) {
					ef.printStackTrace();
				}
				
				MessageBox messageBox = new MessageBox( getShell(), SWT.ICON_WARNING | SWT.OK );
				messageBox.setMessage("已开始内存超限监控！");
				messageBox.open();
				
			}
		});
		btn_StartHprof.setText("\u5F00\u59CB\u76D1\u63A7");
		btn_StartHprof.setEnabled(false);
		btn_StartHprof.setBounds(472, 435, 80, 27);
		
		
		//停止监控内存
		btn_StopHprof.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_StopHprof.setEnabled(false);
				stopHprof = true;
			}
		});
		btn_StopHprof.setText("\u505C\u6B62\u76D1\u63A7");
		btn_StopHprof.setEnabled(false);
		btn_StopHprof.setBounds(472, 468, 80, 27);
		
		
		//手动导出HPROF文件
		btn_ManualHprof.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_ManualHprof.setEnabled(false);
				
				new Thread() {
		            public void run() {
		            	try {
		            		Runtime rt = Runtime.getRuntime();
							Process pr = null;
							BufferedReader input = null;
							String line = null;
							pr = rt.exec("cmd.exe /C start bat\\Dump.bat");
							input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
							while ((line = input.readLine()) != null) {
								System.out.println(line);
							}
							
				        } catch (Exception ex) {
				            ex.printStackTrace();
				        }
		            }
		        }.start();
		        
				btn_ManualHprof.setEnabled(true);
			}
		});
		btn_ManualHprof.setText("\u624B\u52A8\u5BFC\u51FA");
		btn_ManualHprof.setEnabled(false);
		btn_ManualHprof.setBounds(785, 401, 80, 27);
		
		final Button ratioCpu = new Button(container, SWT.RADIO);
		final Button ratioRss = new Button(container, SWT.RADIO);
		final Button ratioProc = new Button(container, SWT.RADIO);
		final Button ratioThread = new Button(container, SWT.RADIO);
		
		//进程按钮radio1
		ratioProc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				radio1 = true;
				ratioProc.setSelection(true);
				radio2 = false;
				ratioThread.setSelection(false);
				if(radio3){
					ratioCpu.setSelection(true);
				}else{
					ratioRss.setSelection(true);
				}
				
			}
		});
		ratioProc.setText("\u8FDB\u7A0B");
		ratioProc.setSelection(true);
		ratioProc.setBounds(336, 15, 43, 17);
		//线程按钮
		ratioThread.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				radio1 = false;
				ratioProc.setSelection(false);
				radio2 = true;
				ratioThread.setSelection(true);
				if(radio3){
					ratioCpu.setSelection(true);
				}else{
					ratioRss.setSelection(true);
				}
				
			}
		});
		ratioThread.setText("\u7EBF\u7A0B");
		ratioThread.setBounds(382, 15, 45, 17);
		
		//CPU按钮
		ratioCpu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				radio3 = true;
				ratioCpu.setSelection(true);
				radio4 = false;
				ratioRss.setSelection(false);
				if(radio1){
					ratioProc.setSelection(true);
				}else{
					ratioThread.setSelection(true);
				}
				
			}
		});
		ratioCpu.setSelection(true);
		ratioCpu.setBounds(656, 15, 43, 17);
		ratioCpu.setText("CPU");
		//RSS按钮
		ratioRss.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				radio3 = false;
				ratioCpu.setSelection(false);
				radio4 = true;
				ratioRss.setSelection(true);
				if(radio1){
					ratioProc.setSelection(true);
				}else{
					ratioThread.setSelection(true);
				}
				
			}
		});
		ratioRss.setText("RSS");
		ratioRss.setBounds(702, 15, 45, 17);
				
		
		//CPU曲线
		final Button btnCurve = new Button(container, SWT.NONE);
		btnCurve.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCurve.setEnabled(false);
				
				if(ratioProc.getSelection()){
					displayProc = true;
				}else{
					displayProc = false;
				}
				cpuCurveRun = false;
				rssCurveRun = false;
				if(ratioCpu.getSelection()){
					cpuCurveRun = true;
				}else{
					rssCurveRun = true;
				}
				
				new Thread() { //实时读取CPU和RSS数据
					String procName = text_packname4.getText().trim();
		            public void run() {
		            	try {
		            		int cpuCol = 2, rssCol = 6;
		        			Runtime rt = Runtime.getRuntime();
		        			Process pr = null;
		        			BufferedReader input = null;
		        			String line = null;
		        			if(displayProc){
		        				pr = rt.exec("adb shell \"top -n 1 -d 0 -m 1\"");
		        			}else{
		        				pr = rt.exec("adb shell \"top -t -n 1 -d 0 -m 1\"");
		        			}
		        			input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
		        			while ((line = input.readLine()) != null) {
		        				line = line.trim();
			    		    	  if(line.startsWith("PID")) {
			    		    		  String[] posStr = line.split("\\s+");
			    		    		  for(int ps=0; ps<posStr.length; ps++) {
			    		    			  if(posStr[ps].equals("CPU%")){
			    		    				  cpuCol = ps+1;
			    		    			  }else if(posStr[ps].equals("RSS")){
			    		    				  rssCol = ps+1;
			    		    			  }
									  }
									  break;
								  }
		        			}
		        			pr.waitFor();
		        			if(displayProc){ //进程
		        				if(cpuCurveRun){
			        				while(cpuCurveRun){
				        				pr = rt.exec("adb shell \"top -n 1 -d 0 | grep "+procName+"$\"");
					        			input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
					        			String[] sTemp = new String[20];
					        			while ((line = input.readLine()) != null) {
					        				if( !line.trim().equals("") ){
					        					sTemp = line.split("\\s+");
						        				cpuCurve = Integer.parseInt(sTemp[cpuCol].substring(0, sTemp[cpuCol].length()-1));
					        				}
					        			}
				        			}
			        			}else{
			        				while(rssCurveRun){
				        				pr = rt.exec("adb shell \"top -n 1 -d 0 | grep "+procName+"$\"");
					        			input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
					        			String[] sTemp = new String[20];
					        			while ((line = input.readLine()) != null) {
					        				if( !line.trim().equals("") ){
					        					sTemp = line.split("\\s+");
						        				rssCurve = Double.parseDouble(sTemp[rssCol].substring(0, sTemp[rssCol].length()-1))/1024.00;
					        				}
					        			}
				        			}
			        			}
		        			}else{ //线程
		        				if(cpuCurveRun){
			        				while(cpuCurveRun){
				        				pr = rt.exec("adb shell \"top -t -n 1 -d 0 | grep "+procName+"\"");
					        			input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
					        			String[] sTemp = new String[20];
					        			while ((line = input.readLine()) != null) {
					        				if( !line.trim().equals("") ){
					        					sTemp = line.split("\\s+");
						        				cpuCurve = Integer.parseInt(sTemp[cpuCol].substring(0, sTemp[cpuCol].length()-1));
					        				}
					        			}
				        			}
			        			}else{
			        				while(rssCurveRun){
				        				pr = rt.exec("adb shell \"top -t -n 1 -d 0 | grep "+procName+"\"");
					        			input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
					        			String[] sTemp = new String[20];
					        			while ((line = input.readLine()) != null) {
					        				if( !line.trim().equals("") ){
					        					sTemp = line.split("\\s+");
						        				rssCurve = Double.parseDouble(sTemp[rssCol].substring(0, sTemp[rssCol].length()-1))/1024.00;
					        				}
					        			}
				        			}
			        			}
		        			}
		        			
		            		
				        } catch (Exception ex) {
				            ex.printStackTrace();
				        }
		            }
		        }.start();
		        
				new Thread() {
					String procName = text_packname4.getText().trim();
		            public void run() {
		            	try {
		            		if(cpuCurveRun){
		            			//曲线图变量
			            		long serialVersionUID = 1L;
			            		TimeSeries timeSeries = new TimeSeries(procName+": CPU (%)", Millisecond.class);
			            		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeSeries);
			            		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(procName+": CPU (%)", "Time (sec)", "%", timeseriescollection, true, true, false);
			            		XYPlot xyplot = jfreechart.getXYPlot();
			            		JFrame frame = new JFrame("CPU");
			            		ValueAxis valueaxis = xyplot.getDomainAxis();
			            		valueaxis.setAutoRange(true);
			            		valueaxis.setFixedAutoRange(30000D);
			            		valueaxis = xyplot.getRangeAxis();
			            		frame.getContentPane().add(new ChartPanel(jfreechart), new BorderLayout().CENTER);
			            		frame.pack();
			            		frame.setVisible(true);
			            		frame.addWindowListener(new WindowAdapter() {
			            			public void windowClosing(WindowEvent windowevent) {
			            				cpuCurveRun = false;
			            				rssCurveRun = false;
			            			}
			            		});
			            		
			            		DecimalFormat decimalFormat = new DecimalFormat(".0");
			            		String dbValue;
			            		while( cpuCurveRun ){
									dbValue = decimalFormat.format(cpuCurve);
			            			timeSeries.add(new Millisecond(), Double.parseDouble(dbValue));
			        				Thread.sleep(100);
			            		}
		            		}else{
		            			//曲线图变量
			            		long serialVersionUID = 1L;
			            		TimeSeries timeSeries = new TimeSeries(procName+": RSS (M)", Millisecond.class);
			            		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeSeries);
			            		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(procName+": RSS (M)", "Time (sec)", "M", timeseriescollection, true, true, false);
			            		XYPlot xyplot = jfreechart.getXYPlot();
			            		JFrame frame = new JFrame("RSS");
			            		ValueAxis valueaxis = xyplot.getDomainAxis();
			            		valueaxis.setAutoRange(true);
			            		valueaxis.setFixedAutoRange(30000D);
			            		valueaxis = xyplot.getRangeAxis();
			            		frame.getContentPane().add(new ChartPanel(jfreechart), new BorderLayout().CENTER);
			            		frame.pack();
			            		frame.setVisible(true);
			            		frame.addWindowListener(new WindowAdapter() {
			            			public void windowClosing(WindowEvent windowevent) {
			            				cpuCurveRun = false;
			            				rssCurveRun = false;
			            			}
			            		});
			            		
			            		DecimalFormat decimalFormat = new DecimalFormat(".0");
			            		String dbValue;
			            		while( rssCurveRun ){
									dbValue = decimalFormat.format(rssCurve);
			            			timeSeries.add(new Millisecond(), Double.parseDouble(dbValue));
			        				Thread.sleep(100);
			            		}
		            		}
		            		
				        } catch (Exception ex) {
				            ex.printStackTrace();
				        }
		            }
		        }.start();
		        
		        //文本框写入配置文件
				FileWriter fwriter2 = null;
				try {
					fwriter2 = new FileWriter("config\\text0", false);
					fwriter2.write(checkTxt1.getText().trim()+"\r\n");
					fwriter2.write(text_packname1.getText().trim()+"\r\n");
					fwriter2.write(text_packname3.getText().trim()+"\r\n");
					fwriter2.write(text_packname4.getText().trim()+"\r\n");
					fwriter2.write(checkTxt2.getText().trim()+"\r\n");
					fwriter2.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				btnCurve.setEnabled(true);
			}
		});
		btnCurve.setText("\u5B9E\u65F6\u66F2\u7EBF\u663E\u793A");
		btnCurve.setBounds(752, 10, 90, 27);
		
		
		
		Label label_12 = new Label(container, SWT.NONE);
		label_12.setText("\u6211\u8981\u5B9E\u65F6\u67E5\u770B");
		label_12.setBounds(256, 15, 75, 17);
		
		Label label_13 = new Label(container, SWT.NONE);
		label_13.setText("\u7684");
		label_13.setBounds(638, 15, 15, 17);
		

		
		Label label_14 = new Label(container, SWT.NONE);
		label_14.setBounds(395, 51, 25, 17);
		label_14.setText("\u5206\u949F");
		
		
		
		Label label_15 = new Label(container, SWT.NONE);
		label_15.setText("\u5206\u949F");
		label_15.setBounds(395, 321, 25, 17);
		
		
		//是否汇总前面N行
		checkBtn3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkBtn3.getSelection()){
					checkTxt2.setEnabled(true);
				}else{
					checkTxt2.setEnabled(false);
				}
			}
		});
		checkBtn3.setText("\u6C47\u603B\u524D");
		checkBtn3.setBounds(463, 208, 54, 17);
		
		
		Label label_16 = new Label(container, SWT.NONE);
		label_16.setText("\u884C");
		label_16.setBounds(546, 208, 17, 17);
		
		
		
		
		strTemp = "";
		strConfig = "";
		btn_ConnDevice.setFocus();
		
		
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
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP);
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
			TopMainTest window = new TopMainTest();
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
		newShell.setText("TopReader CPU内存测试工具  V5.8");
		newShell.setImage(new Image(display, "./icons/Chart-Line.ICO"));
		newShell.addShellListener(new ShellAdapter(){
			public void shellClosed(ShellEvent e){
				stopTop1 = true;
				stopTop2 = true;
				stopMem = true;
				stopProcrank = true;
				tips1 = false;
				tips2 = false;
				tips3 = false;
				tips4 = false;
				cpuCurveRun = false;
				rssCurveRun = false;
				System.exit(0);
				
			  }
		});
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(913, 645);
	}
}