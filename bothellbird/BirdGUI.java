package bothellbird;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;

public class BirdGUI extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7801651004788925924L;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel control;
	private JTextArea txtrTest;
	private JLabel label;
	private JLabel namesLabel;
	private JButton[] btnBirdPic;
	private BirdNameRetriever getStuff;
	private int birdId;
	private ImageIcon male;
	private ImageIcon female;
	private ImageIcon am;
	private JButton back;
	private JScrollPane jscroll = new JScrollPane();
	private String named = "Bird Names: ";

	/**
	 * Launch the application.
	 */
	class MakeSound { 
		 
	    private final int BUFFER_SIZE = 128000; 
	    private File soundFile; 
	    private AudioInputStream audioStream; 
	    private AudioFormat audioFormat; 
	    private  SourceDataLine sourceLine; 
	 
	    /** 
	     *  
	     * @param filename the name of the file that is going to be played 
	     * 
	     */ 
	    public void playSound(String filename){ 
	 
	        String strFilename = filename; 
	 
	        try { 
	            soundFile = new File(strFilename); 
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	            System.exit(1); 
	        } 
	 
	        try { 
	            audioStream = AudioSystem.getAudioInputStream(soundFile); 
	        } catch (Exception e){ 
	            e.printStackTrace(); 
	           System.exit(1); 
	        } 
	 
	        audioFormat = audioStream.getFormat(); 
	 
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat); 
	        try { 
	            sourceLine = (SourceDataLine) AudioSystem.getLine(info); 
	            sourceLine.open(audioFormat); 
	        } catch (LineUnavailableException e) { 
	            e.printStackTrace(); 
	            System.exit(1); 
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	            System.exit(1); 
	        } 
	 
	        sourceLine.start(); 
	 
	        int nBytesRead = 0; 
	        byte[] abData = new byte[BUFFER_SIZE]; 
	        while (nBytesRead != -1) { 
	            try { 
	                nBytesRead = audioStream.read(abData, 0, abData.length); 
	            } catch (IOException e) { 
	                e.printStackTrace(); 
	            } 
	            if (nBytesRead >= 0) { 
	                @SuppressWarnings("unused") 
	                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead); 
	            } 
	        } 
	 
	        sourceLine.drain(); 
	        sourceLine.close(); 
	    } 
	} 
	class SoundAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			MakeSound play = new MakeSound();
			play.playSound("0a0.wav");		
		}
		
	}

	class closeAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			disposer();		
		}
		
	}

	/**
	 * Create the frame.
	 */
	public BirdGUI(int ID, ArrayList<String> common) 
	{
		birdId = ID;
		for(String numName : common)
		{
			named += numName + ", ";
		}
		initComponents();
	
	}
	private void initComponents()
	{
		panel = new JPanel();
		panel.setLayout(new GridLayout());
		panel_1 = new JPanel();
		control = new JPanel();
		txtrTest = new JTextArea();
		getBirdData();
		getText();
		back = new JButton("Close Bird display and return to Bothell Bird");
		ActionListener close = new closeAction(); 
		back.addActionListener(close);
		control.add(back);
		this.setLayout(new BorderLayout());
		this.add(control, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.add(panel_1, BorderLayout.SOUTH);
	}
	public void disposer()
	{
		this.dispose();
	}
	private void getBirdData()
	{
		getStuff = new BirdNameRetriever();
		GenderRetriever sex = new GenderRetriever();
		String scientificName = "";
		try {
			scientificName = getStuff.getScientificName(birdId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedHashMap<String, String> genders = new LinkedHashMap<String, String>();
		try {
			genders = sex.getGender(birdId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Collection<String> named =  genders.keySet();
		ArrayList<String> names = new ArrayList<String>();
		names.addAll(named);
		ArrayList<JLabel> namesLabel = new ArrayList<JLabel>();
		ArrayList<String> labelTxt = new ArrayList<String>();
		labelTxt.add(this.named + " Bird Species: " + scientificName);
		int i = names.size();
		btnBirdPic = new JButton[i];
		int k = 0;
		ActionListener soundOff = new SoundAction();
		for(String name : names)
		{
			ImageMaker maker = new ImageMaker();
			SoundMaker sMaker = new SoundMaker();
			String gen = genders.get(name);
			btnBirdPic[k] = new JButton("");
			btnBirdPic[k].setMargin(new Insets(0, 0, 0, 0));
			btnBirdPic[k].addActionListener(soundOff);
			if(gen.charAt(0) == 'm' || gen.charAt(0) == 'M')
			{
				name.concat(" ,(Male)");
				labelTxt.add(name);
				namesLabel.add(new JLabel(labelTxt.get(k)));
				panel.add(namesLabel.get(k));
				try {
					male = maker.bigImage(birdId, sex.getNameId(birdId, name));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				btnBirdPic[k].setIcon(male);
				panel.add(btnBirdPic[k]);
				try {
					File maleSound = sMaker.getSound(birdId, sex.getNameId(birdId, name));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(gen.charAt(0) == 'f' || gen.charAt(0) == 'F')
			{
				name.concat(" ,(Female)");
				labelTxt.add(name);
				namesLabel.add(new JLabel(labelTxt.get(k)));
				panel.add(namesLabel.get(k));
				try {
					female = maker.bigImage(birdId, sex.getNameId(birdId, name));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				btnBirdPic[k].setIcon(female);
				panel.add(btnBirdPic[k]);
				try {
					File femaleSound = sMaker.getSound(birdId, sex.getNameId(birdId, name));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					am = maker.bigImage(birdId, sex.getNameId(birdId, name));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				labelTxt.add(name);
				namesLabel.add(new JLabel(labelTxt.get(k)));
				panel.add(namesLabel.get(k));
				btnBirdPic[k].setIcon(am);
				panel.add(btnBirdPic[k]);
				try {
					File amSound = sMaker.getSound(birdId, sex.getNameId(birdId, name));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			k++;
		}	
	}
	
	private void getText()
	{
		String description = "";
		descriptionMaker descr = new descriptionMaker();
		try {
			description = descr.getDescription(birdId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		txtrTest.setText(description);
		txtrTest.setEditable(false);
		jscroll = new JScrollPane(txtrTest, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel_1.add(jscroll);
		revalidate();
	}
}
