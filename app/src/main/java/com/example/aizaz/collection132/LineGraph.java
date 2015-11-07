package com.example.aizaz.collection132;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class LineGraph {

	private GraphicalView view;
	
	private TimeSeries dataset = new TimeSeries("X");
	private TimeSeries dataset2 = new TimeSeries("Y");
	private TimeSeries dataset3 = new TimeSeries("Z");
	
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	
	private XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize line 1
	private XYSeriesRenderer renderer2 = new XYSeriesRenderer(); // This will be used to customize line 2
	private XYSeriesRenderer renderer3 = new XYSeriesRenderer(); // This will be used to customize line 3
	
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
	
	public LineGraph()
	{
		// Add single dataset to multiple dataset
		mDataset.addSeries(dataset);
		mDataset.addSeries(dataset2);
		mDataset.addSeries(dataset3);
		
		// Customization time for line 1!
		renderer.setColor(Color.BLACK);
		renderer.setPointStyle(PointStyle.SQUARE);
		renderer.setFillPoints(true);
		
		renderer2.setColor(Color.RED);
		renderer2.setPointStyle(PointStyle.POINT);
		renderer2.setFillPoints(true);
		
		renderer3.setColor(Color.GREEN);
		renderer3.setPointStyle(PointStyle.CIRCLE);
		renderer3.setFillPoints(true);

		// Enable Zoom
		mRenderer.setZoomButtonsVisible(true);
		//mRenderer.setBackgroundColor(Color.TRANSPARENT);
		
		//mRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));//imp
		
		mRenderer.setXTitle("Day #");
		mRenderer.setYTitle("CM in Rainfall");
		//mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins

		// Add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(renderer2);
		mRenderer.addSeriesRenderer(renderer3);
	}


	
	public GraphicalView getView(Context context)
	{
		view =  ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	
	public void addNewPoints(Point p,Point p2,Point p3)
	{
		dataset.add(p.getX(), p.getY());
		dataset2.add(p2.getX(), p2.getY());
		dataset3.add(p3.getX(), p3.getY());
	}
	
}
