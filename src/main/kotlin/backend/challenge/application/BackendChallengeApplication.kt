package backend.challenge.application

import backend.challenge.application.controller.UserController
import backend.challenge.application.metrics.StatisticsHandlerCollector
import backend.challenge.application.module.controllerModules
import backend.challenge.application.module.serviceModules
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.prometheus.client.exporter.HTTPServer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.ObjectMessage
import org.eclipse.jetty.http.HttpStatus
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.StatisticsHandler
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject

object BackendChallengeApplication : KoinComponent {

    private val logger = LogManager.getLogger(BackendChallengeApplication::class.java)

    fun init(): Javalin {

        startKoin {
            modules(controllerModules + serviceModules)
        }

        val userController by inject<UserController>()

        val statisticsHandler = StatisticsHandler()
        initializePrometheus(
            statisticsHandler
        )

        val app = Javalin.create { config ->
            config.requestLogger { ctx, executionTimeMs ->
                logRequest(
                    ctx,
                    executionTimeMs
                )
            }
            config.server {
                Server().apply {
                    handler = statisticsHandler
                }
            }
            config.showJavalinBanner = false
        }.events {
            it.serverStopped(BackendChallengeApplication::stop)
        }.apply {
            exception(BadRequestResponse::class.java) { exception, ctx ->
                ctx.status(HttpStatus.BAD_REQUEST_400)
                ctx.result(exception.message.toString())
            }
        }.start(7000)

        app.routes {
            ApiBuilder.path("users/validate-password") {
                ApiBuilder.post(userController::validatePassword)
            }
        }

        return app
    }

    private fun initializePrometheus(statisticsHandler: StatisticsHandler) {
        StatisticsHandlerCollector.initialize(statisticsHandler)
        HTTPServer(9090)
    }

    private fun logRequest(ctx: Context, executionTimeMs: Float) {
        logger.info(
            ObjectMessage(
                mapOf(
                    "message" to "access log",
                    "transaction-id" to ctx.header("transaction-id"),
                    "method" to ctx.method(),
                    "path" to ctx.endpointHandlerPath(),
                    "status" to ctx.status(),
                    "ms" to executionTimeMs
                )
            )
        )
    }

    private fun stop() {
        stopKoin()
    }
}