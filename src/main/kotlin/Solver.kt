import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.AssertionError
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val MOD = 1_000_000_007

class Solver(stream: InputStream, private val out: java.io.PrintWriter) {
  private val reader = BufferedReader(InputStreamReader(stream), 32768)

  private val N = ni()
  fun solve() {
    out.println(N)
  }













































  private val isDebug = try {
    // なんか本番でエラーでる
    System.getenv("MY_DEBUG") != null
  } catch (t: Throwable) {
    false
  }

  private var tokenizer: StringTokenizer? = null
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
    return IntArray(n) { ni() + offset }
  }
  private fun nal(n: Int, offset: Int = 0): LongArray {
    val res = LongArray(n)
    for (i in 0 until n) {
      res[i] = nl() + offset
    }
    return res
  }

  private fun na2(n: Int, offset: Int = 0): Array<IntArray> {
    val a  = Array(2){IntArray(n)}
    for (i in 0 until n) {
      for (e in a) {
        e[i] = ni() + offset
      }
    }
    return a
  }

  private inline fun debug(msg: () -> String) {
    if (isDebug) System.err.println(msg())
  }

  /**
   * コーナーケースでエラー出たりするので、debug(dp[1])のように添え字付きの場合はdebug{}をつかうこと
   */
  private inline fun debug(a: LongArray) {
    debug { a.joinToString(" ") }
  }

  private inline fun debug(a: IntArray) {
    debug { a.joinToString(" ") }
  }

  private inline fun debug(a: BooleanArray) {
    debug { toString(a) }
  }

  private inline fun toString(a: BooleanArray) = run{a.map { if (it) 1 else 0 }.joinToString("")}

  private inline fun debugDim(A: Array<LongArray>) {
    if (isDebug) {
      for (a in A) {
        debug(a)
      }
    }
  }
  private inline fun debugDim(A: Array<IntArray>) {
    if (isDebug) {
      for (a in A) {
        debug(a)
      }
    }
  }
  private inline fun debugDim(A: Array<BooleanArray>) {
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

  private inline fun assert(b: Boolean) = run{if (!b) throw AssertionError()}
  private inline fun assert(b: Boolean, f: () -> String) = run{if (!b) throw AssertionError(f())}
}

fun main() {
  val out = java.io.PrintWriter(System.out)
  Solver(System.`in`, out).solve()
  out.flush()
}