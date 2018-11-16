import java.util.Scanner;

public class ScannerFactory {
	
	private static Scanner keyboardScanner = null;
	
	public static Scanner getKeyboardScanner() {
		if (keyboardScanner == null)
			keyboardScanner = new Scanner(System.in);
		return keyboardScanner;
	}
	
	public void finalize(){
		keyboardScanner.close();
		keyboardScanner = null;
	}
}
