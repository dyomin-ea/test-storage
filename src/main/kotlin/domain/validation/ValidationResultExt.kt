package domain.validation


inline fun ValidationResult.onValid(body: () -> Unit): ValidationResult =
    apply {
        if (this is ValidationResult.Valid) body()
    }

inline fun ValidationResult.onInvalid(body: (ValidationResult.Invalid) -> Unit): ValidationResult =
    apply {
        if (this is ValidationResult.Invalid) body(this)
    }