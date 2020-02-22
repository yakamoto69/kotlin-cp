package lib

fun factorize(a: Int): MutableMap<Int, Int> {
  val res = mutableMapOf<Int, Int>()
  var x = a
  loop@while(x > 1) {
    var i = 2
    while(i * i <= x) {
      if (x % i == 0) {
        res.merge(i, 1, Int::plus)
        x /= i
        continue@loop
      }
      i++
    }
    res.merge(x, 1, Int::plus)
    x = 0
  }
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