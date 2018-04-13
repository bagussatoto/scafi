package it.unibo.scafi.simulation.gui.view.scalaFX.drawer

import javafx.scene.Node
import javafx.scene.shape.{Circle, Rectangle, Shape}

import it.unibo.scafi.simulation.gui.launcher.SensorName._
import it.unibo.scafi.simulation.gui.model.core.World
import it.unibo.scafi.simulation.gui.model.graphics2D.BasicShape2D.{Circle => InternalCircle, Polygon => InternalPolygon, Rectangle => InternalRectangle}
import it.unibo.scafi.simulation.gui.model.simulation.BasicSensors.{DisplaySensor, OnOffSensor}
import it.unibo.scafi.simulation.gui.view.Drawer
import it.unibo.scafi.simulation.gui.view.scalaFX._

import scalafx.application.Platform
import scalafx.geometry.{Point2D => FXPoint}
import scalafx.scene.Node
import scalafx.scene.paint.Color
private[drawer] object nodeToShape {
  def create[INPUTNODE <: World#NODE](node: INPUTNODE): Shape = {
    val p: FXPoint = node.position
    import scalafx.Includes._
    val defaultShape = new Rectangle {
      this.x = p.x
      this.y = p.y
      this.width = 10
      this.height = 10
      this.fill = Color.Red
    }
    var shape: Shape = defaultShape
    if (node.shape.isDefined) {
      node.shape.get match {
        case r: InternalRectangle => shape = new Rectangle {
          this.x = p.x
          this.y = p.y
          this.width = r.w
          this.height = r.h
        }
        case c: InternalCircle => shape = new Circle {
          this.centerX = p.x
          this.centerY = p.y
          this.radius = c.r
          this.smooth = false
        }
        case _ =>
      }
    }
    shape
  }
}
