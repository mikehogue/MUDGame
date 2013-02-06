package View;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Sheet which holds a BufferedImage of all the possible panels for the minimap. Can
 * get panels based on the type, as defined by exits.
 * @author Chris
 *
 */
public class MapSheet {
	//Sheet which holds a BufferedImage of all the possible panels for the minimap.
	private BufferedImage mapSheet;
	
	/**
	 * Constructs the MapSheet & stores Images/MapPanels as the sheet.
	 */
	public MapSheet()	{
		try
		{
			mapSheet = ImageIO.read(new File("Images/MapPanels.jpg"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes a string corresponding to the locations of the exit in the room that should
	 * be displayed, in N>E>S>W order, and generates the BufferedImage of that room, clipped
	 * from the sheet.
	 * @param roomType
	 * 			The type of room as a string containing the exits.
	 * @return
	 * 			The BufferedImage corresponding to roomType, or null if roomType is invalid.
	 */
	public BufferedImage getRoom(String roomType)	{
		if(roomType.compareTo("NESW") == 0)	{
			return mapSheet.getSubimage(0, 0,180,180);
		}
		else if(roomType.compareTo("NES") == 0)	{
			return mapSheet.getSubimage(180, 0, 180, 180);
		}
		else if(roomType.compareTo("NEW") == 0)	{
			return mapSheet.getSubimage(360, 0, 180, 180);			
		}
		else if(roomType.compareTo("NSW") == 0)	{
			return mapSheet.getSubimage(540, 0, 180, 180);			
		}
		else if(roomType.compareTo("ESW") == 0)	{
			return mapSheet.getSubimage(720, 0, 180, 180);			
		}
		else if(roomType.compareTo("NE") == 0)	{
			return mapSheet.getSubimage(900, 0, 180, 180);			
		}
		else if(roomType.compareTo("ES") == 0)	{
			return mapSheet.getSubimage(0, 180, 180, 180);			
		}
		else if(roomType.compareTo("SW") == 0)	{
			return mapSheet.getSubimage(180, 180, 180, 180);			
		}
		else if(roomType.compareTo("NW") == 0)	{
			return mapSheet.getSubimage(360, 180, 180, 180);			
		}
		else if(roomType.compareTo("NS") == 0)	{
			return mapSheet.getSubimage(540, 180, 180, 180);			
		}
		else if(roomType.compareTo("EW") == 0)	{
			return mapSheet.getSubimage(720, 180, 180, 180);			
		}
		else if(roomType.compareTo("E") == 0)	{
			return mapSheet.getSubimage(900, 180, 180, 180);			
		}
		else if(roomType.compareTo("S") == 0)	{
			return mapSheet.getSubimage(0, 360, 180, 180);			
		}
		else if(roomType.compareTo("W") == 0)	{
			return mapSheet.getSubimage(180, 360, 180, 180);			
		}
		else if(roomType.compareTo("N") == 0)	{
			return mapSheet.getSubimage(360, 360, 180, 180);			
		}
		else
			return null;
	}
}
