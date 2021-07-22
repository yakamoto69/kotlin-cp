package lib

import java.util.*

class SlideKMax<A : Comparable<A>>(val k: Int) {
  data class Entry<A>(val i: Int, val v: A)

  private val queue = ArrayDeque<Entry<A>>() // 先頭にmaxがくる

  fun addAndMax(i: Int, v: A): A {
    // 後ろからいらないEntryを削除していく
    while(queue.isNotEmpty() && (queue.last().i <= i - k || queue.last().v <= v)) {
      queue.removeLast()
    }

    // 先頭からindexチェック
    while(queue.isNotEmpty() && queue.first().i <= i - k) {
      queue.removeFirst()
    }
    queue.addLast(Entry(i, v))

    return queue.first().v // 必ず１つはある
  }
}