import kotlin.math.min
import kotlin.math.max
import kotlin.math.abs

val MOD = 1_000_000_007

fun main() {
  val (N) = readInts()
  println(N)
}

private val isDebug = try {
  // なんか本番でエラーでる
  System.getenv("MY_DEBUG") != null
} catch (t: Throwable) {
  false
}

private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }.toIntArray()
private fun readLongs() = readStrings().map { it.toLong() }.toLongArray()
private inline fun debug(msg: () -> String) {
  if (isDebug) System.err.println(msg())
}
private fun debug(a: LongArray) {
  debug{a.joinToString(" ")}
}
private fun debug(a: IntArray) {
  debug{a.joinToString(" ")}
}
private fun debug(a: BooleanArray) {
  debug{a.map{if(it) 1 else 0}.joinToString("")}
}
private fun debugDim(A: Array<IntArray>) {
  if (isDebug) {
    for (a in A) {
      debug(a)
    }
  }
}