import static cTools.KernelWrapper.*;

class MinimalShell implements ShellInterface{

	@Override
	public void execute(String... r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] userInput() throws ExitShellException {
		final var str = Input.input("");
		final var strings = str.split("\\s+");
		return strings;
	}

	@Override
	public void printCursor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void welcome() {
		// TODO Auto-generated method stub
		
	}

	
}