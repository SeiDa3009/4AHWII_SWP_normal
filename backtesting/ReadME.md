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
Anschließend beginnt die Loop mit der Anzahl der Aktienticker. Jetzt werden die verschiedenen Strategien durchlaufen:
	1. Strategie 1 | 200er avg
	Als erstes wird eine dummyZeile in die Tabelle eingefügt. Danach wird überprüft ob zuletzt gekauft oder verkauft wurde. Je nach dem ob gekauft oder verkauft wird, wird überprüft ob close >/< 200er avg.
	Wenn der Fall eintritt, wird verkauft bzw. gekauft. Am letzten möglichen Tag wird, falls noch nicht verkauft wurde verkauft.
	2. Strategie 2 | 200er avg + 3%
	Als erstes wird eine dummyZeile in die Tabelle eingefügt. Danach wird überprüft ob zuletzt gekauft oder verkauft wurde. Je nach dem ob gekauft oder verkauft wird, wird überprüft ob close >/< 200er avg + 3%.
	Wenn der Fall eintritt, wird verkauft bzw. gekauft. Am letzten möglichen Tag wird, falls noch nicht verkauft wurde verkauft.
	3. Strategie 3 | Buy & Hold
	Als erstes wird eine dummyZeile in die Tabelle eingefügt. Danach wird am erst möglichen Tag gekauft und am letzmöglichen verkauft.

Zu guter Letzt wird das Enddepot noch ausgegeben.


##### **Graph**
![](C:\Users\david\Documents\Schule\SWP Rubner\Aktien\Backtesting\atvi.jpg)

##### **Was wird dafür benötigt?**
- [MySqlConnector](https://dev.mysql.com/downloads/windows/installer/8.0.html)
- [JSON](https://mvnrepository.com/artifact/org.json/json/20140107)
- [CommonsIO](https://mvnrepository.com/artifact/commons-io/commons-io)


