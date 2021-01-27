package o1.SaveTheKnee

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area, val world:saveTheKnee) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var hasRodeExcerciseBike = false          // one-way flag
  private var hasAquaJogged = false                 // one-way flag
  private var bag = Map[String, Item]()
  private var drunkenessLevel = 0
  
  def rehabDone:Boolean =  hasRodeExcerciseBike && hasAquaJogged
  
  def bagsContent = this.bag
  
  def drunkness: Int = this.drunkenessLevel
  
  def excercisingMessage:String = {
    if (!rehabDone)
      if (this.world.turnsUntilFainting>7) "\nMoving is fairly easy."
      else if (this.world.turnsUntilFainting>3) "\nYour knee is aching, walking is a bit hard."
      else "\nYour knee is about to shatter! Get medicine soon!"
    else "\nThere is no pain in the knee anymore."
  }
  
  def has(itemName: String): Boolean  = bag.contains(itemName)
  
  def drop(itemName: String): String = {
    if (this.has(itemName)) {
      currentLocation.addItem(bag(itemName))
      bag.remove(itemName)
       "You part ways with the extra weight that "+itemName+" brings."
    }else "Thank god you aren't carrying such item."
  }
  
  def examine(itemName: String): String = {
     if (this.has(itemName)) "You look closely at the " + itemName +".\n " + bag(itemName).description
     else "If you want to examine something, you need to pick it up first."
  }
  
  def buy(itemName:String): String = {
    if (!this.bag.contains(itemName)) {
      if (currentLocation.contains("painkiller")) {
        bag += "painkiller" -> new Painkiller("painkiller", "This is your elixir of life")
        "You bought some painkillers."
      }
      else if (currentLocation.contains("beer") && currentLocation == this.world.store) {
       bag += "beer" -> new Beer("beer", "Cold. Would this relieve some pain?")
        "You bought a beer."
     }
      else "No one sells " +itemName+ " here, idiot."
    }
    else "Due to your disabilites, you can only carry one " + itemName + " at a time. Use it before buying a new one."
  }
    
  
  def get(itemName: String): String = {
    if (!this.bag.contains(itemName)) {
       if (itemName == "painkiller") {
        "Dude! You better pay for these."
      }
      else if (itemName == "aqua_jogging_belt"){
        "Oh, come on. During hard times like these, why would you like to carry any extraweight?"
      }
      else if (itemName == "excercise_bike"){
        "Yeah, picking up an excercise bike seems like a good idea. What could possibly go wrong?"
      }
      else if (itemName == "staff"){
        "It's too heavy. You just can't lift it with your sphagetti arms."
      }
      else if (currentLocation == this.world.store ){
        "Dude! You better pay for these."
      }
      else if (currentLocation.contains(itemName)) {
        val foundItem = currentLocation.removeItem(itemName)
        bag += itemName -> foundItem.get
       "You torture yourself by picking up the " +itemName +"."
      }else "Luckily, there is no " + itemName + " here to pick up."
    }
    else "Due to your disabilites, you can only carry one " + itemName + " at a time."
  }
   

  def inventory: String = {
    if (bag.isEmpty) "Good. You aren't carrying a thing."
    else "For some unexplainable reason, you are carrying:\n" + bag.keys.mkString("\n")
  }

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) {
      if (!rehabDone) this.world.turnsUntilFainting -= 1
      "Somehow you make your way " + direction + "." + this.excercisingMessage
    }
    else "Trying to go " + direction + " would be insane. You might want to try another direction."
  }


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = {
    if (this.drunkenessLevel>0) {
      this.drunkenessLevel -=1
      "You get some support for your leg and lay for a while. The burning ethanol disturbs your inner peace."
    }
    else "You get some support for your leg and lay for a while."
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }
  
  def complain(): String = {
    if (!currentLocation.npcsHere.isEmpty) currentLocation.npcsHere.toVector(0)._2.guidance
    else "There is no one to listen to your whining."
  }
  
  def help() = {
    "You can use the following commands:\n go 'direction' \n rest \n get 'item name' \n drop 'item name' \n examine 'item name' \n inventory \n use 'item name' \n complain \n buy 'item name' \n quit"
  }

  def use(itemName:String):String = {
    if (currentLocation.contains(itemName) && itemName == "painkiller" && !bag.contains("painkiller")) {
      "It's hard to use something you don't have."
    }
    else if (currentLocation == this.world.store && itemName == "beer" && !bag.contains("beer")) {
      "It's hard to use something you don't have."
    }
    else if (currentLocation.contains(itemName) && itemName == "aqua_jogging_belt") {
      if (drunkenessLevel== 0) {
        if (!rehabDone) this.world.turnsUntilFainting -= 1
        this.hasAquaJogged = true
        currentLocation.itemsHere(itemName).use + excercisingMessage
      }
      else "You are too drunk, you'd just drown. Go rest."
    }
    else if (currentLocation.contains(itemName) && itemName == "beer") {
      drunkenessLevel += 1
      val statement = currentLocation.itemsHere(itemName).use
      currentLocation.removeItem("beer")
      statement
    }
    else if (currentLocation.contains(itemName) && itemName=="excercise_bike") {
      if (this.drunkenessLevel==0) {
        if (!rehabDone) this.world.turnsUntilFainting -= 1
        this.hasRodeExcerciseBike = true
        currentLocation.itemsHere(itemName).use + excercisingMessage
      }
      else "High risk of a heart attack. Go rest."
    }
    else if (currentLocation.contains(itemName) && itemName=="staff") {
      "Nothing interesting happens."
    }
    else if (bag.contains(itemName) && itemName == "beer") {
      drunkenessLevel += 1
      val statement = bag(itemName).use
      bag.remove("beer")
      statement
    }
    else if (bag.contains(itemName) && itemName == "painkiller") {
      if (this.world.turnsUntilFainting <= 10) {
        if (!rehabDone) this.world.turnsUntilFainting += 5
        val statement = bag(itemName).use
        bag.remove("painkiller")
        statement
      }
      else "Wowwwowow, the mission here was not to die due an overdose."
    }
    else "There is no such item available, dumb ass. Are you drunk?"
  }
    
  

  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


}

