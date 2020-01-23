import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val MOD = 1_000_000_007

class Solver(stream: InputStream, private val out: java.io.PrintWriter) {
  fun solve() {
    val N = ni()
    out.println(N)
  }













































  private val isDebug = try {
    // なんか本番でエラーでる
    System.getenv("MY_DEBUG") != null
  } catch (t: Throwable) {
    false
  }

  private var tokenizer: StringTokenizer? = null
  private val reader = BufferedReader(InputStreamReader(stream), 32768)
  private fun next(): String {
    while (tokenizer == null || !tokenizer!!.hasMoreTokens()) {
      tokenizer = StringTokenizer(reader.readLine())
    }
    return tokenizer!!.nextToken()
  }

  private fun ni() = next().toInt()
  private fun nl() = next().toLong()
  private fun ns() = next()
  private fun na(n: Int, offset: Int = 0): IntArray {
    return map(n, offset) { ni() }
  }

  private inline fun map(n: Int, offset: Int = 0, f: (Int) -> Int): IntArray {
    val res = IntArray(n)
    for (i in 0 until n) {
      res[i] = f(i + offset)
    }
    return res
  }

  private inline fun debug(msg: () -> String) {
    if (isDebug) System.err.println(msg())
  }

  private fun debug(a: LongArray) {
    debug { a.joinToString(" ") }
  }

  private fun debug(a: IntArray) {
    debug { a.joinToString(" ") }
  }

  private fun debug(a: BooleanArray) {
    debug { a.map { if (it) 1 else 0 }.joinToString("") }
  }

  private fun debugDim(A: Array<IntArray>) {
    if (isDebug) {
      for (a in A) {
        debug(a)
      }
    }
  }

  /**
   * 勝手にimport消されるのを防ぎたい
   */
  private fun hoge() {
    min(1, 2)
    max(1, 2)
    abs(-10)
  }
}

fun <A, B> pair(a: A, b: B) = RPair(a, b)
data class RPair<A, B>(val _1: A, val _2: B)

fun main() {
  val out = java.io.PrintWriter(System.out)
  Solver(System.`in`, out).solve()
  out.flush()
}