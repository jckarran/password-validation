package password.validation.application.controller

import password.validation.application.model.UserRequest
import password.validation.application.model.UserResponse
import password.validation.domain.service.UserService
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserControllerTests {
    private val ctx = mockk<Context>(relaxed = true)
    private val service = mockk<UserService>(relaxed = true)
    private val userController = UserController(service)
    
    @Test
    fun `with valid password`() {
        every { ctx.body<UserRequest>() } returns UserRequest(
            "AbTp9!foo"
        )
        every { service.checkPassword(any()) } returns true
        userController.validatePassword(ctx)
        verify { ctx.json(UserResponse(true)) }
    }

    @Test
    fun `with invalid password`() {
        every { ctx.body<UserRequest>() } returns UserRequest(
            "abc"
        )
        every { service.checkPassword(any()) } returns false
        userController.validatePassword(ctx)
        verify { ctx.json(UserResponse(false)) }
    }

    @Test
    fun `expecting exception`() {
        every { ctx.body<UserRequest>() } throws BadRequestResponse("")
        assertThrows<BadRequestResponse> {
            userController.validatePassword(ctx)
        }
    }
}