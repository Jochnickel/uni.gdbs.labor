
import java.io.IOException;
import java.io.OutputStream;

import Dummy.cTools;

public class FdOutputStream extends OutputStream {
	final int fd;

	public FdOutputStream(int fd) {
		this.fd = fd;
	}

	@Override
	public void write(int b) throws IOException {
		final var buf = new byte[1];
		if (cTools.write(fd, buf, 1) != 1) {
			throw new IOException();
		}
	}
}
