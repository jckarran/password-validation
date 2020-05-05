package backend.challenge.application.controller

import backend.challenge.application.model.UserRequest
import backend.challenge.application.model.UserResponse
import backend.challenge.domain.service.UserService
import io.javalin.http.Context

class UserController(private val userService: UserService) {

    fun validatePassword(ctx: Context) {
        val user = ctx.body<UserRequest>()
        val userResponse = UserResponse(userService.checkPassword(user.password))

        ctx.json(userResponse)
    }


}