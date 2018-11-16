
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;

public class ExamBuilderGUI extends JFrame {
	private Exam currentExam;
	private JButton loadButton, removeQuestionButton,
					reorderButton, printButton, saveButton, renameButton,
					exitButton;
	private JLabel examInfo, addQuestionLabel;
	private JTextField fileTextField, renameTextField, saveTextField, removeQuestionTextField; 
	private JPanel center;
	private JButton saQuestion, numQuestion, mcQuestion;


	public ExamBuilderGUI(){
		this.loadButton = new JButton("Load Exam");
		this.addQuestionLabel = new JLabel("Add Question of Type");
		this.removeQuestionButton = new JButton("Remove Question #");
		this.reorderButton = new JButton("Reorder Questions");
		this.printButton = new JButton("Print Exam");
		this.saveButton = new JButton("Save Exam As");
		this.renameButton = new JButton("Rename Exam");
		this.exitButton = new JButton("Quit");
		this.examInfo = new JLabel(String.format("Current exam [%s]", this.currentExam));
		this.currentExam = null;
		this.fileTextField = new JTextField(10);
		this.renameTextField = new JTextField(10);
		this.saveTextField = new JTextField(10);
		this.removeQuestionTextField = new JTextField(10);
		this.saQuestion = new JButton("SAQuestion");
		this.numQuestion = new JButton("NumQuestion");
		this.mcQuestion = new JButton("MCQuestion");

		this.center = new JPanel();
		this.makeMainFrame();
		this.addButtonsToMainFrame();
	}

	public void makeMainFrame(){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();

		int xDim = (dim.width * 5)/12;
		int yDim = dim.height/3;

		this.setSize(xDim, yDim);
		this.setTitle("Exam Builder");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
	}

	public void display(){
		this.setVisible(true);
	}

	public void addButtonsToMainFrame(){

		Box box1 = Box.createVerticalBox();
		Box box2 = Box.createHorizontalBox();
		Box box3 = Box.createHorizontalBox();
		Box box4 = Box.createVerticalBox();
		/* Add load button/textField */
		box2.add(loadButton);
		box2.add(fileTextField);

		/* Add Save as button/textField */
		box3.add(saveButton);
		box3.add(saveTextField);

		/* Add remove button/textField */
		box3.add(removeQuestionButton);
		box3.add(removeQuestionTextField);


		/* Add rename button/textField */
		box2.add(renameButton);
		box2.add(renameTextField);

		/* Add rest of buttons */

		box1.add(Box.createRigidArea(new Dimension(5, 10)));
		box1.add(reorderButton);

		box1.add(Box.createRigidArea(new Dimension(5, 10)));
		box1.add(printButton);

		box1.add(Box.createRigidArea(new Dimension(5, 10)));
		box1.add(exitButton);

		/* Put east buttons */
		box4.add(Box.createRigidArea(new Dimension(5, 10)));
		box4.add(addQuestionLabel);
		box4.add(saQuestion);
		box4.add(numQuestion);
		box4.add(mcQuestion);

		/* Put block of buttons on left */
		this.add(box1, BorderLayout.WEST);

		/* Put text associated buttons on top */
		this.add(box2, BorderLayout.NORTH);
		
		/* put text assocated buttons on bottom */
		this.add(box3, BorderLayout.SOUTH);

		this.add(box4, BorderLayout.EAST);

		center.add(examInfo);
		this.add(center, BorderLayout.CENTER);


	}



