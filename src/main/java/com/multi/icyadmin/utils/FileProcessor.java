package com.multi.icyadmin.utils;

import com.multi.icyadmin.Core;

import java.io.*;

/**
 * Created by MultiMote on 08.01.2015.
 */
public class FileProcessor {
    public static final String VARS_FILE = "config/IcyAdmin/variables.cache";
    public static final String USERS_FILE = "config/IcyAdmin/permissed.users";

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
