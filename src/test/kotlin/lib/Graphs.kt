package lib

/**
 * (dist, parent, queue)
 */
fun traceBfs(g: Array<MutableList<Int>>, rt: Int = 0): Array<IntArray> {
  val n = g.size
  val q = IntArray(n)
  val d = IntArray(n)
  val p = IntArray(n){-2}
  var cur = 0
  var last = 1
  p[0] = -1
  q[0] = rt
  d[rt] = 0
  while (cur < last) {
    val v = q[cur++]
    for (u in g[v]) {
      if (p[u] == -2) {
        p[u] = v
        q[last++] = u
        d[u] = d[v] + 1
      }
    }
  }
  return arrayOf(d, p, q)
}