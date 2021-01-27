package o1.SaveTheKnee


/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class saveTheKnee {

  /** The title of the adventure game. */
  val title = "Save the Knee!"
  
  
  private val sportsHall      = new Area("Sports hall", "This is no place for broken knees. There's nothing but more ways to get hurt here.\nIt's noisy ")
  private val park            = new Area("Park", "You see bums here and there. Better stay away from them...\nThe place gives you haunting vibes.")
  private val teekkariVillage = new Area("TeekkariVillage", "You are in the way of all the busy people. Wonder why they are all wearing overalls though.")
  private val swimmingPools   = new Area("Swimming Pools", "Splash splosh.\nDon't run on the corridors, but in the pool you may.")
  private val hospital        = new Area("Hospital", "You feel welcome and cozy. The nurses seem very helpful.")
  private val gym             = new Area("Gym", "Everyone's looking at you like you were in the wrong place. Don't let them disturb you...") 
  private val drugstore       = new Area("Drugstore", "You know what to get. The pharmacist may know womething useful as well.")
          val store           = new Area("Spirits Store",  "The shelfs are loaded with all kinds of temptations. One bottle can't be that bad...")
  private val physiotherapist = new Area("Physiotherapist", "This feels like the right place. Why is he always running late?")

   sportsHall.setNeighbors(Vector(       "north" -> park,           "east" -> swimmingPools,    "south" -> teekkariVillage,  "west" -> drugstore     ))
         park.setNeighbors(Vector(                                  "east" -> gym,              "south" -> sportsHall,       "west" -> store         ))
     teekkariVillage.setNeighbors(Vector("north" -> sportsHall,     "east" -> physiotherapist,                               "west" -> hospital      ))
swimmingPools.setNeighbors(Vector(       "north" -> gym,                                        "south" -> physiotherapist,  "west" -> sportsHall    ))
     hospital.setNeighbors(Vector(       "north" -> drugstore,      "east" -> teekkariVillage                                                        ))
          gym.setNeighbors(Vector(                                                              "south" -> swimmingPools,    "west" -> park          ))
   drugstore.setNeighbors (Vector(       "north" -> store,          "east" -> sportsHall,       "south" -> hospital                                  ))
        store.setNeighbors(Vector(                                  "east" -> park,             "south" -> drugstore                                 ))
 physiotherapist.setNeighbors(Vector(    "north"-> swimmingPools,                                                            "west"-> teekkariVillage))
 
   
         drugstore.addItem(new Painkiller("painkiller", "This is your elixir of life"))
     swimmingPools.addItem(new AquaJoggingBelt("aqua_jogging_belt","Do I really have to wear one of these?"))
               gym.addItem(new ExcerciseBike("excercise_bike","Looks like it's at least 50 years old. The seat is the ultimatum of discomfort."))
        sportsHall.addItem(new Staff("staff", "Gandalf has left something here." ))
              park.addItem(new Beer("beer", "Cold. Would this relieve some pain?"))
   teekkariVillage.addItem(new Beer("beer", "Cold. Would this relieve some pain?"))
             store.addItem(new Beer("beer", "Cold. Would this relieve some pain?"))
   
   
   val coach = new Npc ( "coach" , "coach: Come on! You're better than that!" , "coach: Hmm, I think you should go to the hospital" )
   val therapist = new Npc ("the therapist", "physiotherapist: Yeah, I see what's wrong", "physiotherapist: Aquajogging and cycling will do the job.")
 
               park.addNpc(new Npc("hooded stranger", "someone, somewhere: Is it the police?!", "hooded stranger: Ehi khow are yocu why you walk libke thisi"))
          drugstore.addNpc(new Npc("pharmacist", "pharmacist: Good day! How may I help you?", "pharmacist: I recommend taking a painkiller, It'll help."))
    physiotherapist.addNpc(therapist)
    teekkariVillage.addNpc(new Npc ("a wild engineer", "a wild engineer: Omg, a stranger!", "a wild engineer: Erm, you are better off replacing it with a robot leg."))
              store.addNpc(new Npc("cashier", "cashier: Hi!", "cashier: Sorry, but we only sell beer."))
             hospital.addNpc(new Npc("doctor","doctor: Hmm, looks like your lig. cruciatum anterius is torn apart... I'm not gonna operate it, bro","doctor: Consult a physiotherapsit for help."))
         sportsHall.addNpc(coach)
    

  /** The character that the player controls in the game. */
    val player = new Player(sportsHall,this)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40
  
  var turnsUntilFainting = 6

  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = player.rehabDone && (player.location == sportsHall) && player.has("beer")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || this.turnsUntilFainting == 0

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "Oh no! You hurt your knee real bad. The pain is overwhelming but you can still walk. The next tournament is really soon, come back with a healed knee before it starts! \nRemember to complain as much as possible. \n\nUgh, painkillers could help."

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "Nicely done! You got your knee working properly, now you can finally play beer bong again! Just in time for the major beer pong tournament."
    else if (this.turnCount == this.timeLimit)
      "R.I.P. You didn't get help soon enough. Your leg collapses. It will never work again;(\nGame over!"
    else if (this.turnsUntilFainting == 0)
      "The severe pain in your knee makes you fell dizzy... you faint.\nGame over!"
    else  // game over due to player quitting
      "Quitter!" 
  } 


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
      if (this.player.rehabDone) {
        this.coach.say = "coach: Get some beer boiiii"
        this.therapist.say = "physiotherapsit: Looks like your knee is working properly. I think you can do sports again."
      }
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }


}

