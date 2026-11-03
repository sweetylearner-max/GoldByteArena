package com.goldbyte.arena.config;

/** Escapes text before it is printed in a JSP, so user input can never inject HTML or scripts. */
public class Html {
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
