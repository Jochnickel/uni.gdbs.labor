import cTools.KernelWrapper;
import java.util.LinkedList;
import java.util.Scanner;

public class Tail {

	private final static String STDIN = "-";

	public static void main(final String[] args) {
		try {
			var amt = -10;
			final var filenames = new LinkedList<String>();
			for (int i = 0; i < args.length; i++) {

				if (args[i].matches("-n[+-]?\\d*")) {
					amt = handleMinusN(args, i);

				} else if ("--help".equals(args[i])) {
					printHelp();
					throw new RuntimeException();

				} else if (args[i].isBlank()) {

				} else {
					filenames.add(args[i]);

				}
			}

			if (filenames.size() < 1) {
				tailSingle(amt, STDIN);

			} else if (filenames.size() < 2) {
				tailSingle(amt, filenames.element());

			} else {
				tailAll(amt, filenames);

			}
		} catch (Error e) {
			System.err.println(e.getMessage());
			KernelWrapper.exit(1);

		} catch (RuntimeException e) {
			KernelWrapper.exit(0);

		}
		KernelWrapper.exit(0);
	}

	private static void tailStdIn(int amt) {
		final var scanner = new Scanner(System.in);
		if (amt >= 0) {
			// skip first n lines
			try {
				for (; amt > 1; amt--) {// start at 1st line
					scanner.nextLine();
				}
				for (;;) {
					System.out.println(scanner.nextLine());
				}
			} catch (Exception e) {
			}
		} else {
			// repeat last n lines
			final var list = new LinkedList<String>();
			try {
				for (; list.size() < -amt;) {
					list.add(scanner.nextLine());
				}
				for (;; list.removeFirst()) {
					list.add(scanner.nextLine());
				}
			} catch (Exception e) {
			}
			for (final var str : list) {
				System.out.println(str);
			}
		}
		scanner.close();
	}

	/**
	 * moves the seeker until n newlines have been passed The pointer is points like
	 * we just read a byte
	 * 
	 * 
	 * @param amt The amount of new lines until we return. Negative = backwards
	 * @param fd
	 */
	private static void seekNNewLines(int amt, final int fd) {
		for (; amt != 0;) {
			final var b = new byte[1];

			if (amt < 0) {
				KernelWrapper.lseek(fd, -2, KernelWrapper.SEEK_CUR);
			}
			final var readBytes = KernelWrapper.read(fd, b, 1);

			if (readBytes < 0) {
				throw new Error();

			} else if (readBytes < 1) {
				return;
			}

			if ('\n' == b[0]) {
				if (amt > 0) {
					amt--;
				} else if (amt < 0) {
					amt++;
				}
			}
		}
	}

	/**
	 * Tailing a single file (or stdin) for n lines
	 * 
	 * @param amt
	 * @param filename
	 */
	private static void tailSingle(final int amt, final String filename) {
		if (STDIN.equals(filename)) {
			tailStdIn(amt);
		} else {
			final var fd = KernelWrapper.open(filename, KernelWrapper.O_RDONLY);
			if (fd < 0) {
				throw new Error(String.format("cannot open '%s' for reading: No such file or directory", filename));
			}
			final var fileSize = KernelWrapper.lseek(fd, 0, KernelWrapper.SEEK_END);
			if (amt > 0) {
				KernelWrapper.lseek(fd, 0, KernelWrapper.SEEK_SET);
			}
			seekNNewLines(amt, fd);
			final var somewhere = KernelWrapper.lseek(fd, 0, KernelWrapper.SEEK_CUR);
			final var strLen = fileSize - somewhere;
			final var buf = new byte[strLen];
			if (KernelWrapper.read(fd, buf, strLen) != strLen) {
				throw new Error();
			}
			System.out.print(new String(buf));
		}
	}

	/**
	 * Tailing a list of files for n lines
	 * 
	 * @param amt
	 * @param filenames
	 */
	private static void tailAll(int amt, final LinkedList<String> filenames) {
		for (final var fn : filenames) {
			System.out.printf("==> %s <==", STDIN.equals(fn) ? "standard input" : fn);
			tailSingle(amt, fn);
		}
	}

	/**
	 * Handles an -n on the incoming arguments
	 * 
	 * 
	 * @param args
	 * @param index
	 * @return
	 */
	private static int handleMinusN(final String[] args, final int index) {
		try {
			final var value = args[index + 1].matches("\\d.*") ? "-" + args[index + 1] : args[index + 1];
			if ("-0".equals(value)) {
				throw new RuntimeException();
			}
			args[index] = "";
			args[index + 1] = "";
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			throw new Error(String.format("invalid number of lines: '%s'\n", args[index + 1]));
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new Error("option requires an argument -- 'n'\nTry 'tail --help' for more information.");
		}
	}

	public static void printHelp() {
		System.out.println("Tail help!\n" + " tail asd.txt : print last 10 lines of asd.txt\n"
				+ " tail asd.txt -n 3 : print last 3 lines\n" + " tail asd.txt -n +3 : print everything from line 3\n"
				+ " tail " + STDIN + " : waiting for filenames on StdIn \n" + " tail --help : print this help\n");
	}
}
