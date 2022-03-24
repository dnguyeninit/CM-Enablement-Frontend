package com.coremedia.blueprint.cae.common.predicates;

import com.coremedia.blueprint.common.contentbeans.CMLinkable;
import com.coremedia.blueprint.common.datevalidation.ValidationPeriodPredicate;
import com.coremedia.cap.content.Content;
import com.coremedia.objectserver.beans.ContentBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.function.Predicate;

/**
 * This predicate checks if the given object is a content bean and if this content bean is valid.
 * That means that it is a CMLinkable and the validFrom and validTo properties are not before and after now
 * respectively.
 */
public class ValidContentPredicate implements Predicate<Content> {

  private final ContentBeanFactory contentBeanFactory;

  private static final Logger LOG = LoggerFactory.getLogger(ValidContentPredicate.class);

  public ValidContentPredicate(ContentBeanFactory contentBeanFactory) {
    this.contentBeanFactory = contentBeanFactory;
  }

  @Override
  public boolean test(Content content) {
    if (content == null || content.isDestroyed() || !content.getType().isSubtypeOf("CMLinkable")) {
      return false;
    }

    LOG.debug("Found content of type CMLinkable. Verify that content is valid.");
    CMLinkable linkableBean = contentBeanFactory.createBeanFor(content, CMLinkable.class);

    boolean result = linkableBean != null && new ValidationPeriodPredicate(Calendar.getInstance()).test(linkableBean);
    if (!result) {
      LOG.debug("Content '{}' is currently not valid. Skipping.", content.getId());
    }
    return result;
  }

}
