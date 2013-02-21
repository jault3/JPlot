/**
 * @author aultj
 * 
 * This interface is the interface that all the concrete decorators implement in the Grid
 * See the concrete decorators for the implementation of these methods
 */
package edu.msoe.se2800.h4.jplot.grid;

import java.awt.Component;

public interface GridInterface {
	
	public void initSubviews();
	
	public void addSubview(Component c, Object constraints);
	
	public Component getComponent();
	
	public void redraw();

}
