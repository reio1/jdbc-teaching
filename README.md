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
     * @return Liste aller Schauspieler,
     * @throws SQLException Bei einem Datenbankfehler
     */

    public List<Actor> getAllActors() throws SQLException;
```



