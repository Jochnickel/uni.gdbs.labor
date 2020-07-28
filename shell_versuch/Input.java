import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {

	public static String input(String fQuestion, Object ...fArgs) throws ExitShellException {
		try {
			System.out.printf(fQuestion, fArgs);
			final var scanner = new Scanner(System.in);
			final var answer = scanner.nextLine();
			scanner.close();
			System.out.println("Input.input");
			System.out.println(answer);
			return answer;
		} catch (NoSuchElementException e) {
			throw new ExitShellException();
		}
	}
}
