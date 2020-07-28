import static cTools.KernelWrapper.*;

import java.util.Scanner;

class Main {
	public static void main(String[] args) {
//		_execv("/bin/ls","/");
		printWelcome();
		for (;;) {
			try {
				// UI
				printCursor();
				final var userInput = input();
				final var programCall = getProgramCall(userInput);

				// Execution

				final int childID = _forkAndExec(programCall);
				final var r = _waitpid(childID);
				printExit(r);

			} catch (InvalidInputException e) {
				e.printStackTrace();
			} catch (ExitShellException e) {
				break;
			} catch (ShellError e) {
				e.printStackTrace();
				return;
			}
		}

		exit(0);
	}

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		waitpid(childID, returnCode, 0); // returns int
		return returnCode[0];
	}

	private static int _forkAndExec(programCall programCall) throws ExitShellException {
		final var childID = fork();
		if (childID < 0) {
			throw new ShellError();
		} else if (childID > 0) {
			return childID;
		} else {
			_execv(programCall.program, programCall.args);
			throw new ExitShellException();
		}
	}

	private static void printExit(int i) {
		System.out.printf("Process exited with exit Code %d", i);

	}

	private static programCall getProgramCall(String userInput) throws InvalidInputException {
		// TODO
		return new programCall("/bin/ls", "/");
	}

	public static void _execv(String path, String... args) {
		execv(path, args);
	}

	public static void printWelcome() {
		System.out.println("Welcome to Minimal Shell!");
	}

	public static String input() throws ExitShellException {
		final var scanner = new Scanner(System.in);
		if (scanner.hasNextLine()) {
			return scanner.nextLine();
		} else {
			throw new ExitShellException();
		}
	}

	public static void printCursor() {
		System.out.printf(">_");
	}

}
