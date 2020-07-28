import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramCall {
	final String programPath;
	final private String[] args;

	public ProgramCall(String[] args) throws CommandNotFoundException {
		if (args[0].startsWith("/")) {
			this.programPath = args[0];
		} else {

			for (String dir : System.getenv("PATH").split(":")) {
				final var fullPath = dir + "/" + args[0];

				if (Files.isExecutable(Paths.get(fullPath))) {
					this.programPath = args[0];
					this.args = args;
					return;
				}
			}

		}

		throw new CommandNotFoundException(args[0]);
	}

	public String[] getArgs() {
		return args.clone();
	}

	public String getArgument(int i) {
		return args[i];
	}
}
