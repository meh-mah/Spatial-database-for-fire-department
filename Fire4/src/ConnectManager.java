
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.spatial.network.lod.LODNetworkException;
import oracle.spatial.network.lod.LODNetworkManager;
import oracle.spatial.network.lod.NetworkAnalyst;
import oracle.spatial.network.lod.NetworkIO;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author M&M
 */
public class ConnectManager {

    private Connection dbSession;
    Statement statement;
    NetworkIO networkIo;
    NetworkAnalyst analyst;

    public ConnectManager() {
        try {

            //connect to DB
            System.out.println("trying to connect to DB...");
            dbSession = LODNetworkManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL2", "system", "mehran");
             statement = dbSession.createStatement();

            networkIo = LODNetworkManager.getCachedNetworkIO(dbSession, "Fire", "Fire", null);
            analyst = LODNetworkManager.getNetworkAnalyst(networkIo);
            //register database driver
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            System.out.println("Driver registered");

            
            //dbSession = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL2", "system", "mehran");
            System.out.println("connection to database established");
        } catch (SQLException | LODNetworkException ex) {
            Logger.getLogger(ConnectManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //execute given sql query
    public void execute(String query) {
        try {
            statement = this.dbSession.createStatement();
            statement.executeQuery(query);
            this.dbSession.commit();
            statement.close();

        } catch (SQLException ex) {
            Logger.getLogger(ConnectManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //fetch and return requierd data from database according to the given sql query
    public ResultSet fetchData(String query) throws SQLException {
        statement = this.dbSession.createStatement();
        ResultSet result = statement.executeQuery(query);
        
        return result;
    }

    //close the connection to DB
    public void close() {
        try {
            dbSession.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}