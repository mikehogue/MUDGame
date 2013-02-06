package GameModel;

/**
 * Factory class that generates consumables, armor, or weapons, depending on the method called.
 * @author Chris
 *
 */
public class ItemFactory {
	
	/**
	 * Makes a consumable which matches the passed string.
	 * @param name
	 * 			The name of the consumable being made.
	 * @return
	 * 			A consumable Item defined by name, or null if it does not exist.
	 */
	public static Consumable makeConsumable(String name)	{
		if(name.compareTo("Small Health Potion") == 0)	{
			return new SmallHealthPot();
		}
		else if(name.compareTo("Medium Health Potion") == 0)	{
			return new MediumHealthPot();
		}
		else if(name.compareTo("Large Health Potion") == 0)	{
			return new LargeHealthPot();
		}
		else	{
			System.out.println("Null Item: " + name);
			return null;
		}
	}

	/**
	 * Makes a piece of armor which matches the passed string.
	 * @param name
	 * 			The name of the armor being made.
	 * @return
	 * 			An Armor object defined by name, or null if it does not exist.
	 */
	public static Armor makeArmor(String name)	{
		if(name.compareTo("Leather Armor") == 0)	{
			return new Armor("Leather Armor", 4, "Cow skin is great at stopping sharp objects, right?", 6, EquipSlot.TORSO);
		}
		else if(name.compareTo("Leather Helmet") == 0)	{
			return new Armor("Leather Helmet", 1, "Only slightly more protective than a tin foil hat.", 3, EquipSlot.HEAD);
		}
		else if(name.compareTo("Leather Leggings") == 0)	{
			return new Armor("Leather Leggings", 3, "At least they're not tights.", 5, EquipSlot.LEGS);
		}
		else if(name.compareTo("Iron Armor") == 0)	{
			return new Armor("Iron Armor", 18, "Clearly, the abs engraved on the front are integral to the design.", 15, EquipSlot.TORSO);
		}
		else if(name.compareTo("Iron Helmet") == 0)	{
			return new Armor("Iron Helmet", 6, "Sturdy, reliable, and makes you look like a bullet.", 6, EquipSlot.HEAD);
		}
		else if(name.compareTo("Iron Leggings") == 0)	{
			return new Armor("Iron Leggings", 12, "Your knees have never been safer.", 10, EquipSlot.LEGS);
		}
		else if(name.compareTo("Iron Shield") == 0)  {
			return new Armor("Iron Shield", 12, "If you're going to hide behind your shield all day why didn't you just stay home?", 10, EquipSlot.HAND);
		}
		else if(name.compareTo("Ancient Armor") == 0)	{
			return new Armor("Ancient Armor", 12, "Covered in the runes of a long lost civilization. Or possibly graffiti.", 28, EquipSlot.TORSO);
		}
		else if(name.compareTo("Ancient Helmet") == 0)	{
			return new Armor("Ancient Helmet", 6, "Apparently, elder civilizations liked to impale people with headbutts.", 12, EquipSlot.HEAD);
		}
		else if(name.compareTo("Ancient Leggings") == 0)	{
			return new Armor("Ancient Leggings", 10, "Something this old really shouldn't hold up so well.", 20, EquipSlot.LEGS);
		}
		else if(name.compareTo("Ancient Shield") == 0)	{
			return new Armor("Ancient Shield", 9, "Whoever made this truly understood what it's like to hide behind a giant metal slab.", 22, EquipSlot.HAND);
		}
		else if(name.compareTo("Shield of Hogue") == 0)  {
			return new Armor("Shield of Hogue", 10, "A great hero once ate dinner on this thing, there's a few crumbs left", 20, EquipSlot.HAND);
		}
		else if(name.compareTo("Helm of Latura") == 0)  {
			return new Armor("Helm of Latura", 6, "A great hero once kept his head in here, it must have fallen out", 20, EquipSlot.HEAD);
		}
		else if(name.compareTo("Breastplate of Conway") == 0){
			return new Armor("Breastplate of Conway", 12, "A great hero once wore this to hide his embarrassing tattoo", 35, EquipSlot.TORSO);
		}
		else	{
			System.out.println("Null Armor: " + name);
			return null;
		}
	}
	
	/**
	 * Makes a weapon which matches the passed string.
	 * @param name
	 * 			The name of the weapon being generated.
	 * @return
	 * 			A Weapon object defined by name, or null if it does not exist.
	 */
	public static Weapon makeWeapon(String name)	{
		if(name.compareTo("Battle Axe") == 0)	{
			return new Weapon("Battle Axe", 32, "Because a regular axe just isn't big enough.", 30, 5.0, true);
		}
		else if(name.compareTo("Claymore") == 0)	{
			return new Weapon("Claymore", 25, "A sharpened slab of iron on a short stick.", 25, 4.0, true);
		}
		else if(name.compareTo("Dagger") == 0)	{
			return new Weapon("Dagger", 3, "Who needs damage when you can annoy your enemies to death?", 4, 1.0);
		}
		else if(name.compareTo("Handaxe") == 0)	{
			return new Weapon("Handaxe", 6, "An axe, which you can hold in your hand. It's very complicated.", 14, 2.5);
		}
		else if(name.compareTo("Iron Sword") == 0)	{
			return new Weapon("Iron Sword", 5, "An old iron sword. The name is fairly self-explanatory.", 10, 2.0);
		}
		else if(name.compareTo("Longbow") == 0)	{
			return new Weapon("Longbow", 8, "Stop kidding yourself, you're not an elf.", 16, 4.0, true, 3);
		}
		else if(name.compareTo("Crossbow") == 0) {
			return new Weapon("Crossbow", 10, "That speck in the distance will be a lot closer by the time you reload. Try not to miss.", 20, 6.0, true, 3); 
		}
		else if(name.compareTo("Troll Club") == 0)	{
			return new Weapon("Troll Club", 500, "Why would you think you can even use this?", 5, 500.0, true);
		}
		else if(name.compareTo("Axe of Shihab") == 0) {
			return new Weapon("Axe of Shihab", 32, "A great hero once chopped down his Christmas tree with this axe", 36, 3.5, true);
		}
		else	{
			System.out.println("Null Weapon: " + name);
			return null;
		}
	}
}
