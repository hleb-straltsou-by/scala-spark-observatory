package observatory.grid

import observatory.{GridLocation, Location}
import observatory.calculation.InterpolationCalculations._
import observatory.constant.CalculationConstants._

class Grid(width: Int, height: Int, buffer: Array[Double]) {

  def this(width: Int, height: Int, temperatures: Iterable[(Location, Double)]) {
    this(width, height, new Array[Double](width * height))

    for (y <- 0 until height) {
      for (x <- 0 until width) {
        buffer(y * width + x) = inverseDistanceWeighting(temperatures, xyToLocation(x, y), inverseDistanceWeightingPower)
      }
    }
  }

  def xyToLocation(x: Int, y: Int): Location = Location((height / 2) - y, x - (width / 2))

  def asFunction(): GridLocation => Double = {
    gridLocation => {
      val x = gridLocation.lon + 180
      val y = 90 - gridLocation.lat
      buffer(y * width + x)
    }
  }

  def asArray(): Array[Double] = buffer
}