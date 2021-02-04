package io.skymind.pathmind.webapp.ui.views.search.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.shared.constants.SearchResultItemType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.SearchResult;
import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import io.skymind.pathmind.shared.utils.PathmindStringUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.components.FavoriteStar;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

public class SearchResultItem extends VerticalLayout {
    private SearchResult searchResult;
    private SearchResultItemType searchResultType;
    private Span createdDateComponent;
    private Span lastActivityDateComponent;
    private Div notesComponent;
    private String decodedKeyword;
    private Boolean isArchived;
    private ExperimentDAO experimentDAO;
    private ModelService modelService;

    public SearchResultItem(ExperimentDAO experimentDAO, ModelService modelService, SearchResult item, String decodedKeyword) {
        this.searchResult = item;
        this.decodedKeyword = decodedKeyword;
        this.isArchived = searchResult.getIsArchived();
        this.experimentDAO = experimentDAO;
        this.modelService = modelService;
        searchResultType = searchResult.getItemType();
        createdDateComponent = new Span("Created");
        lastActivityDateComponent = new Span("Last Activity");
        notesComponent = createSearchResultsNotesComponent();
        add(
                createInfoRow(),
                createNameRow(),
                WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(new Span("Notes: "), notesComponent)
        );
        setSpacing(false);
        setPadding(false);
        setClassName("search-result-item");
    }

    private HorizontalLayout createInfoRow() {
        HorizontalLayout infoRow = WrapperUtils.wrapWidthFullBetweenHorizontal();
        HorizontalLayout tags = new HorizontalLayout();
        HorizontalLayout dates = new HorizontalLayout(createdDateComponent, lastActivityDateComponent);

        if (searchResultType.equals(SearchResultItemType.EXPERIMENT)) {
            Experiment experiment = experimentDAO.getExperiment(searchResult.getItemId()).orElse(null);
            if (experiment != null) {
                tags.add(new FavoriteStar(experiment.isFavorite(), newIsFavorite ->
                        ExperimentGuiUtils.favoriteExperiment(experimentDAO, experiment, newIsFavorite)));
            }
        }
        tags.add(new TagLabel(searchResultType.getName(), true, "small"));
        if (isArchived) {
            tags.add(new TagLabel("Archived", false, "small"));
        }
        infoRow.add(tags, dates);
        infoRow.addClassName("info-row");
        return infoRow;
    }

    private VerticalLayout createNameRow() {
        Boolean resultTypeProject = searchResultType.equals(SearchResultItemType.PROJECT);
        Boolean resultTypeModel = searchResultType.equals(SearchResultItemType.MODEL);
        Boolean resultTypeExperiment = searchResultType.equals(SearchResultItemType.EXPERIMENT);
        VerticalLayout nameRow = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        String modelName = searchResult.getModelName();
        String experimentName = searchResult.getExperimentName();
        String modelNameText = "Model #" + searchResult.getModelName();
        String experimentNameText = "Experiment #" + searchResult.getExperimentName();
        RouterLink projectNameLink = new RouterLink();
        nameRow.add(highlightSearchResult(projectNameLink, searchResult.getProjectName(), null, resultTypeProject));
        setProjectLinkRouterTarget(projectNameLink);
        if (resultTypeModel || resultTypeExperiment) {
            RouterLink modelNameLink = new RouterLink();
            nameRow.add(highlightSearchResult(modelNameLink, modelNameText, "(?i)Model\\s#?" + modelName,
                    resultTypeModel && matchedDecodedKeyword(decodedKeyword, "Model", modelName)));
            setModelLinkRouterTarget(modelNameLink);
        }
        if (resultTypeExperiment) {
            RouterLink experimentNameLink = new RouterLink();
            nameRow.add(highlightSearchResult(experimentNameLink, experimentNameText, "(?i)Experiment\\s#?" + experimentName,
                    resultTypeExperiment && matchedDecodedKeyword(decodedKeyword, "Experiment", experimentName)));
            setExperimentLinkRouterTarget(experimentNameLink);
        }
        nameRow.addClassName("name-row");
        return nameRow;
    }

