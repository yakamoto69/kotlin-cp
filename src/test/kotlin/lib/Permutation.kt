package lib

fun permutation(N: Int, f: (IntArray) -> Unit) {
  val A = IntArray(N){it}
  fun swap(i: Int, j: Int) {
    val tmp = A[i]
    A[i] = A[j]
    A[j] = tmp
  }

  fun step(n: Int) {
    when(n) {
      1 -> f(A)
      else -> {
        for (i in 0 until n) {
          step(n - 1)
          if (n % 2 == 0) {
            swap(i, n - 1)
          } else {
            swap(0, n - 1)
          }
        }
      }
    }
  }

  step(N)
}

/**
 * @param A 全部違う値が入ってることを期待する
 */
fun nextPermutation(A: IntArray): Boolean {
  fun swap(i: Int, j: Int) {
    val tmp = A[i]
    A[i] = A[j]
    A[j] = tmp
  }

  tailrec fun reverse(l: Int, r: Int) {
    if (r - l > 1) {
      swap(l, r - 1)
      reverse(l + 1, r - 1)
    }
  }

  tailrec fun binSearch(l: Int, r: Int, x: Int): Int {
    val med = (l + r) / 2
    return if (r - l == 1) l
    else if (A[med] > x) binSearch(med, r, x)
    else binSearch(l, med, x)
  }

  var i = A.size - 2
  while(i >= 0 && A[i] >= A[i + 1]) i--
  return if (i < 0)
    false
  else {
    val ix = binSearch(i + 1, A.size, A[i])
    swap(ix, i)
    reverse(i + 1, A.size)
    true
  }
}