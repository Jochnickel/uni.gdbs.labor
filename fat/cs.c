#include <stdio.h>
#include <ctype.h>

void main (int argc, char* argv[]){
  unsigned char sum = 0;
  char** a = argv;
  for (short fcbNameLen = 8; fcbNameLen > 0; fcbNameLen--){
      char c;
      if ('.'==*argv[1]){
          argv[2] = argv[1]+1;
          c = ' ';
      } else if(*argv[1]){
          c = toupper(*argv[1]++);
      } else {
          c = ' ';
      }
      printf("%c",c);
      sum = ((sum & 1) ? 0x80 : 0) + (sum >> 1) + c;
  }
  for (short fcbNameLen = 3; fcbNameLen > 0; fcbNameLen--){
      char c;
      if(*argv[2]){
          c = toupper(*argv[2]++);
      } else {
          c = ' ';
      }
      printf("%c",c);
      sum = ((sum & 1) ? 0x80 : 0) + (sum >> 1) + c;
  }
  printf ("\nChecksum: %X\n", sum);
}
