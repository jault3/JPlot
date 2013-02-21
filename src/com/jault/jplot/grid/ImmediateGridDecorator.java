/**
 * @author aultj
 * 
 * This decorator pattern class is used when in immediate mode so that the buttons to tell the robot to move
 * on the InfoPanel are disabled and cannot be used until out of immediate mode.
 */
package edu.msoe.se2800.h4.jplot.grid;

public class ImmediateGridDecorator extends GridDecorator {
	
	public ImmediateGridDecorator(GridInterface grid) {
		super(grid);
	}
	
	@Override
	public void initSubviews() {
		super.initSubviews();
		infoPanel.disableSubviews();
	}

}
