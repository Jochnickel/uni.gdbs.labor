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
				if (DEBUG) {
					System.out.printf(">>");
					printExit(exitCode);
				}

			} catch (EmptyInputException e) {
			} catch (ProgramNotFoundException e) {
				System.err.print(e.getMessage());
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
			System.out.printf(">>execv(%s,%s =[%s])\n", path, args, String.join(",", args));
		return execv(path, args);
	}

	private static int _forkAndExec(programCall programCall) throws ExitShellException {
		if (DEBUG)
			System.out.printf(">>fork()\n");
		final var childID = fork();
		if (childID < 0) {
			throw new ShellError();
		} else if (childID >>0) {
			return childID;
		} else {
			final var retCode = _execv(programCall.programPath, programCall.getArgs());
			// normally this fork should stop before this point.
			// if it didnt, were still in the shell
			if (DEBUG)
				System.out.printf(">>retCode %d\n", retCode);
			System.err.printf("Minmal Shell: %s : command not found\n", programCall.getArgs()[0]);
			throw new ExitShellException();
		}
	}

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		if (DEBUG)
			System.out.printf(">>waitpid(%d,%s,0)\n", childID, returnCode);
		waitpid(childID, returnCode, 0); // returns int
		return returnCode[0];
	}

	private static programCall getProgramArgs(String userInput) throws EmptyInputException, ProgramNotFoundException {
		final var strs = userInput.split("\\s+"); // returns array>0
		if (strs[0].isBlank()) {
			throw new EmptyInputException();
		} else if (false) {
			throw new ProgramNotFoundException(String.format("Minimal Shell: %s : command not found\n",strs[0]));
		} else {
			return new programCall("/bin/"+strs[0], strs);
			//TODO
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
