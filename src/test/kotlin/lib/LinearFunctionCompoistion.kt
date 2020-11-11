package lib

class LinearFunctionCompositionBIT(private val N: Int) {

  private val c0 = BIT(N)
  private val c1 = BIT(N)

  /**
   * @param s f(l)の値
   */
  fun add(l: Int, r: Int, delta: Int, s: Int) {
    c1.add(l, delta)
    c1.add(r, -delta)
    c0.add(l, s - l * delta)
    c0.add(r, -(s - l * delta))
  }

  fun calc(x: Int): Int {
    return c1.sumUntil(x + 1) * x + c0.sumUntil(x + 1)
  }
}

class LinearFunctionComposition(val N: Int) {

  private val c0 = LongArray(N)
  private val c1 = LongArray(N)

  /**
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