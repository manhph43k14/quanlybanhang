package Login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import DBConnection.DBConnection;
import Order.Order;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Login {

	private JFrame frame, frmLoginSystems;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private static Connection conn;
	private JButton btnReset, btnExit, btnLogin;

	/**
	 * Launch the application.
	 * 
	 * @throws SQLException
	 */
	public Login() {
		initialize();
		btnExitPerformed();
		btnLoginPerformed();
		btnResetPerformed();
	}

	private void decentralization(String position) {
		switch (position) {
		case "TN":
			try {
				Order window = new Order();
				Order.main("TN");
				frame.setVisible(false);
			} catch (Exception e1) {
				//
			}
			break;
		case "QL":
			try {
				Order window = new Order();
				window.main("QL");
				frame.setVisible(false);
			} catch (Exception e1) {
				//
			}
			break;
		case "PV":
			try {
				Order window = new Order();
				window.main("PV");
				frame.setVisible(false);
			} catch (Exception e1) {
				//
			}
			break;
		default:
			try {
				Order window = new Order();
				window.main(null);
				frame.setVisible(false);
			} catch (Exception e1) {
				//
			}
			break;
		}
	}

	private void btnExitPerformed() {
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLoginSystems = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frmLoginSystems, "Confirm if you want to exit", "Login Systems",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	private void btnLoginPerformed() {
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText().trim();
				String password = txtPassword.getText().trim();

				AccountController ac = new AccountController();
				ArrayList<Account> list = ac.getAllAccount();
				ArrayList<String> arrayAccount = new ArrayList<String>();
				for (Account account : list) {
					arrayAccount.add(account.getUsername());
				}
				if (username.length() > 0 && password.length() > 0) {

					if (arrayAccount.contains(username)) {
						int index = arrayAccount.indexOf(username);
						String temp = list.get(index).getPassword().trim();
						if (password.equals(temp)) {
							String position = list.get(index).getId().trim().substring(0,2);
							decentralization(position);
						}
					} else {
						JOptionPane.showConfirmDialog(null,
								"The username and password do not match. Please check and try again.", "Login Error",
								JOptionPane.ERROR_MESSAGE);
						txtPassword.setText(null);
					}

				} else {
					JOptionPane.showConfirmDialog(null, "Invalid Login Details", "Login Error",
							JOptionPane.ERROR_MESSAGE);
					txtPassword.setText(null);
				}

			}
		});
	}

	private void btnResetPerformed() {
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Times New Roman", Font.PLAIN, 14));
		frame.setBounds(200, 100, 509, 265);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblLogin = new JLabel("Login Systems");
		lblLogin.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblLogin.setBounds(158, 11, 190, 46);
		frame.getContentPane().add(lblLogin);

		JLabel lblUsename = new JLabel("Usename");
		lblUsename.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblUsename.setBounds(69, 86, 91, 14);
		frame.getContentPane().add(lblUsename);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPassword.setBounds(69, 124, 91, 14);
		frame.getContentPane().add(lblPassword);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtUsername.setBounds(190, 83, 243, 20);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtPassword.setToolTipText("Password");
		txtPassword.setBounds(190, 121, 243, 20);
		frame.getContentPane().add(txtPassword);

		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnLogin.setBounds(58, 176, 105, 23);
		frame.getContentPane().add(btnLogin);

		btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnExit.setBounds(357, 176, 100, 23);
		frame.getContentPane().add(btnExit);

		JSeparator separator = new JSeparator();
		separator.setBounds(48, 162, 409, 2);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(48, 58, 409, 2);
		frame.getContentPane().add(separator_1);

		btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnReset.setBounds(215, 176, 91, 23);
		frame.getContentPane().add(btnReset);
	}

	public static void main(String[] args) throws SQLException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
