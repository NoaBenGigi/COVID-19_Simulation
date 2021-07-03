package Exceptions;

import java.io.FileNotFoundException;

public class FileNotFound extends Exception{

	public FileNotFound(String file) {
		super(file);
	}
		

}
