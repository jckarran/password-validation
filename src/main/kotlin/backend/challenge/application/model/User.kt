package backend.challenge.application.model

data class UserRequest(
    val password: String
)

data class UserResponse(
    val status: Boolean
)