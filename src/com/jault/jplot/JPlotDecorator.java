/**
 * @author aultj
 * 
 * This class holds the JPlot component that is used for wrapping in the Decorator pattern
 */
package edu.msoe.se2800.h4.jplot;

import javax.swing.JFrame;


public abstract class JPlotDecorator implements JPlotInterface {
	
	/**
	 * the wrapped component
	 */
	protected JPlotInterface jplot;
	
	public JPlotDecorator(JPlotInterface jplot) {
		this.jplot = jplot;
	}
	
	@Override
	public JFrame getFrame() {
		return jplot.getFrame();
	}

}
