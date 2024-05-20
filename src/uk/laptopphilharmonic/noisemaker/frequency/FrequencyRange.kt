package uk.laptopphilharmonic.noisemaker.frequency

open class FrequencyRange(
    frequency1: Frequency,
    frequency2: Frequency,
) {
    val lower: Frequency = Frequency(frequency1.hz.coerceAtMost(frequency2.hz))
    val upper: Frequency = Frequency(frequency1.hz.coerceAtLeast(frequency2.hz))
}
