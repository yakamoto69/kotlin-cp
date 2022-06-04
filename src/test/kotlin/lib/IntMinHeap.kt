package lib

/**
 * n: 最大の要素数
 */
class IntMinHeap(n: Int) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1
  private val heap = IntArray(N + 1) // 1-indexed
  private var p = 0

  private inline fun up(i0: Int) {
    var i = i0
    while(i >= 2 && heap[i] < heap[i/2]) {
      val tmp = heap[i/2]
      heap[i/2] = heap[i]
      heap[i] = tmp
      i /= 2
    }
  }

  private inline fun minchild(i: Int): Int {
    return if (i * 2 + 1 > p) {
      i * 2
    } else {
      if (heap[i * 2] < heap[i * 2 + 1]) i * 2
      else i * 2 + 1
    }
  }

  // todo whileにする
  private fun down(i: Int) {
    if (i * 2 <= p) {
      val mc = minchild(i)
      if (heap[i] > heap[mc]) {
        val tmp = heap[mc]
        heap[mc] = heap[i]
        heap[i] = tmp
        down(mc)
      }
    }
  }

  fun add(x: Int) {
    heap[++p] = x
    up(p)
  }

  fun remove() {
    heap[1] = heap[p--]
    down(1)
  }

  fun min() = heap[1]
  fun size(): Int = p
  fun toArray() = heap.slice(1 .. p + 1)
  fun isEmpty() = p == 0
  fun isNotEmpty() = p > 0
}