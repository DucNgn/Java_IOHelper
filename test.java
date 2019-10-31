import java.util.*;
import java.io.*;
public class test {
	public static void main(String[] args) {
		File test = new File("test.text");
		IOManager.writeText(test, "This is a test", false);
	}

}
