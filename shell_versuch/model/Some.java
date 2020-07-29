package model;



public class Some {
	final private Some2[] asd;
	
	public Some(String command) {
		final var a = command.split(";");
		asd = new Some2[a.length];
		for (int i = 0; i < a.length; i++) {
			asd[i] = new Some2(a[i]);
		}
	}
}
