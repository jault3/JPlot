
package edu.msoe.se2800.h4.jplot;

import javax.inject.Inject;

import dagger.ObjectGraph;
import edu.msoe.se2800.h4.IRobotController;
import edu.msoe.se2800.h4.LejosModule;

/**
 * Entry point of the program
 * 
 * @author marius, scotta, aultj
 */
public class Main implements Runnable {

    @Inject
    public JPlotController mJPlotController;

    @Inject
    public IRobotController mRobotController;

    @Override
    public void run() {
        mJPlotController.start(mRobotController);
    }

    public static void main(String[] args) {
    	/** used for apple computers to move the JMenuBar to the very top of the screen to provide a consistent
    	 * experience with other programs **/
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", ".Scrumbot");

        // Setup dependency injection
        ObjectGraph objectGraph = ObjectGraph.create(new LejosModule());
        objectGraph.injectStatics();
        Main main = objectGraph.get(Main.class);
        main.run();
    }
}
