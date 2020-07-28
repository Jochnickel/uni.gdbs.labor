import static cTools.KernelWrapper.*;

import java.util.Scanner;

class Main {
	final static boolean DEBUG = true;
	final static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {
				// UI
				printCursor();
				final var userInput = input();
				final var programCall = getProgramCall(userInput);

				// Execution
				final int childID = _forkAndExec(programCall);
				final var exitCode = _waitpid(childID);
				printExit(exitCode);

			} catch (EmptyInputException e) {
			} catch (ExitShellException e) {
				scanner.close();
				exit(0);
				break;
			} catch (ShellError e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public static int _execv(String path, String... args) {
		if (DEBUG)
			System.out.printf("> execv(%s,[%s])\n", path, String.join(",", args));
		return execv(path, args);
	}

	private static int _forkAndExec(programCall programCall) throws ExitShellException {
		if (DEBUG)
			System.out.printf("> fork()\n");
		final var childID = fork();
		if (childID < 0) {
			throw new ShellError();
		} else if (childID > 0) {
			return childID;
		} else {
			final var retCode = _execv(programCall.program, programCall.args);
			//normally the fork should stop before this point.
			if (DEBUG)
				System.out.printf("> retCode %d\n", retCode);
			System.err.printf("Minmal Shell: %s : command not found", programCall.program);
			exit(retCode);
			throw new ShellError();
		}
	}

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		if (DEBUG)
			System.out.printf("> waitpid(%d,%s,0)\n", childID, returnCode);
		waitpid(childID, returnCode, 0); // returns int
		return returnCode[0];
	}

	private static programCall getProgramCall(String userInput) throws EmptyInputException {
		final var strs = userInput.split("\\s+", 2); // returns array>0
		final var prName = strs[0];
		if (prName.isBlank()) {
			throw new EmptyInputException();
		} else if (strs.length < 2) {
			return new programCall(prName);
		} else {
			return new programCall(prName, strs[1].split("\\s+"));
		}
	}

	public static String input() throws ExitShellException {
		if (scanner.hasNextLine()) {
			final var answ = scanner.nextLine();
			return answ;
		} else {
			throw new ExitShellException();
		}
	}

	public static void printCursor() {
		System.out.printf(">_");
	}

	private static void printExit(int i) {
		System.out.printf("Process exited with exit Code %d\n", i);

	}

	public static void printWelcome() {
		System.out.println("Welcome to Minimal Shell!");
	}

}
