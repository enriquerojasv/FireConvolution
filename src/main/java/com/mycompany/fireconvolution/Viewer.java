package com.mycompany.fireconvolution;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 *
 * @author rvenr
 */
public class Viewer extends Canvas {

    BufferedImage convBg;
    BufferStrategy bs;
    BufferedImage bg;
    double[][] filter;
    Thread viewerThread;
    boolean running = false;
    FireConvolution fireConvolution;
    boolean first = true;

    public Viewer(FireConvolution fireConvolution) {
        this.setBackground(Color.BLACK);
        this.fireConvolution = fireConvolution;
        viewerThread = new Thread(() -> {
            paintViewer();
            while (true) {

                setBackground(bg, filter);

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    public BufferedImage getConvBg() {
        return convBg;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public void setBackground(BufferedImage img_bg, double[][] img_filter) {
        paintViewer();

        this.bg = img_bg;
        this.filter = img_filter;

        if (!running) {
            this.running = true;
            viewerThread.start();
        }

        Graphics g = bs.getDrawGraphics();

        if (bg != null) {
            g.drawImage(bg, 0, (int) (this.getHeight() * 0.33), this.getWidth(), (int) (this.getHeight() * 0.67), null);

            g.drawImage(bg, 0, 0, (int) (this.getWidth() * 0.33), (int) (this.getHeight() * 0.33), null);

            if (first == true) {
                this.convBg = new ImageConvolution(bg, filter).getNew_img();
                first = false;
            }
            g.drawImage(convBg, (int) (this.getWidth() * 0.33), 0, (int) (this.getWidth() * 0.33), (int) (this.getHeight() * 0.33), null);

            if (fireConvolution.fire.isRunning()) {
                BufferedImage fire_bg = fireConvolution.fire.getFlame_i();
                g.drawImage(fireConvolution.fire.defaultImg, (int) (this.getWidth() * 0.67), 0, (int) (this.getWidth() * 0.34), (int) (this.getHeight() * 0.33), null);
                g.drawImage(fire_bg, (int) (this.getWidth() * 0.67), 0, (int) (this.getWidth() * 0.34), (int) (this.getHeight() * 0.33), null);
                g.drawImage(fire_bg, 0, (int) (this.getHeight() * 0.33), this.getWidth(), (int) (this.getHeight() * 0.67), null);
            } else {
                g.drawImage(fireConvolution.fire.defaultImg, (int) (this.getWidth() * 0.67), 0, (int) (this.getWidth() * 0.34), (int) (this.getHeight() * 0.33), null);
            }
        }
        g.dispose();
        bs.show();
    }

    private void paintViewer() {
        BufferStrategy buffers;
        buffers = this.getBufferStrategy();
        if (buffers == null) {
            this.createBufferStrategy(3);
            buffers = this.getBufferStrategy();
        }
        if (this.getParent() != null) {
            this.bs = buffers;
        }
    }
}