package com.multi.icyadmin.data;

import java.util.*;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class DynamicStorage {
    public boolean isPermissionsProvided;
    public ArrayList<MenuElement> menus = new ArrayList<MenuElement>();
    public HashMap<String, String> vars_cache = new HashMap<String, String>();
    public List<String> admin_logs = new ArrayList<String>();
    public List<String> last_deads = new ArrayList<String>();
    public Set<String> permissed_users = new HashSet<String>();
}
