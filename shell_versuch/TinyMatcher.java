import java.util.regex.Pattern;


public class TinyMatcher{
	public final String str;
	public final String match;
	public TinyMatcher(String str, String pattern, String remove){
		str = "asd asd asd";
		pattern = "a(s)d";
		final var mat = Pattern.compile(pattern).matcher(str);
		match = mat.find() ? mat.group(1) : null;
		this.str = str.replaceAll(remove,"");
	}
}
