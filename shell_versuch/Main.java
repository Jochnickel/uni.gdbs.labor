

class Main {
  public static void main(String[] args) {
		if (1==1) {
		  	throw new IndexOutOfBoundsException();
		}
		final ShellInterface shell = new MinimalShell();
		shell.welcome();
		for(;;) {
			try {
				shell.printCursor();
				final var userInput = shell.userInput();
				shell.execute(userInput);
			} catch (ExitShellException e) {
				break;
			}
		}
	}
}
