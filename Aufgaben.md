# Ringpuffer

## Ausgangssituation

 Es gibt ein Array ("feld") mit 2 Zeigern ("schreib_index","lese_index"), diese sind mit 0 initiiert.

 Wenn der writer 1 Feld vor dem leser ist (im Kreis hinten dran ist), muss er warten,
 ansonsten schreibt er i++ in sein Feld und läuft eins weiter.
 
 Wenn der reader auf dem writer-Feld ist muss er warten,
 ansonsten liest er das feld und setzt seinen Zeiger eins weiter.

### Warum funktioniert das?

 Es gibt 2 implementierungen von aktivem Warten und 2 Teilnehmer. Diese wissen voneinander (durch die 2 Zeiger).
 Der Zeiger wird als letztes weitergesetzt, also wenn der kritische Abschnitt fertig ist.

### Warum nicht bei >2 Writern ?
 
 Der Leser kann nur Rücksicht auf 1 Writer nehmen.
 Die Writer benutzen den selben Zeiger und schreiben in das gleiche Felasdd gleichzeitig.
  
## Lösung
 
 Es wird eine Semaphore implementiert, es kann jetzt nurnoch 1 Wriiter gleichzeitig agieren.
