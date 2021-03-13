package lib

class SparseTable {
  fun hoge(N: Int, A: IntArray) {
    val tbl = mutableListOf(IntArray((N)))
    var k = 1
    while (2 * k <= N) {
      val build = IntArray(N)
      val lst = tbl.last()
      for (i in 0 until N) {
        build[i] = if (i + k < N) {
          if (A[lst[i]] >= A[lst[i + k]]) lst[i] else lst[i + k]
        } else {
          lst[i]
        }
      }
      tbl += build
      k *= 2
    }

//    debug { "${tbl.map { it.joinToString(" ") }.joinToString("\n")}" }

    val ksize = IntArray(N + 1)
    k = 1
    for (n in 1..N) {
      if (k * 2 == n) k *= 2
      ksize[n] = k
    }
  }
}