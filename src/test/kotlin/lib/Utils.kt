package lib

/**
 * countLt みたいなもの
 * [l, r) 方式
 */
fun lowerBound(A: IntArray, s: Int, x: Int): Int {
  var l = s - 1
  var h = A.size
  while(h - l > 1) {
    val m = (h + l) / 2
    if (A[m] >= x) h = m
    else l = m
  }
  return h
}

fun upperBound(A: IntArray, s: Int, x: Int): Int {
  var l = s - 1
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