import java.util.*
import kotlin.math.min

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
private fun debug(msg: () -> String) {
  if (isDebug) System.err.println(msg())
}
private fun debug(a: LongArray) {
  if (isDebug) debug{a.joinToString(" ")}
}
private fun debug(a: IntArray) {
  if (isDebug) debug{a.joinToString(" ")}
}
private fun debugDim(A: Array<IntArray>) {
  if (isDebug) {
    for (a in A) {
      debug(a)
    }
  }
}