/**
 * 
 */
package BothellBird;

import java.awt.Component;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;



/**
 * @author Bret Van Hof
 *
 */
public class listRenderer extends DefaultListCellRenderer 
{

	private ImageIcon image;
	private Map<String, ImageIcon> icons = null;
	private Map<Integer, String> valueToBirdName;

	public listRenderer(Map<String, ImageIcon> icons, Map<Integer, String> valueToBirdNameMap)
	{
		setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        this.icons = icons;
        valueToBirdName = valueToBirdNameMap;

	}
	public Component getListCellRendererComponent(
		    JList<?> list, Object value, int index,
		    boolean isSelected, boolean cellHasFocus) 
	{
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        String name = "";
        //Set the icon and text.  If icon was null, say so.
        image = icons.get(valueToBirdName.get(value));
        setIcon(image);
        if (image != null) {
        		setText(valueToBirdName.get(value));
        } else {
            setText(name + " (no image available)");
        }

        return this;
	}
}
