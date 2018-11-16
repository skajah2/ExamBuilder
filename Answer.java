import java.util.Scanner;
import java.io.PrintWriter;


public abstract class Answer {
	
	protected Answer(){
		;
	}
	
	public Answer(Scanner sc){
		;
	}
	
	public abstract void print();
	
	public abstract double getCredit(Answer rightAnswer);
	
	public abstract void save(PrintWriter pw);
	
}
