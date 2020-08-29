package lib

import kotlin.math.abs

object LinearFunction {
  /**
   * Y = b/a・X + c/a の形式の１次関数
   * Y軸に並行な場合は a=0, b=1, c=X
   * X軸に並行な場合は a=1, b=0, c=Y
   * a>=0
   *
   * オブジェクトが重いので、Longで表せる範囲ならそうすること
   */
  data class LinearFn(val a: Int, val b: Int, val c: Int)
  fun computeLinearFn(x1: Int, y1: Int, x2: Int, y2: Int): LinearFn {
    var a = x2 - x1
    var b  = y2 - y1
    if (a < 0) {
      a *= -1
      b *= -1
    }
    if (a != 0 && b != 0) {
      val d = gcd(abs(a), abs(b))
      a /= d
      b /= d
    } else {
      if (a == 0) b = 1
      else a = 1
    }

    val c = if (a == 0) x1
    else if (b == 0) y1
    else y1 * a - x1 * b

    return LinearFn(a, b, c)
//    return a * (1L shl 48) + b * (1L shl 32) + c
  }
}