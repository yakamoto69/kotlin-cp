package lib

import java.util.*

/**
 * (dist, parent, queue)
 * @param rt ルートノードを指定する。nullの場合は全ノード辿るまで繰り返す
 */
fun traceBfs(g: Array<IntArray>, rt: Int? = 0): Array<IntArray> {
  val n = g.size
  val q = IntArray(n)
  val d = IntArray(n)
  val p = IntArray(n){-2}
  var h = 0
  var t = 0

  fun bfs(rt: Int) {
    p[rt] = -1
    q[t++] = rt
    d[rt] = 0
    while (h < t) {
      val v = q[h++]
      for (i in g[v].indices) { // List<Int>に変更したときこちらの方法でないとiterator生成して遅くなる
        val u = g[v][i]
        if (p[u] == -2) {
          p[u] = v
          q[t++] = u
          d[u] = d[v] + 1
        }
      }
    }
  }

  if (rt != null) {
    bfs(rt)
  } else {
    for (v in 0 until n) {
      if (p[v] != -2) continue
      bfs(v)
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

/**
 * よくある親の配列で木を初期化するやつ
 */
fun packTree(n: Int, P: IntArray): Array<IntArray> {
  val p = IntArray(n)
  val m = n - 1
  for (i in 0 until m) {
    ++p[P[i]]
    ++p[i + 1]
  }
  val g = Array(n){IntArray(p[it])}
  for (i in 0 until m) {
    g[i + 1][--p[i + 1]] = P[i]
    g[P[i]][--p[P[i]]] = i + 1
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
fun dijk2(g: Array<IntArray>, s: Int): LongArray {
  val n = g.size
  val D = LongArray(n){INF}
  D[s] = 0
  val visited = BooleanArray(n)
  val que = IntArray(n)
  var h = 0
  var t = 0
  que[h++] = s
  while(h > t) {
    val v = que[t++]
    if (visited[v]) continue
    visited[v] = true

    for (i in g[v].indices) {
      val u = g[v][i]
      if (D[v] + 1 < D[u]) {
        D[u] = D[v] + 1
        que[h++] = u
      }
    }
  }
  return D
}

fun eulerTreeTour(N: Int, g: Array<IntArray>, rt: Int): Pair<IntArray, IntArray> {
  val begin = IntArray(N)
  val end = IntArray(N)
  var id = 0
  val ix = IntArray(N)
  val que = IntArray(N)
  val par = IntArray(N)
  var t = -1
  que[++t] = rt
  par[rt] = -1
  while(t >= 0) {
    val v = que[t]
    val i = ix[v]++
    if (i == 0) {
      begin[v] = id++
    }
    if (i == g[v].size) {
      end[v] = id++
      t--
    } else {
      val u = g[v][i]
      if (par[v] == u) continue
      par[u] = v
      que[++t] = u
    }
  }

  return Pair(begin, end)
}
object cycle {
  /**
   * 無向グラフ用
   * @return 見つからなかったらnull
   * edgeで管理してるけど、dfsすれば必ず子->親のループになるので、そんなことせずにできる気がするけど
   */
  fun findCycle(n: Int, m: Int, from: IntArray, to: IntArray): List<Int>? {
    data class Edge(val i: Int, val v: Int) // 外に出せ
    val que = IntArray(n + 10)
    val nodeFlag = IntArray(n)
    val visitedEdge = BooleanArray(m)
    val g = Array(n){ mutableListOf<Edge>()}
    for (i in 0 until m) {
      g[from[i]].add(Edge(i, to[i]))
      g[to[i]].add(Edge(i, from[i]))
    }
    fun dfs(rt: Int): List<Int>?  {
      fun step(pos: Int, v: Int): List<Int>? {
        que[pos] = v
        nodeFlag[v] = 1
        for (i in g[v].indices) {
          val e = g[v][i]
          if (visitedEdge[e.i]) continue
          visitedEdge[e.i] = true
          when(nodeFlag[e.v]) {
            1 -> {
              val ix = que.indexOf(e.v)
              val ans = mutableListOf<Int>()
              for (j in ix..pos) {
                ans += que[j]
              }
              return ans
            }
            0 -> {
              val res = step(pos + 1, e.v)
              if (res != null) return res
            }
            else -> {}
          }
        }
        nodeFlag[v] = 2
        return null
      }

      return step(0, rt)
    }

    for (u in 0 until n) {
      if (nodeFlag[u] == 0) {
        val res = dfs(u)
        if (res != null) return res
      }
    }
    return null
  }

  /**
   * UGraphというかツリーだけど
   */
  fun findCyclesInUGraph(N: Int, P: IntArray) {
    val visited = IntArray(N)
    val cycles = mutableListOf<List<Int>>()
    val path = mutableListOf<Int>()
    fun dfs(v: Int) {
      visited[v] = 1
      path += v
      when (visited[P[v]]) {
        0 -> dfs(P[v])
        1 -> {
          val ix = path.indexOf(P[v])
          cycles += path.slice(ix..path.lastIndex)
        }
        else -> {
        }
      }
      path.removeAt(path.lastIndex)
      visited[v] = 2
    }
    dfs(0)
  }
}

fun shrinkCycle(N: Int, M: Int, from: IntArray, to: IntArray, cycle: List<Int>): List<Int> {
  val L = IntArray(N)
  val R = IntArray(N)
  for (i in 0 until cycle.size) {
    R[cycle[i]] = cycle[(i + 1) % cycle.size]
    L[cycle[i]] = cycle[(cycle.size + i - 1) % cycle.size]
  }
  var rt = -1
  val set = BooleanArray(N)
  for (u in cycle) {
    set[u] = true
    rt = u
  }
  var size = cycle.size
  for (i in 0 until M) {
    val u = from[i]
    val v = to[i]
    if (set[u] && set[v] && L[u] != v && R[u] != v) {
      while(size > 3 && R[u] != v) {
        size--
        set[R[u]] = false
        val k = R[R[u]]
        R[u] = k
        L[k] = u
      }
      rt = u
    }
  }
  val res = mutableListOf<Int>()
  res += rt
  var cur = R[rt]
  while(cur != rt) {
    res += cur
    cur = R[cur]
  }
  return res
}

/**
 * @return node->order　じゃなくて、nodeのリストを返す。
 */
fun topologicalSort(n: Int, g: Array<IntArray>): IntArray? {
  val res = IntArray(n){-1}
  var ptr = 0
  val que = ArrayDeque<Int>()
  val deg = IntArray(n)

  for (v in 0 until n) {
    for (o in g[v]) {
      deg[o]++
    }
  }

  for (v in 0 until n) {
    if (deg[v] == 0) que.add(v)
  }

  while(que.isNotEmpty()) {
    val v = que.poll()
    res[ptr++] = v
    for (i in g[v].indices) {
      val o = g[v][i]
      if (--deg[o] == 0) que.add(o)
    }
  }

  // 全部に順序がつかないとループがあったってこと
  // グラフにでてこないノードにも順序がつく
  return if (ptr < n) null else res
}

fun testBipartite(N: Int, g: Array<IntArray>, D: IntArray): Boolean {
  for (v in 0 until N) {
    for (i in g[v].indices) {
      val u = g[v][i]
      if (D[u] % 2 == D[v] % 2) {
        return false
      }
    }
  }

  return true
}

/**
 * @return (path, from, to)
 * ↑のeulerTreeTourと一緒だからまとめる
 */
fun buildEulerTourPathOfTree(N: Int, g: Array<IntArray>, par: IntArray, que: IntArray): Triple<IntArray, IntArray, IntArray> {
  val dp = IntArray(N)
  for (i in N - 1 downTo 0) {
    val v = que[i]
    dp[v] = 1
    for (j in g[v].indices) {
      val u = g[v][j]
      if (u == par[u]) continue
      dp[v] += dp[u]
    }
  }

  val path = IntArray(2 * N) { -1 }
  val from = IntArray(N)
  val to = IntArray(N)
  for (i in que.indices) {
    val v = que[i]
    path[from[v]] = v
    path[from[v] + 2 * dp[v] - 1] = v
    to[v] = from[v] + 2 * dp[v] - 1
    var add = 1
    for (j in g[v].indices) {
      val u = g[v][j]
      if (u == par[v]) continue
      from[u] = from[v] + add
      add += 2 * dp[u]
    }
  }
  return Triple(path, from, to)
}

/**
 * @return 無向グラフの直径
 */
fun diameter(g: Array<IntArray>): Int {
  val s = longest(g, 0).first
  return longest(g, s).second
}

/**
 * 無向グラフ対象で、sから一番離れているノードを探す
 * @return (v, dist)
 */
fun longest(g: Array<IntArray>, s: Int): Pair<Int, Int> {
  val (D, _) = traceBfs(g, s)
  val dist = D.max()!!
  return Pair(D.indexOf(dist), dist)
}

/**
 * s -> t の最小パス
 */
fun route(g: Array<IntArray>, s: Int, t: Int): List<Int> {
  val rt = mutableListOf<Int>()
  val (D, _, _) = traceBfs(g, t)
  var cur = s
  rt += s
  while(cur != t) {
    for (i in g[cur].indices) {
      val u = g[cur][i]
      if (D[u] == D[cur] - 1) {
        cur = u
        rt += u
        break
      }
    }
  }
  return rt
}