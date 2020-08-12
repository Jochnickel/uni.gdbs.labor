import utils.ExitShellException;

class Main {
	
	/**
	* 
	* 
	* 
	* 
	* 
	* 
	* 
	**************************************
	* Piping Info:
	* 
	* #include <unistd.h>
	* 
	* pipeFd = [read, write]
	* 
	* 1. pipe(int pipeFd[2]) Create Pipe
	* 2. fork() Fork process
	* 3. (close unused fds)
	* 
	* 
	* 
	**************************************
	* waitpid Info:
	* 
	* Childs remain in a 'Zombie' state until "wait_()"
	*
	* That means, PIDs are unique until wait_()
	* 
	* 
	**/
	
	private final static int CHILD = 0;

	public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {
				printPrompt();
				var userInput = input();
				var allExecutables = convertCommandString(userInput);
				executeAll(allExecutables);
			} catch (ExitShellException e) {
				return;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

		}
	}
	

	private static String input() throws ExitShellException {

		try {
			System.in.read();
		} catch (IOException e) {
			throw new Error();
		}
		// TODO
		return "ls | cat";
	}

	private static void printPrompt() {
		System.out.println(">");
	}

	private static void printWelcome() {
		System.out.println("Welcome!");
	}
}
