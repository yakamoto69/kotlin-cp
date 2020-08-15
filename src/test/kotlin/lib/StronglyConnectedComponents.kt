package lib

import java.util.*

// DFSするのでstackサイズを大きくすること
class StronglyConnectedComponents(val N: Int, from: IntArray, to: IntArray) {
  private var id = 0
  private val Ti = IntArray(N)
  private val visited = BooleanArray(N)
  private val g = packDGraph(N, from, to)
  private val gi = packDGraph(N, to, from)

  fun dfs(u: Int) {
    visited[u] = true
    for (v in g[u]) {
      if (visited[v]) continue
      dfs(v)
    }
    Ti[id++] = u
  }

  fun dfs2(u: Int, nodes: MutableList<Int>) {
    visited[u] = true
    for (v in gi[u]) {
      if (visited[v]) continue
      dfs2(v, nodes)
    }
    nodes += u
  }

  fun build(): List<List<Int>> {
    for (u in 0 until N) {
      if (!visited[u]) dfs(u)
    }

    Arrays.fill(visited, false)
    val res = mutableListOf<MutableList<Int>>()
    for (t in N - 1 downTo 0) {
      if (visited[Ti[t]]) continue
      val nodes = mutableListOf<Int>()
      dfs2(Ti[t], nodes)
      res += nodes
    }
    return res
  }
}