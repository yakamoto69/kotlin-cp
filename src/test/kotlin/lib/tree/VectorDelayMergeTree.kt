package lib.tree

typealias Mat = Array<DoubleArray>

private inline fun vec(x: Double, y: Double) =
  arrayOf(
    doubleArrayOf(x),
    doubleArrayOf(y)
  )

private inline fun I(n: Int) =
  Array(n) {
    val res = DoubleArray(n)
    res[it] = 1.0
    res
  }

private inline fun reset(m: Mat) {
  for (i in m.indices) {
    for (j in m[0].indices) {
      m[i][j] = 0.0
    }
  }
}

private inline fun resetI(m: Mat) {
  for (i in m.indices) {
    for (j in m.indices) {
      m[i][j] = if (i == j) 1.0 else 0.0
    }
  }
}

private val tmp = I(2)
/**
 * c = a * b
 * 計算途中でaを書き換えるかもしれないのでtmpに一旦移す
 */
private inline fun mulMat(a: Mat, b: Mat, c: Mat) {
  val n = a.size
  val m = b[0].size
  for (i in 0 until n) {
    for (j in 0 until m) {
      var v = 0.0
      for (k in a[0].indices) {
        v += a[i][k] * b[k][j]
      }
      tmp[i][j] = v
    }
  }
  for (i in 0 until n) {
    for (j in 0 until m) {
      c[i][j] = tmp[i][j]
    }
  }
}

/**
 * a += b
 */
private inline fun plusMat(a: Mat, b: Mat) {
  for (i in a.indices) {
    for (j in a[0].indices) {
      a[i][j] += b[i][j]
    }
  }
}


/**
 * vectorを合成、範囲のvectorにmatrixで変更を加える
 * matrixの計算は左右交換するとおかしくなるので、回転、拡大のような順序関係ない操作だけ行える
 */
private class VectorDelayMergeTree(n: Int, init: Array<Mat>) {
  private val N =
    if (Integer.highestOneBit(n) == n) n
    else Integer.highestOneBit(n) shl 1

  private val value = Array(N * 2){vec(0.0, 0.0)} // vector
  private val delay = Array(N * 2){I(2)} // 遅延したmatrix演算
  private val delayOn = BooleanArray(N * 2) // delayに値があるかどうか

  init {
    for (i in 0 until n) {
      value[N + i] = init[i]
    }
    for (i in N - 1 downTo 1) {
      plusMat(value[i], value[i*2])
      plusMat(value[i], value[i*2 + 1])
    }
  }

  private inline fun applyOperation(k: Int, x: Mat) {
    mulMat(x, value[k], value[k]) // valueはvectorなので MVの形になるように計算する
    mulMat(x, delay[k], delay[k])
    delayOn[k] = true
  }

  private inline fun push(k: Int) {
    if (k < N && delayOn[k]) {
      applyOperation(k*2, delay[k])
      applyOperation(k*2 + 1, delay[k])
      resetI(delay[k])
      delayOn[k] = false
    }
  }

  /**
   * [a, b)
   */
  fun add(a: Int, b: Int, x: Mat, k: Int = 1, l: Int = 0, r: Int = N) {
    if (a >= r || l >= b) return // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      applyOperation(k, x)
      return
    }

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    // value[k] = add(left) + add(right)
    add(a, b, x, lft, l, m)
    add(a, b, x, rgt, m, r)
    reset(value[k])
    plusMat(value[k], value[lft])
    plusMat(value[k], value[rgt])
  }

  /**
   * [a, b)
   */
  fun query(a: Int, b: Int, dest: Mat, k: Int = 1, l: Int = 0, r: Int = N) {
    if (a >= r || l >= b) return // ノードが範囲からはずれてる
    if (a <= l && r <= b) { // ノードが完全に範囲に含まれる
      plusMat(dest, value[k])
      return
    }

    push(k)
    val m = (l + r) / 2
    val lft = k * 2
    val rgt = lft + 1

    query(a, b, dest, lft, l, m)
    query(a, b, dest, rgt, m, r)
  }

  fun eval(i: Int): Mat {
    _eval(N + i)
    return value[N + i]
  }

  fun inspect() = run { Pair(value, delay) }

  fun _eval(k: Int) {
    if (k > 1) {
      _eval(k / 2)
    }
    push(k)
  }
}