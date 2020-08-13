import com.mtsystems.coot.NativeHelper;
import com.mtsystems.coot.String8;

public class Unistd {
	static {
		// The online demo uses the example library "libc.so.6".
		// Have a look at the translated programs for real values.
		NativeHelper h = NativeHelper.get("libc.so.6");
		h.bindDirect(Unistd.class);
	}

	/**
	 * Change the process's working directory to PATH.
	 */
	public static native int chdir(String8 path);
}
