import cTools.KernelWrapper;
import java.nio.file.Files;
import java.nio.file.Paths;
public class SimpleCommand{

	private final String[] args;
	private Integer pid;

	public SimpleCommand(String str){
		args = str.split("\\s+");
	}

	public void run(Integer fdIn, Integer fdOut) throws Exception {
		final var execPath = getExecutablePath();
                if (execPath.isBlank()){
			return;
		}
		final var forkedPid = KernelWrapper.fork();
                
		if(forkedPid<0){
			throw new Error();
		} else if (forkedPid>0){
			// i am parent
			pid = forkedPid;
			if (null!=fdIn ) KernelWrapper.close(fdIn);
			if (null!=fdOut) KernelWrapper.close(fdOut);
		} else {
			// i am child
			System.out.println(execPath);
			System.setIn(new FdInputStream());
			System.setOut(new java.io.PrintStream(new FdOutputStream()));
			KernelWrapper.execv(execPath,args);
			throw new Error();
		}

        }
	public void run() throws Exception {
		run(null,null);
	}

	public void join(){
		if(null==pid){
			return;
		}
		KernelWrapper.waitpid(pid,new int[1],0);
	}

	public String getExecutablePath() throws Exception {
		if(args[0].contains("/")){
			/* User put in a specific path */
			final var path = Paths.get(args[0]);
			if(Files.isDirectory(path)){
				throw new Exception();
			} else if(Files.isExecutable(path)){
				return args[0];
			} else {
				throw new Exception("Invalid Executable Path");
			}
		}
		for(var pre : System.getenv("PATH").split(":")){
			final var path = Paths.get(pre,args[0]);
			if (Files.isExecutable(path) && !Files.isDirectory(path)){
				return path.toString();
			}
		}
		throw new Exception("Executable not found");
	}
}
