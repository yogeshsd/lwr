package com.lwr.software.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.DashboardConstants.Role;

public class MenuFactory {
	
	private static volatile MenuFactory _factory;
	
	private List<MenuItem> menus = new ArrayList<MenuItem>();
	
	private MenuFactory(){
		initialize();
	}

	public static MenuFactory getInstance(){
		if(_factory == null){
			
			synchronized (MenuFactory.class) {
				if(_factory == null){
					_factory = new MenuFactory();
				}
			}
		}
		return _factory;
	}
	
	private void initialize(){
		MenuItem homeMenu = new MenuItem("Home","/lwr/home");
		homeMenu.addRole(Role.ALL);
		homeMenu.addPage(DashboardConstants.ALL_PAGES);
		
		MenuItem adminMenu = new MenuItem("Administration","#");
		adminMenu.addRole(Role.ADMIN);
		adminMenu.addPage(DashboardConstants.ALL_PAGES);
		
		MenuItem userMenu = new MenuItem("User Management","/lwr/usermgmt");
		userMenu.addRole(Role.ADMIN);
		userMenu.addPage(DashboardConstants.ALL_PAGES);
		adminMenu.addSubMenuItem(userMenu);
		
		MenuItem connMenu = new MenuItem("Connection Management","/lwr/connmgmt");
		connMenu.addRole(Role.ADMIN);
		connMenu.addPage(DashboardConstants.ALL_PAGES);
		adminMenu.addSubMenuItem(connMenu);
		
		MenuItem scheduleMenu = new MenuItem("Schedule Management","/lwr/schedmgmt");
		scheduleMenu.addRole(Role.ADMIN);
		scheduleMenu.addPage(DashboardConstants.ALL_PAGES);
		adminMenu.addSubMenuItem(scheduleMenu);


		MenuItem helpMenu = new MenuItem("Help","#");
		helpMenu.addRole(Role.ALL);
		helpMenu.addPage(DashboardConstants.ALL_PAGES);
		
		MenuItem helpStart = new MenuItem("Getting Started","/lwr/getstarted");
		helpStart.addRole(Role.ALL);
		helpStart.addPage(DashboardConstants.ALL_PAGES);

		MenuItem helpAbout = new MenuItem("About","/lwr/about");
		helpAbout.addRole(Role.ALL);
		helpAbout.addPage(DashboardConstants.ALL_PAGES);
		
		helpMenu.addSubMenuItem(helpStart);
		helpMenu.addSubMenuItem(helpAbout);

		MenuItem fileMenu = new MenuItem("File","#");
		fileMenu.addRole(Role.ADMIN);
		fileMenu.addRole(Role.VIEW);
		fileMenu.addPage(DashboardConstants.ALL_PAGES);
		
		MenuItem newMenu = new MenuItem("New Report","/lwr/createedit");
		newMenu.addRole(Role.ADMIN);
		newMenu.addPage("/lwr/home");
		newMenu.addPage("/lwr/report");
		newMenu.addPage("/lwr/about");
		newMenu.addPage("/lwr/getstarted");
		newMenu.addPage("/lwr/usermgmt");
		newMenu.addPage("/lwr/connmgmt");

		
		MenuItem saveMenu = new MenuItem("Save - Public Folder","javascript:save('public')");
		saveMenu.addRole(Role.ADMIN);
		saveMenu.addPage("/lwr/createedit");
		
		MenuItem saveMenuPrivate = new MenuItem("Save - Private Folder","javascript:save('personal')");
		saveMenuPrivate.addRole(Role.ADMIN);
		saveMenuPrivate.addPage("/lwr/createedit");

		
		MenuItem editMenu = new MenuItem("Edit Report","/lwr/createedit");
		editMenu.setUseReportParam(true);
		editMenu.addRole(Role.ADMIN);
		editMenu.addPage("/lwr/report");
		
		fileMenu.addSubMenuItem(newMenu);
		fileMenu.addSubMenuItem(saveMenu);
		fileMenu.addSubMenuItem(saveMenuPrivate);
		fileMenu.addSubMenuItem(editMenu);

		
		MenuItem exportAsCsvMenu = new MenuItem("Export CSV","/lwr/savereport?type=CSV");
		exportAsCsvMenu.setUseReportParam(true);
		exportAsCsvMenu.addRole(Role.ADMIN);
		exportAsCsvMenu.addRole(Role.VIEW);
		exportAsCsvMenu.addPage("/lwr/report");
		fileMenu.addSubMenuItem(exportAsCsvMenu);

		MenuItem exportAsPdfMenu = new MenuItem("Export PDF","/lwr/savereport?type=PDF");
		exportAsPdfMenu.setUseReportParam(true);
		exportAsPdfMenu.addRole(Role.ADMIN);
		exportAsPdfMenu.addRole(Role.VIEW);
		exportAsPdfMenu.addPage("/lwr/report");
		fileMenu.addSubMenuItem(exportAsPdfMenu);
		
		MenuItem exportAsHtmlMenu = new MenuItem("Export HTML","/lwr/savereport?type=HTML");
		exportAsHtmlMenu.setUseReportParam(true);
		exportAsHtmlMenu.addRole(Role.ADMIN);
		exportAsHtmlMenu.addRole(Role.VIEW);
		exportAsHtmlMenu.addPage("/lwr/report");
		fileMenu.addSubMenuItem(exportAsHtmlMenu);

		
		menus.add(homeMenu);
		menus.add(fileMenu);
		menus.add(adminMenu);
		menus.add(helpMenu);
	}
	
	public List<MenuItem> getMenu(Role role,String pageURI){
		System.out.println(pageURI);
		List<MenuItem> toReturn = new ArrayList<MenuItem>();
		for (MenuItem menuItem : menus) {
			MenuItem clone = menuItem.createClone();
			if(isApplicable(menuItem,role,pageURI))
				toReturn.add(clone);
			List<MenuItem> subMenus = menuItem.getSubMenuItems();
			for (MenuItem subMenu : subMenus) {
				MenuItem subMenuClone = subMenu.createClone();
				if(isApplicable(subMenu, role, pageURI))
					clone.addSubMenuItem(subMenuClone);
			}
		}
		return toReturn;
	}
	
	public boolean isApplicable(MenuItem menuItem,Role role, String pageURI){
		boolean isAuthorized = false;
		boolean isApplicable = false;
		Set<Role> roles = menuItem.getRoles();
		for (Role r : roles) {
			if(r == role || r == Role.ALL){
				isAuthorized = true;
				break;
			}
		}
		
		Set<String> pages = menuItem.getPages();
		for (String page : pages) {
			if(page.equalsIgnoreCase(pageURI) || page.equalsIgnoreCase(DashboardConstants.ALL_PAGES)){
				isApplicable = true;
				break;
			}
		}
		if(isAuthorized && isApplicable)
			return true;
		else
			return false;
	}
}

