package lib

import kotlin.math.max
import kotlin.math.min

class SparseTable(val N: Int, val A: IntArray, K: Int) {
  private inline fun fx(a: Int, b: Int) = min(a, b)
  private val zero: Int = 1e9.toInt() + 100
//    private inline fun fx(a: Int, b: Int) = max(a, b)
//    private val zero: Int = 0

  private val tbl = Array(K) { IntArray(N){zero} }
  private val n2k = IntArray(N + 1)

  init {
    A.copyInto(tbl[0])
    for (k in 1 until K) {
      val len = 1 shl (k-1) // k-1での長さ
      val lst = tbl[k - 1]
      for (i in 0 until N - len) {
        tbl[k][i] = fx(lst[i], lst[i + len])
      }
    }

    for (k in 0 until K) {
      val n = 1 shl k
      if (n < N) n2k[n] = k
    }
    for (n in 1 .. N) {
      n2k[n] = max(n2k[n], n2k[n-1])
    }
  }

  /**
   * [l, r)
   */
  fun query(l: Int, r: Int): Int {
    val n = r - l
    if (n == 0) return zero
    val k = n2k[n]
    val len = 1 shl k
    return fx(tbl[k][l], tbl[k][r - len])
  }
}