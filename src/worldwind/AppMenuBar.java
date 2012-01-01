package worldwind;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gov.nasa.worldwind.WorldWindow;

public class AppMenuBar extends JMenuBar implements ActionListener{

    private Application instance;
    private JFrame owner;
    private final WorldWindow wwd;
    ZoomFrame zoomFrame;

    public AppMenuBar(JFrame owner, WorldWindow wwd, Application instance) {
        super();
        this.instance = instance;
        this.owner = owner;
        this.wwd = wwd;

        JMenu optionsMenu = new JMenu("Options");
        JMenuItem zoomLocation = new JMenuItem("Go to location");
        JMenuItem route = new JMenuItem("Create a route");
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(this);
        zoomLocation.addActionListener(this);
        route.addActionListener(this);
        optionsMenu.add(zoomLocation);
        optionsMenu.add(route);
        optionsMenu.add(new JSeparator());
        optionsMenu.add(quit);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(this);
        helpMenu.add(about);
        
        add(optionsMenu);
        add(new GlobeView(wwd));
        add(helpMenu);
    }

    public void actionPerformed(ActionEvent event) {
        String src = event.getActionCommand();
        if (src.equals("Quit")) {
            Object [] options = { "Yes", "Cancel"};
            int result  = JOptionPane.showOptionDialog(this, "Are you sure you want to quit?", "Confirm exit",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == 0) {
                System.exit(0);
            }
            return;
        }
        
        if (src.equals("About")) {
            new AboutWindow(owner, instance);
            return;
        }
        
        if (src.equals("Go to location")) {
            if (zoomFrame == null)
                zoomFrame = new ZoomFrame(wwd);
            zoomFrame.setVisible(true);
            return;
        }
        
        if (src.equals("Create a route")) {
            RouteFrame rf = new RouteFrame(wwd);
            rf.setVisible(true);
        }
    }
}
