package lib

data class DimensionalValue(val v: IntArray) : Comparable<DimensionalValue> {
  override fun compareTo(other: DimensionalValue): Int {
    for (i in v.indices) {
      if (v[i] != other.v[i]) return v[i].compareTo(other.v[i])
    }
    return 0
  }

  fun set(other: DimensionalValue) {
    for (i in v.indices) {
      v[i] = other.v[i]
    }
  }

  /**
   * 重いかも。特に配列を引数で直渡しするとコピー取られるので注意
   */
  fun set(vararg v: Int) {
    for (i in this.v.indices) {
      this.v[i] = v[i]
    }
  }

}