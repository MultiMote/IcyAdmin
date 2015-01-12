package com.multi.icyadmin.utils;

import com.multi.icyadmin.Core;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MultiMote on 08.01.2015.
 */
public class FileProcessor {
    public static final String VARS_FILE = "config/IcyAdmin/variables.cache";
    public static final String USERS_FILE = "config/IcyAdmin/permissed.users";
    public static final String ADMIN_LOG_FILE = "config/IcyAdmin/admins.log";
    public static final String DEATH_LOG_FILE = "config/IcyAdmin/death.log";
    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm-dd.MM");

    public static void writeVars() {
        BufferedWriter writer = null;
        File remote = new File(VARS_FILE);
        try {
            remote.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(remote));
            for (String s : Core.dynStorage.vars_cache.keySet()) {
                writer.write(s + "$" + Core.dynStorage.vars_cache.get(s));
                writer.newLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readVars() {

        InputStream is = null;
        BufferedReader reader = null;
        File remote = new File(VARS_FILE);
        if (!remote.exists()) return;

        Core.logger.info("Reading variables cache...");

        try {
            is = new FileInputStream(remote);
            reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null) {
                if (!s.isEmpty() && s.contains("$")) {
                    String[] splitted = s.split("\\$");
                    Core.dynStorage.vars_cache.put(splitted[0], splitted[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void appendToDeathLog(String str) {
        Date date = new Date();
        String s = "[" + dateFormat.format(date) + "]: " + str;
        Core.dynStorage.last_deads.add(s);
        if (Core.dynStorage.last_deads.size() > 50) Core.dynStorage.last_deads.remove(0);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DEATH_LOG_FILE, true)));
            out.println(s);
            out.close();
        } catch (IOException ignored) {
        }
    }

    public static void appendToAdminLog(String str) {
        Date date = new Date();
        String s = "[" + dateFormat.format(date) + "]: " + str;
        Core.dynStorage.admin_logs.add(s);
        if (Core.dynStorage.admin_logs.size() > 50) Core.dynStorage.admin_logs.remove(0);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ADMIN_LOG_FILE, true)));
            out.println(s);
            out.close();
        } catch (IOException ignored) {
        }
    }

    public static void writeUsers() {
        BufferedWriter writer = null;
        File remote = new File(USERS_FILE);
        try {
            remote.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(remote));
            for (String s : Core.dynStorage.permissed_users) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readUsers() {

        InputStream is = null;
        BufferedReader reader = null;
        File remote = new File(USERS_FILE);
        if (!remote.exists()) return;

        Core.logger.info("Reading users...");

        try {
            is = new FileInputStream(remote);
            reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null) {
                if (!s.isEmpty()) {
                    Core.dynStorage.permissed_users.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
