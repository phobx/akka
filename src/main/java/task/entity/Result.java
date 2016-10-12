package task.entity;

public class Result {

	private final String key;
	private final double value;

	public Result(String key, double value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public double getValue() {
		return value;
	}

}
