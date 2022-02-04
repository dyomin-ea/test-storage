package presentation

import domain.validation.ValidationResult
import presentation.res.Command.BEGIN
import presentation.res.Command.COMMIT
import presentation.res.Command.COUNT
import presentation.res.Command.DELETE
import presentation.res.Command.EXIT
import presentation.res.Command.GET
import presentation.res.Command.HELP
import presentation.res.Command.ROLLBACK
import presentation.res.Command.SET

class ArgsValidator {

    fun validate(args: List<String>): ValidationResult {

        if (args.isEmpty()) {
            return ValidationResult.Invalid { "Arguments are empty" }
        }

        return when (args[0].uppercase()) {
            "" -> ValidationResult.Invalid { "Command is empty, try HELP" }

            SET  -> ensure(args) { it.size == 3 }

            GET,
            DELETE,
            COUNT,
                 -> ensure(args) { it.size >= 2 }

            BEGIN,
            COMMIT,
            ROLLBACK,
            EXIT,
            HELP,
                 -> ValidationResult.Valid

            else -> ValidationResult.Invalid { "Unknown argument, try HELP" }
        }
    }

    private fun ensure(args: List<String>, predicate: (List<String>) -> Boolean): ValidationResult =
        if (!predicate(args)) {
            ValidationResult.Invalid { "arguments of ${args.first()} are invalid" }
        } else {
            ValidationResult.Valid
        }
}