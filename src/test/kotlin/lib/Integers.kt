package lib

import kotlin.math.abs

fun divisors(x: Int): MutableList<Int> {
  val res = mutableListOf<Int>()
  val post = mutableListOf<Int>()
  var d = 1
  while (d * d <= x) {
    if (x % d == 0) {
      res += d
      if (d * d != x) post += x / d
    }
    ++d
  }
  for (i in post.size - 1 downTo 0) {
    res += post[i]
  }
  return res
}

fun powMod(a: Int, n: Long, mod: Int): Long {
  if (n == 0L) return 1
  val res = powMod((a.toLong() * a % mod).toInt(), n / 2, mod)
  return if (n % 2 == 1L) res * a % mod else res
}

fun gcd(A: IntArray): Int {
  var g = A[0]
  for (i in 1 until A.size) {
    g = gcd(g, A[i])
  }
  return g
}

tailrec fun gcd(a: Int, b: Int): Int {
  if (b == 0) return abs(a)
  return gcd(b, a % b)
}

/**
 * a, bがマイナスでもうまいこと動く
 */
private inline fun floor_div(a: Long, b: Long): Long {
  val sign = java.lang.Long.signum(b)
  val x = a*sign
  val y = b*sign
  return if (x > 0) x/y else -((-x+y-1)/y)
}

private inline fun ceil_div(a: Long, b: Long): Long {
  val sign = java.lang.Long.signum(b)
  val x = a*sign
  val y = b*sign
  return if (x > 0) (x+y-1)/y else x/y
}

/**
 * 結果が負だったら正にする
 */
private inline fun safe_mod(a: Long, m: Long): Long {
  val x = a % m
  return if (x >= 0) x else x+m
}

/**
 * @return (gcd(a,b), x; ax + by = gcd(a,b)かつ 0<=x<b/g)
 * a, bが素の場合、mod 上のa^-1。素でないなら(a/gcd(a, b))^-1
 *
 * https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
 * https://github.com/atcoder/ac-library/blob/master/atcoder/internal_math.hpp
 */
fun inv_gcd(a: Long, b: Long): Pair<Long, Long> {
  if (a == 0L) return Pair(b, 0)

  // r = a*s + b*t
  // r[1]: 2つ前, r[0]: 1つ前, r[1]に新しい値を入れる
  val r = longArrayOf(a, b)
  val s = longArrayOf(1, 0)
  val t = longArrayOf(0, 1)
  var i = 0
  while(r[i xor 1] != 0L) {
    i = i xor 1
    val q = floor_div(r[i xor 1], r[i]) // r[i-1] / r[i]
    r[i xor 1] -= q*r[i]
    s[i xor 1] -= q*s[i]
    t[i xor 1] -= q*t[i]
  }
  val g = r[i]
  var x = s[i]
  if (x < 0) x += floor_div(b, g) // axはlcm刻みなので、xの刻みはb/g
  return Pair(g, x)
}

/**
 * 中国人剰余定理
 * @return (crt, lcm)
 *
 * https://qiita.com/drken/items/ae02240cd1f8edfc86fd
 * https://github.com/atcoder/ac-library/blob/master/atcoder/math.hpp
 */
fun crt(m: LongArray, r: LongArray): Pair<Long, Long>? {
  val n = m.size
  var r0 = 0L
  var m0 = 1L
  // 必要ないところも脳死でfloor_div, safe_modを使っている
  for (i in 0 until n) {
    assert(r[i] in 0 until m[i])
    val (g, t) = inv_gcd(m0, m[i])
    if (safe_mod(r[i] - r0, g) != 0L) return null
    val u1 = floor_div(m[i], g)
    val s = safe_mod(floor_div(r[i] - r0, g), u1)
    val x = safe_mod(s * t, u1) // 最後はm0*u1でmodすることになる。後からm0をかけるので、この時点でu1でmodする
    r0 += x*m0 // s*t*m0。 0<=r0<m0, 0<=x<u1 => 0<=r0+x*m0<m0*u1
    m0 *= u1
    if (r0 < 0) r0 += m0
  }
  return Pair(r0, m0)
}


object ModSupports {
  private val MOD = 1_000_000_007
  private inline fun minus(n: Long) = (MOD - n) % MOD
  private inline fun mul(a: Long, b: Long) = a * b % MOD
  private inline fun pls(a: Long, b: Long): Long {
    var res = a + b
    if (res >= MOD) res -= MOD
    return res
  }
}