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
    val u = que[t++]
    if (visited[u]) continue
    visited[u] = true

    for (v in g[u]) {
      if (D[u] + 1 < D[v]) {
        D[v] = D[u] + 1
        que[h++] = v
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
    val u = que[t]
    val i = ix[u]++
    if (i == 0) {
      begin[u] = id++
    }
    if (i == g[u].size) {
      end[u] = id++
      t--
    } else {
      val v = g[u][i]
      if (par[u] == v) continue
      par[v] = u
      que[++t] = v
    }
  }

  return Pair(begin, end)
}

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
    fun step(i: Int, u: Int): List<Int>? {
      que[i] = u
      nodeFlag[u] = 1
      for (e in g[u]) {
        if (visitedEdge[e.i]) continue
        visitedEdge[e.i] = true
        when(nodeFlag[e.v]) {
          1 -> {
            val ix = que.indexOf(e.v)
            val ans = mutableListOf<Int>()
            for (j in ix..i) {
              ans += que[j]
            }
            return ans
          }
          0 -> {
            val res = step(i + 1, e.v)
            if (res != null) return res
          }
          else -> {}
        }
      }
      nodeFlag[u] = 2
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

fun topologicalSort(n: Int, g: Array<IntArray>): IntArray? {
  val res = IntArray(n)
  var ptr = 0
  val que = ArrayDeque<Int>()
  val deg = IntArray(n)

  for (u in 0 until n) {
    for (v in g[u]) {
      deg[v]++
    }
  }

  for (u in 0 until n) {
    if (deg[u] == 0) que.add(u)
  }

  while(que.isNotEmpty()) {
    val u = que.poll()
    res[ptr++] = u
    for (v in g[u]) {
      if (--deg[v] == 0) que.add(v)
    }
  }

  return if (ptr < n) null else res
}