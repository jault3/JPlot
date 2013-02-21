package edu.msoe.se2800.h4.jplot;

import com.google.common.eventbus.EventBus;
import edu.msoe.se2800.h4.FileIO;
import edu.msoe.se2800.h4.IRobotController;
import edu.msoe.se2800.h4.Logger;
import edu.msoe.se2800.h4.StatsTimerDaemon;
import edu.msoe.se2800.h4.administrationFeatures.DatabaseConnection;
import edu.msoe.se2800.h4.administrationFeatures.LoginUI;
import edu.msoe.se2800.h4.administrationFeatures.ResultInfo;
import edu.msoe.se2800.h4.administrationFeatures.UserListController;
import edu.msoe.se2800.h4.jplot.grid.Grid;
import edu.msoe.se2800.h4.jplot.grid.GridInterface;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This is the main class that controls all other components of the program.  It is in charge of
 * keeping the JPlot and Grid components, wrapping them in their respective decorators, changing the
 * viewing mode of the program, and because it is a singleton , it provides methods to all of its sub components
 * to add points, remove points, zoom in/out, and  control the creation and showing of new windows for 
 * creating users and listing users.
 * 
 * @author aultj, suckowm, scotta, koenigj, tohtzk, volkhartm
 *
 */
@Singleton
public class JPlotController {
	/** Singleton object **/
    private static JPlotController instance = null;

    /** The density is the number of grid lines that should be shown horizontally and vertically on the screen **/
    private int gridDensity = Constants.DEFAULT_GRID_DENSITY;

    /** JPlot and Grid components that make up the entire GUI **/
    private JPlotInterface jplot;
    private GridInterface grid;
    
    /** This is used in immediate mode to save all the previous points so they arent lost **/
    private List<Waypoint> oldList;
    
    /** The point that should be drawn orange during a redraw **/
    private Waypoint highlightedPoint;
    private boolean closingForModeChange = false;
    
    /** Events such as the battery or speed update events are posted to this bus **/
    private EventBus mEventBus;
    
    /** The interface that controls the robot movements **/
    protected IRobotController robotController;
    
    /** The current user username */
    private String currentUser = "";
    
    LoginUI login;

    /**
     * The singleton instance method
     * @return the singleton
     */
    public static JPlotController getInstance() {
        if (instance == null) {
            synchronized (JPlotController.class) {
                if (instance == null) {
                    instance = new JPlotController();
                }
            }
        }
        return instance;
    }

    public JPlotController() {
        oldList = new ArrayList<Waypoint>();
        instance = this;
    }

    /**
     * Initializes the grid and jplot as well as adding a window listener to logout instead of closing the program
     */
    public void init() {
        grid = new Grid();
        jplot = new JPlot(DatabaseConnection.UserTypes.OBSERVER, grid);
        jplot.initSubviews();
        jplot.getFrame().addWindowListener(new JPlotWindowListener());
        StatsTimerDaemon.start();
    }

    public GridInterface getGrid() {
        return grid;
    }

