package lib

import kotlin.math.min

/**
 * RangeUpdateTreeと同等と考える
 */
class BIT(val n: Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val bit = IntArray(N + 1)

  /**
   * 0-indexed
   */
  fun get(i: Int): Int {
    var x = i + 1
    var s = 0
    while(x > 0) {
      s += bit[x]
      x -= x and -x
    }
    return s
  }

  /**
   * i: 0-indexed
   */
  fun addFrom(i: Int, a: Int) {
    var x = i + 1
    while(x <= N) {
      bit[x] += a
      x += x and -x
    }
  }

  /**
   * i: 0-indexed
   */
  fun addAt(i: Int, a: Int) {
    addFrom(i, a)
    addFrom(i + 1, -a)
  }


  private inline fun sub(a: Int, b: Int) = a - b
  private inline fun lt(a: Int, b: Int) = a < b

  /**
   * ↑の中身変えること
   * 累積後の値が単調増加になっていること
   * @return 0-indexed 見つからなかったら n
   */
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