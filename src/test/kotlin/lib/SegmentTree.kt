package lib

class SegmentTree(n: Int, val zero: Int, val f: (Int, Int) -> Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val dat = IntArray(2 * N){zero}

  fun update(i: Int, a: Int) {
    var ix = i + N
    dat[ix] = a
    while(ix > 1) {
      dat[ix shr 1] = f(dat[ix], dat[ix xor 1])
      ix = ix shr 1
    }
  }

  /**
    * [a, b)
    */
  fun query(a: Int, b: Int): Int {
    var res: Int = zero
    var left = a + N
    var right = b - 1 + N

    while(left <= right) {
      if ((left and 1) == 1) res = f(res, dat[left])
      if ((right and 1) == 0) res = f(res, dat[right])
      left = (left + 1) shr 1 // 右の子供なら右の親に移動
      right = (right - 1) shr 1 // 左の子供なら左の親に移動
    }

    return res
  }
}
