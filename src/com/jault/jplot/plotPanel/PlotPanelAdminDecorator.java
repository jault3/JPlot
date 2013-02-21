/**
 * @author aultj
 * 
 * When an administrator is logged in, this PlotPanelAdminDecorator class is wrapped around 
 * the original PlotPanel in order to allow a right click to add or delete points.  If you are not
 * an administrator you should not be able to edit the points.
 */
package edu.msoe.se2800.h4.jplot.plotPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import lejos.robotics.navigation.Waypoint;
import edu.msoe.se2800.h4.jplot.Constants;
import edu.msoe.se2800.h4.jplot.JPlotController;

public class PlotPanelAdminDecorator extends PlotPanelDecorator {
	
	private PopUpDemo popUp;

	public PlotPanelAdminDecorator(PlotPanelInterface plotPanel) {
		super(plotPanel);
		getComponent().addMouseListener(new PlotMouseAdapter());
	}
	
	/** Listeners and Adapters **/
	private class PlotMouseAdapter extends MouseAdapter {
		
		/**
		 * A single mouse click is used to highlight a point orange if it was clicked on, this method
		 * determines which point was clicked on and sets the flag for the controller to draw it orange on the
		 * next repaint
		 */
		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON1) {
				Waypoint point = plotPanel.getInterceptedPoint(new Waypoint(event.getX(), event.getY()));
				boolean found = false;
				if (point != null) {
					for (Waypoint p : JPlotController.getInstance().getPath()) {
						if (p.x == point.x && p.y == point.y) {
							found = true;
							JPlotController.getInstance().setHighlightedPoint(JPlotController.getInstance().getPath().indexOf(p));
						}
					}
				}
				if (found == false) {
					JPlotController.getInstance().setHighlightedPoint(-5);
				}
				JPlotController.getInstance().getGrid().redraw();
			} else if (event.getButton() == MouseEvent.BUTTON3) {
				doPop(event);
			}
		}
		/**
		 * This method is in charge of finding out which point is closest to where the user clicked and
		 * if they start dragging, the program knows which points to start moving from this method
		 */
		@Override
		public void mousePressed(MouseEvent event) {
			Waypoint p = new Waypoint(event.getX(), event.getY());
			setActivePoint(getInterceptedPoint(p));
			setActivePointIndexHolder(JPlotController.getInstance().getPath().indexOf(getActivePoint()));
			Constants.DRAGGING_INDEX = getActivePointIndexHolder();
		}
		/**
		 * Resets the point that should be dragged to null after a drag motion is finished
		 */
		@Override
		public void mouseReleased(MouseEvent event) {
			setActivePoint(null);
			setActivePointIndexHolder(-5);
			Constants.DRAGGING_INDEX = -5;
			JPlotController.getInstance().getGrid().redraw();
			Constants.HOVER_INDEX = -5;
		}
	}
	
	/** copied this from the interwebs 
	 * It is the right click menu to add/delete points **/
	private void doPop(MouseEvent e){
		popUp = new PopUpDemo(e);
		popUp.show(e.getComponent(), e.getX(), e.getY());
	}
	private class PopUpDemo extends JPopupMenu {
	    /** Generated serialVersionUID */
		private static final long serialVersionUID = -926882311315622109L;
		JMenuItem add;
	    JMenuItem delete;
	    Waypoint clickedPoint;
	    MenuListener menuListener;
	    public PopUpDemo(MouseEvent e){
	    	menuListener = new MenuListener();
	    	clickedPoint = new Waypoint(e.getX(), e.getY());
	        add = new JMenuItem("Add point");
	        add.setActionCommand("add_point");
	        add.addActionListener(menuListener);
	        delete = new JMenuItem("Delete point");
	        delete.setActionCommand("delete_point");
	        delete.addActionListener(menuListener);
	        add(add);
	        add(delete);
	    }
	    private class MenuListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("add_point")) {
					JPlotController.getInstance().addPoint(translateToNearestPoint(clickedPoint));
				} else if (e.getActionCommand().equalsIgnoreCase("delete_point")) {
					Waypoint p = getInterceptedPoint(clickedPoint);
					if (p != null) {
						JPlotController.getInstance().removePoint(JPlotController.getInstance().getPath().indexOf(p));
					}
				}
			}
	    }
	}

}
