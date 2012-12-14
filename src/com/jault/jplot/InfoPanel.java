package com.jault.jplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jault.jplot.helpers.Constants;

public class InfoPanel extends JPanel {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 4846524799433655631L;
	
	private JTextField xTextField, yTextField;
	
	public InfoPanel() {
		setPreferredSize(new Dimension(Constants.INFO_PANEL_WIDTH, Constants.GRID_HEIGHT));
		setLayout(new FlowLayout(FlowLayout.CENTER));
		initSubviews();
		setVisible(true);
	}
	
	public void initSubviews() {
		xTextField = new JTextField(3);
		yTextField = new JTextField(3);
		JLabel label = new JLabel("x, y");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(100,20));
		add(label);
		add(xTextField);
		add(yTextField);
	}

}
