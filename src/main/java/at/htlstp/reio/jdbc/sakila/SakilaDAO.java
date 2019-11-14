/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.reio.jdbc.sakila;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reio
 */
public class SakilaDAO implements ISakilaDAO {

    private final static String JDBCDRIVER = "org.postgresql.Driver";

    private Connection con = null;

    static {
        try {
            Class.forName(JDBCDRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des Treibers: " + e);
        }
    }

    @Override
    public void connect(String dbUrl, String user, String pwd) throws SQLException {
        con = DriverManager.getConnection(dbUrl, user, pwd);
    }

    @Override
    public boolean isConnected() {
        return con != null;
    }

    @Override
    public void close() throws SQLException {
        System.out.println("close...");
        if (con != null) {
            con.close();
            con = null;
        }
    }

    @Override
    public List<Actor> getAllActors() throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected");
        }
        List<Actor> lst = new ArrayList<>();
        String sql = "SELECT actor_id, first_name, last_name FROM actor";

        try ( Statement s = con.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                lst.add(new Actor(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        }
        return lst;
    }

    @Override
    public boolean updateActor(Actor a) throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected");
        }
        if (a.getId() == null) {
            return false;
        }

        String sql = "update actor set first_name = ?, last_name = ? ";
        sql += "where actor_id = ?";

        try ( PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, a.getFirstname());
            pst.setString(2, a.getLastname());
            pst.setInt(3, a.getId());
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public List<String> getFilmtitlesByActor(Actor actor) throws SQLException {
        String sql = "SELECT film.title FROM film\n";
        sql += "INNER JOIN film_actor ON film.film_id = film_actor.film_id AND film_actor.actor_id = ?;";

        List<String> filmTitles = new ArrayList<>();
        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, actor.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                filmTitles.add(rs.getString(1));
            }
        }

        return filmTitles;
    }

    @Override
    public Actor findActorById(Integer id) throws SQLException {
        String sql = "SELECT actor_id, first_name, last_name from actor where actor_id = ?";
        try ( PreparedStatement s = con.prepareStatement(sql)) {
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return new Actor(rs.getInt(1), rs.getString(2), rs.getString(3));
            } else {
                return null;
            }
        }
    }

    @Override
    public Integer saveActor(Actor a) {
        //TODO: implement
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
