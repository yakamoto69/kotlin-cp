package lib.dp

import kotlin.math.max

class DigitDP(val N: Int, val MOD: Int, val S: IntArray) {
  val dp = Array(N){Array(2){LongArray(2){-1} } }
  fun get(i: Int, less: Int, leadingZero: Int): Long {
    if (i == N) return 1L
    if (dp[i][less][leadingZero] != -1L) return dp[i][less][leadingZero]

    var v = 0L
    val mx = if (less == 1) 9 else S[i]
    for (d in 0 .. mx) {
      val nless = less or (if (d < S[i]) 1 else 0)
      val nleadingZero = leadingZero and (if (d == 0) 1 else 0)
      val coef = if (leadingZero == 1 && i < N - 1) max(1, d) else d // 0回避
      v = (v + coef*get(i + 1, nless, nleadingZero)) % MOD
    }

    dp[i][less][leadingZero] = v
    return v
  }
}