export function completeAfterMinimumDelay(startedAt: number, minimumDelayMs: number, callback: () => void): void {
  const elapsed = Date.now() - startedAt;
  const remaining = Math.max(0, minimumDelayMs - elapsed);
  setTimeout(callback, remaining);
}
