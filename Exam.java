import java.util.ArrayList; 
import java.util.Collections;
import java.util.Scanner;
import java.util.Date;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class Exam {
	
	private String text;
	private ArrayList<Question> questions;
	private String studentName;
	//private String date;
	
	public Exam(String text){
		this.text = text;
		this.questions = new ArrayList<Question>(10);
		this.studentName = "";
	}
	
	public Exam(Scanner sc){
		//System.out.println("Enter your name: ");
		//this.studentName = ScannerFactory.getKeyboardScanner().nextLine().trim();
		String line;
		if (sc.hasNextLine()){ // get exam title
			line = sc.nextLine().trim();
			this.text = line;
		}
		this.questions = new ArrayList<Question>(10);
		while (sc.hasNextLine()){ // go through file and make exam
			line = sc.nextLine().trim();
			if (line.isEmpty())
				continue;
			if (line.equals("SAQuestion")){
				SAQuestion q = new SAQuestion(sc);
				this.questions.add(q);
			} else if (line.equals("MCSAQuestion")){
				MCSAQuestion q = new MCSAQuestion(sc);
				this.questions.add(q);
			} else if (line.equals("MCMAQuestion")){
				MCMAQuestion q = new MCMAQuestion(sc);
				this.questions.add(q);
			} else if (line.equals("NumQuestion")){
				NumQuestion q = new NumQuestion(sc);
				this.questions.add(q);
			}
		}
		this.studentName = "";
		
	}
	
	public void finalize(){ // set all references to null
		System.out.println("Clearing exam memory");
		this.text = null;
		this.studentName = null;
		for (Question q: this.questions){
			if (q instanceof SAQuestion)
				((SAQuestion) q).finalize();
			else if (q instanceof MCSAQuestion)
				((MCSAQuestion) q).finalize();
			else if (q instanceof MCMAQuestion)
				((MCMAQuestion) q).finalize();
			else if (q instanceof NumQuestion){
				((NumQuestion) q).finalize();
			}
		}
		this.questions = null;
	}
	public void print(){
		System.out.println("Printing [" + this.text + "]\n");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e){
			;
		}
		int i = 1;
		for (Question q: this.questions){
			System.out.print(i+") ");
			if (q instanceof MCQuestion)
				((MCQuestion) q).print();
			else if (q instanceof SAQuestion)
				((SAQuestion) q).print();
			else if (q instanceof NumQuestion)
				((NumQuestion) q).print();
			i++;
			System.out.println();
		}
	}
	
	public void addQuestion(Question q){
		this.questions.add(q);
	}
	
	public void reorderQuestions(){
		//System.out.println("Shuffling questions...");
		Collections.shuffle(this.questions);
		this.reorderMCAnswer(-1); // reorder all MCAnswer
	}
	
	public void reorderMCAnswer(int index){
		if (index >= 0 && this.questions.get(index) instanceof MCQuestion){
			MCQuestion mc = (MCQuestion) this.questions.get(index);
			mc.reorderAnswers();
		} else{
			for (Question q: this.questions){
				if (q instanceof MCQuestion)
					((MCQuestion) q).reorderAnswers();
			}
		}
	}
	
	public void getAnswerFromStudent(int index){
		if (index >= 0){
			System.out.print((index+1) + ") ");
			this.questions.get(index).print();
			this.questions.get(index).getAnswerFromStudent();
			return;
		}
		System.out.println("Beginning exam\n");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e){
			;
		}
		Question q;
		for (int i=0; i<this.questions.size(); i++){ // get answers
			System.out.print((i+1) + ") ");
			q = this.questions.get(i); 
			if (q instanceof MCQuestion)
				((MCQuestion) q).print();
			else
				q.print();
			if (q instanceof MCSAQuestion)
				((MCSAQuestion) q).getAnswerFromStudent();
			else if (q instanceof MCMAQuestion)
				((MCMAQuestion) q).getAnswerFromStudent();
			else if (q instanceof SAQuestion)
				((SAQuestion) q).getAnswerFromStudent();
			else if (q instanceof NumQuestion)
				((NumQuestion) q).getAnswerFromStudent();
		}
		
	}
	
	public void save(PrintWriter pw){
		//System.out.println("Saving [" +this.text+"]");
		pw.println(this.text); // save title
		pw.println((new Date()).toString() + '\n');
		//System.out.println("Got here");
		for (Question q: this.questions){ // save questions
			if (q instanceof MCSAQuestion)
				((MCSAQuestion) q).save(pw);
			else if (q instanceof MCMAQuestion)
				((MCMAQuestion) q).save(pw);
			else if (q instanceof SAQuestion)
				((SAQuestion) q).save(pw);
			else if (q instanceof NumQuestion)
				((NumQuestion) q).save(pw);
			pw.println();
		}
	}
	
	public void saveStudentAnswers(PrintWriter pw){

		pw.println(this.text);
		pw.println((new Date()).toString() + '\n');

		for (Question q: this.questions){ // save answers to questions
			if (q instanceof SAQuestion)
				((SAQuestion) q).saveStudentAnswers(pw);
			else if (q instanceof MCSAQuestion)
				((MCSAQuestion) q).saveStudentAnswers(pw);
			else if (q instanceof MCMAQuestion)
				((MCMAQuestion) q).saveStudentAnswers(pw);
			else if (q instanceof NumQuestion)
				((NumQuestion) q).saveStudentAnswers(pw);
			pw.println();
		}
	}
	
	public void restoreStudentAnswers(Scanner sc){
		this.studentName = sc.nextLine().trim(); // get name
		sc.nextLine().trim(); // title 
		sc.nextLine(); // date
		String line;
		int i = 0;
		Question q;
		//System.out.println(this.questions.size());
		while (sc.hasNextLine()){ // get appropriate answers 
			if (i >= this.questions.size())
				break;
			line = sc.nextLine().trim();
			if (line.isEmpty())
				continue;
			q = this.questions.get(i);
			//System.out.println(q.getClass());
			if (line.equals("SAAnswer"))
				((SAQuestion) q).restoreStudentAnswers(sc);
			else if (line.equals("MCSAAnswer"))
				((MCSAQuestion) q).restoreStudentAnswers(sc);
			else if (line.equals("MCMAAnswer"))
				((MCMAQuestion) q).restoreStudentAnswers(sc);
			else if (line.equals("NumAnswer"))
				((NumQuestion) q).restoreStudentAnswers(sc);
			i++;
		}
	}
	
	public double getValue(){
		double total = 0;
		for (Question q: this.questions){
			total += q.getValue();
		}
		return total;
	}
	
	public void reportQuestionValues(){
		System.out.format("%-16s%-16s\n", "Question #", "Points Earned");
		int i = 1;
		for (Question q: this.questions){
			System.out.format("%-16d%-16s\n", i, q.getValue());
			i++;
		}
	}

	public void removeQuestion(int index){
		if (index < 0){
			System.out.println("Removing all questions");
			for (int i=0; i<this.questions.size(); i++){
				this.questions.set(i, null);
			}
		} else if (index >= this.questions.size())
			return;
		else
			this.questions.set(index, null);
		clearNullQuestions();
	}

	public void clearNullQuestions(){
		this.questions.removeAll(Collections.singleton(null));
	}

	public String getName(){
		return this.text;
	}

	public void rename(String name){
		this.text = name;
		//System.out.println("Exam title changed to [" + this.text + "]");
	}

	public int getNumberOfQuestions(){
		return this.questions.size();
	}

	public void printAtIndex(int index){
		Question q = this.questions.get(index);
		if (q instanceof MCQuestion)
			((MCQuestion) q).print();
		else
			q.print();
	}

	public String getStudentName(){
		return this.studentName;
	}

	public ArrayList<String> getScoresAsList(){
		ArrayList<String> scores = new ArrayList<String>();

		for (int i=0; i<this.questions.size(); i++){
			scores.add(Double.toString(this.questions.get(i).getValue()));
		}
		return scores;
	}

	public String getQuestionAsHTML(int index){
		String qText = "";
		Question q = this.questions.get(index);
		if (q instanceof SAQuestion)
			qText = ((SAQuestion) q).toString().replace("\n", "<br/>");
		else if (q instanceof NumQuestion)
			qText = ((NumQuestion) q).toString().replace("\n", "<br/>");
		else if (q instanceof MCQuestion)
			qText = ((MCQuestion) q).toString().replace("\n", "<br/>");
		return "<html><center>Question " + (index+1) + ")</center><br/>" +  qText + "</html>";
	}
}
