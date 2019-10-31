package IOHelper;

import java.io.*;
import java.util.*;

/**
 * @author DUKE_NGN
 * A helper class to handle I/O needs quickly, and improve Java work flow.
 */

public class IOManager {
	private PrintWriter        pw;
	private boolean            statSC;

	private Scanner            sc;
	private boolean            statPW;

	private ObjectOutputStream oos;
	private boolean            statOOS;

	private ObjectInputStream  ois;
	private boolean            statOIS;

	/**
	 * @GETTERS
	 * @SETTERS
	 */
	public PrintWriter getPw() {
		return pw;
	}
	public void setPw(PrintWriter pw) {
		this.pw  = pw;
		statPW   = true;
	}
	public Scanner getSc() {
		return sc;
	}
	public void setSc(Scanner sc) {
		this.sc  = sc;
		statSC   = true;
	}
	public ObjectOutputStream getOos() {
		return oos;
	}
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
		statOOS  = true;
	}
	public ObjectInputStream getOis() {
		return ois;
	}
	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
		statOIS  = true;
	}
	////////////////

	//Initialize status and object stream
	public IOManager() {
		sc      = null;
		statSC  = false;
		pw      = null;
		statPW  = false;
		oos     = null;
		statOOS = false;
		ois     = null;
		statOIS = false;
	}

	/**
	 * Setup both Print Writer and Scanner
	 */
	public void setupPW_SC(File out, Boolean append, File in) {
		try {
			this.setupSC(in);
			this.setupPW(out, append);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Setup Print Writer with target file and status for writing file
	 */
	public void setupPW(File out, boolean append) throws RetrictedAccess {
		if(out.canWrite()) {
			try {
				pw     = new PrintWriter(new FileOutputStream(out, append));
				statPW = true;
			}
			catch (FileNotFoundException e) {
				System.out.println("Cannot find/create file");
			}
		} else {
			throw new RetrictedAccess("write");
		}
	}

	/**
	 * Write text to a file
	 * @param input
	 */
	public void writeText(String input) throws StreamNotSetUp {
		if (statPW) {
			pw.println(input);
		} else {
			throw new StreamNotSetUp("PW");
		}
	}
	
	/**
	 * Quickly write text to a file without manually opening a stream
	 * @param target 
	 * @param content
	 * @param append 
	 */
	public static void writeText(File target, String content, boolean append) {
		try {
			IOManager temp = new IOManager();
			temp.setupPW(target, append);
			temp.writeText(content);
			temp.closeAll();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	//SCANNER OPERATIONS:
	/**
	 * Setup a Scanner for reading file
	 * @param File in 
	 */
	public void setupSC(File in) throws RetrictedAccess {
		if(in.canRead()){
			try {
				this.sc = new Scanner(new FileInputStream(in));
				this.statSC = true;
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			throw new RetrictedAccess("read");
		}
	}
	
	/**
	 * Setup Scanner for user input
	 */
	public void setupSC_forInput() {
		sc = new Scanner(System.in);
	}

	/**
	 * Read text from the already setup Scanner stream 
	 * @return a String
	 */
	public String readAllTextSC() throws StreamNotSetUp {
		if(statSC) {
			String content = "";
			while(sc.hasNextLine()) {
				content = content + sc.nextLine() + "\n";
			}
			return content;
		} else {
			throw new StreamNotSetUp("SC");
		}
	}
	
	/**
	 * Quickly read text from a file without opening a new Stream manually
	 * @param fNameIn
	 * @return String
	 */
	public static String readAllTextFrom(File in) {
		try {
			IOManager temp = new IOManager();
			temp.setupSC(in);
			String content = temp.readAllTextSC();
			temp.closeAll();
			return content;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Copy all the content from one file and write to the other desired file
	 * @param File out 
	 * @param append
	 * @param File in 
	 */
	public static void copyToNewFile(File out,  boolean append, File in) {
		try {
			IOManager temp = new IOManager();
			temp.setupPW_SC(out, append, in);
			temp.writeText(temp.readAllTextSC());
			temp.closeAll();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

    // OBJECT I/O
	public void setupOOS_OIS(File out, Boolean append, File in) {
		try {
			this.setupOOS(out, append);
			this.setupOIS(in);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.getStackTrace();
			System.exit(0);
		}
	}

	public void setupOOS(File out, boolean append) throws RetrictedAccess {
		if(out.canWrite()) {
		 try {
			 oos = new ObjectOutputStream(new FileOutputStream(out, append));
			 statOOS = true;
		 } catch (FileNotFoundException e) {
			 System.out.println("Cannot find/create file");
		 } catch (IOException e1) {
			 System.out.println("Cannot write to file");
		 }
		} else {
			throw new RetrictedAccess("write");
		} 
	}

	public void writeObject(Object obj) throws StreamNotSetUp, IOException {
		if(statOOS) {
			if (obj instanceof Serializable) {
				oos.writeObject(obj); 
			} else {
				System.out.println("Object has not implement Serializable class.");
				System.exit(0);
			}
		} else {
			throw new StreamNotSetUp("oos");
		}
	}

	public void setupOIS(File in) throws RetrictedAccess {
		if (in.canRead()) {
		 try {
			 ois = new ObjectInputStream(new FileInputStream(in));
			 statOIS = true;
		 } catch (FileNotFoundException e) {
			 System.out.println("Cannot find file");
		 } catch (IOException e1) {
			 System.out.println("Cannot write to file");
		 }
		} else {
			throw new RetrictedAccess("read");
		}
	}



	/**
	 * CLOSE ALL THE STREAM <IF HAS BEEN OPENED>
	 */
	public void closeAll() {
		if(statPW) {
			pw.close();
		}
		if(statSC) {
			sc.close();
		}

		try {
			if(statOIS) {
				ois.close();
			}
			if(statOOS) {
				oos.close();
			}
		} catch (IOException e) {
			System.out.println("Cannot close stream(s)");
			System.exit(0);
		}
	}
	
	// EXCEPTION CLASSES
	public static class StreamNotSetUp extends Exception {
		public StreamNotSetUp(String gate) {
			super("Stream " + gate + " has not been setup.");
		}
	}

	public static class RetrictedAccess extends Exception {
		public RetrictedAccess(String access) {
			super("Access to " + access + " has not been granted. ");
		}
	}

}
