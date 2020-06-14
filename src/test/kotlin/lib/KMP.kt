package lib

class KMP<A>(val word: Array<A>) {
  private val kmp = IntArray(word.size + 1)
  init {
    for (i in 2 .. word.size) {
      kmp[i] = longestPrefix(kmp[i - 1], word[i - 1])
    }
  }

  fun findFirst(text: Array<A>, s: Int = 0): Int {
    var j = 0
    for (i in s until text.size) {
      j = longestPrefix(j, text[i])
      if (j == word.size) return i - word.size + 1
    }
    return -1
  }

  fun longestPrefix(matched: Int, a: A): Int {
    var j = matched
    while (true) {
      if (j < word.size && word[j] == a) {
        return j + 1
      } else if (j == 0) {
        return 0
      } else {
        j = kmp[j]
      }
    }
  }

  operator fun get(i: Int) = run{kmp[i]}
}