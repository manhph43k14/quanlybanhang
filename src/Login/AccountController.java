package Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DBConnection.DBConnection;

public class AccountController {
	static Connection conn;

	public static void main(String[] args) {

	}

	public static ArrayList<Account> getAllAccount() {
		conn = DBConnection.getConnection();
		ArrayList<Account> list = new ArrayList<Account>();
		String sql = "Select * from Account";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Account a = new Account();
				a.setUsername(rs.getString("A_account").trim());
				a.setPassword(rs.getString("A_password").trim());
				a.setId(rs.getString("E_id").trim());
				list.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

}
