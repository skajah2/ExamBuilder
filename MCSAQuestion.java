import java.util.Scanner;
import java.io.PrintWriter;


public class MCSAQuestion extends MCQuestion {
		
	public MCSAQuestion(String text, double maxValue){
		super(text, maxValue);
	}
	
	public void finalize(){
		this.text = null;
		this.maxValue = 0;
		this.studentAnswer = null;
		this.map = null;
		for (Answer a: this.answers){
			a = null;
		}
		this.answers = null;
	}
	public MCSAQuestion(Scanner sc){
		super(sc);
		int choices = 0;
		if (sc.hasNextLine())
			choices = Integer.parseInt(sc.nextLine().trim());
		String  line;
		for (int i=0; i<choices; i++){
			MCSAAnswer a = new MCSAAnswer(sc);
			this.addAnswer(a);
		}
		
		
	}
	
	public Answer getNewAnswer(String text){
		return new MCSAAnswer(text, 0);
	}
	
	public Answer getNewAnswer(String text, double creditIfSelected){
		return new MCSAAnswer(text, creditIfSelected);
	}
	
	public void getAnswerFromStudent(){
		char range = 'A';
		for (int i=1; i<this.answers.size(); i++){
			range++;
		}

		for (Answer a: this.answers){
			((MCAnswer) a).setSelected(false);
		}
		//System.out.println("All answers unselected");
		System.out.println("\nEnter A-" + range+" (Single choice)");
		String choice = ScannerFactory.getKeyboardScanner().nextLine().trim().toUpperCase();
		
		if (choice.length() > 1){
			System.out.println("Too many answers given. Nothing selected");
			return;
		}
		try {
			int index = this.map.get(choice.charAt(0));
			this.studentAnswer = this.answers.get(index);
			((MCAnswer) this.studentAnswer).setSelected(true);
		} catch (Exception e){
			System.out.println("Invalid choice. Nothing selected");
		}
	}
	
	public double getValue() {
		return super.getValue((MCAnswer) this.studentAnswer);
	}
	
	public void save(PrintWriter pw){
		pw.println("MCSAQuestion\n" + this.maxValue + '\n' + this.text + '\n' + 
				this.answers.size());
		for (Answer a: this.answers){
			((MCAnswer) a).save(pw);
		}
	}
	
	public void saveStudentAnswers(PrintWriter pw){
		pw.println("MCSAAnswer");
		try {
			pw.println(this.studentAnswer.toString());
		} catch (Exception e){
			pw.println("*null*");
		}
	}
	
	public void restoreStudentAnswers(Scanner sc){
		String text = sc.nextLine().trim();
		if (text.equals("*null*"))
			this.studentAnswer = null;
		else{
			this.studentAnswer = new MCSAAnswer(text, 0);
			((MCAnswer) this.studentAnswer).setSelected(true);
		}
	}
}
