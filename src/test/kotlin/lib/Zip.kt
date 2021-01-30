package lib

class Zip(A: IntArray) {
  val codes: IntArray
  val unq: IntArray
  private val n = A.size

  init {
    val res = factorize(A)
    codes = res.first
    unq = res.second
  }

  /**
   * 存在しなかったら-1
   */
  fun code(a: Int): Int {
    val code = lb(unq, a)
    if (code == n || unq[code] != a) return -1
    return code
  }

  companion object {
    /**
     * return (codes, unq)
     */
    fun factorize(A: IntArray): Pair<IntArray, IntArray> {
      val unq = A.distinct().toIntArray()
      unq.sort()
      val codes = IntArray(A.size)
      for (i in A.indices) {
        codes[i] = lb(codes, A[i])
      }
      return Pair(codes, unq)
    }

    fun lb(A: IntArray, x: Int): Int {
      var l = -1
      var h = A.size
      while (h - l > 1) {
        val m = (h + l) / 2
        if (A[m] >= x) h = m
        else l = m
      }
      return h
    }
  }
}