
public class Pipeline{
	
	private final SimpleCommand[] commands;
	public Pipeline(String str){
		final var splitted = str.split("\\|");
		commands = new SimpleCommand[splitted.length];
		for(int i=0;i<splitted.length;i++){
			commands[i] = new SimpleCommand(splitted[i]);
		}
	}

	public void run() throws Exception{
		//TODO
		for(var cmd : commands){
			cmd.run();
		}
	}

	public void join(){
		//TODO
		for(var cmd : commands){
			cmd.join();
		}
	}
}
