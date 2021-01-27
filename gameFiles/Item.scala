package o1.SaveTheKnee

/** The class `Item` represents items in a text adventure game. Each item has a name
  * and a  *  longer description. (In later versions of the adventure game, items may
  * have other features as well.)
  *
  * N.B. It is assumed, but not enforced by this class, that items have unique names.
  * That is, no two items in a game world have the same name.
  *
  * @param name         the item's name
  * @param description  the item's description */
 abstract class Item(val name: String, val description: String) {
  
  def use: String

  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
}

  class Painkiller(name:String, description:String) extends Item(name, description) {
    def use = {
      "You feel much better. Better keep using these if you are willing to go places."
    }
  }
  
  class ExcerciseBike(name:String, description:String) extends Item(name, description) {
    def use:String = "Wow. This is even less boring than cycling outside."
  }
  
  class AquaJoggingBelt(name:String, description:String) extends Item(name, description) {
    def use = "You put on the belt and descend to the pool. Running in the water like Road Runner, you've never felt so stupid."
  }
  
  class Staff(name:String, description:String) extends Item(name, description) {
    def use = "Doesn't actually help at all. Looks cool though."
  }
  
  class Beer(name:String, description:String) extends Item(name, description) {
    def use = "Crackin' open a cold one with the boys"
  }
  
  
  
  
  