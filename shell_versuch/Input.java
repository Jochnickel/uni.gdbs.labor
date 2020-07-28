import java.util.Scanner;

public class Input {

	public static String read() throws ExitShellException {
		final var scanner = new Scanner(System.in);
		if(scanner.hasNextLine()) {
			final var str = scanner.nextLine();
			return str;
		} else {
			throw new ExitShellException();
		}
	}

}
