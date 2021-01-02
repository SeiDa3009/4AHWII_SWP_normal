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
Nach der Erzeugung der Datenbank wird ein _TABLE_ mit dem Namen der Aktie und den Variablen _shares_, _date_ und _avg_ erstellt.
Die Werte werden dank der Vergabe des Primary Keys nie doppelt von der Hashmap eingelesen.
Als Nächstes kommt die Abfrage, welchen gleitenden Durchschnitt man nehmen wolle.
Anschließend werden die Werte (Close-Wert, Date, AVG) mit dem Befehl Select ausgegeben.

Zum Schluss wird mit den Werten aus der Datenbank (Select) noch ein JAVAFX-Linechart erstellt.
Dieser stellt einmal die Close-Werte mit den Daten als Linie dar und einmal den gleitenden Durchschnitt als Linie dar.
Dabei gilt ist die Close-Werte Linie grün, so handelt es sich um eine gute Aktie.
Wenn diese Linie Rot ist, handelt es sich um eine schlecht Aktie.

Zu guter Letzt kommen noch die Beispielbilder. 
![Grün](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/shares_calculator/ExampleGreen.JPG)
![Rot](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/shares_calculator/ExampleRed.JPG)













