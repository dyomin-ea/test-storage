package domain.validation

sealed interface ValidationResult {

    object Valid : ValidationResult

    fun interface Invalid : ValidationResult {

        fun getReason(): String
    }
}