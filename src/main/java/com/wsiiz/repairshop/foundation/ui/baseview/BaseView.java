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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public abstract class BaseView<E extends BaseEntity> extends Div implements BeforeEnterObserver, I18nAware {

  final int DURATION_OK = 2000;
  final int DURATION_ERR = 5000;

  final AbstractFactory<E> factory;
  final AbstractService<E> service;

  protected Grid<E> grid;

  protected BeanValidationBinder<E> binder;

  final String editPath;

  final Button cancel = new Button(i18n(BaseView.class, "cancel"));
  final Button save = new Button(i18n(BaseView.class, "save"));
  final Button remove = new Button(i18n(BaseView.class, "remove"));

  final Class<E> entityClass;
  protected E entity;

  public BaseView(Class<E> entityClass, AbstractFactory<E> factory, AbstractService<E> service, String editPath) {
    this.entityClass = entityClass;
    this.factory = factory;
    this.service = service;
    this.editPath = editPath;
  }

  protected void createLayout() {
    addClassNames("master-detail-view");

    this.grid = new Grid<>(entityClass, false);

    this.binder = new BeanValidationBinder<>(entityClass);

    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    binder.bindInstanceFields(this);

    add(splitLayout);

    configureGrid();

    grid.setItems(query -> service.list(
            PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
        .stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // when a row is selected or deselected, populate form
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(editPath, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(getClass());
      }
    });

    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (entity == null) {
          this.entity = factory.create();
          binder.writeBean(entity);
          service.add(entity);
        } else {
          binder.writeBean(entity);
          service.change(entity);
        }
        clearForm();
        refreshGrid();
        Notification.show(i18n(BaseView.class, "writeSuccess"), DURATION_OK, Position.MIDDLE);
        UI.getCurrent().navigate(getClass());
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification.show(i18n(BaseView.class, "simultaneousWrite"), DURATION_ERR, Position.MIDDLE)
            .addThemeVariants(NotificationVariant.LUMO_ERROR);
      } catch (ValidationException validationException) {
        Notification.show(i18n(BaseView.class, "validationError"), DURATION_ERR, Position.MIDDLE);
      } catch (Exception ex) {
        if (ex.getCause() != null && ex.getCause().getCause() instanceof ConstraintViolationException) {
          val messages = ((ConstraintViolationException) ex.getCause().getCause()).getConstraintViolations().stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.joining(", "));
          Notification.show(messages, DURATION_ERR, Position.MIDDLE);
        } else {
          Notification.show(i18n(BaseView.class, "writeError"), DURATION_ERR, Position.MIDDLE)
              .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
      }
    });

    remove.addClickListener(e -> {
      try {
        if (entity == null) {
          Notification.show(i18n(BaseView.class, "notSelected"), DURATION_OK, Position.MIDDLE);
          return;
        }
        service.delete(entity.getId());
        clearForm();
        refreshGrid();
        Notification.show(i18n(BaseView.class, "removeSuccess"), DURATION_OK, Position.MIDDLE);
        UI.getCurrent().navigate(getClass());
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification.show(i18n(BaseView.class, "simultaneousWrite"), DURATION_ERR, Position.MIDDLE)
            .addThemeVariants(NotificationVariant.LUMO_ERROR);
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
        Notification.show(i18n(BaseView.class, "notFound") + ", ID=" + id.get(), 3000,
            Position.MIDDLE);
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
    remove.addThemeVariants(ButtonVariant.LUMO_ERROR);
    buttonLayout.add(save, cancel, remove);
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
