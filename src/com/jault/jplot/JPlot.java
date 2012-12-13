package com.jault.jplot;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

import com.jault.jplot.helpers.Constants;

public class JPlot extends JFrame {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8344597455042452839L;
	
	Grid grid;
	
	public JPlot() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        
        grid = Grid.getInstance();
        
		getContentPane().add(grid);
		
		pack();
		setVisible(true);
		
		/*grid.addPoint(new Point(10,20));
		grid.addPoint(new Point(10,30));
		grid.addPoint(new Point(10,40));
		grid.addPoint(new Point(20,20));
		grid.addPoint(new Point(40,30));
		grid.addPoint(new Point(60,5));*/
		grid.addPoint(new Point(0,0));
		grid.addPoint(new Point(12,12));
		grid.addPoint(new Point(24,24));
		grid.addPoint(new Point(36,36));
		grid.addPoint(new Point(48,48));
	}

}
