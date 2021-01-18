package lib

import kotlin.math.min

class LCA(N: Int, val dist: IntArray, par: IntArray, val K: Int) {
  private val anc = Array(K){IntArray(N)}
  init {
    for (u in 0 until N) {
      anc[0][u] = if (dist[u] == 0) u else par[u]
    }
    for (k in 1 until K) {
      for (u in 0 until N) {
        anc[k][u] = anc[k - 1][anc[k - 1][u]]
      }
    }
  }

  tailrec fun up(u: Int, d: Int, k: Int): Int {
    if (d == 0) return u
    else if (d % 2 == 1) return up(anc[k][u], d / 2, k + 1)
    else return up(u, d / 2, k + 1)
  }

  fun lca(uu: Int, vv: Int): Int {
    val d = min(dist[uu], dist[vv])
    var u = up(uu, dist[uu] - d , 0)
    var v = up(vv, dist[vv] - d, 0)
    if (u == v) return u
    for (k in K - 1 downTo 0) {
      if (anc[k][u] != anc[k][v]) {
        u = anc[k][u]
        v = anc[k][v]
      }
    }
    return anc[0][u]
  }

  /**
   * (dist, lca)
   */
  fun distance(v: Int, u: Int): Pair<Int, Int> {
    val w = lca(v, u)
    return Pair(dist[v] + dist[u] - dist[w]*2, w)
  }
}