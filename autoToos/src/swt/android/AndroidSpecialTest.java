package swt.android;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import swt.SelectTestUI;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AndroidSpecialTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AndroidSpecialTest frame = new AndroidSpecialTest();
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
	public AndroidSpecialTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 794, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton button = new JButton("<");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectTestUI.Selectframe.setVisible(true);
				AndroidSpecialTest.this.dispose();
			}
		});
		button.setBounds(10, 10, 25, 22);
		contentPane.add(button);
		
		JButton btnapp = new JButton("\u6267\u884C\u542F\u52A8app");
		btnapp.setBounds(56, 44, 116, 23);
		contentPane.add(btnapp);
		
		JButton button_1 = new JButton("\u7535\u91CF");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		button_1.setBounds(56, 145, 116, 23);
		contentPane.add(button_1);
	}

}
