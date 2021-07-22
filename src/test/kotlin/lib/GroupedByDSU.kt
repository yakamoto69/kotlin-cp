package lib

/**
 * 連結成分毎にsubgraphに分けて、subgraph上で番号を振り直す処理をまとめたもの
 */
class GroupedByDSU(N: Int,
                   M: Int,
                   private val from: IntArray,
                   private val to: IntArray)
{
  data class Group(val N: Int, val from: IntArray, val to: IntArray)

  private val mapping = IntArray(N) // zip_id -> node_id
  private val inv = IntArray(N) // node_id -> zip_id

  private val grp = IntArray(N)
  private val grpNodes = GroupedArray(N, N)
  private val grpEdges = GroupedArray(M, N)

  init {
    val uf = UnionFind(N)
    for (i in 0 until M) {
      uf.unite(from[i], to[i])
    }
    for (i in 0 until N) {
      grp[i] = uf.find(i)
      grpNodes.setGroup(i, grp[i])
    }
    for (i in 0 until M) {
      val g = grp[from[i]]
      grpEdges.setGroup(i, g)
    }
  }

  /**
   * @return (grp, zip_id -> node_id のmapping)
   */
  fun get(g: Int): Pair<Group, IntArray>? {
    val nodes = grpNodes.elements(g) ?: return null
    val edges = grpEdges.elements(g) ?: return null
    for (i in nodes.indices) {
      val v = nodes[i]
      mapping[i] = v
      inv[v] = i
    }

    val gFrom = IntArray(edges.size)
    val gTo = IntArray(edges.size)
    for (i in edges.indices) {
      gFrom[i] = inv[from[edges[i]]]
      gTo[i] = inv[to[edges[i]]]
    }
    val group = Group(nodes.size, gFrom, gTo)
    return Pair(group, mapping)
  }
}
