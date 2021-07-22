package runner

import java.io.*
import java.lang.StringBuilder

/**
 * Solverと同じ階層においておくと、ファイルのアップロード失敗するかもしれないからrunnerパッケージに入れる
 */
fun main(arg: Array<String>) {
  val file = File(TestRunner::class.java.getResource("/${arg[0]}")!!.toURI())
  TestRunner().call(file)
}

private class TestRunner {
  enum class State {
    Default, In, Out
  }

  data class Test(val i: Int, val name: String) {
    val input = StringBuilder()
    val output = StringBuilder()
  }

  private fun <A> elapse(f: () -> A): Pair<A, Long> {
    val s = System.nanoTime()
    val res = f()
    val t = System.nanoTime()
    return Pair(res, (t - s) / 1_000_000) // ミリ秒
  }

  private val sin = System.`in`
  private val sout = System.out

  private fun <A> withStream(iStream: InputStream, oStream: PrintStream, f: () -> A): A {
    System.setIn(iStream)
    System.setOut(oStream)
    try {
      return f()
    } finally {
      System.setIn(sin)
      System.setOut(sout)
    }
  }

  private fun runTest(test: Test): Boolean {
    println("[${test.name}:${test.i}]")

    val out = ByteArrayOutputStream()
    val (_, time) = withStream(test.input.toString().byteInputStream(), PrintStream(out)) {
      elapse { Solver.main() }
    }
    val result = out.toString().trim()
    val expected = test.output.toString().trim()
    val ok = result == expected
    val msg = if (ok) "Success" else "Failure"
    println(result)
    println("$msg, ${time}ms")
    println()
    return ok
  }

  fun call(file: File) {
    var s: State = State.Default
    val tests = mutableListOf<Test>()
    val special = "\uD83D\uDE3A" // 😺

    var i = 0

    file.forEachLine {
      when (s) {
        State.Default ->
          when {
            it.startsWith("${special}in") -> {
              s = State.In
              tests.add(Test(i++, it.split(' ')[1]))
            }
            else -> {
            }
          }

        State.In -> {
          when (it) {
            "${special}out" -> {
              s = State.Out
            }
            else -> {
              tests.last().input.appendln(it)
            }
          }
        }

        State.Out -> {
          when (it) {
            "${special}end" -> {
              s = State.Default
            }
            else -> {
              tests.last().output.appendln(it)
            }
          }
        }
      }
    }

    if (s != State.Default) {
      println("ERROR テストファイルの形式がおかしいです。 finish at $s")
      return
    }

    if (tests.all(::runTest)) {
      println("!!!SUCCESS ALL!!!")
    } else {
      println("***FAILURE***")
    }
  }
}