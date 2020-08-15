package lib

import java.util.*

/**
 * JavaのTreeMap, TreeSetでsizeを呼ぶと遅いので、cntするクラスを用意した
 * queueの管理が面倒な実装問題で使おう
 * put, removeのタイミングで情報更新したいときはフィールドを増やそう
 */
private object MyTreeClasses {
  data class Entry(val v: Int)

  class MyTreeMap {
    private val t = TreeMap<Int, Entry>()
    var size = 0
    var sum = 0L

    fun remove(i: Int) = run {
      val s = t.remove(i)
      if (s != null) {
        size--
        sum -= s.v
      }
      s
    }

    fun put(s: Entry) = run {
      t[s.v] = s
      size++
      sum += s.v
    }

    fun firstEntry() = run { t.firstEntry() }
    fun lastEntry() = run { t.lastEntry() }
    fun firstKey() = run { t.firstKey() }
    fun lastKey() = run { t.lastKey() }

    fun pollFirstEntry() = run {
      val s = t.firstEntry()
      if (s != null) remove(s.key)
      s
    }
    fun pollLastEntry() = run {
      val s = t.lastEntry()
      if (s != null) remove(s.key)
      s
    }

    override fun toString(): String {
      return "$t $size $sum"
    }
  }

  class MyTreeSet {
    private val set = TreeSet<Entry>()
    var size = 0
    var sum = 0L

    fun remove(s: Entry) = run {
      val res = set.remove(s)
      if (res) {
        size--
        sum -= s.v
      }
      res
    }

    fun put(s: Entry) = run {
      set += s
      size++
      sum += s.v
    }

    fun first() = run { set.first() }
    fun last() = run { set.last() }

    fun pollFirst() = run {
      val s = set.first()
      if (s != null) remove(s)
      s
    }
    fun pollLastEntry() = run {
      val s = set.last()
      if (s != null) remove(s)
      s
    }

    override fun toString(): String {
      return "$set $size $sum"
    }
  }
}