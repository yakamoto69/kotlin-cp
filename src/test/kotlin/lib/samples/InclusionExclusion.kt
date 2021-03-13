package lib.samples

import lib.powMod

object InclusionExclusion {
  /**
   * Nの内kを含む集合が、どれを選んでるというのに関係なく(N, k)だけで決まる場合
   * 2^Nやる代わりにコンビネーションですむ
   * inversionの計算のためMODは素数じゃないとだめ。inv_gcd使えば[2, N]までと素なら大丈夫
   */
  fun includeExclude(N: Int, MOD: Int, f: (Int) -> Long): Long {
    // 和集合を除いたものがほしいので、開始は k=0, sign=1
    var sign = 1
    var v = 0L
    var comb = 1L
    for (k in 0 .. N) {
      if (k > 0) {
        comb = comb *
            (N - (k - 1)) % MOD *
            powMod(k.toLong(), (MOD - 2).toLong(), MOD) % MOD
      }

      val amount = comb * f(k) % MOD
      val contribution = (MOD + amount*sign) % MOD
      v = (v + contribution) % MOD
      sign *= -1
    }

    return v
  }
}