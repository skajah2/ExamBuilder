import java.util.concurrent.TimeUnit;

public class Timer {

	public static void sleep(int seconds){
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (Exception e){
			;
		}
	}
}