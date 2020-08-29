package lib

class UnionFind(n: Int) {
  private val par = IntArray(n){it}
  private val rank = IntArray(n){1} // 集合の要素数
  private val visits = IntArray(n) // 訪れた場所をfind毎に用意するのがもったいないのでつかいまわす

  fun find(x: Int): Int {
    var ptr = 0
    var i = x
    while(par[i] != i) {
      visits[ptr++] = i
      i = par[i]
    }

    for (j in 0 until ptr) {
      par[visits[j]] = i
    }
    return i
  }

  private fun merge(node: Int, rt: Int): Int {
    par[node] = rt
    rank[rt] += rank[node]
    return rt
  }

  fun unite(x: Int, y: Int): Boolean {
    val x1 = find(x)
    val y1 = find(y)
    return if (x1 == y1) false
    else {
      if (rank[x1] < rank[y1])
        merge(x1, y1)
      else
        merge(y1, x1)

      true
    }
  }

  fun isSame(x: Int, y: Int) = find(x) == find(y)

  fun isRoot(x: Int) = par[x] == x

  /**
    * xを解決する必要がないときは直にrankをみる
    */
  fun cntNodes(x: Int): Int = rank[find(x)]

  fun inspect() = run{par}
}