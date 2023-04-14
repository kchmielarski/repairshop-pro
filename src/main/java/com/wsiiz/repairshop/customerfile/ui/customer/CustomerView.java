package com.wsiiz.repairshop.customerfile.ui.customer;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wsiiz.repairshop.application.ui.MainLayout;
import com.wsiiz.repairshop.customerfile.domain.customer.Customer;
import com.wsiiz.repairshop.customerfile.domain.customer.CustomerFactory;
import com.wsiiz.repairshop.customerfile.domain.customer.CustomerRole;
import com.wsiiz.repairshop.customerfile.domain.customer.CustomerService;
import com.wsiiz.repairshop.foundation.ui.baseview.BaseView;

@PageTitle("Klienci")
@Route(value = "customers/:id?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class CustomerView extends BaseView<Customer> {

  TextField firstName;
  TextField lastName;
  TextField email;
  TextField phone;
  DatePicker dateOfBirth;
  TextField occupation;
  ComboBox<CustomerRole> role;
  Checkbox important;

  public CustomerView(CustomerFactory factory, CustomerService service) {
    super(Customer.class, factory, service, "customers/%s/edit");
    createLayout();
  }

  @Override
  protected void configureGrid() {
    grid.addColumn("firstName").setAutoWidth(true).setHeader(i18n("firstName"));
    grid.addColumn("lastName").setAutoWidth(true).setHeader(i18n("lastName"));
    grid.addColumn("email").setAutoWidth(true).setHeader(i18n("email"));
    grid.addColumn("phone").setAutoWidth(true).setHeader(i18n("phone"));
    grid.addColumn("dateOfBirth").setAutoWidth(true).setHeader(i18n("dateOfBirth"));
    grid.addColumn("occupation").setAutoWidth(true).setHeader(i18n("occupation"));
    grid.addColumn(t -> t.getRole() == null ? "" : i18n(CustomerRole.class, t.getRole().name())).setAutoWidth(true)
        .setHeader(i18n("role"));
    grid.addColumn(createBooleanRenderer(Customer::isImportant)).setAutoWidth(true).setHeader(i18n("important"));
  }

  @Override
  protected FormLayout createEditorForm(Div editorDiv) {

    FormLayout formLayout = new FormLayout();

    firstName = new TextField(i18n("firstName"));
    lastName = new TextField(i18n("lastName"));
    email = new TextField(i18n("email"));
    phone = new TextField(i18n("phone"));
    dateOfBirth = new DatePicker(i18n("dateOfBirth"));
    occupation = new TextField(i18n("occupation"));
    role = new ComboBox<>(i18n("role"));
    important = new Checkbox(i18n("important"));

    role.setItems(CustomerRole.values());
    role.setItemLabelGenerator(r -> i18n(CustomerRole.class, r.name()));

    formLayout.add(firstName, lastName, email, phone, dateOfBirth, occupation, role, important);

    return formLayout;
  }
}
