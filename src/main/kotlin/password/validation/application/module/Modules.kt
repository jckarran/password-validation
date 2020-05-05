package password.validation.application.module

import password.validation.application.controller.UserController
import password.validation.domain.service.UserService
import org.koin.dsl.module

val serviceModules = module {
    single { UserService() }
}

val controllerModules = module {
    single { UserController(get()) }
}