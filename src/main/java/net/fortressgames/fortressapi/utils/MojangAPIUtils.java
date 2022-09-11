package net.fortressgames.fortressapi.utils;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation to make requests to Mojang's API servers.
 * See http://wiki.vg/Mojang_API for more information.
 *
 * Since all of these methods require connections to Mojang's servers, all of them
 * execute asynchronously, and do therefor not return any values. Instead, a callback mechanism
 * is implemented, which allows for processing of data returned from these requests.
 * If an error occurs when retrieving the data, the 'successful' boolean in the callback
 * will be set to false. In these cases, null will be passed to the callback, even if
 * some data has been received.
 *
 * NOTE: callbacks are always fired on the main thread, even if they end in an error.
 *
 * @author AlvinB
 */
public class MojangAPIUtils {

	private static URL API_STATUS_URL = null;
	private static URL GET_UUID_URL = null;
	private static final JSONParser PARSER = new JSONParser();

	private static Plugin plugin;

	static {
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin.getClass().getProtectionDomain().getCodeSource().equals(MojangAPIUtils.class.getProtectionDomain().getCodeSource())) {
				MojangAPIUtils.plugin = plugin;
			}
		}
		try {
			API_STATUS_URL = new URL("https://status.mojang.com/check");
			GET_UUID_URL = new URL("https://api.mojang.com/profiles/minecraft");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the plugin instance to use for scheduler tasks.
	 *
	 * The plugin instance in the same jar as this class should automatically be found, so only
	 * use this if you for whatever reason need to use another plugin instance.
	 *
	 * @param plugin the plugin instance
	 */
	public void setPlugin(Plugin plugin) {
		MojangAPIUtils.plugin = plugin;
	}

	/**
	 * Gets the current state of Mojang's API
	 *
	 * The keys of the map passed to the callback is the service, and the value is the current state of the service.
	 * Statuses can be either RED (meaning service unavailable), YELLOW (meaning service available,
	 * but with some issues) and GREEN (meaning service fully functional).
	 *
	 * @see APIStatus
	 *
	 * @param callBack the callback of the request
	 */
	@SuppressWarnings("unchecked")
	public static void getAPIStatus(ResultCallBack<Map<String, APIStatus>> callBack) {
		if (plugin == null) {
			return;
		}
		makeAsyncGetRequest(API_STATUS_URL, (successful, response, exception, responseCode) -> {
			if (callBack == null) {
				return;
			}
			if (successful && responseCode == 200) {
				try {
					Map<String, APIStatus> map = Maps.newHashMap();
					JSONArray jsonArray = (JSONArray) PARSER.parse(response);
					for (JSONObject jsonObject : (List<JSONObject>) jsonArray) {
						for (JSONObject.Entry<String, String> entry : ((Map<String, String>) jsonObject).entrySet()) {
							map.put(entry.getKey(), APIStatus.fromString(entry.getValue()));
						}
					}
					new BukkitRunnable() {
						@Override
						public void run() {
							callBack.callBack(true, map, null);
						}
					}.runTask(plugin);
				} catch (Exception e) {
					new BukkitRunnable() {
						@Override
						public void run() {
							callBack.callBack(false, null, e);
						}
					}.runTask(plugin);
				}
			} else {
				new BukkitRunnable() {
					@Override
					public void run() {
						callBack.callBack(false, null, Objects.requireNonNullElseGet(exception, () -> new IOException("Failed to obtain Mojang data! Response code: " + responseCode)));
					}
				}.runTask(plugin);
			}
		});
	}

	/**
	 * The statuses of Mojang's API used by getAPIStatus().
	 */
	public enum APIStatus {
		RED,
		YELLOW,
		GREEN;

		public static APIStatus fromString(String string) {
			return switch (string) {
				case "red" -> RED;
				case "yellow" -> YELLOW;
				case "green" -> GREEN;
				default -> throw new IllegalArgumentException("Unknown status: " + string);
			};
		}
	}

	/**
	 * Gets the UUID of a name at a certain point in time
	 *
	 * The timestamp is in UNIX Time, and if -1 is used as the timestamp,
	 * it will get the current user who has this name.
	 *
	 * The callback contains the UUID and the current username of the UUID.
	 * If the username was not occupied at the specified time, the next
	 * person to occupy the name will be returned, provided that the name
	 * has been changed away from at least once or is legacy. If the name
	 * hasn't been changed away from and is not legacy, the pair passed
	 * to the callback (not the values of the pair) will be null.
	 *
	 * @param username the username of the player to do the UUID lookup on
	 * @param timeStamp the timestamp when the name was occupied
	 * @param callBack the callback of the request
	 */
	public static void getUUIDAtTime(String username, long timeStamp, ResultCallBack<Pair<UUID, String>> callBack) {
		if (plugin == null) {
			return;
		}
		Validate.notNull(username);
		Validate.isTrue(!username.isEmpty(), "username cannot be empty");
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username + (timeStamp != -1 ? "?at=" + timeStamp : ""));
			makeAsyncGetRequest(url, (successful, response, exception, responseCode) -> {
				if (callBack == null) {
					return;
				}
				if (successful && (responseCode == 200 || responseCode == 204)) {
					try {
						@SuppressWarnings("unchecked") Pair<UUID, String>[] pair = new Pair[1];
						if (responseCode == 200) {
							JSONObject object = (JSONObject) PARSER.parse(response);
							String uuidString = (String) object.get("id");
							uuidString = uuidString.substring(0, 7) + "-" + uuidString.substring(8, 11) + "-" + uuidString.substring(12, 15) + "-" + uuidString.substring(16, 19) + "-" +
									uuidString.substring(20);
							pair[0] = ImmutablePair.of(UUID.fromString(uuidString), (String) object.get("name"));
						}
						new BukkitRunnable() {
							@Override
							public void run() {
								callBack.callBack(true, pair[0], null);
							}
						}.runTask(plugin);
					} catch (Exception e) {
						new BukkitRunnable() {
							@Override
							public void run() {
								callBack.callBack(false, null, e);
							}
						}.runTask(plugin);
					}
				} else {
					new BukkitRunnable() {
						@Override
						public void run() {
							callBack.callBack(false, null, Objects.requireNonNullElseGet(exception, () -> new IOException("Failed to obtain Mojang data! Response code: " + responseCode)));
						}
					}.runTask(plugin);
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Gets the name history of a certain UUID
	 *
	 * The callback is passed a Map<String, Long>, the String being the name,
	 * and the long being the UNIX millisecond timestamp the user changed to
	 * that name. If the name was the original name of the user, the long will
	 * be -1L.
	 *
	 * If an unused UUID is supplied, an empty Map will be passed to the callback.
	 *
	 * @param uuid the uuid of the account
	 * @param callBack the callback of the request
	 */
	@SuppressWarnings("unchecked")
	public static void getNameHistory(UUID uuid, ResultCallBack<Map<String, Long>> callBack) {
		if (plugin == null) {
			return;
		}
		Validate.notNull(uuid, "uuid cannot be null!");
		try {
			URL url = new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names");
			makeAsyncGetRequest(url, (successful, response, exception, responseCode) -> {
				if (callBack == null) {
					return;
				}
				if (successful && (responseCode == 200 || responseCode == 204)) {
					try {
						Map<String, Long> map = Maps.newHashMap();
						if (responseCode == 200) {
							JSONArray jsonArray = (JSONArray) PARSER.parse(response);
							for (JSONObject jsonObject : (List<JSONObject>) jsonArray) {
								String name = (String) jsonObject.get("name");
								if (jsonObject.containsKey("changedToAt")) {
									map.put(name, (Long) jsonObject.get("changedToAt"));
								} else {
									map.put(name, -1L);
								}
							}
						}
						new BukkitRunnable() {
							@Override
							public void run() {
								callBack.callBack(true, map, null);
							}
						}.runTask(plugin);
					} catch (Exception e) {
						new BukkitRunnable() {
							@Override
							public void run() {
								callBack.callBack(false, null, e);
							}
						}.runTask(plugin);
					}
				} else {
					new BukkitRunnable() {
						@Override
						public void run() {
							callBack.callBack(false, null, Objects.requireNonNullElseGet(exception, () -> new IOException("Failed to obtain Mojang data! Response code: " + responseCode)));
						}
					}.runTask(plugin);
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void getUUID(ResultCallBack<Map<String, Profile>> callBack, String... usernames) {
		getUUID(Arrays.asList(usernames), callBack);
	}

	@SuppressWarnings("unchecked")
	public static void getUUID(List<String> usernames, ResultCallBack<Map<String, Profile>> callBack) {
		if (plugin == null) {
			return;
		}
		Validate.notNull(usernames, "usernames cannot be null");
		Validate.isTrue(usernames.size() <= 100, "cannot request more than 100 usernames at once");
		JSONArray usernameJson = new JSONArray();
		usernameJson.addAll(usernames.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList()));
		makeAsyncPostRequest(GET_UUID_URL, usernameJson.toJSONString(), (successful, response, exception, responseCode) -> {
			if (callBack == null) {
				return;
			}
			try {
				if (successful && responseCode == 200) {
					Map<String, Profile> map = Maps.newHashMap();
					JSONArray jsonArray = (JSONArray) PARSER.parse(response);

					for(JSONObject jsonObject : (List<JSONObject>) jsonArray) {
						String uuidString = (String) jsonObject.get("id");

						uuidString = uuidString.substring(0, 8) + "-" + uuidString.substring(8, 12) + "-" + uuidString.substring(12, 16) + "-" +
								uuidString.substring(16, 20) + "-" + uuidString.substring(20);

						System.out.println(uuidString);

						String name = (String) jsonObject.get("name");

						boolean legacy = false;
						if (jsonObject.containsKey("legacy")) {
							legacy = (boolean) jsonObject.get("legacy");
						}

						boolean unpaid = false;
						if (jsonObject.containsKey("demo")) {
							unpaid = (boolean) jsonObject.get("demo");
						}

						map.put(name, new Profile(UUID.fromString(uuidString), name, legacy, unpaid));
					}

					new BukkitRunnable() {
						@Override
						public void run() {
							callBack.callBack(true, map, null);
						}
					}.runTask(plugin);
				} else {
					new BukkitRunnable() {
						@Override
						public void run() {
							callBack.callBack(false, null, Objects.requireNonNullElseGet(exception, () -> new IOException("Failed to obtain Mojang data! Response code: " + responseCode)));
						}
					}.runTask(plugin);
				}
			} catch (Exception e) {
				new BukkitRunnable() {
					@Override
					public void run() {
						callBack.callBack(false, null, e);
					}
				}.runTask(plugin);
			}
		});
	}

	public record Profile(@Getter UUID uuid, @Getter String name, @Getter boolean legacy, @Getter boolean unpaid) {

		@Override
		public String toString() {
			return "Profile{uuid=" + uuid + ", name=" + name + ", legacy=" + legacy + ", unpaid=" + unpaid + "}";
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == this) {
				return true;
			}
			if(!(obj instanceof Profile otherProfile)) {
				return false;
			}
			return uuid.equals(otherProfile.uuid) && name.equals(otherProfile.name) && legacy == otherProfile.legacy && unpaid == otherProfile.unpaid;
		}

		@Override
		public int hashCode() {
			return Objects.hash(uuid, name, legacy, unpaid);
		}
	}

	private static void makeAsyncGetRequest(URL url, RequestCallBack asyncCallBack) {
		if (plugin == null) {
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				StringBuilder response = new StringBuilder();
				try {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.connect();
					//noinspection Duplicates
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
						String line = reader.readLine();
						while (line != null) {
							response.append(line);
							line = reader.readLine();
						}
						asyncCallBack.callBack(true, response.toString(), null, connection.getResponseCode());
					}
				} catch (Exception e) {
					asyncCallBack.callBack(false, response.toString(), e, -1);
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	private static void makeAsyncPostRequest(URL url, String payload, RequestCallBack asyncCallBack) {
		if (plugin == null) {
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				StringBuilder response = new StringBuilder();
				try {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json");
					connection.connect();
					try (PrintWriter writer = new PrintWriter(connection.getOutputStream())) {
						writer.write(payload);
					}
					//noinspection Duplicates
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
						String line = reader.readLine();
						while (line != null) {
							response.append(line);
							line = reader.readLine();
						}
						asyncCallBack.callBack(true, response.toString(), null, connection.getResponseCode());
					}
				} catch (Exception e) {
					asyncCallBack.callBack(false, response.toString(), e, -1);
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	@FunctionalInterface
	private interface RequestCallBack {
		void callBack(boolean successful, String response, Exception exception, int responseCode);
	}

	/**
	 * The callback interface
	 *
	 * Once some data is received (or an error is thrown)
	 * the callBack method is fired synchronously with the following data:
	 *
	 * boolean successful - If the data arrived and was interpreted correctly.
	 *
	 * <T> result - The data. Only present if successful is true, otherwise null.
	 *
	 * Exception e - The exception. Only present if successful is false, otherwise null.
	 *
	 * This interface is annotated with @FunctionalInterface, which allows for instantiation
	 * using lambda expressions.
	 */
	@FunctionalInterface
	public interface ResultCallBack<T> {
		void callBack(boolean successful, T result, Exception exception);
	}
}