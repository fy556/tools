package swt.login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import swt.DBHelper;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Signin extends JFrame {

	private JPanel contentPane;
	private JTextField SigninAccount;
	private JTextField SigninEmail;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Signin frame = new Signin();
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
	public Signin() {
		setTitle("Sign in");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u6CE8\u518C\u8D26\u53F7");
		lblNewLabel.setBounds(91, 10, 73, 34);
		contentPane.add(lblNewLabel);

		JLabel label = new JLabel("\u5BC6\u7801");
		label.setBounds(91, 54, 73, 34);
		contentPane.add(label);

		JLabel label_1 = new JLabel("\u786E\u8BA4\u5BC6\u7801");
		label_1.setBounds(91, 98, 73, 34);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("\u90AE\u7BB1");
		label_2.setBounds(91, 142, 73, 34);
		contentPane.add(label_2);

		JLabel hint1 = new JLabel("New label");
		hint1.setBounds(335, 10, 89, 34);
		contentPane.add(hint1);

		JLabel hint2 = new JLabel("New label");
		hint2.setBounds(335, 54, 89, 34);
		contentPane.add(hint2);

		JLabel hint3 = new JLabel("New label");
		hint3.setBounds(335, 98, 89, 34);
		contentPane.add(hint3);

		JLabel hint4 = new JLabel("New label");
		hint4.setBounds(335, 142, 89, 34);
		contentPane.add(hint4);

		SigninAccount = new JTextField();
		SigninAccount.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(SigninAccount.getText().isEmpty()){
					System.out.println("注册账号不能为空");
					hint1.setText("不能为空！");
				}else {
					String sql = "select * from account where UserName = '" + SigninAccount.getText() + "';";
					ResultSet rs = DBHelper.query(sql);// 查询表格中包含该用户名的记录
					try {
						if (!rs.next()) {
							System.out.println("数据库中查询"+SigninAccount.getText()+"账号不存在，可以注册！");
							hint1.setText("可以注册");
						} else {
							System.out.println("数据库中查询"+SigninAccount.getText()+"账号已存在，不可以注册！");
							hint1.setText("账号已被注册");
						}
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(SigninAccount.getText().isEmpty()){
					System.out.println("注册账号不能为空");
					hint1.setText("不能为空！");
				}else {
					String sql = "select * from account where UserName = '" + SigninAccount.getText() + "';";
					ResultSet rs = DBHelper.query(sql);// 查询表格中包含该用户名的记录
					try {
						if (!rs.next()) {
							System.out.println("数据库中查询"+SigninAccount.getText()+"账号不存在，可以注册！");
							hint1.setText("可以注册");
						} else {
							System.out.println("数据库中查询"+SigninAccount.getText()+"账号已存在，不可以注册！");
							hint1.setText("账号已被注册");
						}
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		SigninAccount.setBounds(174, 10, 157, 34);
		contentPane.add(SigninAccount);
		SigninAccount.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(new String(passwordField.getPassword()).isEmpty()){
					System.out.println("密码不能为空");
					hint2.setText("不能为空！");
				}else {
					hint2.setText("");
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(new String(passwordField.getPassword()).isEmpty()){
					System.out.println("密码不能为空");
					hint2.setText("不能为空！");
				}else {
					hint2.setText("");
				}
			}
		});
		passwordField.setBounds(174, 54, 157, 34);
		contentPane.add(passwordField);

		passwordField_1 = new JPasswordField();
		passwordField_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(new String(passwordField_1.getPassword()).isEmpty()){
					System.out.println("确认密码不能为空");
					hint3.setText("不能为空！");
				}else {
					if(new String(passwordField_1.getPassword()).equals(new String(passwordField.getPassword()))){
						hint3.setText("");
					}else {
						hint3.setText("密码不一致");
					}
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(new String(passwordField_1.getPassword()).isEmpty()){
					System.out.println("确认密码不能为空");
					hint3.setText("不能为空！");
				}else {
					if(new String(passwordField_1.getPassword()).equals(new String(passwordField.getPassword()))){
						hint3.setText("");
					}else {
						hint3.setText("密码不一致");
					}
				}
			}
		});
		passwordField_1.setBounds(174, 98, 157, 34);
		contentPane.add(passwordField_1);


		SigninEmail = new JTextField();
		SigninEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(SigninEmail.getText().isEmpty()){
					System.out.println("注册账号的邮箱不能为空");
					hint4.setText("不能为空！");
				}else {
					hint4.setText("");
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(SigninEmail.getText().isEmpty()){
					System.out.println("注册账号的邮箱不能为空");
					hint4.setText("不能为空！");
				}else {
					hint4.setText("");
				}
			}
		});
		SigninEmail.setColumns(10);
		SigninEmail.setBounds(174, 142, 157, 34);
		contentPane.add(SigninEmail);



		JButton button = new JButton("\u7ACB\u5373\u6CE8\u518C");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(SigninAccount.getText().isEmpty()){
					System.out.println("注册账号不能为空");
					hint1.setText("不能为空！");
				}
				if(new String(passwordField.getPassword()).isEmpty()){
					System.out.println("密码不能为空");
					hint2.setText("不能为空！");
				}
				if(new String(passwordField_1.getPassword()).isEmpty()){
					System.out.println("确认密码不能为空");
					hint3.setText("不能为空！");
				}
				if(SigninEmail.getText().isEmpty()){
					System.out.println("注册账号的邮箱不能为空");
					hint4.setText("不能为空！");
				}
				if((!SigninAccount.getText().isEmpty())&&(!new String(passwordField.getPassword()).isEmpty())&&(!new String(passwordField_1.getPassword()).isEmpty())&&(!SigninEmail.getText().isEmpty())){
					if(new String(passwordField_1.getPassword()).equals(new String(passwordField.getPassword()))){
						System.out.println("注册用户名是:"+SigninAccount.getText());
						System.out.println("注册的密码是:"+String.valueOf(passwordField.getPassword()));
						System.out.println("确认的密码是:"+String.valueOf(passwordField_1.getPassword()));
						System.out.println("注册用邮箱是:"+SigninEmail.getText());
						String sql = "select * from account where UserName = '" + SigninAccount.getText() + "';";
						ResultSet rs = DBHelper.query(sql);// 查询表格中包含该用户名的记录
						try {
							if (!rs.next()) {
								System.out.println("数据库中查询"+SigninAccount.getText()+"账号不存在，可以注册！");
								String sql1 = "insert into account(UserName,PassWord,Email) values ('"+SigninAccount.getText()+"','"+new String(passwordField.getPassword())+"','"+SigninEmail.getText()+"');";
								int rs1=DBHelper.update(sql1);
								if(rs1!=-1){
									System.out.println("注册成功");
									/*JDialog dialog=new JDialog(Signin.this, "提示", true);
									Container dialogContainer=dialog.getContentPane();
									
									dialogContainer.add(new JLabel("注册成功"));
									dialogContainer.add(new JButton("确定"));
									dialogContainer.setLayout(null);
									dialog.setSize(200, 100);
									dialog.setVisible(true);*/
//									LoginHint dLoginHint=new LoginHint();
//									dLoginHint.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//									dLoginHint.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
//									dLoginHint.setVisible(true);
									JOptionPane.showMessageDialog(getContentPane(), "注册成功", "提示",JOptionPane.WARNING_MESSAGE);  
								}else {
									System.out.println("注册失败");
								}

							} else {
								System.out.println("数据库中查询"+SigninAccount.getText()+"账号已存在，不可以注册！");
								hint1.setText("账号已被注册");
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}else {
						hint3.setText("密码不一致");
					}
				}
			}
		});
		button.setBounds(174, 213, 93, 23);
		contentPane.add(button);



		JButton back = new JButton("<");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainLogin.UIlogin.setLocation(MainLogin.UIlogin.UIsignin.getX(), MainLogin.UIlogin.UIsignin.getY());
				MainLogin.UIlogin.setVisible(true);
				MainLogin.UIlogin.UIsignin.setVisible(false);
			}
		});
		back.setBounds(10, 10, 25, 22);
		contentPane.add(back);
	}
}
