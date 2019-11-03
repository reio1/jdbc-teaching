/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.reio.jdbc.first;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diese Klasse zeigt wie man sich mit einer Datenbank verbindet
 * Außerdem werden Testdaten in eine Tabelle geschrieben
 * @author reio
 */
public class FirstSteps {
    public static void main(String[] args) {
        final String DATABASE = "reio_first_steps";
        
        String ddl = "";
        ddl += "DROP TABLE IF EXISTS persons;\n";
        ddl += "CREATE TABLE persons (\n";
        ddl += "   per_id INTEGER NOT NULL,\n";
        ddl += "   per_lastname VARCHAR(100),\n";
        ddl += "   per_firstname VARCHAR(100), \n";
        ddl += "   CONSTRAINT fk_persons PRIMARY KEY(per_id)\n";
        ddl += ");";
        
        try {
            // Treiber laden
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FirstSteps.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        String jdbcurl = "jdbc:postgresql://10.128.6.5:5432/" + DATABASE;
        
        try(Connection con = DriverManager.getConnection(jdbcurl, "unterricht", "unterricht")) {
            System.out.println("Verbindung erfolgreich: " + DATABASE);
            System.out.println("SendDDL: " + sendDDL(con, ddl));
        } catch (SQLException ex) {
            Logger.getLogger(FirstSteps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sendet das DataDefinitionStatement ddl an die Datenbank mit der Connection 
     * con
     * @param con
     * @param ddl
     * @return 0 bei Erfolg
     * @throws SQLException 
     */
    private static boolean sendDDL(Connection con, String ddl) throws SQLException {
        try(Statement s = con.createStatement()) {
          return s.executeUpdate(ddl) == 0;
        }
    }
}
