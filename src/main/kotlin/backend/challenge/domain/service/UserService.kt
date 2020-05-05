package backend.challenge.domain.service

import io.prometheus.client.Counter

class UserService {
    val requests: Counter = Counter.build()
        .name("password_validation").labelNames("status").help("Total of password requests.").register()


    fun checkPassword(password: String) =
        (password.contains(Regex("[a-zA-Z0-9*@!#%&^~]{9,}"))
                && password.contains(Regex("[*@!#%&^~]+"))
                && password.contains(Regex("\\d{1}"))
                && password.contains(Regex("[A-Z]+"))
                && password.contains(Regex("[a-z]+")))
            .also {
                requests.labels(it.toStatus()).inc()
            }

    private fun Boolean.toStatus() = if (this) "success" else "failed"
}