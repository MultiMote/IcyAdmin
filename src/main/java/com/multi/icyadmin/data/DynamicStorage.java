package com.multi.icyadmin.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class DynamicStorage {
    public boolean isPermissionsProvided;
    public ArrayList<MenuElement> menus = new ArrayList<MenuElement>();
    public HashMap<String, String> vars_cache = new HashMap<String, String>();
    public Set<String> permissed_users = new HashSet<String>();
}
