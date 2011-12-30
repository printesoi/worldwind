package worldwind;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.poi.Gazetteer;
import gov.nasa.worldwind.poi.PointOfInterest;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.List;

public class ZoomFrame extends JFrame implements ActionListener{
    
    private final WorldWindow wwd;
    private Gazetteer gazetteer;
    private JScrollPane resultsPane;
    private JList resultList;
    
    public ZoomFrame(WorldWindow wwd) {
        super("Zoom location");
        this.wwd = wwd;
        this.gazetteer =  new gov.nasa.worldwind.poi.YahooGazetteer();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Zoom");
        final JTextField inputField = new JTextField("Enter location");
        inputField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                    EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                //handleEntryAction(e);
                                findPlaces(((JTextField) e.getSource()).getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(ZoomFrame.this,
                                    "Error looking up \"" + (inputField.getText() != null ? inputField.getText() :
                                        "") + "\"\n", "Lookup Failure", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
            }
        });
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        this.add(label, BorderLayout.WEST);
        this.add(inputPanel, BorderLayout.CENTER);

        resultList = new JList();
        resultsPane = new JScrollPane();

        pack();

        
    }

    public void actionPerformed(ActionEvent event) {

    }

    private void findPlaces(String lookupString) {
        resultsPane.setVisible(false);

        if (lookupString == null || lookupString.length() < 1) {
            return;
        }
        
        List<PointOfInterest> poi = queryService(lookupString);
        if (poi == null || poi.size() < 1) {
            return;
        }
        
        if (poi.size() == 1) {
            moveToLocation(poi.get(0));
            pack();
        } else {
            Vector<PointOfInterest> rList = new Vector<PointOfInterest>(poi);
            resultList.removeAll();
            resultList = new JList(rList);
            resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resultList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(final ListSelectionEvent e) {

                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JList list = (JList)e.getSource();
                            PointOfInterest poi = (PointOfInterest)list.getSelectedValue();
                            moveToLocation(poi);
                        }
                    });
                }
            });
            resultsPane = new JScrollPane(resultList);
            this.add(resultsPane, BorderLayout.SOUTH);
            resultsPane.setVisible(true);
            this.pack();
        }
    }
    
    private List<PointOfInterest> queryService(String lookupString) {
        List<PointOfInterest> results = this.gazetteer.findPlaces(lookupString);
        if (results == null || results.size() == 0) {
            return null;
        } else {
            return results;
        }
    }
    
    public void moveToLocation(PointOfInterest location) {
        this.wwd.getView().goTo(new Position(location.getLatlon(), 0), 25e3);
    }
}
