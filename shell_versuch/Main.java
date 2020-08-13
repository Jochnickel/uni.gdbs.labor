public class Main {

	public static void main(String[] args) {
		printWelcome();
		for (;;) {
			try {

				// GUI
				printPrompt();
				final var userInput = input();

				// Parse
				final var pipeline = new Pipeline("ls /");

				// Execute
				pipeline.run();

				// Wait
				pipeline.join();

			} catch (ExitShellException e) {
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	private static String input() throws Exception {
		// TODO
		System.in.read();
		return "ls -all | cat >";
	}

	private static void printPrompt() {
		System.out.print(">_");
	}

	private static void printWelcome() {
		System.out.println("Welcome to Minimal Shell!");
	}
}
