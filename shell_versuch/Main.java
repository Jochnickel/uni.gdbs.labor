import static cTools.KernelWrapper.*;

import java.util.Scanner;

class Main {
	final static boolean DEBUG = false;
	final static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
//		System.setOut(System.err); // otherwise have unordered prints
//		System.setErr(System.out); // otherwise have unordered prints
		printWelcome();
		for (;;) {
			try {
				// UI
				printCursor();
				final var userInput = input();
				final var programCall = getProgramArgs(userInput);

				if (DEBUG)
					System.out.printf(">>programCall: %s\n",programCall);

				// Execution
				final int childID = _forkAndExec(programCall);
				final var exitCode = _waitpid(childID);

				if (DEBUG) {
					System.out.printf(">>");
					printExit(exitCode);
				}
				

			} catch (EmptyInputException e) {
			} catch (CommandNotFoundException e) {
				System.err.printf("Minimal Shell: %s\n", e.getMessage());
			} catch (ExitShellException e) {
				scanner.close();
				exit(0);
				break;
			} catch (ShellError e) {
				System.err.println("ShellError");
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

	private static int _forkAndExec(ProgramCall programCall) throws ExitShellException, CommandNotFoundException {
		if (DEBUG)
			System.out.printf(">>fork()\n");
		final var childID = fork();
		if (childID < 0) {
			throw new ShellError();
		} else if (childID > 0) {
			return childID;
		} else {
			_execv(programCall.programPath, programCall.getArgs());
			throw new CommandNotFoundException(programCall.getArgument(0));
		}
	}

	private static int _waitpid(int childID) {
		final var returnCode = new int[1];
		waitpid(childID, returnCode, 0); // returns int
		return returnCode[0];
	}

	private static ProgramCall getProgramArgs(String userInput) throws EmptyInputException, CommandNotFoundException {
		final var strs = userInput.split("\\s+"); // returns array>0
		if (strs[0].isBlank()) {
			throw new EmptyInputException();
		} else {
			return new ProgramCall(strs);
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
		System.out.printf("Program exited with exit code %d\n", i);

	}

	public static void printWelcome() {
		System.out.println("Welcome to Minimal Shell!");
	}

}
