package lib

/**
 * @param merge ツリーを↑に上がっていくときのマージ関数
 * @param plus addでノードを更新する関数 (昔の値, 要素数, 引数)
 */
class DelayMergeTree2(n: Int,
                      val mZero: Long, val merge: (Long, Long) -> Long,
                      val pZero: Long, val plus: (Long, Int, Long) -> Long) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val value = LongArray(N * 2){mZero}
  private val delay = LongArray(N * 2){pZero}
  private val elms = IntArray(N * 2)
  init {
    elms[1] = N
    for (i in 1 until N) {
      elms[2 * i] = elms[i] / 2
      elms[2 * i + 1] = elms[i] / 2
    }
  }

  private fun push(k: Int) {
    if (k < N && delay[k] != pZero) {
      value[k * 2] = plus(value[k * 2], elms[k * 2], delay[k])
      value[k * 2 + 1] = plus(value[k * 2 + 1], elms[k * 2 + 1], delay[k])
      delay[k * 2] = plus(delay[k * 2], elms[k * 2], delay[k])
      delay[k * 2 + 1] = plus(delay[k * 2 + 1], elms[k * 2 + 1], delay[k])
      delay[k] = pZero
    }
  }

  /**
   * [a, b)
   */
  fun add(a: Int, b: Int, x: Long, k: Int = 1, l: Int = 0, r: Int = N) {
    if (a >= r || l >= b) return // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      value[k] = plus(value[k], elms[k], x)
      delay[k] = plus(delay[k], elms[k], x)
      return
    }

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1
    add(a, b, x, lft, l, m)
    add(a, b, x, rgt, m, r)
    value[k] = plus(value[k], elms[k], merge(value[lft], value[rgt]))
  }

  /**
   * [a, b)
   */
  fun query(a: Int, b: Int, k: Int = 1, l: Int = 0, r: Int = N): Long {
    if (a >= r || l >= b) return mZero // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      return value[k]
    }

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    return merge(query(a, b, lft, l, m), query(a, b, rgt, m, r))
  }

  fun eval(i: Int): Long {
    _eval(N + i)
    return value[N + i]
  }

  fun inspect() = run { Pair(value, delay) }

  fun _eval(k: Int) {
    if (k > 1) {
      _eval(k / 2)
    }
    push(k)
  }
}