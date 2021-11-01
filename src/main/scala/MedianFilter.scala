import java.awt.Color
import java.awt.image.BufferedImage
import java.util

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import scala.concurrent._
import ExecutionContext.Implicits.global

object  MedianFilters {

  private def deltaTime[Runtime](blockOfTime: => Runtime): (Runtime, Double) = {
    val timeZero = System.nanoTime()
    val result = blockOfTime
    val timeFinal = System.nanoTime()
    val timedifference = timeFinal-timeZero
    (result, timedifference/ 1E9)
  }


  private def filterRange(InImg:BufferedImage, si:Int, ei:Int, sj:Int, ej:Int) = {
    val pixel = new Array[Color](9)
    val red = new Array[Int](9)
    val green = new Array[Int](9)
    val blue = new Array[Int](9)

    //i = row       j = columns
    for (i <- si until ei) {
      for( j <- sj until ej){

        //neighbor pixel values are stored in window including this pixel
        pixel(0) = new Color(InImg.getRGB(i - 1, j - 1))
        pixel(1) = new Color(InImg.getRGB(i - 1, j))
        pixel(2) = new Color(InImg.getRGB(i - 1, j + 1))
        pixel(3) = new Color(InImg.getRGB(i, j - 1))
        pixel(4) = new Color(InImg.getRGB(i, j))
        pixel(5) = new Color(InImg.getRGB(i, j + 1))
        pixel(6) = new Color(InImg.getRGB(i + 1, j - 1))
        pixel(7) = new Color(InImg.getRGB(i + 1, j))
        pixel(8) = new Color(InImg.getRGB(i + 1, j + 1))
        for (k <- 0 until pixel.length) {
          red(k) = pixel(k).getRed
          blue(k) = pixel(k).getBlue
          green(k) = pixel(k).getGreen
        }
        util.Arrays.sort(red)
        util.Arrays.sort(blue)
        util.Arrays.sort(green)
        InImg.setRGB(i, j, new Color(red(4), green(4), blue(4)).getRGB)
      }
    }
  }

  def medianFP(InImg:BufferedImage): Future[(BufferedImage, Double)] = Future {
    deltaTime{
      //
      val part1 = Future {
        filterRange(InImg, 1, InImg.getWidth/2, 1, InImg.getHeight/2)
      }
      val part2 = Future {
        filterRange(InImg, InImg.getWidth/2, InImg.getWidth - 1, 1, InImg.getHeight/2)
      }
      val part3 = Future {
        filterRange(InImg, 1, InImg.getWidth/2, InImg.getHeight/2, InImg.getHeight - 1)
      }
      val part4 = Future {
        filterRange(InImg, InImg.getWidth/2, InImg.getWidth - 1, InImg.getHeight/2, InImg.getHeight - 1)
      }

      Await.ready(part4, Duration.Inf)

      InImg
    }
  }

  def medianFS(img:BufferedImage): Future[(BufferedImage, Double)] = Future {
    deltaTime {
      filterRange(img, 1, img.getWidth - 1, 1, img.getHeight - 1)
      img
    }
  }

}
