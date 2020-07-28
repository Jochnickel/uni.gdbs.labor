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
				final var programCall = getProgramArgs(userInput);

				// Execution
				final int childID = _forkAndExec(programCall);
				final var exitCode = _waitpid(childID);
				if (DEBUG)
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
			System.out.printf("> execv(%s,%s =[%s])\n", path,args, String.join(",", args));
		return execv(path, args);
	}

	private static int _forkAndExec(String ...params) throws ExitShellException {
		if (DEBUG)
			System.out.printf("> fork()\n");
		final var childID = fork();
		if (childID < 0) {
			throw new ShellError();
		} else if (childID > 0) {
			return childID;
		} else {
			final var retCode = _execv(params[0], params);
			// normally this fork should stop before this point.
			// if it didnt, were still in the shell
			if (DEBUG)
				System.out.printf("> retCode %d\n", retCode);
			System.err.printf("Minmal Shell: %s : command not found\n", programCall.program);

			throw new ExitShellException();
		}
	}

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		if (DEBUG)
			System.out.printf("> waitpid(%d,%s,0)\n", childID, returnCode);
		waitpid(childID, returnCode, 0); // returns int
		return returnCode[0];
	}

	private static String[] getProgramArgs(String userInput) throws EmptyInputException {
		final var strs = userInput.split("\\s+"); // returns array>0
		if (strs[0].isBlank()) {
			throw new EmptyInputException();
		} else {
			return strs;
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
		System.out.printf("Program exited with exit Code %d\n", i);

	}

	public static void printWelcome() {
		System.out.println("Welcome to Minimal Shell!");
	}

}
