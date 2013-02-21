/**
 * @author aultj
 * 
 * This interface is the interface that all the concrete decorators implement in the PlotPanel
 * See the concrete decorators for the implementation of these methods
 */
package edu.msoe.se2800.h4.jplot.plotPanel;

import java.awt.Component;

import lejos.robotics.navigation.Waypoint;

public interface PlotPanelInterface {
	
	public Component getComponent();
	
	public Waypoint translateToNearestPoint(Waypoint p);
	
	public Waypoint getInterceptedPoint(Waypoint point);
	
	public Waypoint getActivePoint();
	
	public int getActivePointIndexHolder();
	
	public void setActivePoint(Waypoint p);
	
	public void setActivePointIndexHolder(int index);

}
