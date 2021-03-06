package worldwind;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.*;

import javax.swing.*;
import java.awt.*;

class SimpleWWJ extends JFrame {

    static Application instance;
    WorldWindowGLCanvas ww;

    SimpleWWJ(){
        super("WorldWind");
        instance = new Application("Simple World Wind App", "1.0", "Dodon Victor", "GPLv3", 2011);
        setSize(700, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(1, 1));


        ww = new WorldWindowGLCanvas();
        BasicModel model = new BasicModel();
        ww.setModel(model);

        addControlsLayer();
        setJMenuBar(new AppMenuBar(this, ww, instance));

        getContentPane().add(ww, BorderLayout.CENTER);
        setVisible(true);
    }

    public void addControlsLayer() {
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        insertBeforeCompass(viewControlsLayer);
        ww.addSelectListener(new ViewControlsSelectListener(ww, viewControlsLayer));
    }
    
    public void insertBeforeCompass(Layer layer) {
        int index = 0;
        LayerList layers = ww.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof CompassLayer) {
                index = layers.indexOf(l);
                break;
            }
        }
        layers.add(index,layer);
    }



    public static void main(String args[]){
        new SimpleWWJ();
    }
}