package at.htlstp.reio.jdbc.sakila;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author reio
 */
public interface ISakilaDAO extends AutoCloseable {
    
 /**
   * Stellt die Verbindung zur Datenbank her.
   *
   * @param user Der Datenbankuser
   * @param pwd Das Datenbankpasswort
   * @throws SQLException wenn die Verbindung nicht hergestellt werden kann
   */
  public void connect(String dbUrl, String user, String pwd) throws SQLException;

  /**
   * Liefert den Verbindungsstatus
   *
   * @return true bei bestehender Verbindung, false sonst
   */
  public boolean isConnected();

  /**
   * Liefert alle Schauspieler als Liste
   *
   * @return Liste aller Schauspieler,
   * @throws SQLException Bei einem Datenbankfehler
   */

  public List<Actor> getAllActors() throws SQLException;

  /**
   * Aktualisiert den Schauspieler a in der Datenbank. Der Schauspieler 
   * muss eine gültige ID (ungleich null) besitzen.
   * @param a der zu aktualisierende Schauspieler
   * @return true bei Erfolg, false wenn der Datensatz nicht aktualisiert wurde
   * @throws SQLException bei einem Datenbankfehler
   */
  public boolean updateActor(Actor a) throws SQLException;
  
  public List<String> getFilmtitlesByActor(Actor actor) throws SQLException;
  
  public Actor findActorById(Integer id) throws SQLException;
  
  public Integer saveActor(Actor a);

  /**
   * Wird ueberschrieben, damit nur eine SQLException behandelt werden
   * muss
   * @throws SQLException 
   */
  @Override
  public void close() throws SQLException;
    
}
