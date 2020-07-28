
public class CommandNotFoundException extends Exception {

	public CommandNotFoundException(String programName) {
		super(String.format("%s : command not found", programName));
	}

	private static final long serialVersionUID = 1L;

}
