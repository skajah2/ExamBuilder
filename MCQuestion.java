import java.util.*;
import java.io.PrintWriter;


public class MCQuestion extends Question {
	
	protected ArrayList<MCAnswer> answers;
	protected HashMap<Character, Integer> map;
	private char let;
	private int index;
	
	public MCQuestion(String text, double maxValue){
		super(text, maxValue);
		this.map = new HashMap<Character, Integer>(); // map a,b,c to index in answer list
		this.let = 'A';
		this.index = 0;
		this.answers = new ArrayList<MCAnswer>();
	}
	
	public MCQuestion(Scanner sc){
		super(sc);
		this.map = new HashMap<Character, Integer>();
		this.let = 'A';
		this.index = 0;
		this.answers = new ArrayList<MCAnswer>();
	}
	public void addAnswer(MCAnswer ans){
		this.answers.add(ans);
		this.map.put(this.let, this.index);
		this.let++;
		this.index++;
	}
	
	public void reorderAnswers(){
		Collections.shuffle(this.answers);
		this.map = new HashMap<Character, Integer>();
		char letter = 'A';
		for (int i=0; i<this.answers.size(); i++){
			this.map.put(letter, i);
			letter++;
		}
	}
	
	public void print(){
		super.print();
		char letter = 'A';
		for (MCAnswer mc: this.answers){
			System.out.print("   " + letter++ + ") ");
			((MCAnswer) mc).print();
		}
	}
	
	public Answer getNewAnswer(){
		return null;
	}
	
	public void getAnswerFromStudent(){
		;
	}
	
	public double getValue(MCAnswer answer){
		
		if (this.studentAnswer == null)
			return 0;
		double credit = 0.0;
		for (MCAnswer a: this.answers){
			credit += a.getCredit(answer);
		}
		return credit * this.maxValue;
	}
	
	public double getValue() {
		if (this.studentAnswer == null)
			return 0.0;
		return studentAnswer.getCredit(this.rightAnswer) * this.maxValue;
	}
	
	public void save(PrintWriter pw){
		
		;
	}
	
	public void saveStudentAnswers(Scanner sc){
		;
	}

	public String toString() {
		String result = this.text + '\n';
		char letter = 'A';
		for (MCAnswer mc: this.answers){
			result += "   " + (letter++) + ") " + ((MCAnswer) mc).toString() + '\n';
		}
		return result;
	}

}
