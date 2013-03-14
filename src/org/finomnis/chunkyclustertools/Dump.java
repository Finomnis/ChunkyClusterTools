package org.finomnis.chunkyclustertools;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class Dump {

	private double data[];
	private long time;
	private int spp;
	private int width;
	private int height;

	public Dump(int x, int y) {
		init(x, y);
	}

	public Dump(File dumpFile) throws IOException {
		DataInputStream inStream = new DataInputStream(new GZIPInputStream(
				new BufferedInputStream(new FileInputStream(dumpFile))));

		width = inStream.readInt();
		height = inStream.readInt();
		spp = inStream.readInt();
		time = inStream.readLong();

		data = new double[width * height * 3];

		for (int i = 0; i < data.length; i++) {
			data[i] = inStream.readDouble();
			
			//System.out.println(data[i]);
		}

		inStream.close();

	}
	
	public void writeToDumpFile(File dumpFile) throws IOException
	{
		
		DataOutputStream outStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(dumpFile))));
		
		outStream.writeInt(width);
		outStream.writeInt(height);
		outStream.writeInt(spp);
		outStream.writeLong(time);
		
		for(int i = 0; i < data.length; i++)
		{
			outStream.writeDouble(data[i]);
		}
		
		outStream.close();
		
	}
	
	public void writeToPNGFile(File pngFile) throws IOException
	{
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++){
				
				double xf = data[3*(y + height*x) + 0];
				double yf = data[3*(y + height*x) + 1];
				double zf = data[3*(y + height*x) + 2];
				
				//rf = x/(double)width;
				//gf = y/(double)height;
				//bf = 0.5;
				
				
				double rf = Math.pow(xf, 1/1.42);
				double gf = Math.pow(yf, 1/1.42);
				double bf = Math.pow(zf, 1/1.42);
			    
				
				int r = (int)(255*rf+0.5);
				int g = (int)(255*gf+0.5);
				int b = (int)(255*bf+0.5);
				
				if(r < 0) r = 0;
				if(g < 0) g = 0;
				if(b < 0) b = 0;
				if(r > 255) r = 255;
				if(g > 255) g = 255;
				if(b > 255) b = 255;
				
				
				image.setRGB(x, y, (r<<16)+(g<<8)+b);
			}
		}

		ImageIO.write(image, "png", pngFile);
		
	}

	public void add(Dump other) throws Exception {
		if (this.width != other.width || this.height != other.height)
			throw new Exception("Dumps have different image sizes!");
		if (other.data.length != data.length || other.spp < 0)
			throw new Exception("Invalid dump!");

		if (other.spp < 1)
			return;

		if (this.spp < 1) {
			this.time = other.time;
			this.spp = other.spp;
			for (int i = 0; i < data.length; i++)
				this.data[i] = other.data[i];
			return;
		}

		double thisratio = this.spp / (double) (other.spp + this.spp);
		double otherratio = other.spp / (double) (other.spp + this.spp);

		for (int i = 0; i < data.length; i++) {
			this.data[i] = this.data[i] * thisratio + other.data[i]
					* otherratio;
		}

		this.time += other.time;
		this.spp += other.spp;

	}

	private void init(int x, int y) {

		this.width = x;
		this.height = y;
		this.spp = 0;
		this.time = 0;
		this.data = new double[x * y * 3];

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSPP() {
		return spp;
	}

	public long getTime() {
		return time;
	}

}
