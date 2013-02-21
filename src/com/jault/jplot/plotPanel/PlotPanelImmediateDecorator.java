/**
 * @author aultj
 * 
 * This concrete decorator is used to wrap the original PlotPanel when in immediate mode
 * It removes all points and every time a click is processed, it removes the old point and draws
 * the new one.
 */
package edu.msoe.se2800.h4.jplot.plotPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import lejos.robotics.navigation.Waypoint;
import edu.msoe.se2800.h4.jplot.JPlotController;

public class PlotPanelImmediateDecorator extends PlotPanelDecorator {

	public PlotPanelImmediateDecorator(PlotPanelInterface plotPanel) {
		super(plotPanel);
		getComponent().addMouseListener(new ImmediateListener());
	}
	
	/**
	 * This method removes the old point and adds a single one to the list
	 * @param point
	 */
	public void replacePoint(Waypoint point) {
		JPlotController.getInstance().getPath().clear();
		JPlotController.getInstance().addPoint(point);
		JPlotController.getInstance().getGrid().redraw();
	}

	/**
	 * every time the user clicks, it replaces the single point being shown with a new one
	 * where they clicked
	 */
	public class ImmediateListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent event) {
			Waypoint p =  new Waypoint(event.getX(), event.getY());
			replacePoint(translateToNearestPoint(p));
		}
		
	}
}
