package com.coremedia.lc.studio.lib.interceptor;

import com.coremedia.blueprint.base.livecontext.ecommerce.common.CommerceConnectionSupplier;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.content.ContentType;
import com.coremedia.cap.multisite.Site;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.ecommerce.studio.rest.CategoryAugmentationHelper;
import com.coremedia.livecontext.ecommerce.augmentation.AugmentationService;
import com.coremedia.livecontext.ecommerce.catalog.Category;
import com.coremedia.livecontext.ecommerce.common.CommerceBean;
import com.coremedia.livecontext.ecommerce.common.CommerceConnection;
import com.coremedia.livecontext.ecommerce.common.CommerceId;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * {@link com.coremedia.rest.cap.intercept.ContentWriteInterceptor}
 */
public class ChangeCategoryWriteInterceptor extends ChangeCommerceReferenceInterceptor {

  public ChangeCategoryWriteInterceptor(ContentType contentType,
                                        CommerceConnectionSupplier commerceConnectionSupplier,
                                        AugmentationService augmentationService,
                                        SitesService sitesService,
                                        ContentRepository contentRepository) {
    super(contentType, commerceConnectionSupplier, augmentationService, sitesService, contentRepository);
  }

  @Nullable
  protected CommerceBean getCommerceBeanById(@NonNull CommerceConnection commerceConnection,
                                           @NonNull CommerceId commerceId,
                                           @NonNull Issues issues) {
    var storeContext = commerceConnection.getInitialStoreContext();

    var category = commerceConnection.getCatalogService().findCategoryById(commerceId, storeContext);
    if (category == null) {
      issues.addIssue(Severity.ERROR, EXTERNAL_ID, "Commerce category could not be found.");
      return null;
    }

    return category;
  }


  @Override
  @NonNull
  protected String computeNewAugmentedCommerceBeanName(@NonNull CommerceBean bean) {
    return CategoryAugmentationHelper.computeDocumentName((Category) bean);
  }

  @NonNull
  @Override
  protected String computeNewAugmentedDestinationPath(@NonNull CommerceBean commerceBean, @NonNull Site site) {
    return CategoryAugmentationHelper.computeFolderPath(commerceBean, site, CategoryAugmentationHelper.DEFAULT_BASE_FOLDER_NAME);
  }
}
