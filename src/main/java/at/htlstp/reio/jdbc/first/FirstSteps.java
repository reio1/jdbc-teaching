/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.reio.jdbc.first;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diese Klasse zeigt wie man sich mit einer Datenbank verbindet Außerdem werden
 * Testdaten in eine Tabelle geschrieben
 *
 * @author reio
 */
public class FirstSteps {

    public static void main(String[] args) {
        final String DATABASE = "reio_first_steps";
        
        Properties props = new Properties();

        String ddl = "";
        ddl += "DROP TABLE IF EXISTS persons;\n";
        ddl += "CREATE TABLE persons (\n";
        ddl += "   per_id INTEGER NOT NULL,\n";
        ddl += "   per_lastname VARCHAR(100),\n";
        ddl += "   per_firstname VARCHAR(100),\n";
        ddl += "   CONSTRAINT pk_persons PRIMARY KEY(per_id)\n";
        ddl += ");";
        

        // Treiber und properties laden
        try {
            // Treiber laden
            Class.forName("org.postgresql.Driver");
            props.load(new FileInputStream("connection_props.properties"));
            System.out.println("Driver and props sucessfully loaded");
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(FirstSteps.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        String dbUrl = props.getProperty("db_url");
        String dbUser = props.getProperty("db_user");
        String dbPassword = props.getProperty("db_password");
                

        try ( Connection con = DriverManager.getConnection(dbUrl + DATABASE, dbUser, dbPassword)) {
            System.out.println("Verbindung erfolgreich: " + DATABASE);
            System.out.println("SendDDL: " + sendDDL(con, ddl));
            int result = insertData_3(con, "names.csv");
            //int result = insertData_3(con, "names.csv");
            for(int i = 1; i <= 1000; i++) {
            result += insertData_3(con, "names.csv");
            }
            System.out.println(result + " Datensätze erfolgreich geschrieben.");
        } catch (SQLException ex) {
            Logger.getLogger(FirstSteps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sendet das DataDefinitionStatement ddl an die Datenbank mit der
     * Connection con
     *
     * @param con
     * @param ddl
     * @return true bei Erfolg
     * @throws SQLException
     */
    private static boolean sendDDL(Connection con, String ddl) throws SQLException {
        try ( Statement s = con.createStatement()) {
            return s.executeUpdate(ddl) == 0;
        }
    }

    /**
     * Schreibt die aus der Datei csvFile gelesenen Datensätze in die Tabelle
     * ohne Transaktion - bei einem Fehler werden nicht alle Daten geschrieben
     * persons
     *
     * @param con
     * @param csvFile
     * @return Anzahl der erfolgreich geschriebenen Datensätze, 0 bei Mißerfolg
     * @throws SQLException bei einem Datenbankfehler
     */
    private static int insertData_1(Connection con, String csvFile) throws SQLException {
        String sql = "INSERT INTO persons VALUES(?, ?, ?);";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(csvFile));
        } catch (IOException ex) {
            System.out.println("Datei nicht gefunden: " + csvFile);
            return 0;
        }
        int result = 0;
        int pk = 1;
        
        // start always with pk = 1 - problems if we call the method twice
        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            for (String l : lines) {
                if (!l.isBlank()) {     // isBlank() requires Java 11
                    String[] token = l.split(";");
                    ps.setInt(1, pk++);
                    ps.setString(2, token[0]);
                    ps.setString(3, token[1]);
                    result += ps.executeUpdate();
                    ps.clearParameters();
                }
            }
        }
        return result;
    }

    /**
     * Schreibt die aus der Datei csvFile gelesenen Datensätze als Transaktion
     * in die Tabelle persons. Bei einem fehler wird die Transaktion
     * zurückgerollt und kein DAtensatz wird geschrieben.
     *
     * @param con
     * @param csvFile
     * @return Anzahl der erfolgreich geschriebenen Datensätze, 0 bei Mißerfolg
     * @throws SQLException bei einem Datenbankfehler
     */
    private static int insertData_2(Connection con, String csvFile) throws SQLException {
        String sql = "INSERT INTO persons VALUES(?, ?, ?);";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(csvFile));
        } catch (IOException ex) {
            System.out.println("Datei nicht gefunden: " + csvFile);
            return 0;
        }
        int result = 0;
        int pk = 1;
        boolean oldCommitState = con.getAutoCommit();
        // start always with pk = 1 - problems if we call the method twice
        con.setAutoCommit(false);
        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            for (String l : lines) {
                if (!l.isBlank()) {
                    String[] token = l.split(";");
                    ps.setInt(1, pk++);
                    ps.setString(2, token[0]);
                    ps.setString(3, token[1]);
                    result += ps.executeUpdate();
                    ps.clearParameters();
                }
            }
            con.commit();
        } catch (Exception ex) {
            System.out.println("Fehler: " + ex.getMessage());
            System.out.println("Transaction rollback");
            result = 0;
            con.rollback();
            //throw ex;
        } finally {
            con.setAutoCommit(oldCommitState);
        }
        return result;
    }
    
    /**
     * Schreibt die aus der Datei csvFile gelesenen Datensätze als Transaktion
     * in die Tabelle persons. Bei einem Fehler wird die Transaktion
     * zurückgerollt und kein Datensatz wird geschrieben.
     * Außerdem wird der größte in der DB gespeicherte PK gelesen und 
     * inkrementiert.
     *
     * @param con
     * @param csvFile
     * @return Anzahl der erfolgreich geschriebenen Datensätze, 0 bei Mißerfolg
     * @throws SQLException bei einem Datenbankfehler
     */
    private static int insertData_3(Connection con, String csvFile) throws SQLException {
        String sql = "INSERT INTO persons VALUES(?, ?, ?);";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(csvFile));
        } catch (IOException ex) {
            System.out.println("Datei nicht gefunden: " + csvFile);
            return 0;
        }
        int result = 0;
      
        int pk = 1;
        String sql1 = "SELECT MAX(per_id) AS maxpk FROM persons;";
        try(Statement s = con.createStatement()) {
            ResultSet rs = s.executeQuery(sql1);
            if(rs.next()) {
                pk = rs.getInt(1);
                pk++;
            }
        }
        boolean oldCommitState = con.getAutoCommit();
        // start always with pk = 1 - problems if we call the method twice
        con.setAutoCommit(false);
        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            for (String l : lines) {
                if (!l.isBlank()) {
                    String[] token = l.split(";");
                    ps.setInt(1, pk++);
                    ps.setString(2, token[0]);
                    ps.setString(3, token[1]);
                    result += ps.executeUpdate();
                    ps.clearParameters();
                }
            }
            con.commit();
        } catch (Exception ex) {
            System.out.println("Fehler: " + ex.getMessage());
            System.out.println("Transaction rollback");
            result = 0;
            con.rollback();
            //throw ex;
        } finally {
            con.setAutoCommit(oldCommitState);
        }
        return result;
    }
}
