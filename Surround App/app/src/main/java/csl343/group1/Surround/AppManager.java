/**
 * Author: Aditya Abhas
 * Entry No: 2012csb1002
 *
 * This is executed on app launch.
 * In this class, volley core objects are initiated.
 * */

package csl343.group1.Surround;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppManager extends Application {

	public static final String name = AppManager.class.getSimpleName();
	private RequestQueue queue;
	private static AppManager manager;

	@Override
	public void onCreate() {
		super.onCreate();
		manager = this;
	}

	public static synchronized AppManager getInstance() {
		return manager;
	}

	public RequestQueue getRequestQueue() {
		if (queue == null) {
			queue = Volley.newRequestQueue(getApplicationContext());
		}

		return queue;
	}

	public <T> void addToRequestQueue(Request<T> request, String tag) {
		request.setTag(TextUtils.isEmpty(tag) ? name : tag);
		getRequestQueue().add(request);
	}

	public <T> void addToRequestQueue(Request<T> request) {
		request.setTag(name);
		getRequestQueue().add(request);
	}

	public void cancelPendingRequests(Object requestTag) {
		if (queue != null) {
			queue.cancelAll(requestTag);
		}
	}
}