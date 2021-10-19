package be.icteam.demo
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import scala.util.Random


object Program {

    def main(args: Array[String]) : Unit = {

        // FROM https://stackoverflow.com/questions/2315912/best-way-to-parse-command-line-parameters
        val usage = """
    filterboi [--path /path/of/pic] [--test <width>X<height>]
    Options:
        --path | -p   -- path of picture
        --test | -t   -- if you wanna test the algorithm
        """

        println("" +
          " _______  ___  ___      _______  _______  ______    _______  _______  ___ \n" +
          "|       ||   ||   |    |       ||       ||    _ |  |  _    ||       ||   |\n" +
          "|    ___||   ||   |    |_     _||    ___||   | ||  | |_|   ||   _   ||   |\n" +
          "|   |___ |   ||   |      |   |  |   |___ |   |_||_ |       ||  | |  ||   |\n" +
          "|    ___||   ||   |___   |   |  |    ___||    __  ||  _   | |  |_|  ||   |\n" +
          "|   |    |   ||       |  |   |  |   |___ |   |  | || |_|   ||       ||   |\n" +
          "|___|    |___||_______|  |___|  |_______||___|  |_||_______||_______||___|\n" +
          "\n" +
          "By Frenzy :3\n" +
          "version 0.1: Ponganse a estudiar :v")

        if (args.length == 0) {
            println(usage)
            
        }
        //val arg_list = args.toList
        val arg_list = List("--test", "100X100")
        def option_map(map: Map[Symbol, Any], list: List[String]) : Map[Symbol, Any] = {
            list match {
                case Nil => map
                case ("--path" | "-p") :: string :: tail =>
                            option_map(map ++ Map(Symbol("path") -> string), tail)
                case ("--test" | "-t") :: value :: tail =>
                            option_map(map ++ Map(Symbol("dimensions") -> value.split("X").toList), tail)
                case string :: Nil =>  
                            option_map(map ++ Map('infile -> string), list.tail)
                case option :: tail => 
                            println("Unknown option " + option) 
                            sys.exit(1) 
            }
        }
        val options = option_map(Map(), arg_list)
        println(options)

        
        val rnd = new Random
        val hundred: Array[Array[Int]] = Array.fill(10)(Array.fill(10)(10+rnd.nextInt( (40) + 1 )))
        print_matrix(hundred)
        filter_boi(hundred, 10, 10, 3)
        
    }
    
    // Filter -> https://en.wikipedia.org/wiki/Median_filter
    /**
      * allocate ouputPixelValue[image width]
      *
      * @param A
      * @return
      */
    def filter_boi(A: Array[Array[Int]], width: Int, height: Int, f_size: Int) : Array[Array[Int]] = {
        if (A.length.equals(0)) return null
        var new_matrix: Array[Array[Int]] = Array.fill(10)(Array.fill(10)(0))
        val indexer = (f_size / 2).floor.toInt
        var temp = new Array[Int](0)

        for (y <- 0 to height-1) {
            for (x <- 0 to width-1) {
                for (fy <- 0 to f_size-1) {
                    if (y + fy - indexer < 0 || y + fy - indexer > height - 1) {
                        for (c <- 0 to f_size - 1) temp = temp :+ 0
                    } else if (x + fy - indexer < 0 || x + indexer > width - 1) {
                        temp :+ 0
                    } else {
                        for (fx <- 0 to f_size-1) {
                            if (x + fx - indexer >= 0) {
                                temp = temp :+ A(y+fy-indexer)(x+fx-indexer)
                            }                             
                        }
                    }

                }
                new_matrix(y)(x) = median(temp, temp.length)
                temp = new Array[Int](0)
            }

        }
        return new_matrix
    }

    /**
      * Finds median
      *
      * @param A
      * @param n
      * @return
      */
    def median(A: Array[Int], n: Int) : Int = {
        if (n <= 0) return 0
        return A.sortWith(_ < _).drop(A.length/2).head
    }

    /**
      * 
      *
      * @param A
      */
    def print_matrix(A: Array[Array[Int]]) : Unit = {
        for (i <- A) {
            println(i.toBuffer)
        }
    }

    def image_to_array() : Array[Int] = {

        return new Array[Int](0)
    }

}

// Thread 1 -> where our implementation is utilized
class Thread_a {

}

// Thread 2 -> where scala's implementation is applied
class Thread_2 {


}


// TODO
// Genereate Pepper object
// Pepper uses OpenCV to add pepper filter to "damage"
// image. For test cases only
// import org.byteco.opencv.opencv_
object Pepper {


}