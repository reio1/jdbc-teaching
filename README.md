# jdbc-training Projekt by reio
Das ist ein Projekt um das Lernen von JDBC zu unterstützen

## FirstSteps
In der Klasse ``at.htlstp.reio.jdbc.first.FirstSteps`` werden die ersten Schritte mit JDBC gezeigt:

1. Verbinden mit einer Datenbank
2. Absetzen eines DDL - Befehls mit Hilfe eines Statements
3. Einfügen von Daten in eine Tabelle mit Hilfe eines Prepared Statements
4. Verwendung einer einfachen Transaktion

## Sakila
Im Packet ``at.htlstp.reio.jdbc.sakila`` wird eine einfache Anwendung mit einem PersistanceLayer gezeigt.
Es gibt ein Domainenmodell mit Geschäftsklassen (Entitäten). Im ersten Schritt wird nur die Entität ``Actor`` verwendet.

Den Zugriff auf die Datenbank steuert das Interface ``ISakilaDAO``.  Hier sind Methoden definiert, mit deren Hilfe jede Anwendung mit der Datenbank kommunizieren kann:

```java
    /**
     * Liefert alle Schauspieler als Liste
     *
     * @return Liste aller Schauspieler
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
  
    /**
     * Liefert eine Liste aller Filmtitel, in denen der übergebene 
     * Schauspieler mitspielt
     * @param actor der 
     * @return Liste aller Filmtitel, in denen actor mitspielt
     * @throws SQLException bei einem Datenbankfehler
    */
    public List<String> getFilmtitlesByActor(Actor actor) throws SQLException;
  
    /**
     * Liefert den Schauspieler mit PK id
     * @param id ... PK des Schauspielers
     * @return den Schauspieler, null wenn die übergebene id nicht existiert 
     *         oder null ist
     * @throws SQLException bei einem Datenbankfehler
    */
    public Actor findActorById(Integer id) throws SQLException;
  
    /**
     * Speichert den Schauspieler a in der DB. In a muss die id noch auf
     * null stehen
     * @param a der zu pesistierende Schauspieler 
     * @return der autogenerierte PK, null wenn a nicht gespeichert wurde
     * @throws SQLException bei einem Datenbankfehler
    */
    public Integer saveActor(Actor a);
```

