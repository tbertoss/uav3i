package com.deev.interaction.uav3i.ui;


import java.awt.Polygon;
import java.awt.geom.Point2D;

import javax.swing.JComponent;


@SuppressWarnings("serial")
public class Map extends JComponent
{
	private Point2D.Double _center; // in meter (georef)
	private double _ppm = 1.; // pixels per meter
	
	public Map()
	{
		_ppm = 1.;
		_center = new Point2D.Double(0., 0.);
	}
	
	/**
	 * Translates the map (pixels coordinates).
	 * @param dx
	 * @param dy
	 */
	public void panPx(double dx, double dy)
	{
		double x = _center.x;
		double y = _center.y;
				
		_center = new Point2D.Double(x + dx/_ppm, y - dy/_ppm);
	}
	
	/**
	 * Zooms the map by a given factor, centered around a point (x, y) expressed in pixels.
	 * @param zfactor
	 * @param x
	 * @param y
	 */
	public void zoomPx(double zfactor, double x, double y)
	{
		// if (zfactor < 1. && _ppm > 10.)
		// 	return;
		
		double dx = x - getWidth()/2.;
		double dy = y - getHeight()/2.;
		
		panPx(dx, dy);
		_ppm *= zfactor;
		panPx(-dx, -dy);
	}

	public void alignWith(Map map)
	{
		Point2D.Double center = map.getCenter();
		
		_center.x = center.x;
		_center.y = center.y;
		_ppm = map.getPPM();
	}
	
	public Point2D.Double getCenter()
	{
		return _center;
	}
	
	public void setCenter(Point2D.Double center)
	{
		_center = center;
	}
	
	/**
	 * @return the scale of the map in pixels per meter.
	 */
	public double getPPM()
	{
		return _ppm;
	}
	
	public void setPPM(double ppm)
	{
		_ppm = ppm;
	}
	
	public double pixelsToMeters(double value)
	{
		return value / _ppm;
	}
	
	public Point2D.Double pixelsToMeters(double xp, double yp)
	{
		double x = xp - getWidth()/2.;
		double y = yp - getHeight()/2.;
		
		x /= _ppm;
		y /= _ppm;
		
		return new Point2D.Double(_center.x + x, _center.y - y);
	}
	
	public Point2D.Double pixelsToMeters(Point2D.Double p)
	{
		double x = p.x - getWidth()/2.;
		double y = p.y - getHeight()/2.;
		
		x /= _ppm;
		y /= _ppm;
		
		return new Point2D.Double(_center.x + x, _center.y - y);
	}
	
	public Polygon pixelsToMeters(Polygon polygon)
	{
		Polygon out = new Polygon();
		
		for (int i=0; i<polygon.npoints; i++)
		{
			Point2D.Double p = pixelsToMeters(polygon.xpoints[i], polygon.ypoints[i]);
			out.addPoint((int) p.x, (int) p.y);
		}
		
		return out;
	}
	
	public double metersToPixels(double value)
	{
		return value * _ppm;
	}
	
	public Point2D.Double metersToPixels(double xm, double ym)
	{
		double x = xm - _center.x;
		double y = ym - _center.y;
		
		x *= _ppm;
		y *= _ppm;
		
		return new Point2D.Double(getWidth()/2. + x, getHeight()/2. - y);
	}
	
	public Point2D.Double metersToPixels(Point2D.Double p)
	{
		double x = p.x - _center.x;
		double y = p.y - _center.y;
		
		x *= _ppm;
		y *= _ppm;
		
		return new Point2D.Double(getWidth()/2. + x, getHeight()/2. - y);
	}

	public Polygon metersToPixels(Polygon polygon)
	{
		Polygon out = new Polygon();
		
		for (int i=0; i<polygon.npoints; i++)
		{
			Point2D.Double p = metersToPixels(polygon.xpoints[i], polygon.ypoints[i]);
			out.addPoint((int) p.x, (int) p.y);
		}
		
		return out;
	}
}
