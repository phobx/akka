package task.util;

import task.entity.IdAmount;

public class Parser {

	public static IdAmount getIdAmount(String line) {
		int id = Integer.parseInt(line.split(";")[0].trim());
		double amount = Double.parseDouble(line.split(";")[1].trim());
		return new IdAmount(id, amount);
	}

}
