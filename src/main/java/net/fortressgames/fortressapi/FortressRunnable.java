package net.fortressgames.fortressapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public abstract class FortressRunnable implements Runnable {

	private BukkitTask task;

	@NotNull
	private BukkitTask setupTask(@NotNull BukkitTask task) {
		this.task = task;
		return task;
	}

	public synchronized boolean isCancelled() throws IllegalStateException {
		checkScheduled();
		return this.task.isCancelled();
	}

	public synchronized void cancel() throws IllegalStateException {
		Bukkit.getScheduler().cancelTask(getTaskId());
	}

	public synchronized int getTaskId() throws IllegalStateException {
		checkScheduled();
		return this.task.getTaskId();
	}

	private void checkScheduled() {
		if(this.task == null) {
			throw new IllegalStateException("Not scheduled yet");
		}
	}

	private void checkNotYetScheduled() {
		if(this.task != null) {
			throw new IllegalStateException("Already scheduled as " + this.task.getTaskId());
		}
	}

	@NotNull
	public synchronized BukkitTask runTaskTimer(@NotNull Plugin plugin, long delay, TimeUnit timeUnit, long period) throws IllegalArgumentException, IllegalStateException {
		checkNotYetScheduled();
		return setupTask(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, returnPeriod(timeUnit, period)));
	}

	@NotNull
	public synchronized BukkitTask runTaskTimer(@NotNull Plugin plugin, TimeUnit timeUnit, long period) throws IllegalArgumentException, IllegalStateException {
		checkNotYetScheduled();
		return setupTask(Bukkit.getScheduler().runTaskTimer(plugin, this, 0, returnPeriod(timeUnit, period)));
	}

	@NotNull
	public synchronized BukkitTask runTaskTimerAsynchronously(@NotNull Plugin plugin, long delay, TimeUnit timeUnit, long period) throws IllegalArgumentException, IllegalStateException {
		checkNotYetScheduled();
		return setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, returnPeriod(timeUnit, period)));
	}

	@NotNull
	public synchronized BukkitTask runTaskTimerAsynchronously(@NotNull Plugin plugin, TimeUnit timeUnit, long period) throws IllegalArgumentException, IllegalStateException {
		checkNotYetScheduled();
		return setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 0, returnPeriod(timeUnit, period)));
	}

	private long returnPeriod(TimeUnit timeUnit, long period) {
		switch (timeUnit) {
			case HOURS -> {
				return ((period * 60) * 60) * 20;
			}

			case MINUTES -> {
				return (period * 60) * 20;
			}

			case SECONDS -> {
				return period * 20;
			}

			default -> {
				return period;
			}
		}
	}
}