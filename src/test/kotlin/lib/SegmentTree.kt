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

  // 条件を満たすか。maxなら '>' minなら '<' が使える
  private inline fun contains(a: Int, x: Int): Boolean = a < x
  /**
   * @param x 左右どちらからか最初に a < x になる場所を探す
   * @param [a, b) 調べる範囲
   * @param LR 左から調べるか
   * @return この範囲に見つからなかったら -1
   **/
  fun find(x: Int, a: Int = 0, b: Int = N, LR: Boolean = true, k: Int = 1, l: Int = 0, r: Int = N): Int {
    if (a >= r || l >= b) return -1// ノードが範囲からはずれてる
    if (!contains(dat[k], x)) return -1
    if (l + 1 == r) return l // 0-indexed

    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    if (LR) {
      val ans = find(x, a, b, LR, lft, l, m)
      if (ans != -1) return ans
      return find(x, a, b, LR, rgt, m, r)
    } else {
      val ans = find(x, a, b, LR, rgt, m, r)
      if (ans != -1) return ans
      return find(x, a, b, LR, lft, l, m)
    }
  }
}
