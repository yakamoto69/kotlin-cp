package lib

object Doubling {
  /**
   * 2^k回遷移したらどこにたどり着くかのテーブルを作る
   * @param next -1で終端を表現
   */
  fun build(N: Int, K: Int, next: IntArray): Array<IntArray> {
    val move = Array(K) { IntArray(N) }
    for (i in 0 until N) {
      move[0][i] = next[i]
    }
    for (k in 1 until K) {
      for (i in 0 until N) {
        if (move[k - 1][i] == -1) {
          move[k][i] = move[k - 1][i]
        } else {
          move[k][i] = move[k - 1][move[k - 1][i]]
        }
      }
    }
    return move
  }
}