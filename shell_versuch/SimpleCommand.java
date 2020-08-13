import cTools.KernelWrapper;

public class SimpleCommand {
	private Integer pid;
	private final String[] args = new String[] { "ls", "/", null };
	private final String execPath = "/bin/ls";

	public SimpleCommand(String string) {
//		this.args = string.split("\\s+");
	}

	public void run(Integer fdIn, Integer fdOut) {
		System.out.println("asd");
		final var forkedPid = KernelWrapper.fork();
		if (forkedPid < 0) {
			throw new Error();
		}
		if (forkedPid > 0) {
			pid = forkedPid;
		} else {
			KernelWrapper.execv("/bin/ls", new String[] { "ls", "/", null });
			System.exit(1);
		}
	}

	public void run() {
		run(null, null);
	}

	public void join() {
		KernelWrapper.waitpid(pid, new int[1], 0);
	}

}
