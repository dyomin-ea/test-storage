package presentation

import domain.storage.MapStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


@OptIn(ExperimentalCoroutinesApi::class)
class StorageViewModelTest {

    companion object {

        @JvmStatic
        fun foo(): Stream<Arguments> = Stream.of(
            Arguments.of(
                """
                    set foo 123
                    get foo
                """,
                """
                    123
                """
            ),
            Arguments.of(
                """
                    set 1 1
                    set 2 1
                    set 3 3
                    count 1
                    count 2
                    count 3
                """,
                """
                    2
                    0
                    1
                """
            ),
            Arguments.of(
                "get 1",
                "key not set"
            ),
            Arguments.of(
                """
                    set 1 1
                    get 1
                    begin
                    set 1 2
                    get 1
                    rollback
                    get 1
                """,
                """
                    1
                    2
                    1
                """
            ),
            Arguments.of(
                """
                    set 1 1
                    get 1
                    delete 1
                    get 1
                """,
                """
                    1
                    key not set
                """
            ),
            Arguments.of(
                """
                    commit
                """,
                """
                    no transaction
                """
            ),
            Arguments.of(
                """
                    rollback
                """,
                """
                    no transaction
                """
            ),
            Arguments.of(
                """
                    set 1 1
                    get 1
                    begin
                    set 1 2
                    get 1
                    begin
                    set 1 3
                    get 1
                    rollback
                    commit
                    get 1
                """,
                """
                    1
                    2
                    3
                    2
                """
            ),
            Arguments.of(
                """
                    set foo 123
                    set bar abc
                    begin
                    set foo 456
                    get foo
                    set bar def
                    get bar
                    rollback
                    get foo
                    get bar
                    commit
                """,
                """
                    456
                    def
                    123
                    abc
                    no transaction
                """
            ),
        )
    }

    private val viewModel = StorageViewModel(MapStorage(), ArgsValidator())

    @ParameterizedTest
    @MethodSource("foo")
    fun `on input command expect corresponding messages`(commands: String, messages: String) = runTest {
        viewModel.messageFlow.assert {
            commands.split().forEach { viewModel.onCommand(it) }

            assertValues(messages.split())
        }
    }

    private fun String.split(): List<String> {
        return trimIndent().removeSurrounding("\n").split('\n')
    }
}