package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pipe {
	private final int[] pipe = new int[2];
	private final InputStream in;
	private final OutputStream out;

	public Pipe() {

		if (cTools.KernelWrapper.pipe(this.pipe) < 0) {
			throw new Error();
		}

		this.in = new InputStream() {
			@Override
			public int read() throws IOException {
				final var buf = new byte[1];
				if (cTools.KernelWrapper.read(pipe[0], buf, 1) < 0) {
					throw new IOException();
				}
				return buf[0];
			}
		};
		
		this.out = new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				cTools.KernelWrapper.write(pipe[1], new byte[] {(byte) b}, 1);
			}
		};
	}

	public InputStream getInputStream() {
		cTools.KernelWrapper.close(pipe[1]);
		return in;
	}
	
	public OutputStream getOutputStream() {
		cTools.KernelWrapper.close(pipe[0]);
		return out;
	}

}
