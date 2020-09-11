package lib

/**
 * 単調増加
 * (l, h] 方式
 * @return マッチするものがなかったらmx
 */
inline fun findMin(mn: Long, mx: Long, f: (Long) -> Boolean): Long {
  var low = mn - 1
  var high = mx
  while(high - low > 1) {
    val m = (low + high) / 2
    if (f(m)) high = m
    else low = m
  }
  return high
}

/**
 * 単調減少
 * [l, h) 方式
 * @return マッチするものがなかったらl
 */
inline fun findMax(l: Long, r: Long, f: (Long) -> Boolean): Long {
  var low = l
  var high = r + 1
  while(high - low > 1) {
    val m = (low + high) / 2
    if (f(m)) low = m
    else high = m
  }
  return low
}