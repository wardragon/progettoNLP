package interfaces;

import javax.swing.*;
import javax.swing.border.*;

import datamodel.Pair;
import datamodel.Riga;
import datamodel.Voto;
import utils.JsonUtil;
import utils.ParsingUtil;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.*;

public class InterfaceSemantics {

	private static int firstRiga;
	private static int column;
	private static int secondRiga;
	private static String data;
	private static int firstOffsetStart;
	private static int firstOffsetEnd;
	private static List<Voto> listOfVoti=JsonUtil.retrieveVoti();
	private static Random randomGen = new Random();
	private static Map<String,List<Riga>> selectedSet;
	private static String[] configArray={"",""};

	private static int index;
	private static List<Pair> listaCoppie;


	public static void main(String[] args) throws Exception{

		//
		//CONFING SETUP
		//

		config();

		//
		//RETRIEVE DATASET
		//

		List<Riga> listaRighe = JsonUtil.retrieveRighe("dataset.json");

		//
		// FRAME
		//

		JFrame frame = new JFrame("Interfaccia Voto");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280,800);

		//-------------------------------------------------------------------------//


		//
		// LEFT PANEL
		//

		JPanel leftPanel = new JPanel(new GridLayout(4, 1, 10, 15));
		leftPanel.setBorder(new EmptyBorder(20, 30, 10, 10));

		JTextField firstSentence = new JTextField("La prima frase è qui");
		JTextField secondSentence = new JTextField("La seconda frase è qui");
		firstSentence.setEditable(false);
		secondSentence.setEditable(false);

		JPanel buttonsPanel = new JPanel();
		JButton voteOne = new JButton("1: Molto diverse");
		JButton voteTwo = new JButton("2: Diverse");
		JButton voteThree = new JButton("3: Poco diverse");
		JButton voteFour = new JButton("4: Abbastanza simili");
		JButton voteFive = new JButton("5: Simili");
		JButton voteSix = new JButton("6: Molto simili");

		buttonsPanel.add(voteOne);buttonsPanel.add(voteTwo);buttonsPanel.add(voteThree);
		buttonsPanel.add(voteFour);buttonsPanel.add(voteFive);buttonsPanel.add(voteSix);

		JTextArea errorLogBox = new JTextArea("Log",1,2);
		errorLogBox.setEditable(false);

		leftPanel.add(firstSentence);
		leftPanel.add(secondSentence);
		leftPanel.add(buttonsPanel);
		leftPanel.add(errorLogBox);


		//--------------------------------------------------------------------------//


		//
		// RIGHT PANEL
		//

		JPanel rightPanel = new JPanel(new GridLayout(3,4,10,15));
		rightPanel.setBorder(new EmptyBorder(20, 10, 10, 40));

		JPanel firstSentencePanel = new JPanel();
		JLabel firstSentenceLabel = new JLabel("Inizio e fine offset (Riga)");
		JTextField firstSentenceOffsetStart = new JTextField("",3);
		JTextField firstSentenceOffsetEnd = new JTextField("",3);

		JPanel secondSentencePanel = new JPanel();
		JLabel secondSentenceLabel = new JLabel("Colonna");
		JTextField columnBox = new JTextField("",3);

		JLabel signatureLabel = new JLabel("Firma:");
		JTextField signatureBox = new JTextField(configArray[0],10);

		JPanel settingsPanel = new JPanel();

		JButton setButton = new JButton("Set");
		settingsPanel.add(setButton);
		JButton loadCoppie = new JButton("CARICA LISTA COPPIE");
		settingsPanel.add(loadCoppie);

		firstSentencePanel.add(firstSentenceLabel);
		firstSentencePanel.add(firstSentenceOffsetStart);
		firstSentencePanel.add(firstSentenceOffsetEnd);

		secondSentencePanel.add(secondSentenceLabel);
		secondSentencePanel.add(columnBox);
		secondSentencePanel.add(signatureLabel);
		secondSentencePanel.add(signatureBox);

		rightPanel.add(firstSentencePanel);
		rightPanel.add(secondSentencePanel);
		rightPanel.add(settingsPanel);

