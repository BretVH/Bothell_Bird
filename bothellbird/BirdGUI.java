package bothell_bird;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BirdGUI extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 7801651004788925924L;
    private JPanel birdGUIMainPanel;
    private JPanel birdDescriptionPanel;
    private JPanel controlsPanel;
    private JTextArea birdDescriptionTextArea;
    private JButton[] birdPicButtons;
    private final int birdId;
    private ImageIcon image;
    private JButton back;
    private JScrollPane jscroll = new JScrollPane();
    private String namesBirdIsKnownBy = "Bird Names: ";
    private LinkedHashMap<String, String> birdIdToGenderSpecificNameMap;

    /**
     * Create application GUI.
     *
     * @param birdId
     * @param commonNames
     */
    public BirdGUI(int birdId, ArrayList<String> commonNames) {
        this.birdId = birdId;
        for (String currentName : commonNames) {
            namesBirdIsKnownBy += currentName + ", ";
        }
        try {
            initComponents();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(BirdGUI.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private void initComponents() throws SQLException, IOException {
        birdGUIMainPanel = new JPanel();
        birdGUIMainPanel.setLayout(new GridLayout());
        birdDescriptionPanel = new JPanel();
        controlsPanel = new JPanel();
        birdDescriptionTextArea = new JTextArea();

        Iterable<String> commonNames = getAliases();
        String birdDescription = getDescriptionText();
        setBirdDescription(birdDescription);
        revalidate();
        populateBirdPicButtons(commonNames);
        initControlsPanel();
        this.setLayout(new BorderLayout());
        this.add(controlsPanel, BorderLayout.NORTH);
        this.add(birdGUIMainPanel, BorderLayout.CENTER);
        this.add(birdDescriptionPanel, BorderLayout.SOUTH);
    }

    public void disposer() {
        this.dispose();
    }

    private List<String> getAliases() throws SQLException {
        birdIdToGenderSpecificNameMap = GenderRetriever.getGender(birdId);
        Collection<String> birdAliases = birdIdToGenderSpecificNameMap.keySet();
        List<String> commonNames = new ArrayList<>();
        commonNames.addAll(birdAliases);
        return commonNames;
    }

    private String getDescriptionText() throws SQLException {
        return DescriptionRetriever.getDescription(birdId);
    }

    private void setBirdDescription(String description) {
        birdDescriptionTextArea.setText(description);
        birdDescriptionTextArea.setEditable(false);
        jscroll = new JScrollPane(birdDescriptionTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        birdDescriptionPanel.add(jscroll);
    }

    private void initControlsPanel() {
        back = new JButton("Close Bird display and return to Bothell Bird");
        ActionListener close = new closeAction();
        back.addActionListener(close);
        controlsPanel.add(back);
    }

    private void populateBirdPicButtons(Iterable<String> commonNames) throws SQLException, IOException {
        List<JLabel> birdPicButtonLabels = new ArrayList<>();
        List<String> birdPicButtonLabelsText;
        birdPicButtonLabelsText = new ArrayList<>();
        String scientificName = BirdsListRetriever.getScientificName(birdId);
        birdPicButtonLabelsText.add(this.namesBirdIsKnownBy + " Bird Species: " + scientificName);
        int numberOfNames = ((List<String>) commonNames).size();
        birdPicButtons = new JButton[numberOfNames];
        ActionListener soundActionListener = new SoundAction();

        int counter = 0;

        for (String name : commonNames) {
            String genderSpecificName = birdIdToGenderSpecificNameMap.get(name);
            StringBuilder aliases = new StringBuilder(name);
            char sex = genderSpecificName.toLowerCase().charAt(0);
            birdPicButtons[counter] = new JButton("");
            birdPicButtons[counter].setMargin(new Insets(0, 0, 0, 0));
            birdPicButtons[counter].addActionListener(soundActionListener);
            switch (sex) {
                case 'f':
                    aliases.append(" ,(Female)");
                    makeButton(counter, aliases, birdPicButtonLabels, birdPicButtonLabelsText);
                    break;
                case 'm':
                    aliases.append(" ,(Male)");
                    makeButton(counter, aliases, birdPicButtonLabels, birdPicButtonLabelsText);
                    break;
                default:
                    makeButton(counter, aliases, birdPicButtonLabels, birdPicButtonLabelsText);
            }
            birdPicButtonLabels.get(counter).setText(birdPicButtonLabelsText.get(counter));
            birdGUIMainPanel.add(birdPicButtonLabels.get(counter));
            counter++;
        }
    }

    private void makeButton(int counter, StringBuilder aliases, List<JLabel> birdPicButtonLabels, List<String> birdPicButtonLabelsText) throws SQLException, IOException {
        image = ImageRetriever.bigImage(birdId, GenderRetriever.getNameId(birdId, aliases.toString()));
        birdPicButtonLabelsText.add(aliases.toString());
        birdPicButtonLabels.add(new JLabel(birdPicButtonLabelsText.get(counter)));
        birdGUIMainPanel.add(birdPicButtonLabels.get(counter));
        birdPicButtons[counter].setIcon(image);
        birdGUIMainPanel.add(birdPicButtons[counter]);
    }

    class SoundAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            MakeSound.playSound("0a0.wav");
        }
    }

    class closeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            disposer();
        }
    }
}
