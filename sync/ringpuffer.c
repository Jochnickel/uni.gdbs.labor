#include "workers.h"
#include "semaphores.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>

#define NUMBERS_CREATED_PER_WRITER 5000

//----------- animation (nicht aufgabenrelevant) ----------------------------

void start_animation(void) {
  printf("\e[64;1H");
  int n=(65+writers)/writers;
  char dots[67]="...................................................................";
  for (int i=0; i<writers; i++) printf("writer %2i: [%.*s]\n", i, n, dots);
  printf("reader   : [%.*s]\n", 66, dots);
}

void show_animation(int i_am_a_writer, int my_id, int value) {
  printf("\e[%i;%iH#", (63-writers)+(i_am_a_writer?my_id:writers),
                13+value*66/(NUMBERS_CREATED_PER_WRITER*writers));
  fflush(stdout);
}

void stop_animation(void) {
  printf("\e[64;1H");
}

// ----------- ende animation -------------------------------------------------


//-----------------------------------------------------------------------------
// alle globalen variablen fuer die beiden worker hier definieren,
// alle unbedingt mit "volatile" !!!
//-----------------------------------------------------------------------------

// der ringpuffer:

#define SIZE 5

volatile struct RINGPUFFER {
  int feld[SIZE];	// wenn (schreib_index == lese_index) dann enthaelt
  int schreib_index;	// feld[] keine daten. Sonst sind feld[lese_index]
  int lese_index;	// bis feld[(schreib_index - 1) mod SIZE] gueltig
} ringpuffer;

// variablen zur fehlererkennung/animation:
int counter;
int sum;

//-----------------------------------------------------------------------------
// bevor der test beginnt wird test_setup() einmal aufgerufen
//-----------------------------------------------------------------------------

volatile semaphore kr_ab_semaphore;

void test_setup(void) {
  printf("Test Setup\n");

  // ringpuffer initialisieren: leer!
  ringpuffer.schreib_index=0;
  ringpuffer.lese_index=0;

  readers=1; // maximal 1 (nicht veraendern!)
  writers=15; // maximal 19

  counter=0;


  // zur Fehlererkennung
  sum=0;

  // dient der Visualisierung
  start_animation();

  // yay, semahpore
  kr_ab_semaphore = sem_init(1);
}

//-----------------------------------------------------------------------------
// wenn beider worker fertig sind wird test_end() noch aufgerufen
//-----------------------------------------------------------------------------

void test_end(void) {
  stop_animation();

  // testauswertung
  int expect = writers * ((NUMBERS_CREATED_PER_WRITER * NUMBERS_CREATED_PER_WRITER)+NUMBERS_CREATED_PER_WRITER)/2;

  printf("Erwartete Summe=%i, erhalten=%i: ", expect, sum);
  if (expect == sum) {
    printf("Test ok\n");
  } else {
    printf("Fehler!\n");
  }
}

//-----------------------------------------------------------------------------
// die beiden worker laufen parallel:
//-----------------------------------------------------------------------------

void writer(long my_id) {
  int i;
  for (i=1; i<=NUMBERS_CREATED_PER_WRITER; i++) {

    sem_p(kr_ab_semaphore);

    // busy wait:
    while ((ringpuffer.schreib_index+1)%SIZE==ringpuffer.lese_index) {
       //do nothing
    }
    ringpuffer.feld[ringpuffer.schreib_index] = i;
    ringpuffer.schreib_index = (ringpuffer.schreib_index + 1) % SIZE;

    sem_v(kr_ab_semaphore);


    show_animation(1, my_id, i);
  }
}

void reader(long my_id) {
  // es darf nur einen leser geben
  if (my_id>0) {
    printf("\nFEHLER: readers>1!\n");
    exit(1);
  }

  while (1) {
    // zaehlt die gelesenen zeichen
    counter++;

    // busy wait:
    while (ringpuffer.schreib_index==ringpuffer.lese_index) {
       //do nothing
    }

    int n=ringpuffer.feld[ringpuffer.lese_index];
    ringpuffer.lese_index = (ringpuffer.lese_index + 1) % SIZE;

    // summiert gelesenen Zahlen fuer die Testausgabe
    sum+=n;

    if (counter==writers*NUMBERS_CREATED_PER_WRITER) { return; }


    show_animation(0, my_id, counter);
  }
}
