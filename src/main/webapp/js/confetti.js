/* Gold + amethyst confetti burst on a full-screen canvas */
function launchConfetti(count) {
  count = count || 160;
  var canvas = document.createElement('canvas');
  canvas.className = 'confetti-canvas';
  document.body.appendChild(canvas);
  var ctx = canvas.getContext('2d');
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  var colors = ['#f2c14e', '#ffe7a3', '#b8861b', '#8b5cf6', '#a98bff', '#ece8f7'];
  var bits = [];
  for (var i = 0; i < count; i++) {
    bits.push({
      x: canvas.width / 2, y: canvas.height * 0.35,
      vx: (Math.random() - 0.5) * 16, vy: Math.random() * -14 - 3,
      size: Math.random() * 7 + 4, color: colors[Math.floor(Math.random() * colors.length)],
      rot: Math.random() * 6, vr: (Math.random() - 0.5) * 0.4
    });
  }
  var frames = 0;
  (function tick() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    bits.forEach(function (b) {
      b.vy += 0.35; b.x += b.vx; b.y += b.vy; b.rot += b.vr;
      ctx.save(); ctx.translate(b.x, b.y); ctx.rotate(b.rot);
      ctx.fillStyle = b.color; ctx.fillRect(-b.size / 2, -b.size / 4, b.size, b.size / 2); ctx.restore();
    });
    if (++frames < 220) requestAnimationFrame(tick); else canvas.remove();
  })();
}
