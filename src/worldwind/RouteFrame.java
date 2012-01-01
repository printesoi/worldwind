package worldwind;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.render.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class RouteFrame extends JFrame implements ActionListener, RoutePointEventListener, ListSelectionListener{
    
    private LocationQuery locationQuery;
    private PointOfInterest startPoint = null;
    private PointOfInterest endPoint = null;
    private Vector<PointOfInterest> via = new Vector<PointOfInterest>();
    private DefaultListModel listModel = new DefaultListModel();
    private JList viaList = new JList(listModel);
    private JPanel newVia = new JPanel(new BorderLayout());
    private RoutePointPanel inputVia = new RoutePointPanel("Via");
    private RenderableLayer layer = new RenderableLayer();
    private Polyline line;
    private Vector<LatLon> locations = null;
    private WorldWindow wwd;

    public RouteFrame(WorldWindow wwd) {
        super("Create a route");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout(1, 1));

        locationQuery = new LocationQuery(wwd);
        this.wwd = wwd;

        JPanel topContainer = new JPanel(new BorderLayout());
        JPanel tmpPanel = new JPanel(new FlowLayout());
        RoutePointPanel startPanel = new RoutePointPanel("Start point");
        startPanel.addEventListener(this);
        tmpPanel.add(startPanel, FlowLayout.LEFT);

        RoutePointPanel destPanel = new RoutePointPanel("End point");
        destPanel.addEventListener(this);
        tmpPanel.add(destPanel);

        JPanel locationActions = new JPanel(new FlowLayout());
        JButton addButton = new JButton(new ImageIcon("img/add.png"));
        addButton.setPreferredSize(new Dimension(16, 16));
        addButton.setActionCommand("newViaLocation");
        addButton.addActionListener(this);
        
        JButton removeButton = new JButton(new ImageIcon("img/remove.png"));
        removeButton.setPreferredSize(new Dimension(16,16));
        removeButton.setActionCommand("removeLocation");
        //removeButton.setEnabled(false);
        removeButton.addActionListener(this);
        locationActions.add(addButton, FlowLayout.LEFT);
        locationActions.add(removeButton);
        
        topContainer.add(tmpPanel, BorderLayout.CENTER);
        topContainer.add(locationActions, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);


        viaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        viaList.addListSelectionListener(this);
        viaList.setVisible(false);
        add(viaList, BorderLayout.CENTER);

        inputVia.addEventListener(this);
        newVia.add(inputVia,BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton doneViaButton = new JButton("Add this location!");
        doneViaButton.setActionCommand("addLocationToViaList");
        doneViaButton.addActionListener(this);
        
        JButton cancelViaButton = new JButton("Cancel");
        cancelViaButton.setActionCommand("cancelAddingLocation");
        cancelViaButton.addActionListener(this);
        
        buttonsPanel.add(doneViaButton, FlowLayout.LEFT);
        buttonsPanel.add(cancelViaButton);
        
        newVia.add(buttonsPanel, BorderLayout.SOUTH); 
        newVia.setVisible(false);
        add(newVia, BorderLayout.SOUTH);

        newVia.setVisible(false);

        pack();
    }
    
    public void handlePointSelectedEvent(RoutePointPanelLocationSelectedEvent event) {
        RoutePointPanel src = (RoutePointPanel)event.getSource();
        if (event.getActionCommand().equals("Start point")) {
            startPoint = src.getLocationPoint();
        }
        
        if (event.getActionCommand().equals("End point")) {
            endPoint = src.getLocationPoint();
        }
        
        if (event.getActionCommand().equals("Via")) {
            
        }
        locationQuery.moveToLocation(src.getLocationPoint());
        renderLine();
    }
    
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton src = (JButton)event.getSource();
            if (src.getActionCommand().equals("newViaLocation")) {
                if (startPoint != null && endPoint != null){
                    newVia.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "You must have a start point and a end point!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                pack();
            }
            if (src.getActionCommand().equals("addLocationToViaList")) {
                PointOfInterest poi = inputVia.getLocationPoint();
                if (!(via.contains(poi))) {
                    via.addElement(poi);
                    listModel.addElement(poi);
                }
                viaList.setVisible(true);
                pack();
            }
            if (src.getActionCommand().equals("cancelAddingLocation")) {
                newVia.setVisible(false);
                pack();
            }
            if (src.getActionCommand().equals("removeLocation")) {
                int index;
                if (viaList.getModel().getSize() == 0) {
                    System.out.println("No items");
                } else {
                    ListSelectionModel model = viaList.getSelectionModel();
                    index = model.getMinSelectionIndex();
                    removeLocation(index);
                }
            }
            renderLine();
        }
    }

    private void removeLocation(int index) {
        if (listModel.getSize() == 0) {
            return;
        }

        listModel.remove(index);
        via.remove(index);
        index--;
        viaList.setSelectedIndex(index);
        viaList.ensureIndexIsVisible(index);
        System.out.println(via);
        pack();
    }
    
    public void valueChanged(ListSelectionEvent event) {
        ListSelectionModel model = ((JList)event.getSource()).getSelectionModel();
        System.out.println(model.getMinSelectionIndex());
    }
    
    public void renderLine() {
        if (startPoint == null || endPoint == null) {
            return;
        }
        layer = null;
        layer = new RenderableLayer();
        locations = null;
        locations = new Vector<LatLon>();
        locations.add(startPoint.getLatlon());        
        for(PointOfInterest p : via) {
            locations.add(p.getLatlon());
        }
        locations.add(endPoint.getLatlon());
        line = new Polyline(locations, 0);
        insertBeforeCompass(layer);
        
    }

    public void insertBeforeCompass(Layer layer) {
        int index = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof CompassLayer) {
                index = layers.indexOf(l);
                break;
            }
        }
        layers.add(index,layer);
    }
}
