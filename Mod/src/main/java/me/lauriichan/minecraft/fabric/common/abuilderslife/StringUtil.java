package me.lauriichan.minecraft.fabric.common.abuilderslife;

/**
 * <a
 * href=https://github.com/Lauriichan/LayLib/blob/master/logger/src/main/java/me/lauriichan/laylib/logger/util/StringUtil.java>
 * Source </a>
 */

public final class StringUtil {

    public static String format(String message, Object[] placeholders) {
        if (placeholders.length == 0) {
            return message;
        }
        StringBuilder output = new StringBuilder();
        char[] chars = message.toCharArray();
        StringBuilder buffer = new StringBuilder();
        int state = 0;
        for (int index = 0; index < chars.length; index++) {
            char current = chars[index];
            switch (current) {
            case '{':
                if (state != 0) {
                    output.append('{');
                    if (buffer.length() != 0) {
                        output.append(buffer);
                        buffer = new StringBuilder();
                    }
                }
                state = 1;
                break;
            case '}':
                if (state != 2) {
                    output.append('{');
                    if (buffer.length() != 0) {
                        output.append(buffer);
                        buffer = new StringBuilder();
                    }
                    state = 0;
                    break;
                }
                try {
                    int idx = Integer.parseInt(buffer.toString());
                    if (idx >= placeholders.length || idx < 0) {
                        output.append('{').append(buffer).append('}');
                        break;
                    }
                    output.append(placeholders[idx]);
                    break;
                } catch (NumberFormatException nfe) {
                    output.append('{').append(buffer).append('}');
                    break;
                } finally {
                    buffer = new StringBuilder();
                    state = 0;
                }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                if (state == 0) {
                    output.append(current);
                    break;
                }
                state = 2;
                buffer.append(current);
                break;
            default:
                output.append(current);
                break;
            }
        }
        return output.toString();
    }

}
