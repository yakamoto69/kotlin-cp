package lib

fun zip(a: IntArray) {
  val ids = a.distinct() as MutableList<Int>
  ids.sort()
  for (i in a.indices) {
    a[i] = lowerBound(ids, 0, a[i])
  }
}

fun lowerBound(A: MutableList<Int>, s: Int, x: Int): Int {
  var l = s - 1
  var h = A.size
  while(h - l > 1) {
    val m = (h + l) / 2
    if (A[m] >= x) h = m
    else l = m
  }
  return h
}