    /**
     * This method changes the current running mode between Observer, Immediate, Programmer, or Administrator.
     * This is called when a user logs in in order to initialize the Grid and JPlot and wrap them when needed. 
     * If changing from immediate mode, the points from the old list are added back into the new list
     * @param accessLevel
     */
    public void changeMode(DatabaseConnection.UserTypes accessLevel) {
        DatabaseConnection.UserTypes mode = accessLevel;
        grid = new Grid();
        if (Constants.CURRENT_MODE == DatabaseConnection.UserTypes.OTHER) {
            robotController.setPath(new Path());

            for (Waypoint p : oldList) {
                robotController.addWaypoint(p);
            }
        }
        Constants.CURRENT_MODE = mode;
        if (mode == DatabaseConnection.UserTypes.OTHER) {
            copyPoints();
            robotController.setPath(new Path());
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                closingForModeChange = true;
                jplot.getFrame().dispose();
                jplot = new JPlot(Constants.CURRENT_MODE, grid);
                if (Constants.CURRENT_MODE != DatabaseConnection.UserTypes.OBSERVER) {
                    jplot = new JPlotProgrammerDecorator(jplot);
                    if (Constants.CURRENT_MODE == DatabaseConnection.UserTypes.ADMIN) {
                        jplot = new JPlotAdminDecorator(jplot);
                    }
                }
                jplot.initSubviews();
                closingForModeChange = false;
                jplot.getFrame().addWindowListener(new JPlotWindowListener());
            }
        });
    }

    public Path getPath() {
        return robotController.getPath();
    }
    
    /**
     * Helper method that returns an array of waypoints instead of just the path itself
     * @return
     */
    public Waypoint[] getPathPoints() {
        Waypoint[] points = new Waypoint[robotController.getPath().size()];
        robotController.getPath().toArray(points);
        return points;
    }
    
    /**
     * Adds a waypoint to the list as well as tell the robot to immediately go to that point if in 
     * immediate mode
     * @param point
     */
    public void addPoint(Waypoint point) {
        if (Constants.CURRENT_MODE != DatabaseConnection.UserTypes.OTHER) {
            robotController.addWaypoint(point);

        }else{
        	robotController.addWaypoint(point);
            robotController.goToImmediate(point);
        }
        if (jplot != null) {
            jplot.getFrame().repaint();
        }
    }

    public void removePoint(int indexOfPoint) {
        robotController.getPath().remove(indexOfPoint);
        jplot.getFrame().repaint();
    }

    /**
     * Helper method removes all points from the current path and saves them in the old list to be added
     * back in after immediate mode is over
     */
    public void copyPoints() {
        oldList.clear();
        for (Waypoint j : robotController.getPath()) {
            oldList.add(j);
        }
        robotController.setPath(new Path());
        grid.redraw();
    }

    /**
     * decrements the grid density to show fewer grid lines giving a zoom in effect
     */
    public void zoomIn() {
        setGridDensity(getGridDensity() - 1);
        grid.redraw();
    }

    /**
     * increments the grid density to show more grid lines giving a zoom out effect
     */
    public void zoomOut() {
        setGridDensity(getGridDensity() + 1);
        grid.redraw();
    }

    public int getGridDensity() {
        return gridDensity;
    }

    /**
     * sets the grid density to the passed in value or a minimum of 1 and a maximum of 100
     * @param density
     */
    public void setGridDensity(int density) {
        if (density > 1) {
            gridDensity = Math.min(density, 100);
            grid.redraw();
        } else {
            gridDensity = 1;
            grid.redraw();
        }
    }

    public Waypoint getHighlightedPoint() {
        return this.highlightedPoint;
    }

    /**
     * helper method to set the highlighted point based upon the index in the array of points previously set
     * in another method
     * @param indexInPointsArray
     */
    public void setHighlightedPoint(int indexInPointsArray) {
        if (indexInPointsArray == -5) {
            this.highlightedPoint = null;
        } else {
            this.highlightedPoint = JPlotController.getInstance().getPath().get(indexInPointsArray);
        }
    }

    /**
     * This method creates a user in the database with the given username, password,and role
     * @param username
     * @param password
     * @param role, one of Programmer, Observer, or Administrator
     * @return
     */
    public ResultInfo createUser(String username, String password, DatabaseConnection.UserTypes role) {
        try {
            return DatabaseConnection.getInstance().addUser(username, password, role);
        } catch (IOException e) {
            return new ResultInfo("Error contacting the database.", false);
        }
    }

    /**
     * Shows the GUI for listing all users, this is also where you change user roles / passwords
     */
    public void listUsers() {
        new UserListController();
    }

    /**
     * Window listener to log out the current user instead of closing the program when a quit command is issued
     * or if the user clicks on the close button.
     *
     */
    private class JPlotWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            //if closing because of mode change, do not want to log out
            if (!closingForModeChange) {
                JPlotController.this.logOut();
            } else {
                jplot.getFrame().dispose();
            }
        }
    }

    /**
     * launches the login screen
     *
     * @param rc the robotController to be used in this run
     */
    public void start(IRobotController rc) {
        checkNotNull(rc);


        this.robotController = rc;
        //create a dummy frame so the dialog shows up in the taskbar
        JFrame dummyFrame = new JFrame();
        dummyFrame.setUndecorated(true);
        dummyFrame.setVisible(true);
        login = new LoginUI(dummyFrame);
        dummyFrame.dispose();
        //if the login was successful, start the program. otherwise a cancel occured and exit the program
        if (login.wasLoginSuccessful()) {
            //log the login to the logger
            Logger.INSTANCE.log(this.getClass().getSimpleName(),
                    "Logged in as: " + DatabaseConnection.getInstance().getLastSuccessfullyValidatedUser());
            //set the current user
            this.currentUser = DatabaseConnection.getInstance().getLastSuccessfullyValidatedUser();
            //initialize
            this.init();
            try {
                //change the access mode to the mode of the user
                this.changeMode(DatabaseConnection.getInstance().getUserRole(login.getUsername()));
            } catch (IOException e) {
                System.out.println("Unable to retrieve user role and set grid mode");
            }
        } else {
            //log in cancelled. stop program execution
            System.exit(0);
        }
    }

    /**
     * logs out. makes sure robot is not running. Asks to save current path before logging out. relaunches the login screen.
     */
    public void logOut() {
        if (jplot == null) {
            throw new NullPointerException("Tried to log out when jplot was null");
        }

        //if the robot is moving, don't allow a log out
        if (!this.robotController.isMoving()) {
            try {
                //observers can't save so don't show the prompt if the current user is an observer
                if (DatabaseConnection.getInstance().getUserRole(this.currentUser) != DatabaseConnection.UserTypes.OBSERVER) {
                    if (FileIO.getCurrentPathFile() != null || !JPlotController.this.getPath().isEmpty()) {
                        int result = JOptionPane
                                .showConfirmDialog(null, "Do you wish to save your current Path?", "Save...?",
                                        JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            FileIO.save();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //close the frame
            this.jplot.getFrame().dispose();
            //this.currentUser = "";
            //log the logout to the logger
            Logger.INSTANCE.log(this.getClass().getSimpleName(),
                    "Logged out of: " + DatabaseConnection.getInstance().getLastSuccessfullyValidatedUser());

        } else {
            JOptionPane.showMessageDialog(null, "Robot is running. Cannot log out.");
        }



    }

    /**
     * Get the event bus that is used for posting battery/speed updates
     * @return
     */
    public EventBus getEventBus() {
        if (mEventBus == null) {
            synchronized (this) {
                mEventBus = new EventBus();
            }
        }
        return mEventBus;
    }

    public IRobotController getRobotController() {
        return robotController;
    }

}
