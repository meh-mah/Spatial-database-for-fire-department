
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author M&M
 */
public class DataSupplier {

    ConnectManager session;
    private int randomNoPeople;
    private int randomFloors;
    

    private DataSupplier() throws FileNotFoundException {
        session = new ConnectManager();

//empty tables to prevent duplication error
        truncateTables();

//insert each bulidings data and WaterResources data to the database form the building and WaterResources file
        feedData("buildingsNode.txt", "hydrantNodes.txt", "firestations.txt", "streetNodes.txt", "links.txt");


//Update the building table to indicate the buldings on fire. read data from onfire file
//        onFire("onfire");

        session.close();

        System.out.println("Database updated Successfully");
    }
    //to avoid duplication error, call this method to empty the tables in DB, before feeding the data from files. 

    private void truncateTables() {
        String query = "TRUNCATE TABLE WaterResources";
        session.execute(query);
        query = "TRUNCATE TABLE fire_history";
        session.execute(query);
        query = "DELETE  FROM Buildings";
        session.execute(query);
        query = "DELETE  FROM firestations";
        session.execute(query);
        query = "TRUNCATE TABLE Fire_node$";
        session.execute(query);
        query = "TRUNCATE TABLE Fire_LINK$";
        session.execute(query);
    }

    private void feedData(String building_file, String WaterResources_file, String FS_file, String streetNodes_file, String links_file) {
        try (Scanner read = new Scanner(new FileInputStream(building_file))) {
            String insertQuery;
            while (read.hasNext()) {
//insert Buildings data

                String[] data = read.nextLine().split(", ");
                randomFloors=randomFloor();
                randomNoPeople=randomResidents();
                insertQuery =
                        "INSERT INTO Buildings VALUES ('"
                        + data[0] + "','"
                        + data[1] + "','"
                        + data[2] + "',"
                        + randomNoPeople + ","
                        + randomFloors 
                        + ",SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE("
                        + data[4] + ","
                        + data[3] + ","
                        + "NULL),"
                        + "NULL, NULL))";

                session.execute(insertQuery);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (Scanner read = new Scanner(new FileInputStream(WaterResources_file))) {
            while (read.hasNext()) {
//insert WaterResources data

                String[] data2 = read.nextLine().split(", ");
                String insertSQL =
                        "INSERT INTO WaterResources VALUES ('"
                        + data2[0] + "','"
                        + data2[1] + "'"
                        + ",SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE("
                        + data2[3] + ","
                        + data2[2] + ","
                        + "NULL),"
                        + "NULL, NULL))";
                session.execute(insertSQL);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         try (Scanner read = new Scanner(new FileInputStream(FS_file))) {
            while (read.hasNext()) {
//insert firestation data

                String[] data2 = read.nextLine().split(", ");
                String insertSQL =
                        "INSERT INTO firestations VALUES ('"
                        + data2[0] + "','"
                        + data2[1] + "'"
                        + ",SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE("
                        + data2[3] + ","
                        + data2[2] + ","
                        + "NULL),"
                        + "NULL, NULL))";
                session.execute(insertSQL);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Scanner read = new Scanner(new FileInputStream(streetNodes_file))) {
            String insertQuery;
            while (read.hasNext()) {
//insert nodes data

                String[] data = read.nextLine().split(", ");
                insertQuery =
                        "INSERT INTO Fire_NODE$ VALUES ('"
                        + data[0] + "','"
                        + data[1] + "','"
                        + data[2] + "','Y','1'"
                        + ",SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE("
                        + data[4] + ","
                        + data[3] + ","
                        + "NULL),"
                        + "NULL, NULL))";

                session.execute(insertQuery);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (Scanner read = new Scanner(new FileInputStream(links_file))) {
            String insertQuery;
            while (read.hasNext()) {
//insert link data

                String[] data = read.nextLine().split(", ");
                insertQuery =
                        "INSERT INTO Fire_link$ VALUES ('"
                        + data[0] + "','"
                        + data[1] + "','"
                        + data[2] + "','"
                        + data[3] + "','street','Y','1'"
                        + ",SDO_GEOMETRY(2002,4326,NULL,SDO_ELEM_INFO_ARRAY(1,2,1),"
                        + "SDO_ORDINATE_ARRAY("+data[5]+","+data[4]+","+data[7]+","+data[6]+")),"
                        + " SDO_GEOM.SDO_LENGTH("
                        + "SDO_GEOMETRY("
                        + "2002,4326,NULL,SDO_ELEM_INFO_ARRAY(1,2,1),"
                        + "SDO_ORDINATE_ARRAY("+data[5]+","+data[4]+","+data[7]+","+data[6]+"))"
                        + ",0.005, 'unit=KM'))";

                session.execute(insertQuery);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onFire(String onfire_file) {
        try (Scanner read = new Scanner(new FileInputStream(onfire_file))) {
            while (read.hasNext()) {
//update the buliding table to include buildings on fire
                String updateQuery = "UPDATE Buildings SET onFire = '1' WHERE building_name = '" + read.nextLine() + "'";
                session.execute(updateQuery);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataSupplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   private int randomResidents (){
    int noOfResidents= 5 + (int)(Math.random() * ((20 - 5) + 1));
        return noOfResidents;
   }
   
   private int randomFloor (){
    int floors= 2 + (int)(Math.random() * ((6 - 2) + 1));
        return floors;
   }
    public static void main(String args[]) throws FileNotFoundException {
        DataSupplier ds = new DataSupplier();
        System.exit(0);
    }
}