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
					System.out.println("ע���˺Ų���Ϊ��");
					hint1.setText("����Ϊ�գ�");
				}else {
					String sql = "select * from account where UserName = '" + SigninAccount.getText() + "';";
					ResultSet rs = DBHelper.query(sql);// ��ѯ����а������û����ļ�¼
					try {
						if (!rs.next()) {
							System.out.println("���ݿ��в�ѯ"+SigninAccount.getText()+"�˺Ų����ڣ�����ע�ᣡ");
							hint1.setText("����ע��");
						} else {
							System.out.println("���ݿ��в�ѯ"+SigninAccount.getText()+"�˺��Ѵ��ڣ�������ע�ᣡ");
							hint1.setText("�˺��ѱ�ע��");
						}
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(SigninAccount.getText().isEmpty()){
					System.out.println("ע���˺Ų���Ϊ��");
					hint1.setText("����Ϊ�գ�");
				}else {
					String sql = "select * from account where UserName = '" + SigninAccount.getText() + "';";
					ResultSet rs = DBHelper.query(sql);// ��ѯ����а������û����ļ�¼
					try {
						if (!rs.next()) {
							System.out.println("���ݿ��в�ѯ"+SigninAccount.getText()+"�˺Ų����ڣ�����ע�ᣡ");
							hint1.setText("����ע��");
						} else {
							System.out.println("���ݿ��в�ѯ"+SigninAccount.getText()+"�˺��Ѵ��ڣ�������ע�ᣡ");
							hint1.setText("�˺��ѱ�ע��");
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
					System.out.println("���벻��Ϊ��");
					hint2.setText("����Ϊ�գ�");
				}else {
					hint2.setText("");
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(new String(passwordField.getPassword()).isEmpty()){
					System.out.println("���벻��Ϊ��");
					hint2.setText("����Ϊ�գ�");
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
					System.out.println("ȷ�����벻��Ϊ��");
					hint3.setText("����Ϊ�գ�");
				}else {
					if(new String(passwordField_1.getPassword()).equals(new String(passwordField.getPassword()))){
						hint3.setText("");
					}else {
						hint3.setText("���벻һ��");
					}
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(new String(passwordField_1.getPassword()).isEmpty()){
					System.out.println("ȷ�����벻��Ϊ��");
					hint3.setText("����Ϊ�գ�");
				}else {
					if(new String(passwordField_1.getPassword()).equals(new String(passwordField.getPassword()))){
						hint3.setText("");
					}else {
						hint3.setText("���벻һ��");
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
					System.out.println("ע���˺ŵ����䲻��Ϊ��");
					hint4.setText("����Ϊ�գ�");
				}else {
					hint4.setText("");
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(SigninEmail.getText().isEmpty()){
					System.out.println("ע���˺ŵ����䲻��Ϊ��");
					hint4.setText("����Ϊ�գ�");
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
					System.out.println("ע���˺Ų���Ϊ��");
					hint1.setText("����Ϊ�գ�");
				}
				if(new String(passwordField.getPassword()).isEmpty()){
					System.out.println("���벻��Ϊ��");
					hint2.setText("����Ϊ�գ�");
				}
				if(new String(passwordField_1.getPassword()).isEmpty()){
					System.out.println("ȷ�����벻��Ϊ��");
					hint3.setText("����Ϊ�գ�");
				}
				if(SigninEmail.getText().isEmpty()){
					System.out.println("ע���˺ŵ����䲻��Ϊ��");
					hint4.setText("����Ϊ�գ�");
				}
				if((!SigninAccount.getText().isEmpty())&&(!new String(passwordField.getPassword()).isEmpty())&&(!new String(passwordField_1.getPassword()).isEmpty())&&(!SigninEmail.getText().isEmpty())){
					if(new String(passwordField_1.getPassword()).equals(new String(passwordField.getPassword()))){
						System.out.println("ע���û�����:"+SigninAccount.getText());
						System.out.println("ע���������:"+String.valueOf(passwordField.getPassword()));
						System.out.println("ȷ�ϵ�������:"+String.valueOf(passwordField_1.getPassword()));
						System.out.println("ע����������:"+SigninEmail.getText());
						String sql = "select * from account where UserName = '" + SigninAccount.getText() + "';";
						ResultSet rs = DBHelper.query(sql);// ��ѯ����а������û����ļ�¼
						try {
							if (!rs.next()) {
								System.out.println("���ݿ��в�ѯ"+SigninAccount.getText()+"�˺Ų����ڣ�����ע�ᣡ");
								String sql1 = "insert into account(UserName,PassWord,Email) values ('"+SigninAccount.getText()+"','"+new String(passwordField.getPassword())+"','"+SigninEmail.getText()+"');";
								int rs1=DBHelper.update(sql1);
								if(rs1!=-1){
									System.out.println("ע��ɹ�");
									/*JDialog dialog=new JDialog(Signin.this, "��ʾ", true);
									Container dialogContainer=dialog.getContentPane();
									
									dialogContainer.add(new JLabel("ע��ɹ�"));
									dialogContainer.add(new JButton("ȷ��"));
									dialogContainer.setLayout(null);
									dialog.setSize(200, 100);
									dialog.setVisible(true);*/
//									LoginHint dLoginHint=new LoginHint();
//									dLoginHint.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//									dLoginHint.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
//									dLoginHint.setVisible(true);
									JOptionPane.showMessageDialog(getContentPane(), "ע��ɹ�", "��ʾ",JOptionPane.WARNING_MESSAGE);  
								}else {
									System.out.println("ע��ʧ��");
								}

							} else {
								System.out.println("���ݿ��в�ѯ"+SigninAccount.getText()+"�˺��Ѵ��ڣ�������ע�ᣡ");
								hint1.setText("�˺��ѱ�ע��");
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}else {
						hint3.setText("���벻һ��");
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
