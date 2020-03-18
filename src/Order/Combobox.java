package Order;

import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import DBConnection.DBConnection;

public class Combobox extends javax.swing.JFrame {
	private DefaultComboBoxModel<String> itemModel;
	private DefaultComboBoxModel<String> sizeModel;

	public Combobox() {
		initItem();
	}

	DefaultComboBoxModel<String> initItem() {
		itemModel = new DefaultComboBoxModel<String>();
		ItemController itemController = new ItemController();
		ArrayList<Item> list = itemController.getAllItem();
		if (list.size() > 0) {
			for (Item item : list) {
				itemModel.addElement(item.getName());
			}
		} else {
			System.out.println("List item is empty");
		}
		return itemModel;
	}


	DefaultComboBoxModel<String> initSize(String string) {
		sizeModel = new DefaultComboBoxModel<String>();
		SizeController sizeController = new SizeController();
		ArrayList<Size> list = sizeController.getAllSize(string);
		if (list.size() > 0) {
			for (Size size : list) {
				sizeModel.addElement(size.getName());
			}
		} else {
			System.out.println("List size is empty");
		}
		return sizeModel;

	}
}
