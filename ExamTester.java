import java.io.*;
import java.util.Scanner;

public class ExamTester {

	public static void takeExam(Exam exam){
		if (exam == null){
			System.out.println("Cannot take null exam");
			return;
		}
		exam.print();
		System.out.println("Reorder Questions? (y/n)");
		String choice = ScannerFactory.getKeyboardScanner().nextLine().trim();
		if (choice.equalsIgnoreCase("y"))
			exam.reorderQuestions();
		exam.getAnswerFromStudent(-1);
		System.out.println("End of Exam");
		System.out.println("Total score: " + exam.getValue()+"\nPrinting Stats...");
		exam.reportQuestionValues();
	}
	
	public static Exam makeExam(String path){
		Exam exam = null;
		try {
			Scanner f = new Scanner(new File(path));
			exam = new Exam(f);
			f.close();
		} catch (Exception e){
			System.out.println("Couldn't make exam. Bad file");
		}
		return exam;
	}
	
	public static void saveExam(Exam exam, String path){
		try {
			PrintWriter f = new PrintWriter(new File(path));
			exam.save(f);
			f.close();
		} catch (Exception e){
			System.out.println("Couldn't save exam. Bad file or null exam object");
		}
	}
	
	public static void saveStudentAnswers(Exam exam, String path){
		try {
			PrintWriter f = new PrintWriter(new File(path));
			exam.saveStudentAnswsers(f);;
			f.close();
		} catch (Exception e){
			System.out.println("Couldn't save student answers. Bad file or null exam object");
		}
	}
	
	public static void restoreStudentAnswers(Exam exam, String path){
		try {
			Scanner sc = new Scanner(new File(path));
			exam.restoreStudentAnswers(sc);
			sc.close();
			exam.print();
			exam.reportQuestionValues();
		} catch (Exception e){
			System.out.println(e + "\nCouldn't restore student answers");
			//System.out.println(e.getStackTrace()[0].getLineNumber());
		}
	}
	
	public static void main(String args[]){
		System.out.println("Samuel Kajah\tskajah2");
		Exam exam = makeExam("exam.txt");
		takeExam(exam);
		saveExam(exam, "savedTest.txt");
		saveStudentAnswers(exam, "studentAnswers.txt");
		exam.finalize();
		exam = null;
		exam = makeExam("savedTest.txt");
		restoreStudentAnswers(exam, "studentAnswers.txt");
		exam.finalize();
		exam = null;
	}
}
