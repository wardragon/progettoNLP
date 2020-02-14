package interfaces;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import datamodel.Riga;
import utils.JsonUtil;
import utils.ParsingUtil;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterfacePosTagging {

	//VARIABILI GLOBALI//

	private static int i=0;
	private static int j=0;
	private static String current="";
	private static int wordNumber=0;
	private static List<Riga> modified = new ArrayList<Riga>();

	public static void main(String[] args) {

		//
		//RETRIEVE DATASET
		//

		List<Riga> listaRighe = JsonUtil.retrieveRighe("dataset.json");

		//
		//Creating the Frame
		//

		JFrame frame = new JFrame("Annotazione Frasi");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1050, 500);

		///////////////////////////////////////PANELS////////////////////////////////////////////////

		//
		//MENUBAR
		//

		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("FILE");
		menuBar.add(menuFile);

		JMenuItem salvaModifiche = new JMenuItem("Save changes to file");
		menuFile.add(salvaModifiche);

		//
		// BOTTOM PANEL
		//

		JPanel bottomPanel = new JPanel(); 

		JButton next = new JButton("Save and next");
		bottomPanel.add(next);

		JButton annulla = new JButton("Revert");
		bottomPanel.add(annulla);

		JButton back = new JButton("Go Back");
		bottomPanel.add(back);

		JTextField t = new JTextField("Riga:",6);
		t.setEditable(false);
		bottomPanel.add(t);

		JTextField titoloColonna = new JTextField("Colonna:",19);
		titoloColonna.setEditable(false);
		bottomPanel.add(titoloColonna);

		JButton jumpTo = new JButton("Jump to");
		bottomPanel.add(jumpTo);

		JTextField jumpIndex = new JTextField("",3);
		bottomPanel.add(jumpIndex);


		//-------------------------------------------------------------------------

		//
		//TEXT PANEL
		//

		JPanel textBoxPanel = new JPanel();

		JTextField textBox = new JTextField("Text appears here",60);
		textBoxPanel.add(textBox);

		JLabel sentimentLabel = new JLabel("Sentiment:");
		textBoxPanel.add(sentimentLabel);

		JButton sentimentPositive = new JButton("Positivo");
		JButton sentimentNegative = new JButton("Negativo");
		JButton sentimentNeutral = new JButton("Neutro");
		textBoxPanel.add(sentimentPositive);
		textBoxPanel.add(sentimentNegative);
		textBoxPanel.add(sentimentNeutral);

		JTextField sentimentBox = new JTextField("Sentiment goes here",10);
		sentimentBox.setEditable(false);
		textBoxPanel.add(sentimentBox);

		JTextField signature = new JTextField("Inserire firma Annotatore",20);
		textBoxPanel.add(signature);

		Highlighter highlighter =textBox.getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);

		//-------------------------------------------------------------------------

		//
		//TAGS PANEL
		//

		JPanel tagButtonsPanel = new JPanel();
		JPanel btnPanel = new JPanel(new GridLayout(10, 1, 10, 5));
		JButton bADJ = new JButton("ADJ");
		JButton bADP = new JButton("ADP");
		JButton bADV = new JButton("ADV");
		JButton bAUX = new JButton("AUX");
		JButton bCCONJ = new JButton("CCONJ");
		JButton bDET = new JButton("DET");
		JButton bINTJ = new JButton("INTJ");
		JButton bNOUN = new JButton("NOUN");
		JPanel btnPanel2 = new JPanel(new GridLayout(10, 1, 10, 5));
		JButton bNUM = new JButton("NUM");
		JButton bPART = new JButton("PART");
		JButton bPRON = new JButton("PRON");
		JButton bPROPN = new JButton("PROPN");
		JButton bPUNCT = new JButton("PUNCT");
		JButton bSCONG = new JButton("SCONG");
		JButton bSYM = new JButton("SYM");
		JButton bVERB = new JButton("VERB");
		btnPanel.add(bADJ);btnPanel.add(bADP);btnPanel.add(bADV);btnPanel.add(bAUX);btnPanel.add(bCCONJ);btnPanel.add(bDET);btnPanel.add(bINTJ);btnPanel.add(bNOUN);
		btnPanel2.add(bNUM);btnPanel2.add(bPART);btnPanel2.add(bPRON);btnPanel2.add(bPROPN);btnPanel2.add(bPUNCT);btnPanel2.add(bSCONG);btnPanel2.add(bSYM);btnPanel2.add(bVERB);
		tagButtonsPanel.add(btnPanel);
		tagButtonsPanel.add(btnPanel2);

		JPanel savePanel = new JPanel(new GridLayout(10, 1, 10, 5));
		JButton saveToFILE = new JButton("SAVE TO FILE!");
		savePanel.add(saveToFILE);
		JButton saveToExternalFILE = new JButton("SAVE TO EXTERNAL FILE");
		savePanel.add(saveToExternalFILE);
		tagButtonsPanel.add(savePanel);

		//-------------------------------------------------------------------------

		//
		//Adding Components to the frame.
		//

		frame.getContentPane().add(BorderLayout.NORTH, menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, textBoxPanel);
		frame.getContentPane().add(BorderLayout.EAST, tagButtonsPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
		frame.setVisible(true);

		//
		////////////////////////////////////ACTIONS///////////////////////////////////////////////
		//

		////////////////////////////////////INIT///////////////////////////////////////////////
		current=updateCurrent(listaRighe);
		textBox.setText(current);
		sentimentBox.setText(listaRighe.get(i).getJthSentiment(j));
		initHighlight(highlighter, painter);
		updateDisplayedPosition(t, titoloColonna);

		////////////////////////////////////ACTION LISTENERS///////////////////////////////////////////////
		ActionListener nextListener=  e -> {

			listaRighe.get(i).setJthWords(j,ParsingUtil.getWordList(textBox.getText()));
			listaRighe.get(i).setJthTags(j,ParsingUtil.getTagList(textBox.getText()));
			listaRighe.get(i).setJthSentiment(j, sentimentBox.getText());
			listaRighe.get(i).setAnnotatore(signature.getText());
			Riga thisRiga = ParsingUtil.getById(modified, listaRighe.get(i).getId());
			if(thisRiga != null) {
				modified.remove(thisRiga);
			}
			modified.add(listaRighe.get(i));

			if(e.getActionCommand().equals("Go Back")) {
				decrease();
			}else if(e.getActionCommand().equals("Jump to")){
				try {
					int jumpIndexRetr = Integer.parseInt(jumpIndex.getText());
					if(0<=jumpIndexRetr&&jumpIndexRetr<listaRighe.size()) {
						i=jumpIndexRetr;
						j=0;
					}
				}catch(NumberFormatException ex){
					System.err.println("Invalid number format");
				}
			}else if(i<listaRighe.size()-1 || j<2) {
				increase();
			}

			updateDisplayedPosition(t, titoloColonna);

			current=updateCurrent(listaRighe);
			textBox.setText(current);
			sentimentBox.setText(listaRighe.get(i).getJthSentiment(j));
			wordNumber=0;
			initHighlight(highlighter, painter);

		};


		ActionListener annullaListener=  e -> {
			current=updateCurrent(listaRighe);
			textBox.setText(current);
			wordNumber=0;
			initHighlight(highlighter, painter);
		};

		ActionListener annota=  e -> {
			String val=textBox.getText();

			String[] temp2 = val.split("[\\s,']");
			String[] temp=Arrays.stream(temp2).filter(w -> w!=null && w.length()>0).toArray(String[]::new);
			
			if(wordNumber < temp.length) {

				Matcher m = Pattern.compile("(ADJ:|ADP:|ADV:|AUX:|CCONJ:|DET:|INTJ:|NOUN:|NUM:|PART:|PRON:|PROPN:|PUNCT:|SCONG:|SYM:|VERB:)?(.*)").matcher(temp[wordNumber]);

				temp[wordNumber]=m.replaceFirst(e.getActionCommand()+":$2");

				current= ParsingUtil.conc(temp);
				wordNumber+=1;
				textBox.setText(current);

				if(wordNumber<temp.length) {

					int index1=0;
					for(int c=0;c<wordNumber;c++) {
						index1+=temp[c].length()+1;
					}
					highlighter.removeAllHighlights();
					try {
						highlighter.addHighlight(index1, index1+temp[wordNumber].length(), painter);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
		};

		ActionListener salvaModificheListener=  e -> {
			listaRighe.get(i).setJthWords(j,ParsingUtil.getWordList(textBox.getText()));
			listaRighe.get(i).setJthTags(j,ParsingUtil.getTagList(textBox.getText()));
			listaRighe.get(i).setJthSentiment(j, sentimentBox.getText());
			listaRighe.get(i).setAnnotatore(signature.getText());
			Riga thisRiga = ParsingUtil.getById(modified, listaRighe.get(i).getId());
			if(thisRiga != null) {
				modified.remove(thisRiga);
			}
			modified.add(listaRighe.get(i));
			JsonUtil.saveRigheOnFile(listaRighe,"dataset.json");
		};

		ActionListener salvaEsterneModificheListener=  e -> {
			listaRighe.get(i).setJthWords(j,ParsingUtil.getWordList(textBox.getText()));
			listaRighe.get(i).setJthTags(j,ParsingUtil.getTagList(textBox.getText()));
			listaRighe.get(i).setJthSentiment(j, sentimentBox.getText());
			listaRighe.get(i).setAnnotatore(signature.getText());
			Riga thisRiga = ParsingUtil.getById(modified, listaRighe.get(i).getId());
			if(thisRiga != null) {
				modified.remove(thisRiga);
			}
			modified.add(listaRighe.get(i));
			JsonUtil.saveRigheOnFile(modified,"posOutput.json");
		};

		ActionListener sentimentListener=  e -> {
			sentimentBox.setText(e.getActionCommand());
		};

		//-------------------------------------------------------------------------

		//
		//ADDING LISTENERS
		//

		next.addActionListener(nextListener);
		back.addActionListener(nextListener);
		jumpTo.addActionListener(nextListener);
		salvaModifiche.addActionListener(salvaModificheListener);
		saveToFILE.addActionListener(salvaModificheListener);
		saveToExternalFILE.addActionListener(salvaEsterneModificheListener);
		annulla.addActionListener(annullaListener);
		sentimentPositive.addActionListener(sentimentListener);
		sentimentNegative.addActionListener(sentimentListener);
		sentimentNeutral.addActionListener(sentimentListener);

		bADJ.addActionListener(annota);bADP.addActionListener(annota);bADV.addActionListener(annota);bAUX.addActionListener(annota);
		bCCONJ.addActionListener(annota);bDET.addActionListener(annota);bINTJ.addActionListener(annota);bNOUN.addActionListener(annota);
		bNUM.addActionListener(annota);bPART.addActionListener(annota);bPRON.addActionListener(annota);bPROPN.addActionListener(annota);
		bPUNCT.addActionListener(annota);bSCONG.addActionListener(annota);bSYM.addActionListener(annota);bVERB.addActionListener(annota);


	}

	//-------------------------------------------------------------------------

	//METHODS

	public static String updateCurrent(List<Riga> listaRighe) {
		String result = null;
		if(listaRighe.get(i).getJthWords(j)!=null && listaRighe.get(i).getJthTags(j)!=null) {
			result=ParsingUtil.getReparsed(listaRighe.get(i).getJthTags(j), listaRighe.get(i).getJthWords(j));
		}
		if(result==null) {
			result=listaRighe.get(i).getJth(j);
		}
		return result;
	}

	public static int indexOfFirst(String current) {
		int indSpace =Math.max(0,current.indexOf(' '));
		int indQuote =Math.max(0,current.indexOf('\''));
		if (indSpace==0 && indQuote==0) {
			return current.length();
		}else if(indSpace!=0 && indQuote!=0){
			return Math.min(indSpace,indQuote);
		}else {
			return Math.max(indSpace,indQuote);
		}

	}

	public static void increase() {
		j= (j+1)%3;
		if(j==0) {
			i+=1;
		}
	}

	public static void decrease() {
		if(i>0 || j>0) {
			if(j==0) {
				i-=1;
				j=2;
			}else {
				j-=1;
			}
		}
	}

	public static void updateDisplayedPosition(JTextField t, JTextField titoloColonna) {
		t.setText("Riga: "+Integer.toString(i));
		if(j==0){
			titoloColonna.setText("Colonna: Messaggio docente");
		}
		else if(j==1){
			titoloColonna.setText("Colonna: Argomento più interessante");
		}
		else if(j==2){
			titoloColonna.setText("Colonna: Argomento meno chiaro");
		}
	}

	public static void initHighlight(Highlighter highlighter, HighlightPainter painter) {
		try {
			int indexOfFirst= indexOfFirst(current);

			highlighter.addHighlight(0, indexOfFirst, painter);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}


}
