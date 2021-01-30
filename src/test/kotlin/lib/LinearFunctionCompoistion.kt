package lib

import java.util.*

class LinearFunctionCompositionBIT(private val N: Int) {

  private val c0 = BIT(N)
  private val c1 = BIT(N)

  /**
   * [l, r)
   * @param s f(l)の値
   */
  fun add(l: Int, r: Int, delta: Int, s: Int) {
    c1.addFrom(l, delta)
    c1.addFrom(r, -delta)
    c0.addFrom(l, s - l * delta)
    c0.addFrom(r, -(s - l * delta))
  }

  fun calc(x: Int): Int {
    return c1.get(x) * x + c0.get(x)
  }
}

class LinearFunctionComposition(val N: Int) {

  private val c0 = LongArray(N)
  private val c1 = LongArray(N)

  /**
   * [l, r)
   * @param s f(l)の値
   */
  fun add(l: Int, r: Int, delta: Long, s: Long) {
    c1[l] += delta
    c1[r] -= delta
    c0[l] += s - l * delta
    c0[r] -= s - l * delta
  }

  fun build() {
    for (i in 0 until N - 1) {
      c0[i + 1] += c0[i]
      c1[i + 1] += c1[i]
    }
  }

  fun calc(x: Int): Long {
    return c1[x] * x + c0[x]
  }
}

class LinearFunctionCompositionMod(val N: Int, val MOD: Long) {

  private val c0 = LongArray(N)
  private val c1 = LongArray(N)

  /**
   * [l, r)
   * @param s f(l)の値
   */
  fun add(l: Int, r: Int, delta: Long, s: Long) {
    val d1 = if (delta >= 0) delta else delta + MOD
    c1[l] += d1
    if (c1[l] >= MOD) c1[l] -= MOD
    c1[r] -= d1
    if (c1[r] < 0) c1[r] += MOD

    var d0 = (s - l*d1%MOD)
    if (d0 < 0) d0 += MOD
    c0[l] += d0
    if (c0[l] >= MOD) c0[l] -= MOD
    c0[r] -= d0
    if (c0[r] < 0) c0[r] += MOD
  }

  fun build() {
    for (i in 0 until N - 1) {
      c0[i + 1] += c0[i]
      if (c0[i + 1] >= MOD) c0[i + 1] -= MOD
      c1[i + 1] += c1[i]
      if (c1[i + 1] >= MOD) c1[i + 1] -= MOD
    }
  }

  fun calc(x: Int): Long {
    return (c1[x] * x + c0[x])%MOD
  }

  fun reset() {
    Arrays.fill(c0, 0)
    Arrays.fill(c1, 0)
  }

  fun inspect() = Pair(c0, c1)
}