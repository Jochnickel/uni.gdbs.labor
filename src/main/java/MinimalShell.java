
public class MinimalShell {

	public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {
				printCursor();
				final var userInput = readInput();
				execInput(userInput);
			} catch (ExitShellException e) {
				break;
			}
		}
	}

	private static void execInput(String[] userInput) {
		if (null == userInput || userInput.length < 1) {
			return;
		}
		final var programName = userInput[0];
		try {
			final var myChildOrNull = KernelWrapperHandler.Fork();
			if(myChildOrNull==0) {
				KernelWrapperHandler.Execv(userInput);
			} else {
				final var exitCode = KernelWrapperHandler.Waitpid(myChildOrNull);
				System.out.printf("%s exited with exit code %d\n",programName,exitCode);
			}
			
		} catch (ProgramNotFoundException e) {
			System.err.printf("Program not found %s\n", programName);
		} catch (ForkFailedException e) {
			System.err.printf("Failed creating fork\n");
		}
	}

	private static String[] readInput() throws ExitShellException {
		final String str = Input.read();
		return str.split("\\s+");
	}

	private static void printCursor() {
		System.out.printf(">_");
	}

	private static void printWelcome() {
		System.out.println("Welcome to minimal shell!");
	}

}
