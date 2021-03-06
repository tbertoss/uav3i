package com.deev.interaction.uav3i.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

public class CircleMnvr extends Manoeuver
{
	private Point2D.Double _centerm;
	private SymbolMap _smap;
	private double _currentRm = 80.;
	private double _lastRm;
	
	static double RPX = 10.;
	
	public CircleMnvr(SymbolMap map, double x, double y)
	{
		_centerm = new Point2D.Double(x, y);
		_smap = map;
	}
	
	@Override
	public void paint(Graphics2D g2)
	{
		AffineTransform old = g2.getTransform();
		
		Point2D.Double centerPx = _smap.metersToPixels(_centerm);
		g2.translate(centerPx.x, centerPx.y);
		
		g2.setStroke(new BasicStroke(4.f));
		g2.setPaint(new Color(1.0f, 0.f, 0.f, 1.0f));
		Ellipse2D.Double ell = new Ellipse2D.Double(-RPX, -RPX, 2*RPX, 2*RPX);
		g2.draw(ell);
		g2.setPaint(new Color(1.0f, 0.1f, 0.1f, 0.2f));
		g2.fill(ell);
				
		double Rpx = _smap.getPPM() * _currentRm;
		
		ell = new Ellipse2D.Double(-Rpx, -Rpx, 2*Rpx, 2*Rpx);
		
		g2.setStroke(new BasicStroke(2.f*(float)GRIP));
		g2.setPaint(new Color(1.0f, 1.0f, 1.0f, 0.5f));
		g2.draw(ell);
		
		g2.setStroke(new BasicStroke(4.f));
		g2.setPaint(new Color(0.0f, 0.f, 1.0f, 1.0f));
		g2.draw(ell);
		
		g2.setTransform(old);
	}
	
	@Override
	public boolean adjustAtPx(double x, double y)
	{
		Point2D.Double centerPx = _smap.metersToPixels(_centerm);
		double Rm = centerPx.distance(new Point2D.Double(x, y))/_smap.getPPM();
		
		if (_adjusting)
		{
			_currentRm += Rm - _lastRm;
			_lastRm = Rm;
			
			if (_currentRm < 2.*RPX/_smap.getPPM())
				_currentRm = 2.*RPX/_smap.getPPM();
			
			return true;
		}
		
		if (Math.abs(_currentRm-Rm) < GRIP/_smap.getPPM())
		{
			_lastRm = Rm;
			_adjusting = true;
		}

		return _adjusting;
	}
}
