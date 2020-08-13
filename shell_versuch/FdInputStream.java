import java.io.IOException;
import java.io.InputStream;

import Dummy.cTools;

public class FdInputStream extends InputStream {

	private final int fd;

	public FdInputStream(int fd) {
		this.fd = fd;
	}

	@Override
	public int read() throws IOException {
		final var buf = new byte[1];
		if (cTools.read(fd, buf, 1) != 1) {
			throw new IOException();
		}
		return buf[0];
	}

}
