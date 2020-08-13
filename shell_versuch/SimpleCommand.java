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
		final var forkedPid = KernelWrapper.fork();
		if (forkedPid < 0) {
			throw new Error("fork()");
		}
		if (forkedPid > 0) {
			// i am parent
			pid = forkedPid;
			if (null != fdIn) {
				KernelWrapper.close(fdIn);
			}
			if (null != fdOut) {
				KernelWrapper.close(fdOut);
			}
		} else {
			// here is something
			if (null != fdIn) {
				System.setIn(new FdInputStream(fdIn));
			}
			if (null != fdOut) {
				System.setOut(new PrintStream(new FdOutputStream(fdOut)));
			}
			
			try {
				KernelWrapper.execv(findExecPath(), args);
			} catch (Exception e) {
				if (null != fdIn) {
					cTools.close(fdIn);
				}
				if (null != fdOut) {
					KernelWrapper.close(fdOut);
				}
				throw new Error(e);
			}
			throw new Error("How did you get here?");
		}

	}

	public void run() {
		run(null, null);
	}

	public void join() {
		// TODO Auto-generated method stub

	}

}
