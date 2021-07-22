package lib

import kotlin.math.min

object ZAlgorithm {
  fun zAlgorithm(A: IntArray): IntArray {
    val n = A.size
    val z = IntArray(n)
    var j = 0
    for (i in 1 until n) {
      var k = if (j + z[j] <= i) 0 else min(j + z[j] - i, z[i - j])
      while(i + k < n && A[k] == A[i + k]) ++k
      z[i] = k
      if (j + z[j] < i + z[i]) j = i
    }
    z[0] = n
    return z
  }
}