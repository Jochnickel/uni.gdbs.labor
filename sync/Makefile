HEADER = workers.h semaphores.h
C_OPTS = -Wall -Wextra -Wno-unused -pthread -std=gnu99


%: %.c main.o semaphores.o
	@gcc $(C_OPTS) main.o semaphores.o -o $@ $<
	@-./$@
	@-/bin/rm $@

main.o: main.c $(HEADER)
	# gcc $(C_OPTS) -c main.c
	# to get rid of a wrong warning from gcc:
	gcc -Wall -Wno-unused -pthread -std=gnu99 -c main.c

semaphores.o: semaphores.c $(HEADER)
	gcc $(C_OPTS) -c semaphores.c


clean:
	/bin/rm -f *.o main *~
	./cleanup_semaphores
