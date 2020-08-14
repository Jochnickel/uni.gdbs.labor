import java.util.regex.Pattern;

public class TinyMatcher {
	public final String str;
	public final String match;
	public final boolean hasMatch;

	public TinyMatcher(final String str,final String pattern,final String remove) {
		final var mat = Pattern.compile(pattern).matcher(str);
		this.hasMatch = mat.find();
		match = hasMatch ? mat.group(1) : null;
		this.str = str.replaceAll(remove, "");
		Logging.debug("%s %s %s", this, this.str, this.match);
	}
}
