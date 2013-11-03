package cn.buaa.myweixin.apiutils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import cn.buaa.myweixin.listener.ResponseListener;
import cn.buaa.myweixin.utils.HttpTools;
import cn.buaa.myweixin.utils.HttpTools.HttpListener;
import cn.buaa.myweixin.utils.LocationTools;
import cn.buaa.myweixin.utils.StreamTools;

public class MCTools {

	private static Account nowAccount;

	private static List<Friend> newFriends;

	private static Handler MCHandler = new Handler();

	private static final String DOMAIN = "http://192.168.1.102:8071";

	private static String lasturl;
	private static Map<String, String> lastparam;
	private static long lasttime;

	public static void ajax(Activity activity, final String url,
			final Map<String, String> param, boolean lock, final int method,
			final int timeout, final ResponseListener responseListener) {
		boolean hasNetwork = HttpTools.hasNetwork(activity);

		if (lock) {
			if ((url.equals(lasturl) && param.equals(lastparam))
					&& new Date().getTime() - lasttime < 5000) {
				return;
			}
		}
		lasturl = url;
		lastparam = param;
		lasttime = new Date().getTime();

		if (!hasNetwork) {
			responseListener.noInternet();
		} else {
			new Thread() {
				@Override
				public void run() {
					super.run();
					HttpListener httpListener = new HttpListener() {

						@Override
						public void handleInputStream(InputStream is) {
							try {
								if (is != null) {
									byte[] b = StreamTools.isToData(is);
									final JSONObject data = new JSONObject(
											new String(b));
									if (data != null) {
										String info = data.getString("提示信息");
										info = info.substring(
												info.length() - 2,
												info.length());

										if (info.equals("成功")) {
											MCHandler.post(new Runnable() {
												@Override
												public void run() {
													responseListener
															.success(data);
												}
											});
										}
										if (info.equals("失败")) {
											MCHandler.post(new Runnable() {
												@Override
												public void run() {
													responseListener
															.unsuccess(data);
												}
											});
										}
									}
								}
								if (is == null) {
									MCHandler.post(new Runnable() {
										@Override
										public void run() {
											responseListener.failed();
										}
									});
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					};
					if (method == HttpTools.SEND_GET) {
						HttpTools.sendGet(DOMAIN + url, timeout, param,
								httpListener);
					}
					if (method == HttpTools.SEND_POST) {
						HttpTools.sendPost(DOMAIN + url, timeout, param,
								httpListener);
					}
				}
			}.start();
		}
	}

	public static Map<String, String> getParamsWithLocation(Activity activity) {
		double[] location = LocationTools.getLocation(activity);
		Map<String, String> map = new HashMap<String, String>();
		map.put("latitude", String.valueOf(location[1]));
		map.put("longitude", String.valueOf(location[0]));
		return map;
	}

	public static List<Friend> getNewFriends() {
		return newFriends;
	}

	public static void setNewFriends(JSONArray accounts) {
		List<Friend> newFriends = new ArrayList<Friend>();
		for (int i = 0; i < accounts.length(); i++) {
			try {
				Friend friend = new Friend(accounts.getJSONObject(i));
				newFriends.add(friend);
			} catch (JSONException e) {
				// e.printStackTrace();
			}
		}
		MCTools.newFriends = newFriends;
	}

	public static void saveAccount(Activity activity, Account account) {

		MCTools.nowAccount = account;

		try {
			OutputStream os = activity.openFileOutput("account",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(account);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Account getLoginedAccount(Activity activity) {
		Account account = MCTools.nowAccount;
		if (account != null) {
			return account;
		}
		try {
			InputStream is = activity.openFileInput("account");
			ObjectInputStream ois = new ObjectInputStream(is);
			account = (Account) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return account;
	}

	public static String createAccessKey() {

		String[] strs = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9" };
		int count = 20;
		String str = "";
		for (int i = 0; i < 9; i++) {
			str += strs[(int) Math.floor(Math.random() * strs.length)];
		}
		str += "a";
		for (int i = 0; i < count - 10; i++) {
			str += strs[(int) Math.floor(Math.random() * strs.length)];
		}
		return str;
	}

	public static void saveFriends(Activity activity, JSONArray friends) {
		DBManager dbManager = new DBManager(activity);
		dbManager.add(friends);
		dbManager.closeDB();
	}

	public static List<Circle> getCircles(Activity activity) {
		List<Circle> circles = new ArrayList<Circle>();
		DBManager dbManager = new DBManager(activity);
		circles = dbManager.queryCircle();
		dbManager.closeDB();
		return circles;
	}

	public static List<Friend> getFriends(Activity activity, int rid) {
		List<Friend> accounts = new ArrayList<Friend>();
		DBManager dbManager = new DBManager(activity);
		accounts = dbManager.queryFriends(rid);
		dbManager.closeDB();
		return accounts;
	}

	public static Map<String, List<Friend>> getCirclesFriends(
			Activity activity, JSONArray friends) {
		Map<String, List<Friend>> map = new HashMap<String, List<Friend>>();
		DBManager dbManager = new DBManager(activity);
		map = dbManager.getCirclesFriends();
		dbManager.closeDB();
		return map;
	}

}

class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "mc.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS relation"
				+ "(rid INTEGER, uid INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS circle"
				+ "(rid INTEGER PRIMARY KEY, name VARCHAR, fuid INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS friend"
				+ "(uid INTEGER PRIMARY KEY, nickName VARCHAR, head VARCHAR, phone VARCHAR, mainBusiness TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE circle ADD COLUMN other STRING");
		db.execSQL("ALTER TABLE friend ADD COLUMN other STRING");
		db.execSQL("ALTER TABLE relation ADD COLUMN other STRING");
	}
}

class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public void add(JSONArray friends) {
		delete();
		db.beginTransaction(); // 开始事务
		try {
			for (int i = 0; i < friends.length(); i++) {
				Circle circle = new Circle(friends.getJSONObject(i));
				if (circle.getRid() != 0) {
					db.execSQL("INSERT INTO circle VALUES(?, ?, ?)",
							new Object[] { circle.getRid(), circle.getName(),
									MCTools.getLoginedAccount(null).getUid() });
				} else {
					db.execSQL("INSERT INTO circle VALUES(?, ?, ?)",
							new Object[] {
									-MCTools.getLoginedAccount(null).getUid(),
									"没有分组",
									MCTools.getLoginedAccount(null).getUid() });
				}
				JSONArray accounts = friends.getJSONObject(i).getJSONArray(
						"accounts");
				for (int j = 0; j < accounts.length(); j++) {
					Friend friend = new Friend(accounts.getJSONObject(j));
					db.execSQL(
							"INSERT INTO friend VALUES(?, ?, ?, ?,?)",
							new Object[] { friend.getUid(),
									friend.getNickName(), friend.getHead(),
									friend.getPhone(),
									friend.getMainBusiness()});
					if (circle.getRid() != 0) {
						db.execSQL(
								"INSERT INTO relation VALUES(?,?)",
								new Object[] { circle.getRid(), friend.getUid() });
					} else {
						db.execSQL("INSERT INTO relation VALUES(?,?)",
								new Object[] {
										-MCTools.getLoginedAccount(null)
												.getUid(), friend.getUid() });
					}
				}

			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} catch (JSONException e) {
			// e.printStackTrace();
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public List<Circle> queryCircle() {
		List<Circle> circles = new ArrayList<Circle>();
		Cursor c = queryCircleCursor();
		while (c.moveToNext()) {
			Circle circle = new Circle();
			circle.setRid(c.getInt(c.getColumnIndex("rid")));
			circle.setName(c.getString(c.getColumnIndex("name")));
			circles.add(circle);
		}
		;
		return circles;

	}

	private Cursor queryCircleCursor() {
		Cursor c = db.rawQuery("SELECT rid,name FROM circle WHERE fuid = ?",
				new String[] { String.valueOf(MCTools.getLoginedAccount(null)
						.getUid()) });
		return c;
	}

	public List<Friend> queryFriends(int rid) {
		List<Friend> friends = new ArrayList<Friend>();
		Cursor c = queryFriendsCursor(rid);
		while (c.moveToNext()) {
			Friend friend = new Friend();
			friend.setNickName(c.getString(c.getColumnIndex("nickName")));
			friend.setHead(c.getString(c.getColumnIndex("head")));
			friend.setPhone(c.getString(c.getColumnIndex("phone")));
			friend.setMainBusiness(c.getString(c.getColumnIndex("mainBusiness")));
			friends.add(friend);
		}
		;
		return friends;
	}

	private Cursor queryFriendsCursor(int rid) {
		Cursor c = db
				.rawQuery(
						"SELECT nickName,head,phone,mainBusiness FROM friend WHERE uid IN (select uid from relation where rid=?)",
						new String[] { String.valueOf(rid) });
		return c;
	}

	public Map<String, List<Friend>> getCirclesFriends() {
		Map<String, List<Friend>> map = new HashMap<String, List<Friend>>();
		List<Circle> circles = queryCircle();
		for (Circle circle : circles) {
			map.put(circle.getName(), queryFriends(circle.getRid()));
		}
		return map;
	}

	private void delete() {
		List<Circle> circles = queryCircle();
		db.beginTransaction();
		try {
			for (Circle circle : circles) {
				db.execSQL(
						"DELETE FROM friend WHERE uid IN(select uid from relation where rid=?)",
						new Object[] { circle.getRid() });
				db.execSQL("DELETE FROM relation WHERE rid=?",
						new Object[] { circle.getRid() });
			}
			db.execSQL("DELETE FROM circle WHERE fuid=?",
					new Object[] { MCTools.getLoginedAccount(null).getUid() });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void closeDB() {
		db.close();
	}
}
