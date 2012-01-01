package worldwind;

import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.WorldWindow;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RoutePointPanel extends JPanel implements ActionListener{

    private List<RoutePointEventListener> _listeners = new ArrayList<RoutePointEventListener>();
    private JTextField inputField;
    private JPanel resultsPanel;
    private JComboBox resultsBox;
    private LocationService service;
    private String command;
    private PointOfInterest locationPoint;
    
    public RoutePointPanel(String label) {
        super(new GridLayout(2, 1));
        setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), new TitledBorder(label)));
        service = new LocationService();
        command = label;
        
        JPanel tmpPanel = new JPanel(new FlowLayout());
        inputField = new JTextField(label);
        inputField.setPreferredSize(new Dimension(200, 20));
        inputField.addActionListener(this);
        tmpPanel.add(inputField, FlowLayout.LEFT);
        add(tmpPanel, FlowLayout.LEFT);


        resultsPanel = new JPanel(new FlowLayout());
        resultsPanel.add(new JLabel("Results: "));
        resultsBox = new JComboBox();
        resultsBox.addActionListener(this);
        resultsPanel.add(resultsBox);
        resultsPanel.setVisible(false);
        add(resultsPanel);

    }

    public void actionPerformed(ActionEvent event) {
        Object src = event.getSource();
        if (src instanceof JTextField) {
            makeResultsBox(((JTextField)src).getText());
        }
        
        if (src instanceof JComboBox) {
            setLocationPoint((PointOfInterest)((JComboBox) src).getSelectedItem());
            this.fireEvent();
        }
    }

    private void setLocationPoint(PointOfInterest point) {
        locationPoint = point;
    }

    public PointOfInterest getLocationPoint() {
        return locationPoint;
    }

    public void makeResultsBox(String lookupString) {
        if (lookupString == null || lookupString.length() < 1) {
            return;
        }

        resultsPanel.setVisible(false);
        List<PointOfInterest> poi = service.queryService(lookupString);

        if (poi != null) {
            if (poi.size() == 1) {
                setLocationPoint(poi.get(0));
                this.fireEvent();
            } else {
                resultsBox.removeAllItems();
                for (PointOfInterest p : poi) {
                    resultsBox.addItem(p);
                }
                resultsPanel.setVisible(true);
            }
        }
    }

    public JComboBox getResultsBox() {
        return resultsBox;
    }


    public synchronized void addEventListener(RoutePointEventListener listener) {
        _listeners.add(listener);
    }

    public synchronized void removeEventListener(RoutePointEventListener listener) {
        _listeners.remove(listener);
    }

    private synchronized void fireEvent() {
        RoutePointPanelLocationSelectedEvent event = new RoutePointPanelLocationSelectedEvent(this);
        event.setActionCommand(command);
        for(RoutePointEventListener r : _listeners) {
            r.handlePointSelectedEvent(event);
        }
    }
}
