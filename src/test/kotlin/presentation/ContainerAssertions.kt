package presentation

import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ContainerAssertions<T>(
    private val source: List<T>,
) {

    fun assertValues(expected: List<T>) {
        assertContentEquals(expected, actual = source)
    }
}