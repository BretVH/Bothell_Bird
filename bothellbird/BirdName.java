package bothellbird;

public class BirdName 
{
	private String name;
	private int nameId;
	private int birdId;
	
	public BirdName(String aName, int aBirdId, int aNameId) 
	{
		name = aName;
	    birdId = aNameId;
	    nameId = aBirdId;
	}
	
	public int getNameId()
	{
		return nameId;
	}
	
	public int getBirdId()
	{
		return birdId;
	}

	public String getName()
	{
		return name;
	}
}
