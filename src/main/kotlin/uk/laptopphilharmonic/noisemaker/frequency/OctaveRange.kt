package uk.laptopphilharmonic.noisemaker.frequency

import kotlin.math.pow

/**
 * Will provide a frequency range of the supplied frequency up to the number of octaves specified
 * @param base - The lower end of the octave range
 * @param octaves - How many octaves the range should cover (default is 1)
 */
class OctaveRange(
    base: Frequency,
    octaves: Int = 1
): FrequencyRange(base, base * (2.0.pow(octaves).toInt()))