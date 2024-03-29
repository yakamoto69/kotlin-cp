package lib

/**
 * countLt みたいなもの
 */
fun lb(A: IntArray, x: Int): Int {
  var l = - 1
  var h = A.size
  while(h - l > 1) {
    val m = (h + l) / 2
    if (A[m] >= x) h = m
    else l = m
  }
  return h
}

fun upperBound(A: IntArray, x: Int): Int {
  var l = - 1
  var h = A.size
  while(h - l > 1) {
    val m = (h + l) / 2
    if (A[m] > x) h = m
    else l = m
  }
  return h
}

fun iterateKSizeSets(M: Int, k: Int, f: (Int) -> Unit) {
  var set = 0
  for (i in 0 until k) {
    set = set or (1 shl i)
  }
  while(set < (1 shl M)) {
    f(set)
    val x = set and -set // 最初の1のビット
    val y = set + x
    set = (set and y.inv()) / x shr 1 or y
  }
}

fun startThread() {
  val th = Thread(null, Runnable {

  },"solve", 1 shl 26)
  th.start()
  th.join()
}

private val D = arrayOf(
  intArrayOf(-1, 0),
  intArrayOf(0, 1),
  intArrayOf(1, 0),
  intArrayOf(0, -1)
)
private inline fun iter_D(N: Int, M: Int, r: Int, c: Int) {
  for (d in D) {
    val nr = r + d[0]
    val nc = c + d[1]
    if (nr in (0 until N) && nc in (0 until M)) {
    }
  }
}

// multisetの代わり
private val cnt = HashMap<Int, Int>() // TreeMapにすると遅いので気をつける
private inline fun incr(i: Int): Int {
  return cnt.merge(i, 1, Int::plus)!!
}
private inline fun decr(i: Int) {
  val now = cnt.merge(i, -1, Int::plus)
  if (now == 0) cnt.remove(i)
}

private fun makePow2(N: Int, MOD: Int) {
  val pow2 = LongArray(N + 1)
  pow2[0] = 1
  for (i in 1 until pow2.size) {
    pow2[i] = pow2[i - 1] * 2 % MOD
  }
}

/**
 * 桁が大きすぎるとBigIntegerで間に合わなくなる
 * 99 -> 100 のようなケースに対応するため、digitsは一桁多めに用意すること
 */
fun incr(digits: IntArray) {
  var i = 0
  digits[i]++
  while (digits[i] == 10) {
    digits[i] = 0
    digits[i + 1]++
    i++
  }
}
fun digitsStr(digits: IntArray): String {
  val str = digits.joinToString("")
  return if (str.startsWith("0")) str.drop(1) else str
}