/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.reio.jdbc.sakila;

import at.htlstp.reio.jdbc.sakila.db.ISakilaDAO;
import at.htlstp.reio.jdbc.sakila.db.SakilaDAO;
import at.htlstp.reio.jdbc.sakila.model.Actor;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reio
 */
public class SakilaDAOTest {
    
    private static ISakilaDAO dao;
    
    public SakilaDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        
        Properties props = new Properties();
        props.load(new FileInputStream("connection_props.properties"));
        dao = new SakilaDAO();
        dao.connect(props.getProperty("sakila_url"), 
                    props.getProperty("db_user"),
                    props.getProperty("db_password"));
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        dao.close();
    }
    
    @Before
    public void setUp() {

    }
    
    @After
    public void tearDown() {
       
    }

    @Test
    public void testGetFilmTitlesByActor() throws Exception {
        System.out.println("getFilmTitlesByActor");
        Actor actor = new Actor("", "");
        actor.setId(2);
        List<String> result = dao.getFilmtitlesByActor(actor);
        assertEquals(25, result.size());
        assertTrue(result.contains("BABY HALL"));
    }

    /**
     * Test if existing Actor could be read from database
     * @throws Exception SQlException possible
     */
    @Test
    public void testFindActorById() throws Exception {
        System.out.println("findActorById");
        Actor actor = dao.findActorById(2);
        assertNotNull(actor);
        assertEquals("NICK", actor.getFirstname());
        assertEquals("WAHLBERG", actor.getLastname());
    }
    
    @Test
    public void testFindActorById2() throws Exception {
        System.out.println("findActorById");
        Actor actor = dao.findActorById(2000);
        assertNull(actor);
    }
    
    @Test
    public void saveActor() throws Exception {
        System.out.println("saveActor - success");
        Actor actor = new Actor("Franz", "Klammer");
        Integer id = dao.saveActor(actor);
        assertTrue(id > 0);
        assertTrue(id.equals(actor.getId()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void saveActor_1() throws Exception {
        System.out.println("saveActor - actor has id");
        Actor actor = new Actor(2000, "Franz", "Klammer");
        dao.saveActor(actor);
    }
    
    
    
}
