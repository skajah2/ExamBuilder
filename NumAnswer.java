import java.util.Scanner;
import java.io.PrintWriter;

public class NumAnswer extends Answer {

	protected double number;

	public NumAnswer(double val){
		this.number = val;
	}

	public NumAnswer(Scanner sc){
		this.number = sc.nextDouble();
	}

	public void print(){
		System.out.println(this.number);
	}


	public double getCredit(Answer right){
		return 0.0;
	}

	public double getCredit(Answer right, double error){
		if (this.number >= ((NumAnswer) right).number-error && 
			this.number <= ((NumAnswer) right).number+error)
			return 1.0;
		return 0.0;
	}

	public void save(PrintWriter pw){
		pw.println(this.number);
	}

	public String toString(){
		return "" + this.number;
	}
}