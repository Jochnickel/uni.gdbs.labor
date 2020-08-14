import cTools.KernelWrapper;

public class Pipeline {

	private final SimpleCommand[] commands;

	public Pipeline(String str) {
		final var splitted = str.split("\\|");
		commands = new SimpleCommand[splitted.length];
		for (int i = 0; i < splitted.length; i++) {
			commands[i] = new SimpleCommand(splitted[i]);
		}
	}

	public void run() throws Exception {
		// TODO
		var fdRead = new Integer[commands.length];
		var fdWrite = new Integer[commands.length];
		for (int i = 0; i < commands.length - 1; i++) {
			int[] fd = new int[2];
			if (KernelWrapper.pipe(fd) < 0) {
				throw new Error();
			}
			fdRead[i + 1] = fd[0];
			fdWrite[i] = fd[1];
		}
		for (int i = 0; i < commands.length; i++) {
			final var cmd = commands[i];
			Logging.info("%s running cmd: %s", this, cmd.str);
			try {
				cmd.run(fdRead[i], fdWrite[i]);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void join() {
		// TODO
		for (var cmd : commands) {
			cmd.join();
		}
	}
}
