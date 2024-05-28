package uk.laptopphilharmonic.noisemaker.frequency

class OctaveRange(base: Frequency): FrequencyRange(base, base.overtone(2))