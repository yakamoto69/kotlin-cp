package lib

import kotlin.math.min

/**
 * https://algo-logic.info/segment-tree/#toc_id_3 <-ここがわかりやすい
 */
class DelayMergeTree(val n: Int) {
  /*
    fx: x同士の結合
    fa: mをxに作用させる
    fm: 作用素を結合
    fp: (x1 + x2)*fp(m, n) == x1*fp(m, n/2) + x2*fp(m, n/2) <- fxが+だったらm*n

    (+, +)
    ↓の実装は区間sum、更新はaddの場合
   */
  private inline fun fx(x1: Long, x2: Long) = x1 + x2
  private inline fun ap(x: Long, m: Long) = x + m
  private inline fun fm(m1: Long, m2: Long) = m1 + m2
  private inline fun fp(m: Long, n: Int) = m*n
  private val zeroX = 0L
  private val zeroM = 0L
  private inline fun contains(k: Int, x: Long): Boolean = false

  /*
    (+, 入れ替え)
   */
//  private inline fun fx(x1: Long, x2: Long) = x1 + x2
//  private inline fun ap(x: Long, m: Long) = m
//  private inline fun fm(m1: Long, m2: Long) = m2
//  private inline fun fp(m: Long, n: Int) = m*n
//  private val inf = 2e18.toLong() + 100
//  private val zeroX = 0L
//  private val zeroM = -inf // 0だと0に更新を表現できない
//  private inline fun contains(k: Int, x: Long): Boolean = false

  /*
    (min, 入れ替え)
    こちらはmin、更新は入れ替えの場合
    更新のところにifいらないかも
   */
//  private inline fun fx(x1: Long, x2: Long) = min(x1, x2)
//  private inline fun ap(x: Long, m: Long) = if (m == zeroM) x else m
//  private inline fun fm(m1: Long, m2: Long) = if (m2 == zeroM) m1 else m2
//  private inline fun fp(m: Long, n: Int) = m
//  private val inf = 2e18.toLong() + 100
//  private val zeroX = inf
//  private val zeroM = inf
//  private inline fun contains(k: Int, x: Long): Boolean = value[k] < x

  /*
    (max, +)
  */
//  private inline fun fx(x1: Long, x2: Long) = max(x1, x2)
//  private inline fun ap(x: Long, m: Long) = x + m
//  private inline fun fm(m1: Long, m2: Long) = m1 + m2
//  private inline fun fp(m: Long, n: Int) = m
//  private val inf = 0L // マイナスありなら -(2e18.toLong + 100)
//  private val zeroX = inf
//  private val zeroM = inf
//  private inline fun contains(k: Int, x: Long): Boolean = value[k] > x

  /*
    (xor, 入れ替え)
   */
//  private inline fun fx(x1: Long, x2: Long) = x1 or x2
//  private inline fun ap(x: Long, m: Long) = m
//  private inline fun fm(m1: Long, m2: Long) = m2
//  private inline fun fp(m: Long, n: Int) = m
//  private val zeroX = 0L
//  private val zeroM = -1 // 0だと0に更新を表現できない
//  private inline fun contains(k: Int, x: Long): Boolean = false // min/maxじゃないと使えない

  private val log = log2_ceil(n)
  private val N = 1 shl log

  /**
   * 最初に x<=2^k になるkを返す
   */
  fun log2_ceil(x: Int): Int {
    var k = 0
    var pow2 = 1L
    while(pow2 < x) {
      pow2 *= 2
      k++
    }
    return k
  }

  // zeroX, zeroMよりも前にあると初期化できなくなるので注意
  private val value = LongArray(N * 2){zeroX}
  private val delay = LongArray(N * 2){zeroM}

  private inline fun push(k: Int, len: Int) {
    if (delay[k] == zeroM) return
    if (k < N) {
      delay[k * 2] = fm(delay[k * 2], delay[k])
      delay[k * 2 + 1] = fm(delay[k * 2 + 1], delay[k])
    }
    value[k] = ap(value[k], fp(delay[k], len))
    delay[k] = zeroM
  }

  private inline fun pushAll(a: Int, b: Int) {
    val ka = a + N
    val kb = b + N
    for (j in log downTo 1) {
      push(ka shr j, N shr j)
      push(kb shr j, N shr j)
    }
  }

  fun build(A: LongArray) {
    for (i in A.indices) {
      value[N + i] = A[i]
    }
    for (k in N - 1 downTo 0) {
      value[k] = fx(value[k*2], value[k*2 + 1])
    }
  }

  /**
   * [a, b)
   */
  fun apply(a: Int, b: Int, x: Long, k: Int = 1, l: Int = 0, r: Int = N) {
    push(k, r - l)
    if (a >= r || l >= b) return // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      delay[k] = fm(delay[k], x)
      push(k, r - l)
      return
    }

    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1
    apply(a, b, x, lft, l, m)
    apply(a, b, x, rgt, m, r)
    value[k] = fx(value[lft], value[rgt])
  }

  /**
   * [a, b)
   */
  fun prod(a: Int, b: Int, k: Int = 1, l: Int = 0, r: Int = N): Long {
    push(k, r - l)
    if (a >= r || l >= b) return zeroX // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      return value[k]
    }

    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    return fx(prod(a, b, lft, l, m), prod(a, b, rgt, m, r))
  }

  fun get(i: Int): Long {
    val k = i + N
    for (j in log downTo 0) {
      push(k shr j, 1 shl j)
    }
    return value[k]
  }

  // todo 要テスト
  fun set(i: Int, x: Long) {
    val k = i + N
    for (j in log downTo 0) {
      push(k shr j, 1 shl j)
    }
    value[k] = x
    for (j in 1 .. log) {
      val kk = k shr j
      value[kk] = fx(value[kk*2], value[kk*2 + 1])
    }
  }

  /**
   * @param x 左右どちらからか最初に contains になる場所を探す。containsはfxがmin/maxかで違う
   * @param fromR 右から探すか
   * @return 見つからなかったら -1
   */
  fun query(x: Long, k: Int = 1, l: Int = 0, r: Int = N, fromR: Boolean = true): Int {
    push(k, r - l)
    if (!contains(k, x)) return -1
    if (l + 1 == r) return l

    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    if (fromR) {
      val ans = query(x, rgt, m, r, fromR)
      if (ans != -1) return ans
      return query(x, lft, l, m, fromR)
    }
    else {
      val ans = query(x, lft, l, m, fromR)
      if (ans != -1) return ans
      return query(x, rgt, m, r, fromR)
    }
  }

  /**
   * @return 最初にvalue>=xになるindexを返す。0-indexed 見つからなかったら n
   * fxがsum かつ 値に負がないことが条件
   */
  fun lowerBound(x: Long): Int {
    var remain = x
    var k = 1
    var len = N
    var res = 0
    push(1, N)
    for (i in 1 .. log) {
      push(k*2, len/2)
      if (value[k*2] < remain) {
        remain -= value[k]
        res += 1 shl (log - i)
        k *= 2
      }
      else {
        push(k + 1, len/2)
        k = k*2 + 1
      }
      len /= 2
    }
    return min(n, res)
  }

  fun inspect() = run { Pair(value, delay) }
  override fun toString() = "[${(0 until N).map { get(it) }.joinToString(" ")}]"
}