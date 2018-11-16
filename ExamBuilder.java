import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExamBuilder {

	private static Scanner keyboard = ScannerFactory.getKeyboardScanner();

	public static void displayMainMenu(){
		System.out.println("Choose one of the following options (1-8)");
		System.out.println("  1) Load exam from file");
		System.out.println("  2) Add question to exam");
		System.out.println("  3) Remove question from exam");
		System.out.println("  4) Reorder questions and answers");
		System.out.println("  5) Print the current exam");
		System.out.println("  6) Save current exam");
		System.out.println("  7) Rename current exam");
		System.out.println("  8) Quit");
	}

	public static Exam loadExamFile(String path){
		Exam exam = null;
		try {
			Scanner file = new Scanner(new File(path));
			exam = new Exam(file);
			file.close();
		} catch (Exception e){
			;
		}
		return exam;
	}


	public static boolean isValidChoice(String str, int max){
		boolean valid = false;
		try {
			int choice = Integer.parseInt(str);
			valid = choice >= 1 && choice <= max ? true : false;
		} catch (Exception e){
			;
		}
		return valid;
	}

	public static void displayQuestionMenu(){
		System.out.println("Enter the type of question to make (1-3)\n");
		System.out.println("  1) Short Answer");
		System.out.println("  2) Numerical Answer");
		System.out.println("  3) Multiple Choice (Single Answer)");
		System.out.println("  4) Multiple Choice (Multiple Answers)\n");
	}

	public static Question makeQuestion(){
		Question q = null;
		ExamBuilder.displayQuestionMenu();
		String input = ExamBuilder.keyboard.nextLine().trim();
		if (ExamBuilder.isValidChoice(input, 3)){

			int choice = Integer.parseInt(input);
			String text, qtext;
			double maxValue;

			while (true){
				System.out.println("Enter question text: ");
				qtext = ExamBuilder.keyboard.nextLine().trim();
				if (qtext.isEmpty())
					System.out.println("Question cannot be blank\n");
				else
					break;
			}
			System.out.println();

			while (true){
				System.out.print("Enter question value: ");
				text = keyboard.nextLine().trim();
				try {
					maxValue = Double.parseDouble(text);
					if (maxValue < 0)
						System.out.println("Need a positive number\n");
					else
						break;
				} catch (Exception e) {
					System.out.println("Not a numberical answer\n");
				}
			}

			System.out.println();

			if (choice == 1){ // SA Question
				q = new SAQuestion(qtext, maxValue);
				while (true){
					System.out.println("Enter the correct answer: ");
					text = keyboard.nextLine().trim();
					if (text.isEmpty())
						System.out.println("Can't have blank answer\n");
					else
						break;
				}
				System.out.println();
				((SAQuestion) q).setRightAnswer(new SAAnswer(text));
			} // end of SA Question

			else if (choice == 2){ // Numerical Question
				double val1, val2;
				String[] nums;
				while (true){
					System.out.println("Enter the correct numberical answer followed by +/- error (eg. 43.23 .05)");
					nums = keyboard.nextLine().trim().split(" ");
					if (nums.length != 2){
						System.out.println("Invalid number of arguments\n");
						continue;
					}
					try {
						val1 = Double.parseDouble(nums[0]);
						val2 = Double.parseDouble(nums[1]);
						break;
					} catch (Exception e) {
						System.out.println("Not a numberical answer\n");
					}
				}
				q = new NumQuestion(qtext, maxValue, val2);
				((NumQuestion) q).setRightAnswer(new NumAnswer(val1));
			} // end of Numerical Question

			else if (choice == 3) { // MCSA Question
				q = new MCSAQuestion(qtext, maxValue);
				
				while (true){
					System.out.println("Enter the correct answer: ");
					text = keyboard.nextLine().trim();
					if (text.isEmpty())
						System.out.println("Can't have blank answer.\n");
					else
						break;
				}
				System.out.println();
				((MCSAQuestion) q).addAnswer(new MCSAAnswer(text, 1.0));
				int count = 1;
				
				String l;
				String[] line;
				double credit = 0;
				while (true){
					System.out.println("Type an answer choice in this format\n" + 
					"credit Answer Choice\nWhere credit is a number [0-1]\nOr enter to end answer choices");
					l = ExamBuilder.keyboard.nextLine().trim();
					if (l.isEmpty()){
						if (count < 2){
							if (count == 1 )
								System.out.println("Need at least one more answer choice\n");
							continue;
						} 
						System.out.println("End of answer choices\n");
						break;
					}

					line = l.split(" ");
					if (line.length < 2){
						System.out.println("Answer text missing\n");
						continue;
					}
					try {
						credit = Double.parseDouble(line[0]);
						if (credit < 0 || credit > 1){
							System.out.println("Credit not in range. Default to 0");
							credit = 0;
						}
					} catch (Exception e){
						System.out.println("Invalid credit. Default to 0\n");
					}
					String answerText = "";
					for (int i=1; i<line.length; i++){
						answerText += line[i] + " ";
					}
					((MCSAQuestion) q).addAnswer(new MCSAAnswer(answerText.trim(), credit));
					count++;
					System.out.println();
				}
			} // end of MCSA Question


			else if (choice == 4){ // MCMA Question
				System.out.println("Enter a the base credit in range [0-1]\nAnything not in this range defaults to 0");
				text = ExamBuilder.keyboard.nextLine().trim();
				double baseCredit = 0.0;
				if (text.isEmpty())
					System.out.println("Number not found, defaulting to zero base credit\n");
				else {
					try {
						baseCredit = Double.parseDouble(text);
						if  (baseCredit < 0){
							System.out.println("Base credit cannot be negative, defaulting to 0 base credit\n");
							baseCredit = 0;
						}
						else if (baseCredit > 1) {
							System.out.println("Base credit should be in [0-1], defaulting to 0\n");
							baseCredit = 0;
						}
					} catch (Exception e){
						System.out.println("Number not found, defaulting to 0 base credit\n");
					}
				}
				System.out.println();
				q = new MCMAQuestion(qtext, maxValue, baseCredit);
				int count = 0;
				String l;
				String[] line;
				double credit = 0;
				while (true){
					System.out.println("Type an answer choice in this format\n" + 
					"credit Answer Choice\nWhere credit is a number [0-1]\nOr enter to end answer choices");
					l = ExamBuilder.keyboard.nextLine().trim();
					if (l.isEmpty()){
						if (count < 2){
							if (count ==0 )
								System.out.println("Need at least one more answer choice\n");
							else
								System.out.println("Need at least 2 more answer choices\n");
							continue;
						} 
						System.out.println("End of answer choices\n");
						break;
					}

					line = l.split(" ");
					if (line.length < 2){
						System.out.println("Answer text missing\n");
						continue;
					}
					try {
						credit = Double.parseDouble(line[0]);
						if (credit < 0 || credit > 1){
							System.out.println("Credit not in range. Default to 0");
							credit = 0;
						}
					} catch (Exception e){
						System.out.println("Invalid credit. Default to 0\n");
					}
					String answerText = "";
					for (int i=1; i<line.length; i++){
						answerText += line[i] + " ";
					}
					((MCMAQuestion) q).addAnswer(new MCMAAnswer(answerText.trim(), credit));
					count++;
					System.out.println();
				} 
			} // end of MCMA Question
		}// end of big if
		else
			System.out.println("*Invalid choice*");
		return q;
	} // end make quesiton



	public static void removeQuestion(Exam exam){
		System.out.println("The exam will now be printed. Look through it and decide which numbers to delete");
		System.out.println("Only valid question numbers will be considered\n" + 
			"If you want to delete all, enter a negative number\n" +
			"Press any key to continue");
		keyboard.nextLine();

		exam.print();

		System.out.println("Enter the questions to delete (eg. 1 2 5)");
		String[] nums = keyboard.nextLine().trim().split(" ");
		for (String s: nums){
			try {
				int num = Integer.valueOf(s);
				exam.removeQuestion(num - 1);
			} catch (Exception e){
				;
			}
		}
		exam.clearNullQuestions();
		System.out.println("Exam after changes\n");
		exam.print();
	}


	public static void main(String args[]){

		System.out.println("\nSamuel Kajah\tskajah2");
		System.out.println("\nWelcome to ExamBuilder. Follow the on screen directions\n");
		
		Exam currentExam = null;
		String input;

		while (true){
			ExamBuilder.displayMainMenu();
			input = keyboard.nextLine().trim();

			if (input.isEmpty())
				continue;
			else if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit") || 
				input.equalsIgnoreCase("q")){
				System.out.println("Quitting...");
				break;
			}
			else if (ExamBuilder.isValidChoice(input, 8)){
				int choice = Integer.parseInt(input);

				if (choice == 1){ // load exam
					System.out.print("Enter the exam file name: ");
					Exam newExam = loadExamFile(keyboard.nextLine().trim());
					
					if (newExam == null)
						System.out.println("Failed to load exam");
					else {
						currentExam = newExam;
						System.out.println("Exam [" + currentExam.getName() + "] loaded");
					}
				} // end of load exam block

				else if (choice == 2){ // Add question to exam
					if (currentExam == null){
						System.out.print("No exam found. Making new exam\nEnter exam title: ");
						String text = keyboard.nextLine().trim();
						while (text.isEmpty()){
							System.out.println("Title can't be blank\nEnter exam title: ");
							text = keyboard.nextLine().trim();
						}
						currentExam = new Exam(text);
					}
					System.out.println();
					Question q = ExamBuilder.makeQuestion();
					if (q != null){
						currentExam.addQuestion(q);
						System.out.println("\nNew question added");
					}
					else
						System.out.println("No question added");
				} // end of add question block

				else if (choice == 3) { // remove question from exam
					if (currentExam == null)
						System.out.println("No exam found. Can't remove questions");
					else {
						ExamBuilder.removeQuestion(currentExam);
					}
				}

				else if (choice == 4){ // Reorder questions/answers
					if (currentExam == null)
						System.out.println("No exam found. Cannot shuffle questions");
					else
						currentExam.reorderQuestions();
				}
				else if (choice == 5){ // Print exam
					if (currentExam == null)
						System.out.println("No exam found. Cannot print exam");
					else
						currentExam.print();
				}
				else if (choice == 6){ // save exam
					if (currentExam == null)
						System.out.println("No exam found. Nothing to save");
					else {
						String line;
						while (true){
							System.out.println("Enter file name to save as: ");
							line = keyboard.nextLine().trim();
							if (!line.isEmpty())
								break;
							else
								System.out.println("Name cannot be blank\n");
						}

						try {
							PrintWriter pw = new PrintWriter(new File(line));
							currentExam.save(pw);
							pw.close();
						} catch (Exception e){
							System.out.println("Could not save file");
						}

					}
				} // end of save exam
				else if (choice == 8){ // quit
					System.out.println("Qutting...");
					break;
				}
				else if (choice == 7 ){ // rename exam
					String name;
					while (true){
						System.out.println("Enter the new name of the exam: ");
						name = keyboard.nextLine().trim();
						if (!name.isEmpty())
							break;
						System.out.println("Name cannot be empty");
					}
					if (currentExam != null)
						currentExam.rename(name);
					else {
						System.out.println("Exam not found. Making new exam with tile: " + name);
						currentExam = new Exam(name);
					}
				} // end rename 

			}  // end choice
			else // not valid choice
				System.out.println("*Invalid selection*");
			System.out.println();
		} // end of while 
		ExamBuilder.keyboard = null;
	} // end of main


} // end of class