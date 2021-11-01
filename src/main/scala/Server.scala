import akka.actor.Actor
import akka.event.{Logging, LoggingAdapter}

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{File, FileNotFoundException}
import javax.imageio.{IIOException, ImageIO}
import scala.concurrent.ExecutionContext.Implicits.global

class Server extends Actor {

  val log: LoggingAdapter = Logging(context.system, this)

  val usage = """
    filterboi [--path /path/of/pic] [--test <width>X<height>]
    Options:
        -> /path/of/file.jpg
        """
  println(usage)
  val path = scala.io.StdIn.readLine("->  ")


  def receive : PartialFunction[Any, Unit] = {


    case "parallel" ⇒ {

      try {
        val imgP: BufferedImage = ImageIO.read(new File(path))
        //save Futures in variables
        val parallel = MedianFilters.medianFP(imgP)


        log.info("Applying Parallel Median Filter...")
        parallel.map {
          result =>
            var curr: BufferedImage = result._1
            val g = curr.getGraphics
            g.setFont(g.getFont.deriveFont(18f))
            g.setColor(Color.white)
            g.drawString(s"Parallel Median Filter, run time in (seconds): ${result._2.toString} s", 100, 50)
            ImageIO.write(curr, "jpg", new File("./src/main/out/parallel.jpg"))
            log.info("Success! output is called 'parallel.jpg' and it is inside your /out/")

        }
      } catch{
        case e: FileNotFoundException ⇒ println("File not found,restart client and enter a valid path")
        case e: IIOException ⇒ println("File not found, restart client and enter a valid path")
          System.exit(0)
      }
    }

    case "concurrent" ⇒ {

      try {
        val imgS: BufferedImage = ImageIO.read(new File(path))
        val concurrent = MedianFilters.medianFS(imgS)
        log.info("Applying concurrent Median Filter...")
        concurrent.map {
          result =>
            var curr: BufferedImage = result._1
            val g = curr.getGraphics
            g.setFont(g.getFont.deriveFont(18f))
            g.setColor(Color.white)
            g.drawString(s"concurrent Median Filter, run time in (seconds) : ${result._2.toString} s", 100, 50)
            ImageIO.write(curr, "jpg", new File("./src/main/out/concurrent.jpg"))
            log.info("Success! output is called 'concurrent.jpg' and it is inside your /out/'")
            log.info("Press enter to exit program")

            val exit = scala.io.StdIn.readLine()
            if(exit == ""){
              println("Thank you for using the median filter simulator client!")
              System.exit(0)
            }

        }
      } catch{
        case e: FileNotFoundException ⇒ println("File not found,restart client and enter a valid path")
        case e: IIOException ⇒ println("File not found, restart client and enter a valid path")
          System.exit(0)
      }
    }
  }
}

