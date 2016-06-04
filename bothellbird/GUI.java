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
import java.util.stream.Collectors;
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
    private final Set<Bird> fullListOfBirds;
    private Set<Bird> birds;
    private ListCellRenderer lr;
    private final JLabel featuresJlistJLabel;
    private JLabel defaultPicture;
    private JList<String> selectableFeaturesJList = new JList<>();
    private final DefaultListModel<String> jListModel;
    private Map<String, List<Feature>> filterToFeatures;

    /**
     * @throws HeadlessException
     */
    public GUI() throws HeadlessException {
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
        fullListOfBirds = birds.stream().collect(Collectors.toCollection(LinkedHashSet::new));
        populateBirdIconsAndNames();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        this.setExtendedState(this.MAXIMIZED_BOTH | this.getExtendedState());
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
        for (Bird bird : birds) {
            birdNames = bird.getNames();
        }
    }

    public void repopulateBirds() {
        for (Bird bird : fullListOfBirds) {
            birds.add(bird);
        }
    }

    private void createFeaturesJList(int filterIndex) throws SQLException {
        filterToFeatures = FiltersToFeaturesRetriever.getFilterToFeaturesMap();
        String[] filters = getFilters();
        List<Feature> features = filterToFeatures.get(filters[filterIndex]);
        features.sort(new FeatureComparator());
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
        JButton skip = new JButton("Skip current set of criteria");
        JButton next = new JButton("Next set of criteria");
        imagePanel.removeAll();
        JLabel filterLabel = new JLabel(filters[filterIndex]);
        imagePanel.add(filterLabel);
        imagePanel.add(featuresJlistJLabel);
        imagePanel.add(jScrollPanes);
        ActionListener flterListener = new NextFilterListener(filters, filterIndex);
        next.addActionListener(flterListener);
        ActionListener skipListener = new SkipFilterListener(filters, filterIndex);
        skip.addActionListener(skipListener);
        imagePanel.add(next);
        imagePanel.add(skip);
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

    private void displayBirdGUI(DisplayBird bird) throws SQLException, IOException {
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
            Bird selectedBird = null;
            for (BirdName name : birdNames) {
                if (name.getName().toLowerCase().contains(birdName.toLowerCase())) {
                    for (Bird bird : birds) {
                        if (bird.getBirdId() == name.getBirdId()) {
                            selectedBird = bird;
                            try {
                                displayBirdGUI(new DisplayBird(selectedBird));
                            } catch (SQLException | IOException ex) {
                                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                    break;
                }

            }
            if (selectedBird == null) {
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
            //inefficient should sort/search
            for (Bird bird : birds) {
                if (bird.getBirdId() == (Integer) birdsJList.getSelectedValue()) {
                    try {
                        displayBirdGUI(new DisplayBird(bird));
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

    class SkipFilterListener implements ActionListener {

        //skip button is only created after createFeaturesJList
        //completes...can refactor later...for now assume
        //that is always the case...
        private final String[] filters;
        private int filterIndex;

        SkipFilterListener(String[] filters, int filterIndex) {
            this.filters = filters;
            this.filterIndex = filterIndex;
        }

        public void actionPerformed(ActionEvent e) {
            if (filterIndex >= filters.length - 1) {
                imagePanel.removeAll();
                imagePanel.add(defaultPicture);
                imagePanel.revalidate();
                filterIndex = 0;
            } else {
                try {
                    createFeaturesJList(filterIndex + 1);
                } catch (SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class NextFilterListener implements ActionListener {

        //next button is only created after createFeaturesJList
        //completes...can refactor later...for now assume
        //that is always the case...
        private final String[] filters;
        private int filterIndex;

        NextFilterListener(String[] filters, int filterIndex) {
            this.filters = filters;
            this.filterIndex = filterIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedFeatureIndex = selectableFeaturesJList.getSelectedIndex() + 1;
            updatedBirdData = filterBirds(selectedFeatureIndex, filters[filterIndex]);
            if (updatedBirdData != null) {
                birds.retainAll(updatedBirdData);
            } else {
                birds = new LinkedHashSet<>();
            }
            birdsJList.removeAll();
            try {
                updateJList(birds);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (filterIndex >= filters.length - 1) {
                imagePanel.removeAll();
                imagePanel.add(defaultPicture);
                imagePanel.revalidate();
                filterIndex = 0;
            } else {
                try {
                    createFeaturesJList(filterIndex + 1);
                } catch (SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                case "Bill":
                    return filterByBillShape(featureId);
                case "Wing":
                    return filterByWingShape(featureId);
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
                List<Integer> birdColors = bird.getSecondaryColorIds();
                if (birdColors.contains(featureId)) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByPrimaryColor(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getPrimaryColorIds().contains(featureId)) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByHabitat(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                List<Integer> habitats = bird.getHabitatIds();
                if (habitats.contains(featureId)) {
                    remainingBirds.add(bird);
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
                List<Integer> locations = bird.getLocationIds();
                if (locations.contains(featureId)) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByBillShape(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getBillShapeId() == featureId) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }

        private ArrayList<Bird> filterByWingShape(int featureId) {
            ArrayList<Bird> remainingBirds = new ArrayList<>();
            for (Bird bird : birds) {
                if (bird.getWingShapeId() == featureId) {
                    remainingBirds.add(bird);
                }
            }
            return remainingBirds;
        }
    }
}
