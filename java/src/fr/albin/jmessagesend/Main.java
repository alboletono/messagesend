package fr.albin.jmessagesend;

import javax.swing.JFrame;
import fr.albin.jmessagesend.ihm.MainFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new MainFrame();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
