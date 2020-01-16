package io.skymind.pathmind.ui.views.dashboard.dataprovider;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.security.SecurityUtils;

@UIScope
@SpringComponent
public class DashboardDataProvider extends AbstractBackEndDataProvider<Experiment, Void> {

	@Autowired
	private ExperimentDAO experimentDao;
	
	@Override
	protected Stream<Experiment> fetchFromBackEnd(Query<Experiment, Void> query) {
		return experimentDao.getLatestExperimentsForUser(SecurityUtils.getUserId(), query.getOffset(), query.getLimit()).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Experiment, Void> query) {
		return experimentDao.getCountExperimentsForUser(SecurityUtils.getUserId());
	}


}
