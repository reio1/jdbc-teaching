package at.htlstp.reio.jdbc.sakila.db;

import at.htlstp.reio.jdbc.sakila.model.Actor;
import java.sql.SQLException;
import java.util.List;

/**
 * @author reio
 */
public interface ISakilaDAO extends AutoCloseable {

    /**
     * Stellt die Verbindung zur Datenbank her.
     *
     * @param dbUrl Datenbank-URL
     * @param user Der Datenbankuser
     * @param pwd  Das Datenbankpasswort
     * @throws SQLException wenn die Verbindung nicht hergestellt werden kann
     */
    void connect(String dbUrl, String user, String pwd) throws SQLException;

    /**
     * Liefert den Verbindungsstatus
     *
     * @return true bei bestehender Verbindung, false sonst
     */
    boolean isConnected();

    /**
     * Liefert alle Schauspieler als Liste
     *
     * @return Liste aller Schauspieler,
     * @throws SQLException Bei einem Datenbankfehler
     */

    List<Actor> getAllActors() throws SQLException;

    /**
     * Aktualisiert den Schauspieler a in der Datenbank. Der Schauspieler
     * muss eine gültige ID (ungleich null) besitzen.
     *
     * @param a der zu aktualisierende Schauspieler
     * @return true bei Erfolg, false wenn der Datensatz nicht aktualisiert wurde
     * @throws SQLException bei einem Datenbankfehler
     */
    boolean updateActor(Actor a) throws SQLException;

    /**
     * Liefert eine Liste aller Filmtitel, in denen der übergebene
     * Schauspieler mitspielt
     *
     * @param actor der
     * @return Liste aller Filmtitel, in denen actor mitspielt
     * @throws SQLException bei einem Datenbankfehler
     */
    List<String> getFilmtitlesByActor(Actor actor) throws SQLException;

    /**
     * Liefert den Schauspieler mit PK id
     *
     * @param id ... PK des Schauspielers
     * @return den Schauspieler, null wenn die übergebene id nicht existiert
     * oder null ist
     * @throws SQLException bei einem Datenbankfehler
     */
    Actor findActorById(Integer id) throws SQLException;


    /**
     * Speichert den Schauspieler a in der DB. In a muss die id noch auf
     * null stehen
     *
     * @param a der zu pesistierende Schauspieler
     * @return der autogenerierte PK, null wenn a nicht gespeichert wurde
     * @throws SQLException bei einem Datenbankfehler
     */
    Integer saveActor(Actor a) throws SQLException;

    /**
     * Wird ueberschrieben, damit nur eine SQLException behandelt werden
     * muss
     *
     * @throws SQLException
     */
    @Override
    void close() throws SQLException;

}
