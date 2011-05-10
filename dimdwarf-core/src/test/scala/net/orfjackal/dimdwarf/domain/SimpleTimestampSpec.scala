package net.orfjackal.dimdwarf.domain

import org.junit.runner.RunWith
import net.orfjackal.specsy._
import org.junit.Assert._
import org.hamcrest.Matchers._
import org.hamcrest.MatcherAssert.assertThat
import java.lang.Long

@RunWith(classOf[Specsy])
class SimpleTimestampSpec extends Spec {
  "Timestamps are value objects" >> {
    val ts1a = SimpleTimestamp(1L)
    val ts1b = SimpleTimestamp(1L)
    val ts2 = SimpleTimestamp(2L)

    assertThat(ts1a, equalTo(ts1b))
    assertThat(ts1a, not(equalTo(ts2)))
    assertThat(ts2, not(equalTo(ts1a)))
    assertThat(ts1a.hashCode, equalTo(ts1b.hashCode))
    assertThat(ts1a.hashCode, not(equalTo(ts2.hashCode)))
  }

  "Timestamps are shown in hexadecimal format" >> {
    assertThat(SimpleTimestamp(0L).toString, is("{00000000-00000000}"))
    assertThat(SimpleTimestamp(1L).toString, is("{00000000-00000001}"))
    assertThat(SimpleTimestamp(Long.MAX_VALUE).toString, is("{7fffffff-ffffffff}"))
    assertThat(SimpleTimestamp(Long.MIN_VALUE).toString, is("{80000000-00000000}"))
    assertThat(SimpleTimestamp(-1L).toString, is("{ffffffff-ffffffff}"))
  }

  "Timestamps can be incremented" >> {
    assertThat(SimpleTimestamp(0L).next, is(SimpleTimestamp(1L)))
    assertThat(SimpleTimestamp(1L).next, is(SimpleTimestamp(2L)))
    assertThat(SimpleTimestamp(Long.MAX_VALUE).next, is(SimpleTimestamp(Long.MIN_VALUE)))
    assertThat(SimpleTimestamp(-2L).next, is(SimpleTimestamp(-1L)))
  }

  "Timestamps cannot overflow" >> {
    try {
      SimpleTimestamp(-1L).next

      fail("should have thrown an exception")
    } catch {
      case e: IllegalStateException =>
        assertThat(e.getMessage, containsString("overflow"))
    }
  }

  "Timestamps are ordered" >> {
    val order = Seq(
      SimpleTimestamp(0L),
      SimpleTimestamp(1L),
      SimpleTimestamp(Integer.MAX_VALUE - 1L),
      SimpleTimestamp(Integer.MAX_VALUE),
      SimpleTimestamp(Integer.MAX_VALUE + 1L),
      SimpleTimestamp(Long.MAX_VALUE - 1L),
      SimpleTimestamp(Long.MAX_VALUE),
      SimpleTimestamp(Long.MIN_VALUE),
      SimpleTimestamp(Long.MIN_VALUE + 1L),
      SimpleTimestamp(Integer.MIN_VALUE - 1L),
      SimpleTimestamp(Integer.MIN_VALUE),
      SimpleTimestamp(Integer.MIN_VALUE + 1L),
      SimpleTimestamp(-1L))

    for (i <- 0 until order.length) {
      val first = order(i)
      assertEqualToItself(first)

      for (j <- i + 1 until order.length) {
        val second = order(j)

        "Case: " + first + " < " + second >> {
          assertLessThan(first, second)
        }
      }
    }

    // TODO: use Scala's < operator
  }

  private def assertEqualToItself(ts: Timestamp) {
    assertTrue("did not satisfy: " + ts + " == " + ts, ts.compareTo(ts) == 0)
  }

  private def assertLessThan(ts1: Timestamp, ts2: Timestamp) {
    assertTrue("did not satisfy: " + ts1 + " < " + ts2, ts1.compareTo(ts2) < 0)
    assertTrue("did not satisfy: " + ts2 + " > " + ts1, ts2.compareTo(ts1) > 0)
  }
}
