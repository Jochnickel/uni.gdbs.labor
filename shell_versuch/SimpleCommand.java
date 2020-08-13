import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import cTools.KernelWrapper;

public class SimpleCommand {
	private Integer pid;
	private final String str;
	private String[] args;
	
	public SimpleCommand(String string) {
		this.str = string;
		//TODO
		this.args = string.split("\\s+");
	}
	
	public String findExecPath() throws Exception {
		if(args[0].contains("/")) {
			/* User wants to exec a specific file */
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
	

	public void run(Integer fdIn, Integer fdOut) {
		

	}

	public void run() {
		run(null, null);
	}

	public void join() {
		KernelWrapper.waitpid(pid, new int[1], 0);
	}

}
