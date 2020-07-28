public class programCall {
	final String programPath;
	final private String[] args;

	public programCall(String[] args, String programPath) {
		this.programPath = programPath;
		this.args = args;
	}

	public String[] getArgs() {
		return args.clone();
	}
}
