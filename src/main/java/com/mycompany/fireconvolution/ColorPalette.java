package com.mycompany.fireconvolution;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author rvenr
 */
public class ColorPalette {

    ArrayList<Color> palette = new ArrayList<>();

    Color[] defaultColor = {
        new Color(0, 0, 0, 0),
        new Color(225, 32, 0, 150),
        new Color(238, 130, 23, 175),
        new Color(255, 227, 45, 200),
        new Color(255, 255, 255, 255)
    };

    Color[] inverted = {
        new Color(0, 0, 0, 0),
        new Color(0, 0, 102, 200),
        new Color(0, 142, 187, 175),
        new Color(0, 255, 255, 150),
        new Color(255, 255, 255, 255)
    };

    Color[] bubbleGum = {
        new Color(0, 0, 0, 0),
        new Color(255, 0, 102, 150),
        new Color(255, 85, 130, 175),
        new Color(255, 115, 140, 200),
        new Color(255, 255, 255, 255)
    };
    
        Color[] phantom = {
        new Color(0, 0, 0, 0),
        new Color(0, 40, 60, 150),
        new Color(30, 130, 80, 175),
        new Color(60, 240, 170, 200),
        new Color(255, 255, 255, 255)
    };

    Color[] colors = defaultColor;

    public ColorPalette() {
        usePalette(colors);
    }

    private void usePalette(Color[] colors) {
        for (int i = 0; i < colors.length - 1; i++) {
            interpolate(colors[i], colors[i + 1]);
        }
    }

    private void interpolate(Color startColor, Color finishColor) {
        double startA = startColor.getAlpha();
        double startR = startColor.getRed();
        double startG = startColor.getGreen();
        double startB = startColor.getBlue();

        double finishA = finishColor.getAlpha();
        double finishR = finishColor.getRed();
        double finishG = finishColor.getGreen();
        double finishB = finishColor.getBlue();

        int blends = 10;

        double blendingA;
        double blendingR;
        double blendingG;
        double blendingB;

        for (int i = 0; i <= blends; i++) {
            blendingA = (finishA - startA) / (blends + 1);
            blendingR = (finishR - startR) / (blends + 1);
            blendingG = (finishG - startG) / (blends + 1);
            blendingB = (finishB - startB) / (blends + 1);

            int resultA = (int) Math.round(startA + (blendingA * i));
            int resultR = (int) Math.round(startR + (blendingR * i));
            int resultG = (int) Math.round(startG + (blendingG * i));
            int resultB = (int) Math.round(startB + (blendingB * i));

            Color result = new Color(resultR, resultG, resultB, resultA);
            this.palette.add(result);
        }

    }

    public Color getColor(int temperature) {
        int index = Math.round(temperature / (this.colors.length + 1));
        return palette.get(index);
    }

    public void setColors(String option) {
        this.palette = new ArrayList<>();

        if (null != option) {
            switch (option) {
                case "Default":
                    this.colors = this.defaultColor;
                    break;
                case "Phantom":
                    this.colors = this.phantom;
                    break;
                case "Inverted":
                    this.colors = this.inverted;
                    break;
                case "BubbleGum":
                    this.colors = this.bubbleGum;
                    break;
                default:
                    break;
            }
        }
        usePalette(colors);
    }

    public void setColors(Color[] colors_temp) {
        this.palette = new ArrayList<>();

        Color color1 = new Color(colors_temp[0].getRed(), colors_temp[0].getGreen(), colors_temp[0].getBlue(), 0);
        Color color2 = new Color(colors_temp[1].getRed(), colors_temp[1].getGreen(), colors_temp[1].getBlue(), 150);
        Color color3 = new Color(colors_temp[2].getRed(), colors_temp[2].getGreen(), colors_temp[2].getBlue(), 175);
        Color color4 = new Color(colors_temp[3].getRed(), colors_temp[3].getGreen(), colors_temp[3].getBlue(), 200);
        Color color5 = new Color(colors_temp[4].getRed(), colors_temp[4].getGreen(), colors_temp[4].getBlue(), 255);

        Color[] colorsFinal = {
            color1,
            color2,
            color3,
            color4,
            color5
        };
        usePalette(colorsFinal);
    }
}
