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
import java.util.List;
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
            try {
                new GUI().setVisible(true);
            } catch (HeadlessException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private JButton search;
    private JTextField searchBox;
    private JList<?> birdsJList;
    private JScrollPane jScrollPane;
    private JPanel listPanel;
    private ArrayList<ImageIcon> birdIcons;
    private Iterable<BirdName> birdNames;
    private ArrayList<Bird> updatedBirdData;
    private JPanel searchPanel;
    private JPanel imagePanel;
    private JButton searchByFeatures;
    private JButton resetList;
    private JPanel buttonPanel;
    private JButton displayBird;
    private Map<Integer, String> birdIdToBirdName;
    private Set<Bird> fullListOfBirds;
    private Set<Bird> birds;
    private ListCellRenderer lr;
    private ActionListener flterListener;
    private JButton next = new JButton("Select Feature and Continue Search");
    private final JLabel featuresJlistJLabel;
    private JLabel defaultPicture;
    private JList<String> selectableFeaturesJList = new JList<>();
    private final DefaultListModel<String> jListModel;
    private Map<String, List<Feature>> filterToFeatures;
    private final HashMap<String, Integer> birdNameToBirdIdMap;

    /**
     * @throws HeadlessException
     */
    public GUI() throws HeadlessException {
        this.birdNameToBirdIdMap = new HashMap<>();
        this.filterToFeatures = new HashMap<>();
        this.jListModel = new DefaultListModel<>();
        this.featuresJlistJLabel = new JLabel();
        setVisible(true);
        try {
            birds = BirdsListRetriever.getBirdsList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Can't connect to database!",
                    "", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(null, "Using offline data");
        }
        populateBirdIconsAndNames();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        setName("BothellBirder \u00a9 Bret Van Hof");
        setTitle("BothellBirder \u00a9 Bret Van Hof");
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

    private String[] getFilters() {
        String[] filters = new String[filterToFeatures.size()];
        int z = 0;
        for (Map.Entry<String, List<Feature>> entry : filterToFeatures.entrySet()) {
            filters[z] = entry.getKey();
            z++;
        }
        return filters;
    }

    private int[] getFeatureIds(List<Feature> selectedFeature) {
        int arraySize = selectedFeature.size();
        int[] featureIDs = new int[arraySize];
        int counter = 0;
        //get ids.
        for (Feature feature : selectedFeature) {
            featureIDs[counter] = feature.getFeatureId();
            counter++;
        }
        return featureIDs;
    }

    private void populateBirdIconsAndNames() {
        birdIcons = new ArrayList<>();
        fullListOfBirds = new LinkedHashSet<>();
        for (Bird bird : birds) {
            birdNames = bird.getNames();
            fullListOfBirds.add(bird);
        }
    }

    public void repopulateBirds() {
        for (Bird bird : fullListOfBirds) {
            birds.add(bird);
        }
    }

    public ActionListener makeNewNextFilterActionListener(String[] filters, int filterIndex, int[] featureIds, List<Feature> features) {
        ActionListener nextAction = new NextFilterListener(filters, filterIndex, featureIds, features);
        return nextAction;
    }

    private void createFeaturesJList(int filterIndex) throws SQLException {
        filterToFeatures = FiltersToFeaturesRetriever.getFilterToFeaturesMap();
        String[] filters = getFilters();
        List<Feature> features = filterToFeatures.get(filters[filterIndex]);
        features.sort(new FeatureComparator());
        int[] featureIds = getFeatureIds(features);
        jListModel.clear();
        int i = 0;
        String[] featureNames = new String[features.size()];
        for (Feature feature : features) {
            String featureName = feature.getFeatureName();
            jListModel.add(i, featureName);
            featureNames[i] = featureName;
            i++;
        }
        selectableFeaturesJList = new JList<>(featureNames);
        selectableFeaturesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectableFeaturesJList.setModel(jListModel);
        selectableFeaturesJList.setSelectedIndex(0);
        JScrollPane jScrollPanes = new javax.swing.JScrollPane(selectableFeaturesJList);
        next = new JButton("Next set of criteria");
        imagePanel.removeAll();
        JLabel filterLabel = new JLabel(filters[filterIndex]);
        imagePanel.add(filterLabel);
        imagePanel.add(featuresJlistJLabel);
        imagePanel.add(jScrollPanes);
        next.removeActionListener(flterListener);
        flterListener = makeNewNextFilterActionListener(filters, filterIndex, featureIds, features);
        next.addActionListener(flterListener);
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
        createJList(birds);
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
        ActionListener birdSelectListener = new BirdDisplayListener();
        displayBird.addActionListener(birdSelectListener);
        ActionListener featureSearchListener = new FeatureSearchListener();
        searchByFeatures.addActionListener(featureSearchListener);
        resetList = new JButton("Reset List to default (clear features search)");
        ActionListener resetListener = new ResetListener();
        resetList.addActionListener(resetListener);
    }

    private void updateJList(Set<Bird> listOfBirds) throws IOException, SQLException {
        createJList(listOfBirds);
        listPanel.revalidate();
        getContentPane().revalidate();
    }

    private void createJList(Set<Bird> listOfBirds) throws IOException, SQLException {
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
        for (Bird bird : birds) {
            birdIcons.add(bird.getIcon());
            birdNameToBirdIconMap.put(bird.getNeutralName().getName(), birdIcons.get(index));
            birdIdToBirdName.put(bird.getBirdId(), bird.getNeutralName().getName());
            index++;
        }
        if (birds.isEmpty()) {
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

    private Integer[] getBirdIds(Set<Bird> birds) {
        Integer[] birdIds = new Integer[birds.size()];
        int count = 0;
        for (Bird bird : birds) {
            birdIds[count] = bird.getBirdId();
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

    private void displayBirdGUI(Bird bird) throws SQLException, IOException {
        JFrame display = new BirdGUI(bird);
        display.setVisible(true);
        display.setName(bird.getName() + " \u00a9 Bret Van Hof");
        display.setTitle(bird.getName() + " \u00a9 Bret Van Hof");
        display.setBounds(30, 30, 800, 600);
        display.toFront();
    }

    class NameSearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String birdName = searchBox.getText();
            if (birdNameToBirdIdMap.get(birdName) != null) {
                Bird selectedBird;
                for (BirdName name : birdNames) {
                    if (name.getBirdId() == birdNameToBirdIdMap.get(birdName)) {
                        for (Bird bird : birds) {
                            if (bird.getBirdId() == name.getBirdId()) {
                                selectedBird = bird;
                                try {
                                    displayBirdGUI(selectedBird);
                                } catch (SQLException | IOException ex) {
                                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
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
            imagePanel.removeAll();
            repopulateBirds();
            updatedBirdData = new ArrayList<>();
            updatedBirdData.addAll(birds);
            try {
                createJList(BirdsListRetriever.getBirdsList());

            } catch (IOException | SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            listPanel.revalidate();
            imagePanel.revalidate();
        }
    }

    class BirdDisplayListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int birdId = 0;
            //inefficient should sort/search
            for (Bird bird : birds) {
                if (bird.getBirdId() == (Integer) birdsJList.getSelectedValue()) {
                    birdId = bird.getBirdId();
                    try {
                        displayBirdGUI(bird);
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    class FeatureSearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                updatedBirdData = new ArrayList<>();
                updatedBirdData.addAll(birds);
                createFeaturesJList(0);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class NextFilterListener implements ActionListener {

        //next button is only created after createFeaturesJList
        //completes...can refactor later...for now assume
        //that is always the case...
        private final String[] filters;
        private int filterIndex;
        private final int[] featureIds;
        private final List<Feature> features;

        NextFilterListener(String[] filters, int filterIndex, int[] featureIds, List<Feature> features) {
            this.filters = filters;
            this.filterIndex = filterIndex;
            this.featureIds = featureIds;
            this.features = features;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (filterIndex >= filters.length - 1) {
                imagePanel.removeAll();
                imagePanel.add(defaultPicture);
                imagePanel.revalidate();
                filterIndex = 0;
            } else {
                int selectedFeatureIndex = selectableFeaturesJList.getSelectedIndex() + 1;
                updatedBirdData = filterBirds(selectedFeatureIndex, filters[filterIndex]);
                removeFilteredBirds();
                birdsJList.removeAll();
                try {
                    updateJList(birds);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    createFeaturesJList(filterIndex + 1);
                } catch (SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void removeFilteredBirds() {
            //make array big enough to hold all birds incase they
            //are all filtered out.
            Bird[] fileteredBirds = new Bird[birds.size()];
            int numberOfFilteredBirds = 0;
            for (Bird bird : birds) {
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

        private ArrayList<Bird> filterBirds(int featureId, String filter) {
            switch (filter) {
                case "Family":
                    return filterByFamily(featureId);
                case "SecondaryColor":
                    return filterBySecondaryColor(featureId);
                case "PrimaryColors":
                    return filterByPrimaryColor(featureId);
                case "Habitats":
                    return filterByHabitat(featureId);
                case "ConservationStatus":
                    return filterByConservationStatus(featureId);
                case "Size":
                    return filterBySize(featureId);
                case "Locations":
                    return filterByLocation(featureId);
                default:
                    return null;
            }
        }

        private ArrayList<Bird> filterByFamily(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getFamilyNameId() == featureId) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterBySecondaryColor(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                List<Integer> birdColors = bird.getSecondaryColors();
                if (birdColors.contains((Integer) featureId)) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByPrimaryColor(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getPrimaryColors().contains(featureId)) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByHabitat(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                List<Feature> habitats = bird.getHabitats();
                //again need to sort/search...
                for (Feature habitat : habitats) {
                    if (habitat.getFeatureId() == featureId) {
                        remainingBirds.add(bird);
                        break;
                    }
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByConservationStatus(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getConservationStatusId() == featureId) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterBySize(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getSizeId() == featureId) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByLocation(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                List<Feature> locations = bird.getLocations();
                for (Feature location : locations) {
                    if (location.getFeatureId() == featureId) {
                        remainingBirds.add(bird);
                    }
                }
            }
            return remainingBirds;
        }
    }
}
