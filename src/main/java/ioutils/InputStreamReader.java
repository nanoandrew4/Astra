package ioutils;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class InputStreamReader {
	public static List<String> readAsStringList(@NotNull InputStream inputStream) {
		List<String> list = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(inputStream));

		String line;
		try {
			while ((line = reader.readLine()) != null)
				list.add(line);
		} catch (IOException e) {
			System.err.println("Error reading input stream as list");
		}

		return list;
	}
}
