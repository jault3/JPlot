package com.jault.jplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jault.jplot.helpers.Constants;

public class PlotPanel extends JPanel {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2975690672805786359L;
	
	private int activePointIndexHolder;
	private Point activePointHolder;
	private Point activePoint;
	//private Point draggingPoint;

	public PlotPanel() {
		setPreferredSize(new Dimension(Constants.GRID_WIDTH, Constants.GRID_HEIGHT));
		setBackground(Color.BLACK);
		setVisible(true);
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent event) {
				if (activePoint != null) {
					activePoint = translateToNearestPoint(new Point(event.getX(),event.getY()));
					Grid.getInstance().getPoints().set(activePointIndexHolder, activePoint);
					Grid.getInstance().redraw();
					Graphics g = getGraphics();
					g.setColor(Color.ORANGE);
					drawPoint(g, activePoint);
					//drawLine(g, activePoint, draggingPoint);
				}
			}
			@Override
			public void mouseMoved(MouseEvent event) {//ignore
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				System.out.println("clicked "+event.getX()+","+event.getY());
			}
			@Override
			public void mousePressed(MouseEvent event) {
				Point p = new Point(event.getX(), event.getY());
				setInterceptedPoint(p);
			}
			@Override
			public void mouseReleased(MouseEvent event) {
				activePoint = null;
				activePointHolder = null;
				activePointIndexHolder = -5;
				Constants.DRAGGING_INDEX = -5;
				Grid.getInstance().repaint();
			}
		});
	}
	
	public void setInterceptedPoint(Point point) {
		for (Point p : Grid.getInstance().getPoints()) {
			Point temp = translateToLocation(p);
			temp.x-=Constants.POINT_RADIUS;
			temp.y+=Constants.POINT_RADIUS;
			if ((point.x < temp.x+10 && point.x > temp.x-10) && (point.y < temp.y+10 && point.y > temp.y-10)) {
				activePoint = p;
				activePointHolder = new Point(p.x, p.y);
				activePointIndexHolder = Grid.getInstance().getPoints().indexOf(p);
				Constants.DRAGGING_INDEX = activePointIndexHolder;
			}
		}
		System.out.println(""+activePoint);
	}
	
	public void drawGridLines(int density, Graphics g) {
		int drawingWidth = (Constants.GRID_WIDTH/density)+Constants.GRID_WIDTH;
		int drawingHeight = (Constants.GRID_HEIGHT/density)+Constants.GRID_HEIGHT;
		
		g.setColor(Color.DARK_GRAY);
		
		/** Draws the vertical lines */
		for (int i=Constants.GRID_OFFSET; i<=drawingWidth;i+=(Constants.GRID_WIDTH/density)) g.drawLine(i, 0, i, drawingHeight);
		
		/** Draws the horizontal lines */
		for (int i=(Constants.GRID_HEIGHT-Constants.GRID_OFFSET); i>=(Constants.GRID_HEIGHT-drawingHeight);i-=(Constants.GRID_HEIGHT/density)) g.drawLine(0, i, drawingWidth, i);
	}
	
	public void drawPoint(Graphics g, Point p) {
		//int tempx = p.x;
		//int tempy = p.y;
		
		//if (Constants.SNAP_TO_GRID_CORNERS) {
			//tempx = round(tempx);
			//tempy = round(tempy);
		//}
		
		//x = (int)Math.floor(0.5+((Constants.GRID_WIDTH/Grid.getInstance().getGridDensity())*(x/Constants.STEP_INCREMENT)));
		//y = (int)Math.floor(0.5+((Constants.GRID_HEIGHT/Grid.getInstance().getGridDensity())*(y/Constants.STEP_INCREMENT)));
		
		//we need to "flip" y because pixels start at 0,0 in the top left, but a human readable plot starts 0,0 in the
		//bottom left
		//y = Constants.GRID_HEIGHT-Constants.GRID_OFFSET-y;
		
		//account for the radius of the point and the grid offset
		//x-=(Constants.POINT_RADIUS-Constants.GRID_OFFSET);
		//y+=(Constants.POINT_RADIUS-Constants.GRID_OFFSET);
		Point temp = translateToLocation(p);
		int x = temp.x;
		int y = temp.y;
		x-=Constants.POINT_RADIUS;
		y+=Constants.POINT_RADIUS;
		
		g.fillOval(x, y, Constants.POINT_RADIUS*2, Constants.POINT_RADIUS*2);
	}
	
	public int round(int num) {
		int newNum = 0;
		
		boolean keepStepping = true;
		int highestStep = 0;
		while (keepStepping) {
			if (highestStep >= num) {
				keepStepping = false;
			} else {
				highestStep+=Constants.STEP_INCREMENT;
			}
		}
		if ((highestStep-num) < (num-(highestStep-Constants.STEP_INCREMENT))) {
			newNum = highestStep;
		} else {
			newNum = (highestStep-Constants.STEP_INCREMENT);
		}
		return newNum;
	}
	
	public void drawLine(Graphics g, Point one, Point two) {
		Point translated1 = translateToLocation(one);
		Point translated2 = translateToLocation(two);
		
		//we must add 10 to the y coordinates because the translate method accounts for the offset for points, not a line
		g.drawLine(translated1.x, translated1.y+10, translated2.x, translated2.y+10);
	}
	
	public Point translateToLocation(Point p) {
		Point translated = new Point();
		
		int x = p.x;
		int y = p.y;
		
		if (Constants.SNAP_TO_GRID_CORNERS) {
			x = round(x);
			y = round(y);
		}
		
		x = (int)Math.floor(0.5+((Constants.GRID_WIDTH/Grid.getInstance().getGridDensity())*(x/Constants.STEP_INCREMENT)));
		y = (int)Math.floor(0.5+((Constants.GRID_HEIGHT/Grid.getInstance().getGridDensity())*(y/Constants.STEP_INCREMENT)));
		y = Constants.GRID_HEIGHT-Constants.GRID_OFFSET-y;
		x+=Constants.GRID_OFFSET;
		y-=Constants.GRID_OFFSET;
		translated.x = x;
		translated.y = y;
		
		return translated;
	}
	
	public Point translateToNearestPoint(Point p) {
		Point translated = new Point();
		
		int x = p.x;
		int y = p.y;
		
		x-=Constants.GRID_OFFSET;
		y+=Constants.GRID_OFFSET;
		y = Constants.GRID_HEIGHT-Constants.GRID_OFFSET-y;
		x = (int)Math.floor(0.5+((x*Constants.STEP_INCREMENT)/(Constants.GRID_WIDTH/Grid.getInstance().getGridDensity())));
		y = (int)Math.floor(0.5+((y*Constants.STEP_INCREMENT)/(Constants.GRID_HEIGHT/Grid.getInstance().getGridDensity())));
		
		
		if (Constants.SNAP_TO_GRID_CORNERS) {
			x = round(x);
			y = round(y);
		}
		
		translated.x = x;
		translated.y = y;
		
		return translated;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		drawGridLines(Grid.getInstance().getGridDensity(), g);
		//loop that draws each point
		for (Point p : Grid.getInstance().getPoints()) {
			g.setColor(Color.CYAN);
			if (Grid.getInstance().getPoints().indexOf(p) == Constants.DRAGGING_INDEX) {
				g.setColor(Color.ORANGE);
			}
			drawPoint(g, p);
		}
		
		//loop that draws the lines between each point
		for (int i=0;i<Grid.getInstance().getPoints().size();i++) {
			if (i+1<Grid.getInstance().getPoints().size()) {
				g.setColor(Color.CYAN);
				if (Constants.DRAGGING_INDEX == i+1 || Constants.DRAGGING_INDEX == i) {
					g.setColor(Color.ORANGE);
				}
				drawLine(g, Grid.getInstance().getPoints().get(i), Grid.getInstance().getPoints().get(i+1));
			}
		}
	}
}