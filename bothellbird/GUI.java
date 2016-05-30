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
    private ImageIcon pic;
    private ArrayList<ImageIcon> birdIcons;
    private ArrayList<BirdInfo> birdNames;
    private ArrayList<BirdInfo> updatedBirdData;
    private JPanel searchPanel;
    private JPanel imagePanel;
    private JButton searchByFeatures;
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
    private JLabel defaultPicture;
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
        setName("BothellBirder \u00a9 Bret Van Hof");
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage("defaultBird.jpg")); //sets icon
        setVisible(true);
        setContentPane(new JDesktopPane());
        try {
            initComponents();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        //selectedFeature is a list of feature names followed by...
        //feature ids,  we only want the ids...needs refactored
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

    private void initComponents() throws IOException, SQLException {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        createJPanels();
        defaultPicture = new JLabel(new ImageIcon("defaultBird.jpg"));
        imagePanel.add(defaultPicture);
        createButtons();
        addButtonsToPanels();
        createSearchBox();
        createJList(birdNames);
    }

    private void createSearchBox() {
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
        setName("BothellBirder \u00a9 Bret Van Hof");
    }

    private void createButtons() {
        search = new JButton("Search by name");
        ActionListener nameSearchListener = new NameSearchListener();
        search.addActionListener(nameSearchListener);
        searchByFeatures = new JButton("Search by features");
        displayBird = new JButton("Display Bird Selected in List");
        ActionListener birdSelectListener = new Birder();
        displayBird.addActionListener(birdSelectListener);
        ActionListener featureSearchListener = new FeatureSearchListener();
        searchByFeatures.addActionListener(featureSearchListener);
        resetList = new JButton("Reset List to default (clear features search)");
        ActionListener resetListener = new ResetListener();
        resetList.addActionListener(resetListener);
    }

    private void updateJList(ArrayList<BirdInfo> listOfBirds) throws IOException, SQLException {
        createJList(listOfBirds);
        listPanel.revalidate();
        getContentPane().revalidate();
    }

    private void createJList(ArrayList<BirdInfo> listOfBirds) throws IOException, SQLException {
        Map<String, ImageIcon> birdNameToBirdIconMap = getInitJListData();
        Integer[] birdIds = getBirdIds(listOfBirds);
        listPanel.removeAll();
        birdsJList = createBirdsJList(birdIds);
        jScrollPane = new javax.swing.JScrollPane(birdsJList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(jScrollPane, java.awt.BorderLayout.WEST);
        if (lr == null) {
            lr = new ListRenderer(birdNameToBirdIconMap, birdIdToBirdName);
        }
        birdsJList.setCellRenderer(lr);
    }

    private Map<String, ImageIcon> getInitJListData() throws IOException, SQLException {
        Map<String, ImageIcon> birdNameToBirdIconMap = new HashMap<>();
        birdIcons = new ArrayList<>();
        ImageIcon test = new ImageIcon("0a0.jpg");
        birdIdToBirdName = new HashMap<>();
        int index = 0;
        for (BirdInfo name : birdNames) {
            birdIcons.add(ImageRetriever.readData(name.getBirdId(), name.getNameId()));
            birdNameToBirdIconMap.put(name.getName(), birdIcons.get(index));
            birdIdToBirdName.put(name.getNameId(), name.getName());
            index++;
        }
        if (birdNames.isEmpty()) {
            birdNameToBirdIconMap.put("empty", test);
        }
        return birdNameToBirdIconMap;
    }

    private JList<?> createBirdsJList(Integer[] birdIds) {
        JList<?> birdsJList = new JList<>(birdIds);
        birdsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        birdsJList.setSelectedIndex(0);
        birdsJList.setVisibleRowCount(3);
        return birdsJList;
    }

    private Integer[] getBirdIds(ArrayList<BirdInfo> birdNames) {
        Integer[] birdIds = new Integer[birdNames.size()];
        int count = 0;
        for (BirdInfo ID : birdNames) {
            birdIds[count] = (int) ID.getNameId();
            count++;
        }
        return birdIds;
    }

    private void addButtonsToPanels() {
        searchPanel.add(search);
        buttonPanel.add(searchByFeatures);
        buttonPanel.add(resetList);
        buttonPanel.add(displayBird);
    }

    private void displayBirdGUI(int birdId, ArrayList<String> names, String name) {
        JFrame display = new BirdGUI(birdId, names);
        display.setVisible(true);
        setName(name + " \u00a9 Bret Van Hof");
        display.setBounds(30, 30, 800, 600);
        display.toFront();
    }

    class NameSearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String birdName = searchBox.getText();
            if (birdNameToBirdIdMap.get(birdName) != null) {
                int birdId = birdNameToBirdIdMap.get(birdName);
                ArrayList<String> names = new ArrayList<>();
                for (BirdInfo aName : birdNames) {
                    if (aName.getBirdId() == birdNameToBirdIdMap.get(birdName)) {
                        names.add(aName.getName());
                    }
                }
                displayBirdGUI(birdId, names, birdName);
            } else {
                JOptionPane.showMessageDialog(null, "Bird Not Found!",
                        "", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class ResetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            listPanel.removeAll();
            try {
                createJList(birdNames);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            hasAdded = false;
            listPanel.revalidate();
        }
    }

    class Birder implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int birdId = 0;
            String name = "";
            ArrayList<String> names = new ArrayList<>();
            for (BirdInfo birdName : birdNames) {
                if (birdName.getNameId() == (Integer) birdsJList.getSelectedValue()) {
                    birdId = birdName.getBirdId();
                    name = birdName.getName();
                }
            }
            for (BirdInfo aName : birdNames) {
                if (aName.getBirdId() == birdId) {
                    names.add(aName.getName());
                }
            }
            if (birdId != 0) {
                displayBirdGUI(birdId, names, name);
            }
        }
    }

    class FeatureSearchListener implements ActionListener {

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
        private final int[] featureIds;

        Next(String[] a, int index, int[] counter) {
            featureNames = a;
            theIndexOfTheCurrentFeature = index;
            featureIds = counter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (theIndexOfTheCurrentFeature >= featureNames.length - 1) {
                imagePanel.removeAll();
                imagePanel.add(defaultPicture);
                imagePanel.revalidate();
                theIndexOfTheCurrentFeature = 0;
                hasAdded = false;
            } else {
                try {
                    int featureId = BirdNameRetriever.getFeatureID(featureNames[theIndexOfTheCurrentFeature]);
                    int featureIndex = selectableFeaturesJList.getSelectedIndex() + 1;

                    updatedBirdData = BirdNameRetriever.updateData(featureId, featureIndex);

                    if (!hasAdded) {
                        updateBirdSetAndGetUpdatedBirdsList();
                    } else {
                        removeFilteredBirds();
                    }

                    birdsJList.removeAll();
                    ArrayList<BirdInfo> updatedList = new ArrayList<>();
                    updatedList.addAll(birds);
                    updateJList(updatedList);
                    hasAdded = true;
                    createFeaturesJList(theIndexOfTheCurrentFeature + 1);
                } catch (SQLException e1) {
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void updateBirdSetAndGetUpdatedBirdsList() {
            birds = new LinkedHashSet<>(updatedBirdData);
            hasAdded = true;
            ArrayList<BirdInfo> updatedList;
            updatedList = new ArrayList<>();
            updatedList.addAll(birds);
        }

        private void removeFilteredBirds() {
            //make array big enough to hold all birds incase they
            //are all filtered out.
            BirdInfo[] fileteredBirds = new BirdInfo[birds.size()];
            int numberOfFilteredBirds = 0;
            for (BirdInfo bird : birds) {
                boolean stillExists = false;
                            //could sort then use binary search...if this gets
                //large...
                for (int i = 0; i < updatedBirdData.size(); i++) {
                    if (updatedBirdData.get(i).getBirdId() == bird.getBirdId()) {
                        stillExists = true;
                    }
                }
                //add non-existent bird to list of Filtered birds...
                if (!stillExists) {
                    fileteredBirds[numberOfFilteredBirds] = bird;
                    numberOfFilteredBirds++;
                }
            }
            //remove filtered birds...
            for (int j = 0; j < numberOfFilteredBirds; j++) {
                birds.remove(fileteredBirds[j]);
            }
        }
    }

}
