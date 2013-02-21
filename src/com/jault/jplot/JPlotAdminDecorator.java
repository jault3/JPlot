/**
 * @author aultj
 * 
 * This concrete decorator adds menu items to list and create users for when an Administrator is logged in.
 */
package edu.msoe.se2800.h4.jplot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.msoe.se2800.h4.administrationFeatures.CreateUserUI;

public class JPlotAdminDecorator extends JPlotDecorator {

	public JPlotAdminDecorator(JPlotInterface jplot) {
		super(jplot);
	}

	/**
	 * Creates and adds the two new menus for administrators to create and list users
	 */
	@Override
	public void initSubviews() {
		jplot.initSubviews();
		JMenu jMenuAdmin = new JMenu();
        jMenuAdmin.setText("Administration");

        JMenuItem mnuCreateNew = new JMenuItem();
        mnuCreateNew.setText("Create User");
        mnuCreateNew.setActionCommand("create_user");
        mnuCreateNew.setName("create_user");
        mnuCreateNew.addActionListener(new MenuActionListener());

        JMenuItem mnuList = new JMenuItem();
        mnuList.setText("List Users");
        mnuList.setActionCommand("list_user");
        mnuList.setName("list_user");
        mnuList.addActionListener(new MenuActionListener());

        jMenuAdmin.add(mnuCreateNew);
        jMenuAdmin.add(mnuList);
        
		getFrame().getJMenuBar().add(jMenuAdmin);
	}
	
	/**
	 * Handles user clicks on the menu items to show the GUI to list/create a user
	 */
	public class MenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("create_user")) {
                CreateUserUI createUserUI = new CreateUserUI(JPlotController.getInstance());
                createUserUI.setVisible(true);
            } else if (e.getActionCommand().equals("list_user")) {
                JPlotController.getInstance().listUsers();
            }
        }

    }

}
