package backend.challenge.domain.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.tableOf
import io.prometheus.client.CollectorRegistry
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserServiceTest {

    @AfterAll
    fun afterAll() {
        CollectorRegistry.defaultRegistry.clear()
    }

    private val userService = UserService()

    @Test
    fun `password validation cases`() {
        tableOf("case-name", "input", "result")
            .row("With valid password", "Backend*Chall3ng3", true)
            .row("Without number", "Pass%word", false)
            .row("Without especial char ", "P3ss6word", false)
            .row("Without uppercase ", "p3ss6word", false)
            .row("Without lowercase", "P3SS6WORD", false)
            .row("With less than 9 chars ", "p3ss$6", false)
            .row("Empty", "", false)
            .forAll { _, password, result ->
                assertThat(userService.checkPassword(password)).isEqualTo(result)
            }
    }
}