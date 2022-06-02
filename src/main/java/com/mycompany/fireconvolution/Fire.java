package com.mycompany.fireconvolution;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author rvenr
 */
public final class Fire {

    boolean running;
    boolean paused;
    int fps;
    Thread fireThread;
    BufferedImage image;
    BufferedImage defaultImg;

    public Fire(FireConvolution fireConvolution) {
        this.fireConvolution = fireConvolution;
        try {
            this.defaultImg = ImageIO.read(new File("src\\images\\blackBG.png"));
        } catch (IOException ex) {
            System.out.println(ex);
        }
        this.colorPalette = new ColorPalette();
        newThread();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    FireConvolution fireConvolution;
    ColorPalette colorPalette;

    int WIDTH;
    int HEIGHT;
    int[][] temperature;

    BufferedImage flame_i;
    byte[] buffer;
    byte[] imageBuffer;

    float cooling = 0.1f;
    int sparkChance = 30;

    Random rand = new Random();

    int tolerance = 100;

    boolean applyConv = false;

    public void setApplyConv(boolean applyConv) {
        this.flame_i = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.applyConv = applyConv;
    }

    public BufferedImage getFlame_i() {
        return flame_i;
    }

    public void setCooling(double cooling) {

        this.cooling = (float) cooling;
    }

    public void setSparkChance(int sparkChance) {
        this.sparkChance = sparkChance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public void resetTemperature() {
        this.temperature = new int[WIDTH][HEIGHT];
    }

    public void flameEvolve() {
        calculate(this.temperature);

        for (int x = 0; x < temperature.length; x++) {
            for (int y = 0; y < temperature[0].length; y++) {
                flame_i.setRGB(x, y, colorPalette.getColor(temperature[x][y]).getRGB());
            }
        }
    }

    public void createSparks() {
        for (int x = 0; x < temperature.length; x++) {
            for (int y = 0; y < temperature[0].length; y++) {

                if (y == this.HEIGHT - 2) {
                    int random = rand.nextInt(100);

                    if (random <= sparkChance) {
                        temperature[x][y] = 255;
                    }
                }
            }
        }
    }

    public void imageSparks() {
        Color pixel_color;

        for (int x = 0; x < temperature.length; x++) {
            for (int y = 0; y < temperature[0].length; y++) {
                pixel_color = new Color(image.getRGB(x, y), true);

                if (pixel_color.getRed() + pixel_color.getGreen() + pixel_color.getBlue() / 3 >= tolerance) {
                    int random = rand.nextInt(100);

                    if (random <= sparkChance) {
                        temperature[x][y] = 255;
                    }
                }
            }
        }
    }

    private void calculate(int[][] temperature) {
        int up;
        int left;
        int right;
        int down;

        int[][] temp = new int[temperature.length][temperature[0].length];

        for (int i = 1; i < temperature.length - 1; i++) {
            for (int j = 1; j < temperature[0].length - 1; j++) {
                up = temperature[i - 1][j];
                left = temperature[i][j - 1];
                right = temperature[i][j + 1];
                down = temperature[i + 1][j];

                int avg = (up + left + right + down) / 4;
                avg -= cooling;

                if (avg < 0) {
                    avg = 0;
                }
                temp[i][j - 1] = avg;
            }
        }
        System.arraycopy(temp, 0, temperature, 0, temp.length);
    }

    public void newThread() {
        fireThread = new Thread(() -> {
            this.image = fireConvolution.viewer.getConvBg();

            this.WIDTH = image.getWidth();
            this.HEIGHT = image.getHeight();

            this.temperature = new int[WIDTH][HEIGHT];

            this.imageBuffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

            this.flame_i = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            this.buffer = ((DataBufferByte) flame_i.getRaster().getDataBuffer()).getData();

            while (running) {
                try {
                    if (!paused) {
                        if (applyConv) {
                            this.image = fireConvolution.viewer.getConvBg();
                            imageSparks();
                        } else {
                            createSparks();
                        }
                        flameEvolve();
                    }
                    try {
                        Thread.sleep(1000 / fps);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (Exception ex) {
                }
            }
            System.out.println("a");
            this.flame_i = this.defaultImg;
        });
    }
}
