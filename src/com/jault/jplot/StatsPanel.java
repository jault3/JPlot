
package edu.msoe.se2800.h4.jplot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import edu.msoe.se2800.h4.StatsUpdateEvent;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * GUI Panel that displays the battery and velocity information from the Robot
 * 
 * @author marius
 */
@SuppressWarnings("serial")
public class StatsPanel extends JPanel {

    private static final String sBatteryText = "% Battery remaining";
    private static final String sSpeedText = " Meters per second";

    private JLabel mSpeed;
    private JLabel mBattery;

    public StatsPanel() {
        setPreferredSize(new Dimension(Constants.GRID_WIDTH(), Constants.STATS_HEIGHT));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setVisible(true);
    }

    /**
     * Creates the components of the panel
     */
    public void initSubviews() {
        mSpeed = new JLabel();
        mBattery = new JLabel();

        mSpeed.setName("velocity_display");
        mBattery.setName("battery_display");

        add(mSpeed);
        add(mBattery);

        setSpeed(0);
        setBattery(100);
    }

    /**
     * Subscribes to the {@link EventBus} for {@link StatsUpdateEvent}s. Updates the display when an
     * event is received.
     * 
     * @param event distributed by the {@link EventBus} containing stats information
     */
    @Subscribe
    public void recordStatsChange(StatsUpdateEvent event) {
        setBattery(event.getmilliVoltsPercent());
        setSpeed(event.getVelocity());
        repaint();
    }

    private void setSpeed(double speed) {
        mSpeed.setText(Double.toString(speed) + sSpeedText);
    }

    private void setBattery(int milliVolts) {
        mBattery.setText(Integer.toString(milliVolts) + sBatteryText);
    }

}