    private void setProjectLinkRouterTarget(RouterLink link) {
        String searchResultTypeName = searchResult.getItemType().getName();
        Long projectId = (long) 0;
        switch (searchResultTypeName) {
            case "Project":
                projectId = searchResult.getItemId();
                break;
            case "Model":
                Model model = modelService.getModel(searchResult.getItemId())
                        .orElseThrow(() -> new InvalidDataException("Attempted to get invalid model: " + searchResult.getItemId()));
                projectId = model.getProjectId();
                break;
            case "Experiment":
                projectId = experimentDAO.getExperimentWithRuns(searchResult.getItemId()).get().getProject().getId();
                break;
        }
        link.setRoute(ProjectView.class, "" + projectId);
    }

    private void setModelLinkRouterTarget(RouterLink link) {
        String searchResultTypeName = searchResult.getItemType().getName();
        Long projectId = (long) 0;
        Long modelId = (long) 0;

        switch (searchResultTypeName) {
            case "Model":
                Model model = modelService.getModel(searchResult.getItemId())
                        .orElseThrow(() -> new InvalidDataException("Attempted to get invalid model: " + searchResult.getItemId()));
                projectId = model.getProjectId();
                modelId = searchResult.getItemId();
                break;
            case "Experiment":
                Experiment experiment = experimentDAO.getExperimentWithRuns(searchResult.getItemId()).get();
                projectId = experiment.getProject().getId();
                modelId = experiment.getModelId();
                break;
        }
        link.setRoute(ProjectView.class, PathmindUtils.getProjectModelParameter(projectId, modelId));
    }

    private void setExperimentLinkRouterTarget(RouterLink link) {
        Long experimentId = searchResult.getItemId();
        Experiment experiment = experimentDAO.getExperimentWithRuns(experimentId).get();
        if (experiment.isDraft()) {
            link.setRoute(NewExperimentView.class, ""+experimentId);
        } else {
            link.setRoute(ExperimentView.class, ""+experimentId);
        }
    }

    private boolean matchedDecodedKeyword(String keyword, String itemTypePrefix, String name) {
        return keyword.equals(itemTypePrefix + " " + name) || keyword.equals(itemTypePrefix.toLowerCase() + " " + name) ||
                keyword.equals(itemTypePrefix + "# " + name) || keyword.equals(itemTypePrefix.toLowerCase() + "# " + name) ||
                keyword.equals(name) || keyword.equals(name.toLowerCase());
    }

    private Div createSearchResultsNotesComponent() {
        String notes = searchResult.getNotes();
        if (notes.isEmpty()) {
            return new Div(new Span("—"));
        } else {
            Div notesColumn = (Div) highlightSearchResult(new Div(), notes, null, true);
            notesColumn.addClassName("grid-notes-column");
            return notesColumn;
        }
    }

    private <ROW extends Component & HasComponents & HasStyle> ROW highlightSearchResult(ROW searchResultColumn, String columnText, String toMatch, boolean isHighlightable) {
        String escapedKeyword = PathmindStringUtils.escapeNonAlphanumericalCharacters(decodedKeyword);
        String[] parts = columnText.split("(?i)((?<=" + escapedKeyword + ")|(?=(?i)" + escapedKeyword + "))");

        if (!isHighlightable) {
            searchResultColumn.add(new Span(columnText));
            return searchResultColumn;
        }

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].toLowerCase().equals(decodedKeyword.toLowerCase())) {
                searchResultColumn.add(
                        LabelFactory.createLabel(parts[i], CssPathmindStyles.HIGHLIGHT_LABEL)
                );
            } else if (toMatch != null && escapedKeyword.matches(toMatch)) {
                searchResultColumn.add(
                        LabelFactory.createLabel(columnText, CssPathmindStyles.HIGHLIGHT_LABEL)
                );
            } else {
                searchResultColumn.add(parts[i]);
            }
        }
        searchResultColumn.addClassName("highlighted-text-wrapper");
        return searchResultColumn;
    }

    @Override
    protected void onAttach(AttachEvent evt) {
        VaadinDateAndTimeUtils.withUserTimeZoneId(evt.getUI(), timeZoneId -> {
            createdDateComponent.setText("Created: " + DateAndTimeUtils.formatDateAndTimeShortFormatter(searchResult.getCreateDate(), timeZoneId));
            lastActivityDateComponent.setText("Last Activity: " + DateAndTimeUtils.formatDateAndTimeShortFormatter(searchResult.getUpdateDate(), timeZoneId));
        });
    }
}