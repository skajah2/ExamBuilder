import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class ExamGrader {

	private static Scanner keyboard = ScannerFactory.getKeyboardScanner();

	public static void displayMainMenu(){
		System.out.println("Choose one of the following options (1-5)");
		System.out.println("  1) Load an answer file (test file is automatically found)");
		System.out.println("  2) Grade answers");
		System.out.println("  3) Save score info");
		System.out.println("  4) Print exam");
		System.out.println("  5) Quit");
	}

	public static Exam getExam(){
		Exam exam = null;
		System.out.println("Enter the name of the answer file:");
		String path = keyboard.nextLine().trim();

		if (path.isEmpty())
			return exam;

		String examName = "";

		try {
			Scanner answerFile = new Scanner(new File(path));
			int i =0;
			answerFile.nextLine(); // name
			examName = answerFile.nextLine().trim();
		} catch (Exception e){
			;
		}
		if (examName.isEmpty())
			return exam;

		Scanner sc;
		String examFileName = "";
		File directory = new File(".");
		File[] files = directory.listFiles();

		for (File f: files){
			if (!f.isFile())
				continue;
			try {
				sc = new Scanner(f);
			} catch (Exception e){
				continue;
			}
			if (!sc.hasNextLine())
				continue;
			if (sc.nextLine().trim().equals(examName)){
				examFileName = f.getName();
				break;
			}
		}
		if (examFileName.isEmpty())
			return exam;

		try {
			exam = new Exam(new Scanner(new File(examFileName)));
			exam.restoreStudentAnswers(new Scanner(new File(path)));
		} catch (Exception e){
			;
		}

		return exam;
	}


	public static void main(String[] args){

		System.out.println("Samuel Kajah\tskajah2\n\nFollow on screen directions to grade a test\n");
		//String studentName = null;
		Exam currentExam = null;
		String line;
		int choice;
		while (true){
			ExamGrader.displayMainMenu();
			line = keyboard.nextLine().trim();

			if (line.isEmpty())
				continue;

			if (line.equalsIgnoreCase("quit") ||
				line.equalsIgnoreCase("q") || line.equalsIgnoreCase("exit"))
				break;

			if (!ExamBuilder.isValidChoice(line, 5)){
				System.out.println("*Invalid choice*\n");
				continue;
			}
			choice = Integer.parseInt(line);

			if (choice == 1){ // load exam and answer
				Exam newExam = ExamGrader.getExam();
				if (newExam == null)
					System.out.println("Could not find corresponding exam");
				else{
					currentExam = newExam;
					System.out.println("Exam and answer file loaded");
				}
			} // end load answer

			else if (choice == 2){ // Grade answers
				if (currentExam == null)
					System.out.println("No exam/answer file loaded");
				else{
					System.out.println("Grading [" + currentExam.getName() +"] ...");
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (Exception e){
						;
					}
					currentExam.reportQuestionValues();
				}
			} // end grade answers

			else if (choice == 3){ // save score info to csv
				if (currentExam == null)
					System.out.println("No exam/answer file found");
				else{

					String path;
					while (true){
						System.out.println("Enter the name for this file: ");
						path = keyboard.nextLine().trim();
						if (!path.isEmpty())
							break;
						else
							System.out.println("File name cannot be blank\n");
					}

					System.out.println();

					ArrayList<String> scores = currentExam.getScoresAsList();
					Double total = currentExam.getValue();
					String name = currentExam.getStudentName();

					PrintWriter pw;
					try {
						pw = new PrintWriter(new File(path));
					} catch (Exception e){
						System.out.println("Couldn't save asnwers\n");
						continue;
					}

					System.out.println("Saving [" + currentExam.getName() +"] ...");
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (Exception e){
						;
					}
					pw.print(name + "," + total);
					for (String score: scores){
						pw.print(",");
						pw.print(score);
					}
					pw.println();
					pw.close();
				}
			} // end of save

			else if (choice == 5)
				break;
			else if (choice == 4){
				if (currentExam == null)
					System.out.println("No exam loaded. Nothing to print");
				else
					currentExam.print();
			}
			System.out.println();
		} // end while
	} // end main 
} // end class