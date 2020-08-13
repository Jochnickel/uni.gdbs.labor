public class Main {

	public static void main(String... args){
		printWelcome();
		for(;;) try{
			// GUI
			printPrompt();
			final var userInput = input();
			
			// Parse
			final var pipeline = new Pipeline(userInput);

			// Exec
			pipeline.run();

			// Wait
			pipeline.join();
			
		} catch (ExitShellException e){
			return;
		} catch (CommandNotFoundException e){
			System.out.println(e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void printWelcome(){
		System.out.println("Welcome to Minimal Shell!");
	}
	public static void printPrompt(){
		System.out.print(">_");
	}
	public static String input() throws ExitShellException{
		var scanner = new java.util.Scanner(System.in);
		if(scanner.hasNextLine()){
			return scanner.nextLine();
		} else {
			throw new ExitShellException();
		}
	}

}
