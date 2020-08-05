
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import utils.ExitShellException;
import utils.Pipe;
import utils.Pipeline;
import utils.SimpleCommand;

class Main {
	private final static int CHILD = 0;

	public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {
				printPrompt();
				var userInput = input();
				var command = new SimpleCommand(userInput);
				executeSimpleCommand(command, null, null);
			} catch (ExitShellException e) {
				return;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

		}
	}

	private static void executeSimpleCommand(SimpleCommand command, Pipe pipeInput, Pipe pipeOutput) throws Exception {
		final var execPath = command.findExecPath(); //Its important to find the executable before we fork for Error Handling
		final var pid = _fork();
		if (CHILD == pid) {
			attachPipes(pipeInput, pipeOutput);
			attachFileInOut(command.inFileName, command.outFileName);
			cTools.KernelWrapper.execv(execPath, command.args);
			throw new Error("This should never be reached, since the exec exists");
		} else {
			final var childReturnCode = new int[1];
			final var childPID = cTools.KernelWrapper.waitpid(pid, childReturnCode, 0);
			
		}
	}

	private static void attachPipes(Pipe pipeInput, Pipe pipeOutput) {
		if (null != pipeInput) {
			System.setIn(pipeInput.getInputStream());
		}
		if (null != pipeOutput) {
			final var printStream = new PrintStream(pipeOutput.getOutputStream());
			System.setOut(printStream);
		}
	}
	
	private static void attachFileInOut(String inFileName, String outFileName) throws FileNotFoundException {
		if (inFileName!=null) {
			System.setIn(new FileInputStream(inFileName));
		}
		if (outFileName!=null) {
			final var printStream = new PrintStream(new FileOutputStream(outFileName));
			System.setOut(printStream);
		}
	}

	private static int _fork() {
		final var pid = cTools.KernelWrapper.fork();
		if (pid < 0) {
			throw new Error();
		}
		return pid;
	}

	private static void executePipeline(Pipeline pipeline) {
		pipeline.accept(fooForkExec, fooPipe);

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
