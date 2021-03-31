package lib

import kotlin.math.cos
import kotlin.math.sin

object Convolution {

  data class Complex(val r: Double, val i: Double) {
    operator fun plus(b: Complex) = Complex(r + b.r, i + b.i)
    operator fun minus(b: Complex) = Complex(r - b.r, i - b.i)
    operator fun times(b: Complex) = Complex(r*b.r - i*b.i, r*b.i + i*b.r)

    fun conjugate() = Complex(r, -i)
    fun scale(n: Double) = Complex(r*n, i*n)

    companion object {
      fun polar(r: Double, theta: Double) = run {
        // kotlin.math.sinはsignと間違いやすいので使うな
        Complex(r* cos(theta), r* sin(theta))
      }

      val zero = Complex(0.0, 0.0)
    }
  }

  /**
   * @return n * m - 1 じゃなくて、2^k個の配列を返す
   */
  fun convolve(a: DoubleArray, b: DoubleArray): Array<Complex> {
    val N = 1 shl log2_ceil(a.size + b.size - 1)
    val A = Array(N){
      if (it < a.size) Complex(a[it], 0.0) else Complex.zero
    }
    val B = Array(N){
      if (it < b.size) Complex(b[it], 0.0) else Complex.zero
    }
    dft(A, 1)
    dft(B, 1)
    for (i in 0 until N) {
      A[i] *= B[i]
    }
    idft(A)
    return A
  }

  fun dft(a: Array<Complex>, inv: Int) {
    val N = a.size
    if (N == 1) return

    val even = Array(N/2){a[it*2]}
    val odd = Array(N/2){a[it*2 + 1]}
    dft(even, inv)
    dft(odd, inv)

    for (k in 0 until N/2) {
      val angle = (-2.0*Math.PI*k / N) *inv
      val w = Complex.polar(1.0, angle)
      a[k] = even[k] + (w * odd[k])
      a[k + N/2] = even[k] - (w * odd[k])
    }
  }

  fun idft(a: Array<Complex>) {
    val N = a.size
    dft(a, -1)
    for (i in 0 until N) {
      a[i] = a[i].scale(1.0/N)
    }
  }

  /**
   * 最初に x<=2^k になるkを返す
   */
  fun log2_ceil(x: Int): Int {
    var k = 0
    var pow2 = 1L
    while(pow2 < x) {
      pow2 *= 2
      k++
    }
    return k
  }
}
