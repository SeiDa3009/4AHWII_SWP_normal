# **stockMarket**
##### **Was macht dieses Programm?**
Das Program dient dazu beliebige Aktien automatisch auszuwerten.

##### **Wie funktioniert das Programm?**
Zuerst werden mittels der API von Alphavantage (link weiter unten) die Adjusted-Close Werte einer Aktie in das Programm importiert.
Die Split-Correction ist im Adjusted-Close Wert schon eingebaut, da Alphavantage die Funktion, dass der Close-Wert schon Split korriegiert ist.
Anschließend werden die Daten (Datum, Adjusted-Close Wert und der jeweilige gleitende 200er Mittelwert) in eine Datenbank abgespeichert.
Zur Berechnung des gleitenden 200er Mittelwert werden die letzten 200 Werte der Aktie summiert und daraufhin mit 200 dividiert.
Für die Datenbank in diesem Programm wurde MySql verwendet.
Nachdem die Daten erfolgreich in Datenbank mit dem Tablename der Aktie abgespeichert wurde, werden die Werte anschließend dem Benutzer wieder ausgegeben.
Daraufhin werden mit den Daten weiters zwei Graphen mit JavaFx erstellt. 
1. Der erste Graph:

![](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/stockMarket/Examples/2021_APRIL_3_full.png)

Hierbei wird der ganze Verlauf der Aktie seit Gründung angezeigt.
2. Der zweite Graph: 

![](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/stockMarket/Examples/2021_APRIL_3_compact.png)

Hierbei werden nur die letzten 200 Werte angezeigt, also grob gesagt ein bisschen mehr als 1 Jahr.

Allgemein sei gesagt das die graue Linie den Adjusted-Close Wert und die schwarze Linie den gleitenden 200er Mittelwert anzeigt.
Zusätzlich wird der Hintergrund entweder grün (letzter Adjusted-Close Wert > gleitender 200er Mittelwert) oder rot (letzter Adjusted-Close Wert < gleitender 200er Mittelwert).

##### **Was wird dafür benötigt?**
- [MySqlConnector](https://dev.mysql.com/downloads/windows/installer/8.0.html)
- [JSON](https://mvnrepository.com/artifact/org.json/json/20140107)
- [CommonsIO](https://mvnrepository.com/artifact/commons-io/commons-io)


