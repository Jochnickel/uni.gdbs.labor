import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramCall {
	final String programPath;
	final private String[] args;

	public ProgramCall(String[] args) throws CommandNotFoundException{
		if (args[0].startsWith("/")) {
			this.programPath = args[0];
		} else {
			final var $path = System.getenv("PATH");
			final var $pathArray = $path.split(":");
			for (String dir : $pathArray) {
				final var fullPath = dir+"/"+args[0];
				System.out.println(fullPath);
				if(Files.isExecutable(Paths.get(fullPath))) {
					this.programPath = args[0];
					break;
				}
			}
			throw new CommandNotFoundException(args[0]);
		}
		this.args = args;
		
	}

	public String[] getArgs() {
		return args.clone();
	}
	
	public String getArgument(int i) {
		return args[i];
	}
}
