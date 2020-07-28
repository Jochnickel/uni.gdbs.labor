/**
 * a class to isolate the KernelWrapper
 * 
 * @author Janke
 * 
 */
public class KernelWrapperHandler {

	public static int fork() throws ForkFailedException {
		try {
			final var f = KernelWrapperDummy.fork();
			if (f < 0) {
				throw new ForkFailedException();
			}
			return f;
		} catch (ForkFailedException e) {
			throw e;
		} catch (Exception e) {
			System.err.println("Please implement KernelWrapperHandler.fork");
			e.printStackTrace();
			return 0;
		}
	}

	public static int execv(String[] userInput) {
		try {
			return KernelWrapperDummy.execv(System.getProperty("user.dir"), userInput);
		} catch (Exception e) {
			System.err.println("Please implement KernelWrapperHandler.execv");
			e.printStackTrace();
			return 0;
		}
	}

	public static int waitpid(int myChildOrNull) {
		try {
			return KernelWrapperDummy.waitpid(myChildOrNull, null, 0);
		} catch (Exception e) {
			System.err.println("Please implement KernelWrapperHandler.waitpid");
			e.printStackTrace();
			return 0;
		}
	}

}
