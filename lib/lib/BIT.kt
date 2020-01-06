package lib

class BIT(n: Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val bit = IntArray(N + 1)

  fun sum(i: Int): Int {
    var x = i
    var s = 0
    while(x > 0) {
      s += bit[x]
      x -= x and -x
    }
    return s
  }

  fun add(i: Int, a: Int) {
    var x = i + 1
    while(x <= N) {
      bit[x] += a
      x += x and -x
    }
  }
}