/*
 * Copyright (C) 2016-2017, Roberto Casadei, Mirko Viroli, and contributors.
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package it.unibo.scafi.renderer3d.util

import it.unibo.scafi.renderer3d.util.RichScalaFx._
import scalafx.geometry.Point3D
import scalafx.scene.AmbientLight
import scalafx.scene.control.Label
import scalafx.scene.paint.{Color, Material, PhongMaterial}
import scalafx.scene.shape.{Box, Cylinder}
import scalafx.scene.text.Font
import scalafx.scene.transform.{Rotate, Translate}

object Rendering3DUtils {
  private var materialCache: Map[Color, Material] = Map()

  def createAmbientLight: AmbientLight = new AmbientLight()

  def createLabel(string: String, fontSize: Int, position: Point3D): Label = {
    val label = new Label(){
      font = new Font(fontSize)
      text = string
    }
    label.moveTo(position)
    label
  }

  def createBox(size: Int, color: Color, position: Point3D): Box = {
    val box = new Box(size, size, size)
    box.setColor(color)
    box.moveTo(position)
    box
  }

  def createMaterial(color: Color): Material =
    materialCache.getOrElse(color, {
      val material = new PhongMaterial {diffuseColor = color; specularColor = color}
      materialCache += (color -> material)
      material
    })

  def createLine(origin: Point3D, target: Point3D, visible: Boolean, color: java.awt.Color): Cylinder = {
    val line = createCylinder(origin, target)
    line.setColor(color)
    line.setVisible(visible)
    line
  }

  /**
   * From https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
   * */
  private def createCylinder(origin: Point3D, target: Point3D, thickness: Int = 2) = {
    val differenceVector = target.subtract(origin)
    val lineMiddle = target.midpoint(origin)
    val moveToMidpoint = new Translate(lineMiddle.getX, lineMiddle.getY, lineMiddle.getZ)
    val axisOfRotation = differenceVector.crossProduct(Rotate.YAxis)
    val angle = Math.acos(differenceVector.normalize.dotProduct(Rotate.YAxis))
    val rotateAroundCenter = new Rotate(-Math.toDegrees(angle), new Point3D(axisOfRotation))
    val line = new Cylinder(thickness, differenceVector.magnitude, 3)
    line.getTransforms.addAll(moveToMidpoint, rotateAroundCenter)
    line
  }
}
