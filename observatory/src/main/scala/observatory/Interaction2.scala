package observatory

import observatory.constant.ColorConstants._
import observatory.config.DataConfig._
/**
  * Object for basic visualisation with layers and final user interface polishing
  */
object Interaction2 {

  /**
    * This method returns the layers you want the user to be able to visualize.
    * Each layer has a name, a color scale and a range of supported years.
    *
    * @return The available layers of the application
    */
  def availableLayers: Seq[Layer] = List(
    Layer(LayerName.Temperatures, temperaturesColorScale, Range(firstYear, lastYear)),
    Layer(LayerName.Deviations, anomaliesColorScale, Range(normalYearsBefore, lastYear))
  )

  /**
    * This method takes the selected layer signal and
    * returns the years range supported by this layer.
    *
    * @param selectedLayer A signal carrying the layer selected by the user
    * @return A signal containing the year bounds corresponding to the selected layer
    */
  def yearBounds(selectedLayer: Signal[Layer]): Signal[Range] = {
    Signal(selectedLayer().bounds)
  }

  /**
    * This method takes the selected layer and the year slider value and returns
    * the actual selected year, so that this year is not out of the layer bounds.
    *
    * @param selectedLayer The selected layer
    * @param sliderValue The value of the year slider
    * @return The value of the selected year, so that it never goes out of the layer bounds.
    *         If the value of `sliderValue` is out of the `selectedLayer` bounds,
    *         this method should return the closest value that is included
    *         in the `selectedLayer` bounds.
    */
  def yearSelection(selectedLayer: Signal[Layer], sliderValue: Signal[Year]): Signal[Year] = Signal({
    val bounds = selectedLayer().bounds
    sliderValue()
      .max(bounds.start)
      .min(bounds.end)
  })

  /**
    * This method takes the selected layer and the selected year and returns
    * the pattern of the URL to use to retrieve the tiles.
    *
    * @param selectedLayer The selected layer
    * @param selectedYear The selected year
    * @return The URL pattern to retrieve tiles
    */
  def layerUrlPattern(selectedLayer: Signal[Layer], selectedYear: Signal[Year]): Signal[String] = {
    Signal(s"./target/${selectedLayer().layerName.id}/${selectedYear()}/{z}/{x}-{y}.png")
  }

  /**
    * @param selectedLayer The selected layer
    * @param selectedYear The selected year
    * @return The caption to show
    */
  def caption(selectedLayer: Signal[Layer], selectedYear: Signal[Year]): Signal[String] = Signal(
    s"${selectedLayer().layerName.id} ${selectedYear()}"
  )

}

sealed abstract class LayerName(val id: String)
object LayerName {
  case object Temperatures extends LayerName("temperatures")
  case object Deviations extends LayerName("deviations")
}

/**
  * @param layerName Name of the layer
  * @param colorScale Color scale used by the layer
  * @param bounds Minimum and maximum year supported by the layer
  */
case class Layer(layerName: LayerName, colorScale: Seq[(Temperature, Color)], bounds: Range)

