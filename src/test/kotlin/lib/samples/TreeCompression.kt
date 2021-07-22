package lib.samples

import lib.buildEulerTourPathOfTree
import lib.LCA
import lib.traceBfs

object TreeCompression {
  /**
   * vs0を結ぶ最小の木の辺の長さ
   */
  fun usage(N: Int, g: Array<IntArray>, k: Int, vs0: IntArray): Int {
    val (dist, par, que) = traceBfs(g, 0)
    val lca = LCA(N, dist, par, 20)
    val (_, begin, _) = buildEulerTourPathOfTree(N, g, par, que)
    val vs = vs0.sortedBy { begin[it] }
    var len = 0
    for (i in 0 until k) {
      val v = vs[i]
      val u = vs[(i + 1) % k]
      val w = lca.lca(v, u)
      len += dist[v] + dist[u] - dist[w] * 2
    }
    return len / 2
  }
}