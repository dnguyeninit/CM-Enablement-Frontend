package com.coremedia.ecommerce.studio.rest;

import com.coremedia.ecommerce.studio.rest.model.ChildRepresentation;
import com.coremedia.ecommerce.studio.rest.model.Marketing;
import com.coremedia.ecommerce.studio.rest.model.Segments;
import com.coremedia.livecontext.ecommerce.catalog.Catalog;
import com.coremedia.livecontext.ecommerce.catalog.Category;
import com.coremedia.livecontext.ecommerce.common.StoreContext;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Store representation for JSON.
 */
public class StoreRepresentation extends AbstractCatalogRepresentation {

  private StoreContext context;

  private String vendorName;
  private boolean multiCatalog;
  private List<Catalog> catalogs;
  private Catalog defaultCatalog;
  private List<Category> rootCategories;

  private boolean marketingEnabled = false;
  private String timeZoneId;

  private Category rootCategory;

  public void setContext(StoreContext context) {
    this.context = context;
  }

  public boolean isMultiCatalog() {
    return multiCatalog;
  }

  public void setMultiCatalog(boolean multiCatalog) {
    this.multiCatalog = multiCatalog;
  }

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public String getName() {
    return context != null ? context.getStoreName() : null;
  }

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public String getStoreId() {
    return context.getStoreId();
  }

  // The entries correspond to those of #getChildrenData()
  public List<Object> getTopLevel() {
    List<Object> topLevel = new ArrayList<>();

    if (isMarketingEnabled()) {
      topLevel.add(getMarketing());
    }

    List<Catalog> catalogs = getCatalogs();
    if (!catalogs.isEmpty()) {
      List<Category> catalogRootCategories = catalogs.stream()
              .map(Catalog::getRootCategory)
              .collect(toList());

      topLevel.addAll(catalogRootCategories);
    } else {
      topLevel.add(rootCategory);
    }

    return topLevel;
  }

  // The entries correspond to those of #getTopLevel()
  public List<ChildRepresentation> getChildrenData() {
    List<ChildRepresentation> result = new ArrayList<>();

    if (isMarketingEnabled()) {
      result.add(new ChildRepresentation("store-marketing", getMarketing()));
    }

    List<Catalog> catalogs = getCatalogs();
    if (!catalogs.isEmpty()) {
      for (Catalog catalog : catalogs) {
        String catalogName = catalog.getName().value();
        //let the root category of the catalog represent it as the root category can be augmented etc.
        result.add(new ChildRepresentation(catalogName, catalog.getRootCategory()));
      }
    } else {
      result.add(new ChildRepresentation("root-category", rootCategory));
    }

    return result;
  }

  public Marketing getMarketing() {
    return new Marketing(context);
  }

  public Segments getSegments() {
    return new Segments(context);
  }

  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  public boolean isMarketingEnabled() {
    return marketingEnabled;
  }

  public void setMarketingEnabled(boolean marketingEnabled) {
    this.marketingEnabled = marketingEnabled;
  }

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public String getTimeZoneId() {
    return timeZoneId;
  }

  public void setTimeZoneId(String timeZoneId) {
    this.timeZoneId = timeZoneId;
  }

  public void setRootCategory(Category rootCategory) {
    this.rootCategory = rootCategory;
  }

  public Category getRootCategory() {
    return rootCategory;
  }

  @NonNull
  public List<Catalog> getCatalogs() {
    return (catalogs != null) ? catalogs : emptyList();
  }

  public void setCatalogs(List<Catalog> catalogs) {
    this.catalogs = catalogs;
  }

  public Catalog getDefaultCatalog() {
    return defaultCatalog;
  }

  public void setDefaultCatalog(Catalog defaultCatalog) {
    this.defaultCatalog = defaultCatalog;
  }

  public List<Category> getRootCategories() {
    return rootCategories;
  }

  public void setRootCategories(List<Category> rootCategories) {
    this.rootCategories = rootCategories;
  }
}
