public class CommandNotFoundException extends Exception {
	public CommandNotFoundException(){
		super();
	}
	public CommandNotFoundException(String s){
                super(s);
        }
        public CommandNotFoundException(Throwable e){
                super(e);
        }

}
