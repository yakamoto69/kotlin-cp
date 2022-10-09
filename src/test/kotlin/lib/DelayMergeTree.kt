import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * https://algo-logic.info/segment-tree/#toc_id_3 <-ここがわかりやすい
 */
class DelayMergeTree(val n: Int, val A: LongArray) {
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
//  private val inf = 2e18.toLong()
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
//  private val zeroM = inf + 1
//  private inline fun contains(k: Int, x: Long): Boolean = value[k] < x

  /*
    (max, 入れ替え)
   */
//  private inline fun fx(x1: Long, x2: Long) = max(x1, x2)
//  private inline fun ap(x: Long, m: Long) = if (m == zeroM) x else m
//  private inline fun fm(m1: Long, m2: Long) = if (m2 == zeroM) m1 else m2
//  private inline fun fp(m: Long, n: Int) = m
//  private val zeroX = inf
//  private val zeroM = inf - 1
//  private inline fun contains(k: Int, x: Long): Boolean = value[k] > x

  /*
    (min, +)
  */
//  private inline fun fx(x1: Long, x2: Long) = min(x1, x2)
//  private inline fun ap(x: Long, m: Long) = x + m
//  private inline fun fm(m1: Long, m2: Long) = m1 + m2
//  private inline fun fp(m: Long, n: Int) = m
//  private val inf = 2e18.toLong() + 100
//  private val zeroX = inf
//  private val zeroM = 0L
//  private inline fun contains(k: Int, x: Long): Boolean = value[k] < x

  /*
    (max, +)
  */
//  private inline fun fx(x1: Long, x2: Long) = max(x1, x2)
//  private inline fun ap(x: Long, m: Long) = x + m
//  private inline fun fm(m1: Long, m2: Long) = m1 + m2
//  private inline fun fp(m: Long, n: Int) = m
//  private val inf = -2e18.toLong()
//  private val zeroX = inf
//  private val zeroM = 0L
//  private inline fun contains(k: Int, x: Long): Boolean = value[k] > x

  /*
    (xor, 入れ替え)
   */
//  private inline fun fx(x1: Long, x2: Long) = x1 xor x2
//  private inline fun ap(x: Long, m: Long) = if (m == zeroM) x else m
//  private inline fun fm(m1: Long, m2: Long) = if (m2 == zeroM) m1 else m2
//  private inline fun fp(m: Long, n: Int) = if (n%2 == 1) m else zeroX
//  private val zeroX = 0L
//  private val zeroM = -5e18.toLong()
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
  private val value = LongArray(N * 2) // initで初期化する
  private val delay = LongArray(N * 2){zeroM}

  private fun init() {
    for (k in n - 1 downTo 0) {
      value[N + k] = A[k]
    }
    for (k in N - 1 downTo 1) {
      value[k] = fx(value[2*k], value[2*k + 1])
    }
  }

  init {
    init()
  }

  fun clear() {
    init()
    delay.fill(zeroM)
  }

  private inline fun push(k: Int, len: Int) {
    if (delay[k] == zeroM) return
    if (k < N) {
      applyM(k*2, delay[k], len/2)
      applyM(k*2 + 1, delay[k], len/2)
      delay[k] = zeroM
    }
  }

  private inline fun applyM(k: Int, m: Long, len: Int) {
    value[k] = ap(value[k], fp(m, len))
    if (k < N) delay[k] = fm(delay[k], m)
  }

  /**
   * たどる道をルートに向かって更新する
   */
  private inline fun updateAll(k: Int) {
    for (j in 1 .. log) {
      val kk = k shr j
      value[kk] = fx(value[kk*2], value[kk*2 + 1])
    }
  }

  private inline fun updateAll(a: Int, b: Int) {
    var ka = (a + N) shr 1
    var kb = (b + N) shr 1
    for (i in 0 until log) {
      if (delay[ka] == zeroM) value[ka] = fx(value[ka*2], value[ka*2 + 1])
      if (ka != kb && delay[kb] == zeroM) value[kb] = fx(value[kb*2], value[kb*2 + 1])
      ka /= 2
      kb /= 2
    }
  }

  private inline fun pushAll(a: Int, b: Int) {
    val ka = a + N
    val kb = b + N
    var len = N
    for (j in log downTo 1) {
      push(ka shr j, len)
      if (ka shr j != kb shr j) push(kb shr j, len)
      len /= 2
    }
  }

  /**
   * [a, b)
   */
  fun apply(a: Int, b: Int, x: Long) {
    if (a == b) return

    pushAll(a, b - 1)

    var left = a + N
    var right = b - 1 + N
    var len = 1

    while(left <= right) {
      if ((left and 1) == 1) {
        applyM(left, x, len)
      }
      if ((right and 1) == 0) {
        applyM(right, x, len)
      }
      left = (left + 1) shr 1 // 右の子供なら右の親に移動
      right = (right - 1) shr 1 // 左の子供なら左の親に移動
      len *= 2
    }

    updateAll(a, b - 1)
  }

  /**
   * [a, b)
   */
  fun prod(a: Int, b: Int): Long {
    pushAll(a, b - 1)

    var resL: Long = zeroX
    var resR: Long = zeroX
    var left = a + N
    var right = b - 1 + N
    var len = 1

    while(left <= right) {
      if ((left and 1) == 1) {
        resL = fx(resL, value[left])
      }
      if ((right and 1) == 0) {
        resR = fx(value[right], resR)
      }
      left = (left + 1) shr 1 // 右の子供なら右の親に移動
      right = (right - 1) shr 1 // 左の子供なら左の親に移動
      len *= 2
    }

    return fx(resL, resR)
  }

