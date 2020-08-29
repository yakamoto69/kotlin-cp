package lib

fun factorize(a: Int): List<Pair<Int, Int>> {
  val res = mutableListOf<Pair<Int, Int>>()
  var x = a
  var i = 2 // 2L **Longに注意**
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

  /**
   * 因数分解の結果を p1*p1*p2 -> primes:[p1, p2] primeCnt:[2, 1], lst: 2
   * で受けとって、perm で使う因数の個数を管理しながら約数を生成する
   */
  private inline fun nextDivisor(primes: IntArray, primeCnt: IntArray, lst: Int, perm: IntArray): Int {
    var res = 1
    perm[0]++
    for (i in 0 until lst) {
      if (perm[i] > primeCnt[i]) {
        perm[i] = 0
        perm[i + 1]++
      }
      for (j in 0 until perm[i]) {
        res *= primes[i]
      }
    }
    return res
  }
}

fun Primes(MAX: Int) {
  val flg = BooleanArray(MAX + 1){true}
  val primes = mutableListOf<Int>()
  flg[0] = false
  flg[1] = false

  for (i in 2 .. MAX) {
    if (!flg[i]) continue
    primes += i

    var j = i * 2
    while (j <= MAX) {
      flg[j] = false
      j += i
    }
  }

  /**
   * MAX^2　までの数値を判定できる
   */
  fun isPrime(a: Int): Boolean {
    for (p in primes) {
      if (a <= p) return true
      if (a % p == 0) return false
    }
    return true
  }
}
