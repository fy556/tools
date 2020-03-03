package tools;



import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMail {

	// 发件人的 邮箱 �? 密码（替换为自己的邮箱和密码�?
	// PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，�? SMTP 客户端设置了独立密码（有的邮箱称为�?�授权码”）, 
	//     对于�?启了独立密码的邮�?, 这里的邮箱密码必�?使用这个独立密码（授权码）�??
	public static String myEmailAccount = "1953373742@qq.com";
	public static String myEmailPassword = "mdrljkakiaowgidb";

	// 发件人邮箱的 SMTP 服务器地�?, 必须准确, 不同邮件服务器地�?不同, �?�?(只是�?�?, 绝非绝对)格式�?: smtp.xxx.com
	// 网易163邮箱�? SMTP 服务器地�?�?: smtp.163.com
	public static String myEmailSMTPHost = "smtp.qq.com";

	// 收件人邮箱（替换为自己知道的有效邮箱�?
	public static String receiveMailAccount = "1224924674@qq.com";

	public static void main(String[] args) throws Exception {
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();                    // 参数配置
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求�?
		props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱�? SMTP 服务器地�?
		props.setProperty("mail.smtp.auth", "true");            // �?要请求认�?

		// PS: 某些邮箱服务器要�? SMTP 连接�?要使�? SSL 安全认证 (为了提高安全�?, 邮箱支持SSL连接, 也可以自己开�?),
		//     如果无法连接邮件服务�?, 仔细查看控制台打印的 log, 如果有有类似 “连接失�?, 要求 SSL 安全连接�? 等错�?,
		//     打开下面 /* ... */ 之间的注释代�?, �?�? SSL 安全连接�?

		// SMTP 服务器的端口 (�? SSL 连接的端口一般默认为 25, 可以不添�?, 如果�?启了 SSL 连接,
		//                  �?要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
		//                  QQ邮箱的SMTP(SLL)端口�?465�?587, 其他邮箱自行去查�?)
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);


		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getInstance(props);
		session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发�? log

		// 3. 创建�?封邮�?
		MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

		// 4. 根据 Session 获取邮件传输对象
		Transport transport = session.getTransport();

		// 5. 使用 邮箱账号 �? 密码 连接邮件服务�?, 这里认证的邮箱必须与 message 中的发件人邮箱一�?, 否则报错
		// 
		//    PS_01: 成败的判断关键在此一�?, 如果连接服务器失�?, 都会在控制台输出相应失败原因�? log,
		//           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链�?, 根据给出的错�?
		//           类型到对应邮件服务器的帮助网站上查看具体失败原因�?
		//
		//    PS_02: 连接失败的原因�?�常为以下几�?, 仔细�?查代�?:
		//           (1) 邮箱没有�?�? SMTP 服务;
		//           (2) 邮箱密码错误, 例如某些邮箱�?启了独立密码;
		//           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
		//           (4) 请求过于频繁或其他原�?, 被邮件服务器拒绝服务;
		//           (5) 如果以上几点都确定无�?, 到邮件服务器网站查找帮助�?
		//
		//    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明�??
		transport.connect(myEmailAccount, myEmailPassword);

		// 6. 发�?�邮�?, 发到�?有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的�?有收件人, 抄�?�人, 密�?�人
		transport.sendMessage(message, message.getAllRecipients());

		// 7. 关闭连接
		transport.close();
	}

	/**
	 * 创建�?封只包含文本的简单邮�?
	 *
	 * @param session 和服务器交互的会�?
	 * @param sendMail 发件人邮�?
	 * @param receiveMail 收件人邮�?
	 * @return
	 * @throws Exception
	 */
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. 创建�?封邮�?
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称�?
		message.setFrom(new InternetAddress(sendMail, "test1", "UTF-8"));

		// 3. To: 收件人（可以增加多个收件人�?�抄送�?�密送）
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "XX用户", "UTF-8"));

		// 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
		message.setSubject("test", "UTF-8");

		// 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发�?�内容）
		MimeBodyPart text=new MimeBodyPart();
		text.setContent("XX用户你好, 今天全场5�?, 快来抢购, 错过今天再等�?年�?��?��??", "text/html;charset=UTF-8");

		MimeBodyPart attachment=new MimeBodyPart();
		DataHandler dh2=new DataHandler(new FileDataSource("E:/UIAuto/1.xml"));
		attachment.setDataHandler(dh2);
		attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

		MimeMultipart mm=new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(attachment);
		mm.setSubType("mixed");
		//		        message.setContent("XX用户你好, 今天全场5�?, 快来抢购, 错过今天再等�?年�?��?��??", "text/html;charset=UTF-8");
		message.setContent(mm);
		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}
}