  fun get(i: Int): Long {
    val k = i + N
    for (j in log downTo 1) {
      push(k shr j, 1 shl j)
    }
    return value[k]
  }

  /**
   * ap(i, x)
   */
  fun set(i: Int, x: Long) {
    val k = i + N
    for (j in log downTo 1) {
      push(k shr j, 1 shl j)
    }
    value[k] = ap(value[k], x)
    updateAll(k)
  }

  /**
   * @param x 左右どちらからか最初に contains になる場所を探す。containsはfxがmin/maxかで違う
   * @param fromR 右から探すか
   * @return 見つからなかったら -1
   */
  fun find(x: Long, k: Int = 1, l: Int = 0, r: Int = N, fromR: Boolean = true): Int {
    push(k, r - l)
    if (!contains(k, x)) return -1
    if (l + 1 == r) return l

    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    if (fromR) {
      val ans = find(x, rgt, m, r, fromR)
      if (ans != -1) return ans
      return find(x, lft, l, m, fromR)
    }
    else {
      val ans = find(x, lft, l, m, fromR)
      if (ans != -1) return ans
      return find(x, rgt, m, r, fromR)
    }
  }

  /**
   * @return 最初に sum(0, i)>=xになるindexを返す。0-indexed 見つからなかったら n
   * fxがsum かつ 値に負がないことが条件
   */
  fun lowerBound(x: Long): Int {
    var remain = x
    var k = 1
    var len = N
    push(1, N)
    if (value[1] < remain) return n

    for (i in 1 .. log) {
      push(k*2, len/2)
      if (value[k*2] < remain) {
        remain -= value[k*2]
        push(k*2 + 1, len/2)
        k = k*2 + 1
      }
      else {
        k *= 2
      }
//        debug{"k:$k $remain"}
      len /= 2
    }
    return min(k - N, n)
  }

  fun inspect() = run { Pair(value, delay) }
  override fun toString() = "[${(0 until N).map { get(it) }.joinToString(" ")}]"
}

object DelayMergeTreeTest {
  fun test() {

    val rand = Random()
    val MAX = 100

    for (n in 1 .. 16) {
      // DelayMergeの中と合わせる
//      fun ap(x: Long, m: Long) = x + m
//      fun fx(x1: Long, x2: Long) = x1 + x2

      // (+, +)
//      fun ap(x: Long, m: Long) = x + m
//      fun fx(x1: Long, x2: Long) = x1 + x2
//      val zeroX = 0L

      // (+, 入れ替え)
//      fun ap(x: Long, m: Long) = m
//      fun fx(x1: Long, x2: Long) = x1 + x2
//      val zeroX = 0L

      // (min, 入れ替え)
//      fun ap(x: Long, m: Long) = m
//      fun fx(x1: Long, x2: Long) = min(x1, x2)
//      val zeroX = 2e18.toLong() + 100

      // (max, +)
//      fun ap(x: Long, m: Long) = x + m
//      fun fx(x1: Long, x2: Long) = max(x1, x2)
//      val zeroX = -2e18.toLong()

      // (xor, 入れ替え)
      fun ap(x: Long, m: Long) = m
      fun fx(x1: Long, x2: Long) = x1 xor x2
      val zeroX = 0L

      val value = LongArray(n) { rand.nextInt(MAX).toLong() - MAX/2 }
      val t = DelayMergeTree(n, value)
      fun show() = "n=$n actual=[${
        (0 until n).map(t::get).joinToString(" ")
      }], expected=[${value.joinToString(" ")}] value=[${t.inspect().first.joinToString(" ")}], delay=[${t.inspect().second.joinToString(" ")}]"

      for (times in 0 until 1000) {
        when (rand.nextInt(4)) {
          // set
          0 -> {
            val i = rand.nextInt(n)
            val v = rand.nextInt(MAX).toLong()
            t.set(i, v)
//          t.apply(i, i + 1, v)
            value[i] = ap(value[i], v)
          }

          // get
          1 -> {
            val i = rand.nextInt(n)
            val actual = t.get(i)
            val expected = value[i]
            assert(actual == expected) {
              "get($i)={$actual, $expected}  ${show()}"
            }
          }

          // apply
          2 -> {
            val l = rand.nextInt(n)
            val r = l + rand.nextInt(n - l)
            val v = rand.nextInt(MAX).toLong()
            t.apply(l, r, v)
            (l until r).forEach { value[it] = ap(value[it], v) }
          }

          // prod
          3 -> {
            val l = rand.nextInt(n)
            val r = l + rand.nextInt(n - l)
            val expected = (l until r).map { value[it] }.fold(zeroX, ::fx)
            val actual = t.prod(l, r)
            assert(actual == expected) {
              "prod($l,$r)=($actual, $expected) ${show()}"
            }
          }

          // lowerBound
          4 -> {
            val x = rand.nextInt((value.sum() + 1).toInt()).toLong() // 0を防ぐ
            var i = 0
            var sum = 0L
            while (i < n && sum + value[i] < x) {
              sum += value[i++]
            }
            val actual = t.lowerBound(x)
            assert(actual == i) {
              "lowerBound($x)={$actual, $i} ${show()}"
            }
          }
        }
      }
    }
  }
}