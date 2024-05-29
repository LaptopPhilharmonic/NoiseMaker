package uk.laptopphilharmonic.noisemaker.frequency

/**
 * Standardised frequency range class. It doesn't matter what order the frequencies are provided in, the
 * class will assign lower and upper correctly for you.
 */
open class FrequencyRange(
    frequency1: Frequency,
    frequency2: Frequency,
) {
    /** The lower end of the range */
    val lower: Frequency = Frequency(frequency1.hz.coerceAtMost(frequency2.hz))
    /** The upper end of the range */
    val upper: Frequency = Frequency(frequency1.hz.coerceAtLeast(frequency2.hz))
}
