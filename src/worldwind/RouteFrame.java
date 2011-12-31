package worldwind;

import gov.nasa.worldwind.WorldWindow;

import javax.swing.*;

public class RouteFrame extends JFrame{

    final WorldWindow wwd;

    public RouteFrame(WorldWindow wwd) {
        super("Create a route");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.wwd = wwd;
    }
}
