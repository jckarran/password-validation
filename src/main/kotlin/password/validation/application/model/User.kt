package password.validation.application.model

data class UserRequest(
    val password: String
)

data class UserResponse(
    val status: Boolean
)