		//----------------------------------------------------------------------------//

		//
		// BOTTOM PANEL
		//

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(100, 0, 10, 0));

		//--------------------------------------------------------------------------//

		//
		//LEFT BOTTOM PANEL
		//

		JPanel leftBottomPanel = new JPanel(new GridLayout(3, 1, 1, 1));
		leftBottomPanel.setBorder(new EmptyBorder(0, 1, 20, 200));

		JLabel firstSentenceRow = new JLabel("");
		JLabel secondSentenceRow = new JLabel("");

		JLabel sentencesColumn = new JLabel("");

		JLabel counterLabel = new JLabel("Count: ");
		JLabel counterNumber = new JLabel(configArray[1]);

		leftBottomPanel.add(firstSentenceRow);
		leftBottomPanel.add(secondSentenceRow);
		leftBottomPanel.add(counterLabel);
		leftBottomPanel.add(counterNumber);
		leftBottomPanel.add(sentencesColumn);

		//---------------------------------------------------------------------------//

		//
		//RIGHT BOTTOM PANEL
		//

		JPanel rightBottomPanel = new JPanel(new GridLayout(3, 1, 1, 1));
		rightBottomPanel.setBorder(new EmptyBorder(0, 100, 20, 100));

		JPanel rightBottomPanelVote = new JPanel();
		JPanel rightBottomPanelJump = new JPanel();
		JPanel rightBottomPanelSaveButtons = new JPanel();

		JButton saveAndNextInListButton = new JButton("Save and next IN LIST");
		JButton saveAndNextButton = new JButton("Save and next RANDOM");
		JButton saveToFileButton = new JButton("Save to file");

		JLabel choosenVoteLabel = new JLabel("Voto scelto:");

		JTextField choosenVote = new JTextField("",11);
		choosenVote.setEditable(false);

		JLabel jumpBox1Label = new JLabel("Prima Riga:");
		JTextField jumpBox1 = new JTextField("",4);
		JLabel jumpBox2Label = new JLabel("Seconda Riga:");
		JTextField jumpBox2 = new JTextField("",4);
		JLabel jumpBoxColumnLabel = new JLabel("Colonna:");
		JTextField jumpBoxColumn = new JTextField("",4);
		JButton jump = new JButton("Jump");

		rightBottomPanelVote.add(choosenVoteLabel);
		rightBottomPanelVote.add(choosenVote);

		rightBottomPanelJump.add(jumpBox1Label);
		rightBottomPanelJump.add(jumpBox1);
		rightBottomPanelJump.add(jumpBox2Label);
		rightBottomPanelJump.add(jumpBox2);
		rightBottomPanelJump.add(jumpBoxColumnLabel);
		rightBottomPanelJump.add(jumpBoxColumn);
		rightBottomPanelJump.add(jump);

		rightBottomPanelSaveButtons.add(saveAndNextInListButton);
		rightBottomPanelSaveButtons.add(saveAndNextButton);
		rightBottomPanelSaveButtons.add(saveToFileButton);

		rightBottomPanel.add(rightBottomPanelVote);
		rightBottomPanel.add(rightBottomPanelJump);
		rightBottomPanel.add(rightBottomPanelSaveButtons);

		//--------------------------------------------------------------------------//

		//
		// ADDING COMPONENTS TO THE BOTTOM PANEL
		//

		bottomPanel.add(leftBottomPanel, BorderLayout.WEST);
		bottomPanel.add(rightBottomPanel, BorderLayout.EAST);

		//-----------------------------------------------------------------------------//


		//
		// ADDING COMPONENTS TO THE FRAME
		//

		frame.getContentPane().add(BorderLayout.WEST, leftPanel);
		frame.getContentPane().add(BorderLayout.EAST, rightPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
		frame.setVisible(true);

		//-----------------------------------------------------------------------------//

		////////////////////////////////////INIT///////////////////////////////////////////////
		firstOffsetStart =0;
		firstOffsetEnd=listaRighe.size();
		column=0;
		updateSet(listaRighe,firstOffsetStart, firstOffsetEnd, column, errorLogBox);
		nextRandom(listaRighe,firstSentence,secondSentence);
		updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);

		//
		// ACTION LISTENERS
		//

		ActionListener saveAndNext=  e -> {
			saveVote(choosenVote.getText(),signatureBox.getText() ,listaRighe);

			nextRandom(listaRighe,firstSentence,secondSentence);

			updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);
			
			choosenVote.setText("");

			updateCounter(counterNumber);
		};

		ActionListener saveAndNextInList=  e -> {
			saveVote(choosenVote.getText(),signatureBox.getText() ,listaRighe);

			nextOnList(firstSentence, secondSentence, errorLogBox);

			updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);

			choosenVote.setText("");

			updateCounter(counterNumber);

		};

		//		ActionListener randomCheck=  e -> {
		//			if (checkBox.isSelected()) {
		//				rand=true;
		//			}
		//			else {
		//				rand=false;
		//				firstRiga=firstOffsetStart;
		//				secondRiga=secondOffsetStart;
		//				if(firstRiga==secondRiga) {
		//					secondRiga++;
		//				}
		//				column=-1;
		//				nextNotRandom(listaRighe,firstSentence,secondSentence);
		//				updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);
		//				choosenVote.setText("");
		//			}
		//		};

		ActionListener setOffsets=  e -> {
			try {
				int tempStart = Integer.parseInt(firstSentenceOffsetStart.getText());
				int tempEnd = Integer.parseInt(firstSentenceOffsetEnd.getText());
				int tempCol = Integer.parseInt(columnBox.getText());
				if(tempStart < tempEnd &&  tempStart>=0 && tempEnd<=listaRighe.size() && tempCol>=0 && tempCol <= 2) {

					updateSet(listaRighe,tempStart,tempEnd, tempCol,errorLogBox);

					nextRandom(listaRighe,firstSentence,secondSentence);

					updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);

					choosenVote.setText("");
				}else {
					errorLogBox.setText("Invalid offset values");
					System.err.println("Invalid offset values");
				}
			}catch(NumberFormatException ex){
				errorLogBox.setText("Invalid number format");
				System.err.println("Invalid number format");
			}
		};

		ActionListener saveToFile=  e -> {
			JsonUtil.saveVotiOnFile(listOfVoti);
			listOfVoti=JsonUtil.retrieveVoti();
			try {
				updateConfig(signatureBox,counterNumber);
			}
			catch(Exception ex) {
				System.out.println("updateConfig() exception throwed");
			}
		};

		ActionListener loadCoppieListener=  e -> {
			try {
				listaCoppie=JsonUtil.retrieveCoupleList("listaRighe0", listaRighe);

				index=0;
				column=-1;

				nextOnList(firstSentence,secondSentence,errorLogBox);

				updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);

				choosenVote.setText("");

			} catch (IOException e1) {
				e1.printStackTrace();
				errorLogBox.setText(e1.getMessage());
			}

		};

		ActionListener voteListener=  e -> choosenVote.setText(e.getActionCommand());

		ActionListener jumpListener=  e -> {

			saveVote(choosenVote.getText(),signatureBox.getText() ,listaRighe);

			try {
				int jb1 = Integer.parseInt(jumpBox1.getText());
				int jb2 = Integer.parseInt(jumpBox1.getText());
				int jbc = Integer.parseInt(jumpBoxColumn.getText());
				if( jb1>=0 && jb1<=listaRighe.size() && jb2>=0 && jb2<=listaRighe.size() && jbc>=0 && jbc <= 2) {

					firstRiga = jb1;
					column = jbc;
					secondRiga = jb2;

					firstSentence.setText(ParsingUtil.getById(listaRighe, firstRiga).getJth(jbc));

					secondSentence.setText(ParsingUtil.getById(listaRighe, secondRiga).getJth(jbc));

					updatePositionalLabels(firstSentenceRow,secondSentenceRow,sentencesColumn);

					choosenVote.setText("");
				}else {
					errorLogBox.setText("Invalid jump values");
					System.err.println("Invalid jump values");
				}
			}catch(NumberFormatException ex){
				errorLogBox.setText("Invalid number format");
				System.err.println("Invalid number format");
			}
		};

		//------------------------------------------------------------------------------//

		//
		// ADDING LISTENERS
		//

		voteOne.addActionListener(voteListener);
		voteTwo.addActionListener(voteListener);
		voteThree.addActionListener(voteListener);
		voteFour.addActionListener(voteListener);
		voteFive.addActionListener(voteListener);
		voteSix.addActionListener(voteListener);
		setButton.addActionListener(setOffsets);
		saveAndNextButton.addActionListener(saveAndNext);
		saveToFileButton.addActionListener(saveToFile);
		jump.addActionListener(jumpListener);
		loadCoppie.addActionListener(loadCoppieListener);
		saveAndNextInListButton.addActionListener(saveAndNextInList);
		//------------------------------------------------------------------------------//

	}

	private static void nextOnList(JTextField firstSentence, JTextField secondSentence, JTextArea errorLog) {
		if(index<listaCoppie.size()) {
			if(column<2) {
				column++;
				firstSentence.setText(listaCoppie.get(index).getFirstRiga().getJth(column));
				secondSentence.setText(listaCoppie.get(index).getSecondRiga().getJth(column));
			}else {
				index++;
				column=0;
				firstSentence.setText(listaCoppie.get(index).getFirstRiga().getJth(column));
				secondSentence.setText(listaCoppie.get(index).getSecondRiga().getJth(column));
			}

			firstRiga=listaCoppie.get(index).getFirstRiga().getId();
			secondRiga=listaCoppie.get(index).getSecondRiga().getId();
			errorLog.setText("Pair list retrieved. You are now voting on pair number "+index+", column "+resolveColumn(column));
		}

	}

	private static void updateSet(List<Riga> listaRighe, int tempStart, int tempEnd, int tempCol, JTextArea errorLogBox) {

		Map<String,List<Riga>> tempSet=new HashMap<>();

		for(int c=tempStart; c<tempEnd ; c++) {
			tempSet.put(listaRighe.get(c).getData(), new ArrayList<>());
		}
		for(int c=tempStart; c<tempEnd ; c++) {
			tempSet.get(listaRighe.get(c).getData()).add(listaRighe.get(c));
		}

		int numberOfCouples = 0;
		int tempCouples = 0;
		Map<String,List<Riga>> tempSet2=new HashMap<>();

		for (Map.Entry<String,List<Riga>> entry : tempSet.entrySet()) {
			tempCouples = entry.getValue().size();
			if(tempCouples>1) {
				numberOfCouples= numberOfCouples+  ((tempCouples*(tempCouples-1))/2);
				tempSet2.put(entry.getKey(), entry.getValue());
			}
		}
		if(numberOfCouples==0) {
			errorLogBox.setText("No couples in selected offset");
		}else {
			errorLogBox.setText(numberOfCouples+" couples found");
			selectedSet= tempSet2;
			firstOffsetEnd=tempEnd;
			firstOffsetStart=tempStart;
			column=tempCol;

			//            	//print List of dates and number of Righe per date
			//            	List<Integer> listaNum = new ArrayList<>();
			//            	List<String> keysAsArray = new ArrayList<String>(selectedSet.keySet());
			//            	for(String s : keysAsArray) {
			//            		listaNum.add(selectedSet.get(s).size());
			//            	}
			//            	System.out.println(listaNum);
			//            	System.out.println(keysAsArray);
		}
	}

	private static void saveVote(String text,String signature, List<Riga> listaRighe) {
		if(text!= null && text.length()>0) {
			Voto voto = new Voto();
			voto.setFirstId(ParsingUtil.getById(listaRighe, firstRiga).getId());
			voto.setSecondId(ParsingUtil.getById(listaRighe ,secondRiga).getId());
			voto.setColumn(resolveColumn(column));
			voto.setVoto(Integer.parseInt(text.substring(0, 1)));
			voto.setFirstPhrase(ParsingUtil.getById(listaRighe, firstRiga).getJth(column));
			voto.setSecondPhrase(ParsingUtil.getById(listaRighe, secondRiga).getJth(column));
			voto.setAnnotatore(signature);
			voto.setId(ParsingUtil.getId(firstRiga,secondRiga,column));
			listOfVoti.add(voto);
		}
	}

	private static String resolveColumn(int j) {
		if(j==0){
			return "Messaggio docente";
		}
		else if(j==1){
			return "Argomento più interessante";
		}
		else if(j==2){
			return "Argomento meno chiaro";
		}else {
			System.err.println("Internal logic error");
			return "";
		}
	}

	//	private static void nextNotRandom(List<Riga> listaRighe, JTextField firstSentence, JTextField secondSentence) {
	//		if(column<2) {
	//			column++;
	//		}else {
	//			column=0;
	//			if(secondRiga<secondOffsetEnd-1) {
	//				secondRiga++;
	//				if(firstRiga==secondRiga) {
	//					if(secondRiga<secondOffsetEnd-1) {
	//						secondRiga++;
	//					}else {
	//						firstRiga=firstOffsetStart;
	//						secondRiga=secondOffsetStart;
	//						if(firstRiga==secondRiga) {
	//							secondRiga++;
	//						}
	//					}
	//				}
	//			}else {
	//				if(firstRiga<firstOffsetEnd-1) {
	//					firstRiga++;
	//					secondRiga=secondOffsetStart;
	//					if(firstRiga==secondRiga) {
	//						secondRiga++;
	//					}
	//				}else {
	//					firstRiga=firstOffsetStart;
	//					secondRiga=secondOffsetStart;
	//					if(firstRiga==secondRiga) {
	//						secondRiga++;
	//					}
	//				}
	//			}
	//		}
	//		firstSentence.setText(listaRighe.get(firstRiga).getJth(column));
	//		secondSentence.setText(listaRighe.get(secondRiga).getJth(column));
	//	}


	private static void nextRandom(List<Riga> listaRighe, JTextField firstSentence, JTextField secondSentence) {
		List<String> keysAsArray = new ArrayList<String>(selectedSet.keySet());
		data=keysAsArray.get(randomInRange(0, keysAsArray.size()));
		firstRiga=selectedSet.get(data).get(randomInRange(0, selectedSet.get(data).size())).getId();

		if(selectedSet.get(data).size()>1) {
			do {
				secondRiga=selectedSet.get(data).get(randomInRange(0, selectedSet.get(data).size())).getId();
			}while(firstRiga==secondRiga);
		}else {
			secondRiga=selectedSet.get(data).get(randomInRange(0, selectedSet.get(data).size())).getId();
		}

		firstSentence.setText(ParsingUtil.getById(listaRighe, firstRiga).getJth(column));
		secondSentence.setText(ParsingUtil.getById(listaRighe, secondRiga).getJth(column));
	}

	private static void updatePositionalLabels(JLabel firstSentenceRow, JLabel secondSentenceRow, JLabel sentencesColumn) {
		firstSentenceRow.setText("First row: "+firstRiga);
		secondSentenceRow.setText("Second row: "+secondRiga);
		sentencesColumn.setText("Column: "+column);
	}

	private static int randomInRange(int a, int b) {
		return randomGen.nextInt(b - a) + a;
	}

	private static void updateCounter (JLabel counterNumber){
		int c= Integer.parseInt(counterNumber.getText());
		c+=1;
		counterNumber.setText(String.valueOf(c));
	}

	private static void config() throws Exception{
		File file = new File("config.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String line;
		int l=0;

		String[] configContent={"",""};
		configContent[0]=br.readLine();
		configContent[1]=br.readLine();

		String firma;
		String counter;

		firma = configContent[0].substring(6);
		firma = firma.replaceAll("\\s","");

		counter = configContent[1].substring(6);
		counter = counter.replaceAll("\\s","");

		// configArray = {firmaAnnotatore,counter}
		configArray[0]= firma;
		configArray[1]= counter;

		br.close();
	}

	public static void updateConfig(JTextField signatureBox, JLabel counterNumber) throws Exception{
		String firma = "firma:"+signatureBox.getText();
		String count = "count:"+counterNumber.getText();
		File configTxt=new File("config.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(configTxt));
		writer.write(firma);
		writer.newLine();
		writer.write(count);
		writer.close();
	}

}
