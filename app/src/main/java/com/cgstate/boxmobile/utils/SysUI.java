/**
 * 
 */
package com.cgstate.boxmobile.utils;

/**
 * @author Administrator
 *
 */
public class SysUI {
	
	public static void show() {
        new Thread() {
            public void run() {
                Process su = null;
                try {
                    su = Runtime.getRuntime().exec("su");                      
                    String cmd = "am startservice -n com.android.systemui/.SystemUIService\n";
                    su.getOutputStream().write(cmd.getBytes());
                    String exit = "exit\n";
                    su.getOutputStream().write(exit.getBytes());
                    su.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (su != null) {
                        su.destroy();
                    }
                }                
            };            
        }.start();
	}
	
	public static void hide() {
        new Thread() {
            public void run() {
                Process su = null;
                try {
                    su = Runtime.getRuntime().exec("su");
                    String cmd = "service call activity 42 s16 com.android.systemui\n";
                    su.getOutputStream().write(cmd.getBytes());
                    String exit = "exit\n";
                    su.getOutputStream().write(exit.getBytes());
                    su.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (su != null) {
                        su.destroy();
                    }
                }
            };
        }.start();
	}	
}
