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
 
 Es wird eine Semaphore implementiert, es kann jetzt nurnoch 1 Writer gleichzeitig agieren.
 Der kritische Abschnitt beinhaltet den check, ob man dem 1 Reader auf die Stoßstange fährt, den Write-Vorgang und Zeiger-Inkrement.

# Philosophen
 
 ## Setup
  Es gibt 5 Philosophen.
  Jeder Philosoph will 100 Veggie-Bällchen essen.
  Jeder Philosoph hat ein persönliches Ess-Stäbchen.
  An Zufälligen Zeitpunkten greift ein Philosph sein Stäbchen und ein anderes (jew. falls möglich).
  Hat ein Philosoph 2 Stäbchen, so isst er 1 Veggie-Bällchen und legt die Stäbchen zurück.
  Wenn jeder Philosoph genau ein Stäbchen hat, geben sie freiwillig dieses ab.

 ## Problem
  Während die Philosophen zum Stäbchen greifen, kann jemand anderes dieses "wegschnappen".

 ## Lösung
  Es wird ein kritischer Abschnitt in Stäbchen_Nehmen implementiert.

# Zwei synchrone Zähler

 ## Lösung
  Es werden 2 Semaphoren benötigt.
  Schaubild:
              reader sem1 sem2 writer
                     ---------
                     OOOO XXXX
                     XXXX XXXX p(sem1)
                     XXXX XXXX global++;
                     XXXX OOOO v(sem2)
             p(sem1) XXXX OOOO
                     XXXX OOOO
                     OOOO XXXX
                     OOOO XXXX
                     OOOO XXXX
                     OOOO XXXX
                     OOOO XXXX
                     OOOO XXXX

