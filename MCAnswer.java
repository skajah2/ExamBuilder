import java.util.Scanner;
import java.io.PrintWriter;


public class MCAnswer extends Answer {
	protected String text;
	protected boolean selected;
	protected double creditIfSelected;
	
	protected MCAnswer(String text, double credit){
		this.text = text;
		this.selected = false;
		this.creditIfSelected = credit;
	}
	
	public MCAnswer(Scanner sc){
		String line = "";
		if (sc.hasNextLine())
			line = sc.nextLine().trim();
		int index = line.indexOf(' ');
		double credit = 0.0;
		if (sc.hasNextLine())
			credit = Double.valueOf(line.substring(0, index));
		this.text = line.substring(index + 1);
		this.creditIfSelected = credit;
		this.selected = false;
	}
	
	public void print(){
		String s = this.selected ? " (selected)" : "";
		//System.out.println("In MCAnswer: selected = " + this.selected);
		System.out.println(this.text + s);
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
		//System.out.println("Changed selected");
	}
	
	public double getCredit(Answer rightAnswer){
		MCAnswer right = (MCAnswer) rightAnswer;
		if (this.text.equalsIgnoreCase(right.text))
			return this.creditIfSelected;
		return 0;
	}
	
	public void save(PrintWriter pw){
		pw.println(this.creditIfSelected + " " + this.text);
	}
	
	public String toString(){
		String s = this.selected ? " (selected)" : "";
		return this.text + s;
	}
	
	
}
