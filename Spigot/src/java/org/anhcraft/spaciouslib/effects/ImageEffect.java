package org.anhcraft.spaciouslib.effects;

import org.anhcraft.spaciouslib.protocol.Particle;
import org.anhcraft.spaciouslib.scheduler.DelayedTask;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageEffect extends Effect {
    private BufferedImage image;
    private boolean alignCenter;
    private boolean transparent;

    public ImageEffect(Location location, BufferedImage image) {
        super(location);
        this.image = image;
        alignCenter = true;
        transparent = false;
        setParticleType(Particle.Type.REDSTONE);
        setParticleAmount(this.image.getWidth() * this.image.getHeight());
    }

    public ImageEffect(Location location, File file) {
        super(location);
        try {
            this.image = ImageIO.read(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
        alignCenter = true;
        transparent = false;
        setParticleType(Particle.Type.REDSTONE);
        setParticleAmount(this.image.getWidth() * this.image.getHeight());
    }

    public ImageEffect(Location location, InputStream input) {
        super(location);
        try {
            this.image = ImageIO.read(input);
        } catch(IOException e) {
            e.printStackTrace();
        }
        alignCenter = true;
        transparent = false;
        setParticleType(Particle.Type.REDSTONE);
        setParticleAmount(this.image.getWidth() * this.image.getHeight());
    }

    @Override
    public void spawn() {
        new DelayedTask(() -> {
            int w = image.getWidth();
            int h = image.getHeight();
            double ratio = safeDivide(w * h, particleAmount);
            double ratioX = safeDivide(w, h) * ratio;
            double ratioY = safeDivide(h, w) * ratio;
            for(int x = 0; x < w; x++){
                for(int y = 0; y < h; y++){
                    Color color = new Color(image.getRGB(x, y));
                    if(transparent && (color.getRGB() >> 24) == 0x00) {
                        continue;
                    }
                    Location loc = location.clone();
                    if(alignCenter) {
                        loc.setX(loc.getX() - (w / 2));
                        loc.setY(loc.getY() - (h / 2));
                    }
                    Vector vec = rotate(new Vector(ratioX * x, ratioY * y, 0));
                    loc = location.clone().add(vec);
                    spawnParticle(loc, color);
                }
            }
        }, 0).run();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setImage(File file) {
        try {
            this.image = ImageIO.read(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void setImage(InputStream input) {
        try {
            this.image = ImageIO.read(input);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlignCenter() {
        return alignCenter;
    }

    public void setAlignCenter(boolean alignCenter) {
        this.alignCenter = alignCenter;
    }

    public void setImageSize(int width, int height){
        Image img = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bf = new BufferedImage(width, height, Image.SCALE_REPLICATE);
        bf.getGraphics().drawImage(img, 0, 0, null);
        image = bf;
        setParticleAmount(width * height);
    }

    public void setImageSize(double ratio){
        setImageSize((int) (image.getWidth() * ratio), (int) (image.getHeight() * ratio));
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
}
