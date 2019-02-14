/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.List;

import forex.genetic.manager.PropertiesManager;

/**
 *
 * @author ricardorq85
 */
public class ThreadUtil {

	public static void joinThreads(List<Thread> threads) {
		joinThreads(threads.toArray(new Thread[0]));
	}

	public static void joinThreads(Thread[] threads) {
		for (Thread t : threads) {
			if ((t != null) && (t.isAlive())) {
				try {
					LogUtil.logTime("Init Waiting for Thread " + t.getName(), 1);
					t.join();
					LogUtil.logTime("End Waiting for Thread " + t.getName(), 1);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 *
	 * @param t
	 */
	public static void joinThread(Thread t) {
		joinThread(t, 0);
	}

	public static void joinThread(Thread t, long millis) {
		if ((t != null) && (t.isAlive())) {
			try {
				LogUtil.logTime("Init Waiting for Thread " + t.getName(), 1);
				t.join(millis);
				LogUtil.logTime("End Waiting for Thread " + t.getName(), 1);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param t
	 */
	public static void launchThread(Thread t) {
		if (PropertiesManager.isThread()) {
			t.start();
		} else {
			t.run();
		}
	}
}
