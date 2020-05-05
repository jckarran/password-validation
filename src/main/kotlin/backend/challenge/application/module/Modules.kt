package backend.challenge.application.module

import backend.challenge.application.controller.UserController
import backend.challenge.domain.service.UserService
import org.koin.dsl.module

val serviceModules = module {
    single { UserService() }
}

val controllerModules = module {
    single { UserController(get()) }
}