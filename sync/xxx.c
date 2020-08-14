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

#define MX 10000

volatile int increase_me = 0;
volatile semaphore sammy;

//-----------------------------------------------------------------------------
// bevor der test beginnt wird test_setup() einmal aufgerufen
//-----------------------------------------------------------------------------

void test_setup(void) {
  printf("Test Setup\n");
  readers=1;
  writers=1;
  sammy = sem_init(0);
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

 for(int i = 1;i<10000;i++){
  //hufe scharren
  sem_p(sammy);

  //arbeit arbeit
  if(own_i!=increase_me){
    perror("Nein Nein Nein");
    exit(1);
  }
  ssem_v(sammy);

 }
}



void writer(long long_my_id) {
  
  for(;;);
}
