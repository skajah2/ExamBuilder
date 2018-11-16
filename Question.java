import java.util.Scanner;
import java.io.PrintWriter;


public abstract class Question {
	
	protected String text;
	protected Answer rightAnswer, studentAnswer;
	protected double maxValue;
	
	protected Question(String text, double maxValue){
		this.text = text;
		this.maxValue = maxValue;
		this.rightAnswer = null;
		this.studentAnswer = null;
	}
	
	public Question(Scanner sc){
		this.maxValue = Double.valueOf(sc.nextLine().trim());
		this.text = sc.nextLine().trim();
		this.rightAnswer = null;
		this.studentAnswer = null;
	}
	public void print(){
		System.out.println(this.text);
	}
	
	public void setRightAnswer(Answer ans){
		this.rightAnswer = ans;
	}
	
	public abstract void save(PrintWriter pw);
	
	public void saveStudentAnswer(PrintWriter pw){
		; 
	}
	
	public void restoreStudentAnswers(Scanner sc){
		// do stuff
	}
	
	public abstract Answer getNewAnswer();
	
	public abstract void getAnswerFromStudent();
	
	public abstract double getValue();
}
