package swt.login;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import swt.DBHelper;
import swt.SelectTestUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainLogin extends JFrame {

	private JPanel contentPane;
	private JTextField userNameC;
	private JPasswordField passwordC;
	boolean frame=false;
	static MainLogin UIlogin;//��ΪҪ�������ݴ��� ����Ϊstatic
	Signin UIsignin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		
			public void run() {
				try {
					UIlogin=new MainLogin();
					UIlogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	


	/**
	 * Create the frame.
	 */
	public MainLogin() {
		setTitle("login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 401, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_username = new JLabel("\u7528\u6237\u540D");
		lblNewLabel_username.setBounds(116, 20, 45, 22);
		contentPane.add(lblNewLabel_username);
		
		userNameC = new JTextField();
		
		userNameC.setBounds(194, 21, 78, 22);
		contentPane.add(userNameC);
		userNameC.setColumns(10);
		
		JLabel lblNewLabel_password = new JLabel("\u5BC6  \u7801");
		lblNewLabel_password.setBounds(116, 52, 45, 22);
		contentPane.add(lblNewLabel_password);
		
		passwordC = new JPasswordField();
		passwordC.setBounds(194, 53, 78, 22);
		contentPane.add(passwordC);
		
		JButton btn_login = new JButton("\u767B\u5F55");
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select * from account where UserName = '" + userNameC.getText() + "';";
				ResultSet rs = DBHelper.query(sql);// ��ѯ����а������û����ļ�¼
				ResultSetMetaData data;
				try {
					if (!rs.next()) {
//						JDialog dialog=new JDialog(UIlogin);
//						dialog.setTitle("��¼��ʾ");
//						dialog.setBounds(100, 100, 340, 89);
//						dialog.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
//						
//						JLabel label = new JLabel("�˺Ų���Ϊ��");
//						label.setHorizontalAlignment(SwingConstants.CENTER);
//						dialog.getContentPane().add(label);
//						
//						JButton btnOk = new JButton("ok");
//						btnOk.addActionListener(new ActionListener() {
//							public void actionPerformed(ActionEvent e) {
//									dialog.dispose();
//							}
//						});
//						dialog.getContentPane().add(btnOk);
//						dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
//						dialog.setVisible(true);
						JOptionPane.showMessageDialog(getContentPane(), "�˺Ų���Ϊ��", "��ʾ",JOptionPane.WARNING_MESSAGE);  
					} else {
					data = (ResultSetMetaData) rs.getMetaData();
					System.out.println("��ѯ�û�"+userNameC.getText()+"�Ľ��Ϊ��");
					for(int i=1;i<=data.getColumnCount();i++){
						System.out.print(data.getColumnName(i)+" ");
					}
					System.out.println();
					for(int i=1;i<=data.getColumnCount();i++){
						String tableColumnName=rs.getString(i);
						System.out.print(tableColumnName+" ");
					}
					System.out.println();
					String tmp=rs.getString("PassWord");
					if(tmp.equals(new String(passwordC.getPassword()))){
						System.out.println("��¼�ɹ�");
						SelectTestUI selectTestframe = new SelectTestUI();
						selectTestframe.setLocation(MainLogin.this.getX(), MainLogin.this.getY());
						selectTestframe.setVisible(true);
						MainLogin.this.dispose();
						UIsignin.dispose();
						
					}else {
//						JDialog dialog=new JDialog(UIlogin);
//						dialog.setTitle("��¼��ʾ");
//						dialog.setBounds(100, 100, 340, 89);
//						dialog.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
//						
//						JLabel label = new JLabel("��¼ʧ��,�������");
//						label.setHorizontalAlignment(SwingConstants.CENTER);
//						dialog.getContentPane().add(label);
//						
//						JButton btnOk = new JButton("ok");
//						btnOk.addActionListener(new ActionListener() {
//							public void actionPerformed(ActionEvent e) {
//									dialog.dispose();
//							}
//						});
//						dialog.getContentPane().add(btnOk);
//						dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
//						dialog.setVisible(true);
						JOptionPane.showMessageDialog(getContentPane(), "�˺�/�������", "��ʾ",JOptionPane.WARNING_MESSAGE);  
					}

					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}
		});
		btn_login.setBounds(116, 155, 156, 23);
		contentPane.add(btn_login);
		
		JButton btn_signin = new JButton("\u6CE8\u518C\u5165\u53E3");
		btn_signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(frame){
				UIsignin.setLocation(MainLogin.this.getX(), MainLogin.this.getY());
				UIsignin.setVisible(true);
				MainLogin.this.setVisible(false);
				}else {
					UIsignin=new Signin();
					System.out.println("�½�ע�ᴰ��");
				    frame=true;
				    UIsignin.setLocation(MainLogin.this.getX(), MainLogin.this.getY());
				    UIsignin.setVisible(true);
				    MainLogin.this.setVisible(false);
				}
			}
		});
		btn_signin.setBounds(116, 202, 156, 23);
		contentPane.add(btn_signin);
	}
}
