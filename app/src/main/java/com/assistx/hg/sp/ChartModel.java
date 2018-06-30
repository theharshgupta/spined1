package com.assistx.hg.sp;

import java.util.ArrayList;
import java.util.List;

public class ChartModel {
    private List<ChartDataModel> chartData = new ArrayList<>(0);

    private String chartType;

    public List<ChartDataModel> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartDataModel> chartData) {
        this.chartData = chartData;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
}
