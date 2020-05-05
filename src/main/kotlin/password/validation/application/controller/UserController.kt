package password.validation.application.controller

import password.validation.application.model.UserRequest
import password.validation.application.model.UserResponse
import password.validation.domain.service.UserService
import io.javalin.http.Context

class UserController(private val userService: UserService) {

    fun validatePassword(ctx: Context) {
        val user = ctx.body<UserRequest>()
        val userResponse =
            UserResponse(userService.checkPassword(user.password))

        ctx.json(userResponse)
    }


}