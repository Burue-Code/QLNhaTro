package com.nctu.quanlynhatro.main;

import javax.swing.SwingUtilities;

import com.nctu.quanlynhatro.controller.MainController;
import com.nctu.quanlynhatro.view.DashboardView;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			DashboardView view = new DashboardView();
			MainController controller = new MainController(view);
			controller.showMainView();
		});

	}
}
