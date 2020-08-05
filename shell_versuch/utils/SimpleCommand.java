package utils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleCommand {
	public final String[] args;
	public final String inFileName;
	public final String outFileName;

	public SimpleCommand(String cmdString) throws Exception {

		outFileName = findLast(cmdString, ">\\s*(\\S+)");
		cmdString = cmdString.replace(">\\s*\\S+", "");

		inFileName = findLast(cmdString, "<\\s*(\\S+)");
		cmdString = cmdString.replace("<\\s*\\S+", "");

		args = cmdString.split("\\s+");
	}

	private String findLast(String str, String groupedRegex) {
		final var mat = java.util.regex.Pattern.compile(groupedRegex).matcher(str);
		String out = null;
		while (mat.find()) {
			out = mat.group(1);
		}
		return out;
	}

	public String findExecPath() throws Exception {
		if(args[0].contains("/")) {
			if (Files.isExecutable(Paths.get(args[0]))) {
				return args[0];
			} else {
				throw new Exception("No such file");
			}
		}
		for (String dir : System.getenv("PATH").split(":")) {
			final var path = Paths.get(dir,args[0]);
			if(Files.isExecutable(path)) {
				return path.toAbsolutePath().toString();
			}
		}
		throw new Exception("Command not found");
	}

}
