package com.coremedia.blueprint.ecommerce.contentbeans.impl;

import com.coremedia.blueprint.base.ecommerce.catalog.CmsCatalogService;
import com.coremedia.blueprint.base.ecommerce.catalog.CmsCategory;
import com.coremedia.blueprint.base.ecommerce.catalog.CmsProduct;
import com.coremedia.blueprint.ecommerce.common.contentbeans.CMAbstractCategoryImpl;
import com.coremedia.blueprint.ecommerce.contentbeans.CMCategory;
import com.coremedia.blueprint.ecommerce.contentbeans.CMProduct;
import com.coremedia.cae.aspect.Aspect;
import com.coremedia.cap.content.Content;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class CMCategoryImpl extends CMAbstractCategoryImpl implements CMCategory {
  private CmsCatalogService catalogService;


  // --- configuration ----------------------------------------------

  @Required
  public void setCatalogService(CmsCatalogService catalogService) {
    this.catalogService = catalogService;
  }


  // --- Standard Blueprint typing overrides ------------------------

  @Override
  public CMCategory getMaster() {
    return (CMCategory) super.getMaster();
  }

  @Override
  public Map<Locale, ? extends CMCategory> getVariantsByLocale() {
    return getVariantsByLocale(CMCategory.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<? extends CMCategory> getLocalizations() {
    return (Collection<? extends CMCategory>) super.getLocalizations();
  }

  /**
   * @deprecated since 1907.1; Implement optional features as extensions.
   */
  @Deprecated
  @Override
  @SuppressWarnings("unchecked")
  public Map<String, ? extends Aspect<? extends CMCategory>> getAspectByName() {
    return (Map<String, ? extends Aspect<? extends CMCategory>>) super.getAspectByName();
  }

  /**
   * @deprecated since 1907.1; Implement optional features as extensions.
   */
  @Deprecated
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends Aspect<? extends CMCategory>> getAspects() {
    return (List<? extends Aspect<? extends CMCategory>>) super.getAspects();
  }

  @NonNull
  @Override
  public List<CMCategory> getSubcategories() {
    CmsCategory category = getCategory();
    if (category == null) {
      return Collections.emptyList();
    }

    List<Content> result = category.getChildren().stream()
            .filter(CmsCategory.class::isInstance)
            .map(CmsCategory.class::cast)
            .map(CmsCategory::getContent)
            .collect(Collectors.toUnmodifiableList());

    return createBeansFor(result, CMCategory.class);
  }

  @NonNull
  @Override
  public List<CMProduct> getProducts() {
    CmsCategory category = getCategory();
    if (category == null) {
      return Collections.emptyList();
    }

    List<Content> result = category.getProducts().stream()
            .filter(CmsProduct.class::isInstance)
            .map(CmsProduct.class::cast)
            .map(CmsProduct::getContent)
            .collect(Collectors.toUnmodifiableList());

    return createBeansFor(result, CMProduct.class);
  }

  // --- Features ---------------------------------------------------

  private CmsCategory getCategory() {
    return catalogService.findCategoryByContent(getContent());
  }
}
