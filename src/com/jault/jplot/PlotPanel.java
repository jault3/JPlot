package com.jault.jplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jault.jplot.helpers.Constants;

public class PlotPanel extends JPanel {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2975690672805786359L;

	public PlotPanel() {
		setPreferredSize(new Dimension(Constants.GRID_WIDTH, Constants.GRID_HEIGHT));
		setBackground(Color.BLACK);
		setVisible(true);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				System.out.println("clicked at "+event.getX()+","+event.getY());
			}
		});
	}
	
	public void drawGridLines(int density, Graphics g) {
		int drawingWidth = (Constants.GRID_WIDTH/density)+Constants.GRID_WIDTH;
		int drawingHeight = (Constants.GRID_HEIGHT/density)+Constants.GRID_HEIGHT;
		
		g.setColor(Color.DARK_GRAY);
		
		for (int i=Constants.GRID_OFFSET; i<=drawingWidth;i+=(Constants.GRID_WIDTH/density)) g.drawLine(i, 0, i, drawingHeight);
		
		for (int i=(0-Constants.GRID_OFFSET); i<=drawingHeight;i+=(Constants.GRID_HEIGHT/density)) g.drawLine(0, i, drawingWidth, i);
	}
	
	public void drawPoint(Point p) {
		Graphics g = getGraphics();
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
		
		g.setColor(Color.CYAN);
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
		g.setColor(Color.CYAN);
		Point translated1 = translateToLocation(one);
		Point translated2 = translateToLocation(two);
		System.out.println("translated ("+one.x+","+one.y+") to ("+translated1.x+","+translated1.y+")");
		System.out.println("translated ("+two.x+","+two.y+") to ("+translated2.x+","+translated2.y+")");
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
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGridLines(Grid.getInstance().getGridDensity(), g);
		//loop that draws each point
		for (Point p : Grid.getInstance().getPoints()) drawPoint(p);
		
		//loop that draws the lines between each point
		for (int i=0;i<Grid.getInstance().getPoints().size();i++) {
			if (i+1<Grid.getInstance().getPoints().size()) {
				drawLine(g, Grid.getInstance().getPoints().get(i), Grid.getInstance().getPoints().get(i+1));
				//g.drawLine(Grid.getInstance().getPoints().get(i).x, Grid.getInstance().getPoints().get(i+1).x, Grid.getInstance().getPoints().get(i).y, Grid.getInstance().getPoints().get(i+1).y);
			}
		}
	}
}