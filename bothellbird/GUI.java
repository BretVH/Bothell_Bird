/**
 *
 */
package bothell_bird;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/**
 * @author Bret Van Hof
 *
 */
public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * @param args//
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            new GUI().setVisible(true);
        });
    }
    private JButton search;
    private JTextField searchBox;
    private JList<?> birdsJList;
    private JScrollPane jScrollPane;
    private JPanel listPanel;
    private ArrayList<ImageIcon> birdIcons;
    private ArrayList<BirdInfo> birdNames;
    private ArrayList<BirdInfo> updatedBirdData;
    private JPanel searchPanel;
    private JPanel imagePanel;
    private JButton searchByFeatures;
    private JLabel pic;
    private JButton resetList;
    private JPanel buttonPanel;
    private JButton displayBird;
    private Map<Integer, String> birdIdToBirdName;
    private Set<BirdInfo> birds;
    private ListCellRenderer lr;
    private boolean hasAdded = false;
    private ActionListener listener;
    private JButton next = new JButton("Select Feature and Continue Search");
    private final JLabel featuresJlistJLabel;
    private JList<String> selectableFeaturesJList = new JList<>();
    private final DefaultListModel<String> jListModel;
    private Map<String, ArrayList<String>> featureToSelectableFeatureMap;
    private final HashMap<String, Integer> birdNameToBirdIdMap;

    /**
     * @throws HeadlessException
     */
    public GUI() throws HeadlessException {
        this.birdNameToBirdIdMap = new HashMap<>();
        this.featureToSelectableFeatureMap = new HashMap<>();
        this.jListModel = new DefaultListModel<>();
        this.featuresJlistJLabel = new JLabel();
        this.birds = new LinkedHashSet<>();
        setVisible(true);
        populateBirdNameToBirdIdMap();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        setName("BothellBirder by Team 5");
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage("defaultBird.jpg")); //sets icon
        setVisible(true);
        setContentPane(new JDesktopPane());
        initComponents();
        revalidate();
    }

    private String[] getFeatureNames() {
        String[] featureNames = new String[featureToSelectableFeatureMap.size()];
        int z = 0;
        for (Map.Entry<String, ArrayList<String>> entry : featureToSelectableFeatureMap.entrySet()) {
            featureNames[z] = entry.getKey();
            z++;
        }
        return featureNames;
    }

    private int[] getFeatureIds(ArrayList<String> selectedFeature) {
        //selectedFeature is a list of feature names followed by that...
        //feature ids,  we only want the ids...
        int arraySize = (selectedFeature.size() / 2) + 1;
        int[] featureIDs = new int[arraySize];
        int counter = 0;
        //get ids.
        for (int g = 1; g < featureIDs.length; g += 2) {
            int featureID = Integer.parseInt(selectedFeature.get(g));
            featureIDs[counter] = featureID;
            counter++;
        }
        return featureIDs;
    }

    private void populateBirdNameToBirdIdMap() {
        birdNames = new ArrayList<>();
        birdIcons = new ArrayList<>();

        try {
            birdNames = BirdNameRetriever.readData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Can't connect to database!",
                    "", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(null, "Using offline data");
            BirdInfo bird = new BirdInfo("CondorMan", 0, 0);
            birdNames.add(bird);
        }
        for (BirdInfo birdInfo : birdNames) {
            birdNameToBirdIdMap.put(birdInfo.getName(), birdInfo.getBirdId());
        }
    }

    public ActionListener makeThis(String[] a, int b, int[] c) {
        ActionListener nextAction = new Next(a, b, c);
        return nextAction;
    }

    private void createFeaturesJList(int selectedFeaturesIndex) throws SQLException {
        featureToSelectableFeatureMap = DescriptionRetriever.getFeatures();
        String[] featureNames = getFeatureNames();
        ArrayList<String> selectedFeature = featureToSelectableFeatureMap.get(featureNames[selectedFeaturesIndex]);
        int[] featureIds = getFeatureIds(selectedFeature);
        //remove ids from selected features, leave only names....
        ///Description retriever needs refactored...featureToSelctableFeatureMap
        //Should be a Map to BirdFeature objects and a BirdFeature class
        //should be introduced...
        for (int index = 0; index < selectedFeature.size() - 1; index++) {
            selectedFeature.remove(index + 1);
        }
        int i = 0;
        jListModel.clear();
        for (String featureName : selectedFeature) {
            jListModel.add(i, featureName);
            i++;
        }
        selectableFeaturesJList = new JList<>((String[]) selectedFeature.toArray());
        selectableFeaturesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectableFeaturesJList.setModel(jListModel);
        selectableFeaturesJList.setSelectedIndex(0);
        JScrollPane jScrollPanes = new javax.swing.JScrollPane(selectableFeaturesJList);
        next = new JButton("Next set of criteria");
        imagePanel.removeAll();
        JLabel aFeatureName = new JLabel(featureNames[selectedFeaturesIndex]);
        imagePanel.add(aFeatureName);
        imagePanel.add(featuresJlistJLabel);
        imagePanel.add(jScrollPanes);
        listener = makeThis(featureNames, selectedFeaturesIndex, featureIds);
        next.addActionListener(listener);
        imagePanel.add(next);
        imagePanel.revalidate();
        revalidate();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        createJPanels();
        createButtons();
        createSearchBox();
        createList();
    }

    private void createSearchBox() {
        pic = new JLabel(new ImageIcon("defaultBird.jpg"));
        imagePanel.add(pic);
        searchBox = new JTextField();
        searchBox.setText("Enter Bird Name Here");
        searchBox.setColumns(20);
        searchPanel.add(searchBox);
        searchBox.grabFocus();
    }

    private void createJPanels() {
        listPanel = new JPanel();
        imagePanel = new JPanel();
        searchPanel = new JPanel();
        buttonPanel = new JPanel();
        setLayout(new BorderLayout());
        add(listPanel, BorderLayout.WEST);
        add(searchPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        setName("BothellBirder by Team 5");
    }

    private void createButtons() {
        search = new JButton("Search by name");
        ActionListener nameSearches = new NameSearch();
        search.addActionListener(nameSearches);
        searchByFeatures = new JButton("Search by features");
        displayBird = new JButton("Display Bird Selected in List");
        ActionListener dis = new Birder();
        displayBird.addActionListener(dis);
        ActionListener feat = new Searcher();
        searchByFeatures.addActionListener(feat);
        resetList = new JButton("Reset List to default (clear features search)");
        ActionListener reset = new Reseter();
        resetList.addActionListener(reset);
        searchPanel.add(search);
        buttonPanel.add(searchByFeatures);
        buttonPanel.add(resetList);
        buttonPanel.add(displayBird);
    }

    private void updateList(ArrayList<BirdInfo> listOfBirds) {
        int k = 0;
        Integer[] birdID = new Integer[listOfBirds.size()];
        for (BirdInfo IDa : listOfBirds) {
            birdID[k] = (Integer) IDa.getNameId();
            k++;
        }
        birdsJList = new JList<>(birdID);
        birdsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        birdsJList.setSelectedIndex(0);
        birdsJList.setVisibleRowCount(3);
        jScrollPane = new javax.swing.JScrollPane(birdsJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.removeAll();
        birdsJList.setCellRenderer(lr);
        listPanel.add(jScrollPane, java.awt.BorderLayout.WEST);
        listPanel.revalidate();
        getContentPane().revalidate();
    }

    private void createList() {
        Map<String, ImageIcon> birdNameToBirdIconMap = getInitJListData();
        Integer[] birdIds = new Integer[birdNames.size()];
        int i = 0;
        for (BirdInfo ID : birdNames) {
            birdIds[i] = (Integer) ID.getNameId();
            i++;
        }
        birdsJList = new JList<>(birdIds);
        birdsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        birdsJList.setSelectedIndex(0);
        birdsJList.setVisibleRowCount(3);
        jScrollPane = new javax.swing.JScrollPane(birdsJList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(jScrollPane, java.awt.BorderLayout.WEST);
        lr = new ListRenderer(birdNameToBirdIconMap, birdIdToBirdName);
        birdsJList.setCellRenderer(lr);
    }

    private Map<String, ImageIcon> getInitJListData() {
        Map<String, ImageIcon> birdNameToBirdIconMap = new HashMap<>();
        birdIcons = new ArrayList<>();
        ImageIcon test = new ImageIcon("0a0.jpg");
        birdIdToBirdName = new HashMap<>();
        int index = 0;
        for (BirdInfo name : birdNames) {
            try {
                birdIcons.add(ImageRetriever.readData(name.getBirdId(), name.getNameId()));
            } catch (SQLException e) {
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            birdNameToBirdIconMap.put(name.getName(), birdIcons.get(index));
            birdIdToBirdName.put(name.getNameId(), name.getName());
            index++;
        }
        if (birdNames.isEmpty()) {
            birdNameToBirdIconMap.put("empty", test);
        }
        return birdNameToBirdIconMap;
    }

    class NameSearch implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String birdName = searchBox.getText();
            if (birdNameToBirdIdMap.get(birdName) != null) {
                ArrayList<String> names = new ArrayList<>();
                for (BirdInfo aName : birdNames) {
                    if (aName.getBirdId() == birdNameToBirdIdMap.get(birdName)) {
                        names.add(aName.getName());
                    }
                }
                JFrame display = new BirdGUI(birdNameToBirdIdMap.get(birdName), names);
                display.setVisible(true);
                setName("BothellBirder by Team 5");
                display.setBounds(30, 30, 800, 600);
                display.toFront();
            } else {
                JOptionPane.showMessageDialog(null, "Bird Not Found!",
                        "", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class Reseter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            listPanel.removeAll();
            createList();
            hasAdded = false;
            listPanel.revalidate();
        }
    }

    class Birder implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int birdId = 0;
            ArrayList<String> names = new ArrayList<>();
            for (BirdInfo birdName : birdNames) {
                if (birdName.getNameId() == (Integer) birdsJList.getSelectedValue()) {
                    birdId = birdName.getBirdId();
                }
            }
            for (BirdInfo aName : birdNames) {
                if (aName.getBirdId() == birdId) {
                    names.add(aName.getName());
                }
            }
            if (birdId != 0) {
                JFrame display = new BirdGUI(birdId, names);
                display.setVisible(true);
                setName("BothellBirder by Team 5");
                display.setBounds(30, 30, 800, 600);
                display.toFront();
            }
        }
    }

    class Searcher implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                createFeaturesJList(0);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class Next implements ActionListener {

        private final String[] featureNames;
        private int theIndexOfTheCurrentFeature;
        private final int[] IDS;

        Next(String[] a, int index, int[] counter) {
            featureNames = a;
            theIndexOfTheCurrentFeature = index;
            IDS = counter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (theIndexOfTheCurrentFeature >= featureNames.length - 1) {
                imagePanel.removeAll();
                imagePanel.add(pic);
                imagePanel.revalidate();
                theIndexOfTheCurrentFeature = 0;
                hasAdded = false;
            } else {
                try {
                    int featureId = BirdNameRetriever.getFeatureID(featureNames[theIndexOfTheCurrentFeature]);
                    int featureIndex = selectableFeaturesJList.getSelectedIndex() + 1;

                    updatedBirdData = BirdNameRetriever.updateData(featureId, featureIndex);

                    if (!hasAdded) {
                        birds = new LinkedHashSet<>(updatedBirdData);
                        hasAdded = true;
                        ArrayList<BirdInfo> updatedList;
                        updatedList = new ArrayList<>();
                        updatedList.addAll(birds);
                    } else {
                        BirdInfo[] b = new BirdInfo[birds.size()];
                        int theIndex = 0;
                        for (BirdInfo a : birds) {
                            boolean stillExists = false;

                            for (int z = 0; z < updatedBirdData.size(); z++) {
                                if (updatedBirdData.get(z).getBirdId() == a.getBirdId()) {
                                    stillExists = true;
                                }
                            }
                            if (!stillExists) {
                                b[theIndex] = a;
                                theIndex++;
                            }
                        }
                        for (int j = 0; j < theIndex; j++) {
                            birds.remove(b[j]);
                        }
                    }

                    birdsJList.removeAll();
                    Integer[] birdID;
                    birdID = new Integer[birds.size()];

                    int k = 0;
                    ArrayList<BirdInfo> updatedList = new ArrayList<>();
                    for (BirdInfo ID : birds) {
                        birdID[k] = (Integer) ID.getBirdId();
                        k++;
                    }
                    updatedList.addAll(birds);
                    updateList(updatedList);
                    createFeaturesJList(theIndexOfTheCurrentFeature + 1);
                } catch (SQLException e1) {
                }
            }
        }
    }

}
