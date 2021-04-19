package lib

/**
 * @param mod 逆数計算するため素数でないといけない
 */
class Comb(n: Int, val mod: Int) {

  val F = LongArray(n + 1)
  val I = LongArray(n + 1)

  // n>=modでも処理できるように、*modを別扱いにする
  val cntF = IntArray(n + 1) // Fの*modの数
  val x = IntArray(n + 1) // 数値から*modを取り除いた値
  val cntX = IntArray(n + 1) // xの*modの数

  init {
    F[0] = 1
    I[0] = 1
    for (i in 1..n) {
      x[i] = i
      if(x[i] % mod == 0) {
        val j = i/mod
        cntX[i] = cntX[j] + 1
        x[i] = x[j]
      }
      F[i] = F[i - 1] * x[i] % mod
      cntF[i] += cntF[i - 1] + cntX[i]
    }

    I[n] = powMod(F[n], (mod - 2).toLong(), mod)
    for (i in n - 1 downTo 0) {
      I[i] = I[i + 1] * x[i + 1] % mod
    }
  }

  private fun powMod(a: Long, n: Long, mod: Int): Long {
    if (n == 0L) return 1
    val res = powMod(a * a % mod, n / 2, mod)
    return if (n % 2 == 1L) res * a % mod else res
  }

  fun comb(n: Int, k: Int): Long {
    if (n < k) return 0L
    val c = cntF[n] - cntF[k] - cntF[n - k]
    return if (c > 0) 0 else F[n] * I[k] % mod * I[n - k] % mod
  }

  fun perm(n: Int, k: Int): Long {
    val c = cntF[n] - cntF[n - k]
    return if (c > 0) 0 else F[n] * I[n - k] % mod
  }

  fun inv(x: Int): Long {
    val c = cntF[x] - cntF[x - 1]
    return if (c > 0) 0 else I[x] * F[x - 1] % mod
  }

  /**
   * nのグループからk回重複ありで選ぶ組み合わせ数
   * n - 1のしきりとkの○で考える
   */
  fun H(n: Int, k: Int) = comb(n + k - 1, k)
}