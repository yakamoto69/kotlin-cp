package lib

fun powMat(a: Array<LongArray>, n: Long, mod: Int): Array<LongArray> {
  if (n == 1L) return a
  val res = powMat(mulMat(a, a, mod), n / 2, mod)
  return if (n % 2 == 1L) mulMat(res, a, mod) else res
}

fun mulMat(a: Array<LongArray>, b: Array<LongArray>, mod: Int): Array<LongArray> {
  assert(a[0].size == b.size)
  val len = a[0].size
  val h = a.size
  val w = b[0].size
  val res = Array(h){LongArray(w)}
  val th = 7e18.toLong()
  for (i in 0 until h) {
    for (j in 0 until w) {
      var v = 0L
      for (k in 0 until len) {
        v += a[i][k] * b[k][j]
        if (v > th) v %= mod
      }
      res[i][j] = if (v >= mod) v % mod else v
    }
  }
  return res
}