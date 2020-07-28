
public class ProgramNotFoundException extends Exception {
	public static ProgramNotFoundException f(String string, Object... fArgs) {
		return new ProgramNotFoundException(String.format(string, fArgs));
	}

	public ProgramNotFoundException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
