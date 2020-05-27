package lib

fun factorize(a: Int): List<Pair<Int, Int>> {
  val res = mutableListOf<Pair<Int, Int>>()
  var x = a
  var i = 2 // 2L
  while(i * i <= x) {
    var cnt = 0
    while (x % i == 0) {
      cnt++
      x /= i
    }
    if (cnt > 0) res += Pair(i, cnt)
    i++
  }
  if (x > 1) res += Pair(x, 1)
  return res
}

class Factorize(N: Int) {
  val prime = IntArray(N)
  val factor = IntArray(N + 1)
  var p = 0

  init {
    for (i in 2..N) {
      if (factor[i] == 0) {
        factor[i] = i
        prime[p++] = i
      }

      var j = 0
      while((j < p) and (prime[j] * i <= N)) {
        factor[prime[j] * i] = prime[j]
//        if (prime[j] == i) break // いらなくない？
        j++
      }
    }
  }

  tailrec fun factorize(x: Int, fn: (Int) -> Unit) {
    if (x > 1) {
      val f = factor[x]
      fn(f)
      factorize(x / f, fn)
    }
  }
}