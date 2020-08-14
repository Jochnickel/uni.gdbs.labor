public class Main {
	public static java.nio.file.Path dir = java.nio.file.Paths.get("").toAbsolutePath();

	public static void main(String... args) {
//		Logging.LVL = 0;
//		Logging.debug(args.length);
		printWelcome();
		for (;;)
			try {
				// GUI
				printPrompt();
				final var userInput = input();

				// Filter
				final var cmd = treatUserInput(userInput);

				// Parse
				final var pipeline = new Pipeline(cmd);

				// Exec
				pipeline.run();

				// Wait
				pipeline.join();

			} catch (ExitShellException e) {
				return;
			} catch (CommandNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public static void printWelcome() {
		System.out.println("Welcome to Minimal Shell! (type help)");
	}

	public static void printPrompt() {
		System.out.printf("%s>_", dir);
	}

	public static String treatUserInput(String inp) throws Exception {
		return inp.strip().replace("$$", String.valueOf(ProcessHandle.current().pid()))
				.replace("$..", dir.resolve("..").toAbsolutePath().toString())
				.replace("$.", dir.toAbsolutePath().toString());
		// return inp;

	}

	public static String input() throws ExitShellException {
		var scanner = new java.util.Scanner(System.in);
		if (scanner.hasNextLine()) {
			return scanner.nextLine();
		} else {
			throw new ExitShellException();
		}
	}

}
