import static cTools.KernelWrapper.*;

class Main {
  public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {
				printCursor();
				final var userInput = readInput();
				execInput(userInput);
			} catch (ExitShellException e) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void execInput(List<String> userInput) throws Exception {
		// TODO Auto-generated method stub

	}

	private static String[] readInput() throws ExitShellException {
		// TODO Auto-generated method stub
		return null;
	}

	private static void printCursor() {
		System.out.printf(">_");

	}

	private static void printWelcome() {
		System.out.printf("Welcome to the minimal Shell!\n");
	}
}
