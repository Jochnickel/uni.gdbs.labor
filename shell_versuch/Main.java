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
				final String[] programCall = getProgramArgs(userInput);

				// Execution
				final int childID = _forkAndExec(programCall);
				final var exitCode = _waitpid(childID);
				if (DEBUG) {
					System.out.printf(">>");
					printExit(exitCode);
				}

			} catch (EmptyInputException e) {
			} catch (ProgramNotFoundException e) {
				// we are in child fork
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

	private static int _forkAndExec(String... programCall) throws ExitShellException, ProgramNotFoundException {
		if (DEBUG)
			System.out.printf(">>fork()\n");
		final var childID = fork();
		if (childID < 0) {
			throw new ShellError();
		} else if (childID > 0) {
			return childID;
		} else {

			final var $path = System.getenv("PATH");
			final var $pathArray = $path.split(":");
			for (String pre : $pathArray) {
				_execv(String.format("%s/%s", pre, programCall[0]), programCall);
			}
			_execv(programCall[0], programCall);

			throw new ProgramNotFoundException(String.format("Minmal Shell: %s : command not found\n", programCall[0]));
		}
	}

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		if (DEBUG)
			System.out.printf(">>waitpid(%d,%s,0)\n", childID, returnCode);
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