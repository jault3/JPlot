/**
 * @author aultj
 * 
 * This class is the overall JFrame that contains all other parts of the program such as the Grid,
 * Info Panel, Axis Panels, etc.
 */
package edu.msoe.se2800.h4.jplot;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import edu.msoe.se2800.h4.administrationFeatures.DatabaseConnection;
import edu.msoe.se2800.h4.jplot.grid.AdminGridDecorator;
import edu.msoe.se2800.h4.jplot.grid.GridInterface;
import edu.msoe.se2800.h4.jplot.grid.ImmediateGridDecorator;

public class JPlot extends JFrame implements JPlotInterface {

    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = -8344597455042452839L;
    
    private JMenuBar jMenuBar;

    /**
     * Initializes all sub components based on what mode we are in
     * @param mode one of OBSERVER, PROGRAMMER, ADMINISTRATOR, or OTHER
     * @param grid
     */
    public JPlot(DatabaseConnection.UserTypes mode, GridInterface grid) {
    	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	setResizable(false);
        setTitle("JPlot - " + mode);
        getContentPane().setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));

        if (mode != DatabaseConnection.UserTypes.OBSERVER) {
            Constants.INFO_PANEL_WIDTH = 150;
            if (mode == DatabaseConnection.UserTypes.ADMIN || mode == DatabaseConnection.UserTypes.PROGRAMMER) {
                grid = new AdminGridDecorator(grid);
            } else if (mode == DatabaseConnection.UserTypes.OTHER) {
                grid = new ImmediateGridDecorator(grid);
            }
        }

        grid.initSubviews();

        getContentPane().add(grid.getComponent());
        
        jMenuBar = new JMenuBar();

        pack();
        setVisible(true);
    }
    
    @Override
    public void initSubviews() {
    	
    	JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Log out");
        logoutItem.setName("logout");
        logoutItem.setActionCommand("logout");
        logoutItem.addActionListener(new MenuActionListener());
        fileMenu.add(logoutItem);
        jMenuBar.add(fileMenu);

        setJMenuBar(jMenuBar);
    }
    
    @Override
    public JFrame getFrame() {
        return this;
    }

    /**
     * The class to handle clicking the logging out menu item
     * @author aultj
     *
     */
    public class MenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        	if (e.getActionCommand().equalsIgnoreCase("logout")) {
	        	JPlotController.getInstance().logOut();
                JPlotController.getInstance().start(JPlotController.getInstance().robotController);
            }
        }

    }

}
