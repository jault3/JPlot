package com.jault.jplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jault.jplot.helpers.Constants;

public class Grid extends JPanel {
	
	public static Grid getInstance() {
		return LazyHolder.instance;
	}
	
	private static class LazyHolder {
		private static Grid instance = new Grid();
	}
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1623331249534914071L;
	
	private PlotPanel plotPanel;
	private AxisPanel xAxisPanel, yAxisPanel;
	private List<Point> points;
	private InfoPanel infoPanel;
	private Point highlightedPoint;
	
	private int gridDensity = Constants.DEFAULT_GRID_DENSITY;

	private Grid() {
		points = Collections.synchronizedList(new ArrayList<Point>());
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
		initSubviews();
		setVisible(true);
		
		redraw();
	}
	
	private void initSubviews() {
		plotPanel = new PlotPanel();
		xAxisPanel = new AxisPanel(Constants.HORIZONTAL);
		yAxisPanel = new AxisPanel(Constants.VERTICAL);
		infoPanel = new InfoPanel();
		
		add(xAxisPanel, BorderLayout.SOUTH);
		add(yAxisPanel, BorderLayout.WEST);
		add(infoPanel, BorderLayout.EAST);
		add(plotPanel, BorderLayout.CENTER);
	}
	
	public void addPoint(Point p) {
		points.add(p);
		infoPanel.setPointsLabel(points.size());
		redraw();
	}
	
	public List<Point> getPoints() {
		return this.points;
	}
	
	public int getNumPoints() {
		return points.size();
	}
	
	public void redraw() {
		repaint();
	}
	
	public int getGridDensity() {
		return gridDensity;
	}
	
	public void setGridDensity(int density) {
		gridDensity = density;
		redraw();
	}
	
	public Point getHighlightedPoint() {
		return this.highlightedPoint;
	}
	
	public void setHighlightedPoint(int indexInPointsArray) {
		if (indexInPointsArray == -5) {
			this.highlightedPoint = null;
		} else {			
			this.highlightedPoint = this.points.get(indexInPointsArray);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		g.clearRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

}
