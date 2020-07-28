package de.jj22.uni.gdbs.labor;

public class MinimalShell {
	public static void main(String[] args) {
		printWelcome();
		for(;;) {
			try {
				printCursor();
				final var userInput = readInput();
				execInput(userInput);
			} catch (ExitShellException e) {
				break;
			}
		}
	}

	private static void printCursor() {
		// TODO Auto-generated method stub
		
	}

	private static void printWelcome() {
		// TODO Auto-generated method stub
		
	}

	private static Object readInput() throws ExitShellException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
