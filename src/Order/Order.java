package Order;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import DBConnection.DBConnection;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;

import javax.swing.JComboBox;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.sun.webkit.ContextMenu.ShowContext;

import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Order {

	private static Connection conn;
	private JFrame frameOrder;
	private JTextField txtAmount, txtBiNo, txtEmployees, txtTotal, txtPayment, txtExtraMoney;
	private String itemNameChoose, itemIDChoose;
	private JTable table;
	private JComboBox cbbItemName;
	private DefaultTableModel tbn;
	private JButton btnReset, btnAdd, btnUpdate, btnDelete, btnPrint, btnExit, btnSearch, btnNew, btnSubtraction;
	private JPanel panelOption;

	/**
	 * Launch the application.
	 * 
	 * @throws SQLException
	 */

	public Order() {
		initialize(); // design UI
		loadCombobox(); // load data for combobox item and size
		loadDataTable(); // loadData for BillsDetail
		btnPrintPerformed();
		btnExitPerformed();
		btnSearchPerformed();
		btnSubtractionPerformed();
	}

	public void OrderTN() {
		btnNewPerformed();
		btnResetPerformed();
		btnAddPerformed();
		btnUpdatePerformed();
		btnDeletePerformed();
	}

	public void OrderQL() {
		createAccount();
	}

	private void updateBills() {
		int moneyPayment = 0;
		if (!txtPayment.getText().equals("")) {
			moneyPayment = Integer.parseInt(txtPayment.getText());
		} else {
			txtPayment.setText(String.valueOf(0));
			moneyPayment = 0;
		}
		int total = Integer.parseInt(txtTotal.getText());
		txtExtraMoney.setText(String.valueOf(moneyPayment - total));

		try {
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(
					"Update Bills Set Bi_timeOut = cast(getdate() as time), Bi_moneyTotal = ?, Bi_payments = ?,  Bi_extraMoney = ? where Bi_id = ?");
			ps.setString(1, txtTotal.getText().trim());
			if (!txtPayment.getText().equals("")) {
				ps.setString(2, String.valueOf(0));
			} else {
				ps.setString(2, txtPayment.getText());
			}
			ps.setString(3, txtExtraMoney.getText().trim());
			ps.setString(4, txtBiNo.getText().trim());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void btnSubtractionPerformed() {
		btnSubtraction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateBills();
			}
		});

	}

	private void createAccount() {

	}

	private void btnNewPerformed() {
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = DBConnection.getConnection();
					PreparedStatement ps = conn.prepareStatement(
							"Insert into Bills(Bi_id,Bi_date,Bi_timeIn,E_id) values(trim(dbo.newBillsID()),cast(getdate() as date),cast(getdate() as time),?)");
					ps.setString(1, txtEmployees.getText().trim().toUpperCase());
					int check = ps.executeUpdate();
					if (check > 0) {
						JOptionPane.showMessageDialog(panelOption, "New bill successful");
						tbn.setRowCount(0);
						loadDatatxtBiNo();
						loadDataTable();
					} else {
						JOptionPane.showMessageDialog(panelOption, "New bill fail");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void btnSearchPerformed() {
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					conn = DBConnection.getConnection();
					PreparedStatement ps = conn.prepareStatement("Select Bi_id From BillsDetail where Bi_id = ?");
					ps.setString(1, txtBiNo.getText());
					tbn.setRowCount(0);
					loadDataTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void btnExitPerformed() {
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frameOrder = new JFrame();
				if (JOptionPane.showConfirmDialog(frameOrder, "Confirm if you want to exit", "Systems",
						JOptionPane.YES_NO_OPTION) == (JOptionPane.YES_NO_OPTION)) {
					System.exit(0);
				}
			}
		});

	}

	private void btnPrintPerformed() {
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// Connect printer
				} catch (Exception e) {
					System.err.format("No Printer found", e.getMessage());
				}
			}
		});

	}

	private void btnDeletePerformed() {
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = DBConnection.getConnection();
					ItemController item = new ItemController();
					PreparedStatement ps = conn
							.prepareStatement("Delete From BillsDetail where Bi_id = ? and I_id = ?");
					ps.setString(1, txtBiNo.getText());
					ps.setString(2, item.getItemId(cbbItemName.getSelectedItem().toString()));

					if (JOptionPane.showConfirmDialog(frameOrder, "Delete this item?", "Confirm",
							JOptionPane.YES_NO_OPTION) == (JOptionPane.YES_NO_OPTION)) {
						ps.executeUpdate();
						txtAmount.setText(null); // set default txtAmount
						tbn.setRowCount(0);
						loadCombobox(); // set default cbbItemName
						loadDataTable();
						updateBills();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void btnUpdatePerformed() {
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = DBConnection.getConnection();
					ItemController item = new ItemController();
					PreparedStatement ps = conn
							.prepareStatement("Update BillsDetail set I_amount =? where Bi_id = ? and I_id =? ");
					ps.setString(3, item.getItemId(cbbItemName.getSelectedItem().toString()).trim());
					ps.setString(1, txtAmount.getText());
					ps.setString(2, txtBiNo.getText().trim());
					int check = ps.executeUpdate();
					if (check > 0) {
						JOptionPane.showMessageDialog(panelOption, "Update successful");
						tbn.setRowCount(0);
						loadDataTable();

					} else {
						JOptionPane.showMessageDialog(panelOption, "Update fail");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void btnResetPerformed() {
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadCombobox();
				cbbItemName.setSelectedItem(0);
				txtAmount.setText(null);
			}
		});
	}

	private void btnAddPerformed() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = DBConnection.getConnection();
					ItemController item = new ItemController();
					PreparedStatement ps = conn
							.prepareStatement("Insert into BillsDetail(Bi_id,I_id,I_amount) values(?,?,?)");
					ps.setString(1, txtBiNo.getText().toUpperCase());
					ps.setString(2, item.getItemId(cbbItemName.getSelectedItem().toString()));
					ps.setString(3, txtAmount.getText());

					int check = ps.executeUpdate();
					if (check > 0) {
						JOptionPane.showMessageDialog(panelOption, "Add successful");
						tbn.setRowCount(0);
						loadDataTable();
					} else {
						JOptionPane.showMessageDialog(panelOption, "Add fail");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void loadCombobox() {
		try {
			Combobox combobox = new Combobox();
			cbbItemName.setModel(combobox.initItem());
			cbbItemName.addPopupMenuListener(new PopupMenuListener() {
				public void popupMenuCanceled(PopupMenuEvent arg0) {
//					cbbItemName.setSelectedItem(1);
				}

				public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
					if (cbbItemName.getSelectedItem().equals(null)) {
						cbbItemName.setSelectedItem(1);
					}
					itemNameChoose = cbbItemName.getSelectedItem().toString();
					ItemController itemController = new ItemController();
					itemIDChoose = itemController.getItemId(itemNameChoose);
				}

				public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
//					cbbItemName.setSelectedItem(1);
				}
			});
		} catch (Exception e) {
			System.out.println("loadComboboxItem: " + e.toString());
		}

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void loadDatatxtBiNo() {
		try {
			PreparedStatement pst = conn.prepareStatement("Select trim(max(Bi_id)) as Bi_id from Bills");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				txtBiNo.setText(rs.getString("Bi_id").trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadDataTable() {
		try {
			conn = DBConnection.getConnection();
			tbn = new DefaultTableModel();
			int number;
			Vector row = null;
			ItemController item = new ItemController();
			PreparedStatement ps = conn.prepareStatement("Select * From BillsDetail where Bi_id = ?");
			ps.setString(1, txtBiNo.getText().trim());
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData metadata = rs.getMetaData();
			number = metadata.getColumnCount(); // get the number of columns
			int total = 0;
			for (int i = 2; i <= number; i++) {
				tbn.addColumn(metadata.getColumnName(i)); // get title of columns
			}
			while (rs.next()) {
				row = new Vector();
				for (int i = 2; i <= number; i++) {
					if (i == number) {
						if (rs.getString(i) == null) {
							total += 0;
						} else {
							total += Integer.parseInt(rs.getString(i));
						}
					}
					if (i == 2) {
						row.addElement(item.getItemName(rs.getString(i).trim()));
					} else {
						row.addElement(rs.getString(i));
					}
				}
				tbn.addRow(row);
				table.setModel(tbn);
			}
			txtTotal.setText(String.valueOf(total));
			// Get data from table to textfield
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					if (table.getSelectedRow() >= 0) {
						txtAmount.setText((String) table.getValueAt(table.getSelectedRow(), 1));
						cbbItemName.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 0));
					}

				}
			});
		} catch (Exception e) {
			System.out.println("loadDataTable: " + e.toString());
			e.printStackTrace();
		}
	}

	public void initialize() {
		frameOrder = new JFrame();
		frameOrder.setBounds(250, 100, 900, 550);
		frameOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameOrder.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(176, 224, 230));
		panel.setBorder(new MatteBorder(7, 7, 7, 7, (Color) new Color(95, 158, 160)));
		panel.setBounds(0, 0, 881, 511);
		frameOrder.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panelOrder = new JPanel();
		panelOrder.setBackground(new Color(176, 224, 230));
		panelOrder.setBorder(new MatteBorder(7, 7, 7, 7, (Color) new Color(95, 158, 160)));
		panelOrder.setBounds(24, 25, 339, 231);
		panel.add(panelOrder);
		panelOrder.setLayout(null);

		JLabel lblItemName = new JLabel("Item name");
		lblItemName.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblItemName.setBounds(35, 31, 75, 41);
		panelOrder.add(lblItemName);

		cbbItemName = new JComboBox();
		cbbItemName.setBackground(new Color(255, 255, 255));
		cbbItemName.setBounds(131, 41, 177, 20);
		panelOrder.add(cbbItemName);

		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setBounds(35, 122, 46, 14);
		panelOrder.add(lblAmount);

		JLabel lblshowValidation = new JLabel("");
		lblshowValidation.setForeground(Color.RED);
		lblshowValidation.setBounds(131, 142, 130, 14);
		panelOrder.add(lblshowValidation);

		txtAmount = new JTextField();
		txtAmount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent even) {
				String string = txtAmount.getText();
				char c = even.getKeyChar();
				if (even.getKeyChar() >= '0' && even.getKeyChar() <= '9') {
					txtAmount.setEditable(true);
				} else {
					if (even.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE
							|| even.getExtendedKeyCode() == KeyEvent.VK_DELETE) {
						txtAmount.setEditable(true);
					} else
						txtAmount.setEditable(false);
				}
			}
		});

		txtAmount.setBackground(new Color(255, 255, 255));
		txtAmount.setBounds(131, 119, 177, 20);
		panelOrder.add(txtAmount);
		txtAmount.setColumns(10);

		btnReset = new JButton("Reset");
		btnReset.setBounds(131, 178, 96, 31);
		panelOrder.add(btnReset);

		panelOption = new JPanel();
		panelOption.setLayout(null);
		panelOption.setBorder(new MatteBorder(7, 7, 7, 7, (Color) new Color(95, 158, 160)));
		panelOption.setBackground(new Color(176, 224, 230));
		panelOption.setBounds(421, 25, 439, 108);
		panel.add(panelOption);
		//
		btnAdd = new JButton("Add");
		btnAdd.setBounds(47, 28, 85, 43);
		panelOption.add(btnAdd);

		btnUpdate = new JButton("Update");
		btnUpdate.setBounds(182, 28, 85, 43);
		panelOption.add(btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setBounds(317, 28, 85, 43);
		panelOption.add(btnDelete);

		JPanel panelBills = new JPanel();
		panelBills.setLayout(null);
		panelBills.setBorder(new MatteBorder(7, 7, 7, 7, (Color) new Color(95, 158, 160)));
		panelBills.setBackground(new Color(255, 255, 255));
		panelBills.setBounds(20, 267, 840, 144);
		panel.add(panelBills);

		table = new JTable(new DefaultTableModel(new Object[][] { { null, null, null, null }, },
				new String[] { "Name", "Amount", "Price", "Money" }));
		table.setFont(new Font("Tahoma", Font.PLAIN, 11));
		table.setSurrendersFocusOnKeystroke(true);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setBounds(10, 11, 820, 128);
		panelBills.add(table);

		JPanel panelLast = new JPanel();
		panelLast.setLayout(null);
		panelLast.setBorder(new MatteBorder(7, 7, 7, 7, (Color) new Color(95, 158, 160)));
		panelLast.setBackground(new Color(176, 224, 230));
		panelLast.setBounds(421, 144, 178, 112);
		panel.add(panelLast);

		btnNew = new JButton("New");
		btnNew.setBounds(37, 54, 96, 31);
		panelLast.add(btnNew);

		txtEmployees = new JTextField();
		txtEmployees.setColumns(10);
		txtEmployees.setBounds(72, 23, 96, 20);
		panelLast.add(txtEmployees);

		JLabel lblEmployees = new JLabel("Employees");
		lblEmployees.setBounds(10, 23, 66, 17);
		panelLast.add(lblEmployees);

		JLabel lblBillNo = new JLabel("Bill number");
		lblBillNo.setBounds(640, 238, 67, 17);
		panel.add(lblBillNo);

		txtBiNo = new JTextField();
		txtBiNo.setBounds(726, 236, 86, 20);
		panel.add(txtBiNo);
		txtBiNo.setColumns(10);

		btnSearch = new JButton("Search");
		btnSearch.setBounds(822, 236, 30, 20);
		panel.add(btnSearch);

		btnPrint = new JButton("Print");
		btnPrint.setBounds(626, 161, 96, 31);
		panel.add(btnPrint);

		btnExit = new JButton("Exit");
		btnExit.setBounds(756, 161, 96, 31);
		panel.add(btnExit);

		JLabel lblTotal = new JLabel("Total");
		lblTotal.setBounds(42, 435, 46, 14);
		panel.add(lblTotal);

		txtTotal = new JTextField();
		txtTotal.setBounds(98, 432, 86, 20);
		panel.add(txtTotal);
		txtTotal.setColumns(10);

		JLabel lblPayment = new JLabel("Payment");
		lblPayment.setBounds(268, 435, 75, 14);
		panel.add(lblPayment);

		txtPayment = new JTextField();
		txtPayment.setBounds(353, 432, 86, 20);
		panel.add(txtPayment);
		txtPayment.setColumns(10);

		JLabel lblExtraMoney = new JLabel("Extra Money");
		lblExtraMoney.setBounds(640, 435, 82, 14);
		panel.add(lblExtraMoney);

		txtExtraMoney = new JTextField();
		txtExtraMoney.setBounds(750, 432, 86, 20);
		panel.add(txtExtraMoney);
		txtExtraMoney.setColumns(10);

		btnSubtraction = new JButton("Subtraction");
		btnSubtraction.setBounds(485, 431, 107, 23);
		panel.add(btnSubtraction);

	}

	public static void main(String args) throws SQLException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					switch (args) {
					case "TN":
						try {
							Order window = new Order();
							window.OrderTN();
							window.frameOrder.setVisible(true);
						} catch (Exception e1) {
							//
						}
						break;
					case "QL":
						try {
							Order window = new Order();
							window.OrderTN();
							window.OrderQL();
							window.frameOrder.setVisible(true);
						} catch (Exception e1) {
							//
						}
						break;
					case "PV":
						try {
							Order window = new Order();
							window.frameOrder.setVisible(true);
						} catch (Exception e1) {
							//
						}
						break;
					default:
						try {
							Order window = new Order();
							window.frameOrder.setVisible(true);
						} catch (Exception e1) {
							//
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
