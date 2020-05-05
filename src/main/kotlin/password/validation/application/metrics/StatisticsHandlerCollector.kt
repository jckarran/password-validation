package password.validation.application.metrics

import io.prometheus.client.Collector
import org.eclipse.jetty.server.handler.StatisticsHandler
import java.util.ArrayList
import java.util.Arrays


class StatisticsHandlerCollector private constructor(private val statisticsHandler: StatisticsHandler) :
    Collector() {
    override fun collect(): List<MetricFamilySamples> {
        return listOf(
            buildCounter(
                "jetty_requests_total",
                "Number of requests",
                statisticsHandler.requests.toDouble()
            ),
            buildGauge(
                "jetty_request_time_max_seconds",
                "Maximum time spent handling requests",
                statisticsHandler.requestTimeMax / 1000.0
            ),
            buildCounter(
                "jetty_request_time_seconds_total",
                "Total time spent in all request handling",
                statisticsHandler.requestTimeTotal / 1000.0
            ),
            buildGauge(
                "jetty_dispatched_active_max",
                "Maximum number of active dispatches being handled",
                statisticsHandler.dispatchedActiveMax.toDouble()
            ),
            buildCounter(
                "jetty_expires_total",
                "Number of async requests requests that have expired",
                statisticsHandler.expires.toDouble()
            ),
            buildStatusCounter(),
            buildGauge(
                "jetty_stats_seconds",
                "Time in seconds stats have been collected for",
                statisticsHandler.statsOnMs / 1000.0
            ),
            buildCounter(
                "jetty_responses_bytes_total",
                "Total number of bytes across all responses",
                statisticsHandler.responsesBytesTotal.toDouble()
            )
        )
    }

    private fun buildStatusCounter(): MetricFamilySamples {
        val name = "jetty_responses_total"
        return MetricFamilySamples(
            name,
            Type.COUNTER,
            "Number of requests with response status",
            Arrays.asList(
                buildStatusSample(
                    name,
                    "1xx",
                    statisticsHandler.responses1xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "2xx",
                    statisticsHandler.responses2xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "3xx",
                    statisticsHandler.responses3xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "4xx",
                    statisticsHandler.responses4xx.toDouble()
                ),
                buildStatusSample(
                    name,
                    "5xx",
                    statisticsHandler.responses5xx.toDouble()
                )
            )
        )
    }

    companion object {
        private val EMPTY_LIST: List<String> = ArrayList()
        fun initialize(statisticsHandler: StatisticsHandler) {
            StatisticsHandlerCollector(statisticsHandler).register<Collector>()
        }

        private fun buildGauge(name: String, help: String, value: Double): MetricFamilySamples {
            return MetricFamilySamples(
                name,
                Type.GAUGE,
                help,
                listOf(
                    MetricFamilySamples.Sample(
                        name,
                        EMPTY_LIST,
                        EMPTY_LIST,
                        value
                    )
                )
            )
        }

        private fun buildCounter(name: String, help: String, value: Double): MetricFamilySamples {
            return MetricFamilySamples(
                name,
                Type.COUNTER,
                help,
                listOf(
                    MetricFamilySamples.Sample(
                        name,
                        EMPTY_LIST,
                        EMPTY_LIST,
                        value
                    )
                )
            )
        }

        private fun buildStatusSample(
            name: String,
            status: String,
            value: Double
        ): MetricFamilySamples.Sample {
            return MetricFamilySamples.Sample(
                name, listOf("code"), listOf(status),
                value
            )
        }
    }

}