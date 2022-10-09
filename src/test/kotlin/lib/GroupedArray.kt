package lib

/**
 * listをgroupに分けたいときに、Array(N){mutableList<Int>()}が重いので、軽い実装
 *
 * @param N 要素のサイズ
 * @param M グループのサイズ
 */
class GroupedArray(val N: Int, val M: Int) {
  private val rt = IntArray(M){-1}
  private val next = IntArray(N){-2}
  private val cnt = IntArray(M)

  fun setGroup(i: Int, g: Int) {
    next[i] = rt[g]
    rt[g] = i
    cnt[g]++
  }

  /**
   * setGroupを呼んだ順序
   */
  fun elements(g: Int): IntArray? {
    if (cnt[g] == 0) return null

    val res = IntArray(cnt[g])
    var cur = rt[g]
    for (i in cnt[g] - 1 downTo 0) {
      res[i] = cur
      cur = next[cur]
    }

    return res
  }
}