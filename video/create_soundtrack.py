# -*- coding: utf-8 -*-
"""
Generador de banda sonora sintetizada estilo Cyberpunk / Tech Programming
para el video oficial de OmniTrack Logistics.
Totalmente instrumental, sintetizada matematicamente con NumPy y SciPy.
"""

import numpy as np
import scipy.io.wavfile as wavfile
import os

SAMPLE_RATE = 44100
BPM = 124.0
BEAT_DUR = 60.0 / BPM
SIXTEENTH = BEAT_DUR / 4.0
TOTAL_DURATION = 50.0  # 50 segundos
TOTAL_SAMPLES = int(SAMPLE_RATE * TOTAL_DURATION)

def generate_kick(duration=0.3):
    t = np.linspace(0, duration, int(SAMPLE_RATE * duration), False)
    # Pitch envelope: 150 Hz -> 42 Hz
    freq = 42.0 + 108.0 * np.exp(-32.0 * t)
    phase = 2.0 * np.pi * np.cumsum(freq) / SAMPLE_RATE
    amp = np.exp(-12.0 * t)
    kick = np.sin(phase) * amp
    # Punch click
    click = np.sin(2.0 * np.pi * 800.0 * t[:int(0.008 * SAMPLE_RATE)]) * np.exp(-500.0 * t[:int(0.008 * SAMPLE_RATE)])
    kick[:len(click)] += click * 0.4
    return np.tanh(kick * 1.5)

def generate_hihat(duration=0.06, open_hat=False):
    dur = 0.22 if open_hat else duration
    n = int(SAMPLE_RATE * dur)
    t = np.linspace(0, dur, n, False)
    noise = np.random.uniform(-1, 1, n)
    decay = 16.0 if open_hat else 70.0
    env = np.exp(-decay * t)
    # High-pass filter simulation
    filt = np.diff(noise, prepend=0)
    filt = np.diff(filt, prepend=0)
    hh = filt * env
    return hh / (np.max(np.abs(hh)) + 1e-6) * (0.35 if open_hat else 0.22)

def generate_snare(duration=0.25):
    n = int(SAMPLE_RATE * duration)
    t = np.linspace(0, duration, n, False)
    # Tone body
    tone = np.sin(2.0 * np.pi * 185.0 * t) * np.exp(-25.0 * t)
    # Noise body
    noise = np.random.uniform(-1, 1, n) * np.exp(-18.0 * t)
    snare = 0.4 * tone + 0.6 * noise
    return np.tanh(snare * 1.4) * 0.65

def note_freq(midi_note):
    return 440.0 * (2.0 ** ((midi_note - 69) / 12.0))

def generate_bass_note(midi, duration):
    n = int(SAMPLE_RATE * duration)
    t = np.linspace(0, duration, n, False)
    f = note_freq(midi)
    # Sawtooth with filter decay
    saw = 2.0 * (f * t - np.floor(0.5 + f * t))
    sub = np.sin(2.0 * np.pi * (f / 2.0) * t)
    env = np.exp(-4.5 * t)
    bass = (saw * 0.6 + sub * 0.4) * env
    return np.tanh(bass * 1.8) * 0.45

def generate_lead_note(midi, duration):
    n = int(SAMPLE_RATE * duration)
    t = np.linspace(0, duration, n, False)
    f = note_freq(midi)
    pulse = np.sign(np.sin(2.0 * np.pi * f * t) - 0.2)
    env = np.exp(-8.0 * t)
    return pulse * env * 0.22

