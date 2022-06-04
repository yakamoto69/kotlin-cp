package lib

import java.util.*

/**
 * BitSetをケチるためNode上のBitSetを使い回す
 * そのため単純にfxで新しいBitSetを返せないので、計算結果をどこにおくのかを指定する
 */
class BitsetSegmentTree(val n: Int, val bitCnt: Int) {

  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private inline fun fxL(x: BitSet, a: BitSet) = x.or(a)
  private inline fun fxR(a: BitSet, x: BitSet) = x.or(a)
  /**
   * dstとa, bは違うBitSetでないといけない
   */
  private inline fun fx(a: BitSet, b: BitSet, dst: BitSet){
    // dst.clear() orする一方なので必要ない
    dst.or(a)
    dst.or(b)
  }
  private inline fun ap(x: BitSet, m: BitSet) = x.or(m)
  private inline fun zero() = BitSet(bitCnt)

  private val dat = Array(2 * N) { zero() }

  fun update(i: Int, a: BitSet) {
    var ix = i + N
    ap(dat[ix], a)
    ix = ix shr 1
    while (ix >= 1) {
      fx(dat[ix * 2], dat[ix * 2 + 1], dat[ix])
      ix = ix shr 1
    }
  }

  /**
   * [a, b)
   */
  fun query(a: Int, b: Int): BitSet {
    val resL: BitSet = zero()
    val resR: BitSet = zero()
    var left = a + N
    var right = b - 1 + N

    while (left <= right) {
      if ((left and 1) == 1) fxL(resL, dat[left])
      if ((right and 1) == 0) fxR(dat[right], resR)
      left = (left + 1) shr 1 // 右の子供なら右の親に移動
      right = (right - 1) shr 1 // 左の子供なら左の親に移動
    }

    fxL(resL, resR)
    return resL
  }

  fun get(i: Int) = dat[N + i]

  override fun toString() = "[${(0 until n).map { get(it) }.joinToString(" ")}]"
}