	private void addLoadAction(JFrame frame){
		loadButton.addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
      		String fileName = fileTextField.getText();
      		Exam newExam = ExamBuilder.loadExamFile(fileName);
      		if (newExam != null){
      			currentExam = newExam;
      			examInfo.setText(String.format("Current exam [%s]", currentExam.getName()));
      		}
      		else{
      			JDialog d = new JDialog(frame, "Load Exam");
      			JPanel panel = new JPanel();
      			panel.add(new JLabel("Failed to load exam :("));
      			d.add(panel);
        		d.setLocationRelativeTo(frame);
        		d.setSize(200, 100);
        		panel.setVisible(true);
        		d.setVisible(true);
      		}
        	
      	}
    	});
	}

	private Object[] getQuestionTextAndPointsPanels(){

		JPanel textPanel = new JPanel();
		JTextField qText = new JTextField(20);
		textPanel.add(new JLabel("Enter Question Text"));
		textPanel.add(qText);

		JPanel pointPanel = new JPanel();
		JTextField pointText = new JTextField(10);
		pointPanel.add(new JLabel("Enter Question Value"));
		pointPanel.add(pointText);

		Object[] objects = {textPanel, qText, pointPanel, pointText};
		return objects;
	}

	private boolean validNumTolerance(String answer, String tolerance){
		try {
			Double.parseDouble(answer);
			Double.parseDouble(tolerance);
		} catch (Exception e){
			return false;
		}
		return true;

	}

	private void addMCQuestionAction(JFrame frame){
		this.mcQuestion.addActionListener(new ActionListener(){
			String type = "sa";
			double baseCredit = 0;
			public void actionPerformed(ActionEvent e){
				JDialog d = new JDialog(frame, "MCQuestion Maker");
				if (currentExam == null){
					JPanel panel = new JPanel();
					panel.add(new JLabel("Load or Rename Exam first"));
					d.add(panel);
					d.setSize(300, 60);
					d.setLocationRelativeTo(frame);
					d.setVisible(true);
					//Timer.sleep(1);
					//d.dispose();
					return;
				}
				d.setLayout(new BorderLayout());

				Object[] objects = getQuestionTextAndPointsPanels();
				JPanel cards = new JPanel(new CardLayout());
				cards.add((JPanel) objects[0], "qText");
				cards.add((JPanel) objects[2], "pText");
				CardLayout cardLayout = (CardLayout) cards.getLayout();

				JPanel typeMC = new JPanel();
				typeMC.add(new JLabel("<html><center>Choose type of MCQuestion</center></html>"));

				ButtonGroup group = new ButtonGroup();
				JRadioButton sa = new JRadioButton("MCSA Question");
				JRadioButton ma = new JRadioButton("MCMA Question");
		
				group.add(sa); group.add(ma);
				sa.setSelected(true);

				typeMC.add(sa); typeMC.add(ma);

				cards.add(typeMC, "Type");

				sa.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						type = "sa";
					}
				});
				ma.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						type = "ma";
						JDialog d = new JDialog(frame, "Base Credit");
						JPanel p = new JPanel(new BorderLayout());
						p.add(new JLabel("<html><center>Enter Base Credit in range [0-1]" +
										"</br>Invalid input will be set to base credit 0" +
										"</center></html>"), BorderLayout.SOUTH);
						JButton creditDone = new JButton("Save Credit");
						JTextField credit = new JTextField("Credit [0-1]", 4);
						p.add(creditDone, BorderLayout.NORTH);
						p.add(credit, BorderLayout.CENTER);
						creditDone.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								try {
									baseCredit = Double.parseDouble(credit.getText());
									if (!(baseCredit >=0 && baseCredit<=1))
										baseCredit = 0;
								} catch (Exception ex){
									;
								}
								d.dispose();
							}
						});
						d.add(p);
						d.setSize(300, 300);
						d.setLocationRelativeTo(frame);
						d.setVisible(true);
					} 
				}); // end MA listener

				JPanel choicePanel = new JPanel(new BorderLayout());
				cards.add(choicePanel, "choicePanel");

				d.add(cards, BorderLayout.CENTER);

				Box answerChoicesBox = Box.createVerticalBox();
				ArrayList<JTextField> choices = new ArrayList<JTextField>();
				ArrayList<JTextField> credits = new ArrayList<JTextField>();

				Box pointsBox = Box.createVerticalBox();
				Box generateChoices = Box.createHorizontalBox();
				Box combo = Box.createHorizontalBox();

				JButton generateAnswersButton = new JButton("Generate 'N' Choices");
				JTextField numQuestions = new JTextField("Enter 'N'", 10);

				generateChoices.add(generateAnswersButton);
				generateChoices.add(numQuestions);
				choicePanel.add(generateChoices, BorderLayout.NORTH);

				generateAnswersButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){

						int numChoices = 0;
						try {
							numChoices = Integer.parseInt(numQuestions.getText());
							if (numChoices > 0){
								credits.clear();
								choices.clear();
								answerChoicesBox.removeAll();
								pointsBox.removeAll();
								answerChoicesBox.setVisible(false);
								pointsBox.setVisible(false);
							}
						} catch (Exception ex){
							;
						}
						
						for (int i=0; i<numChoices; i++){
							JTextField field = new JTextField("Answer Choice", 15);
							JTextField point = new JTextField("Credit", 2);

							answerChoicesBox.add(field);
							pointsBox.add(point);
							credits.add(point);
							choices.add(field);
						}
						combo.add(answerChoicesBox);
						combo.add(pointsBox);
						choicePanel.add(combo, BorderLayout.CENTER);
						answerChoicesBox.setVisible(!false);
						pointsBox.setVisible(!false);
						//combo.setVisible(true);
					}
				}); // end genrate banswers

				JButton nextButton = new JButton("Next");
				JButton previousButton = new JButton("Prev");
				JButton doneButton = new JButton("Done");

				/* Place next, previous, anddone buttons */
				d.add(nextButton, BorderLayout.EAST);
				d.add(previousButton, BorderLayout.WEST);
				d.add(doneButton, BorderLayout.SOUTH);

				/* add listeners to next button */
				nextButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.next(cards);
					}
				});

				/* add listener to prev button */
				previousButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.previous(cards);
					}
				});


				doneButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String qText = ((JTextField) objects[1]).getText();
						String pointText = ((JTextField) objects[3]).getText();
						JDialog dl = new JDialog(frame, "MCQuestion Maker");
						JPanel panel = new JPanel();
						double val = 5;
						try {
							val = Double.parseDouble(pointText);
						} catch (Exception ex){
							;
						}
						if (!validQuestionPoint(qText, pointText) || !validAnswerChoices(choices, credits)){
							panel.add(new JLabel("<html><center>Something went wrong</center></br>" +
												"1) Empty question and/or value\n</br>" + 
												"2) Question value is invalid (negative or NAN)\n</br>" + 
												"3) One or more answer choices are empty\n</br>" +
												"4) One or more credits are empty and/or are not in range [0-1]\n</html>"));
						} else {
							Question q = typeMC.equals("sa")? new MCSAQuestion(qText, val) : new MCMAQuestion(qText, val, baseCredit);
							double c = 0;
							for (int i=0; i<choices.size(); i++){
								JTextField choice = choices.get(i);
								JTextField credit = credits.get(i);
								try{
									c = Double.parseDouble(credit.getText());
								} catch (Exception ex){
									;
								}
								if (typeMC.equals("sa"))
									((MCSAQuestion) q).addAnswer(new MCSAAnswer(choice.getText(), c));
								else
									((MCMAQuestion) q).addAnswer(new MCMAAnswer(choice.getText(), c));
							}
							currentExam.addQuestion(q);
							d.dispose();
							panel.add(new JLabel("<html><center>Question Made!</center></html>"));
							//Timer.sleep(1);
							dl.dispose();
						}

						dl.add(panel);
						dl.setSize(300, 200);
						dl.setLocationRelativeTo(frame);
						dl.setVisible(true);
					}
					
				}); // end done button
				
				d.add(cards);
				d.setSize(400, 400);
				d.setLocationRelativeTo(frame);
				cardLayout.show(cards, "qText");
				d.setVisible(true);
			} // end action performed
		});
	}

	private boolean validAnswerChoices(ArrayList<JTextField> choices, ArrayList<JTextField> credits){
		if (choices.size() < 1)
			return false;
		for (int i=0; i<choices.size(); i++){
			JTextField choice = choices.get(i);
			JTextField credit = credits.get(i);
			if (choice.getText().isEmpty() || credit.getText().isEmpty())
				return false;
			try {
				double a = Double.parseDouble(credit.getText());
				if (!(a>=0 && a <=1))
					return false;
			} catch (Exception e){
				return false;
			}
		}
		return true;
	}

	private void addNumQuestionAction(JFrame frame){
		this.numQuestion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JDialog d = new JDialog(frame, "NumQuestion Maker");
				if (currentExam == null){
					JPanel panel = new JPanel();
					panel.add(new JLabel("Load or Rename Exam first"));
					d.add(panel);
					d.setSize(300, 60);
					d.setLocationRelativeTo(frame);
					d.setVisible(true);
					return;
				}
				d.setLayout(new BorderLayout());

				Object[] objects = getQuestionTextAndPointsPanels();
				JPanel cards = new JPanel(new CardLayout());
				cards.add((JPanel) objects[0], "qText");
				cards.add((JPanel) objects[2], "pText");
				CardLayout cardLayout = (CardLayout) cards.getLayout();
				
				/* make and add rightAnswerPanel */
				JPanel rightAnswerPanel = new JPanel(new FlowLayout());
				rightAnswerPanel.add(new JLabel("Enter the right answer and tolerance"));
				JTextField rightAnswerText = new JTextField("Right answer", 12);
				rightAnswerPanel.add(rightAnswerText);
				JTextField toleranceText = new JTextField("Tolerance (+/-)", 12);
				rightAnswerPanel.add(toleranceText);

				cards.add(rightAnswerPanel, "rText");



				d.add(cards, BorderLayout.CENTER);
				/* Make buttons */
				JButton nextButton = new JButton("Next");
				JButton previousButton = new JButton("Prev");
				JButton doneButton = new JButton("Done");

				/* Place next, previous, anddone buttons */
				d.add(nextButton, BorderLayout.EAST);
				d.add(previousButton, BorderLayout.WEST);
				d.add(doneButton, BorderLayout.SOUTH);

				/* add listeners to next button */
				nextButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.next(cards);
					}
				});

				/* add listener to prev button */
				previousButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.previous(cards);
					}
				});

				doneButton.addActionListener(new ActionListener(){
					
					public void actionPerformed(ActionEvent e){
						String qText = ((JTextField) objects[1]).getText();
						String pointText = ((JTextField) objects[3]).getText();
						JDialog dl = new JDialog(frame, "NumQuestion Maker");
						JPanel panel = new JPanel();
						double val = 5;
						double tolerance = 0;
						double right = 0;
						if (!validQuestionPoint(qText, pointText) || !validNumTolerance(rightAnswerText.getText(), toleranceText.getText()))
							panel.add(new JLabel("<html><center>Something went wrong</center></br>" +
												"1) Empty question, value, answer, and/or tolerance\n</br>" + 
												"2) Question value is invalid (negative or NAN)\n</br>" + 
												"3) Answer and/or tolerance is NAN\n</html>"));
						else{
							try {
								val = Double.parseDouble(pointText);
								right = Double.parseDouble(rightAnswerText.getText());
								tolerance = Double.parseDouble(toleranceText.getText());
							} catch (Exception ex){
								;
							}

							NumQuestion q = new NumQuestion(qText, val, tolerance);
							q.setRightAnswer(new NumAnswer(right));
							currentExam.addQuestion(q);
							panel.add(new JLabel("Question made!"));
							//Timer.sleep(1);
							//dl.dispose();
							d.dispose();
						}
						dl.add(panel);
						dl.setSize(200, 100);
						dl.setLocationRelativeTo(frame);
						dl.setVisible(true);

					} // end action performed
				}); // end done
				d.add(cards);
				d.setSize(400, 400);
				d.setLocationRelativeTo(frame);
				cardLayout.show(cards, "qText");
				d.setVisible(true);
			}
		});
	}

	private void addSAQuestionAction(JFrame frame){
		this.saQuestion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JDialog d = new JDialog(frame, "SAQuestion Maker");
				if (currentExam == null){
					JPanel panel = new JPanel();
					panel.add(new JLabel("Load or Rename Exam first"));
					d.add(panel);
					d.setSize(300, 60);
					d.setLocationRelativeTo(frame);
					d.setVisible(true);
					return;
				}
				
				d.setLayout(new BorderLayout());

				Object[] objects = getQuestionTextAndPointsPanels();
				JPanel cards = new JPanel(new CardLayout());
				cards.add((JPanel) objects[0], "qText");
				cards.add((JPanel) objects[2], "pText");
				CardLayout cardLayout = (CardLayout) cards.getLayout();
				
				/* make and add rightAnswerPanel */
				JPanel rightAnswerPanel = new JPanel();
				rightAnswerPanel.add(new JLabel("Enter the right answer"));
				JTextField rightAnswerText = new JTextField(15);
				rightAnswerPanel.add(rightAnswerText);
				cards.add(rightAnswerPanel, "rText");

				d.add(cards, BorderLayout.CENTER);
				/* Make buttons */
				JButton nextButton = new JButton("Next");
				JButton previousButton = new JButton("Prev");
				JButton doneButton = new JButton("Done");

				/* Place next, previous, anddone buttons */
				d.add(nextButton, BorderLayout.EAST);
				d.add(previousButton, BorderLayout.WEST);
				d.add(doneButton, BorderLayout.SOUTH);

				/* add listeners to next button */
				nextButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.next(cards);
					}
				});

				/* add listener to prev button */
				previousButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.previous(cards);
					}
				});

				doneButton.addActionListener(new ActionListener(){
					
					public void actionPerformed(ActionEvent e){
						//JDialog result = new JDialog();
						String qText = ((JTextField) objects[1]).getText();
						String pointText = ((JTextField) objects[3]).getText();
						JDialog dl = new JDialog(frame, "SAQuestion Maker");
						JPanel panel = new JPanel();
						double val = 5;
						if (!validQuestionPoint(qText, pointText) || rightAnswerText.getText().isEmpty())
							panel.add(new JLabel("<html><center>Something went wrong\n</center></br>" +
												"1) Empty question, value, and/or answer\n</br>" + 
												"2) Question value is invalid (negative or NAN)\n</html>"));
						else{
							try {
								val = Double.parseDouble(pointText);
							} catch (Exception ex){
								;
							}
							SAQuestion q = new SAQuestion(qText, val);
							q.setRightAnswer(new SAAnswer(rightAnswerText.getText()));
							currentExam.addQuestion(q);
							panel.add(new JLabel("Question made!"));
							//Timer.sleep(1);
							//dl.dispose();
							d.dispose();

						}
						dl.add(panel);
						dl.setSize(400, 100);
						dl.setLocationRelativeTo(frame);
						dl.setVisible(true);
					}
				});
				d.add(cards);
				d.setSize(400, 400);
				d.setLocationRelativeTo(frame);
				cardLayout.show(cards, "qText");
				d.setVisible(true);
			}
		});
	}

	private boolean validQuestionPoint(String qText, String pointText){
		if (qText.isEmpty())
			return false;
		double point = -1;
		try {
			point = Double.parseDouble(pointText);
		} catch (Exception e){
			;
		}

		return point >= 0;
	}

	private void addSaveAction(JFrame frame){
		this.saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JDialog d = new JDialog(frame, "Save Exam As");
				JPanel p = new JPanel();
				String text = saveTextField.getText();
				if (currentExam == null)
					p.add(new JLabel("No exam loaded. Can't save"));
				else if (text.isEmpty())
					p.add(new JLabel("File name can't be blank"));
				else {
					try {
						PrintWriter pw = new PrintWriter(new File(text));
						currentExam.save(pw);
						pw.close();
					} catch (Exception ex){
						;
					}
					p.add(new JLabel(String.format("Exam saved as [%s]", text)));
				}
				d.add(p);
				d.setSize(350, 60);
				d.setLocationRelativeTo(frame);
				d.setVisible(true);

			}
		});
	}
	private void addPrintAction(JFrame frame){
		this.printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JDialog d = new JDialog(frame, "Print Exam");	
				if (currentExam == null){
      				JPanel panel = new JPanel();
      				panel.add(new JLabel("No exam loaded. Can't print"));
		      		d.add(panel);
		        	d.setLocationRelativeTo(frame);
		        	d.setSize(250, 60);
		        	panel.setVisible(true);
		        	d.setVisible(true);
		        	return;
				} // end exam null
				d.setLayout(new BorderLayout()); // set layout for printing exam

				/* Add question cards to cardLayout */ 
				JPanel cards = new JPanel(new CardLayout());
				int numQuestions = currentExam.getNumberOfQuestions();
				
				for (int i=0; i<numQuestions; i++){
					JPanel panel = new JPanel();
					panel.add(new JLabel(currentExam.getQuestionAsHTML(i)));
					cards.add(panel, "Question " + (i+1));
				}
				CardLayout cardLayout = (CardLayout) cards.getLayout();

				/* Add butons to dialog */
				JButton nextButton = new JButton("Next");
				JButton previousButton = new JButton("Prev");
				d.add(nextButton, BorderLayout.EAST);
				d.add(previousButton, BorderLayout.WEST);

				/* add listeners to next button */
				nextButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.next(cards);
					}
				});

				/* add listener to prev button */
				previousButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						cardLayout.previous(cards);
					}
				});
				

				d.add(cards, BorderLayout.CENTER);

				cardLayout.show(cards, "Question "+ 1);
				d.setLocationRelativeTo(frame);
		        d.setSize(550, 250);
		        d.setVisible(true);
			} // end action listener
		});	
	}

	private void addReorderAction(JFrame frame){
		reorderButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JPanel panel = new JPanel();
				JDialog d = new JDialog(frame, "Reorder Questions");

				if (currentExam == null)
					panel.add(new JLabel("No exam loaded. Can't reorder questions"));
				else{
					panel.add(new JLabel("Questions shuffled!"));
					currentExam.reorderQuestions();
				}
				d.add(panel);
				d.setSize(300, 60);
				d.setLocationRelativeTo(frame);
				d.setVisible(true);
				panel.setVisible(true);
			}
		});
	}

	private void addRemoveAction(JFrame frame){
		removeQuestionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JPanel panel = new JPanel();
				JDialog d = new JDialog(frame, "Remove Question");
				int questionNum = -1;

				if (currentExam == null)
					panel.add(new JLabel("No exam loaded. Can't remove any question"));
				else {
					try {
						questionNum = Integer.parseInt(removeQuestionTextField.getText());
						if (!(questionNum >=1 && questionNum <= currentExam.getNumberOfQuestions()))
							questionNum = -1;
					} catch (Exception ex){
						;
					}
					if (questionNum == -1)
						panel.add(new JLabel("Invalid question number"));
					else {
						currentExam.removeQuestion(questionNum - 1);
						panel.add(new JLabel(String.format("Question # %d removed", questionNum)));
					}
				} // end non null exam

				d.add(panel);
				d.setSize(350, 60);
				d.setLocationRelativeTo(frame);
				panel.setVisible(true);
				d.setVisible(true);
			}
		});
	}
	private void addRenameAction(JFrame frame){
		renameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String newName = renameTextField.getText();
				JPanel panel = new JPanel();
				JDialog d = new JDialog(frame, "Rename Exam");
				d.setLayout(new FlowLayout());
				boolean display = false;
				int x = 0; 
				if (newName.isEmpty()){
					panel.add(new JLabel("Exam Title cannot be blank"));
					x = 250;
					display = true;
				}
				else{
					if (currentExam != null)
						currentExam.rename(newName);
					else{
						panel.add(new JLabel(String.format("No previous Exam found. Made new exam with Title [%s]", newName)));
						currentExam = new Exam(newName);
						display = true;
						x = 430;
					}
					examInfo.setText(String.format("Current exam [%s]", currentExam.getName()));
				}
				if (!display)
					return;
				d.add(panel);
				d.setLocationRelativeTo(frame);
				d.setSize(x, 60);
				panel.setVisible(true);
				d.setVisible(true);
				//Timer.sleep(1);
				//d.dispose();
			}
		});
	}

	private void addExitAction(){
		this.exitButton.addActionListener(e -> System.exit(0));
	}

	public void addAllActions(){
		addExitAction();
		addLoadAction(this);
		addRenameAction(this);
		addReorderAction(this);
		addPrintAction(this);
		addSaveAction(this);
		addRemoveAction(this);
		addSAQuestionAction(this);
		addNumQuestionAction(this);
		addMCQuestionAction(this);
	}

	public static void main(String[] args){
		ExamBuilderGUI gui = new ExamBuilderGUI();
		gui.addAllActions();
		gui.display();
	}
}