package lib

object LIS {
  fun lis(A: IntArray): Int {
    val N = A.size
    val cnt = IntArray(N)
    var p = 0
    for (i in 0 until N) {
      val idx = lb(cnt, 0, p, A[i])
      cnt[idx] = A[i]
      if (idx == p) p++
    }
    return p
  }

  /**
   * countLt みたいなもの
   */
  private fun lb(A: IntArray, s: Int, t: Int, x: Int): Int {
    var l = s - 1
    var h = t
    while(h - l > 1) {
      val m = (h + l) / 2
      if (A[m] >= x) h = m
      else l = m
    }
    return h
  }
}