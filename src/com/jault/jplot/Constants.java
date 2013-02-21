/**
 * @author Josh Ault
 * 
 * Contains nearly all constants or final values used throughout the program. No actual logic methods are in here
 */
package edu.msoe.se2800.h4.jplot;

import edu.msoe.se2800.h4.administrationFeatures.DatabaseConnection;

public class Constants {

    /** These are dynamic values.  If anything in this class depends on these values, a getter
	 * should be implemented for those values.
	 */
	public static DatabaseConnection.UserTypes CURRENT_MODE = DatabaseConnection.UserTypes.OBSERVER;
	public static int INFO_PANEL_WIDTH = 0;//WINDOW_WIDTH-GRID_WIDTH-Y_AXIS_WIDTH;
	
	public static int DRAGGING_INDEX = -5;
	public static int HOVER_INDEX = -5;
	
	public static int ZOOM = 1;
	
	/**You may change these values**/
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH = 800;
	public static final int DEFAULT_GRID_DENSITY = 20;
	public static final int STEP_INCREMENT = 10;
	
	/** DO NOT CHANGE VALUES BELOW THIS LINE **/
	public static final int Y_AXIS_WIDTH = 50;
	public static final int X_AXIS_HEIGHT = 50;
	
	public static final int GRID_HEIGHT = WINDOW_HEIGHT-50;
	
	public static final int GRID_OFFSET = 10;
	
	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 2;
	
	public static final int POINT_RADIUS = 5;

    public static int GRID_WIDTH() {
        int GRID_WIDTH = WINDOW_WIDTH - INFO_PANEL_WIDTH - Y_AXIS_WIDTH;
		return GRID_WIDTH;
	}
    
    public static final int STATS_HEIGHT = 20;

}
