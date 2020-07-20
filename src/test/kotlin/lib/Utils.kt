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

fun startThread() {
  val th = Thread(null, Runnable {

  },"", 1 shl 26)
  th.start()
  th.join()
}