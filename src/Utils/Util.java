package Utils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util {
    // Utility functions can be added here in the future
    public static byte[] longToBytes(long v) {
        return ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(v).array();
    }

    public static long bytesToLong(byte[] b) {
        if (b == null || b.length != 8)
            throw new IllegalArgumentException("length != 8");
        return ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    public static BufferedImage getImage(byte tileData, int tileSize) {
        if (tileData == MagicNumbers.TILE_BOMB) {
            return Util.getBombImage();
        } else if (tileData == MagicNumbers.TILE_FLAG) {
            return Util.getFlagImage();
        } else if (tileData == MagicNumbers.TILE_QUESTION) {
            return Util.getQuestionImage();
        } else if (tileData == MagicNumbers.TILE_DEPRESSED_QUESTION) {
            return Util.getDepressedQuestionImage();
        } else if (tileData == MagicNumbers.TILE_EXPLODED_BOMB) {
            return Util.getExplodedBombImage();
        } else if (tileData == MagicNumbers.TILE_WRONG_FLAG) {
            return Util.getWrongFlagImage();
        } else if (tileData != MagicNumbers.TILE_HIDDEN) {
            return Util.getNumberImage(tileData & MagicNumbers.TILE_NUMBER_MASK);
        } else {
            return Util.getHiddenImage();
        }
    }

    public static BufferedImage getHiddenImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(0, 51, 16, 16);
    }

    public static BufferedImage getEmptyImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(17, 51, 32, 32);
    }

    public static BufferedImage getFlagImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(34, 51, 32, 32);
    }

    public static BufferedImage getQuestionImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(51, 51, 32, 32);
    }

    public static BufferedImage getDepressedQuestionImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(68, 51, 32, 32);
    }

    public static BufferedImage getBombImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(85, 51, 32, 32);
    }

    public static BufferedImage getExplodedBombImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(102, 51, 32, 32);
    }

    public static BufferedImage getWrongFlagImage() {
        return MagicNumbers.SPRITE_SHEET.getSubimage(119, 51, 32, 32);
    }

    public static BufferedImage getFaceImage(int state) {
        // state: 0 - normal, 1 - pressed, 2 - holding, 3 - win, 4 - lose
        if (state < 0 || state > 4) {
            throw new IllegalArgumentException("Invalid face state: " + state);
        }
        int x = 27 * state;
        return MagicNumbers.SPRITE_SHEET.getSubimage(x, 24, 26, 26);
    }

    public static BufferedImage getTimerImage(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Invalid timer digit: " + digit);
        }
        return MagicNumbers.SPRITE_SHEET.getSubimage(14 * digit, 0, 13, 23);
    }

    public static BufferedImage getNumberImage(int number) {
        if (number < 1 || number > 8) {
            throw new IllegalArgumentException("Invalid number: " + number);
        }
        return MagicNumbers.SPRITE_SHEET.getSubimage((number - 1) * 17, 68, 32, 32);
    }
}