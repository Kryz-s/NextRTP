package io.github.krys.nextrtp.common.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LegacyParser {
    private static final Map<Character, String> REPLACES = HashMap.newHashMap(22);
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private static final char ampersandChar = '&';
    private static final String match = "<#";
    private static final String close = ">";

    static {
        REPLACES.put('0', "<black>");
        REPLACES.put('1', "<dark_blue>");
        REPLACES.put('2', "<dark_green>");
        REPLACES.put('3', "<dark_aqua>");
        REPLACES.put('4', "<dark_red>");
        REPLACES.put('5', "<dark_purple>");
        REPLACES.put('6', "<gold>");
        REPLACES.put('7', "<gray>");
        REPLACES.put('8', "<dark_gray>");
        REPLACES.put('9', "<blue>");
        REPLACES.put('a', "<green>");
        REPLACES.put('b', "<aqua>");
        REPLACES.put('c', "<red>");
        REPLACES.put('d', "<light_purple>");
        REPLACES.put('e', "<yellow>");
        REPLACES.put('f', "<white>");
        REPLACES.put('k', "<magic>");
        REPLACES.put('l', "<bold>");
        REPLACES.put('m', "<strikethrough>");
        REPLACES.put('n', "<underline>");
        REPLACES.put('o', "<italic>");
        REPLACES.put('r', "<reset>");
    }

    public static String parse(final String legacy) {
        final StringBuilder builder = new StringBuilder();
        final char[] chars = legacy.toCharArray();

        int i = 0;
        while (i < chars.length) {
            if (chars[i] == ampersandChar && i + 1 < chars.length) {
                char next = chars[i + 1];
                String replacement = REPLACES.get(next);
                if (replacement != null) {
                    builder.append(replacement);
                    i += 2;
                    continue;
                }
            }
            builder.append(chars[i]);
            i++;
        }

        String result = builder.toString();
        Matcher matcher = HEX_PATTERN.matcher(result);

        if(matcher.find()) {
            result = matcher.replaceAll(mr -> LegacyParser.match + mr.group(1) + close);
        }

        return result;
    }
}