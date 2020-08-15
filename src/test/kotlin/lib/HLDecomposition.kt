package lib

import kotlin.math.max
import kotlin.math.min

object HLDecomposition {
  data class Edge(val id: Int, val v: Int, val w: Int)
  data class HLCol(val id: Int, val ix: Int, val node: HLNode, val e: Edge?) // ツリーのノードと一対一
  data class HLNode(val id: Int, val depth: Int, val parNode: Int) {
    val cols = mutableListOf<HLCol>()
  }

  /**
   * @return nodes, nodeId, colId
   */
  fun buildHLTree(N: Int, t: Array<MutableList<Edge>>): Pair<List<HLNode>, Array<HLCol>> {
    val dp = IntArray(N) { 1 }
//    val (_, par, que) = traceBfs(t)
    val par = IntArray(N)
    val que = IntArray(N)
    for (i in N - 1 downTo 0) {
      val u = que[i]
      for (e in t[u]) {
        if (e.v == u) continue
        dp[u] += dp[e.v]
      }
    }

    val nodes = mutableListOf<HLNode>()
    val toNode = IntArray(N)
    val col = arrayOfNulls<HLCol>(N)

    nodes.add(HLNode(0, 0, -1))
    col[0] = HLCol(0, 0, nodes[0], null)
    nodes[0].cols.add(col[0]!!)
    for (i in 0 until N) {
      val u = que[i]

      var mxId = -1
      var mx = 0
      for (e in t[u]) {
        if (e.v == par[u]) continue
        if (dp[e.v] > mx) {
          mxId = e.v
          mx = dp[e.v]
        }
      }

      for (e in t[u]) {
        if (e.v == par[u]) continue
        if (e.v == mxId) {
          toNode[e.v] = toNode[u]
          val node = nodes[toNode[u]]
          col[e.v] = HLCol(e.v, node.cols.size, node, Edge(e.id, u, e.w))
          node.cols += col[e.v]!!
        } else {
          toNode[e.v] = nodes.size
          nodes.add(HLNode(toNode[e.v], nodes[toNode[u]].depth + 1, toNode[u]))
          val node = nodes[toNode[e.v]]
          col[e.v] = HLCol(e.v, 0, node, Edge(e.id, u, e.w))
          node.cols += col[e.v]!!
        }
      }
    }

    return Pair(nodes, col as Array<HLCol>)
  }

  fun usage(nodes: List<HLNode>, cols: Array<HLCol>) {
    val seg = Array(nodes.size) { SegmentTree(nodes[it].cols.size - 1, 0) { a, b -> max(a, b) } }
    for (node in nodes) {
      for (i in 1 until node.cols.size) {
        seg[node.id].update(i - 1, node.cols[i].e!!.w)
      }
    }

//    for (i in 0 until M) {
//      var mx = 0
//      var u = cols[E[i].u]
//      var v = cols[E[i].v]
//
//      fun ascend(col: HLCol): HLCol {
//        mx = max(mx, col.e!!.w)
//        return cols[col.e.v]
//      }
//
//      fun step(col: HLCol): HLCol {
//        if (col.ix == 0) {
//          return ascend(col)
//        } else {
//          val node = cols[col.e!!.v].node
//          mx = max(mx, seg[node.id].query(0, col.ix))
//          return ascend(node.cols[0])
//        }
//      }
//
//      while (u.node != v.node) {
//        if (u.node.depth > v.node.depth) {
//          u = step(u)
//        } else {
//          v = step(v)
//        }
//      }
//
//      if (u.ix != v.ix) {
//        mx = max(mx, seg[u.node.id].query(min(u.ix, v.ix), max(u.ix, v.ix)))
//      }
//    }
  }
}