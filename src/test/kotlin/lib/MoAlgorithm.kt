package lib

class MoAlgorithm(val Q: Int) {
  private val q = kotlin.math.sqrt(Q.toDouble()).toLong()
  // [l, r)
  inner class Query(val id: Int, val l: Int, val r: Int) : Comparable<Query>{
    override fun compareTo(o: Query): Int {
      if (l/q != o.l/q) return (l/q).compareTo(o.l/q)
      return r.compareTo(o.r)
    }

    override fun toString(): String {
      return "($l,$r)"
    }
  }

  fun exe(queries: Array<Query>) {
    queries.sort()

    var l = 0
    var r = 0 // exclusive
//    val cnt = TreeMap<Int, Long>() // 初期値は空
    var pairCnt = 0L
    fun add(i: Int) {
//      pairCnt += cnt.getOrDefault(cum[i] xor K, 0)
//      incr(cnt, cum[i])
    }

    fun del(i: Int) {
//      decr(cnt, cum[i])
//      pairCnt -= cnt.getOrDefault(cum[i] xor K, 0)
    }
//    add(r++) // 0追加
//    val ans = LongArray(Q)
    for (q in queries) {
      while (q.l < l) add(--l)
      while (q.r > r) add(r++)
      while (q.l > l) del(l++)
      while (q.r < r) del(--r)

//      ans[q.id] = pairCnt
    }
  }
}