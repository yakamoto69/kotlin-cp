import java.io.*
import TestRunner.State.*
import java.lang.StringBuilder

fun main(arg: Array<String>) {
  TestRunner().call(File(arg[0]))
}

private class TestRunner {
  enum class State {
    Default, In, Out
  }

  data class Test(val name: String) {
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
    println("[${test.name}]")

    val out = ByteArrayOutputStream()
    val (_, time) = withStream(test.input.toString().byteInputStream(), PrintStream(out)) {
      elapse { main() }
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
    var s: State = Default
    val tests = mutableListOf<Test>()
    val special = "\uD83D\uDE3A" // 😺

    file.forEachLine {
      when (s) {
        Default ->
          when {
            it.startsWith("${special}in") -> {
              s = In
              tests.add(Test(it.split(' ')[1]))
            }
            else -> {
            }
          }

        In -> {
          when (it) {
            "${special}out" -> {
              s = Out
            }
            else -> {
              tests.last().input.appendln(it)
            }
          }
        }

        Out -> {
          when (it) {
            "${special}end" -> {
              s = Default
            }
            else -> {
              tests.last().output.appendln(it)
            }
          }
        }
      }
    }

    if (s != Default) {
      println("ERROR テストファイルの形式がおかしいです。 finish at $s")
      return
    }

    val dupNames = tests
      .groupBy{it.name}
      .mapValues{it.value.size}
      .filter{it.value > 1}
      .map{it.key}

    if (dupNames.isNotEmpty()) {
      println("同じテスト名がありました。${dupNames.joinToString(" ")}")
      return
    }

    if (tests.all(::runTest)) {
      println("SUCCESS ALL!")
    } else {
      println("FAILURE!")
    }
  }
}