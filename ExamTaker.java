import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;
public class ExamTaker {

	static Scanner keyboard = ScannerFactory.getKeyboardScanner();

	public static String getStudentName(){
		String name;
		while (true){
			System.out.println("Enter you name: ");
			name = keyboard.nextLine().trim();

			if (name.isEmpty())
				System.out.println("Name cannot be blank\n");
			else
				break;
		}
		System.out.println("Student Name: " + name +"\n");
		return name;
	}

	public static void displayMainMenu(){
		System.out.println("Choose one of the following options (1-7)");
		System.out.println("  1) Enter your name");
		System.out.println("  2) Load an exam file");
		System.out.println("  3) Take test");
		System.out.println("  4) Review test");
		System.out.println("  5) Save your answers");
		System.out.println("  6) Print exam");
		System.out.println("  7) Quit");
	}

	public static Exam loadExam(){
		Exam exam = null;
		System.out.println("Enter the name of the exam file:");
		String path = keyboard.nextLine().trim();

		try {
			exam = new Exam(new Scanner(new File(path)));
		} catch (Exception e){
			;
		}
		return exam;
	}

	public static void takeExam(Exam exam){
		exam.getAnswerFromStudent(-1);
		System.out.println("\nEnd of exam\n");
	}

	public static void reviewExam(Exam exam){
		System.out.println("After each question prints, you can change your answer");
		System.out.println("Choosing to change your answer overrides your previous choice");
		System.out.println("Even if you didn't type in anything for your 'new' answer\nEnter to continue\n");

		keyboard.nextLine();

		int i;
		String c;
		String[] choices = {"yes", "y"};
		for (i=0; i<exam.getNumberOfQuestions(); i++){
			exam.printAtIndex(i);
			System.out.println("\nChange Answer? (y/n)");
			c = keyboard.nextLine().trim();
			if (!Arrays.asList(choices).contains(c))
				continue;
			else{
				System.out.println();
				exam.getAnswerFromStudent(i);
			}
			System.out.println();
		}
	}

	public static void saveStudentAnswers(Exam exam, String name){
		String line;
		while (true){
			System.out.println("Enter file name to save: ");
			line = keyboard.nextLine().trim();
			if (!line.isEmpty())
				break;
			else
				System.out.println("File name cannot be blank\n");
		}


		try {

			PrintWriter pw = new PrintWriter(new File(line));
			pw.println(name);
			exam.saveStudentAnswers(pw);
			pw.close();
		} catch (Exception e){
			System.out.println("Failed to save student answers");
		}
	}


	public static void main(String[] args){
		System.out.println("\nSamuel Kajah\tskajah2");
		System.out.println("\nFollow on screen directions to take a test\n");
		String studentName = null;
		Exam currentExam = null;
		String line;
		int choice;
		while (true){
			ExamTaker.displayMainMenu();
			line = keyboard.nextLine().trim();

			if (line.isEmpty())
				continue;

			if (line.equalsIgnoreCase("quit") ||
				line.equalsIgnoreCase("q") || line.equalsIgnoreCase("exit"))
				break;

			if (!ExamBuilder.isValidChoice(line, 7)){
				System.out.println("*Invalid choice*\n");
				continue;
			}
			choice = Integer.parseInt(line);
			if (choice == 1){ // Enter new name for student
				if (studentName != null)
					System.out.println("Previous name will be overriden\n");
				studentName = ExamTaker.getStudentName();
			}  
			else if (choice == 2){ // load an exam file
				Exam newExam = ExamTaker.loadExam();
				if (newExam == null)
					System.out.println("Failed to laod exam");
				else{
					currentExam = newExam;
					System.out.println("Exam [" + currentExam.getName() + "] loaded");
				}
			}
			else if (choice == 3){ // take an exam
				if (currentExam == null)
					System.out.println("No exam loaded");
				else
					ExamTaker.takeExam(currentExam);
			}
			else if (choice == 4){ // review the exam
				if (currentExam == null)
					System.out.println("No exam laoded");
				else
					ExamTaker.reviewExam(currentExam);
			}
			else if (choice == 5){ // save the student answers
				if (currentExam == null)
					System.out.println("No exam loaded");
				else{
					if (studentName == null){
						System.out.println("Need your name\n");
						studentName = ExamTaker.getStudentName();
					}
					ExamTaker.saveStudentAnswers(currentExam, studentName);
					System.out.println("Exam ["+currentExam.getName() +"] saved");
				}
			}
			else if (choice == 7)
				break;
			else if (choice == 6){
				if (currentExam == null)
					System.out.println("No exam loaded. Cannot print");
				else
					currentExam.print();
			}
			System.out.println();
		} // end of while 

		currentExam = null;
		ExamTaker.keyboard = null;
	}
}