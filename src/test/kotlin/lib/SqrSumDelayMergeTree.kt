package lib

class SqrSumDelayMergeTree(n: Int, s: LongArray, s2: LongArray) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val value = LongArray(N * 2)
  private val value2 = LongArray(N * 2)
  private val delay = LongArray(N * 2)
  private val elm = IntArray(N * 2)

  init {
    elm[1] = N
    for (i in 2 until N * 2) {
      elm[i] = elm[i / 2] / 2
    }

    for (i in 0 until n) {
      value[N + i] = s[i]
      value2[N + i] = s2[i]
    }
    for (i in N - 1 downTo 1) {
      value[i] = value[i * 2] + value[i * 2 + 1]
      value2[i] = value2[i * 2] + value2[i * 2 + 1]
    }
  }

  private fun push(k: Int) {
    if (k < N) {
      val x = delay[k]
      value2[k * 2] += x * 2 * value[k * 2] + x * x * elm[k * 2]
      value2[k * 2 + 1] += x * 2 * value[k * 2 + 1] + x * x * elm[k * 2 + 1]

      value[k * 2] += x * elm[k * 2]
      value[k * 2 + 1] += x * elm[k * 2 + 1]

      delay[k * 2] += x
      delay[k * 2 + 1] += x
      delay[k] = 0
    }
  }

  /**
   * [a, b)
   */
  fun add(a: Int, b: Int, x: Long, k: Int = 1, l: Int = 0, r: Int = N) {
    if (a >= r || l >= b) return // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      value2[k] += elm[k] * x * x + 2 * value[k] * x
      value[k] += x * elm[k]
      delay[k] += x
      return
    }

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1
    add(a, b, x, lft, l, m)
    add(a, b, x, rgt, m, r)
    value[k] = value[lft] + value[rgt]
    value2[k] = value2[lft] + value2[rgt]
  }

  fun query(a: Int, b: Int, k: Int = 1, l: Int = 0, r: Int = N): Long {
    if (a >= r || l >= b) return 0// ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      return value2[k]
    }
    if (l + 1 == r) return value2[l]

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    return query(a, b, rgt, m, r) + query(a, b, lft, l, m)
  }
}