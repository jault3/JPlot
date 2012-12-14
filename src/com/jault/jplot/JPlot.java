package com.jault.jplot;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jault.jplot.helpers.Constants;

public class JPlot extends JFrame {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8344597455042452839L;
	
	private Grid grid;
	private PopUpDemo popUp;
	
	public JPlot() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("JPlot");
        getContentPane().setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        
        grid = Grid.getInstance();
        
		getContentPane().add(grid);
		
		addMouseListener(new JPlotMouseAdapter());
		
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
		grid.addPoint(new Point(60,60));
	}
	
	/** copied this from the interwebs **/
	private void doPop(MouseEvent e){
		popUp = new PopUpDemo();
		popUp.show(e.getComponent(), e.getX(), e.getY());
	}
	private class PopUpDemo extends JPopupMenu {
	    /** Generated serialVersionUID */
		private static final long serialVersionUID = -926882311315622109L;
		JMenuItem add;
	    JMenuItem delete;
	    public PopUpDemo(){
	        add = new JMenuItem("Add point");
	        delete = new JMenuItem("Delete point");
	        add(add);
	        add(delete);
	    }
	}
	
	/** Listeners and Adapters **/
	private class JPlotMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON3) {
				System.out.println("right clicked");
				doPop(event);
			}
		}
	}

}
