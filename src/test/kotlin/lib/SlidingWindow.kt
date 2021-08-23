package lib

class ArrayQueue<A>(val queue: Array<A>) {
  private var s = 0
  private var t = 0

  fun size() = t - s
  fun add(a: A) {
    queue[t++] = a
  }
  fun last() = queue[t - 1]
  fun first() = queue[s]
  fun popFirst(): A = queue[s++]
  fun popLast(): A = queue[--t]
  fun reset() {
    s = 0
    t = 0
  }

  companion object {
    inline fun <reified A>create(max: Int): ArrayQueue<A> {
      return ArrayQueue(arrayOfNulls<A>(max) as Array<A>)
    }
  }
}
class SlidingWindow<A>(max: Int, val cmp: Comparator<A>) {
  private val queue = ArrayQueue.create<IndexedValue<A>>(max) // 小さい順に並んでいる

  fun add(i: Int, a: A) {
    while(queue.size() >= 1 && cmp.compare(a, queue.last().value) < 0) {
      queue.popLast()
    }
    queue.add(IndexedValue(i, a))
  }

  fun popFirst() {
    queue.popFirst()
  }

  fun reset() {
    queue.reset()
  }

  fun size() = queue.size()
  fun first(): A = queue.first().value

  /**
   * @param leftMost windowの左端。これより小さいと削除される
   */
  fun check(leftMost: Int) {
    while(queue.size() > 0 && leftMost > queue.first().index) {
      queue.popFirst()
    }
  }
}