package tools;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import swt.SelectTestUI;
import swt.vad_test.VadJF;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SendMailUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField sendUser_smtpAddress;
	private JTextField textField_3;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendMailUI frame = new SendMailUI();
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
	public SendMailUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel label = new JLabel("\u8BF7\u8F93\u5165\u53D1\u4EF6\u4EBA\u90AE\u7BB1\u8D26\u53F7");
		label.setBounds(10, 41, 143, 27);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("\u8BF7\u8F93\u5165\u53D1\u4EF6\u4EBA\u90AE\u7BB1\u5BC6\u7801");
		label_1.setBounds(10, 78, 143, 27);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("\u610F\u89C1\u53CD\u9988");
		label_2.setBounds(10, 247, 84, 27);
		contentPane.add(label_2);
		
		JButton button = new JButton("\u53D1\u9001");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String SendmailUserName=textField.getText();
				String SendmailPassWord=textField_1.getText();
				String Sendmail_transport_protocol=textField_3.getText();
				String Sendmail_transport_port=textField_4.getText();
				String Sendmail_smtpHostAddress=sendUser_smtpAddress.getText();
				
				String ReceiveMailAccount=textField_2.getText();
				
				
				// 1. 创建参数配置, 用于连接邮件服务器的参数配置
				Properties props = new Properties();                    // 参数配置
				props.setProperty("mail.transport.protocol", Sendmail_transport_protocol);   
				props.setProperty("mail.smtp.host", Sendmail_smtpHostAddress);   
				props.setProperty("mail.smtp.auth", "true");            

			

				final String smtpPort = "465";
				props.setProperty("mail.smtp.port", Sendmail_transport_port);
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.smtp.socketFactory.fallback", "false");
				props.setProperty("mail.smtp.socketFactory.port", Sendmail_transport_port);


				// 2. 根据配置创建会话对象, 用于和邮件服务器交互
				Session session = Session.getInstance(props);
				session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发log

				// 3. 创建
				MimeMessage message;
				try {
					message = createMimeMessage(session, SendmailUserName,ReceiveMailAccount);
					// 4. 根据 Session 获取邮件传输对象
					Transport transport = session.getTransport();

				
					transport.connect(SendmailUserName, SendmailPassWord);

					
					transport.sendMessage(message, message.getAllRecipients());

					// 7. 关闭连接
					transport.close();
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
			}
		});
		button.setBounds(529, 379, 93, 23);
		contentPane.add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 247, 345, -129);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 286, 345, 116);
		contentPane.add(textArea);
		
		textField = new JTextField();
		textField.setBounds(163, 44, 192, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(163, 81, 192, 21);
		contentPane.add(textField_1);
		
		JLabel lblstmp = new JLabel("\u8BF7\u8F93\u5165\u53D1\u4EF6\u4EBA\u90AE\u7BB1\u7684smtp\u670D\u52A1\u5668\u4E3B\u673A\u5730\u5740\uFF1A\u4F8B\u5982smtp.xxx.com");
		lblstmp.setBounds(10, 142, 345, 27);
		contentPane.add(lblstmp);
		
		JLabel label_4 = new JLabel("\u8BF7\u8F93\u5165\u6536\u4EF6\u4EBA\u90AE\u7BB1\u8D26\u53F7");
		label_4.setBounds(10, 210, 143, 27);
		contentPane.add(label_4);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(163, 213, 192, 21);
		contentPane.add(textField_2);
		
		sendUser_smtpAddress = new JTextField();
		sendUser_smtpAddress.setBounds(10, 179, 345, 21);
		contentPane.add(sendUser_smtpAddress);
		sendUser_smtpAddress.setColumns(10);
		
		JLabel label_3 = new JLabel("\u8BF7\u8F93\u5165\u53D1\u4EF6\u4EBA\u90AE\u7BB1\u534F\u8BAE");
		label_3.setBounds(10, 115, 143, 27);
		contentPane.add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(163, 118, 192, 21);
		contentPane.add(textField_3);
		
		JLabel label_5 = new JLabel("\u7AEF\u53E3");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(363, 115, 37, 27);
		contentPane.add(label_5);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(410, 118, 93, 21);
		contentPane.add(textField_4);
		
		JButton button_1 = new JButton("<");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectTestUI.Selectframe.setLocation(SendMailUI.this.getX(), SendMailUI.this.getY());
				SelectTestUI.Selectframe.setVisible(true);
				SendMailUI.this.dispose();
			}
		});
		button_1.setBounds(10, 10, 25, 22);
		contentPane.add(button_1);
	}
	
	
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. 创建
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称?
		message.setFrom(new InternetAddress(sendMail, "test1", "UTF-8"));

		// 3. To: 收件人（可以增加多个收件人抄送密送）
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "XX用户", "UTF-8"));

		// 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
		message.setSubject("test", "UTF-8");

		// 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发内容）
		MimeBodyPart text=new MimeBodyPart();
		text.setContent("XX用户你好", "text/html;charset=UTF-8");

		MimeBodyPart attachment=new MimeBodyPart();
		DataHandler dh2=new DataHandler(new FileDataSource("E:/UIAuto/1.xml"));
		attachment.setDataHandler(dh2);
		attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

		MimeMultipart mm=new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(attachment);
		mm.setSubType("mixed");

		message.setContent(mm);
		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}
}
