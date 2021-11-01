import akka.actor.{ActorSystem, Props}

object Client extends App{
  val server = ActorSystem("Server")
  val actor = server.actorOf(Props[Server], name="filterboi")

  println(
    "" +
      " _______  ___  ___      _______  _______  ______    _______  _______  ___ \n" +
      "|       ||   ||   |    |       ||       ||    _ |  |  _    ||       ||   |\n" +
      "|    ___||   ||   |    |_     _||    ___||   | ||  | |_|   ||   _   ||   |\n" +
      "|   |___ |   ||   |      |   |  |   |___ |   |_||_ |       ||  | |  ||   |\n" +
      "|    ___||   ||   |___   |   |  |    ___||    __  ||  _   | |  |_|  ||   |\n" +
      "|   |    |   ||       |  |   |  |   |___ |   |  | || |_|   ||       ||   |\n" +
      "|___|    |___||_______|  |___|  |_______||___|  |_||_______||_______||___|\n" +
      "\n")

  actor ! "parallel"
  actor ! "concurrent"

}
