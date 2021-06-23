# **Backtesting**
##### **Was macht dieses Programm?**
Das Programm berechnet mithilfe der Angabe des Aktienticker, eines Startdatums und dem Startkapital, mit 3 Strategien den Erzielten Gewinn bzw. Verlust bis gestern.

##### **Wie funktioniert das Programm?**
Im Programm gibt es zwei bzw. drei verschiedene EntryPoint's:
- EntryPointAPIData:
Zuerst muss eine Datenbank erstellt werden und die genauen Close-Werte der bestimmten Aktien von der API ausgelesen werden. Danach müssen DB-Table erstellt und die Verbindung zu DB aufgebaut werden. 
Anschließend werden die Close-Werte von der API mit dem dazugehörigen Datum und Splitcoeffizienten in einen Datenbank Table mit dem Namen des Tickers gespeichert. 
Zusätzlich müssen die adjustedClose-Werte und der gleitender 200er Durchschnitt berechnet und ebendfalls in die DB eingeschrieben werden.
Diese Schritte bis auf die Erstellung der Datenbank wird so oft wiederholt wie Aktienticker in Textdatei (stocks.txt) vorhanden sind.

- EntryPointBacktesting: 
Hier muss zuerst der User mittels Eingabe das Startkapital und das Startdatum angegeben werden. Falls, inkorrekte Werte eingegeben wurden, hat der User 3 mal die Chance sein eingegebene Werte zu korrigieren, ansonsten wird das Programm beendet.
Anschließend beginnt die Loop mit der Anzahl der Aktienticker. 
1. Der erste Graph:

![](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/stockMarket/Examples/2021_APRIL_3_full.png)

Hierbei wird der ganze Verlauf der Aktie seit Gründung angezeigt.

2. Der zweite Graph: 

![](https://github.com/SeiDa3009/4AHWII_SWP_normal/blob/master/stockMarket/Examples/2021_APRIL_3_compact.png)

Hierbei werden nur die letzten 200 Werte angezeigt, also grob gesagt ein bisschen mehr als 1 Jahr.

Allgemein sei gesagt, dass die graue Linie den Adjusted-Close Wert und die schwarze Linie den gleitenden 200er Mittelwert anzeigt.
Zusätzlich wird der Hintergrund entweder grün (letzter Adjusted-Close Wert > gleitender 200er Mittelwert) oder rot (letzter Adjusted-Close Wert < gleitender 200er Mittelwert).

Das ganze Programm (cmd Datei) wird dann nur noch in der Windows Aufgabenplannung eingefügt und schon wird das Programm je nach Einstellung (z.B. täglich) ausgeführt.

##### **Was wird dafür benötigt?**
- [MySqlConnector](https://dev.mysql.com/downloads/windows/installer/8.0.html)
- [JSON](https://mvnrepository.com/artifact/org.json/json/20140107)
- [CommonsIO](https://mvnrepository.com/artifact/commons-io/commons-io)


