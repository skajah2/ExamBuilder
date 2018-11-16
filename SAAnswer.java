import java.io.PrintWriter;
import java.util.Scanner;

public class SAAnswer extends Answer {
	
	protected String text;
	
	public SAAnswer(String text){
		this.text = text;
	}
	
	public SAAnswer(Scanner sc){
		this.text = sc.nextLine().trim();
	}
	public void print(){
		System.out.println(this.text);
	}
	
	public void save(PrintWriter pw){
		pw.println(this.text);
	}
	
	
	public double getCredit(Answer rightAnswer){
		SAAnswer right = (SAAnswer) rightAnswer;
		if (right.text.equalsIgnoreCase(this.text))
			return 1;
		return 0;
	}
	
	public String toString(){
		return this.text;
	}
}
