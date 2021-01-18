package lib

class Comb(n: Int, val mod: Int) {

  val F = LongArray(n + 1)
  val I = LongArray(n + 1)
  init {
    F[0] = 1
    for (i in 1..n) {
      F[i] = F[i - 1] * i % mod
    }
    I[n] = powMod(F[n], (mod - 2).toLong(), mod)
    for (i in n - 1 downTo 0) {
      I[i] = I[i + 1] * (i + 1) % mod
    }
  }

  private fun powMod(a: Long, n: Long, mod: Int): Long {
    if (n == 0L) return 1
    val res = powMod(a * a % mod, n / 2, mod)
    return if (n % 2 == 1L) res * a % mod else res
  }

  fun comb(n: Int, k: Int): Long {
    if (n < k) return 0L
    return F[n] * I[k] % mod * I[n - k] % mod
  }

  fun perm(n: Int, k: Int): Long {
    return F[n] * I[n - k] % mod
  }

  fun inv(x: Int): Long {
    return I[x] * F[x - 1] % mod
  }

  /**
   * nのグループからk回重複ありで選ぶ組み合わせ数
   * n - 1のしきりとkの○で考える
   */
  fun H(n: Int, k: Int) = comb(n + k - 1, k)
}