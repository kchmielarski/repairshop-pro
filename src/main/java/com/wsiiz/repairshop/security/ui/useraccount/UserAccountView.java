package com.wsiiz.repairshop.security.ui.useraccount;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wsiiz.repairshop.application.ui.MainLayout;
import com.wsiiz.repairshop.foundation.ui.baseview.BaseView;
import com.wsiiz.repairshop.security.domain.useraccount.UserAccount;
import com.wsiiz.repairshop.security.domain.useraccount.UserAccountFactory;
import com.wsiiz.repairshop.security.domain.useraccount.UserAccountService;
import com.wsiiz.repairshop.security.domain.useraccount.UserType;

@PageTitle("Użytkownicy")
@Route(value = "gui/security/user-accounts/:id?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class UserAccountView extends BaseView<UserAccount> {

  TextField userName;
  TextField login;
  ComboBox<UserType> type;
  DatePicker validFrom;
  DatePicker validTo;
  DateTimePicker registrationTime;
  DateTimePicker recentLoginTime;
  Checkbox active;

  public UserAccountView(UserAccountFactory factory, UserAccountService service) {
    super(UserAccount.class, factory, service, "gui/security/user-accounts/%s/edit");
    createLayout();
  }


  @Override
  protected void configureGrid() {
    grid.addColumn("userName").setAutoWidth(true).setHeader(i18n("userName"));
    grid.addColumn("login").setAutoWidth(true).setHeader(i18n("login"));
    grid.addColumn(t -> t.getType() == null ? "" : i18n(UserType.class, t.getType().name())).setAutoWidth(true)
        .setHeader(i18n("type"));
    grid.addColumn("recentLoginTime").setAutoWidth(true).setHeader(i18n("recentLoginTime"));
    grid.addColumn(createBooleanRenderer(UserAccount::isActive)).setAutoWidth(true).setHeader(i18n("active"));
  }

  @Override
  protected FormLayout createEditorForm(Div editorDiv) {

    FormLayout formLayout = new FormLayout();

    userName = new TextField(i18n("userName"));
    login = new TextField(i18n("login"));
    type = new ComboBox<>(i18n("type"));
    validFrom = new DatePicker(i18n("validFrom"));
    validTo = new DatePicker(i18n("validTo"));
    registrationTime = new DateTimePicker(i18n("registrationTime"));
    recentLoginTime = new DateTimePicker(i18n("recentLoginTime"));
    active = new Checkbox(i18n("active"));

    type.setItems(UserType.values());
    type.setItemLabelGenerator(t -> i18n(UserType.class, t.name()));

    registrationTime.setReadOnly(true);
    recentLoginTime.setReadOnly(true);

    formLayout.add(userName, login, type, validFrom, validTo, registrationTime, recentLoginTime, active);

    return formLayout;
  }
}
