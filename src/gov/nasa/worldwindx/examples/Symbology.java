/*
 * Copyright (C) 2011 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.SurfaceImage;
import gov.nasa.worldwind.symbology.TacticalGraphic;
import gov.nasa.worldwind.symbology.milstd1477.MilStd1477IconRetriever;
import gov.nasa.worldwind.symbology.milstd2525.*;
import gov.nasa.worldwind.util.WWUtil;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

/**
 * Demonstrates how to create and render symbols from the MIL-STD-2525 symbol set. See the <a title="Symbology Usage
 * Guide" href="http://goworldwind.org/developers-guide/symbology/" target="_blank">Usage Guide</a> for more information
 * on symbology support in World Wind.
 *
 * @author pabercrombie
 * @version $Id: Symbology.java 194 2011-11-23 19:29:21Z pabercrombie $
 */
public class Symbology extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            RenderableLayer symbolLayer = new RenderableLayer();
            symbolLayer.setName("Tactical Symbols");

            RenderableLayer lineLayer = new RenderableLayer();
            lineLayer.setName("Tactical Graphics (Lines)");

            RenderableLayer areaLayer = new RenderableLayer();
            areaLayer.setName("Tactical Graphics (Areas)");

            // Create tactical symbols and graphics and add them to the layer
            this.createSymbols(symbolLayer);
            this.createLineGraphics(lineLayer);
            this.createAreaGraphics(areaLayer);

            insertBeforePlacenames(getWwd(), symbolLayer);
            insertBeforePlacenames(getWwd(), lineLayer);
            insertBeforePlacenames(getWwd(), areaLayer);

            this.getLayerPanel().update(this.getWwd());

            // Size the World Window to provide enough screen space for the graphics, and center the World Window
            // on the screen.
            Dimension size = new Dimension(1200, 800);
            this.setPreferredSize(size);
            this.pack();
            WWUtil.alignComponent(null, this, AVKey.CENTER);
        }

        protected void createSymbols(RenderableLayer layer)
        {
            // Display a MIL-STD2525 tactical icon
            //      Warfighting
            String URL = "http://worldwindserver.net/milstd2525/";
            MilStd2525IconRetriever symGen = new MilStd2525IconRetriever(URL);
            AVListImpl params = new AVListImpl();
            BufferedImage img = symGen.createIcon("SUGPIXH---H----", params);
            //BufferedImage img = symGen.createIcon("SKGPUSTST------", params);
            Sector s = new Sector(Angle.fromDegrees(34.7), Angle.fromDegrees(34.8),
                Angle.fromDegrees(-117.7), Angle.fromDegrees(-117.57));
            SurfaceImage symbol = new SurfaceImage(img, s);
            layer.addRenderable(symbol);

            //      Signals Intelligence
            img = symGen.createIcon("IGAPSCO--------");
            s = new Sector(Angle.fromDegrees(34.7), Angle.fromDegrees(34.6),
                Angle.fromDegrees(-117.7), Angle.fromDegrees(-117.57));
            symbol = new SurfaceImage(img, s);
            layer.addRenderable(symbol);

            //      Stability Operations
            img = symGen.createIcon("OHOPYT---------");
            s = new Sector(Angle.fromDegrees(34.8), Angle.fromDegrees(34.7),
                Angle.fromDegrees(-117.55), Angle.fromDegrees(-117.42));
            symbol = new SurfaceImage(img, s);
            layer.addRenderable(symbol);

            //      Emergency Management
            img = symGen.createIcon("ESFPBB----H----");
            s = new Sector(Angle.fromDegrees(34.7), Angle.fromDegrees(34.6),
                Angle.fromDegrees(-117.9), Angle.fromDegrees(-117.77));
            symbol = new SurfaceImage(img, s);
            layer.addRenderable(symbol);

            // Display a MIL-STD1477 icon
            URL = "http://worldwindserver.net/milstd1477/";
            MilStd1477IconRetriever symGen1477 = new MilStd1477IconRetriever(URL);
            params = new AVListImpl();
            // use temporary test values: Storage_Location, Tree, Building, Church, Tower, Mountain, Bridge
            img = symGen1477.createIcon("Storage_Location", params);
            s = new Sector(Angle.fromDegrees(34.7), Angle.fromDegrees(34.8),
                Angle.fromDegrees(-117.9), Angle.fromDegrees(-117.77));
            symbol = new SurfaceImage(img, s);
            layer.addRenderable(symbol);
        }

        protected void createLineGraphics(RenderableLayer layer)
        {
            MilStd2525GraphicFactory factory = new MilStd2525GraphicFactory();
            MilStd2525TacticalGraphic graphic;

            /////////////////////////////////////////////
            // Phase line (2.X.2.1.2.4)
            /////////////////////////////////////////////

            // Create a Friendly Phase Line
            List<Position> positions = Arrays.asList(
                Position.fromDegrees(34.9349, -117.6303, 0),
                Position.fromDegrees(34.9843, -117.5885, 0),
                Position.fromDegrees(34.9961, -117.4891, 0));
            graphic = factory.createGraphic("GFGPGLP----AUSX", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Phase line: friendly");
            graphic.setText("A");
            layer.addRenderable(graphic);

            // Create a Hostile Phase Line
            positions = Arrays.asList(
                Position.fromDegrees(34.9190, -117.5919, 0),
                Position.fromDegrees(34.9589, -117.5618, 0),
                Position.fromDegrees(34.9701, -117.4798, 0));
            graphic = factory.createGraphic("GHGPGLP----AUSX", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Phase line: hostile");
            graphic.setText("B");
            layer.addRenderable(graphic);

            // Create a Hostile, Anticipated, Phase Line
            positions = Arrays.asList(
                Position.fromDegrees(34.8910, -117.5726, 0),
                Position.fromDegrees(34.9367, -117.5354, 0),
                Position.fromDegrees(34.9480, -117.4741, 0));
            graphic = factory.createGraphic("GHGAGLP----AUSX", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Phase line: hostile (anticipated)");
            graphic.setText("C");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Main Attack (2.X.2.5.2.1.4.1)
            /////////////////////////////////////////////

            positions = Arrays.asList(
                Position.fromDegrees(34.4643, -117.7323, 0), // Pt. 1: Tip of the arrow
                Position.fromDegrees(34.4962, -117.7808, 0), // Pt. 2: First path control point
                Position.fromDegrees(34.4934, -117.8444, 0), // Pt. N - 1: Last path control point
                Position.fromDegrees(34.4602, -117.8570, 0),
                Position.fromDegrees(34.4844, -117.7303, 0)); // Pt. N: Width of the arrow head
            graphic = factory.createGraphic("GFGPOLAGM-----X", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Main Attack");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Attack, rotary wing (2.X.2.5.2.1.3)
            /////////////////////////////////////////////

            positions = Arrays.asList(
                Position.fromDegrees(34.5610, -117.4541, 0),
                Position.fromDegrees(34.5614, -117.5852, 0),
                Position.fromDegrees(34.5287, -117.6363, 0),
                Position.fromDegrees(34.4726, -117.6363, 0),
                Position.fromDegrees(34.5820, -117.4700, 0));
            graphic = factory.createGraphic("GFGPOLAR------X", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Attack, Rotary Wing");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Aviation axis of advance (2.X.2.5.2.1.1)
            /////////////////////////////////////////////

            positions = Arrays.asList(
                Position.fromDegrees(34.5437, -117.8007, 0),
                Position.fromDegrees(34.5535, -117.9256, 0),
                Position.fromDegrees(34.6051, -117.9707, 0),
                Position.fromDegrees(34.5643, -117.8219, 0));
            graphic = factory.createGraphic("GFGPOLAV------X", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Aviation");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Supporting attack (2.X.2.5.2.1.4.2)
            /////////////////////////////////////////////

            positions = Arrays.asList(
                Position.fromDegrees(34.4980, -117.5541, 0),
                Position.fromDegrees(34.4951, -117.4667, 0),
                Position.fromDegrees(34.4733, -117.4303, 0),
                Position.fromDegrees(34.4217, -117.4056, 0),
                Position.fromDegrees(34.4780, -117.53, 0));
            graphic = factory.createGraphic("GFGPOLAGS-----X", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Supporting Attack");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Minimum risk route (2.X.2.2.2.2)
            /////////////////////////////////////////////

            // Create a Minimum Risk Route graphic
            // First create some Air Control Points to place along the route
            List<TacticalGraphic> controlPoints = new ArrayList<TacticalGraphic>();

            positions = Arrays.asList(
                Position.fromDegrees(34.8802, -117.9773, 0),
                Position.fromDegrees(35.0947, -117.9578, 0),
                Position.fromDegrees(35.1739, -117.6957, 0));

            // Create an Air Control Point
            graphic = factory.createGraphic("GFFPAPP-------X", positions.get(0), null);
            graphic.setText("1");
            controlPoints.add(graphic);

            // And another Air Control Point
            graphic = factory.createGraphic("GFFPAPP-------X", positions.get(1), null);
            graphic.setText("2");
            controlPoints.add(graphic);

            // And a Communication Checkpoint
            graphic = factory.createGraphic("GFFPAPC-------X", positions.get(2), null);
            graphic.setText("3");
            controlPoints.add(graphic);

            // Now create the route itself
            graphic = factory.createGraphic("GFGPALM-------X", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Minimum Risk Route");
            graphic.setText("KNIGHT");
            graphic.setModifier(AVKey.WIDTH, 2000.0);
            graphic.setModifier(AVKey.ALTITUDE, Arrays.asList("50 FT AGL", "200 FT AGL"));

            // Set the control points as child graphics of the route
            graphic.setModifier(AVKey.GRAPHIC, controlPoints);

            // And add the route to the layer. Note that we do not need to add the individual control points
            // to the layer because the route will take care of drawing them.
            layer.addRenderable(graphic);
        }

        protected void createAreaGraphics(RenderableLayer layer)
        {
            MilStd2525GraphicFactory factory = new MilStd2525GraphicFactory();
            MilStd2525TacticalGraphic graphic;

            /////////////////////////////////////////////
            // Assembly area (2.X.2.1.3.2)
            /////////////////////////////////////////////

            List<Position> positions = Arrays.asList(
                Position.fromDegrees(34.9130, -117.1897, 0),
                Position.fromDegrees(34.9789, -117.1368, 0),
                Position.fromDegrees(34.9706, -116.9900, 0),
                Position.fromDegrees(34.9188, -116.9906, 0));
            graphic = factory.createGraphic("GHGPGAA----AUSX", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Assembly Area");
            graphic.setText("Atlanta");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Airfield zone (2.X.2.1.3.11)
            /////////////////////////////////////////////

            positions = Arrays.asList(
                Position.fromDegrees(34.9001, -117.4044, 0),
                Position.fromDegrees(34.9910, -117.3297, 0),
                Position.fromDegrees(34.9851, -117.2224, 0),
                Position.fromDegrees(34.9134, -117.2670, 0));
            graphic = factory.createGraphic("GFGPGAZ----AUSX", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Airfield Zone");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Restricted Operation Zone (2.X.2.2.3.1)
            /////////////////////////////////////////////

            positions = Arrays.asList(
                Position.fromDegrees(35.1991, -117.6241, 0),
                Position.fromDegrees(35.2292, -117.4271, 0),
                Position.fromDegrees(35.1998, -117.2560, 0),
                Position.fromDegrees(35.0447, -117.2614, 0),
                Position.fromDegrees(35.0338, -117.6387, 0));
            graphic = factory.createGraphic("GFGPAAR----AUSX", positions, null);
            graphic.setValue(AVKey.DISPLAY_NAME, "Restricted Operations Zone (ROZ)");
            graphic.setText("(Unit ID)");
            graphic.setModifier(AVKey.DATE_TIME, Arrays.asList(new Date(), new Date()));
            graphic.setModifier(AVKey.ALTITUDE, Arrays.asList("100 FT AGL", "1000 FT AGL"));
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Circular target (2.X.4.3.1.2)
            /////////////////////////////////////////////

            Position position = (Position.fromDegrees(35.1108, -117.0470, 0));
            graphic = factory.createGraphic("GHFPATC-------X", position, null);
            graphic.setText("AG9999");
            graphic.setModifier(AVKey.RADIUS, 5000.0); // Radius of the circle
            graphic.setValue(AVKey.DISPLAY_NAME, "Circular Target");
            layer.addRenderable(graphic);

            /////////////////////////////////////////////
            // Rectangular target (2.X.4.3.1.1)
            /////////////////////////////////////////////

            position = (Position.fromDegrees(35.0295, -116.9290, 0));

            AVListImpl modifiers = new AVListImpl();
            // Length and width of the rectangle are specified as distance modifiers
            modifiers.setValue(AVKey.WIDTH, 4000.0);
            modifiers.setValue(AVKey.LENGTH, 8000.0);

            graphic = factory.createGraphic("GHFPATR-------X", position, modifiers);
            graphic.setText("AB0176");
            graphic.setValue(AVKey.DISPLAY_NAME, "Rectangular Target");
            layer.addRenderable(graphic);
        }
    }

    public static void main(String[] args)
    {
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 34.81);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -117.44);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 140000);

        ApplicationTemplate.start("World Wind Symbology", AppFrame.class);
    }
}
