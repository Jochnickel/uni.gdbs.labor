
public interface ShellInterface {

	public void execute(String ...r);

	public String[] userInput() throws ExitShellException;

	public void printCursor();

	public void welcome();
}
