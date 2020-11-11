package lib

import kotlin.math.min

class BIT(val n: Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val bit = IntArray(N + 1)

  fun sumUntil(i: Int): Int {
    var x = i
    var s = 0
    while(x > 0) {
      s += bit[x]
      x -= x and -x
    }
    return s
  }

  fun add(i: Int, a: Int) {
    var x = i + 1
    while(x <= N) {
      bit[x] += a
      x += x and -x
    }
  }


  private inline fun sub(a: Int, b: Int) = a - b
  private inline fun lt(a: Int, b: Int) = a < b

  fun lowerBound(W: Int): Int {
    var k = N
    var x = 0
    var w = W
    while(k > 0) {
      if (x + k <= N && lt(bit[x + k], w)) {
        w = sub(w, bit[x + k])
        x += k
      }
      k /= 2
    }
    return min(n, x)
  }
}