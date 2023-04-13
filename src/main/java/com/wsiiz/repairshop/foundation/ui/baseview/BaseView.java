package com.wsiiz.repairshop.foundation.ui.baseview;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.wsiiz.repairshop.foundation.domain.AbstractFactory;
import com.wsiiz.repairshop.foundation.domain.AbstractService;
import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import com.wsiiz.repairshop.foundation.ui.i18n.I18nAware;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public abstract class BaseView<E extends BaseEntity> extends Div implements BeforeEnterObserver, I18nAware {

  final String routeTemplate;

  final AbstractFactory<E> factory;
  final AbstractService<E> service;

  protected Grid<E> grid;
  BeanValidationBinder<E> binder;

  final Button cancel = new Button(i18n(BaseView.class, "cancel"));
  final Button save = new Button(i18n(BaseView.class, "save"));

  final Class<E> entityClass;
  protected E entity;

  public BaseView(String routeTemplate, Class<E> entityClass, AbstractFactory<E> factory, AbstractService<E> service) {
    this.routeTemplate = routeTemplate;
    this.entityClass = entityClass;
    this.factory = factory;
    this.service = service;
  }

  protected void createLayout() {
    addClassNames("master-detail-view");

    this.grid = new Grid<>(entityClass, false);

    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    configureGrid();

    grid.setItems(query -> service.list(
            PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
        .stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // when a row is selected or deselected, populate form
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(routeTemplate, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(getClass());
      }
    });

    // Configure Form
    binder = new BeanValidationBinder<>(entityClass);

    // Bind fields. This is where you'd define e.g. validation rules
    binder.bindInstanceFields(this);

    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (entity == null) {
          this.entity = factory.create();
        }
        binder.writeBean(entity);
        service.update(entity);
        clearForm();
        refreshGrid();
        Notification.show("Data updated");
        UI.getCurrent().navigate(getClass());
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification n = Notification.show(
            "Error updating the data. Somebody else has updated the record while you were making changes.");
        n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
      } catch (ValidationException validationException) {
        Notification.show("Failed to update the data. Check again that all values are valid");
      }
    });
  }

  protected abstract void configureGrid();

  protected abstract FormLayout createEditorForm(Div editorDiv);

  protected void createEditorLayout(SplitLayout splitLayout) {
    Div editorLayoutDiv = new Div();
    editorLayoutDiv.setClassName("editor-layout");
    Div editorDiv = new Div();
    editorDiv.setClassName("editor");
    editorLayoutDiv.add(editorDiv);
    editorDiv.add(createEditorForm(editorDiv));
    createButtonLayout(editorLayoutDiv);
    splitLayout.addToSecondary(editorLayoutDiv);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<Long> id = event.getRouteParameters().get("id").map(Long::parseLong);
    if (id.isPresent()) {
      Optional<E> entityFromBackend = service.get(id.get());
      if (entityFromBackend.isPresent()) {
        populateForm(entityFromBackend.get());
      } else {
        Notification.show(
            String.format("The requested entity was not found, ID = %s", id.get()), 3000,
            Notification.Position.BOTTOM_START);
        // when a row is selected but the data is no longer available,
        // refresh grid
        refreshGrid();
        event.forwardTo(getClass());
      }
    }
  }

  protected void createButtonLayout(Div editorLayoutDiv) {
    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setClassName("button-layout");
    cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonLayout.add(save, cancel);
    editorLayoutDiv.add(buttonLayout);
  }

  private void createGridLayout(SplitLayout splitLayout) {
    Div wrapper = new Div();
    wrapper.setClassName("grid-wrapper");
    splitLayout.addToPrimary(wrapper);
    wrapper.add(grid);
  }

  private void refreshGrid() {
    grid.select(null);
    grid.getDataProvider().refreshAll();
  }

  private void clearForm() {
    populateForm(null);
  }

  private void populateForm(E value) {
    this.entity = value;
    binder.readBean(entity);
  }

  protected LitRenderer<E> createBooleanRenderer(Predicate<E> predicate) {
    return LitRenderer.<E>of(
            "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
        .withProperty("icon", entity -> predicate.test(entity) ? "check" : "minus").withProperty("color",
            entity -> predicate.test(entity)
                ? "var(--lumo-primary-text-color)"
                : "var(--lumo-disabled-text-color)");
  }
}
