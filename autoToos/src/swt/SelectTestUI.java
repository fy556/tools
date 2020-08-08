package swt;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import swt.android.AndroidMonkey;
import swt.android.AndroidSpecialTest;
import swt.android.AndroidUiautomator;
import swt.vad_test.VadJF;
import tools.SendMailUI;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;


public class SelectTestUI extends JFrame {

	private JPanel contentPane;
	public static SelectTestUI Selectframe=new SelectTestUI();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Selectframe.setVisible(true);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SelectTestUI() {
		setTitle("SelectTest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 656, 509);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAndroidmonkey = new JButton("AndroidMonkey");
		btnAndroidmonkey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AndroidMonkey androidMonkeyframe = new AndroidMonkey();
				androidMonkeyframe.setLocation(SelectTestUI.this.getX(), SelectTestUI.this.getY());
				androidMonkeyframe.setVisible(true);
				SelectTestUI.this.setVisible(false);
			}
		});
		btnAndroidmonkey.setBounds(11, 94, 134, 32);
		contentPane.add(btnAndroidmonkey);
		
		JButton btnAndroiduiautomator = new JButton("AndroidUiautomator");
		btnAndroiduiautomator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AndroidUiautomator androidUiautomator = new AndroidUiautomator();
				androidUiautomator.setLocation(SelectTestUI.this.getX(), SelectTestUI.this.getY());
				androidUiautomator.setVisible(true);
				SelectTestUI.this.setVisible(false);
			}
		});
		btnAndroiduiautomator.setBounds(301, 94, 186, 32);
		contentPane.add(btnAndroiduiautomator);
		
		JButton btnAndroid = new JButton("Android\u4E13\u9879\u6D4B\u8BD5");
		btnAndroid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AndroidSpecialTest androidSpecialTest = new AndroidSpecialTest();
				androidSpecialTest.setLocation(SelectTestUI.this.getX(), SelectTestUI.this.getY());
				androidSpecialTest.setVisible(true);
				SelectTestUI.this.setVisible(false);
			}
		});
		btnAndroid.setBounds(155, 94, 134, 32);
		contentPane.add(btnAndroid);
		
		JButton btnIosmonkey = new JButton("IOSMonkey");
		btnIosmonkey.setBounds(11, 164, 134, 32);
		contentPane.add(btnIosmonkey);
		
		JButton btnIosuitest = new JButton("IOS XCUitest");
		btnIosuitest.setBounds(301, 164, 186, 32);
		contentPane.add(btnIosuitest);
		
		JButton btnIos = new JButton("IOS\u4E13\u9879\u6D4B\u8BD5");
		btnIos.setBounds(155, 164, 134, 32);
		contentPane.add(btnIos);
		
		JButton btnWebselenuim = new JButton("WebSelenuim");
		btnWebselenuim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnWebselenuim.setBounds(11, 236, 134, 32);
		contentPane.add(btnWebselenuim);
		
		JButton btnApitest = new JButton("APITest");
		btnApitest.setBounds(155, 236, 134, 32);
		contentPane.add(btnApitest);
		
		JLabel label = new JLabel("请选择测试类型:");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setBounds(11, 10, 134, 23);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Android");
		label_1.setBounds(11, 61, 68, 23);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("IOS");
		label_2.setBounds(11, 133, 68, 23);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("WEB");
		label_3.setBounds(11, 203, 68, 23);
		contentPane.add(label_3);

		
		JButton button_1 = new JButton("\u610F\u89C1\u53CD\u9988");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendMailUI mailUI=new SendMailUI();
				mailUI.setLocation(SelectTestUI.this.getX(), SelectTestUI.this.getY());
				mailUI.setVisible(true);
				SelectTestUI.this.setVisible(false);
			}
		});
		button_1.setBounds(543, 438, 97, 23);
		contentPane.add(button_1);
		
		JLabel label_4 = new JLabel("\u8BED\u97F3");
		label_4.setBounds(11, 278, 68, 23);
		contentPane.add(label_4);
		
		JButton btnvad = new JButton("\u8BED\u97F3VAD\u6D4B\u8BD5");
		btnvad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VadJF vadJF=new VadJF();
				vadJF.setLocation(SelectTestUI.this.getX(), SelectTestUI.this.getY());
				vadJF.setVisible(true);
				SelectTestUI.this.setVisible(false);
			}
		});
		btnvad.setBounds(10, 311, 134, 32);
		contentPane.add(btnvad);
		
		JLabel label_5 = new JLabel("\u5176\u4ED6");
		label_5.setBounds(11, 353, 68, 23);
		contentPane.add(label_5);
		
		JButton button = new JButton("\u5176\u4ED6");
		button.setBounds(11, 386, 134, 32);
		contentPane.add(button);
		
		JLabel label_6 = new JLabel("\u7535\u8111\u65F6\u95F4\uFF1A");
		label_6.setBounds(301, 10, 67, 23);
		contentPane.add(label_6);
		
		JLabel label_7 = new JLabel("------");
		label_7.setBounds(372, 10, 68, 23);
		contentPane.add(label_7);
//		setVisible(true);
	}
}
