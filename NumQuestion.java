import java.io.PrintWriter;
import java.util.Scanner;

public class NumQuestion extends Question {
	
	private double error;

	public NumQuestion(String text, double maxValue, double error){
		super(text, maxValue);
		this.error = error;
	}
	
	public NumQuestion(Scanner sc){
		super(sc);
		this.setRightAnswer(new NumAnswer(sc));
	}

	public void print(){
		System.out.println(this.text);
		if (this.studentAnswer == null)
			System.out.println("*null*");
		else 
			this.studentAnswer.print();
	}
	
	public String toString() {
		String result = this.text+'\n';
		if (this.studentAnswer == null)
			result += "*null*\n";
		else 
			result += this.studentAnswer.toString() +'\n';
		return result;
	}
	
	public void finalize(){
		this.studentAnswer = null;
		this.rightAnswer = null;
		this.text = null;
	}
	public void save(PrintWriter pw){
		pw.println("NumQuestion\n" + this.maxValue + "\n" + this.text + '\n' +
			this.rightAnswer);
		((NumAnswer) this.rightAnswer).save(pw);
		pw.println(this.error);
	}
	
	public void saveStudentAnswers(PrintWriter pw){
		pw.println("NumAnswer");
		if (this.studentAnswer == null)
			pw.println("*null*");
		else
			pw.println(this.studentAnswer.toString());
	}
	
	public void restoreStudentAnswers(Scanner sc){
		String d = sc.nextLine().trim();
		try {
			this.studentAnswer = new NumAnswer(Double.parseDouble(d));
		} catch (Exception e) {
			;
		}
	}
	
	public Answer getNewAnswer(){
		return null;
	}
	
	public void getAnswerFromStudent(){
		String s = ScannerFactory.getKeyboardScanner().nextLine().trim();
		if (s.isEmpty())
			s = "*null*";
		try {
			this.studentAnswer = new NumAnswer(Double.parseDouble(s));
		}catch (Exception e){
			;
		}
		System.out.println();
	}
	
	public double getValue(){
		if (this.studentAnswer == null)
			return 0.0;
		return this.maxValue * ((NumAnswer) this.studentAnswer).getCredit(this.rightAnswer, this.error);
	}
}