const ExponentialFit = {
  /* Fit some [x, y] pairs, returning [A, B] to fit y = Ae^Bx (or ln y = ln A + Bx) */
  bestFitCoeffs(values) {
    // http://mathworld.wolfram.com/LeastSquaresFittingExponential.html
    let n = values.length;
    let y = 0, xy = 0, xxy = 0, ylny = 0, xylny = 0;

    for (let i = 0; i < n; i++) {
      let vx = values[i][0], vy = values[i][1], lny = Math.log(vy);
      y += vy;
      xy += vx * vy;
      xxy += vx * vx * vy;
      ylny += vy * lny;
      xylny += vx * vy * lny;
    }

    var scale = 1.0 / (y * xxy - xy * xy);
    var linearFit = [
      (xxy *  ylny - xy * xylny) * scale,
      (  y * xylny - xy *  ylny) * scale
    ];
    return linearFit;
  },

  bestFitLine(values) {
    const fit = this.bestFitCoeffs(values);
    return values.map(function(x) {
      return [x[0], Math.exp(fit[0] + x[0] * fit[1])];
    });
  },

  /** Given an array of {name, [t, v] values), trim each at the start so that their first entry is non-zero. */
  trimFirstZerosInPlace(data) {
    data.forEach(line => {
      let firstNonZero = 0;
      while (firstNonZero < line.values.length && line.values[firstNonZero].v === 0) {
        firstNonZero++;
      }
      if (firstNonZero !== line.values.length) {
        line.values = line.values.slice(firstNonZero);
      }
    });
  }
};

export default ExponentialFit;
