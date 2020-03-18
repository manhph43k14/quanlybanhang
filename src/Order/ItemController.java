package Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DBConnection.DBConnection;

public class ItemController {
	static Connection conn;

	public static ArrayList<Item> getAllItem() {
		conn = DBConnection.getConnection();
		ArrayList<Item> list = new ArrayList<Item>();
		String sql = "Select * from Items";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				item.setName(rs.getNString("I_name"));
				item.setId(rs.getString("I_id"));
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}
	String getItemId(String itemNameChoose) {
		conn = DBConnection.getConnection();
		String sql = "Select I_id from Items where I_name = N'"+ itemNameChoose + "'";
		String itemIDChoose="";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				itemIDChoose = rs.getString("I_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemIDChoose;
	}
	String getItemName(String itemIDChoose) {
		conn = DBConnection.getConnection();
		String sql = "Select I_name from Items where I_id = N'"+ itemIDChoose + "'";
		String itemNameChoose="";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				itemNameChoose = rs.getString("I_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemNameChoose;
	}
}
