package io.skymind.pathmind.webapp.ui.views.experiment.chart;

import com.vaadin.flow.component.charts.model.DataSeriesItem;
import io.skymind.pathmind.shared.data.RewardScore;

public class RewardScoreSeriesItem extends DataSeriesItem {
	
	private Integer episodeCount;
	
	public RewardScoreSeriesItem(RewardScore score) {
		setX(score.getIteration());
		setY(score.getMean());
		setEpisodeCount(score.getEpisodeCount());
	}

	public Integer getEpisodeCount() {
		return episodeCount;
	}

	public void setEpisodeCount(Integer episodeCount) {
		this.episodeCount = episodeCount;
		makeCustomized();
	}
	
}
