package lib

/**
 * グラフがエッジIDを持つ形のグラフ
 */
object WithEdgeIdGraph {
  data class Edge(val v: Int, val id: Int)

  fun packUGraph(n: Int, from: IntArray, to: IntArray): Array<Array<Edge>> {
    val p = IntArray(n)
    val m = from.size
    for (i in 0 until m) {
      ++p[from[i]]
      ++p[to[i]]
    }
    val g = Array(n) { arrayOfNulls<Edge>(p[it]) }
    for (i in 0 until m) {
      g[from[i]][--p[from[i]]] = Edge(to[i], i)
      g[to[i]][--p[to[i]]] = Edge(from[i], i)
    }
    return g as Array<Array<Edge>>
  }

  /**
   * (dist, parent, queue)
   * @param rt ルートノードを指定する。nullの場合は全ノード辿るまで繰り返す
   */
  fun traceBfs(g: Array<Array<Edge>>, rt: Int? = 0): Triple<IntArray, IntArray, Array<Edge>> {
    val n = g.size
    val q = arrayOfNulls<Edge>(n)
    val d = IntArray(n)
    val p = IntArray(n) { -2 }
    var h = 0
    var t = 0

    fun bfs(rt: Int) {
      p[rt] = -1
      q[t++] = Edge(rt, -1)
      d[rt] = 0
      while (h < t) {
        val u = q[h++]!!.v
        for (e in g[u]) {
          if (p[e.v] == -2) {
            p[e.v] = u
            q[t++] = e
            d[e.v] = d[u] + 1
          }
        }
      }
    }

    if (rt != null) {
      bfs(rt)
    } else {
      for (u in 0 until n) {
        if (p[u] != -2) continue
        bfs(u)
      }
    }
    return Triple(d, p, q as Array<Edge>)
  }
}