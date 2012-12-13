package com.jault.jplot.tests;

import org.junit.Assert;
import org.junit.Test;

import com.jault.jplot.PlotPanel;

public class PlotPanelTest {

	@Test
	public void testRound() {
		PlotPanel p = new PlotPanel();
		Assert.assertEquals(0, p.round(0));
		Assert.assertEquals(0, p.round(1));
		Assert.assertEquals(0, p.round(2));
		Assert.assertEquals(0, p.round(3));
		Assert.assertEquals(0, p.round(4));
		Assert.assertEquals(0, p.round(5));
		Assert.assertEquals(0, p.round(6));
		Assert.assertEquals(12, p.round(7));
		Assert.assertEquals(12, p.round(8));
		Assert.assertEquals(12, p.round(9));
		Assert.assertEquals(12, p.round(10));
		Assert.assertEquals(12, p.round(11));
		Assert.assertEquals(12, p.round(12));
		Assert.assertEquals(12, p.round(13));
		Assert.assertEquals(12, p.round(14));
		Assert.assertEquals(12, p.round(15));
		Assert.assertEquals(12, p.round(16));
		Assert.assertEquals(12, p.round(17));
		Assert.assertEquals(12, p.round(18));
		Assert.assertEquals(24, p.round(19));
		Assert.assertEquals(24, p.round(20));
		Assert.assertEquals(24, p.round(21));
		Assert.assertEquals(24, p.round(22));
		Assert.assertEquals(24, p.round(23));
		Assert.assertEquals(24, p.round(24));
	}

}
