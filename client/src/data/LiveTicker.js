/**
 * Ticker that invokes listeners with a given period.
 */
class LiveTickerImpl {
  listeners: Set;

  constructor(delayMs) {
    this.listeners = new Set();
    // PICK: Optimize to only tick when listeners are attached?
    window.setInterval(this.triggerListeners.bind(this), delayMs);
  }

  addListener(callback): Function {
    if (this.listeners.has(callback)) {
      window.console.error("Listener already attached!");
      return null;
    }
    this.listeners.add(callback);
    return () => this.listeners.delete(callback);
  }

  triggerListeners() {
    this.listeners.forEach(listener => listener());
  }
}

const LiveTicker = new LiveTickerImpl(500);

export default LiveTicker;
