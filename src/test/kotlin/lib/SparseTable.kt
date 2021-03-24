package lib

import kotlin.math.max

class SparseTable {
  fun build(N: Int, A: IntArray, K: Int) {
    val tbl = Array(K){IntArray(N)}
    A.copyInto(tbl[0])
    var len = 1
    for (k in 1 until K) {
      val lst = tbl[k - 1]
      for (i in 0 until N) {
        tbl[k][i] = if (i + len < N) max(lst[i], lst[i + len]) else lst[i]
      }
      len *= 2
    }

    val n2k = IntArray(N + 1)
    val n2len = IntArray(N + 1)
    len = 1
    var k = 1
    for (n in 1..N) {
      if (len * 2 == n) {
        len *= 2
        k++
      }
      n2k[n] = k
      n2len[n] = len
    }

    /**
     * [l, r)
     */
    fun max(l: Int, r: Int): Int {
      val n = r - l
      val k = n2k[n]
      val len = n2len[n]
      val e1 = tbl[k][l]
      val j = n - len
      val e2 = tbl[k][l + j]
      return max(e1, e2)
    }
  }
}