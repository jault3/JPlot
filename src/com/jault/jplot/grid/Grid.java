/**
 * @author aultj
 * 
 * This class is the main JPanel that is shown on the screen that contains all the other panels such as the Grid,
 * the InfoPanel and the x and y AxisPanels. 
 */
package edu.msoe.se2800.h4.jplot.grid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.JPanel;

import edu.msoe.se2800.h4.administrationFeatures.DatabaseConnection;
import edu.msoe.se2800.h4.jplot.AxisPanel;
import edu.msoe.se2800.h4.jplot.Constants;
import edu.msoe.se2800.h4.jplot.plotPanel.PlotPanel;
import edu.msoe.se2800.h4.jplot.plotPanel.PlotPanelAdminDecorator;
import edu.msoe.se2800.h4.jplot.plotPanel.PlotPanelImmediateDecorator;
import edu.msoe.se2800.h4.jplot.plotPanel.PlotPanelInterface;

public class Grid extends JPanel implements GridInterface {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1623331249534914071L;
	
	private PlotPanelInterface plotPanel;
	private AxisPanel xAxisPanel, yAxisPanel;
	private File loadedFile;

    /**
     * Grid constructor, setting layout and size
     */
	public Grid() {
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
		setVisible(true);
		
		redraw();
	}

    /**
     * Adds the x & y axis. Adds the center panel based on {@link GridMode}
     */
	@Override
	public void initSubviews() {
		plotPanel = new PlotPanel();
		if (Constants.CURRENT_MODE == DatabaseConnection.UserTypes.ADMIN) {
			plotPanel = new PlotPanelAdminDecorator(plotPanel);
		} else if (Constants.CURRENT_MODE == DatabaseConnection.UserTypes.OTHER) {
			plotPanel = new PlotPanelImmediateDecorator(plotPanel);
		}
		xAxisPanel = new AxisPanel(Constants.HORIZONTAL);
		yAxisPanel = new AxisPanel(Constants.VERTICAL);
		
		add(xAxisPanel, BorderLayout.SOUTH);
		add(yAxisPanel, BorderLayout.WEST);
		add(plotPanel.getComponent(), BorderLayout.CENTER);
	}

    /**
     * Used to add as a subview for the Decorators
     * @param c
     * @param constraints
     */
	@Override
	public void addSubview(Component c, Object constraints) {
		add(c, constraints);
	}

    /**
     * Repaints the frame
     */
	@Override
	public void redraw() {
		repaint();
	}

    /**
     * @return the loaded file
     */
	public File getLoadedPathFile() {
		return loadedFile;
	}

    /**
     * @param file that contains the path to load
     */
	public void setLoadedPathFile(File file) {
		this.loadedFile = file;
	}

    /**
     * Sets the background color of the grid and repaints
     * @param g
     */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		g.clearRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

    /**
     * @return this Grid to be added to the main GUI
     */
	@Override
	public Component getComponent() {
		return this;
	}

}
