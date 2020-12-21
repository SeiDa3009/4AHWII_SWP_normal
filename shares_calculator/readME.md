# Aktien Auslesung und Bearbeitung
Für das folgende Programm werden die Libraries verwendet:
* [SQLite](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc) 
* [CommonsIO](https://mvnrepository.com/artifact/commons-io/commons-io) 
* [JSON](https://mvnrepository.com/artifact/org.json/json/20140107) 

Nachdem die Libraries heruntergeladen wurden müssen sie noch beim Project Structure hinzugefügt werden

Das Programm wird verwendet um Aktien auslesen und bearbeiten.

Anfangs findet ein API-Aufrauf statt (https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&outputsize=full&apikey=N4XI1HLI5LGAYT87). Dieser importiert den Wert Close und das Datum. 
Anschließend werden die Werte mithilfe von SQLite in eine Datenbank gespeichert.
Als letzen Schritt wird ein JavaFX Liniendiagramm erstellt.

##### Ausführung:
Anfangs muss kommt eine Abfrage, welche Aktie herausgelesen werden soll (in diesem Fall IBM). 
Anschließend wird die API aufgerufen und die herausgelesenen Werte werden sortiert in eine HASHMAP gespeichert.

Dann wird die Datenbank mit dem Namen shares.db erstellt.
Nach der Erzeugung der Datenbank wird ein _TABLE_ mit dem Namen der Aktie und den Variablen _shares_ und _date_ erstellt.
Die Werte werden dank der Vergabe des Primary Keys nie doppelt von der Hashmap eingelesen. Zum Primary Key zusätzlich habe ich noch ein _UNIQUE_ hinzugefügt, um beim Insert der Daten mit dem Befehl _INSERT OR IGNORE INTO_ arbeiten können.
Dieser Vorgang erspart einem sehr viel Zeit beim Einlesen.
Als Nächstes kommt die Abfrage, welchen gleitenden Durchschnitt man nehmen wolle. Diese Zahl limitiert dann zugleich die Ausgabe der Werte. 
Zusätzlich zur Ausgabe der Werte werden diese noch in eine ArrayList gespeichert, um den gleitenden Durchschnitt zu berechnen.
(Wird in den Weihnachtsferien mit Select ausgebessert, da es eigentlich unnötig ist; Wollte aber zuerst das Programm zum Laufen bringen)

Zum Schluss wird mit den Werten aus der Datenbank noch ein JAVAFX-Linechart erstellt.
Dieser stellt einmal die Close-Werte mit den Daten als Linie dar und einmal den gleitenden Durchschnitt als Linie dar.
(Hier werden auch unnötige Listen verwendet, die ebenso in den Weihnachtsferien ausgebessert werden)

##### Zusätzlich: 
Ich wollte die Linien, die über dem gleitenden Durchschnitt liegen, grün färben und die Linien, die unter dem gleitenden Durchschnitt liegen, rot färben.
Da muss ich mich allerdings noch mal erkundigen, wie ich das Lösen kann.

Zu guter Letzt kommt noch das Beispielbild (IBM-Aktie):
![.](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/shares_calculator/ExampleIBM.JPG)













