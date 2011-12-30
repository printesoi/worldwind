package worldwind;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.globes.FlatGlobe;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import gov.nasa.worldwind.layers.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GlobeView extends JMenu{
    private WorldWindow wwd;
    private Globe roundGlobe;
    private FlatGlobe flatGlobe;

    private class PoliticalBoundaryItem extends JMenuItem implements  ActionListener {
        private boolean boundariesEnabled;
        CountryBoundariesLayer layer;
        
        public PoliticalBoundaryItem(WorldWindow ww) {
            super();
            
            for (Layer l : ww.getModel().getLayers()) {
                if (l instanceof CountryBoundariesLayer) {
                    this.layer = (CountryBoundariesLayer)l;
                    boundariesEnabled = l.isEnabled();
                    break;
                }
            }
            setItemText();
            addActionListener(this);
            
        }
        
        public void setItemText() {
            if (boundariesEnabled) {
                setText("Political Boundaries [Enabled]");
            } else {
                setText("Political Boundaries [Disabled]");
            }
        }
        public void actionPerformed(ActionEvent e) {
            boundariesEnabled = !boundariesEnabled;
            setItemText();
            layer.setEnabled(boundariesEnabled);
        }
    }

    public GlobeView(WorldWindow wwd) {
        
        super("View");
        
        this.wwd = wwd;
        if (isFlatGlobe())
        {
            this.flatGlobe = (FlatGlobe)wwd.getModel().getGlobe();
            this.roundGlobe = new Earth();
        }
        else
        {
            this.flatGlobe = new EarthFlat();
            this.roundGlobe = wwd.getModel().getGlobe();
        }
        
        this.makeMenu();
    }
    
    public JMenu makeMenu() {
        
        JMenu control = this;

        JMenuItem roundView = new JMenuItem("Round");
        roundView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFlatGlobe(false, null);
            }
        });

        JMenu flatView = new JMenu("Flat");
        JMenuItem mercator = new JMenuItem("Mercator");
        mercator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFlatGlobe(true, FlatGlobe.PROJECTION_MERCATOR);
                updateProjection(FlatGlobe.PROJECTION_MERCATOR);
            }
        });

        JMenuItem latLon = new JMenuItem("Lat-lon");
        latLon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFlatGlobe(true, FlatGlobe.PROJECTION_LAT_LON);
                updateProjection(FlatGlobe.PROJECTION_LAT_LON);
            }
        });

        JMenuItem modSin = new JMenuItem("Modified sin.");
        modSin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFlatGlobe(true, FlatGlobe.PROJECTION_MODIFIED_SINUSOIDAL);
                updateProjection(FlatGlobe.PROJECTION_MODIFIED_SINUSOIDAL);
            }
        });

        JMenuItem sinusoidal = new JMenuItem("Sinusoidal");
        sinusoidal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFlatGlobe(true, FlatGlobe.PROJECTION_SINUSOIDAL);
                updateProjection(FlatGlobe.PROJECTION_SINUSOIDAL);
            }
        });

        flatView.add(mercator);
        flatView.add(latLon);
        flatView.add(modSin);
        flatView.add(sinusoidal);
        control.add(roundView);
        control.add(flatView);
        control.add(new JSeparator());
        
        JMenuItem politicalBoundaries = new PoliticalBoundaryItem(wwd);
        control.add(politicalBoundaries);

        return control;
    }
    

    // Update flat globe projection
    private void updateProjection(String projection) {
        if (!isFlatGlobe())
            return;

        // Update flat globe projection
        this.flatGlobe.setProjection(projection);
        this.wwd.redraw();
    }

    public boolean isFlatGlobe() {
        return wwd.getModel().getGlobe() instanceof FlatGlobe;
    }

    public void enableFlatGlobe(boolean flat, String projection) {
        if(isFlatGlobe() == flat)
            return;

        if(!flat)
        {
            // Switch to round globe
            wwd.getModel().setGlobe(roundGlobe) ;
            // Switch to orbit view and update with current position
            FlatOrbitView flatOrbitView = (FlatOrbitView)wwd.getView();
            BasicOrbitView orbitView = new BasicOrbitView();
            orbitView.setCenterPosition(flatOrbitView.getCenterPosition());
            orbitView.setZoom(flatOrbitView.getZoom( ));
            orbitView.setHeading(flatOrbitView.getHeading());
            orbitView.setPitch(flatOrbitView.getPitch());
            wwd.setView(orbitView);
            // Change sky layer
            LayerList layers = wwd.getModel().getLayers();
            for(int i = 0; i < layers.size(); i++)
            {
                if(layers.get(i) instanceof SkyColorLayer)
                    layers.set(i, new SkyGradientLayer());
            }
        }
        else
        {
            // Switch to flat globe
            wwd.getModel().setGlobe(flatGlobe);
            flatGlobe.setProjection(projection);
            // Switch to flat view and update with current position
            BasicOrbitView orbitView = (BasicOrbitView)wwd.getView();
            FlatOrbitView flatOrbitView = new FlatOrbitView();
            flatOrbitView.setCenterPosition(orbitView.getCenterPosition());
            flatOrbitView.setZoom(orbitView.getZoom( ));
            flatOrbitView.setHeading(orbitView.getHeading());
            flatOrbitView.setPitch(orbitView.getPitch());
            wwd.setView(flatOrbitView);
            // Change sky layer
            LayerList layers = wwd.getModel().getLayers();
            for(int i = 0; i < layers.size(); i++)
            {
                if(layers.get(i) instanceof SkyGradientLayer)
                    layers.set(i, new SkyColorLayer());
            }
        }

        wwd.redraw();
    }
}
