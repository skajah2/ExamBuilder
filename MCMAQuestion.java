import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class MCMAQuestion extends MCQuestion {
	public double baseCredit;
	protected ArrayList<Answer> studentAnswer;
	
	public MCMAQuestion(String text, double maxValue, double baseCredit){
		super(text, maxValue);
		this.baseCredit = baseCredit;
		this.studentAnswer = new ArrayList<Answer>();
	}
	
	public void finalize(){ // set all things to null/0
		this.baseCredit = 0;
		this.text = null;
		this.maxValue = 0;
		this.map = null;
		for (Answer a: this.studentAnswer){
			a = null;
		}
		for (Answer a: this.answers){
			a = null;
		}
		this.studentAnswer = null;
		this.answers = null;
	}
	
	public MCMAQuestion(Scanner sc){
		super(sc);
		this.studentAnswer = new ArrayList<Answer>();
		double baseCredit;
		int choices = 0;
		if (sc.hasNextLine()){ // get base credit
			baseCredit = Double.parseDouble(sc.nextLine().trim());
			this.baseCredit = baseCredit;
		}
		
		if (sc.hasNextLine()) // get number of choices
			choices = Integer.parseInt(sc.nextLine().trim());
		String line;
		for (int i=0; i<choices; i++){
			this.addAnswer(new MCMAAnswer(sc));
		}
	}
	
	public void getAnswerFromStudent(){
		char range = 'A';
		for (int i=1; i<this.answers.size(); i++){
			range++;
		}
		System.out.println("\nEnter A-" + range+" (multiple choices)");
		String[] choices = ScannerFactory.getKeyboardScanner().nextLine().trim().toUpperCase().split(" ");
		
		for (Answer a: this.answers){
			((MCAnswer) a).setSelected(false);
		}
		//System.out.println("All answers unselected");

		for (String s: choices) {
			try {
				if (s.length() != 1)
					continue;
				int index = this.map.get(s.charAt(0));
				Answer a = this.answers.get(index);
				((MCAnswer) a).setSelected(true);
				this.studentAnswer.add(a);
			} catch (Exception e){
				//System.out.println("Invalid choice. Nothing selected");
				;
			}
		}
			
	}
	
	public Answer getNewAnswer(String text, double creditIfSelected){
		return new MCMAAnswer(text, creditIfSelected);
	}
	
	public double getValue(){
		double total = 0.0;
		for (Answer a: this.studentAnswer){
			total += super.getValue((MCAnswer) a);
		}
		return total + (this.baseCredit * this.maxValue);
	}
	
	public void save(PrintWriter pw){
		pw.println("MCMAQuestion\n" + this.maxValue + '\n' + this.text + '\n' +
				this.baseCredit + '\n' + this.answers.size());
		for (Answer a: this.answers){
			((MCAnswer) a).save(pw);
		}
	}
	
	public void saveStudentAnswers(PrintWriter pw){
		pw.println("MCMAAnswer");
		int size = 0 ;
		try {
			size = this.studentAnswer.size();
			pw.println(size);
			for (Answer a: this.studentAnswer){
				pw.println(((MCAnswer) a).toString());
			}
		} catch (Exception e){
			pw.println("0");
		}
	}
	
	public void restoreStudentAnswers(Scanner sc){
		this.studentAnswer = new ArrayList<Answer>();
		int numAnswers = Integer.parseInt(sc.nextLine().trim()); // get number of choices chosen
		String text;
		for (int i=0; i<numAnswers; i++){ // add those answers to question
			text = sc.nextLine().trim();
			MCMAAnswer a = new MCMAAnswer(text, 0);
			a.setSelected(true);
			this.studentAnswer.add(a);
		}
	}
}
