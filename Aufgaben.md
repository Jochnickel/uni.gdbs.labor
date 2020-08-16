# Semaphore

 ## Begriffe

 ## Ringpuffer

  ### Ausgangssituation

    Es gibt ein Array ("feld") mit 2 Zeigern ("schreib_index","lese_index"), diese sind mit 0 initiiert.
  
    Wenn der writer 1 Feld vor dem leser ist (im Kreis hinten dran ist), muss er warten,
    ansonsten schreibt er i++ in sein Feld und läuft eins weiter.
   
    Wenn der reader auf dem writer-Feld ist muss er warten,
    ansonsten liest er das feld und setzt seinen Zeiger eins weiter.

  ### Warum funktioniert das?

    Es gibt 2 Implementierungen von aktivem Warten und 2 Teilnehmer. Diese wissen voneinander (durch die 2 Zeiger).
    Der Zeiger wird als letztes weitergesetzt, also wenn der kritische Abschnitt fertig ist.
    Das Array beinhaltet genügend Platz für die Manövrierung (3).

  ### Warum nicht bei >2 Writern ?
 
    Der Leser kann nur Rücksicht auf 1 Writer nehmen.
    Die Writer benutzen den selben Zeiger und schreiben in das gleiche Feld gleichzeitig.
  
  ### Lösung
 
    Es wird eine Semaphore implementiert, es kann jetzt nurnoch 1 Writer gleichzeitig agieren.
    Der kritische Abschnitt beinhaltet den check, ob man dem 1 Reader auf die Stoßstange fährt, den Write-Vorgang und Zeiger-Inkrement.

 ## Philosophen
 
  ### Setup

    Es gibt 5 Philosophen.
    Jeder Philosoph will 100 Veggie-Bällchen essen.
    Jeder Philosoph hat ein persönliches Ess-Stäbchen.
    An zufälligen Zeitpunkten greift ein Philosph sein Stäbchen und ein anderes (jew. falls möglich).
    Hat ein Philosoph 2 Stäbchen, so isst er 1 Veggie-Bällchen und legt die Stäbchen zurück.
    Wenn jeder Philosoph genau ein Stäbchen hat, geben sie freiwillig dieses ab.

  ### Problem

    Während die Philosophen zum Stäbchen greifen, kann jemand anderes dieses "wegschnappen".

  ### Lösung

    Es wird ein kritischer Abschnitt in Stäbchen_Nehmen implementiert.

 ## Zwei synchrone Zähler

  ### Problem

    Der Writer/Reader soll auf den anderen warten, und nicht einfach weitermachen.
    D.h. der Writer muss mit dem Zählen warten, der Reader mit dem Lesen.

  ### Lösung

    Es werden 2 Semaphoren benötigt.
    Schaubild:
              reader sem1 sem2 writer
                     ---- XXXX
                     XXXX XXXX p(sem1)
                     XXXX XXXX global++;
                     XXXX ---- v(sem2)
             p(sem2) XXXX XXXX
                read XXXX XXXX
             v(sem1) ---- XXXX
             compare XXXX XXXX p(sem1)
                     .
                     .
                     .

    Die kritischen Abschnitte sind hier nur um die increase bzw. compare-methode.


# FAT

 ## Begriffe
  
  ### Endianness
   little Endian = 'verdreht'
  ### Byte
   kleine Einheit, 8 Bit, 0x00 Hex
  ### Cluster
   Paket von Bytes
   z.B. 512 Byte = Byte 0x000 bis 0x200
  ### Sector
   Paket von Clustern, hier 8 Cluster = 1 Sektor

 ## rename falsch.txt
  
  16 Bytes Long file names
  16 Bytes Long file names
  8 Bytes fn               3 Bytes ext, 1 byte info, 4 bytes?
  6 Bytes? 2Bytes time     2 Bytes date, 2 Bytes first cluster, 4 Bytes size

             --  -----------------------------   -- --   --     -------------------------------------  ------   -----------
  richtig = (41) 72 __ 69 __ 63 __ 68 __ 74 __  (0F 00)  AE     69 __ 67 __ 2E __ (74 __ 78 __ 74 __)  (00 00)  00 __ FF FF
  RICHTIG = 52 49 43 48 54 49 47

  Datum = 86 41
  
 ## kurz.txt
  
  bisher 03 - richtig.txt   04 - kurz.txt   05 - 06 - lang.txt   07 - movehere  08 - problem   0B - subdir
  neuer cluster 0D
  size ist nun 17 10 __ __

 ## subdir movehere

                                                                ..
                                                                .O.O
   0000D440   41 62 00 65 00 69 00 73  00 70 00 0F 00 7D 69 00  Ab.e.i.s.p...}i.
   0000D450   65 00 6C 00 2E 00 74 00  78 00 00 00 74 00 00 00  e.l...t.x...t...
   0000D460   42 45 49 53 50 49 45 4C  54 58 54 20 00 42 AD 7E  BEISPIELTXT .B.~
   0000D470   1C 4F 1C 4F 00 00 AD 7E  1C 4F 0C 00 09 00 00 00  .O.O...~.O......

