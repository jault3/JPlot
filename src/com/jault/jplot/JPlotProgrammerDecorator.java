/**
 * @author aultj
 * 
 * Concrete decorator that adds menu items for switching between immediate mode and normal mode
 */
package edu.msoe.se2800.h4.jplot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.msoe.se2800.h4.administrationFeatures.DatabaseConnection;

public class JPlotProgrammerDecorator extends JPlotDecorator {

	public JPlotProgrammerDecorator(JPlotInterface jplot) {
		super(jplot);
	}

	/**
	 * Creates and adds menu items to the JMenuBar
	 */
	@Override
	public void initSubviews() {
		jplot.initSubviews();
		JMenu jMenuMode = new JMenu();
        jMenuMode.setText("Operating Mode");

        JMenuItem mnuImmediate = new JMenuItem();
        mnuImmediate.setText("Immediate Mode");
        mnuImmediate.setActionCommand("immediate");
        mnuImmediate.setName("immediate_mode");
        mnuImmediate.addActionListener(new MenuActionListener());

        JMenuItem mnuAdministrator = new JMenuItem();
        mnuAdministrator.setText("Administrator Mode");
        mnuAdministrator.setActionCommand("administrator");
        mnuAdministrator.setName("administrator_mode");
        mnuAdministrator.addActionListener(new MenuActionListener());

        jMenuMode.add(mnuImmediate);
        jMenuMode.add(mnuAdministrator);
        
        getFrame().getJMenuBar().add(jMenuMode);
	}
	
	/**
	 * This class handles user clicking to change to immediate mode and back
	 */
	public class MenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("immediate")) {
                JPlotController.getInstance().changeMode(DatabaseConnection.UserTypes.OTHER);
            } else if (e.getActionCommand().equalsIgnoreCase("administrator")) {
                try {
					JPlotController.getInstance().changeMode(DatabaseConnection.getInstance().getUserRole(DatabaseConnection.getInstance().getLastSuccessfullyValidatedUser()));
				} catch (IOException e1) {
					System.out.println("Could not switch out of immediate mode");
					e1.printStackTrace();
				}
            }
        }

    }

}
