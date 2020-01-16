package io.skymind.pathmind.ui.views.dashboard.dataprovider;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.services.DashboardItemService;

@UIScope
@SpringComponent
public class DashboardDataProvider extends AbstractBackEndDataProvider<DashboardItem, Void> {

	@Autowired
	private DashboardItemService service;
	
	@Override
	protected Stream<DashboardItem> fetchFromBackEnd(Query<DashboardItem, Void> query) {
		return service.getDashboardItemsForUser(SecurityUtils.getUserId(), query.getOffset(), query.getLimit()).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<DashboardItem, Void> query) {
		return service.getDashboardItemsForUser(SecurityUtils.getUserId(), 0, 100).size();
	}


}
