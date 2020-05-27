package lib

/**
 * @param n 個数 最大値じゃないぞ。
 * iの範囲は[0, n - 1]
 *
 * AがIntやLongのときは埋め込んでしまおう
 * type A = Int
 */
class RangeUpdateTree(n: Int,
                      private val zero: Int,
                      private val f: (Int, Int) -> Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val dat = IntArray(2 * N){zero}

//  private inline fun f(a: Int, b: Int) = a + b

  /**
   * [l, r)
   */
  fun add(l: Int, r: Int, a: Int) {
    var left = l + N
    var right = r - 1 + N

    while(left <= right) {
      if ((left and 1) == 1) dat[left] = f(dat[left], a)
      if ((right and 1) == 0) dat[right] = f(dat[right], a)
      left = (left + 1) shr  1 // 右の子供なら右の親に移動
      right = (right - 1) shr 1 // 左の子供なら左の親に移動
    }
  }

  fun query(i: Int): Int {
    var ix = N + i
    var res: Int = zero

    while(ix >= 1) {
      res = f(res, dat[ix])
      ix = ix shr 1
    }

    return res
  }
}
