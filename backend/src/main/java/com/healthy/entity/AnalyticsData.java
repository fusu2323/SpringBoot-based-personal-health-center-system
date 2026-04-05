package com.healthy.entity;

import lombok.Data;
import java.util.List;

@Data
public class AnalyticsData {
    private List<String> dates;           // x-axis date labels (e.g., ["2026-04-01", "2026-04-02", ...])
    private List<Double> values;          // raw values per date for chart line
    private Double avg;                  // average over period
    private Double min;                  // minimum value over period
    private Double max;                  // maximum value over period
    private Double total;                // sum over period (for totals like total workouts)
    private Long count;                  // number of records
}
