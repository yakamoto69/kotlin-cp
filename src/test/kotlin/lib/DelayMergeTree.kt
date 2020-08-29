package lib

import kotlin.math.min

class DelayMergeTree(n: Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val value = IntArray(N * 2)
  private val delay = IntArray(N * 2)

  private inline fun push(k: Int) {
    if (k < N && delay[k] != 0) {
      value[k * 2] += delay[k]
      value[k * 2 + 1] += delay[k]
      delay[k * 2] += delay[k]
      delay[k * 2 + 1] += delay[k]
      delay[k] = 0
    }
  }

  // ここをmax, minにする
  private inline fun merge(a: Int, b: Int): Int = min(a, b)

  // 条件を満たすか。maxなら '>' minなら '<' が使える
  private inline fun contains(a: Int, x: Int): Boolean = a < x

  /**
   * [a, b)
   */
  fun add(a: Int, b: Int, x: Int, k: Int = 1, l: Int = 0, r: Int = N) {
    if (a >= r || l >= b) return // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      value[k] += x
      delay[k] += x
      return
    }

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1
    add(a, b, x, lft, l, m)
    add(a, b, x, rgt, m, r)
    value[k] = merge(value[lft], value[rgt])
  }

  /**
   * @param x 左右どちらからか最初に a > x になる場所を探す
   * @return -1
   */
  fun query(x: Int, k: Int = 1, l: Int = 0, r: Int = N): Int {
    if (!contains(value[k], x)) return -1
    if (l + 1 == r) return r // 1-indexed

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    // 右から探す。左から探したい場合は左右を逆にすればいい
    val ans = query(x, rgt, m, r)
    if (ans != -1) return ans
    return query(x, lft, l, m)
  }

  fun eval(i: Int): Int {
    _eval(N + i)
    return value[N + i]
  }

  fun _eval(k: Int) {
    if (k > 1) {
      _eval(k / 2)
    }
    push(k)
  }
}