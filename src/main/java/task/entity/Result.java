package task.entity;

import java.util.Map;

public class Result {

	private final Map<Integer, Double> result;

	public Result(Map<Integer, Double> result) {
		this.result = result;
	}

	public Map<Integer, Double> getResult() {
		return result;
	}

}
