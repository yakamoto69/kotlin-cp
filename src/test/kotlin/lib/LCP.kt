package lib

import kotlin.math.max
import kotlin.math.min

/**
 * (sa, rank)
 */
fun suffixArray(s: String): Pair<IntArray, IntArray> {
  val n = s.length
  val sa = Array(n + 1){it}
  val rank = IntArray(n + 1)

  for (i in 0 until n) {
    rank[i] = s[i].toInt()
  }
  rank[n] = -1

  val tmp = IntArray(n + 1)
  var len = 1
  fun cmp(i: Int, j: Int) = run {
    if (rank[i] != rank[j]) rank[i].compareTo(rank[j])
    else {
      // どちらかの長さが足りない場合は、文字が短い方が辞書順で先になる
      if (i + len >= n || j + len >= n) {
        if (i > j) -1 else 1
      } else {
        rank[i + len].compareTo(rank[j + len])
      }
    }
  }

  while(len <= n) {
    // 今のランクとダブリングをつかってlen*2までの長さで並び替える
    sa.sortWith(Comparator(::cmp))

    // 次のlen*2の長さで並び替えたsaからrankの値を計算する
    tmp[sa[0]] = 0
    for (i in 1..n) {
      tmp[sa[i]] = tmp[sa[i - 1]]
      if (cmp(sa[i - 1], sa[i]) < 0) tmp[sa[i]]++ // 前の順位のものより大きい場合は+1する
    }
    System.arraycopy(tmp, 0, rank, 0, n + 1)
    len *= 2
  }

  return Pair(sa.toIntArray(), rank)
}

/**
 * (lcp, sa, rank)
 * lcp: [0, lcp(rank0,rank1), lcp(rank1,rank2) .. lcp(rankN-1,rankN)]
 */
fun longestCommonPrefix(s: String): Triple<IntArray, IntArray, IntArray> {
  val n = s.length
  val lcp = IntArray(n + 1)

  val (sa, rank) = suffixArray(s)
  var h = 0
  for (i in 0 until n) {
    val j = sa[rank[i] - 1] // sa上で１個前
    h = max(0, h - 1)
    while(i + h < n && j + h < n && s[i + h] == s[j + h]) {
      h++
    }
    lcp[rank[i]] = h
  }

  return Triple(lcp, sa, rank)
}

class LCPTree(lcp: IntArray, private val rank: IntArray) {
  private val n = lcp.size - 1
  private val t = SegmentTree(n, Int.MAX_VALUE / 2){a, b -> min(a, b)}
  init {
    // t: [lcp(rank0,rank1), lcp(rank1,rank2) .. lcp(rankN-1,rankN)]
    for (i in 0 until n) {
      t.update(i, lcp[i + 1]) // indexが-1されてるのに注意
    }
  }

  fun commonPrefixLength(i: Int, j: Int): Int {
    val l = min(rank[i], rank[j])
    val r = max(rank[i], rank[j])
    return t.query(l, r)
  }

  /**
   * [lRank, rRank)の範囲で、最初に rank < len となるrankを探す
   * @return みつからなかったら -1
   */
  fun findFirstLt(len: Int, lRank: Int, rRank: Int): Int {
    val ix = t.find(len, lRank, rRank) // tree上でのindex
    return if (ix == -1) -1 else ix + 1 // rankにするには+1しないといけない
  }
}