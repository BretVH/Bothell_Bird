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
import java.util.TreeMap;
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
    private JButton nameSearch;
    private JTextField searchBox;
    private JList<?> listJList;
    private JScrollPane jScrollPane;
    private JPanel listPanel;
    private ArrayList<ImageIcon> images;
    private ArrayList<BirdInfo> birdNames;
    private ArrayList<BirdInfo> updater;
    private BirdNameRetriever birdNamer;
    private ImageRetriever imager;
    private JPanel nameSearcher;
    private JPanel imagePanel;
    private JButton searchByFeatures;
    private JLabel pic;
    private JButton resetList;
    private JPanel buttonPanel;
    private JButton displayBird;
    private Map<Integer, String> birdIdToBirdName;
    private Map<String, ImageIcon> icons = new TreeMap<>();
    private final ArrayList<BirdInfo> newer;
    private Set<BirdInfo> uniqueSetOfBirdNameOb;
    private ListCellRenderer lr;
    private boolean hasAdded = false;
    private ActionListener listener;
    private JButton next = new JButton("Select Feature and Continue Search");
    private final JLabel featuresJlistJLabel;
    private JList<String> selectableFeaturesJList = new JList<>();
    private final DefaultListModel<String> model;
    private Map<String, ArrayList<String>> featureToSelectableFeatureMap;
    private final HashMap<String, Integer> birdie;

    /**
     * @throws HeadlessException
     */
    public GUI() throws HeadlessException {
        this.birdie = new HashMap<>();
        this.featureToSelectableFeatureMap = new TreeMap<>();
        this.model = new DefaultListModel<>();
        this.featuresJlistJLabel = new JLabel();
        this.uniqueSetOfBirdNameOb = new LinkedHashSet<>();
        this.newer = new ArrayList<>();
        setVisible(true);
        createData();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setExtendedState(this.MAXIMIZED_BOTH | this.getExtendedState());
        setName("BothellBirder by Team 5");
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage("defaultBird.jpg")); //sets icon
        setVisible(true);
        setContentPane(new JDesktopPane());
        initComponents();
        revalidate();
    }

    class NameSearch implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String toFind = searchBox.getText();
            if (birdie.get(toFind) != null) {
                ArrayList<String> names = new ArrayList<>();
                for (BirdInfo aName : birdNames) {
                    if (aName.getBirdId() == birdie.get(toFind)) {
                        names.add(aName.getName());
                    }
                }
                JFrame display = new BirdGUI(birdie.get(toFind), names);
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
                if (birdName.getNameId() == (Integer) listJList.getSelectedValue()) {
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
            makeFeaturesJList(0);
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
                ArrayList<BirdInfo> newer = new ArrayList<>();
                try {
                    int featr = birdNamer.getFeatureID(featureNames[theIndexOfTheCurrentFeature]);
                    int aGoodIndex = selectableFeaturesJList.getSelectedIndex() + 1;

                    updater = birdNamer.updateData(featr, aGoodIndex);

                    if (!hasAdded) {
                        uniqueSetOfBirdNameOb = new LinkedHashSet<>(updater);
                        hasAdded = true;
                        ArrayList<BirdInfo> updatedList = new ArrayList<>();
                        updatedList.addAll(uniqueSetOfBirdNameOb);
                    } else {
                        BirdInfo[] b = new BirdInfo[uniqueSetOfBirdNameOb.size()];
                        int theIndex = 0;
                        for (BirdInfo a : uniqueSetOfBirdNameOb) {
                            boolean stillExists = false;

                            for (int z = 0; z < updater.size(); z++) {
                                if (updater.get(z).getBirdId() == a.getBirdId()) {
                                    stillExists = true;
                                }
                            }
                            if (!stillExists) {
                                b[theIndex] = a;
                                theIndex++;
                            }
                        }
                        for (int j = 0; j < theIndex; j++) {
                            uniqueSetOfBirdNameOb.remove(b[j]);
                        }
                    }

                    listJList.removeAll();
                    Integer[] birdID;
                    birdID = new Integer[uniqueSetOfBirdNameOb.size()];

                    int k = 0;
                    ArrayList<BirdInfo> updatedList = new ArrayList<>();
                    for (BirdInfo ID : uniqueSetOfBirdNameOb) {
                        birdID[k] = (Integer) ID.getBirdId();
                        k++;
                    }
                    updatedList.addAll(uniqueSetOfBirdNameOb);
                    updateList(updatedList);
                    boolean hasAdded = true;
                    makeFeaturesJList(theIndexOfTheCurrentFeature + 1);
                } catch (SQLException e1) {
                }
            }
        }
    }

    private void createData() {
        birdNames = new ArrayList<>();
        images = new ArrayList<>();
        birdNamer = new BirdNameRetriever();
        imager = new ImageRetriever();
        try {
            birdNames = birdNamer.readData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Can't connect to database!",
                    "", JOptionPane.WARNING_MESSAGE);
        }
        for (BirdInfo aBirdie : birdNames) {
            birdie.put(aBirdie.getName(), aBirdie.getBirdId());
        }
    }

    public ActionListener makeThis(String[] a, int b, int[] c) {
        ActionListener nextAction = new Next(a, b, c);
        return nextAction;
    }

    private void makeFeaturesJList(int anIndex) {
        DescriptionRetriever make = new DescriptionRetriever();
        try {
            featureToSelectableFeatureMap = make.getFeatures();
        } catch (SQLException e1) {
        }
        String[] featureName = new String[featureToSelectableFeatureMap.size()];
        int z = 0;
        for (Map.Entry<String, ArrayList<String>> entry : featureToSelectableFeatureMap.entrySet()) {
            featureName[z] = entry.getKey();
            z++;
        }
        ArrayList<String> feature = new ArrayList<>(featureToSelectableFeatureMap.get(featureName[anIndex]));//new ArrayList<String>();
        int[] IDS;
        if (feature.size() == 2) {
            IDS = new int[2];
        } else {
            IDS = new int[feature.size() / 2 + 1];
        }
        ArrayList<String> list = featureToSelectableFeatureMap.get(featureName[anIndex]);
        int counter = 0;
        for (int g = 1; g < IDS.length - 2; g += 2) {
            int featureID = Integer.parseInt(list.get(g));
            IDS[counter] = featureID;
            counter++;
        }
        for (int index = 0; index < list.size(); index++) {
            list.remove(index + 1);
        }
        int i = 0;
        model.clear();
        for (String fam : list) {
            model.add(i, fam);
            i++;
        }
        String[] as = new String[list.size()];
        selectableFeaturesJList = new JList<>(list.toArray(as));
        selectableFeaturesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectableFeaturesJList.setModel(model);
        selectableFeaturesJList.setSelectedIndex(0);
        JScrollPane jScrollPanes = new javax.swing.JScrollPane(selectableFeaturesJList);
        next = new JButton("Next set of criteria");
        imagePanel.removeAll();
        JLabel aFeatureName = new JLabel(featureName[anIndex]);
        imagePanel.add(aFeatureName);
        imagePanel.add(featuresJlistJLabel);
        imagePanel.add(jScrollPanes);
        listener = makeThis(featureName, anIndex, IDS);
        next.addActionListener(listener);
        imagePanel.add(next);
        imagePanel.revalidate();
        revalidate();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        createJPanels();
        createButtons();
        createOther();
        createList();
    }

    private void createOther() {
        pic = new JLabel(new ImageIcon("defaultBird.jpg"));
        imagePanel.add(pic);
        searchBox = new JTextField();
        searchBox.setText("Enter Bird Name Here");
        searchBox.setColumns(20);
        nameSearcher.add(searchBox);
        searchBox.grabFocus();
    }

    private void createJPanels() {
        listPanel = new JPanel();
        imagePanel = new JPanel();
        nameSearcher = new JPanel();
        buttonPanel = new JPanel();
        setLayout(new BorderLayout());
        add(listPanel, BorderLayout.WEST);
        add(nameSearcher, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        setName("BothellBirder by Team 5");
    }

    private void createButtons() {
        nameSearch = new JButton("Search by name");
        ActionListener nameSearches = new NameSearch();
        nameSearch.addActionListener(nameSearches);
        searchByFeatures = new JButton("Search by features");
        displayBird = new JButton("Display Bird Selected in List");
        ActionListener dis = new Birder();
        displayBird.addActionListener(dis);
        ActionListener feat = new Searcher();
        searchByFeatures.addActionListener(feat);
        resetList = new JButton("Reset List to default (clear features search)");
        ActionListener reset = new Reseter();
        resetList.addActionListener(reset);
        nameSearcher.add(nameSearch);
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
        listJList = new JList<>(birdID);
        listJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listJList.setSelectedIndex(0);
        listJList.setVisibleRowCount(3);
        jScrollPane = new javax.swing.JScrollPane(listJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.removeAll();
        listJList.setCellRenderer(lr);
        listPanel.add(jScrollPane, java.awt.BorderLayout.WEST);
        listPanel.revalidate();
        getContentPane().revalidate();
    }

    private void createList() {
        getInitJListData();
        Integer[] birdID = new Integer[birdNames.size()];
        int i = 0;
        for (BirdInfo ID : birdNames) {
            birdID[i] = (Integer) ID.getNameId();
            i++;
        }
        listJList = new JList<>(birdID);
        listJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listJList.setSelectedIndex(0);
        listJList.setVisibleRowCount(3);
        jScrollPane = new javax.swing.JScrollPane(listJList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(jScrollPane, java.awt.BorderLayout.WEST);
        lr = new ListRenderer(icons, birdIdToBirdName);
        listJList.setCellRenderer(lr);
    }

    private void getInitJListData() {
        icons = new TreeMap<>();
        images = new ArrayList<>();
        ImageIcon test = new ImageIcon("0a0.jpg");
        birdIdToBirdName = new HashMap<>();
        int index = 0;
        for (BirdInfo name : birdNames) {
            try {
                images.add(imager.readData(name.getBirdId(), name.getNameId()));
            } catch (SQLException e) {
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            icons.put(name.getName(), images.get(index));
            birdIdToBirdName.put(name.getNameId(), name.getName());
            index++;
        }
        if (birdNames.isEmpty()) {
            icons.put("empty", test);
        }
    }

    /**
     * @param args//
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            new GUI().setVisible(true);
        });
    }
}
