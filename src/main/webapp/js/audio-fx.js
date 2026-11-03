/* Tiny sound effects with the Web Audio API (no audio files needed). Toggle is remembered in localStorage. */
var Sfx = (function () {
  var ctx = null;
  function enabled() { try { return localStorage.getItem('gba_sound') !== 'off'; } catch (e) { return true; } }
  function tone(freq, start, dur, type) {
    if (!enabled()) return;
    try {
      ctx = ctx || new (window.AudioContext || window.webkitAudioContext)();
      var o = ctx.createOscillator(), g = ctx.createGain();
      o.type = type || 'triangle'; o.frequency.value = freq;
      g.gain.setValueAtTime(0.0001, ctx.currentTime + start);
      g.gain.exponentialRampToValueAtTime(0.18, ctx.currentTime + start + 0.02);
      g.gain.exponentialRampToValueAtTime(0.0001, ctx.currentTime + start + dur);
      o.connect(g); g.connect(ctx.destination);
      o.start(ctx.currentTime + start); o.stop(ctx.currentTime + start + dur + 0.05);
    } catch (e) { }
  }
  return {
    win: function () { tone(523, 0, .18); tone(659, .15, .18); tone(784, .3, .18); tone(1047, .45, .4); },
    lose: function () { tone(220, 0, .25, 'sawtooth'); tone(165, .22, .35, 'sawtooth'); },
    click: function () { tone(880, 0, .06, 'square'); },
    toggle: function () {
      var on = enabled();
      try { localStorage.setItem('gba_sound', on ? 'off' : 'on'); } catch (e) { }
      this.refresh(); if (!on) this.click();
    },
    refresh: function () {
      var b = document.getElementById('soundBtn');
      if (b) b.textContent = enabled() ? 'Sound on' : 'Sound off';
    }
  };
})();
document.addEventListener('DOMContentLoaded', function () { Sfx.refresh(); });
