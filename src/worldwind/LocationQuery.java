package worldwind;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.render.PointPlacemark;

import java.util.Vector;

public class LocationQuery {
    private WorldWindow wwd;
    private RenderableLayer renderableLayer;
    private Vector<PointPlacemark> marks;

    public LocationQuery(WorldWindow wwd) {
        this.wwd = wwd;
        this.renderableLayer = new RenderableLayer();
        this.marks = new Vector<PointPlacemark>();
    }
    
    public void moveToLocation(PointOfInterest location) {
        this.wwd.getView().goTo(new Position(location.getLatlon(), 0), 25e3);
    }

    public void placeMark(PointOfInterest location, boolean removePreviousMarks) {
        LayerList layers = wwd.getModel().getLayers();
        int index = 0;

        if (removePreviousMarks) {
            if(renderableLayer != null) {
                layers.remove(renderableLayer);
                renderableLayer = null;
            }
            if (marks != null) {
                marks = null;
            }
        }

        if (renderableLayer == null) {
            renderableLayer = new RenderableLayer();
        }
        
        if (marks == null) {
            marks = new Vector<PointPlacemark>();
        }

        PointPlacemark newPoint = new PointPlacemark(new Position(location.getLatlon(), 0));
        marks.add(newPoint);
        newPoint.setLabelText(location.toString());
        for (PointPlacemark p : marks) {
            renderableLayer.addRenderable(p);
        }
        for (Layer l : layers) {
            if (l instanceof CompassLayer) {
                index = layers.indexOf(l);
                break;
            }
        }
        layers.add(index, renderableLayer);
    }
}
