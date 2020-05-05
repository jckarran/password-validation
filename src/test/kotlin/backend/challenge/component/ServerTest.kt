package backend.challenge.component

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import backend.challenge.application.BackendChallengeApplication
import backend.challenge.application.model.UserRequest
import backend.challenge.application.model.UserResponse
import io.javalin.Javalin
import io.prometheus.client.CollectorRegistry
import io.restassured.RestAssured.given
import org.eclipse.jetty.http.HttpStatus
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerTest {

    private lateinit var app: Javalin

    @BeforeAll
    fun beforeAll() {
        app = BackendChallengeApplication.init()
    }

    @AfterAll
    fun afterAll() {
        app.stop()
        CollectorRegistry.defaultRegistry.clear()
    }

    @Test
    fun `POST with valid password`() {
        val response = given()
            .header("transaction-id", 1)
            .`when`()
            .body(UserRequest("B4ckend&Challenge"))
            .post("http://localhost:7000/users/validate-password")
            .then()
            .statusCode(HttpStatus.OK_200)
            .extract().`as`(UserResponse::class.java)

        assertThat(response.status).isTrue()
    }

    @Test
    fun `POST password without especial characters `() {
        val response = given()
            .header("transaction-id", 1)
            .`when`()
            .body(UserRequest("B4ckendChallenge"))
            .post("http://localhost:7000/users/validate-password")
            .then()
            .statusCode(HttpStatus.OK_200)
            .extract().`as`(UserResponse::class.java)

        assertThat(response.status).isFalse()
    }

    @Test
    fun `POST password without number characters`() {
        val response = given()
            .header("transaction-id", 1)
            .`when`()
            .body(UserRequest("Backend%Challenge"))
            .post("http://localhost:7000/users/validate-password")
            .then()
            .statusCode(HttpStatus.OK_200)
            .extract().`as`(UserResponse::class.java)

        assertThat(response.status).isFalse()
    }

    @Test
    fun `POST password blank`() {
        val response = given()
            .header("transaction-id", 1)
            .`when`()
            .body(UserRequest(""))
            .post("http://localhost:7000/users/validate-password")
            .then()
            .statusCode(HttpStatus.OK_200)
            .extract().`as`(UserResponse::class.java)

        assertThat(response.status).isFalse()
    }

    @Test
    fun `POST password without upper case`() {
        val response = given()
            .header("transaction-id", 1)
            .`when`()
            .body(UserRequest("b4ckend%challenge"))
            .post("http://localhost:7000/users/validate-password")
            .then()
            .statusCode(HttpStatus.OK_200)
            .extract().`as`(UserResponse::class.java)

        assertThat(response.status).isFalse()
    }

    @Test
    fun `POST password without lower case`() {
        val response = given()
            .header("transaction-id", 1)
            .`when`()
            .body(UserRequest("B4CKEND%CHALLENGE"))
            .post("http://localhost:7000/users/validate-password")
            .then()
            .statusCode(HttpStatus.OK_200)
            .extract().`as`(UserResponse::class.java)

        assertThat(response.status).isFalse()
    }

    companion object {

    }
}