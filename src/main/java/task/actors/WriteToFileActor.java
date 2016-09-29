package task.actors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import akka.actor.UntypedActor;
import task.entity.Result;
import task.util.GenerateFile;

public class WriteToFileActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Result) {
			((Result) message).getResult().forEach((id, amount) -> {
				try {
					Files.write(Paths.get(GenerateFile.DESTINATION_FILENAME), idAndAmountToBytes(id, amount), StandardOpenOption.APPEND,
							StandardOpenOption.CREATE);
				} catch (IOException e) {
					System.out.println("Failed to write to file.");
				}
			});
		}

	}

	private static byte[] idAndAmountToBytes(Integer id, Double amount) {
		return new StringBuilder().append(id).append(';').append(amount).toString().getBytes();
	}

}
