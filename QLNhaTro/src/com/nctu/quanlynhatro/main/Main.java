package com.nctu.quanlynhatro.main;

import javax.swing.SwingUtilities;

import com.nctu.quanlynhatro.controller.MainController;
import com.nctu.quanlynhatro.view.DashboardView;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// 1. Tạo View
			DashboardView view = new DashboardView();

			// 2. Tạo Controller và đưa View vào để quản lý
			MainController controller = new MainController(view);

			// 3. Hiển thị
			controller.showMainView();
		});

	}
}
