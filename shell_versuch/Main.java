import static cTools.KernelWrapper.*;

class Main {
  class ExitShellException extends Exception{}
  public static void main(String[] args) {
    System.out.println("Hello, World!");
    final var childID = fork();
    if(childID<0){
     System.err.println("Couldnt createe fork");
     exit(childID);
    } else if (childID > 0 ){
     final var returnCode = new int[1];
     final var wpid = waitpid(childID, returnCode, 0);
     System.out.printf("Process exited with exit Code %d",returnCode[0]);
    } else {
     _execv("/bin/ls","/");
    }
    exit(0);
  }
  public static void _execv(String path, String ... args){
   execv(path, args);
  }
}
