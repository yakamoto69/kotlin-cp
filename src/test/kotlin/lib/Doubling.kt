package lib

object Doubling {
  /**
   * 2^k回遷移したらどこにたどり着くかのテーブルを作る
   */
  fun build(N: Int, K: Int, next: IntArray, inf: Int): Array<IntArray> {
    val move = Array(K) { IntArray(N) }
    for (i in 0 until N) {
      move[0][i] = next[i]
    }
    for (k in 1 until K) {
      for (i in 0 until N) {
        if (move[k - 1][i] == inf) {
          move[k][i] = move[k - 1][i]
        } else {
          move[k][i] = move[k - 1][move[k - 1][i]]
        }
      }
    }
    return move
  }

  /**
   * sから初めて、t以上になるには最低何回移動しないといけないか
   */
  fun usage(N: Int, K: Int, table: Array<IntArray>, s: Int, t: Int, inf: Int) {
    var i = s
    var ans = 0
    for (k in K - 1 downTo 0) {
      if (i != inf && table[k][i] < t) {
        i = table[k][i]
        ans = ans or (1 shl k)
      }
    }
    ans++ // rの１こ手前になる
  }
}