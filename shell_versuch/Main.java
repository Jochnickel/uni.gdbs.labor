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

	
	/**
	* Loop design:	prompt + Input
	* 		parsing (throw Syntax/Grammar Exception)
	*			- single command infos
	*			- pipe infos
	*		execute
	* 			- create all pipes
	*			- fork
	* 			- attach pipes
	*			- file handles
	*			- execv
	*				- close unused ends
	*				- else print.err, close pipes and handles
	*
	**/
	public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {
				// GUI
				printPrompt();
				final var userInput = input();
				
				// Parsing
				final var pl = new Pipeline(userInput);
				
				// Launching Pipes, FileReaders, FileWriters and Executing
				launchPipeline(pl);
				
				// waiting for all to return
				waitAndFinishPipeline(pl);
				
			} catch (ExitShellException e) {
				return;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

		}
	}
	
	private static void launchPipeline(Pipeline pl){
		pl.runEach((/*inStream*/null,)=>{
			
			
			return new OutputSteam();
		});
	}
	
	private static void waitAndFinishPipeline(){
		
		
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
