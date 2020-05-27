package lib

import java.util.*

/**
 * (dist, parent, queue)
 */
fun traceBfs(g: Array<IntArray>, rt: Int = 0): Array<IntArray> {
  val n = g.size
  val q = IntArray(n)
  val d = IntArray(n)
  val p = IntArray(n){-2}
  var cur = 0
  var last = 1
  p[rt] = -1
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

fun packUGraph(n: Int, from: IntArray, to: IntArray): Array<IntArray> {
  val p = IntArray(n)
  val m = from.size
  for (i in 0 until m) {
    ++p[from[i]]
    ++p[to[i]]
  }
  val g = Array(n){IntArray(p[it])}
  for (i in 0 until m) {
    g[from[i]][--p[from[i]]] = to[i]
    g[to[i]][--p[to[i]]] = from[i]
  }
  return g
}

fun packDGraph(n: Int, from: IntArray, to: IntArray): Array<IntArray> {
  val p = IntArray(n)
  val m = from.size
  for (i in 0 until m) {
    ++p[from[i]]
  }
  val g = Array(n){IntArray(p[it])}
  for (i in 0 until m) {
    g[from[i]][--p[from[i]]] = to[i]
  }
  return g
}

data class Edge(val v: Int, val weight: Long)
data class Visit(val v: Int, val cost: Long)
val INF = 1e18.toLong()
fun dijk(g: Array<MutableList<Edge>>, s: Int): LongArray {
  val D = LongArray(g.size){INF}
  D[s] = 0
  val visited = BooleanArray(g.size)
  val que = PriorityQueue<Visit>(compareBy { it.cost })
  que.add(Visit(s, 0))
  while(que.isNotEmpty()) {
    val v = que.poll()!!
    if (visited[v.v]) continue
    visited[v.v] = true

    for (i in 0 until g[v.v].size) {
      val e = g[v.v][i]
      if (v.cost + e.weight < D[e.v]) {
        D[e.v] = v.cost + e.weight
        que.add(Visit(e.v, D[e.v]))
      }
    }
  }
  return D
}