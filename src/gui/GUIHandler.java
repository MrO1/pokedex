package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class GUIHandler {

	private JTextField nameEntry;
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel imagePanel;
	
	private JLabel imageIcon;
	
	private JPanel widgetPanel;
	
	
	private JButton searchButton;
	private JButton quitButton;
	private JTextPane currentShowingData;
	private JScrollPane currentShowingJSP;
	private JPanel textPanel;
	private JTextPane data;
	private JScrollPane jsp;
	
	
	public GUIHandler(){
		
	}
	
	public void setUpGUI(){
		
		frame = new JFrame();
		
		
		mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		imagePanel = new JPanel();
		c.gridx = 0;
		c.gridy = 0;
		
		imageIcon = new JLabel();
		imageIcon.setPreferredSize(new Dimension(300, 300));
		imagePanel.add(imageIcon);
		
		mainPanel.add(imagePanel, c);
		
		widgetPanel = new JPanel(new BorderLayout());
		c.gridx = 1; // columns
		c.gridy = 0; // rows
		c.gridwidth = 2;
		c.insets = new Insets(10,0,0,0);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		nameEntry = new JTextField();
		widgetPanel.add(nameEntry, BorderLayout.PAGE_START);
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchForQuery(nameEntry.getText());				
			}
			
		});
		widgetPanel.add(searchButton, BorderLayout.LINE_START);
		
		//Space holder
		widgetPanel.add(Box.createRigidArea(new Dimension(35,0)));
		
		quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() { 
		    public void actionPerformed(ActionEvent e) { 
		        System.exit(0);
		    } 
		});
		widgetPanel.add(quitButton, BorderLayout.LINE_END);
		
		currentShowingData = new JTextPane();
		currentShowingJSP = new JScrollPane(currentShowingData);
		currentShowingData.setEditable(false);
		currentShowingData.setContentType("text/html");
		currentShowingData.setPreferredSize(new Dimension(200, 60));
		currentShowingData.setText("<center><u>Currently Showing:</u> <br> Number &thinsp; &thinsp; Name   <br> Type </center>");

		widgetPanel.add(currentShowingJSP, BorderLayout.PAGE_END);
		mainPanel.add(widgetPanel, c);
		
		
		textPanel = new JPanel();
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.insets = new Insets(10,10,10,10);
		
		data = new JTextPane();
		jsp = new JScrollPane(data);
		data.setEditable(false);
		data.setContentType("text/html");
//		data.setText("");
		data.setPreferredSize( new Dimension(400, 100));
//		textPanel.add(data);
		textPanel.add(jsp);
		
		mainPanel.add(textPanel, c);

		frame.add(mainPanel);
		frame.pack();
        frame.setVisible(true);
	}
	
	
	public void searchForQuery(String monsterName){
		System.out.println(monsterName);
		if (monsterName.equals("Unknown")){
			monsterName = monsterName.replace("Unknown", "Unown");
		}
		Document doc = null;
		try {
			doc = Jsoup.connect("http://bulbapedia.bulbagarden.net/wiki/" + monsterName + "_(Pokemon)").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements nameElements = doc.select("#firstHeading");
		Elements numberElements = doc.select("#mw-content-text > table:nth-child(3) > tbody > tr:nth-child(1) > td > table > tbody > tr:nth-child(1) > th > big > big > a > span");
		Elements typeElements = doc.select("#mw-content-text > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(1) > table > tbody > tr");
		Elements biologyElements = doc.select("#mw-content-text > p");
		
		
		
		String name = Jsoup.clean(nameElements.first().text(), Whitelist.none()).replace("(Pokémon)", "");
		String number = Jsoup.clean(numberElements.text(), Whitelist.none());
		String type = Jsoup.clean(typeElements.text(), Whitelist.none());
		String biology = Jsoup.clean(biologyElements.text(), Whitelist.none());
		
		if (type.contains(" ")){
			type = type.replace(" ", ", ");
		}
		if (type.contains("Unknown")){
			type = type.replace(", Unknown", "");
		}
		//"<center><u>Currently Showing:</u> <br> Number &thinsp; &thinsp; Name   <br> Type </center>"
		currentShowingData.setText("<center><u>Currently Showing:</u> <br>" + number + " &thinsp; &thinsp; " +  name + " <br>" + type + "</center>");
		data.setText(biology);
		System.out.println(biology);
		System.out.println(name + " : " + number + " : " + type);
		
		
		//Image
		Image image = null;
		Elements imageElements = doc.select("#mw-content-text > table:nth-child(3) > tbody > tr:nth-child(1) > td > table > tbody > tr:nth-child(2) > td > table > tbody > tr:nth-child(1) > td > a > img");
		String urlImage = imageElements.first().absUrl("src");
		System.out.println(urlImage);
		
		try{
			URL url = new URL(urlImage);
			image = ImageIO.read(url);
		}catch(Exception e){
			System.out.println(e);
		}
		
		imageIcon.setIcon(new ImageIcon(image));

	}
	

}
