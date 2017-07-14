
import java.util.Arrays;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author M&M
 */
public class Building {

    String name;
    Coordinate coordinate;

    public Building(double[] coords, String name) {
        coordinate = new Coordinate(coords[1], coords[0]);
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Building)) {
            return false;
        }
        Building other = (Building) object;
        if ((this.coordinate == null && other.coordinate != null) || (this.coordinate != null && !this.coordinate.toString().equals(other.coordinate.toString()))) {
            return false;
        }
        return this.coordinate.toString().equals(other.coordinate.toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.coordinate.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        String ret = name;
        return ret;

    }
}