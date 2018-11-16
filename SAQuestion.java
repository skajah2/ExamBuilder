import java.util.Scanner;
import java.io.PrintWriter;


public class SAQuestion extends Question {

	public SAQuestion(String text, double maxValue){
		super(text, maxValue);
	}
	
	public SAQuestion(Scanner sc){
		super(sc);
		SAAnswer rightAnswer = new SAAnswer(sc);
		this.setRightAnswer(rightAnswer);
	}
	
	public void finalize(){
		this.text = null;
		this.maxValue = 0;
		this.rightAnswer = null;
	}
	public Answer getNewAnswer(String text){
		return new SAAnswer(text);
	}

	public void print(){
		System.out.println(this.text);
		if (this.studentAnswer == null)
			System.out.println("*null*");
		else
			this.studentAnswer.print();
	}
	
	public String toString(){
		String result = this.text+'\n';
		if (this.studentAnswer == null)
			result += "*null*\n";
		else 
			result += this.studentAnswer.toString() +'\n';
		return result;
	}

	
	public Answer getNewAnswer(){
		return null;
	}
	
	public void getAnswerFromStudent(){
		String s = ScannerFactory.getKeyboardScanner().nextLine().trim();
		if (s.isEmpty())
			s = "*null*";
		this.studentAnswer = new SAAnswer(s);
		System.out.println();
	}
	
	public double getValue(){
		if (this.studentAnswer == null)
			return 0;
		return studentAnswer.getCredit(this.rightAnswer) * this.maxValue;
	}
	
	public void save(PrintWriter pw){
		pw.println("SAQuestion\n" + this.maxValue + '\n' + this.text);
		((SAAnswer) this.rightAnswer).save(pw);
	}
	
	public void saveStudentAnswers(PrintWriter pw){
		try {
			pw.println("SAAnswer\n" + this.studentAnswer.toString());
		} catch (Exception e){
			pw.println("*null*");
		}
	}
	
	public void restoreStudentAnswers(Scanner sc){
		this.studentAnswer = new SAAnswer(sc.nextLine().trim());
		//System.out.println("SAAnswer: " + this.studentAnswer.toString());
	}
	
}
