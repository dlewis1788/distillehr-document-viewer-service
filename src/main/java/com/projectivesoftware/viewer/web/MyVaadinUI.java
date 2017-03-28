package com.projectivesoftware.viewer.web;

import com.projectivesoftware.viewer.domain.Document;
import com.projectivesoftware.viewer.domain.DocumentFilter;
import com.projectivesoftware.viewer.domain.DocumentSort;
import com.projectivesoftware.viewer.service.DocumentStorageService;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.SortOrder;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
public class MyVaadinUI extends UI {

    @Autowired
    private DocumentStorageService documentStorageService;

    public MyVaadinUI() {

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Grid<Document> documentGrid = new Grid<>();
        documentGrid.addColumn(Document::getPatientId).setCaption("MRN").setSortable(false);
        documentGrid.addColumn(Document::getEncounterId).setCaption("Encounter ID").setSortProperty("encounterId");
        documentGrid.addColumn(Document::getServiceDate).setCaption("Service Date").setSortProperty("serviceDate");
        documentGrid.addColumn(Document::getDocumentType).setCaption("Type").setSortable(false);
        documentGrid.addColumn(Document::getDocumentTitle).setCaption("Title").setSortable(false);
        documentGrid.addColumn(Document::getDocumentSubtitle).setCaption("Subtitle").setSortable(false);
        documentGrid.addColumn(document -> "PDF", new ButtonRenderer<>(clickEvent -> {
            System.out.println("PDF button called for documentId " + clickEvent.getItem().toString());
            Window window = new Window();
            window.setWidth("90%");
            window.setHeight("90%");
            BrowserFrame e = new BrowserFrame("PDF File", new StreamResource(documentStorageService.getDocumentContent(clickEvent.getItem().getDocumentId()), "foo.pdf"));
            e.setWidth("100%");
            e.setHeight("100%");
            window.setContent(e);
            window.center();
            window.setModal(true);
            UI.getCurrent().addWindow(window);
        }));
        documentGrid.setSizeFull();

        DataProvider<Document, DocumentFilter> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {
                    List<DocumentSort> documentSortList = new ArrayList<>();
                    for (SortOrder<String> queryOrder : query.getSortOrders()) {
                        DocumentSort documentSort = new DocumentSort(queryOrder.getSorted(), queryOrder.getDirection() == SortDirection.DESCENDING);
                        documentSortList.add(documentSort);
                    }
                    DocumentFilter documentFilter = query.getFilter().orElse(null);
                    return documentStorageService.getPagedDocumentList(query.getOffset(), query.getLimit(), documentFilter != null ? documentFilter.patientId : null, documentSortList);
                },
                query -> {
                    DocumentFilter documentFilter = query.getFilter().orElse(null);
                    return (int) documentStorageService.getDocumentCount(documentFilter != null ? documentFilter.patientId : null);
                }
        );

        ConfigurableFilterDataProvider<Document, Void, DocumentFilter> wrapper = dataProvider.withConfigurableFilter();

        TextField filterByMrn = new TextField("Filter By MRN");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(filterByMrn);
        verticalLayout.setHeight("100%");
        verticalLayout.setWidth("100%");
        setContent(verticalLayout);
        documentGrid.setDataProvider(wrapper);

        filterByMrn.addValueChangeListener(e -> {
            if (e.getValue().isEmpty()) {
                verticalLayout.removeComponent(documentGrid);
            } else {
                Long patientId = Long.parseLong(e.getValue());
                wrapper.setFilter(new DocumentFilter(patientId));
                verticalLayout.addComponent(documentGrid);
            }
        });
    }
}
