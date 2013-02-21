/**
 * @author aultj
 * 
 * This interface is the interface that all the concrete decorators implement in the JPlot
 * See the concrete decorators for the implementation of these methods
 */
package edu.msoe.se2800.h4.jplot;

import javax.swing.JFrame;

public interface JPlotInterface {
	
	public JFrame getFrame();
	
	public void initSubviews();

}
