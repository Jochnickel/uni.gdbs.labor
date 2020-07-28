import static cTools.KernelWrapper.*;

import cTools.KernelWrapper;
/**
 * a class to isolate the KernelWrapper
 * 
 * @author Janke
 * 
 */
public class KernelWrapperHandler {

	public static int Fork() throws ForkFailedException {
		try {
			final var f = KernelWrapper.fork();
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

	public static int Execv(String[] userInput) {
		try {
			return KernelWrapper.execv(System.getProperty("user.dir"), userInput);
		} catch (Exception e) {
			System.err.println("Please implement KernelWrapperHandler.execv");
			e.printStackTrace();
			return 0;
		}
	}

	public static int Waitpid(int myChildOrNull) {
		try {
			return KernelWrapper.waitpid(myChildOrNull, null, 0);
		} catch (Exception e) {
			System.err.println("Please implement KernelWrapperHandler.waitpid");
			e.printStackTrace();
			return 0;
		}
	}

}
