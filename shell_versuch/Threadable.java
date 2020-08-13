import java.util.function.Consumer;
public interface Threadable{
	
	public run();
	public tryRun(Consumer<? extends Exception> ...catchRoutine);
	public join();
}
