package lib.geometry

import kotlin.math.abs

object Geometry {
  data class Tri(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val x3: Int, val y3: Int) {
    /**
     * 面積の２倍
     */
    fun area(): Long = run {
      abs((x1-x3).toLong()*(y2-y3) - (x2 - x3).toLong()*(y1-y3))
    }
  }
}