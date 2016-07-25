package com.lwr.software.menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lwr.software.reporter.DashboardConstants.Role;

public class MenuItem {

	private String menuLabel;
	
	private String menuAction;
	
	private boolean useReportParam;
	
	private List<MenuItem> subMenuItems = new ArrayList<MenuItem>();
	
	private Set<String> pages = new HashSet<String>();
	
	private Set<Role> roles = new HashSet<Role>();
	
	private Set<String> parameterNames = new HashSet<String>();
	
	public MenuItem(String menuLabel,String menuAction) {
		this.menuLabel=menuLabel;
		this.menuAction=menuAction;
	}
	
	
	public Set<String> getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(Set<String> parameterNames) {
		this.parameterNames = parameterNames;
	}
	
	public void addParameterName(String parameterName){
		this.parameterNames.add(parameterName);
	}

	public boolean isUseReportParam() {
		return useReportParam;
	}

	public void setUseReportParam(boolean useReportParam) {
		this.useReportParam = useReportParam;
	}

	public String getMenuAction() {
		return menuAction;
	}

	public String getMenuLabel() {
		return menuLabel;
	}

	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}

	public String getMenuAction(String param) {
		if(useReportParam){
			if(menuAction.contains("?"))
				return menuAction+"&"+param;
			else
				return menuAction+"?"+param;
		}
		else
			return menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	public List<MenuItem> getSubMenuItems() {
		return subMenuItems;
	}

	public void setSubMenuItems(List<MenuItem> subMenuItems) {
		this.subMenuItems = subMenuItems;
	}
	
	public void addSubMenuItem(MenuItem subMenuItem){
		this.subMenuItems.add(subMenuItem);
	}
	
	public void addPage(String pageName){
		this.pages.add(pageName);
	}
	
	public void addRole(Role roleName){
		this.roles.add(roleName);
	}
	
	public Set<String> getPages() {
		return pages;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	public MenuItem createClone() {
		MenuItem item = new MenuItem(this.menuLabel,this.menuAction);
		item.useReportParam=this.useReportParam;
		item.roles=this.roles;
		item.pages=this.pages;
		return item;
	}
}
