import static cTools.KernelWrapper.*;

class hello {
	public static void main(String[] args) {
		System.out.println("Hello, World!");
		int forkedPid = fork();
		if (forkedPid < 0) {
			exit(1);
		} else if (forkedPid == 0){
			execv("/bin/sleep", new String[]{"sleep","3"});
			throw new Error();
		} else {
			waitpid(forkedPid,new int[1],0);
			System.out.println("asdasd2");
		}
		exit(0);
	}
}
