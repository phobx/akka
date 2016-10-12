package task.util;

import task.entity.IdAmount;

public class Parser {

	public static IdAmount getIdAmount(String line) {
		String[] splitted = line.split(";");
		int id = Integer.parseInt(splitted[0].trim());
		double amount = Double.parseDouble(splitted[1].trim());
		return new IdAmount(id, amount);
	}

	public static int getId(String line) {
		int id = Integer.parseInt(line.split(";")[0].trim());
		return id;
	}

	public static double getAmount(String line) {
		double amount = Double.parseDouble(line.split(";")[1].trim());
		return amount;
	}

}
