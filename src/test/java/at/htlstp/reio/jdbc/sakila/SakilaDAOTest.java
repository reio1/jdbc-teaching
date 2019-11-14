/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.reio.jdbc.sakila;

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
    public void setUp() throws SQLException {

    }
    
    @After
    public void tearDown() {
       
    }

    
    @Test
    public void testGetFilmtitlesByActor() throws Exception {
        System.out.println("getFilmtitlesByActor");
        Actor actor = new Actor("", "");
        actor.setId(2);
        List<String> result = dao.getFilmtitlesByActor(actor);
        assertEquals(25, result.size());
        assertTrue(result.contains("BABY HALL"));
    }
    
    /**
     * Test if existing Actor could be read from database
     * @throws Exception 
     */
    @Test
    public void testfindActorById() throws Exception {
        System.out.println("findAcorById");
        Actor actor = dao.findActorById(2);
        assertNotNull(actor);
        assertEquals("NICK", actor.getFirstname());
        assertEquals("WAHLBERG", actor.getLastname());
    }
    
    @Test
    public void testfindActorById2() throws Exception {
        System.out.println("findAcorById");
        Actor actor = dao.findActorById(2000);
        assertNull(actor);
    }
    
    
    
}
