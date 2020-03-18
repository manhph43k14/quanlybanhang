package Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;

import DBConnection.DBConnection;

public class SizeController {
	static Connection conn;

	public static ArrayList<Size> getAllSize(String string) {
		conn = DBConnection.getConnection();
		ArrayList<Size> list = new ArrayList<Size>();
		String sql = "Select * from Size where I_id = '" + string + "'";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Size size = new Size();
				size.setName(rs.getString("S_size"));
				list.add(size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
