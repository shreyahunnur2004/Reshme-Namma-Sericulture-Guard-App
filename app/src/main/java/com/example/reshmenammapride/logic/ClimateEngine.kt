package com.example.reshmenammapride.logic

object ClimateEngine {

    // Ideal ranges for each Instar stage
    // Pair(minTemp, maxTemp) and Pair(minHumidity, maxHumidity)
    private val instarTempRanges = mapOf(
        1 to Pair(26f, 28f),
        2 to Pair(25f, 27f),
        3 to Pair(24f, 26f),
        4 to Pair(23f, 25f),
        5 to Pair(22f, 24f)
    )

    private val instarHumidityRanges = mapOf(
        1 to Pair(85f, 90f),
        2 to Pair(80f, 85f),
        3 to Pair(75f, 80f),
        4 to Pair(70f, 75f),
        5 to Pair(65f, 70f)
    )

    // Calculate current Instar stage based on days elapsed
    fun getCurrentInstar(daysSinceStart: Int): Int {
        return when {
            daysSinceStart <= 3  -> 1
            daysSinceStart <= 6  -> 2
            daysSinceStart <= 9  -> 3
            daysSinceStart <= 13 -> 4
            daysSinceStart <= 20 -> 5
            else                 -> 5
        }
    }

    // Calculate days elapsed from batch start date
    fun getDaysElapsed(startDateMillis: Long): Int {
        val diff = System.currentTimeMillis() - startDateMillis
        return (diff / (1000 * 60 * 60 * 24)).toInt() + 1
    }

    // Check if harvest time has come
    fun isHarvestTime(daysSinceStart: Int): Boolean {
        return daysSinceStart >= 20
    }

    // Main evaluation function — returns ClimateResult
    fun evaluate(
        temperature: Float,
        humidity: Float,
        instar: Int
    ): ClimateResult {

        val tempRange     = instarTempRanges[instar]     ?: Pair(24f, 26f)
        val humidityRange = instarHumidityRanges[instar] ?: Pair(70f, 80f)

        val tempStatus     = checkValue(temperature, tempRange.first, tempRange.second)
        val humidityStatus = checkValue(humidity,    humidityRange.first, humidityRange.second)

        // Overall status — worst of the two wins
        val overallStatus = when {
            tempStatus == Status.RED    || humidityStatus == Status.RED    -> Status.RED
            tempStatus == Status.ORANGE || humidityStatus == Status.ORANGE -> Status.ORANGE
            else                                                           -> Status.GREEN
        }

        val advice = generateAdvice(temperature, humidity, tempRange, humidityRange, overallStatus)

        return ClimateResult(
            status         = overallStatus,
            advice         = advice,
            idealTempMin   = tempRange.first,
            idealTempMax   = tempRange.second,
            idealHumMin    = humidityRange.first,
            idealHumMax    = humidityRange.second,
            instar         = instar
        )
    }

    private fun checkValue(value: Float, min: Float, max: Float): Status {
        return when {
            value > max + 3 || value < min - 3 -> Status.RED
            value > max     || value < min      -> Status.ORANGE
            else                                -> Status.GREEN
        }
    }

    private fun generateAdvice(
        temp: Float,
        humidity: Float,
        tempRange: Pair<Float, Float>,
        humidityRange: Pair<Float, Float>,
        status: Status
    ): String {
        return when {
            temp > tempRange.second + 3 ->
                "DANGER! Temperature is too high. Immediately spread wet gunny bags on the floor and open all windows and doors now."

            temp < tempRange.first - 3 ->
                "DANGER! Temperature is too low. Close all windows, use room heaters, and cover the rearing trays with newspaper."

            humidity < humidityRange.first - 5 ->
                "DANGER! Humidity is very low. Spray water on the floor and walls immediately. Hang wet cloth near the rearing trays."

            humidity > humidityRange.second + 5 ->
                "DANGER! Humidity is very high. Open all ventilation windows immediately to allow air circulation."

            temp > tempRange.second ->
                "CAUTION! Temperature is slightly high. Open side windows for ventilation. Monitor every 30 minutes."

            temp < tempRange.first ->
                "CAUTION! Temperature is slightly low. Close side windows and reduce ventilation slightly."

            humidity < humidityRange.first ->
                "CAUTION! Humidity is slightly low. Lightly spray water on the floor near the trays."

            humidity > humidityRange.second ->
                "CAUTION! Humidity is slightly high. Open ventilation windows slightly for fresh air."

            else ->
                "Perfect conditions! Silkworms are healthy and growing well. Keep maintaining the current environment."
        }
    }
}

// Status levels
enum class Status { GREEN, ORANGE, RED }

// Result returned to every screen
data class ClimateResult(
    val status: Status,
    val advice: String,
    val idealTempMin: Float,
    val idealTempMax: Float,
    val idealHumMin: Float,
    val idealHumMax: Float,
    val instar: Int
)