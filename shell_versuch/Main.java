import static cTools.KernelWrapper.*;

import java.util.Scanner;

class Main {
	final static boolean DEBUG = true;

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
				break;
			} catch (ShellError e) {
				e.printStackTrace();
				return;
			}
		}
		exit(0);
	}

	public static void _execv(String path, String... args) {
		if (DEBUG) System.out.printf("execv(%s,[%s])\n",String.join(",", args));
		execv(path, args);
	}

	private static int _forkAndExec(programCall programCall) throws ExitShellException {
		if (DEBUG) System.out.printf("fork()\n");
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

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		if (DEBUG) System.out.printf("waitpid(%d,%s,0)\n",childID, returnCode);
		waitpid(childID, returnCode, 0); // returns int
		return returnCode[0];
	}

	private static programCall getProgramCall(String userInput) throws EmptyInputException {
		final var strs = userInput.split("\\s+", 2);
		if(strs.length<1) {
			throw new EmptyInputException();
		} else if(strs.length<2) {
			return new programCall(strs[0]);
		} else {
			return new programCall(strs[0],strs[1].split("\\s+"));
		}
	}

	public static String input() throws ExitShellException {
		final var scanner = new Scanner(System.in);
		if (scanner.hasNextLine()) {
			final var answ = scanner.nextLine();
			scanner.close();
			return answ;
		} else {
			scanner.close();
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
