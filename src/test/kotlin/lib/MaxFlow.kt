package lib

import java.util.*
import kotlin.math.min

object MaxFlow {
  data class Edge(val u: Int, val v: Int, var cap: Int)
  data class RTEdge(val to: Int, var cap: Int, val rev: Int)
  fun maxFlow(n: Int, E: List<Edge>, s: Int, t: Int, inf: Int): Int {
    val level = IntArray(n)
    val iter = IntArray(n)
    val queue = IntArray(n)
    val g = Array(n) { mutableListOf<RTEdge>() }
    for (e in E) {
      g[e.u].add(RTEdge(e.v, e.cap, g[e.v].size))
      g[e.v].add(RTEdge(e.u, 0, g[e.u].size - 1))
    }

    fun bfs() {
      var head = 0
      var tail = 0
      level[s] = 0
      queue[head++] = s
      while (head > tail) {
        val u = queue[tail++]
        for (i in 0 until g[u].size) {
          val e = g[u][i]
          if (e.cap > 0 && level[e.to] < 0) {
            level[e.to] = level[u] + 1
            queue[head++] = e.to
          }
        }
      }
    }

    fun dfs(u: Int, f: Int): Int {
      if (u == t) return f
      while (iter[u] < g[u].size) {
        val e = g[u][iter[u]++]
        if (e.cap > 0 && level[u] < level[e.to]) {
          val gain = dfs(e.to, min(f, e.cap))
          if (gain > 0) {
            e.cap -= gain
            g[e.to][e.rev].cap += gain
            return gain
          }
        }
      }
      return 0
    }

    var flow = 0
    while (true) {
      Arrays.fill(iter, 0)
      Arrays.fill(level, -1)
      bfs()
      if (level[t] < 0) break
      while (true) {
        val f = dfs(s, inf)
        if (f <= 0) break
        flow = min(inf, flow + f)
      }
    }
    return if (flow == inf) -1 else flow
  }
}