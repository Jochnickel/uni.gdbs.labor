public class programCall {
	final String programPath;
	final private String[] args;

	public programCall(String programPath, String[] args) {
		this.programPath = programPath;
		this.args = args;
	}

	public String[] getArgs() {
		return args.clone();
	}
}
