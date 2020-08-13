import cTools.KernelWrapper;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleCommand{
	private final String[] args;
	private final String overrideIn;
	private final String overrideOut;
	private Integer pid;
	
	public SimpleCommand(String str){
		final var tmp = TinyMatcher(str,">\\s*(\\S+)",">\\s*\\S+");
		overrideOut = tmp.match;
		final var tmp2 = TinyMatcher(tmp.str,"<\\s*(\\S+)","<\\s*\\S+");
		overrideIn = tmp2.match;
		
		args = tmp2.str.trim().split("\\s+");
	}

	public void run(Integer fdIn, Integer fdOut) throws Exception {
		Logging.debug("%s run(1) %s",this, args[0]);
		if(args[0].startsWith("$log=")){
			Logging.LVL = Integer.parseInt(args[0].substring(5));
			return;
		}
		if (args[0].isBlank()){
			return;
		}
                final var execPath = getExecutablePath();
                Logging.debug("%s run(2) %s",this, execPath);
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
			Logging.debug("%s fdIn %d",this,fdIn);
                        Logging.debug("%s fdOut %d",this,fdOut);

			if (null!=overrideIn){
                        	fdIn = KernelWrapper.open(overrideIn,KernelWrapper.O_RDONLY);
				if (fdIn<0) throw new Error();
			}
                        if (null!=overrideOut){
                                fdOut = KernelWrapper.open(overrideOut,KernelWrapper.O_WRONLY);
                        	if (fdOut<0) throw new Error();
			}

                        if (null!=fdIn){
				KernelWrapper.close(KernelWrapper.STDIN_FILENO);
				KernelWrapper.dup2(fdIn, KernelWrapper.STDIN_FILENO);
			}
                        if (null!=fdOut){
				KernelWrapper.close(KernelWrapper.STDOUT_FILENO);
				KernelWrapper.dup2(fdOut, KernelWrapper.STDOUT_FILENO);
			}

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
		throw new CommandNotFoundException(String.format("Command not found: %s",args[0]));
	}
}
