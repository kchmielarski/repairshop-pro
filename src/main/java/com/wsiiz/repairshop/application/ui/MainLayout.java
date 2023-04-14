package com.wsiiz.repairshop.application.ui;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.wsiiz.repairshop.customerfile.ui.customer.CustomerView;
import com.wsiiz.repairshop.foundation.ui.appnav.AppNav;
import com.wsiiz.repairshop.foundation.ui.appnav.AppNavItem;
import com.wsiiz.repairshop.foundation.ui.i18n.I18nAware;
import com.wsiiz.repairshop.security.ui.useraccount.UserAccountView;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements I18nAware {

  private H2 viewTitle;

  public MainLayout() {
    setPrimarySection(Section.DRAWER);
    addDrawerContent();
    addHeaderContent();
  }

  private void addHeaderContent() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.getElement().setAttribute("aria-label", "Menu toggle");

    viewTitle = new H2();
    viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

    addToNavbar(true, toggle, viewTitle);
  }

  private void addDrawerContent() {
    H1 appName = new H1(i18n("repairshop"));
    appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
    Header header = new Header(appName);

    Scroller scroller = new Scroller(createNavigation());

    addToDrawer(header, scroller, createFooter());
  }

  private AppNav createNavigation() {
    // AppNav is not yet an official component.
    // For documentation, visit https://github.com/vaadin/vcf-nav#readme
    AppNav nav = new AppNav();

    nav.addItem(new AppNavItem(i18n("customers"), CustomerView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
    nav.addItem(new AppNavItem(i18n("userAccounts"), UserAccountView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
    nav.addItem(new AppNavItem(i18n("about"), AboutView.class, LineAwesomeIcon.FILE.create()));

    return nav;
  }

  private Footer createFooter() {
    Footer layout = new Footer();

    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
    return title == null ? "" : title.value();
  }
}