def main():
    print("Sintetizando banda sonora Cyberpunk Tech Programming...")
    left = np.zeros(TOTAL_SAMPLES, dtype=np.float32)
    right = np.zeros(TOTAL_SAMPLES, dtype=np.float32)

    kick = generate_kick()
    snare = generate_snare()
    hh_closed = generate_hihat(open_hat=False)
    hh_open = generate_hihat(open_hat=True)

    # 1. Ritmo de bateria (Drums)
    total_beats = int(TOTAL_DURATION / BEAT_DUR)
    for b in range(total_beats):
        beat_time = b * BEAT_DUR
        idx = int(beat_time * SAMPLE_RATE)

        # Kick on 1, 2, 3, 4 (with occasional syncopation)
        if b >= 4 and idx + len(kick) < TOTAL_SAMPLES:
            left[idx:idx + len(kick)] += kick * 0.7
            right[idx:idx + len(kick)] += kick * 0.7

        # Snare on 2 and 4
        if b >= 8 and (b % 4 == 1 or b % 4 == 3):
            if idx + len(snare) < TOTAL_SAMPLES:
                left[idx:idx + len(snare)] += snare * 0.6
                right[idx:idx + len(snare)] += snare * 0.6

        # Hi-hats 16th notes
        if b >= 4:
            for sixteenth_step in range(4):
                s_idx = int((beat_time + sixteenth_step * SIXTEENTH) * SAMPLE_RATE)
                if s_idx < TOTAL_SAMPLES:
                    if sixteenth_step == 2:
                        h = hh_open
                    else:
                        h = hh_closed
                    l_len = min(len(h), TOTAL_SAMPLES - s_idx)
                    pan_l = 0.8 if sixteenth_step % 2 == 0 else 0.5
                    pan_r = 0.5 if sixteenth_step % 2 == 0 else 0.8
                    left[s_idx:s_idx + l_len] += h[:l_len] * pan_l
                    right[s_idx:s_idx + l_len] += h[:l_len] * pan_r

    # 2. Linea de Bajo (Bassline en Re menor: D, F, G, A, C)
    # Patron de bajo de 4 compases (16 tiempos)
    # MIDI: D2=38, F2=41, G2=43, A2=45, C3=48, D3=50
    bass_pattern = [
        # Bar 1 (D)
        (0.0, 38, 0.4), (0.5, 38, 0.4), (1.0, 50, 0.3), (1.75, 38, 0.3),
        (2.0, 38, 0.4), (2.5, 41, 0.4), (3.0, 43, 0.4), (3.5, 45, 0.4),
        # Bar 2 (D)
        (4.0, 38, 0.4), (4.5, 38, 0.4), (5.0, 50, 0.3), (5.75, 48, 0.3),
        (6.0, 48, 0.4), (6.5, 45, 0.4), (7.0, 43, 0.4), (7.5, 41, 0.4),
        # Bar 3 (Bb / F)
        (8.0, 34, 0.4), (8.5, 34, 0.4), (9.0, 46, 0.3), (9.75, 34, 0.3),
        (10.0, 34, 0.4), (10.5, 38, 0.4), (11.0, 41, 0.4), (11.5, 45, 0.4),
        # Bar 4 (C / A)
        (12.0, 36, 0.4), (12.5, 36, 0.4), (13.0, 48, 0.3), (13.75, 36, 0.3),
        (14.0, 45, 0.4), (14.5, 43, 0.4), (15.0, 41, 0.4), (15.5, 40, 0.4),
    ]

    pattern_bars = 4
    pattern_dur = pattern_bars * 4.0 * BEAT_DUR
    repeats = int(TOTAL_DURATION / pattern_dur) + 1
    for r in range(repeats):
        offset = r * pattern_dur
        if offset < 2.0:  # Start bass around 2s
            offset = 2.0
        for b_time, midi, dur in bass_pattern:
            t_start = offset + b_time * BEAT_DUR
            if 2.0 <= t_start < TOTAL_DURATION - 2.0:
                s_idx = int(t_start * SAMPLE_RATE)
                bass_buf = generate_bass_note(midi, dur * BEAT_DUR)
                l_len = min(len(bass_buf), TOTAL_SAMPLES - s_idx)
                left[s_idx:s_idx + l_len] += bass_buf[:l_len] * 0.75
                right[s_idx:s_idx + l_len] += bass_buf[:l_len] * 0.75

    # 3. Sintetizador Arpegiador (Arp Tech) en 16avas notas
    # Escala D menor arpegios: D4(62), F4(65), A4(69), C5(72), E5(76), D5(74), C5(72), A4(69)
    arp_notes = [62, 65, 69, 72, 74, 76, 74, 72, 69, 65, 62, 65, 69, 72, 76, 77]
    total_sixteenths = int(TOTAL_DURATION / SIXTEENTH)
    for s in range(total_sixteenths):
        t_start = s * SIXTEENTH
        if 4.0 <= t_start < TOTAL_DURATION - 1.5:
            note = arp_notes[s % len(arp_notes)]
            # Alternate octave every 16 steps
            if (s // 16) % 2 == 1:
                note += 12
            s_idx = int(t_start * SAMPLE_RATE)
            lead_buf = generate_lead_note(note, SIXTEENTH * 1.5)
            l_len = min(len(lead_buf), TOTAL_SAMPLES - s_idx)
            # Stereo ping-pong
            pan_left = 0.3 + 0.5 * np.sin(s * 0.4)
            pan_right = 0.3 + 0.5 * np.cos(s * 0.4)
            left[s_idx:s_idx + l_len] += lead_buf[:l_len] * pan_left * 0.4
            right[s_idx:s_idx + l_len] += lead_buf[:l_len] * pan_right * 0.4

    # 4. Tech telemetry blips (sonidos de computadora / terminal)
    np.random.seed(42)
    for _ in range(60):
        t_blip = np.random.uniform(1.0, TOTAL_DURATION - 2.0)
        f_blip = np.random.choice([1200, 1800, 2400, 3200, 4800, 960])
        dur_blip = np.random.uniform(0.03, 0.08)
        n_blip = int(SAMPLE_RATE * dur_blip)
        t_b = np.linspace(0, dur_blip, n_blip, False)
        blip = np.sin(2.0 * np.pi * f_blip * t_b) * np.exp(-40.0 * t_b) * 0.08
        idx = int(t_blip * SAMPLE_RATE)
        if idx + n_blip < TOTAL_SAMPLES:
            pan = np.random.uniform(0.2, 0.8)
            left[idx:idx + n_blip] += blip * pan
            right[idx:idx + n_blip] += blip * (1.0 - pan)

    # 5. Normalizacion y Fade-in / Fade-out
    # Fade in 1.5s
    fade_in_len = int(1.5 * SAMPLE_RATE)
    fade_in = np.linspace(0.0, 1.0, fade_in_len)
    left[:fade_in_len] *= fade_in
    right[:fade_in_len] *= fade_in

    # Fade out 2.5s
    fade_out_len = int(2.5 * SAMPLE_RATE)
    fade_out = np.linspace(1.0, 0.0, fade_out_len)
    left[-fade_out_len:] *= fade_out
    right[-fade_out_len:] *= fade_out

    # Mastering limiter
    max_val = max(np.max(np.abs(left)), np.max(np.abs(right)))
    if max_val > 0.0:
        left = (left / max_val) * 0.92
        right = (right / max_val) * 0.92

    stereo = np.vstack((left, right)).T
    stereo_int16 = (stereo * 32767.0).astype(np.int16)

    output_path = os.path.join("video", "soundtrack.wav")
    wavfile.write(output_path, SAMPLE_RATE, stereo_int16)
    print(f"Banda sonora generada: {output_path} ({TOTAL_DURATION}s, 44.1kHz Stereo)")

if __name__ == "__main__":
    main()
