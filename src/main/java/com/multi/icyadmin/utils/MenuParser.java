package com.multi.icyadmin.utils;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class MenuParser {

    public static MenuParser instance = new MenuParser();
    private final String FILE_HEADER = "menu_file_begin";
    private final String REMOTE_LOCATION = "config/IcyAdmin";
    private final String LOCAL_LOCATION = "assets/icyadmin/menus/";
    String CURRENT_MENU = null;
    private boolean GLOBAL_FAILURE;

    public void parseMenu(String menu, boolean remote) {
        long millis = System.currentTimeMillis();
        Core.logger.info("Parsing menu " + menu + "...");
        Core.dynStorage.menus.clear();

        InputStream is = null;
        BufferedReader reader = null;
        try {

            if (!remote) {
                is = getClass().getClassLoader().getResourceAsStream(LOCAL_LOCATION + menu);
            } else {
                is = new FileInputStream(new File(REMOTE_LOCATION, menu));
            }

            if (is == null) {
                Core.logger.error(menu + " is not exists! Kill yourself!");
                return;
            }
            reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
            read(reader, menu);

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
        Core.logger.info("Menu parsing finished" + (GLOBAL_FAILURE ? " with critical error" : "") + ", took " + (float) (System.currentTimeMillis() - millis) / 1000F + " s.");
    }

    public boolean checkAndParseCustom() {
        File f = new File(REMOTE_LOCATION, "custom.menu");
        if (f.exists()) {
            Core.logger.info("Found custom menu file, parsing it.");
            MenuParser.instance.parseMenu("custom.menu", true);
            return true;
        }
        return false;
    }


    private void read(BufferedReader reader, String menu) {
        try {
            String line;
            boolean startReached = false;

            int lineNumber = 0;

            while ((line = reader.readLine()) != null && !GLOBAL_FAILURE) {
                lineNumber++;
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#") && !line.startsWith("//")) {
                    if (line.equals(FILE_HEADER)) {
                        Core.logger.info("File " + menu + " looks like a valid menu file, processing...");
                        startReached = true;
                    } else if (startReached) {
                        if (!this.parseLine(menu, line)) {
                            Core.logger.warn("Skipping line " + lineNumber + " of menu " + menu + ".");
                        }
                    }
                }
            }
            if (!startReached) Core.logger.error("File " + menu + " does not contains header word!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean parseLine(String menu, String line) {
        if (!line.contains(" "))
            return false;

        if (!(line.startsWith("end") || line.startsWith("begin") || line.startsWith("add") || line.startsWith("include")))
            return false;

        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line); //древняя магия со StackOverflow.com
        List<String> slist = new ArrayList<String>();
        while (m.find())
            slist.add(m.group(1).replace("\"", ""));


        if (line.startsWith("begin")) {
            if (CURRENT_MENU != null) {
                Core.logger.error("Parser failure while parsing " + menu + "! Another begin tag found while parsing " + CURRENT_MENU + " aborting!");
                fail();
            }
            String s = getTagValue("begin", slist);
            if (isStringValid(s)) CURRENT_MENU = s;
        } else if (line.startsWith("end")) {
            if (CURRENT_MENU == null) {
                Core.logger.error("Parser failure while parsing " + menu + "! end tag found, but begin is not!");
                fail();
            } else {
                String s = getTagValue("end", slist);
                if (!(isStringValid(s) && CURRENT_MENU.equals(s))) {
                    Core.logger.error("Parser failure while parsing " + menu + "! end tag found, but it is not same as begin tag!");
                    fail();
                } else CURRENT_MENU = null;
            }
        } else if (CURRENT_MENU != null) {
            ItemListNode node;

            if (line.startsWith("include")) {
                String include = getTagValue("include", slist);
                RequestEnum inc = RequestEnum.parseElement(include);
                if (!isStringValid(include)) { //unworking
                    Core.logger.error("Can't include nothing.");
                    return false;
                } else if (inc == null || !inc.isInclude()) {
                    Core.logger.error("Can't include " + include + ", WTF is that?");
                    return false;
                }
                node = ItemListNode.include(inc);
            } else {
                String as = getTagValue("as", slist);
                String name = getTagValue("add", slist);

                if (name.equals("SEPARATOR")) {
                    node = ItemListNode.separator();
                } else if (!isStringValid(as)) {
                    Core.logger.error("Line without action, this is UNACCEPTABLE!");
                    return false;
                } else if (as.equals("PAGE")) {
                    String to = getTagValue("to", slist);

                    if (!isStringValid(to)) {
                        Core.logger.error("Page element without page link!");
                        return false;
                    }

                    node = ItemListNode.create(name);
                    node.setColor(getTagHEXInt("color", slist));
                    node.setType(NodeActionsEnum.PAGE);
                    node.setCommandData(to);

                } else if (as.equals("CMD_EXEC")) {
                    String cmd = getTagValue("cmd", slist);
                    if (!isStringValid(cmd)) {
                        Core.logger.error("Command executor without command? Why?");
                        return false;
                    }
                    node = ItemListNode.create(name);
                    node.setColor(getTagHEXInt("color", slist));
                    node.setActiveColor(getTagHEXInt("activeColor", slist));
                    ListensEnum listen = ListensEnum.parseElement(getTagValue("listens", slist));
                    if (listen != null) {
                        if (listen == ListensEnum.PROP) {
                            Core.logger.warn("Command can't listen prop!");
                        } else node.setListens(listen);
                    }
                    node.setType(NodeActionsEnum.CMD_EXEC);
                    node.setCommandData(cmd);

                } else {
                    NodeActionsEnum role = NodeActionsEnum.parseElement(as);
                    if (role == null) {
                        Core.logger.error(as + " is not valid action, skipping.");
                        return false;
                    }
                    node = role == NodeActionsEnum.TITLE ? ItemListNode.title(name) : ItemListNode.create(name);
                    node.setColor(getTagHEXInt("color", slist));
                    node.setActiveColor(getTagHEXInt("activeColor", slist));
                    ListensEnum listen = ListensEnum.parseElement(getTagValue("listens", slist));
                    if (listen != null) {
                        if (!role.canListen() && listen == ListensEnum.PROP) {
                            Core.logger.warn(role + " doesn't have prop to listen!");
                        } else node.setListens(listen);
                    }
                    node.setType(role);

                }

            }

            Core.dynStorage.menus.add(MenuElement.create(CURRENT_MENU, node));

        } else {
            Core.logger.error(menu + " syntax error! " + line + " must be between begin and end tags! Skipping.");
        }

        return true;
    }

    private void fail() {
        GLOBAL_FAILURE = true;
        Core.dynStorage.menus.clear();
    }

    private boolean isStringValid(String s) {
        return s != null && !s.trim().equals("");
    }

    private int parsePositiveInt(String s) {
        int i = 0;
        if (s == null) return i;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
        }
        return i < 0 ? 0 : i;
    }

    private int parsePositiveHEXInt(String s) {
        int i = 0xFFFFFF;
        if (s == null) return i;
        try {
            i = Integer.parseInt(s.replaceAll("#", ""), 16);
        } catch (NumberFormatException ignored) {
        }
        return i < 0 ? 0xFFFFFF : i;
    }

    private String getTagValue(String tag, List<String> tagList) {
        if (tagList.contains(tag)) {
            int tagIndex = tagList.indexOf(tag);
            if (this.isIndexInList(tagIndex + 1, tagList)) return tagList.get(tagIndex + 1);
        }
        return null;
    }

    private boolean hasTag(String tag, List<String> tagList) {
        return tagList.contains(tag);
    }


    private int getTagInt(String tag, List<String> tagList) {
        String val = this.getTagValue(tag, tagList);
        if (tag != null) return this.parsePositiveInt(val);
        return 0;
    }

    private int getTagHEXInt(String tag, List<String> tagList) {
        String val = this.getTagValue(tag, tagList);
        if (tag != null) return this.parsePositiveHEXInt(val);
        return 0;
    }

    private boolean isIndexInList(int index, List list) {
        return index > 0 && index < list.size();
    }


}