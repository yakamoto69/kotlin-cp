package lib

fun divisors(x: Int): MutableList<Int> {
    val res = mutableListOf<Int>()
    var d = 1
    while (d * d <= x) {
        if (x % d == 0) {
            res += d
            if (d * d != x) res += x / d
        }
        ++d
    }
    return res
}

fun powMod(a: Int, n: Long, mod: Int): Long {
    if (n == 0L) return 1
    val res = powMod((a.toLong() * a % mod).toInt(), n / 2, mod)
    return if (n % 2 == 1L) res * a % mod else res
}