package me.lauriichan.minecraft.fabric.common.abuilderslife.message;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public final class ComponentParser {

    private ComponentParser() {
        throw new UnsupportedOperationException();
    }

    private static final TextColor INVALID_COLOR = TextColor.fromFormatting(Formatting.WHITE);
    private static final TextColor DEFAULT_COLOR = TextColor.fromFormatting(Formatting.GRAY);

    public static MutableText parse(final String richString) {
        return parse(richString, DEFAULT_COLOR);
    }

    public static MutableText parse(final String richString, final Formatting defaultColor) {
        return parse(richString, (defaultColor == null || defaultColor.isModifier()) ? DEFAULT_COLOR : TextColor.fromFormatting(defaultColor));
    }

    public static MutableText parse(final String richString, final TextColor defaultColor) {
        final MutableText output = Text.empty();
        Style style = Style.EMPTY.withColor(defaultColor);
        StringBuilder builder = new StringBuilder();
        final char[] chars = richString.toCharArray();
        char chr;
        for (int index = 0; index < chars.length; index++) {
            chr = chars[index];
            if (chr != '&') {
                builder.append(chr);
                continue;
            }
            if (index + 1 >= chars.length) {
                builder.append(chr);
                continue;
            }
            if (chars[index + 1] != '#' || index + 2 >= chars.length) {
                final Formatting format = Formatting.byCode(chr);
                if (format == null) {
                    builder.append(chr);
                    continue;
                }
                index++;
                if (builder.length() > 0) {
                    output.append(Text.literal(builder.toString()).fillStyle(style));
                    builder = new StringBuilder();
                }
                if (format == Formatting.RESET) {
                    style = Style.EMPTY.withColor(defaultColor);
                    continue;
                }
                style = style.withFormatting(format);
                continue;
            }
            StringBuilder hex = new StringBuilder("#");
            int maxLength = Math.min(chars.length - index, 7);
            if (maxLength == 2) {
                index += 2;
                hex.append(chars[index]);
                hex.append(chars[index]);
                hex.append(chars[index]);
                hex.append(chars[index]);
                hex.append(chars[index]);
                hex.append(chars[index]);
            } else if(maxLength == 4) {
                index += 2;
                hex.append(chars[index]);
                hex.append(chars[index]);
                hex.append(chars[index + 1]);
                hex.append(chars[index + 1]);
                hex.append(chars[index + 2]);
                hex.append(chars[index + 2]);
            } else if(maxLength == 7) {
                index += 2;
                hex.append(chars[index]);
                hex.append(chars[index + 1]);
                hex.append(chars[index + 2]);
                hex.append(chars[index + 3]);
                hex.append(chars[index + 4]);
                hex.append(chars[index + 5]);
            } else {
                builder.append(chr);
                continue;
            }
            if (builder.length() > 0) {
                output.append(Text.literal(builder.toString()).fillStyle(style));
                builder = new StringBuilder();
            }
            TextColor color = TextColor.parse(hex.toString());
            style = style.withColor(color == null ? INVALID_COLOR : color);
        }
        if (builder.length() > 0) {
            output.append(Text.literal(builder.toString()).fillStyle(style));
        }
        return output;
    }

}
