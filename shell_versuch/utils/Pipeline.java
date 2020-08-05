package utils;

import java.util.Arrays;

public class Pipeline {
	private final SimpleCommand[] commandList;

	public Pipeline(String pipelineString) {
		commandList = Arrays.stream(pipelineString.split("|")).toArray(SimpleCommand[]::new);
	}
	
}
