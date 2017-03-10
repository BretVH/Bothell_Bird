package bothell_bird;

import java.awt.Component;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 * @author Bret Van Hof
 *
 */
public class ListRenderer extends DefaultListCellRenderer {

    private ImageIcon image;
    private final Map<String, ImageIcon> icons;
    private final Map<Integer, String> birdIdToBirdNameMap;

    public ListRenderer(Map<String, ImageIcon> icons, Map<Integer, String> birdIdToBirdNameMap) {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        this.icons = icons;
        this.birdIdToBirdNameMap = birdIdToBirdNameMap;

    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object birdIdObject, int index,
            boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        String name = "";
        //Set the icon and text.  If icon was null, say so.
        String birdId = birdIdToBirdNameMap.get((int) birdIdObject);
        image = icons.get(birdId);
        setIcon(image);
        if (image != null) {
            setText(birdIdToBirdNameMap.get((int) birdIdObject));
        } else {
            setText(name + " (no image available)");
        }
        return this;
    }
}
