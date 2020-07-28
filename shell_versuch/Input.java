import java.util.Scanner;

public class Input {

	public static Object input(String fQuestion, Object ...fArgs) {
		final var scanner = new Scanner(System.in);
		System.out.printf(fQuestion, fArgs);
		final var answer = scanner.nextLine();
		scanner.close();
		System.out.println("Input.input");
		System.out.println(answer);
		return answer;
	}
}
