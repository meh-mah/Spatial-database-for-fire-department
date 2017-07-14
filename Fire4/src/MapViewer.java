
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author M&M
 */
public class MapViewer extends JMapViewer {

    //private BufferedImage image;
    ArrayList<Double> waterResources;
    ArrayList<Building> fireStations;
    ArrayList<Building> buildings;
    ArrayList<Double> underConstruct;
    Set<Building> onFire;
    Set<Building> wasOnFire;
    
//    int[] xCoords;
//    int[] yCoords;
    boolean resetMap;
    MapMarkerDot mmd;
    List<Coordinate> route;
    List<Coordinate> userPoly;
    Coordinate coord;

    public MapViewer() {
        this.waterResources = new ArrayList<>();
        this.fireStations = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.underConstruct= new ArrayList<>(); 
        this.onFire = new HashSet<>();
        this.wasOnFire = new HashSet<>();
        route = new ArrayList<>();
        userPoly = new ArrayList<>();
        
        //set the center of the map to stockholm
        this.setDisplayPositionByLatLon(59.327, 18.07, 12);

    }

    /*this method adds the clicked points on the map to the lat and lon arrays.
     * These points will be used to draw the polygon on the map. */
    public void addClickedPoint(double lat, double lon) {
        coord = new Coordinate(lat, lon);
        userPoly.add(coord);

    }

    // add the start point as the last point to complete the shape
    public void closePoly() {
        userPoly.add(userPoly.get(0));

    }

    // get the coordinations of the polygon drwan by the user. 
    public String getRangePolygon() {
        String vertices;
        vertices = userPoly.get(0).getLon() + "," + userPoly.get(0).getLat();
        for (int i = 1; i < userPoly.size(); i++) {
            vertices = vertices + ", " + userPoly.get(i).getLon() + ", " + userPoly.get(i).getLat();
        }


        return vertices;
    }

    // reset the lat and lon to make it ready to store new polygon vertices.
    public void removePoly() {
        userPoly.clear();

    }
    // removes all the drawn shapes and points on the map.

    public void resetMap() {
        waterResources.clear();
        underConstruct.clear();
        buildings.clear();
        onFire.clear();
        wasOnFire.clear();
        route.clear();
        fireStations.clear();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.addMapPolygon(new MapPolyLine(route));

//draw circle around each Water Resources point.
        for (int i = 0; i < (waterResources.size()); i = i + 2) {
            coord = new Coordinate(waterResources.get(i), waterResources.get(i + 1));
            mmd = new MapMarkerDot("", coord);
            mmd.setBackColor(Color.blue);
            this.addMapMarker(mmd);

        }
        
        //mark streets that are closed due to maintenance.
        for (int i = 0; i < (underConstruct.size()); i = i + 2) {
            coord = new Coordinate(underConstruct.get(i), underConstruct.get(i + 1));
            mmd = new MapMarkerDot("Blocked", coord);
            mmd.setBackColor(Color.YELLOW);
            this.addMapMarker(mmd);

        }

//draw a dot for each buildings
        for (int i = 0; i < buildings.size(); i++) {
            mmd = new MapMarkerDot(buildings.get(i).toString(), buildings.get(i).coordinate);
            mmd.setBackColor(Color.green);
            this.addMapMarker(mmd);

        }
        //draw a dot for each firestations
        for (int i = 0; i < fireStations.size(); i++) {
            mmd = new MapMarkerDot(fireStations.get(i).toString(), fireStations.get(i).coordinate);
            mmd.setBackColor(Color.PINK);
            this.addMapMarker(mmd);
        }

        for (Iterator<Building> it = onFire.iterator(); it.hasNext();) {
            
            Building b = it.next();
            mmd = new MapMarkerDot(b.toString(), b.coordinate);
            mmd.setBackColor(Color.red);
            this.addMapMarker(mmd);

        }
        
        for (Iterator<Building> it = wasOnFire.iterator(); it.hasNext();) {
            
            Building b = it.next();
            mmd = new MapMarkerDot(b.toString(), b.coordinate);
            mmd.setBackColor(Color.GREEN);
            this.addMapMarker(mmd);

        }
        
//draw polygon selected by user when clicking on the map(in case of "range" query)
        if (userPoly.size() > 0) {

            this.addMapPolygon(new MapPolyLine(userPoly));
        }

        if (resetMap) {
            resetMap();
        }
    }
}