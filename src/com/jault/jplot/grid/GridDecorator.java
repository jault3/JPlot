/**
 * @author aultj
 * 
 * This abstract decorator holds the grid, info panel, and stats panel components that will be wrapped in the 
 * concrete decorators. It simply initializes these wrapped components and provides getters and setters defined
 * in GridInterface.java
 */
package edu.msoe.se2800.h4.jplot.grid;

import edu.msoe.se2800.h4.jplot.InfoPanel;
import edu.msoe.se2800.h4.jplot.JPlotController;
import edu.msoe.se2800.h4.jplot.StatsPanel;

import java.awt.BorderLayout;
import java.awt.Component;

public abstract class GridDecorator implements GridInterface {
	
	protected GridInterface grid;
	protected InfoPanel infoPanel;
	protected StatsPanel statsPanel;
	
	public GridDecorator(GridInterface grid) {
		this.grid = grid;
	}
	
	/**
	 * Initializes wrapped components and adds them to the overall JPanel in the correct locations of the
	 * BorderLayout
	 */
	public void initSubviews() {
		grid.initSubviews();
		infoPanel = new InfoPanel();
		infoPanel.initSubviews();
		addSubview(infoPanel, BorderLayout.EAST);
		statsPanel = new StatsPanel();
		statsPanel.initSubviews();
		addSubview(statsPanel, BorderLayout.NORTH);
		JPlotController.getInstance().getEventBus().register(statsPanel);
	}
	
	@Override
	public void addSubview(Component c, Object constraints) {
		grid.addSubview(c, constraints);
	}

	@Override
	public Component getComponent() {
		return grid.getComponent();
	}

    @Override
    public void redraw() {
        grid.redraw();
    }
}
