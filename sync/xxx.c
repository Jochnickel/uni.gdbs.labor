#include "workers.h"
#include "semaphores.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>


//-----------------------------------------------------------------------------
// alle globalen variablen fuer die beiden worker hier definieren,
// alle unbedingt mit "volatile" !!!
//-----------------------------------------------------------------------------

#define LIMIT 10000

volatile int increase_me = 0;
volatile semaphore readerRdy;
volatile semaphore writerDone;

//-----------------------------------------------------------------------------
// bevor der test beginnt wird test_setup() einmal aufgerufen
//-----------------------------------------------------------------------------

void test_setup(void) {
  printf("Test Setup\n");
  readers=1;
  writers=1;
  readerRdy = sem_init(1);
  writerDone = sem_init(0);
}

//-----------------------------------------------------------------------------
// wenn beider worker fertig sind wird test_end() noch aufgerufen
//-----------------------------------------------------------------------------

void test_end(void) {
  printf("Test End\n");
}

//-----------------------------------------------------------------------------
// die beiden worker laufen parallel:
//-----------------------------------------------------------------------------

void reader(long my_id) {
	for(int i = 1;i<=LIMIT;i++){

		// Vorbereitung
		int readBuf;


//		sem_p(writerDone); //////
		readBuf = increase_me;///
//		sem_v(readerRdy);////////


		if(i!=readBuf){
			puts("\nNein Nein Nein\n");
			exit(1);
		}

		printf("%d matched\n",i);

	}
}



void writer(long long_my_id) {
	for(;increase_me<LIMIT;){

//		sem_p(readerRdy);/////
		increase_me++;////////
//		sem_v(writerDone);////

	}
}
