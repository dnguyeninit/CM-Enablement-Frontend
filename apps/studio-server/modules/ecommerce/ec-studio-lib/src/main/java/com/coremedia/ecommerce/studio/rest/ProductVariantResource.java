package com.coremedia.ecommerce.studio.rest;

import com.coremedia.blueprint.base.livecontext.ecommerce.common.CatalogAliasTranslationService;
import com.coremedia.blueprint.base.livecontext.ecommerce.id.CommerceIdFormatterHelper;
import com.coremedia.ecommerce.studio.rest.model.Store;
import com.coremedia.livecontext.ecommerce.catalog.ProductVariant;
import com.coremedia.livecontext.ecommerce.common.CommerceConnection;
import com.coremedia.livecontext.ecommerce.common.CommerceId;
import com.coremedia.livecontext.ecommerce.common.StoreContext;
import com.coremedia.rest.cap.content.ContentRepositoryResource;
import com.coremedia.xml.Markup;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.Locale;
import java.util.Map;

import static com.coremedia.blueprint.base.livecontext.util.CommerceBeanUtils.getCatalog;

/**
 * A catalog {@link ProductVariant} object as a RESTful resource.
 */
@RestController
@RequestMapping(value = ProductVariantResource.URI_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductVariantResource extends CommerceBeanResource<ProductVariant> {

  static final String PATH_TYPE = "sku";
  static final String URI_PATH = "livecontext/" + PATH_TYPE + "/{" + PATH_SITE_ID + "}/{" + PATH_CATALOG_ALIAS + "}/{id:.+}";

  @Autowired
  private ContentRepositoryResource contentRepositoryResource;

  @Autowired
  public ProductVariantResource(CatalogAliasTranslationService catalogAliasTranslationService) {
    super(catalogAliasTranslationService);
  }

  @Override
  protected ProductVariantRepresentation getRepresentation(@NonNull Map<String, String> params) {
    ProductVariantRepresentation productVariantRepresentation = new ProductVariantRepresentation();
    fillRepresentation(params, productVariantRepresentation);
    return productVariantRepresentation;
  }

  private void fillRepresentation(@NonNull Map<String, String> params, ProductVariantRepresentation representation) {
    ProductVariant entity = getEntity(params);
    if (entity == null) {
      String errorMessage = String.format("Could not load product variant with id '%s'.", params.get(PATH_ID));
      throw new CatalogBeanNotFoundRestException(errorMessage);
    }
    super.fillRepresentation(params, entity, representation);

    representation.setId(CommerceIdFormatterHelper.format(entity.getId()));
    representation.setName(entity.getName());
    representation.setExternalId(entity.getExternalId());
    representation.setExternalTechId(entity.getExternalTechId());
    Markup shortDescription = entity.getShortDescription();
    if (shortDescription != null) {
      representation.setShortDescription(shortDescription.asXml());
    }
    Markup longDescription = entity.getLongDescription();
    if (longDescription != null) {
      representation.setLongDescription(longDescription.asXml());
    }
    String thumbnailUrl = entity.getThumbnailUrl();
    representation.setThumbnailUrl(RepresentationHelper.modifyAssetImageUrl(thumbnailUrl, contentRepositoryResource.getContentRepository()));
    representation.setParent(entity.getParent());
    representation.setCategory(entity.getCategory());
    representation.setStore(new Store(entity.getContext()));
    getCatalog(entity).ifPresent(representation::setCatalog);
    representation.setOfferPrice(entity.getOfferPrice());
    representation.setListPrice(entity.getListPrice());

    Currency currency = entity.getCurrency();
    Locale locale = entity.getLocale();
    if (currency != null && locale != null) {
      representation.setCurrency(currency.getSymbol(locale));
    }

    representation.setPictures(entity.getPictures());
    representation.setDownloads(entity.getDownloads());
    representation.setDefiningAttributes(entity.getDefiningAttributes());
    representation.setDescribingAttributes(entity.getDescribingAttributes());
  }

  @Override
  protected ProductVariant doGetEntity(@NonNull Map<String, String> params) {
    StoreContext storeContext = getStoreContext(params)
            .orElseThrow(() -> new IllegalArgumentException("No store context available."));
    CommerceConnection connection = storeContext.getConnection();

    CommerceId commerceId = connection.getIdProvider()
            .formatProductVariantId(storeContext.getCatalogAlias(), params.get(PATH_ID));

    return connection.getCatalogService()
            .findProductVariantById(commerceId, storeContext);
  }
}
