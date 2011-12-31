package worldwind;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.exception.NoItemException;
import gov.nasa.worldwind.poi.PointOfInterest;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import java.util.List;

public class ZoomFrame extends JFrame {
    
    private final WorldWindow wwd;
    private JScrollPane resultsPane;
    private JList resultList;
    private LocationQuery locationQuery;

    class LocationNotFoundException extends Exception {
        public LocationNotFoundException() {
            super();
        }
    }
    
    public ZoomFrame(WorldWindow wwd) {
        super("Zoom location");
        this.wwd = wwd;
        this.locationQuery = new LocationQuery(wwd);
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
                            } catch (LocationNotFoundException e) {
                                JOptionPane.showMessageDialog(ZoomFrame.this, "Location \"" + (inputField.getText()
                                    != null ? inputField.getText() : "") + "\" not found!\n", "Lookup Failure",
                                    JOptionPane.ERROR_MESSAGE);
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
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        this.add(label, BorderLayout.WEST);
        this.add(inputPanel, BorderLayout.CENTER);

        resultList = new JList();
        resultsPane = new JScrollPane();

        pack();

        
    }

    private void findPlaces(String lookupString) throws IOException, ParserConfigurationException,
        XPathExpressionException, SAXException, NoItemException, IllegalArgumentException, LocationNotFoundException {
        resultsPane.setVisible(false);

        if (lookupString == null || lookupString.length() < 1) {
            return;
        }
        
        List<PointOfInterest> poi = locationQuery.queryService(lookupString);
        if (poi == null || poi.size() < 1) {
            throw new LocationNotFoundException();
        }
        
        if (poi.size() == 1) {
            locationQuery.moveToLocation(poi.get(0));
            locationQuery.placeMark(poi.get(0), true);
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
                            locationQuery.moveToLocation(poi);
                            locationQuery.placeMark(poi, true);
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
    
}
