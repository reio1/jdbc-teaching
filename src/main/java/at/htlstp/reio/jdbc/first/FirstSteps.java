/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.reio.jdbc.first;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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

        try ( Connection con = DriverManager.getConnection(jdbcurl, "unterricht", "unterricht")) {
            System.out.println("Verbindung erfolgreich: " + DATABASE);
            System.out.println("SendDDL: " + sendDDL(con, ddl));
            //int result = insertData_1(con, "names.csv");
            int result = insertData_2(con, "names.csv");
            // result = insertData_2(con, "names.csv");
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
     * @return 0 bei Erfolg
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
}
