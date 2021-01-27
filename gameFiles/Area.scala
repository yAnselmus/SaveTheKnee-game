package o1.SaveTheKnee

import scala.collection.mutable.Map

/** The class `Area` represents locations in a text adventure game world. A game world
  * consists of areas. In general, an "area" can be pretty much anything: a room, a building,
  * an acre of forest, or something completely different. What different areas have in
  * common is that players can be located in them and that they can have exits leading to
  * other, neighboring areas. An area also has a name and a description.
  * @param name         the name of the area
  * @param description  a basic description of the area (typically not including information about items) */
class Area(var name: String, var description: String) {

  private val neighbors = Map[String, Area]()
  private val items = Map[String, Item]()
  private val npcs = Map [String, Npc]()
  
  def itemsHere = this.items
  def npcsHere = this.npcs


  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)


  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in
    * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }


  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction--area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Area)]) = {
    this.neighbors ++= exits
  }


  /** Returns a multi-line description of the area as a player sees it. This includes a basic
    * description of the area as well as information about exits and items. The return
    * value has the form "DESCRIPTION\n\nExits available: DIRECTIONS SEPARATED BY SPACES".
    * The directions are listed in an arbitrary order. */
  def fullDescription = {
    val exitList = "\n\nExits available: " + this.neighbors.keys.mkString(" ")
    
    if (items.isEmpty && npcs.isEmpty) this.description + exitList
    else if (!items.isEmpty && npcs.isEmpty) this.description + "\n" + "You see here: " + items.keys.mkString(" ") + exitList
    else if (items.isEmpty && !npcs.isEmpty) this.description + "\n\n"+ this.npcs.toVector(0)._2.say + exitList
    else  this.description + "\n\n" + this.npcs.toVector(0)._2.say + "\n\n"+ "You see here: " + items.keys.mkString(" ")+ exitList
  }


  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)
  
  def addItem(item: Item): Unit = {
    this.items += item.name -> item
  }
  
  def contains(itemName: String): Boolean = {
    this.items.contains(itemName)
  }

  def removeItem(itemName: String): Option[Item] = {
    val poistettava = this.items.get(itemName)
    this.items.remove(itemName)
    poistettava
  }
  
  def containsNpc(npcName: String):Boolean = {
    this.npcs.contains(npcName)
  }

  def addNpc(npc: Npc): Unit = {
    this.npcs += npc.name -> npc
  }
  
}
